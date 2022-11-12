package com.packt.chapter4

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.concurrent.duration.DurationInt
import scala.language.postfixOps

object AddFuture extends App {
  val future = Future(1 + 2).mapTo[Int]
  val sum = Await.result(future, 10 seconds)

  println(s"Future result: $sum")
}
