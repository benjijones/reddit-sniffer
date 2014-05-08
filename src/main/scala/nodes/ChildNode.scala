package nodes

import com.typesafe.config.ConfigFactory

import akka.actor.ActorSystem
import akka.kernel.Bootable

object ChildNode extends App {
	new ChildNode("ChildSystem").startup
}

class ChildNode(systemName : String) extends Bootable {

	val config = ConfigFactory.load.getConfig(systemName)
	val system = ActorSystem(systemName, config)
	
	def startup {
		println(this + " is starting...")
	}
	
	def shutdown {
		println(this + " is shutting down.")
		system.shutdown
		kuhn.api.shutdown
	}
}
