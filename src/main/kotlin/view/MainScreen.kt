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
import org.jetbrains.skia.impl.Stats.enabled
import view.graph.GraphView
import viewmodel.MainScreenViewModel
import viewmodel.graph.CircularPlacementStrategy
import viewmodel.graph.GraphViewModel

@Composable
fun <D> MainScreen(viewModel: MainScreenViewModel<D>) {
  var expandedAlgorithmsMenu by remember { mutableStateOf(false) }
  var expandedAddMenu by remember { mutableStateOf(false) }
  var expandedRemoveMenu by remember { mutableStateOf(false) }

  var showDijkstraDialog by remember { mutableStateOf(false) }
  var showAddEdgeDialog by remember { mutableStateOf(false) }
  var showAddVertexDialog by remember { mutableStateOf(false) }
  var showRemoveVertexDialog by remember { mutableStateOf(false) }
  var showRemoveEdgeDialog by remember { mutableStateOf(false) }

  Row(
    horizontalArrangement = Arrangement.spacedBy(20.dp)
  ) {
    Column(modifier = Modifier.width(300.dp).fillMaxHeight().background(Color.Gray)) {
      Row {
        Checkbox(checked = viewModel.showVerticesLabels.value, onCheckedChange = {
          viewModel.showVerticesLabels.value = it
        }, colors = CheckboxDefaults.colors(
          checkedColor = Color(125, 21, 21)
        ))
        Text("Show vertices data", fontSize = 18.sp, modifier = Modifier.padding(10.dp))
      }
      Row {
        Checkbox(checked = viewModel.showEdgesLabels.value, onCheckedChange = {
          viewModel.showEdgesLabels.value = it
        }, colors = CheckboxDefaults.colors(
          checkedColor = Color(125, 21, 21)
        ))
        Text("Show edges weight", fontSize = 18.sp, modifier = Modifier.padding(10.dp))
      }
      Button(
        onClick = viewModel::resetGraphView,
        enabled = true,
      ) {
        Text(
          text = "Reset default settings", fontSize = 20.sp, modifier = Modifier.padding(4.dp)
        )
      }
      Box {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Button(onClick = { expandedAlgorithmsMenu = true }) {
            Text("Algorithm")
            Icon(Icons.Default.ArrowDropDown, contentDescription = "Select algorithm")
          }
        }
        DropdownMenu(
          expanded = expandedAlgorithmsMenu,
          onDismissRequest = { expandedAlgorithmsMenu = false },
          modifier = Modifier.background(Color.Gray)
        ) {
          DropdownMenuItem(onClick = {
            expandedAlgorithmsMenu = false
            showDijkstraDialog = true
          }) {
            Text("Dijkstra")
          }
          DropdownMenuItem(onClick = {
            expandedAlgorithmsMenu = false
            viewModel.runKosarajuAlgorithm()
          }) {
            Text("Kosaraju")
          }
          DropdownMenuItem(onClick = {
            expandedAlgorithmsMenu = false
            viewModel.runLouvainAlgorithm()
          }) {
            Text("Louvain")
          }
          DropdownMenuItem(onClick = {
            expandedAlgorithmsMenu = false
            viewModel.runPrimAlgorithm()
          }) {
            Text("Prim")
          }
        }
      }
      Row {
        Box(modifier = Modifier.padding(2.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Button(onClick = { expandedAddMenu = true }) {
              Text("Add")
              Icon(Icons.Default.ArrowDropDown, contentDescription = "Add")
            }
          }
          DropdownMenu(
            expanded = expandedAddMenu,
            onDismissRequest = { expandedAddMenu = false },
            modifier = Modifier.background(Color.Gray)
          ) {
            DropdownMenuItem(onClick = {
              expandedAddMenu = false
              showAddVertexDialog = true
            }) {
              Text("Add vertex")
            }
            DropdownMenuItem(onClick = {
              expandedAddMenu = false
              showAddEdgeDialog = true
            }) {
              Text("Add edge")
            }
          }
        }
        Box(modifier = Modifier.padding(2.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Button(onClick = { expandedRemoveMenu = true }) {
              Text("Remove")
              Icon(Icons.Default.ArrowDropDown, contentDescription = "Remove")
            }
          }
          DropdownMenu(
            expanded = expandedRemoveMenu,
            onDismissRequest = { expandedRemoveMenu = false },
            modifier = Modifier.background(Color.Gray)
          ) {
            DropdownMenuItem(onClick = {
              expandedRemoveMenu = false
              showRemoveVertexDialog = true
            }) {
              Text("Remove vertex")
            }
            DropdownMenuItem(onClick = {
              expandedRemoveMenu = false
              showRemoveEdgeDialog = true
            }) {
              Text("Remove edge")
            }
          }
        }
      }
    }
    Box {
      var expandedThemeMenu by remember { mutableStateOf(false) }
      Button(
        onClick = { expandedThemeMenu = true },
        modifier = Modifier.align(Alignment.TopEnd)
      ) {
        Text("Theme")
        Icon(Icons.Default.ArrowDropDown, contentDescription = "Select theme")
      }
      DropdownMenu(
        expanded = expandedThemeMenu,
        onDismissRequest = { expandedThemeMenu = false },
        modifier = Modifier.background(Color.Red)
      ) {
        DropdownMenuItem(onClick = {
          expandedThemeMenu = false
        }) {
          Text("Nastya's theme")
        }
        DropdownMenuItem(onClick = {
          expandedThemeMenu = false
        }) {
          Text("Liya's theme")
        }
        DropdownMenuItem(onClick = {
          expandedThemeMenu = false
        }) {
          Text("Katya's theme")
        }
      }
    }
    Surface(
      modifier = Modifier.weight(1f),
    ) {
      GraphView(viewModel.graphViewModel)
    }
  }
  if (showDijkstraDialog) {
    DijkstraDialog(
      onDismiss = { showDijkstraDialog = false },
      onRunAlgorithm = { start, end ->
        viewModel.runDijkstraAlgorithm(start, end)
        showDijkstraDialog = false
      }
    )
  }
  if (showAddVertexDialog) {
    AddVertexDialog(
      onDismiss = { showAddVertexDialog = false },
      onRunAlgorithm = { id, data ->
        viewModel.addVertex(id, data as D)
        showAddVertexDialog = false
      }
    )
  }
  if (showRemoveVertexDialog) {
    RemoveVertexDialog(
      onDismiss = { showRemoveVertexDialog = false },
      onRunAlgorithm = { id ->
        viewModel.removeVertex(id)
        showRemoveVertexDialog = false
      }
    )
  }
  if (showAddEdgeDialog) {
    AddEdgeDialog(
      onDismiss = { showAddEdgeDialog = false },
      onRunAlgorithm = { from, to, w ->
        viewModel.addEdge(from, to, w)
        showAddEdgeDialog = false
      }
    )
  }
  if (showRemoveEdgeDialog) {
    RemoveEdgeDialog(
      onDismiss = { showRemoveEdgeDialog = false },
      onRunAlgorithm = { from, to ->
        viewModel.removeEdge(from, to)
        showRemoveEdgeDialog = false
      }
    )
  }
}
