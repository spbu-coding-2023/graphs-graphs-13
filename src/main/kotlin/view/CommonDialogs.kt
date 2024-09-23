package view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

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

@Composable
fun AddVertexDialog(onDismiss: () -> Unit, onRunAlgorithm: (Int, String) -> Unit) {
  var id by remember { mutableStateOf("") }
  var data by remember { mutableStateOf("") }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Text("Enter id and data vertex")
    },
    text = {
      Column(modifier = Modifier.padding(16.dp)) {
        TextField(
          value = id,
          onValueChange = { id = it },
          label = { Text("Enter the id:") },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
          modifier = Modifier.padding(bottom = 12.dp)
        )
        TextField(
          value = data,
          onValueChange = { data = it },
          label = { Text("Enter the data:") },
          modifier = Modifier.padding(bottom = 12.dp)
        )
      }
    },
    confirmButton = {
      Button(
        onClick = {
          val idInt = id.toIntOrNull()

          if (idInt != null) {
            onRunAlgorithm(idInt, data)
          }
        },
        colors = ButtonDefaults.buttonColors(backgroundColor = Color(139, 0, 0))
      ) {
        Text("Add the vertex", color = Color(255, 250, 250))
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

@Composable
fun RemoveVertexDialog(onDismiss: () -> Unit, onRunAlgorithm: (Int) -> Unit) {
  var id by remember { mutableStateOf("") }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Text("Enter id vertex")
    },
    text = {
      Column(modifier = Modifier.padding(16.dp)) {
        TextField(
          value = id,
          onValueChange = { id = it },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
          modifier = Modifier.padding(bottom = 12.dp)
        )
      }
    },
    confirmButton = {
      Button(
        onClick = {
          val idInt = id.toIntOrNull()

          if (idInt != null) {
            onRunAlgorithm(idInt)
          }
        },
        colors = ButtonDefaults.buttonColors(backgroundColor = Color(139, 0, 0))
      ) {
        Text("Remove the vertex", color = Color(255, 250, 250))
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

@Composable
fun AddEdgeDialog(onDismiss: () -> Unit, onRunAlgorithm: (Int, Int, Int?) -> Unit) {
  var from by remember { mutableStateOf("") }
  var to by remember { mutableStateOf("") }
  var w by remember { mutableStateOf<String?>(null) }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Text("Enter edge parameters")
    },
    text = {
      Column(modifier = Modifier.padding(16.dp)) {
        TextField(
          value = from,
          onValueChange = { from = it },
          label = { Text("Enter the start id:") },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
          modifier = Modifier.padding(bottom = 12.dp)
        )
        TextField(
          value = to,
          onValueChange = { to = it },
          label = { Text("Enter the end id:") },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
          modifier = Modifier.padding(bottom = 12.dp)
        )
        TextField(
          value = w ?: "",
          onValueChange = { newValue ->
            w = newValue.ifEmpty { null }
          },
          label = { Text("Enter the weight (optional):") },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
          modifier = Modifier.padding(bottom = 12.dp)
        )
      }
    },
    confirmButton = {
      Button(
        onClick = {
          val fromInt = from.toIntOrNull()
          val toInt = to.toIntOrNull()
          val wInt = w?.toIntOrNull()

          if (fromInt != null && toInt != null) {
            onRunAlgorithm(fromInt, toInt, wInt)
          }
        },
        colors = ButtonDefaults.buttonColors(backgroundColor = Color(139, 0, 0))
      ) {
        Text("Add the edge", color = Color(255, 250, 250))
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

@Composable
fun RemoveEdgeDialog(onDismiss: () -> Unit, onRunAlgorithm: (Int, Int) -> Unit) {
  var from by remember { mutableStateOf("") }
  var to by remember { mutableStateOf("") }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Text("Enter edge parameters")
    },
    text = {
      Column(modifier = Modifier.padding(16.dp)) {
        TextField(
          value = from,
          onValueChange = { from = it },
          label = { Text("Enter the start id:") },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
          modifier = Modifier.padding(bottom = 12.dp)
        )
        TextField(
          value = to,
          onValueChange = { to = it },
          label = { Text("Enter the end id:") },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
          modifier = Modifier.padding(bottom = 12.dp)
        )
      }
    },
    confirmButton = {
      Button(
        onClick = {
          val fromInt = from.toIntOrNull()
          val toInt = to.toIntOrNull()

          if (fromInt != null && toInt != null) {
            onRunAlgorithm(fromInt, toInt)
          }
        },
        colors = ButtonDefaults.buttonColors(backgroundColor = Color(139, 0, 0))
      ) {
        Text("Remove the edge", color = Color(255, 250, 250))
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
