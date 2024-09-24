package viewmodel.graph

import androidx.compose.runtime.State
import androidx.compose.ui.graphics.Color
import model.graph.Edge

class EdgeViewModel<D>(
  val u: VertexViewModel<D>,
  val v: VertexViewModel<D>,
  var color: Color,
  var strokeWidth: Float,
  private val e: Edge<D>,
  private val _labelVisible: State<Boolean>,
) {
  val label
    get() = e.weight.toString()

  val labelVisible
    get() = _labelVisible.value
}