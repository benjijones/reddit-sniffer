import scala.collection.concurrent.TrieMap
import scala.collection.mutable.Map

object LinkMap extends Map[CommentLocation, CommentLocation] {
	
	val map = TrieMap[CommentLocation, CommentLocation]()
	
	def -= (key : CommentLocation) = {
		map -= key
		this
	}
	
	def += (kv : (CommentLocation, CommentLocation)) = {
		map += kv
		this
	}
	
	def get(key : CommentLocation) = {
		map get key
	}
	
	def iterator = map.iterator
	
	override def toString = {
		map map { case (k, v) => k + "\n -> " + v } mkString "\n"
	}
	
	def toDot = {
		"digraph {\n" +
		{ map map { case (k, v) => "\t" + k + "->" + v + ";" } mkString "\n" } +
		"\n}"
	}
}