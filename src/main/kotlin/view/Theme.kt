package view

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun Material3AppTheme(theme: Theme, content: @Composable () -> Unit) {
  val colors = when (theme) {
    Theme.NASTYA -> NastyaColorPalette
    Theme.KATYA -> KatyaColorPalette
    else -> LiyaColorPalette
  }
  MaterialTheme(
    colorScheme = colors,
    content = content
  )
}

enum class Theme {
  NASTYA, KATYA, LIYA
}