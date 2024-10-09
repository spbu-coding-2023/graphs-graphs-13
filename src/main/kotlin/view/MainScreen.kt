package view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import model.graph.Graph
import androidx.compose.material3.MaterialTheme
import databases.FileSystem
import org.jetbrains.skia.impl.Stats.enabled
import view.graph.GraphView
import viewmodel.MainScreenViewModel
import viewmodel.graph.CircularPlacementStrategy
import viewmodel.graph.GraphViewModel

@Composable
fun MainScreen(viewModel: MainScreenViewModel) {
  var theme by remember { mutableStateOf(Theme.NASTYA) }
  var expandedAlgorithmsMenu by remember { mutableStateOf(false) }
  var expandedAddMenu by remember { mutableStateOf(false) }
  var expandedRemoveMenu by remember { mutableStateOf(false) }
  var expandedSaveMenu by remember { mutableStateOf(false) }

  var showDijkstraDialog by remember { mutableStateOf(false) }
  var showCycleSearchDialog by remember { mutableStateOf(false) }
  var showAddEdgeDialog by remember { mutableStateOf(false) }
  var showAddVertexDialog by remember { mutableStateOf(false) }
  var showRemoveVertexDialog by remember { mutableStateOf(false) }
  var showRemoveEdgeDialog by remember { mutableStateOf(false) }
  var showNeo4jDialog by remember { mutableStateOf(false) }
  var showErrorDialog by remember { mutableStateOf(false) }
  var errorMessage: String? = null
  fun catchError(messageOfError: String? ) {
    if(messageOfError != null ) {
      errorMessage = messageOfError
      showErrorDialog = true
    }
  }

  Material3AppTheme(theme) {
    Row(
      horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
      Column(modifier = Modifier.width(300.dp).fillMaxHeight().background(MaterialTheme.colorScheme.surface)) {
        Row {
          Checkbox(
            checked = viewModel.showVerticesLabels.value, onCheckedChange = {
              viewModel.showVerticesLabels.value = it
            }, colors = CheckboxDefaults.colors(
              checkedColor = MaterialTheme.colorScheme.primary
            )
          )
          Text(
            text = "Show vertices data",
            fontSize = 18.sp,
            modifier = Modifier.padding(10.dp),
            color = MaterialTheme.colorScheme.onSurface
          )
        }
        Row {
          Checkbox(
            checked = viewModel.showEdgesLabels.value, onCheckedChange = {
              viewModel.showEdgesLabels.value = it
            }, colors = CheckboxDefaults.colors(
              checkedColor = MaterialTheme.colorScheme.primary
            )
          )
          Text(
            text = "Show edges weight",
            fontSize = 18.sp,
            modifier = Modifier.padding(10.dp),
            color = MaterialTheme.colorScheme.onSurface
          )
        }
        Button(
          onClick = viewModel::resetGraphView,
          enabled = true,
          colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
          )
        ) {
          Text(
            text = "Reset default settings",
            fontSize = 20.sp,
            modifier = Modifier.padding(4.dp),
            color = MaterialTheme.colorScheme.onPrimary
          )
        }
        Box {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Button(
              onClick = { expandedAlgorithmsMenu = true }, colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
              )
            ) {
              Text(text = "Algorithm", color = MaterialTheme.colorScheme.onPrimary)
              Icon(Icons.Default.ArrowDropDown, contentDescription = "Select algorithm")
            }
          }
          DropdownMenu(
            expanded = expandedAlgorithmsMenu,
            onDismissRequest = { expandedAlgorithmsMenu = false },
            modifier = Modifier.background(MaterialTheme.colorScheme.secondary)
          ) {
            DropdownMenuItem(onClick = {
              expandedAlgorithmsMenu = false
              showDijkstraDialog = true
            }) {
              Text(text = "Dijkstra", color = MaterialTheme.colorScheme.onSecondary)
            }
            DropdownMenuItem(onClick = {
              expandedAlgorithmsMenu = false
              catchError(viewModel.runKosarajuAlgorithm())
            }) {
              Text(text = "Kosaraju", color = MaterialTheme.colorScheme.onSecondary)
            }
            DropdownMenuItem(onClick = {
              expandedAlgorithmsMenu = false
              viewModel.runLouvainAlgorithm()
            }) {
              Text(text = "Louvain", color = MaterialTheme.colorScheme.onSecondary)
            }
            DropdownMenuItem(onClick = {
              expandedAlgorithmsMenu = false
              catchError(viewModel.runPrimAlgorithm())
            }) {
              Text(text = "Prim", color = MaterialTheme.colorScheme.onSecondary)
            }
            DropdownMenuItem(onClick = {
              expandedAlgorithmsMenu = false
              showCycleSearchDialog = true
            }) {
              Text(text = "CycleSearch", color = MaterialTheme.colorScheme.onSecondary)
            }
          }
        }
        Row {
          Box(modifier = Modifier.padding(2.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Button(
                onClick = { expandedAddMenu = true }, colors = ButtonDefaults.buttonColors(
                  backgroundColor = MaterialTheme.colorScheme.primary,
                  contentColor = MaterialTheme.colorScheme.onPrimary
                )
              ) {
                Text(text = "Add", color = MaterialTheme.colorScheme.onPrimary)
                Icon(Icons.Default.ArrowDropDown, contentDescription = "Add")
              }
            }
            DropdownMenu(
              expanded = expandedAddMenu,
              onDismissRequest = { expandedAddMenu = false },
              modifier = Modifier.background(MaterialTheme.colorScheme.secondary)
            ) {
              DropdownMenuItem(onClick = {
                expandedAddMenu = false
                showAddVertexDialog = true
              }) {
                Text("Add vertex", color = MaterialTheme.colorScheme.onSecondary)
              }
              DropdownMenuItem(onClick = {
                expandedAddMenu = false
                showAddEdgeDialog = true
              }) {
                Text("Add edge", color = MaterialTheme.colorScheme.onSecondary)
              }
            }
          }
          Box(modifier = Modifier.padding(2.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Button(
                onClick = { expandedRemoveMenu = true }, colors = ButtonDefaults.buttonColors(
                  backgroundColor = MaterialTheme.colorScheme.primary,
                  contentColor = MaterialTheme.colorScheme.onPrimary
                )
              ) {
                Text(text = "Remove", color = MaterialTheme.colorScheme.onPrimary)
                Icon(Icons.Default.ArrowDropDown, contentDescription = "Remove")
              }
            }
            DropdownMenu(
              expanded = expandedRemoveMenu,
              onDismissRequest = { expandedRemoveMenu = false },
              modifier = Modifier.background(MaterialTheme.colorScheme.secondary)
            ) {
              DropdownMenuItem(onClick = {
                expandedRemoveMenu = false
                showRemoveVertexDialog = true
              }) {
                Text("Remove vertex", color = MaterialTheme.colorScheme.onSecondary)
              }
              DropdownMenuItem(onClick = {
                expandedRemoveMenu = false
                showRemoveEdgeDialog = true
              }) {
                Text("Remove edge", color = MaterialTheme.colorScheme.onSecondary)
              }
            }
          }
        }
        Box(modifier = Modifier.padding(2.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Button(
              onClick = { expandedSaveMenu = true }, colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
              )
            ) {
              Text(text = "Save graph", color = MaterialTheme.colorScheme.onPrimary)
              Icon(Icons.Default.ArrowDropDown, contentDescription = "Save graph")
            }
          }
          DropdownMenu(
            expanded = expandedSaveMenu,
            onDismissRequest = { expandedSaveMenu = false },
            modifier = Modifier.background(MaterialTheme.colorScheme.secondary)
          ) {
            DropdownMenuItem(onClick = {
              expandedSaveMenu = false
              showNeo4jDialog = true
            }) {
              Text("Save to Neo4j", color = MaterialTheme.colorScheme.onSecondary)
            }
            DropdownMenuItem(onClick = {
              expandedSaveMenu = false
              catchError(viewModel.saveToFile())
            }) {
              Text("Save to json-file", color = MaterialTheme.colorScheme.onSecondary)
            }
          }
        }
      }
      Box {
        var expandedThemeMenu by remember { mutableStateOf(false) }
        Button(
          onClick = { expandedThemeMenu = true },
          modifier = Modifier.align(Alignment.TopEnd), colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
          )
        ) {
          Text(text = "Theme", color = MaterialTheme.colorScheme.onPrimary)
          Icon(Icons.Default.ArrowDropDown, contentDescription = "Select theme")
        }
        DropdownMenu(
          expanded = expandedThemeMenu,
          onDismissRequest = { expandedThemeMenu = false },
          modifier = Modifier.background(MaterialTheme.colorScheme.secondary)
        ) {
          DropdownMenuItem(onClick = {
            theme = Theme.NASTYA
            expandedThemeMenu = false
          }) {
            Text("Nastya's theme", color = MaterialTheme.colorScheme.onSecondary)
          }
          DropdownMenuItem(onClick = {
            theme = Theme.LIYA
            expandedThemeMenu = false
          }) {
            Text("Liya's theme", color = MaterialTheme.colorScheme.onSecondary)
          }
          DropdownMenuItem(onClick = {
            theme = Theme.KATYA
            expandedThemeMenu = false
          }) {
            Text("Katya's theme", color = MaterialTheme.colorScheme.onSecondary)
          }
        }
      }
      Surface(
        modifier = Modifier.weight(1f).background(MaterialTheme.colorScheme.background),
      ) {
        GraphView(viewModel.graphViewModel)
      }
    }
    if (showDijkstraDialog) {
      DijkstraDialog(
        onDismiss = { showDijkstraDialog = false },
        onRunAlgorithm = { start, end ->
          catchError(viewModel.runDijkstraAlgorithm(start, end))
          showDijkstraDialog = false
        }
      )
    }
    if (showAddVertexDialog) {
      AddVertexDialog(
        onDismiss = { showAddVertexDialog = false },
        onRunAlgorithm = { id, data ->
          catchError(viewModel.addVertex(id, data))
          showAddVertexDialog = false
        }
      )
    }
    if (showCycleSearchDialog) {
      CycleSearchDialog(
        onDismiss = { showCycleSearchDialog = false },
        onRunAlgorithm = { vertexId ->
          catchError(viewModel.runCycleSearchAlgorithm(vertexId))
          showCycleSearchDialog = false
        }
      )
    }
    if (showRemoveVertexDialog) {
      RemoveVertexDialog(
        onDismiss = { showRemoveVertexDialog = false },
        onRunAlgorithm = { id ->
          catchError(viewModel.removeVertex(id))
          showRemoveVertexDialog = false
        }
      )
    }
    if (showAddEdgeDialog) {
      AddEdgeDialog(
        onDismiss = { showAddEdgeDialog = false },
        onRunAlgorithm = { from, to, w ->
          catchError(viewModel.addEdge(from, to, w))
          showAddEdgeDialog = false
        }
      )
    }
    if (showRemoveEdgeDialog) {
      RemoveEdgeDialog(
        onDismiss = { showRemoveEdgeDialog = false },
        onRunAlgorithm = { from, to ->
          catchError(viewModel.removeEdge(from, to))
          showRemoveEdgeDialog = false
        }
      )
    }
    if (showNeo4jDialog) {
      Neo4jDialog(
        onDismiss = { showNeo4jDialog = false },
        onRunAlgorithm = { uri, user, password ->
          catchError(viewModel.saveToNeo4j(uri, user, password))
          showNeo4jDialog = false
        }
      )
    }
    if (showErrorDialog) {
      ErrorDialog(
        onDismiss = { showErrorDialog = false },
        errorMessage!!
      )
    }
  }
}