package com.packt.chapter1

import akka.actor.{Actor, ActorSystem, PoisonPill, Props}

case object Stop

class ShutdownActor extends Actor {
  override def receive: Receive = {
    case msg: String => println(s"$msg")
    case Stop => context.stop(self)
  }
}

object Shutdown extends App {
  val actorSystem = ActorSystem("HelloAkka")

  val shutdownActor1 = actorSystem.actorOf(Props[ShutdownActor], "ShutdownActor1")
  shutdownActor1 ! "hello"
  shutdownActor1 ! PoisonPill
  shutdownActor1 ! "Are you there?"

  val shutdownActor2 = actorSystem.actorOf(Props[ShutdownActor], "ShutdownActor2")
  shutdownActor2 ! "hello"
  shutdownActor2 ! Stop
  shutdownActor2 ! "Are you there?"
}
