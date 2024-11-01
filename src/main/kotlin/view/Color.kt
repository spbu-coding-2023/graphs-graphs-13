package view

import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val DarkRed00 = Color(125, 21, 21)
val DarkRed01 = Color(41, 28, 28)
val DarkRed02 = Color(189, 0, 0)
val LightRed = Color(204, 0, 0)
val LightGray = Color(199, 199, 199)
val DarkGray = Color(74, 74, 74)
val Gray = Color(150, 150, 150)
val White = Color(245, 245, 245)
val Black = Color(0, 0, 0)

val NastyaColorPalette = lightColorScheme(
)

val KatyaColorPalette = lightColorScheme(
  primary = DarkRed00,
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
  onSurface = DarkRed01,
  outline = DarkRed01,
  errorContainer = DarkRed02,
  onErrorContainer = Black,
)

val LiyaColorPalette = lightColorScheme(
)