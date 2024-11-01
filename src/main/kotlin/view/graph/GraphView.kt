package view.graph

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import viewmodel.graph.GraphViewModel
import viewmodel.graph.VertexViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun GraphView(
  viewModel: GraphViewModel,
) {
  Box(modifier = Modifier
    .fillMaxSize()

  ) {
    viewModel.verticesView.values.forEach { v ->
      VertexView(v, Modifier)
    }
    viewModel.edgesView.values.forEach { e ->
      EdgeView(e, Modifier)
    }
  }
}