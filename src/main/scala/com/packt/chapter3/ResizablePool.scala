package com.packt.chapter3

import akka.actor.{Actor, ActorSystem, Props}
import akka.routing.{DefaultResizer, RoundRobinPool}

case object Load

class LoadActor extends Actor {
  override def receive: Receive= {
    case Load => println("Handling loads of requests")
  }
}

object ResizablePoolApp extends App {
  val system = ActorSystem("Hello-Akka")
  val resizer= DefaultResizer(lowerBound = 2, upperBound = 15)
  val router = system.actorOf(RoundRobinPool(5, Some(resizer)).props(Props[LoadActor]))

  router ! Load
}
