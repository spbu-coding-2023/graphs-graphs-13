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
  internal val verticesView: HashMap<Int, VertexViewModel<D>> = hashMapOf()
  internal val edgesView: HashMap<Edge<D>, EdgeViewModel<D>> = hashMapOf()

  init {
    graph.getVertices().forEach { vertex ->
      verticesView[vertex.id] = VertexViewModel(0.dp, 0.dp, Color.Gray, vertex, showVerticesLabels)
    }
    graph.edges.forEach { edge ->
      val fst = verticesView[edge.vertices.first]
        ?: throw IllegalStateException("VertexView for vertex with id: ${edge.vertices.first} not found")
      val snd = verticesView[edge.vertices.second]
        ?: throw IllegalStateException("VertexView for vertex with id: ${edge.vertices.second} not found")
      edgesView[edge] = EdgeViewModel(fst, snd, edge, showEdgesLabels)
    }
  }

  val verticesViewValues: Collection<VertexViewModel<D>>
    get() = verticesView.values

  val edgesViewValues: Collection<EdgeViewModel<D>>
    get() = edgesView.values
}
