package model.algorithms

import model.graph.Graph

/**
 * Louvain algorithm for detecting communities in a graph.
 * @param graph The graph in which communities should be discovered.
 */
class Louvain(private val graph: Graph) {

  /**
   * Method for detecting communities in a graph using the Louvain algorithm.
   * @return A list of vertex sets representing the detected communities.
   */
  fun detectCommunities(): List<Set<Int>> {
    // each vertex starts in its own classer
    val communities = graph.vertices.keys.associateWith { it }.toMutableMap()
    var bestCommunities = communities.toMap()
    var bestModularity = calculateModularity(bestCommunities)
    var changed: Boolean
    while (true) { // until modularity stops increasing
      changed = false
      // all vertices of the network are enumerated, and each vertex tries to move to the classer
      // with a maximum increase in modularity with such movement
      for (vertex in graph.vertices.keys) {
        for (community in communities.keys) {
          if (vertex != community && communities[vertex] != community) {
            val newCommunities = bestCommunities.toMutableMap()
            newCommunities[vertex] = communities[community]!!
            val newModularity = calculateModularity(newCommunities)

            if (newModularity > bestModularity) {
              bestModularity = newModularity
              bestCommunities = newCommunities
              communities[vertex] = communities[community]!!
              changed = true
              break
            }
          }
        }
        if (changed) break
      }
      if (!changed) break
    }
    val resultCommunities = mutableListOf<Set<Int>>()
    for (resultCommunity in bestCommunities.values.toSet()) {
      val verticesOfCommunity = mutableSetOf<Int>()
      for ((vertex, community) in bestCommunities) {
        if (community == resultCommunity)
          verticesOfCommunity.add(vertex)
      }
      resultCommunities.add(verticesOfCommunity)
    }
    return resultCommunities
  }

  /**
   * Method for calculating community modularity.
   * @param communities Communities for which modularity needs to be calculated.
   * @return The meaning of modularity for the community.
   */
  private fun calculateModularity(communities: Map<Int, Int>): Double {
    val edgesNum = graph.edges.size.toDouble()
    var modularity = 0.0

    for ((vertex, neighbors) in graph.adjacency) {
      for (neighbor in neighbors.keys) {
        val delta = if (communities[vertex] == communities[neighbor]) 1.0 else 0.0

        // calculate modularity taking into account the direction of the edges
        val outDegree = graph.adjacency[vertex]!!.size // outgoing vertex degree
        val inDegree = neighbors.size // incoming vertex degree
        modularity += delta - (outDegree * inDegree) / edgesNum
      }
    }
    return modularity / edgesNum
  }
}
