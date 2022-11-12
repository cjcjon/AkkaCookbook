package com.packt.chapter5

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}

class LoggingActor extends Actor with ActorLogging {
  override def receive: Receive = {
    case (a: Int, b: Int) => log.info(s"sum of $a and $b is ${a + b}")
    case msg => log.warning(s"I don't know what are you talking about: $msg")
  }
}

object Logging extends App {
  val system = ActorSystem("Hello-Akka")
  val actor = system.actorOf(Props[LoggingActor], "SumActor")
  actor ! (10, 12)
  actor ! "Hello !!"
  system.terminate()
}
