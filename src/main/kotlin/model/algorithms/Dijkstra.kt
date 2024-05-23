package model.algorithms

import model.graph.*
import java.util.PriorityQueue

class Dijkstra<D>(private val graph: Graph<D>) {
  fun findShortestPaths(start: Int, end: Int): List<Int> {
    if (!graph.vertices.contains(start) || !graph.vertices.contains(end)) {
      throw NoSuchElementException("The vertex doesn't exist in the graph.")
    }
    val distances = hashMapOf<Int, Int>()
    val previousVertices = hashMapOf<Int, Int?>()
    val queue = PriorityQueue<Pair<Int, Int>>(compareBy { it.second })
    distances[start] = 0
    queue.add(Pair(start, 0))

    while (queue.isNotEmpty()) {
      val (currentVertex, currentDistance) = queue.poll()
      if (currentVertex == end) break
      graph.adjacency[currentVertex]?.forEach { (neighbor, weight) ->
        if (weight == null) {
          throw IllegalArgumentException("Edge without weights in Dijkstra's algorithm.")
        } else if (weight < 0) {
          throw IllegalArgumentException("Edge with negative weights in Dijkstra's algorithm.")
        } else {
          val newDistance = currentDistance + weight
          if (newDistance < (distances[neighbor] ?: Int.MAX_VALUE)) {
            distances[neighbor] = newDistance
            previousVertices[neighbor] = currentVertex
            queue.add(Pair(neighbor, newDistance))
          }
        }
      }
    }
    val path = mutableListOf<Int>()
    previousVertices[end] ?: return path
    var current = end
    while (current != start) {
      path.add(current)
      current = previousVertices[current] ?: break
    }
    path.add(start)
    path.reverse()
    return path
  }
}