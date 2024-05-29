package view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
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