package walker

import akka.actor.ActorRef
import akka.actor.actorRef2Scala
import kuhn.Link
import kuhn.api.links
import kuhn.api.subreddit
import linkmap.LinkMap

class SubredditWalker extends RedditWalkable {
	
	def receive = {
		case WalkSubreddit(name, actor) => 
//			println(this + " received " + name)
			links(subreddit(name)) { case link => walk(actor, link) }
		case AddToLinkMap(source, destination) =>
//			println("adding to linkMap: " + source + " -> " + destination)
			LinkMap(source) = destination
	}
	
	private def walk(actor : ActorRef, link : Link) {
//		println(this + " is sending " + link.id + " to " + actor + "...")
		actor ! new WalkLink(link.id)
	}
	
}
