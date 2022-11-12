package com.packt.chapter3

import akka.actor.{Actor, ActorSystem, Props}
import akka.routing.BalancingPool

class BalancingPoolActor extends Actor {
  override def receive: Receive = {
    case msg: String => println(s"I am ${self.path.name}")
    case _ => println(s"I don't understand the message")
  }
}

object BalancingPoolApp extends App {
  val actorSystem = ActorSystem("Hello-akka")
  val router = actorSystem.actorOf(BalancingPool(5).props(Props[BalancingPoolActor]))

  for (i <- 1 to 5) {
    router ! s"Hello $i"
  }
}
