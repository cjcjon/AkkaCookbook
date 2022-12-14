package com.packt.chapter2

import akka.actor.SupervisorStrategy.{Escalate, Restart}
import akka.actor.{Actor, ActorRef, ActorSystem, OneForOneStrategy, Props, SupervisorStrategy}
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt
import scala.language.postfixOps

case object Error
case class StopActor(actorRef: ActorRef)

class LifeCycleActor extends Actor {
  var sum = 1

  override def preRestart(reason: Throwable, message: Option[Any]): Unit =
    println(s"sum in preRestart is $sum")

  override def preStart(): Unit =
    println(s"sum in preStart is $sum")

  def receive: Receive = {
    case Error => throw new ArithmeticException()
    case _ => println("default msg")
  }

  override def postStop(): Unit =
    println(s"sum in postStop is ${sum * 3}")

  override def postRestart(reason: Throwable): Unit = {
    sum = sum * 2
    println(s"sum in postRestart is $sum")
  }
}

class Supervisor extends Actor {
  override val supervisorStrategy: OneForOneStrategy = OneForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 1 minute) {
    case _: ArithmeticException => Restart
    case t => super.supervisorStrategy.decider.applyOrElse(t, (_: Any) => Escalate)
  }

  def receive: Receive = {
    case (props: Props, name: String) => sender ! context.actorOf(props, name)
    case StopActor(actorRef) => context.stop(actorRef)
  }
}

object ActorLifeCycle extends App {
  implicit val timeout: Timeout = Timeout(2 seconds)

  val actorSystem = ActorSystem("Supervision")
  val supervisor = actorSystem.actorOf(Props[Supervisor], "supervisor")
  val childFuture = supervisor ? (Props(new LifeCycleActor), "LifeCycleActor")

  val child = Await.result(childFuture.mapTo[ActorRef], 2 seconds)
  child ! Error

  Thread.sleep(1000)
  supervisor ! StopActor(child)
}
