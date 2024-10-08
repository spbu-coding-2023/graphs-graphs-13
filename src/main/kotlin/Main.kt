import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import databases.FileSystem
import databases.Neo4jRepository
import model.graph.*
import view.ErrorDialog
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
    val fileSystem = FileSystem()
    var graphType by remember { mutableStateOf<String?>(null) }
    var graph by remember { mutableStateOf<Graph?>(null) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    fun catchErrorOrGetGraph(result:Pair<Graph?,String?>) {
      if (result.second == null ) {
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
          "Neo4j" -> graph = neo4j.loadGraph()
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
  }
}

fun main() = application {
  Window(onCloseRequest = ::exitApplication) {
    App()
  }
}