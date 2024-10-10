import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import databases.FileSystem
import databases.Neo4jRepository
import model.graph.DirectedGraph
import model.graph.Graph
import model.graph.UndirectedGraph
import view.ErrorDialog
import view.MainScreen
import view.Neo4jDialog
import view.WelcomeScreen
import viewmodel.MainScreenViewModel
import viewmodel.graph.CircularPlacementStrategy

@Composable
@Preview
fun App() {
  MaterialTheme {
    val fileSystem = FileSystem()
    var graphType by remember { mutableStateOf<String?>(null) }
    var graph by remember { mutableStateOf<Graph?>(null) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var showNeo4jDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    fun catchErrorOrGetGraph(result: Pair<Graph?, String?>) {
      if (result.second == null) {
        graph = result.first
      } else {
        errorMessage = result.second
        showErrorDialog = true
      }
    }

    if (graph == null) {
      WelcomeScreen { selectedGraphType ->
        graphType = selectedGraphType
        when (graphType) {
          "Directed" -> graph = DirectedGraph()
          "Undirected" -> graph = UndirectedGraph()
          "Neo4j" -> showNeo4jDialog = true
          else -> catchErrorOrGetGraph(fileSystem.openGraph())
        }
      }
    } else {
      graph?.let {
        MainScreen(MainScreenViewModel(it, CircularPlacementStrategy()))
      }
    }
    if (showErrorDialog) {
      ErrorDialog(
        onDismiss = { showErrorDialog = false },
        errorMessage!!
      )
    }
    if (showNeo4jDialog) {
      Neo4jDialog(
        onDismiss = { showNeo4jDialog = false },
        onRunAlgorithm = { uri, user, password ->
          val neo4j = Neo4jRepository(uri, user, password)
          catchErrorOrGetGraph(neo4j.loadGraph())
          showNeo4jDialog = false
        }
      )
    }
  }
}

fun main() = application {
  Window(onCloseRequest = ::exitApplication) {
    App()
  }
}