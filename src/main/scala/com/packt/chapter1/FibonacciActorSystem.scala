package com.packt.chapter1

import akka.actor.{Actor, ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps

class FibonacciActor extends Actor {
  override def receive: Receive = {
    case num: Int =>
      val fibonacciNumber = fib(num)
      sender ! fibonacciNumber
  }

  def fib(n: Int): Int = n match {
    case 0 | 1 => n
    case _ => fib(n - 1) + fib(n - 2)
  }
}

object FibonacciActorSystem extends App {
  implicit val timeout: Timeout = Timeout(10 seconds)

  val actorSystem = ActorSystem("HelloAkka")
  val actor = actorSystem.actorOf(Props[FibonacciActor])

  // 액터에 결과 요청
  val future = (actor ? 10).mapTo[Int]
  val fibonacciNumber = Await.result(future, 10 seconds)
  println(fibonacciNumber)
}