package walker

import akka.actor.ActorRef
import akka.actor.actorRef2Scala
import kuhn.Comment
import kuhn.api.comments
import linkmap.CommentLocation

class LinkWalker extends RedditWalkable {
	
	def receive = {
		case WalkLink(linkId) => 
			val replyTo = sender
//			println(this.context.self + " received " + linkId + ", replyTo: " + replyTo)
			comments(linkId) {
				case (ancestors, comment) => 
					walk(comment, replyTo)
			}
	}
	
	def walk(comment : Comment, replyTo : ActorRef) {
//		print(this + " is reading " + comment.id + "...")
		comment.body match {
			case redditURLPattern(http, www, subreddit, linkId, commentId) => 
//				println("MATCH FOUND.")
				replyTo ! new AddToLinkMap(CommentLocation(comment), CommentLocation(subreddit, linkId, commentId))
			case _ =>
//				println("not found.")
		}
	}
}
