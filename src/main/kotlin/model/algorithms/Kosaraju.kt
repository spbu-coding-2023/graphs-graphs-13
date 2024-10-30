package model.algorithms

import model.graph.DirectedGraph

/**
 * The Kosaraju class implements the Kosaraju algorithm for finding strongly connected
 * components (scc) in a directed graph.
 * @property graph is a directed graph in which we are looking for scc
 * @property visited map storing information about visited vertices
 * @property stack a stack for storing vertices in the order in which they were traversed
 * @property stronglyConnectedComponents list of strongly connected components in graph
 */
class Kosaraju(private val graph: DirectedGraph) {
  private val visited = hashMapOf<Int, Boolean>()
  private val stack = mutableListOf<Int>()
  private val stronglyConnectedComponents = mutableListOf<List<Int>>()

  /**
   * The findStronglyConnectedComponents method runs an algorithm to find a scc.
   * @return list of graph components
   */
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

  /**
   * The dfs method performs a depth-first traversal of the graph, starting from a given vertex.
   * @param vertexID identifier of the vertex from which the traversal begins
   * @param stack for storing vertices in traversed order
   * @param graph graph in which the traversal occurs
   */
  private fun dfs(vertexID: Int, stack: MutableList<Int>, graph: DirectedGraph) {
    visited[vertexID] = true
    for (nextVertexID in (graph.adjacency[vertexID]?.keys ?: emptyList()))
      if (visited[nextVertexID] != true) {
        dfs(nextVertexID, stack, graph)
      }
    stack.add(vertexID)
  }

  /**
   * The transposeGraph method transposes the graph.
   * @return the transposed graph
   */
  private fun transposeGraph(): DirectedGraph {
    val transposedGraph = DirectedGraph()
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
