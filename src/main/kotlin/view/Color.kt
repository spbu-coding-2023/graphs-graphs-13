package view

import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

//TODO: make more suitable colors
val DarkRed = Color(125, 21, 21)
val LightGray = Color(199, 199, 199)
val DarkGray = Color(74, 74, 74)
val Gray = Color(150, 150, 150)
val White = Color(245, 245, 245)
val Black = Color(0, 0, 0)
val DarkRed2 = Color(41, 28, 28)
val LightRed = Color(204, 0, 0)

val NastyaColorPalette = lightColorScheme(
)

val KatyaColorPalette = lightColorScheme(
  primary = DarkRed,
  onPrimary = White,
  primaryContainer = LightGray,
  onPrimaryContainer = Black,
  secondary = DarkGray,
  onSecondary = White,
  error = LightRed,
  onError = Black,
  background = LightGray,
  onBackground = DarkGray,
  surface = Gray,
  onSurface = DarkRed2,
  outline = DarkRed2
)

val LiyaColorPalette = lightColorScheme(
)