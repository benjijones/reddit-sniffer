package linkmap

object CommentLocation {
	def apply(comment : kuhn.Comment) = {
		new CommentLocation(comment.subreddit, comment.link_id.substring(3), comment.id)
	}
	def apply(subreddit : String, linkId : String, id : String) = {
		new CommentLocation(subreddit, linkId, id)
	}
}

class CommentLocation(	val subreddit : String,
						val linkId : String,
						val id : String) extends Serializable {
	override def toString = List(subreddit, linkId, id) mkString "/"
}