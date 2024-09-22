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
  var expanded by remember { mutableStateOf(false) }
  var showDialog by remember { mutableStateOf(false) }
  Row(
    horizontalArrangement = Arrangement.spacedBy(20.dp)
  ) {
    Column(modifier = Modifier.width(300.dp).fillMaxHeight().background(Color.Gray)) {
      Row {
        Checkbox(checked = viewModel.showVerticesLabels.value, onCheckedChange = {
          viewModel.showVerticesLabels.value = it
        })
        Text("Show vertices data", fontSize = 20.sp, modifier = Modifier.padding(4.dp))
      }
      Row {
        Checkbox(checked = viewModel.showEdgesLabels.value, onCheckedChange = {
          viewModel.showEdgesLabels.value = it
        })
        Text("Show edges weight", fontSize = 20.sp, modifier = Modifier.padding(4.dp))
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
          Button(onClick = { expanded = true }) {
            Text("Algorithm")
            Icon(Icons.Default.ArrowDropDown, contentDescription = "Select algorithm")
          }
        }
        DropdownMenu(
          expanded = expanded,
          onDismissRequest = { expanded = false },
          modifier = Modifier.background(Color.Green)
        ) {
          DropdownMenuItem(onClick = {
            expanded = false
            showDialog = true
          }) {
            Text("Dijkstra")
          }
          DropdownMenuItem(onClick = {
            expanded = false
          }) {
            Text("Kosaraju")
          }
          DropdownMenuItem(onClick = {
            expanded = false
          }) {
            Text("Louvain")
          }
        }
      }
    }
    if (showDialog) {
      DijkstraDialog(
        onDismiss = { showDialog = false },
        onRunAlgorithm = { start, end ->
          viewModel.runDijkstraAlgorithm(start, end)
          showDialog = false
        }
      )
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
}
@Composable
fun DijkstraDialog(onDismiss: () -> Unit, onRunAlgorithm: (Int, Int) -> Unit) {
  var start by remember { mutableStateOf("") }
  var end by remember { mutableStateOf("") }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Text("Select vertices to find shortest path")
    },
    text = {
      Column(modifier = Modifier.padding(16.dp)) {
        TextField(
          value = start,
          onValueChange = { start = it },
          label = { Text("Enter the id of the starting vertex:") },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
          modifier = Modifier.padding(bottom = 12.dp)
        )
        TextField(
          value = end,
          onValueChange = { end = it },
          label = { Text("Enter the id of the destination vertex:") },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
          modifier = Modifier.padding(bottom = 12.dp)
        )
      }
    },
    confirmButton = {
      Button(
        onClick = {
          val startInt = start.toIntOrNull()
          val endInt = end.toIntOrNull()

          if (startInt != null && endInt != null) {
            onRunAlgorithm(startInt, endInt)
          }
        },
        colors = ButtonDefaults.buttonColors(backgroundColor = Color(139, 0, 0))
      ) {
        Text("Find the way", color = Color(255, 250, 250))
      }
    },
    dismissButton = {
      Button(
        onClick = onDismiss,
        colors = ButtonDefaults.buttonColors(backgroundColor = Color(139, 0, 0))
      ) {
        Text("Cancel", color = Color(255, 250, 250))
      }
    }
  )
}