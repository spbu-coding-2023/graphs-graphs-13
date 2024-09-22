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

  fun runDijkstraAlgorithm(start: Int, end: Int) {
    resetGraphView()
    val dijkstra = Dijkstra(graph)
    val result = dijkstra.findShortestPaths(start, end)
    for (vertexId in result) {
      graphViewModel.verticesView[vertexId]?.color = Color(240, 128, 128)
    }
  }
}