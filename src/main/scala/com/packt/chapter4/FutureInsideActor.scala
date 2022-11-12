package com.packt.chapter4

import akka.actor.{Actor, ActorSystem, Props}

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.DurationInt
import scala.language.postfixOps

class FutureActor extends Actor {
  // 액터 내의 default execution context를 불러온다
  import context.dispatcher

  override def receive: Receive = {
    case (a: Int, b: Int) =>
      val f = Future(a + b)
      println(s"Future result is ${Await.result(f, 10 seconds)}")
  }
}

object FutureInsideActor extends App {
  val actorSystem = ActorSystem("Hello-Akka")
  val futureActor = actorSystem.actorOf(Props[FutureActor])

  futureActor ! (10, 20)
}
