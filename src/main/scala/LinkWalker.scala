import kuhn.api.comments
import kuhn.api.links
import kuhn.api.shutdown
import kuhn.api.subreddit
import kuhn.Comment

class LinkWalker extends RedditWalkable {
	
	def receive = {
		case WalkLink(linkId) => comments(linkId) { case (ancestors, comment) => walk(ancestors, comment) }
	}
	
	private def walk(ancestors : Seq[Comment], comment : Comment) {
		comment.body match {
			case redditURLPattern(http, www, subreddit, linkId, commentId) => 
				LinkMap(CommentLocation(comment)) = CommentLocation(subreddit, linkId, commentId)
    		case _ =>
		}
		
	}
}