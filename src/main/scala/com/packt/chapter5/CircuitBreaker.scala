package com.packt.chapter5

import akka.actor.{Actor, ActorSystem, Props}
import akka.pattern.{CircuitBreaker, ask}
import akka.util.Timeout

import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.duration.DurationInt
import scala.language.postfixOps

case class FetchRecord(recordId: Int)

case class Person(name: String, age: Int)

object DB {
  val data: Map[Int, Person] = Map(
    1 -> Person("John", 21),
    2 -> Person("Peter", 30),
    3 -> Person("James", 40),
    4 -> Person("Alice", 25),
    5 -> Person("Henry", 26),
    6 -> Person("Jackson", 48)
  )
}

class DBActor extends Actor {
  override def receive: Receive = {
    case FetchRecord(recordId) =>
      if (recordId >= 3 && recordId <= 5)
        Thread.sleep(3000)
      else sender ! DB.data.get(recordId)
  }
}

object CircuitBreaker extends App {
  val system = ActorSystem("Hello-Akka")
  implicit val ec: ExecutionContextExecutor = system.dispatcher
  implicit val timeout: Timeout = Timeout(3 seconds)

  val breaker = new CircuitBreaker(
    scheduler = system.scheduler,
    maxFailures = 3,
    callTimeout = 1 seconds,
    resetTimeout = 2 seconds,
  )
    .onOpen(println("============= Circuit is open ============="))
    .onClose(println("============= Circuit is closed ============="))

  val db = system.actorOf(Props[DBActor], "DBActor")

  (1 to 10).foreach(recordId => {
    Thread.sleep(3000)

    val askFuture = breaker.withCircuitBreaker(db ? FetchRecord(recordId))
    askFuture
      .map(record => s"Record is: $record and RecordId is: $recordId")
      .recover({
        case fail => "Failed with: " + fail.toString
      })
      .foreach(x => println(x))
  })
}
