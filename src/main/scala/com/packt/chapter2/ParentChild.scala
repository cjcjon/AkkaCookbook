package com.packt.chapter2

import akka.actor.SupervisorStrategy.{Escalate, Restart}
import akka.actor.{Actor, ActorSystem, OneForOneStrategy, Props}

import scala.concurrent.duration.DurationInt
import scala.language.postfixOps


case object CreateChild
case class Greet(msg: String)

class ChildActor extends Actor {
  def receive: Receive = {
    case Greet(msg) => println(s"My parent[${self.path.parent}] greeted to me [${self.path}] $msg")
  }
}

class ParentActor extends Actor {
  def receive: Receive = {
    case CreateChild =>
      val child = context.actorOf(Props[ChildActor], "child")
      child ! Greet("Hello Child")
  }
}

object ParentChild extends App {
  val actorSystem = ActorSystem("Supervisoion")
  val parent = actorSystem.actorOf(Props[ParentActor], "parent")

  parent ! CreateChild
}
