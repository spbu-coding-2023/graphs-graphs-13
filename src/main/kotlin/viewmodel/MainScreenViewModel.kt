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
    representationStrategy.place(800.0, 600.0, graphViewModel.verticesViewValues)
  }

  fun resetGraphView() {
    representationStrategy.place(800.0, 600.0, graphViewModel.verticesViewValues)
    graphViewModel.verticesViewValues.forEach { v -> v.color = Color.Gray }
  }

  /** Paint the vertices of the found path.
   */
  fun runDijkstraAlgorithm(start: Int, end: Int) {
    resetGraphView()
    val dijkstra = Dijkstra(graph)
    val result = dijkstra.findShortestPaths(start, end)
    for (vertexId in result) {
      graphViewModel.verticesView[vertexId]?.color = Color(240, 128, 128)
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
      Color(240, 128, 128),
      Color(106, 90, 205),
      Color(102, 205, 170),
      Color(188, 143, 143),
      Color(218, 112, 214)
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
      Color(240, 128, 128),
      Color(106, 90, 205),
      Color(102, 205, 170),
      Color(188, 143, 143),
      Color(218, 112, 214)
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
