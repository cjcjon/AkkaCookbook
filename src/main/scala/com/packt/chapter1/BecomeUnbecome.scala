package com.packt.chapter1

import akka.actor.{Actor, ActorSystem, Props}

class BecomeUnbecomeActor extends Actor {
  override def receive: Receive = {
    case true => context.become(isStateTrue)
    case false => context.become(isStateFalse)
  }

  def isStateTrue: Receive = {
    case msg: String => println(s"$msg")
    case false => context.become(isStateFalse)
  }

  def isStateFalse: Receive = {
    case msg: Int => println(s"$msg")
    case true => context.become(isStateTrue)
  }
}

object BecomeUnbecome extends App {
  val actorSystem = ActorSystem("HelloAkka")
  val becomeUnbecome = actorSystem.actorOf(Props[BecomeUnbecomeActor])

  becomeUnbecome ! true
  becomeUnbecome ! "Hello how are you?"
  becomeUnbecome ! false
  becomeUnbecome ! 1100
  becomeUnbecome ! true
  becomeUnbecome ! "What do u do?"
}
