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

class GraphViewModel<D>(
  private val graph: Graph<D>,
  private val showVerticesLabels: State<Boolean>,
  private val showEdgesLabels: State<Boolean>,
) {
  internal val verticesView: HashMap<Int, VertexViewModel<D>> = hashMapOf()
  internal val edgesView: HashMap<Edge<D>, EdgeViewModel<D>> = hashMapOf()

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

  fun addVertex(id: Int, data: D) {
    graph.addVertex(id, data)
    verticesView[id] = VertexViewModel(0.dp, 0.dp, defaultColorVertex, graph.vertices[id]!!, showVerticesLabels)
  }

  fun removeVertex(id: Int) {
    graph.removeVertex(id)
    verticesView.remove(id)
    val edgesToRemove = edgesView.keys.filter { edge -> edge.vertices.first == id || edge.vertices.second == id }
    edgesToRemove.forEach { edge ->
      edgesView.remove(edge)
    }
  }

  fun addEdge(from: Int, to: Int, w: Int?) {
    graph.addEdge(Pair(from, to), w)
    val fst = verticesView[from] ?: throw IllegalStateException("VertexView for vertex with id: $from not found")
    val snd = verticesView[to] ?: throw IllegalStateException("VertexView for vertex with id: $to not found")
    graph.edges.find { it.vertices == Pair(from, to) }?.let { edge ->
      edgesView[edge] = EdgeViewModel(fst, snd, defaultColorLine, defaultStrokeWidth, edge, showEdgesLabels)
    }
  }

  fun removeEdge(from: Int, to: Int) {
    graph.removeEdge(Pair(from, to))
    edgesView.keys.find { it.vertices == Pair(from, to) }?.let { edge ->
      edgesView.remove(edge)
    }
    if (graph is UndirectedGraph) {
      edgesView.keys.find { it.vertices == Pair(to, from) }?.let { edge ->
        edgesView.remove(edge)
      }
    }
  }
}
