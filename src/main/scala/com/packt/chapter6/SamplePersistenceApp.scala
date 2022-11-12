package com.packt.chapter6

import akka.actor.{ActorSystem, Props}
import akka.persistence.{PersistentActor, SnapshotOffer}

sealed trait UserCustomAction
case object AddAction extends UserCustomAction
case object RemoveAction extends UserCustomAction
case class UserUpdate(userId: String, action: UserCustomAction)

sealed trait Event
case class AddUserEvent(userId: String) extends Event
case class RemoveUserEvent(userId: String) extends Event

case class ActiveUsers(users: Set[String] = Set.empty[String]) {
  def update(evt: Event): ActiveUsers = evt match {
    case AddUserEvent(userId) => copy(users + userId)
    case RemoveUserEvent(userId) => copy(users.filterNot(_ == userId))
  }

  override def toString = s"$users"
}

class SamplePersistenceActor extends PersistentActor {
  override val persistenceId = "unique-id-1"
  var state: ActiveUsers = ActiveUsers()

  def updateState(event: Event): Unit =
    state = state.update(event)

  val receiveRecover: Receive = {
    case evt: Event => updateState(evt)
    case SnapshotOffer(_, snapshot: ActiveUsers) => state = snapshot
  }

  val receiveCommand: Receive = {
    case UserUpdate(userId, AddAction) => persist(AddUserEvent(userId))(updateState)
    case UserUpdate(userId, RemoveAction) => persist(RemoveUserEvent(userId))(updateState)
    case "snap" => saveSnapshot(state)
    case "print" => println(state)
  }
}

object SamplePersistenceApp extends App {
  val system = ActorSystem("example")
  val persistentActor1 = system.actorOf(Props[SamplePersistenceActor])

  persistentActor1 ! UserUpdate("foo", AddAction)
  persistentActor1 ! UserUpdate("baz", AddAction)
  persistentActor1 ! UserUpdate("def", AddAction)
  persistentActor1 ! "snap"
  persistentActor1 ! "print"

  persistentActor1 ! UserUpdate("baz", RemoveAction)
  persistentActor1 ! "print"
  Thread.sleep(5000)
  system.stop(persistentActor1)

  val persistentActor2 = system.actorOf(Props[SamplePersistenceActor])
  persistentActor2 ! "print"
  Thread.sleep(2000)

  system.terminate()
}