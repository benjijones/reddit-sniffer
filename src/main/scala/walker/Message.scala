package walker

import akka.actor.ActorPath
import akka.actor.ActorRef
import linkmap.CommentLocation

sealed trait Message

case class WalkLink(linkId : String) extends Message

case class Register extends Message

case class SearchSubreddit(subreddit : String) extends Message

case class RegisterLinkWorker(worker : ActorRef) extends Message

case class WalkSubreddit(subreddit : String, actor : ActorRef) extends Message

case class AddToLinkMap(source : CommentLocation, destination : CommentLocation) extends Message