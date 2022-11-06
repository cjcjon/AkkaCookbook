package com.packt.chapter1

import akka.actor.{Actor, ActorSystem, Props}

class SummingActorWithConstructor(initialSum: Int) extends Actor {
  // 액터 내부 상태
  var sum = 0

  // 상태에 적용될 행위
  override def receive: Receive = {
    // Integer 메시지를 수신한
    case x: Int =>
      sum = initialSum + sum + x
      println(s"my state as sum is $sum")

    case _ =>
      println("I don't know what are you talking about")
  }
}

object BehaviorAndState extends App {
  val actorSystem = ActorSystem("HelloAkka")

  // 액터 시스템 내부에 액터를 생성함
  // val actor = actorSystem.actorOf(Props[SummingActor])
  val actor = actorSystem.actorOf(Props(classOf[SummingActorWithConstructor], 0), "summing-actor")

  // 액터의 경로를 인쇄함
  println(actor.path)

  // 덧셈 메시지 전송
  for (_ <- 1 to 10) {
    actor ! 1
  }

  // 오류 메시지 전송
  actor ! "hello word"
}
