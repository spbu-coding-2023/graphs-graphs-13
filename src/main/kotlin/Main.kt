import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import model.graph.Graph
import model.graph.UndirectedGraph
import view.MainScreen
import viewmodel.MainScreenViewModel
import viewmodel.graph.CircularPlacementStrategy

val sampleGraph = UndirectedGraph<String>().apply {
  addVertex(1, "A")
  addVertex(2, "B")
  addVertex(3, "C")
  addVertex(4, "D")
  addVertex(5, "E")

  addEdge(Pair(1, 2), 1)
  addEdge(Pair(2, 3), 2)
  addEdge(Pair(3, 4), 3)
  addEdge(Pair(2, 4), 4)
  addEdge(Pair(1, 5), 5)
}

@Composable
@Preview
fun App() {
  MaterialTheme {
    MainScreen(MainScreenViewModel(sampleGraph, CircularPlacementStrategy()))
  }
}

fun main() = application {
  Window(onCloseRequest = ::exitApplication) {
    App()
  }
}