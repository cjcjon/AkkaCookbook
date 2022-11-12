package com.packt.chapter4

import akka.util.Timeout

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.concurrent.duration.DurationInt
import scala.language.postfixOps

object ReducingFutures extends App {
  val timeout = Timeout(10 seconds)

  val listOfFutures = (1 to 10).map(Future(_))
  val finalFuture = Future.reduceLeft(listOfFutures)(_ + _)

  println(s"sum of numbers from 1 to 10 is ${Await.result(finalFuture, 10 seconds)}")
}
