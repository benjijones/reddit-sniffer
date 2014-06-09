package nodes

import akka.actor.Actor
import walker.RegisterLinkWorker
import linkmap.LinkMap
import walker.AddToLinkMap
import akka.actor.ActorRef
import kuhn.api._
import walker.WalkLink

class SubredditWorker(reddit : String) extends Actor {
	var backendWorkers = IndexedSeq[ActorRef]()
	var jobCount = 0
	
	def receive = {
		case RegisterLinkWorker(worker) if !backendWorkers.contains(worker) => 
			backendWorkers = backendWorkers :+ worker
			
		case add : AddToLinkMap =>
			context.parent forward add
	}
	
	links(subreddit(reddit)) {
		case link => {
			jobCount += 1
			backendWorkers(jobCount % backendWorkers.size) ! WalkLink(link.id)
		}
	}
}