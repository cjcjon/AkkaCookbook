package com.packt.chapter1

import akka.actor.{Actor, ActorSystem, Props}
import akka.dispatch.{PriorityGenerator, UnboundedPriorityMailbox}
import com.typesafe.config.Config

class MyPriorityActor extends Actor {
  def receive: PartialFunction[Any, Unit] = {
    // Integer 메시지
    case x: Int => println(x)
    // 문자열 메시지
    case x: String => println(x)
    // Long 메시지
    case x: Long => println(x)
    // 그 밖의 메시지
    case x => println(x)
  }
}

class MyPriorityActorMailbox(settings: ActorSystem.Settings, config: Config) extends UnboundedPriorityMailbox(
  // 우선순위 생성기를 만듦. 숫자가 낮을수록 우선순위 높음.
  PriorityGenerator {
    // Int 메시지
    case x: Int => 1
    // 문자열 메시지
    case x: String => 0
    // Long 메시지
    case x: Long => 2
    // 그 밖의 메시지
    case _ => 3
  }
)

object PriorityMailbox extends App {
  val actorSystem = ActorSystem("HelloAkka")
  val myPriorityActor = actorSystem.actorOf(Props[MyPriorityActor].withDispatcher("prio-dispatcher"))

  myPriorityActor ! 6.0
  myPriorityActor ! 1
  myPriorityActor ! 5.0
  myPriorityActor ! 3
  myPriorityActor ! "Hello"
  myPriorityActor ! 5
  myPriorityActor ! "I am priority actor"
  myPriorityActor ! "I process string messages first, then integer, long and others"
}