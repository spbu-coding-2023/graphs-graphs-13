import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import databases.Neo4jRepository
import model.graph.*
import view.MainScreen
import view.WelcomeScreen
import viewmodel.MainScreenViewModel
import viewmodel.graph.CircularPlacementStrategy

@Composable
@Preview
fun App() {
  MaterialTheme {
    // пока что, для удобства разработки
    val neo4j = Neo4jRepository("bolt://localhost:7687", "neo4j", "password")
    var graphType by remember { mutableStateOf<String?>(null) }
    var sampleGraph by remember { mutableStateOf<Graph?>(null) }
    if (graphType == null) {
      WelcomeScreen { selectedGraphType ->
        graphType = selectedGraphType
        sampleGraph = when (graphType) {
          "Directed" -> DirectedGraph()
          "Neo4j" -> neo4j.loadGraph()
          else -> UndirectedGraph()
        }
      }
    } else {
      sampleGraph?.let {
        MainScreen(MainScreenViewModel(it, CircularPlacementStrategy()))
      }
    }
  }
}

fun main() = application {
  Window(onCloseRequest = ::exitApplication) {
    App()
  }
}