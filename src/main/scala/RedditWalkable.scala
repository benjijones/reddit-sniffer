import scala.util.matching.Regex

import akka.actor.Actor

trait RedditWalkable extends Actor {
	
	val redditURLPattern = new Regex(""".*(http\://)?(www\.|np\.)reddit\.com/r/([a-zA-Z0-9]+)/comments/([a-zA-Z0-9]+)/[a-zA-Z0-9_]+/([a-zA-Z0-9]+).*""", "http", "www", "subreddit", "linkId", "commentId")
	
	val redditURLSplitPattern = new Regex("""(?:http\://)?(?:www\.|np\.)(?:reddit\.com/r/)([a-zA-Z0-9]+)/comments/([a-zA-Z0-9]+)/([a-zA-Z0-9_]+)/([a-zA-Z0-9]+)""", "subreddit", "linkId", "linkTitle", "commentId")
	
}