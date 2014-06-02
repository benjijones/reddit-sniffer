package walker

import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.actorRef2Scala
import kuhn.api.links
import kuhn.api.subreddit
import akka.actor.ActorLogging

class SubredditWorker extends Actor with ActorLogging {
	
	var linkWorkers = IndexedSeq[ActorRef]()
	var jobCount = 0
	def receive = {
		
		case RegisterLinkWorker(worker) if !linkWorkers.contains(worker) => 
			println("NEW WORKER " + worker.path)
			context watch worker
			linkWorkers = linkWorkers :+ sender
			
		case SearchSubreddit(name) => 
			log.info(this + " received " + name)
			
			links(subreddit(name)) { case link => {
					log.info(this + " is sending " + "yo")//link.id)// + " to " + actor + "...")
					jobCount += 1
					linkWorkers(jobCount % linkWorkers.size) ! new WalkLink(link.id)
				}
			}
			
		case mes : AddToLinkMap =>
			context.parent forward mes
//			println("adding to linkMap: " + source + " -> " + destination)
	}
	
}
