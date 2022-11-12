package com.packt.chapter3

import akka.actor.{Actor, ActorSystem, Props}
import akka.routing.ConsistentHashingPool
import akka.routing.ConsistentHashingRouter.{ConsistentHashMapping, ConsistentHashable, ConsistentHashableEnvelope}

case class Evict(key: String)
case class Get(key: String) extends ConsistentHashable {
  override def consistentHashKey: Any = key
}
case class Entry(key: String, value: String)

class Cache extends Actor {
  var cache = Map.empty[String, String]

  override def receive: Receive = {
    case Entry(key, value) =>
      println(s"${self.path.name} adding key $key")
      cache += (key -> value)
    case Get(key) =>
      println(s"${self.path.name} fetching key $key")
      sender ! cache.get(key)
    case Evict(key) =>
      println(s"${self.path.name} removing key $key")
      cache -= key
  }
}

object ConsistentHasingPoolApp extends App {
  def hashMapping: ConsistentHashMapping = {
    case Evict(key) => key
  }

  val actorSystem = ActorSystem("Hello-akka")
  val cache = actorSystem.actorOf(ConsistentHashingPool(10, hashMapping = hashMapping).props(Props[Cache]), name = "cache")

  cache ! ConsistentHashableEnvelope(message = Entry("hello", "HELLO"), hashKey = "hello")
  cache ! ConsistentHashableEnvelope(message = Entry("hi", "HI"), hashKey = "hi")
  cache ! Get("hello")
  cache ! Get("hi")
  cache ! Evict("hi")
}
