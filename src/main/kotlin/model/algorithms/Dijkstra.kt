package model.algorithms

import model.graph.Graph
import java.util.PriorityQueue

/**
 * The Dijkstra class is used to find the shortest paths in a weighted graph using Dijkstra's algorithm.
 *
 * @property graph A directed graph in which shortest paths are searched.
 * @property distances Stores the lengths of the shortest paths from the starting vertex to other vertices.
 * @property previousVertices Stores previous vertices along the shortest path to each vertex.
 * @property queue Queue for processing vertices in order of increasing distances.
 */
class Dijkstra<D>(private val graph: Graph<D>) {
  private val distances = hashMapOf<Int, Int>()
  private val previousVertices = hashMapOf<Int, Int?>()
  private val queue = PriorityQueue<Pair<Int, Int>>(compareBy { it.second })

  /**
   * Finds the shortest path from one vertex (start) to another vertex (end) and returns a list of id vertices of the path.
   *
   * @param start The starting vertex from which to start searching for the shortest path.
   * @param end The end vertex to which the shortest path is sought.
   * @return A list of vertices that form the shortest path from vertex start to vertex end.
   */
  fun findShortestPaths(start: Int, end: Int): List<Int> {
    if (!graph.vertices.contains(start) || !graph.vertices.contains(end)) {
      throw NoSuchElementException("The vertex doesn't exist in the graph.")
    }
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
    // construct final path using previous vertices
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
