package com.packt.chapter2

import akka.actor.{Actor, ActorRef, ActorSystem, Props}

import scala.collection.mutable.ListBuffer

case class DoubleValue(x: Int)
case object CreateChild
case object Send
case class Response(x: Int)

class DoubleActor extends Actor {
  def receive: Receive = {
    case DoubleValue(number) =>
      println(s"${self.path.name} got the number $number")
      sender ! Response(number * 2)
  }
}

class ParentActor extends Actor {
  val random = new scala.util.Random
  var children: ListBuffer[ActorRef] = scala.collection.mutable.ListBuffer[ActorRef]()

  def receive: Receive = {
    case CreateChild => children ++= List(context.actorOf(Props[DoubleActor]))
    case Send =>
      println(s"Sending messages to children")
      children.zipWithIndex map { case(child, value) => child ! DoubleValue(random.nextInt(10)) }
    case Response(x) =>
      println(s"Parent: Response from child ${sender.path.name} is $x")
  }
}

object SendMessageToChildren extends App {
  val actorSystem = ActorSystem("Hello-Akka")
  val parent = actorSystem.actorOf(Props[ParentActor], "parent")

  parent ! CreateChild
  parent ! CreateChild
  parent ! CreateChild
  parent ! Send
}
