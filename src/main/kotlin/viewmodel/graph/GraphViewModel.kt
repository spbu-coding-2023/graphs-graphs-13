package viewmodel.graph

import androidx.compose.runtime.State
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import model.graph.Edge
import model.graph.Graph
import model.graph.Vertex

class GraphViewModel<D>(
  private val graph: Graph<D>,
  showVerticesLabels: State<Boolean>,
  showEdgesLabels: State<Boolean>,
) {
  private val verticesView: HashMap<Vertex<D>, VertexViewModel<D>> = hashMapOf()

  init {
    graph.getVertices().forEach { vertex ->
      verticesView[vertex] = VertexViewModel(0.dp, 0.dp, Color.Blue, vertex, showVerticesLabels)
    }
  }

  private val edgesView: HashMap<Edge<D>, EdgeViewModel<D>> = hashMapOf()

  init {
    graph.edges.forEach { edge ->
      val fst = verticesView[graph.vertices[edge.vertices.first]]
        ?: throw IllegalStateException("VertexView for vertex with id: ${edge.vertices.first} not found")
      val snd = verticesView[graph.vertices[edge.vertices.second]]
        ?: throw IllegalStateException("VertexView for vertex with id: ${edge.vertices.second} not found")
      edgesView[edge] = EdgeViewModel(fst, snd, edge, showEdgesLabels)
    }
  }

  val verticesViewValues: Collection<VertexViewModel<D>>
    get() = verticesView.values

  val edgesViewValues: Collection<EdgeViewModel<D>>
    get() = edgesView.values
}
