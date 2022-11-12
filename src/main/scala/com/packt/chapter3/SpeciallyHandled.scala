package com.packt.chapter3

import akka.actor.{Actor, ActorSystem, PoisonPill, Props}
import akka.routing.{Broadcast, RandomPool}

case object Handle

class SpeciallyHandledActor extends Actor {
  override def receive: Receive = {
    case Handle => println(s"${self.path.name} says hello")
  }
}

object SpeciallyHandled extends App {
  val actorSystem = ActorSystem("Hello-akka")
  val router = actorSystem.actorOf(RandomPool(5).props(Props[SpeciallyHandledActor]))

  router ! Broadcast(Handle)
  router ! Broadcast(PoisonPill)
  router ! Handle
}
