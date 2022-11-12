package com.packt.chapter5

import akka.actor.ActorSystem

import scala.concurrent.duration.DurationInt
import scala.language.postfixOps

object ScheduleOperation extends App {
  val actorSystem = ActorSystem("Hello-akka")

  import actorSystem.dispatcher

  actorSystem.scheduler.scheduleOnce(10 seconds) {
    println(s"Sum of (1 + 2) is ${1 + 2}")
  }
  actorSystem.scheduler.scheduleWithFixedDelay(11 seconds, 2 seconds)(() =>
    println(s"Hello, Sorry for disturbing you every 2 seconds")
  )
}
