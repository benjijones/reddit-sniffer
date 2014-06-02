package walker

import scala.annotation.varargs
import scala.util.matching.Regex

import akka.actor.Actor
import akka.actor.ActorIdentity
import akka.actor.ActorRef
import akka.actor.ActorSelection.toScala
import akka.actor.Identify
import akka.actor.RootActorPath
import akka.actor.actorRef2Scala
import akka.cluster.Cluster
import akka.cluster.ClusterEvent.CurrentClusterState
import akka.cluster.ClusterEvent.InitialStateAsEvents
import akka.cluster.ClusterEvent.MemberUp
import akka.cluster.MemberStatus
import kuhn.Comment
import kuhn.api.comments
import linkmap.CommentLocation


