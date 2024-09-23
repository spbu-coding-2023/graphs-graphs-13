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
import model.graph.DirectedGraph
import model.graph.Graph
import model.graph.UndirectedGraph
import view.MainScreen
import viewmodel.MainScreenViewModel
import viewmodel.graph.CircularPlacementStrategy

val sampleGraph = DirectedGraph<String>().apply {
  addVertex(1, "A")
  addVertex(2, "B")
  addVertex(3, "C")
  addVertex(4, "D")
  addVertex(5, "E")
  addVertex(6, "F")
  addVertex(7, "G")
  addVertex(8, "H")
  addVertex(9, "I")
  addEdge(Pair(1, 2), 4)
  addEdge(Pair(2, 3), 12)
  addEdge(Pair(3, 4), 3)
  addEdge(Pair(4, 3), 56)
  addEdge(Pair(5, 6), null)
  addEdge(Pair(7, 8), 0)
  addEdge(Pair(8, 9), -19)
  addEdge(Pair(9, 7), 2)
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