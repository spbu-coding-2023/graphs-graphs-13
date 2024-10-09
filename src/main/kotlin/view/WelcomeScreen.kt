package view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@Composable
fun WelcomeScreen(selectType: (String) -> Unit) {
  Surface(modifier = Modifier.fillMaxSize()) {
    Column {
      Text("Welcome to the Graph Application")
      Button(onClick = { selectType("Undirected") }) {
        Text("Select Undirected graph")
      }
      Button(onClick = { selectType("Directed") }) {
        Text("Select Directed graph")
      }
      Button(onClick = { selectType("Neo4j") }) {
        Text("Open graph from Neo4j")
      }
      Button(onClick = { selectType("File") }) {
        Text("Open graph from json-file")
      }
    }
  }
}
