package linkmap

import scala.collection.concurrent.TrieMap
import scala.collection.mutable.Map

object LinkMap extends Map[CommentLocation, CommentLocation] {
	
	val map = Map[CommentLocation, CommentLocation]()
	
	def -= (k : CommentLocation) = {
		map -= k
		this
	}
	
	def += (kv : (CommentLocation,CommentLocation)) = {
		map += kv
		this
	}
	
	def get (k : CommentLocation) = {
		map get k
	}
	
	def iterator = map.iterator
	
	override def toString = {
		map map { case (k, v) => k + "\n -> " + v } mkString "\n"
	}
	
	def toDot : String = {
		"digraph {\n" +
		{ map map { case (k, v) => "\t" + k + " -> " + v + " ;" } mkString "\n" } +
		"\n}"
	}

	def toDotBySubreddit : String = {
		"digraph {\n" +
		map.toList.map{case (k, v) => (k.subreddit, v.subreddit)}
				  .groupBy{tuple => tuple}
				  .map{case ((source, destination), values) => "\t" + source + " -> " + destination + " [label=" + values.length + "] ;"}
				  .mkString("\n") + 
		"\n}"	
	}
}
