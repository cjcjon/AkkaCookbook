package com.packt.chapter1

import akka.actor.{Actor, ActorRef, ActorSystem, Props}

import scala.util.Random.nextInt

// 액터에 사용할 메시지 오브젝트
object Messages {
  case class Start(actorRef: ActorRef)
  case class Done(randomNumber: Int)
  case object GiveMeRandomNumber
}

// 임의의 숫자 생성 액터
class RandomNumberGeneratorActor extends Actor {
  import com.packt.chapter1.Messages._

  override def receive: Receive = {
    case GiveMeRandomNumber =>
      println("received a message to generate a random number")
      val randomNumber = nextInt
      sender ! Done(randomNumber)
  }
}

// 숫자 요청 액터
class QueryActor extends Actor {
  import com.packt.chapter1.Messages._

  override def receive: Receive = {
    case Start(actorRef) =>
      println(s"send me the next random number")
      actorRef ! GiveMeRandomNumber

    case Done(randomNumber) =>
      println(s"received a random number $randomNumber")
  }
}

object Communication extends App {
  import com.packt.chapter1.Messages._

  val actorSystem = ActorSystem("HelloAkka")
  val randomNumberGenerator = actorSystem.actorOf(Props[RandomNumberGeneratorActor], "randomNumberGeneratorActor")
  val queryActor = actorSystem.actorOf(Props[QueryActor], "queryActor")

  queryActor ! Start(randomNumberGenerator)
}
