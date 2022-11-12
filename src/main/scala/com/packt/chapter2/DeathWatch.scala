package com.packt.chapter2

import akka.actor.{Actor, ActorRef, ActorSystem, Props, Terminated}

case object Service
case object Kill

class ServiceActor extends Actor {
  def receive: Receive = {
    case Service => println("I provide a special service")
  }
}

class DeathWatchActor extends Actor {
  val child: ActorRef = context.actorOf(Props[ServiceActor], "serviceActor")
  context.watch(child)

  def receive: Receive = {
    case Service => child ! Service
    case Kill => context.stop(child)
    case Terminated(`child`) => println("The service actor has terminated and no longer available")
  }
}

object DeathWatchApp extends App {
  val actorSystem = ActorSystem("Supervision")
  val deathWatchActor = actorSystem.actorOf(Props[DeathWatchActor])

  deathWatchActor ! Service
  deathWatchActor ! Service

  Thread.sleep(1000)

  deathWatchActor ! Kill
  deathWatchActor ! Service
}
