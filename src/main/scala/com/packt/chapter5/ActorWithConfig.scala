package com.packt.chapter5

import akka.actor.{Actor, ActorSystem, Props}
import com.typesafe.config.{Config, ConfigFactory}

class MyActor extends Actor {
  override def receive: Receive = {
    case msg: String => println(msg)
  }
}

object ActorWithConfig extends App {
  val config: Config = ConfigFactory.load("application.conf")
  val actorSystem = ActorSystem(config.getString("my-actor.actor-system"))
  val actorName = config.getString("my-actor.actor-name")
  val actor = actorSystem.actorOf(Props[MyActor], actorName)
  println(actor.path)
}
