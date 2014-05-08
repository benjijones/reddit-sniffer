package linkmap

import scala.collection.concurrent.TrieMap
import scala.collection.mutable.Map

object LinkMap extends Map[CommentLocation, CommentLocation] {
	
	val map = TrieMap[CommentLocation, CommentLocation]()
	
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
	
	def toDot = {
		"digraph {\n" +
		{ map map { case (k, v) => "\t" + k + " -> " + v + " ;" } mkString "\n" } +
		"\n}"
	}
}