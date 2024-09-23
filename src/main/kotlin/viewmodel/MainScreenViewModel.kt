package viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import model.graph.Graph
import viewmodel.graph.GraphViewModel
import viewmodel.graph.RepresentationStrategy
import model.algorithms.*
import model.graph.DirectedGraph
import model.graph.UndirectedGraph

class MainScreenViewModel<D>(private val graph: Graph<D>, private val representationStrategy: RepresentationStrategy) {
  val showVerticesLabels = mutableStateOf(false)
  val showEdgesLabels = mutableStateOf(false)
  val graphViewModel = GraphViewModel(graph, showVerticesLabels, showEdgesLabels)
  private var algorithmRunning = false // флаг для отслеживания состояния работы алгоритма

  init {
    representationStrategy.place(800.0, 600.0, graphViewModel.verticesView.values)
  }

  fun resetGraphView() {
    representationStrategy.place(800.0, 600.0, graphViewModel.verticesView.values)
    graphViewModel.verticesView.values.forEach { v -> v.color = Color.Gray }
  }

  fun addVertex(id: Int, data: D) {
    graphViewModel.addVertex(id, data)
  }

  fun addEdge(from: Int, to: Int, w: Int?) {
    graphViewModel.addEdge(from, to, w)
  }

  fun removeVertex(id: Int) {
    graphViewModel.removeVertex(id)
  }

  fun removeEdge(from: Int, to: Int) {
    graphViewModel.removeEdge(from, to)
  }

  /** Paint the vertices of the found path.
   */
  fun runDijkstraAlgorithm(start: Int, end: Int) {
    resetGraphView()
    val dijkstra = Dijkstra(graph)
    val result = dijkstra.findShortestPaths(start, end)
    for (vertexId in result) {
      graphViewModel.verticesView[vertexId]?.color = Color(125, 21, 21)
    }
  }

  /** Paint each ccs its own color. The number of colors is limited,
   *  so if there are more than 5 ccs, the colors will begin to repeat.
   */
  fun runKosarajuAlgorithm() {
    if (graph is UndirectedGraph) {
      throw IllegalArgumentException("Kosaraju's algorithm cannot be run on undirected graphs.")
    }
    val colors = listOf(
      Color(125, 21, 21),
      Color(41, 37, 37),
      Color(145, 86, 86),
      Color(56, 4, 4),
      Color(161, 161, 161)
    )
    resetGraphView()
    val kosaraju = Kosaraju(graph as DirectedGraph)
    val result = kosaraju.findStronglyConnectedComponents()
    for ((i, ccs) in result.withIndex()) {
      for (vertexId in ccs) {
        graphViewModel.verticesView[vertexId]?.color = colors[i % 5]
      }
    }
  }

  /** Paint each community its own color. The number of colors is limited,
   *  so if there are more than 5 communities, the colors will begin to repeat.
   */
  fun runLouvainAlgorithm() {
    val colors = listOf(
      Color(125, 21, 21),
      Color(41, 37, 37),
      Color(145, 86, 86),
      Color(56, 4, 4),
      Color(161, 161, 161)
    )
    resetGraphView()
    val louvain = Louvain(graph)
    val result = louvain.detectCommunities()
    for ((i, community) in result.withIndex()) {
      for (vertexId in community) {
        graphViewModel.verticesView[vertexId]?.color = colors[i % 5]
      }
    }
  }
}
