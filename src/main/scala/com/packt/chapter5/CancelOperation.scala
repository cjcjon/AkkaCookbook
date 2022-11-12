package com.packt.chapter5

import akka.actor.{Actor, ActorSystem, Cancellable, Props}

import scala.concurrent.duration.DurationInt
import scala.language.postfixOps

class CancelOperation extends Actor {
  var i = 10

  override def receive: Receive = {
    case "tick" =>
      println(s"Hi, Do you know i do the same task again and again")
      i -= 1
      if (i == 0) Scheduler.cancellable.cancel()
  }
}

object Scheduler extends App {
  val actorSystem = ActorSystem("Hello-akka")
  import actorSystem.dispatcher

  val actor = actorSystem.actorOf(Props[CancelOperation])
  val cancellable: Cancellable = actorSystem.scheduler.scheduleWithFixedDelay(0 seconds, 2 seconds, actor, "tick")
}
