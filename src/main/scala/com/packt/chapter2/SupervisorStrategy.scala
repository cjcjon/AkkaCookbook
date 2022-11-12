package com.packt.chapter2

import akka.actor.SupervisorStrategy.{Escalate, Restart, Resume, Stop}
import akka.actor.{Actor, ActorRef, ActorSystem, OneForOneStrategy, Props}

import scala.language.postfixOps

class Printer extends Actor {
  override def preRestart(reason: Throwable, messsage: Option[Any]): Unit =
    println("Printer: I am restarting because of ArithmeticException")

  def receive: Receive = {
    case msg: String => println(s"Printer $msg")
    case msg: Int => 1 / 0
  }
}

class IntAdder extends Actor {
  var x = 0

  def receive: Receive = {
    case msg: Int =>
      x = x + msg
      println(s"IntAdder: sum is $x")
    case msg: String =>
      throw new IllegalArgumentException
  }

  override def postStop: Unit = {
    println("IntAdder: I am getting stopped because i got a string message")
  }
}

class SupervisorStrategy extends Actor {
  import scala.concurrent.duration._

  override val supervisorStrategy: OneForOneStrategy = OneForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 1 minute) {
    case _: ArithmeticException => Restart
    case _: NullPointerException => Resume
    case _: IllegalArgumentException => Stop
    case _: Exception => Escalate
  }

  val printer: ActorRef = context.actorOf(Props[Printer])
  val intAdder: ActorRef = context.actorOf(Props[IntAdder])

  def receive: Receive = {
    case "Start" =>
      printer ! "Hello printer"
      printer ! 10
      intAdder ! 10
      intAdder ! 10
      intAdder ! "Hello int adder"
  }
}

object SupervisorStrategyApp extends App {
  val actorSystem = ActorSystem("Supervision")
  actorSystem.actorOf(Props[SupervisorStrategy]) ! "Start"
}
