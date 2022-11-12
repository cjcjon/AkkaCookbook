package com.packt.chapter4

import akka.actor.{Actor, ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt
import scala.language.postfixOps

class ComputationActor extends Actor {
  override def receive: Receive = {
    case (a: Int, b: Int) => sender ! (a + b)
  }
}

object FutureWithActor extends App {
  implicit val timeout: Timeout = Timeout(10 seconds)

  val actorSystem = ActorSystem("Hello-Akka")
  val computationActor = actorSystem.actorOf(Props[ComputationActor])

  val future = (computationActor ? (2, 3)).mapTo[Int]
  val sum = Await.result(future, 10 seconds)

  println(s"Future result: $sum")
}
