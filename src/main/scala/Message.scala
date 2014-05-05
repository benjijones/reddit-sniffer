import kuhn.Link
import kuhn.Comment

sealed trait Message

case class WalkLink(linkId : String) extends Message

case class WalkSubreddit(subreddit : String) extends Message

//case class AddToLinkMap(comment : Comment, ) extends Message