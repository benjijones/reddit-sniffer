import nodes.{Backend, Frontend}
import walker.SearchSubreddit
import scala.io.Source
import kuhn.api
import linkmap.LinkMap
import java.io.File

object Client extends App {
	
	Backend.main(Array[String]("8989"))
	Backend.main(Array[String]("8990"))
	Frontend.main(Array[String]())
	
	for (input <- Source.stdin.getLines){ input match {
		case "exit" => {
			api.shutdown
			Backend.shutdown
			Frontend.shutdown
		}
		case add : String if add.startsWith("add ") => {
			Frontend.frontend ! SearchSubreddit(add.split(" ")(1))
		}
		case "run" => {
			List("SubredditDrama", "pics") foreach (Frontend.frontend ! SearchSubreddit(_))
		}
		case "map" => {
			val pw = new java.io.PrintWriter(new File("linkmap.out"))
			try pw.write(LinkMap.toDotBySubreddit) finally pw.close
		}
		case _ => //do nothing
	}}
}
