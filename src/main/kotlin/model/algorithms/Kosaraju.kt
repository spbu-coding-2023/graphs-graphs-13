package model.algorithms

import model.graph.DirectedGraph

class Kosaraju<D>(private val graph: DirectedGraph<D>) {
  private val visited = hashMapOf<Int, Boolean>()
  private val stack = mutableListOf<Int>()
  private val stronglyConnectedComponents = mutableListOf<List<Int>>()

  fun findStronglyConnectedComponents(): List<List<Int>> {
    for (vertexID in graph.vertices.keys) {
      if (visited[vertexID] != true) {
        dfs(vertexID, stack, graph)
      }
    }
    val transposedGraph = transposeGraph()
    visited.replaceAll { _, _ -> false }
    while (stack.isNotEmpty()) {
      val vertexID = stack.removeAt(stack.size - 1)
      if (visited[vertexID] != true) {
        val component = mutableListOf<Int>()
        dfs(vertexID, component, transposedGraph)
        stronglyConnectedComponents.add(component)
      }
    }
    return stronglyConnectedComponents
  }

  private fun dfs(vertexID: Int, stack: MutableList<Int>, graph: DirectedGraph<D>) {
    visited[vertexID] = true
    for (nextVertexID in (graph.adjacency[vertexID]?.keys ?: emptyList()))
      if (visited[nextVertexID] != true) {
        dfs(nextVertexID, stack, graph)
      }
    stack.add(vertexID)
  }

  private fun transposeGraph(): DirectedGraph<D> {
    val transposedGraph = DirectedGraph<D>()
    for ((id, vertex) in graph.vertices) {
      transposedGraph.addVertex(id, vertex.data)
    }
    for (edge in graph.edges) {
      val (firstVertexID, secondVertexID) = edge.vertices
      transposedGraph.addEdge(Pair(secondVertexID, firstVertexID), edge.weight)
    }
    return transposedGraph
  }
}
