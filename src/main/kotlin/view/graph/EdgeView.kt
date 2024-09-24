package view.graph

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import viewmodel.graph.EdgeViewModel
import kotlin.math.pow
import kotlin.math.sqrt

@Composable
fun <D> EdgeView(
  viewModel: EdgeViewModel<D>,
  modifier: Modifier = Modifier,
) {

  fun calculateEdge(): Pair<DpOffset, DpOffset> {
    val startX = (viewModel.u.x + viewModel.u.radius).value
    val startY = (viewModel.u.y + viewModel.u.radius).value
    val endX = (viewModel.v.x + viewModel.v.radius).value
    val endY = (viewModel.v.y + viewModel.v.radius).value
    val returnStart: DpOffset
    val returnEnd: DpOffset

    val difXStart: Float
    val difYStart: Float
    val difXEnd: Float
    val difYEnd: Float
    val vertexDistance: Float =
      sqrt((startX - endX).pow(2) + (startY - endY).pow(2))

    val coefficientStart: Float = viewModel.u.radius.value / vertexDistance
    val coefficientEnd: Float = viewModel.v.radius.value / vertexDistance
    difXStart = coefficientStart * (endX - startX)
    difYStart = coefficientStart * (endY - startY)
    difXEnd = coefficientEnd * (startX - endX)
    difYEnd = coefficientEnd * (startY - endY)

    returnStart = DpOffset(
      (startX + difXStart).dp,
      (startY + difYStart).dp
    )
    returnEnd = DpOffset(
      (endX + difXEnd).dp,
      (endY + difYEnd).dp
    )
    return returnStart to returnEnd
  }

  Canvas(modifier = modifier.fillMaxSize()) {
    drawLine(
      start = Offset(
        x = calculateEdge().first.x.toPx(),
        y = calculateEdge().first.y.toPx()
      ),
      end = Offset(
        x = calculateEdge().second.x.toPx(),
        y = calculateEdge().second.y.toPx()
      ),
      color = viewModel.color
    )
  }
  if (viewModel.labelVisible) {
    Text(
      modifier = Modifier
        .offset(
          viewModel.u.x + (viewModel.v.x - viewModel.u.x) / 2,
          viewModel.u.y + (viewModel.v.y - viewModel.u.y) / 2
        ),
      text = viewModel.label,
    )
  }
}