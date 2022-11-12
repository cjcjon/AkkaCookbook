package com.packt.chapter5

import akka.actor.{Actor, ActorSystem, Props}

import scala.concurrent.duration.DurationInt
import scala.language.postfixOps
import scala.util.Random

class RandomIntAdder extends Actor {
  val random: Random.type = scala.util.Random

  override def receive: Receive = {
    case "tick" =>
      val randomIntA = random.nextInt(10)
      val randomIntB = random.nextInt(10)
      println(s"sum of $randomIntA and $randomIntB is ${randomIntA + randomIntB}")
  }
}

object ScheduleActor extends App {
  val actorSystem = ActorSystem("Hello-akka")
  import actorSystem.dispatcher

  val actor = actorSystem.actorOf(Props[RandomIntAdder])
  actorSystem.scheduler.scheduleOnce(10 seconds, actor, "tick")
  actorSystem.scheduler.scheduleWithFixedDelay(11 seconds, 2 seconds, actor, "tick")
}
