package com.packt.chapter3

import akka.actor.{Actor, ActorSystem, Props}
import akka.routing.BroadcastPool

class BroadcastPoolActor extends Actor {
  override def receive: Receive = {
    case msg: String => println(s"I am ${self.path.name}")
    case _ => println(s"I don't understand the message")
  }
}

object BroadcastPoolApp extends App {
  val actorSystem = ActorSystem("Hello-akka")
  val router = actorSystem.actorOf(BroadcastPool(5).props(Props[BroadcastPoolActor]))

  router ! "Hello"
}
