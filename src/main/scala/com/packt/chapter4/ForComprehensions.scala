package com.packt.chapter4

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.concurrent.duration.DurationInt
import scala.language.postfixOps

object ForComprehensions extends App {
  val futureA = Future(20 + 20)
  val futureB = Future(30 + 30)

  val finalFuture: Future[Int] = for {
    a <- futureA
    b <- futureB
  } yield a + b

  println("Future result is " + Await.result(finalFuture, 1 seconds))
}
