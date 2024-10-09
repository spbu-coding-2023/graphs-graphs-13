package viewmodel.graph

import androidx.compose.runtime.State
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import model.graph.Edge
import model.graph.Graph
import model.graph.UndirectedGraph
import model.graph.Vertex
import viewmodel.defaultColorLine
import viewmodel.defaultColorVertex
import viewmodel.defaultStrokeWidth

class GraphViewModel(
  private val graph: Graph,
  private val showVerticesLabels: State<Boolean>,
  private val showEdgesLabels: State<Boolean>,
) {
  internal val verticesView: HashMap<Int, VertexViewModel> = hashMapOf()
  internal val edgesView: HashMap<Edge, EdgeViewModel> = hashMapOf()

  init {
    graph.getVertices().forEach { vertex ->
      verticesView[vertex.id] = VertexViewModel(0.dp, 0.dp, defaultColorVertex, vertex, showVerticesLabels)
    }
    graph.edges.forEach { edge ->
      val fst = verticesView[edge.vertices.first]
        ?: throw IllegalStateException("VertexView for vertex with id: ${edge.vertices.first} not found")
      val snd = verticesView[edge.vertices.second]
        ?: throw IllegalStateException("VertexView for vertex with id: ${edge.vertices.second} not found")
      edgesView[edge] = EdgeViewModel(fst, snd, defaultColorLine, defaultStrokeWidth, edge, showEdgesLabels)
    }
  }

  fun addVertex(id: Int, data: String): String? {
    val addedResult = graph.addVertex(id, data)
    if (addedResult != null) return addedResult
    verticesView[id] = VertexViewModel(0.dp, 0.dp, defaultColorVertex, graph.vertices[id]!!, showVerticesLabels)
    return null
  }

  fun removeVertex(id: Int): String? {
    val removedResult = graph.removeVertex(id)
    if (removedResult != null) return removedResult
    verticesView.remove(id)
    val edgesToRemove = edgesView.keys.filter { edge -> edge.vertices.first == id || edge.vertices.second == id }
    edgesToRemove.forEach { edge ->
      edgesView.remove(edge)
    }
    return null
  }

  fun addEdge(from: Int, to: Int, w: Int?): String? {
    val addedResult = graph.addEdge(Pair(from, to), w)
    if (addedResult != null) return addedResult
    val fst = verticesView[from] ?: return "VertexView for vertex with id: $from not found"
    val snd = verticesView[to] ?: return "VertexView for vertex with id: $to not found"
    graph.edges.find { it.vertices == Pair(from, to) }?.let { edge ->
      edgesView[edge] = EdgeViewModel(fst, snd, defaultColorLine, defaultStrokeWidth, edge, showEdgesLabels)
    }
    return null
  }

  fun removeEdge(from: Int, to: Int): String? {
    val removedResult = graph.removeEdge(Pair(from, to))
    if (removedResult != null) return removedResult
    edgesView.keys.find { it.vertices == Pair(from, to) }?.let { edge ->
      edgesView.remove(edge)
    }
    if (graph is UndirectedGraph) {
      edgesView.keys.find { it.vertices == Pair(to, from) }?.let { edge ->
        edgesView.remove(edge)
      }
    }
    return null
  }
}
