package viewmodel.graph

import androidx.compose.runtime.State
import androidx.compose.ui.graphics.Color
import model.graph.Edge

class EdgeViewModel(
  val u: VertexViewModel,
  val v: VertexViewModel,
  var color: Color,
  var strokeWidth: Float,
  private val e: Edge,
  private val _labelVisible: State<Boolean>,
) {
  val label
    get() = e.weight?.toString() ?: ""

  val labelVisible
    get() = _labelVisible.value
}