package view.graph

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import viewmodel.graph.VertexViewModel

@Composable
fun VertexView(
  viewModel: VertexViewModel,
  modifier: Modifier = Modifier,
) {

  fun isColorDark(color: Color): Boolean {
    val red = color.red
    val green = color.green
    val blue = color.blue
    val brightness = (red * 299 + green * 587 + blue * 114) / 1000
    return brightness < 0.5
  }

  Box(modifier = modifier
    .size(viewModel.radius * 2, viewModel.radius * 2)
    .offset(viewModel.x, viewModel.y)
    .background(
      color = viewModel.color,
      shape = CircleShape
    )
    .pointerInput(viewModel) {
      detectDragGestures { change, dragAmount ->
        change.consume()
        viewModel.onDrag(dragAmount)
      }
    }
  ) {
    if (viewModel.labelVisible) {
      Text(
        modifier = Modifier
          .align(Alignment.Center),
        text = viewModel.label,
        color = if (isColorDark(viewModel.color)) Color.White else Color.Black
      )
    }
    if (viewModel.idVisible) {
      Text(
        modifier = Modifier
          .align(Alignment.Center).offset(0.dp, -viewModel.radius - 10.dp),
        text = viewModel.id,
        color = Color.Black
      )
    }
  }
}
