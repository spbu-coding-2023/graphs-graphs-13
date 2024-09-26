package viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import databases.Neo4jRepository
import model.graph.Graph
import viewmodel.graph.GraphViewModel
import viewmodel.graph.RepresentationStrategy
import model.algorithms.*
import model.graph.DirectedGraph
import model.graph.UndirectedGraph
import model.graph.Vertex

var defaultColorLine: Color = Color.Black
var defaultColorVertex: Color = Color.Gray
var defaultStrokeWidth: Float = 4f

class MainScreenViewModel<D>(private val graph: Graph<D>, private val representationStrategy: RepresentationStrategy) {
  val showVerticesLabels = mutableStateOf(false)
  val showEdgesLabels = mutableStateOf(false)
  val graphViewModel = GraphViewModel(graph, showVerticesLabels, showEdgesLabels)

  init {
    representationStrategy.place(800.0, 600.0, graphViewModel.verticesView.values)
  }

  fun resetGraphView() {
    representationStrategy.place(800.0, 600.0, graphViewModel.verticesView.values)
    graphViewModel.verticesView.values.forEach { v -> v.color = defaultColorVertex }
    graphViewModel.edgesView.values.forEach { e ->
      e.color = defaultColorLine
      e.strokeWidth = defaultStrokeWidth
    }
  }

  fun addVertex(id: Int, data: D) {
    graphViewModel.addVertex(id, data)
  }

  fun saveToNeo4j(uri: String, user: String, password: String) {
    val neo = Neo4jRepository(uri, user, password)
    neo.addGraph(graph)
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

  /** Paint the vertices and edges of the found path.
   */
  fun runDijkstraAlgorithm(start: Int, end: Int) {
    resetGraphView()
    val dijkstra = Dijkstra(graph)
    val result = dijkstra.findShortestPaths(start, end)
    for (vertexId in result) {
      graphViewModel.verticesView[vertexId]?.color = Color(125, 21, 21)
    }
    for (edgeView in graphViewModel.edgesView) {
      if (edgeView.key.vertices.first in result && edgeView.key.vertices.second in result) {
        edgeView.value.color = Color(10, 230, 248)
        edgeView.value.strokeWidth = 9f
      }
    }
  }

  /** Paint each ccs its own color. The number of colors is limited,
   *  so if there are more than 10 ccs, the colors will begin to repeat.
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
      Color(161, 161, 161),
      Color(115, 72, 101),
      Color(38, 11, 29),
      Color(255, 133, 141),
      Color(99, 48, 37),
      Color(61, 67, 74)
    )
    resetGraphView()
    val kosaraju = Kosaraju(graph as DirectedGraph)
    val result = kosaraju.findStronglyConnectedComponents()
    for ((i, ccs) in result.withIndex()) {
      for (vertexId in ccs) {
        graphViewModel.verticesView[vertexId]?.color = colors[i % 10]
      }
    }
  }

  /** Paint each community its own color. The number of colors is limited,
   *  so if there are more than 10 communities, the colors will begin to repeat.
   */
  fun runLouvainAlgorithm() {
    val colors = listOf(
      Color(125, 21, 21),
      Color(41, 37, 37),
      Color(145, 86, 86),
      Color(56, 4, 4),
      Color(161, 161, 161),
      Color(115, 72, 101),
      Color(38, 11, 29),
      Color(255, 133, 141),
      Color(99, 48, 37),
      Color(61, 67, 74)
    )
    resetGraphView()
    val louvain = Louvain(graph)
    val result = louvain.detectCommunities()
    for ((i, community) in result.withIndex()) {
      for (vertexId in community) {
        graphViewModel.verticesView[vertexId]?.color = colors[i % 10]
      }
    }
  }

  /** Paints over the vertices and edges that belong to the found MST.
   */
  fun runPrimAlgorithm(): Int {
    if (graph is DirectedGraph<D>) {
      throw IllegalArgumentException("Prims's algorithm cannot be run on directed graphs.")
    }
    val prim = Prim(graph as UndirectedGraph<D>)
    val result = prim.treePrim()
    val weight = prim.weightPrim()
    resetGraphView()
    for (graphComponent in result) {
      for (vertexId in graphComponent.vertices.keys) {
        graphViewModel.verticesView[vertexId]?.color = Color(10, 230, 208)
      }
      for (edgeView in graphViewModel.edgesView) {
        if (edgeView.key in graphComponent.edges) {
          edgeView.value.color = Color(10, 230, 248)
          edgeView.value.strokeWidth = 9f
        }
      }
    }
    return weight
  }

  fun runCycleSearchAlgorithm(vertexId: Int): Boolean {
    if (vertexId !in graph.vertices.keys) {
      throw IllegalArgumentException("Vertex with id = $vertexId doesn't exists in the graph.")
    }
    if (graph is DirectedGraph<D>) {
      throw IllegalArgumentException("CycleSearch algorithm cannot be run on directed graphs.")
    }
    val cycleSearch = CycleSearch(graph as UndirectedGraph)
    val result = cycleSearch.findCycle(Vertex(vertexId, graph.vertices[vertexId]!!.data))
    resetGraphView()
    if (result != null) {
      for (idVertex in result.vertices.keys) {
        graphViewModel.verticesView[idVertex]?.color = Color(10, 230, 208)
      }
      for (edgeView in graphViewModel.edgesView) {
        if (edgeView.key in result.edges) {
          edgeView.value.color = Color(10, 230, 248)
          edgeView.value.strokeWidth = 9f
        }
      }
      return false
    }
    return true
  }

}
