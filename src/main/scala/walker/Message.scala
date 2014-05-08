package walker

import akka.actor.ActorRef
import linkmap.CommentLocation

sealed trait Message

case class WalkLink(linkId : String) extends Message

case class WalkSubreddit(subreddit : String, actor : ActorRef) extends Message

case class AddToLinkMap(source : CommentLocation, destination : CommentLocation) extends Message