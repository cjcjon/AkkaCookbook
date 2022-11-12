package com.packt.chapter3

import akka.actor.{Actor, ActorSystem, Props}
import akka.routing.SmallestMailboxPool

class SmallestMailboxActor extends Actor {
  override def receive: Receive = {
    case msg: String => println(s"I am ${self.path.name}")
    case _ => println(s"I don't understand the message")
  }
}

object SmallestMailbox extends App {
  val actorSystem = ActorSystem("Hello-Akka")
  val router = actorSystem.actorOf(SmallestMailboxPool(5).props(Props[SmallestMailboxActor]))

  for (i <- 1 to 5) {
    router ! s"Hello $i"
  }
}
