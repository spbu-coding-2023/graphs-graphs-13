package viewmodel.graph

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin
import kotlin.random.Random

class CircularPlacementStrategy : RepresentationStrategy {
  override fun <D> place(width: Double, height: Double, vertices: Collection<VertexViewModel<D>>) {
    if (vertices.isEmpty()) {
      println("CircularPlacementStrategy.place: there is nothing to place 👐🏻")
      return
    }
    val center = Pair(width / 2, height / 2)
    val angle = 2 * Math.PI / vertices.size

    val sorted = vertices.sortedBy { it.label }
    val first = sorted.first()
    var point = Pair(center.first, center.second - min(width, height) / 2)
    first.x = point.first.dp
    first.y = point.second.dp

    sorted
      .drop(1)
      .onEach {
        point = point.rotate(center, angle)
        it.x = point.first.dp
        it.y = point.second.dp
      }
  }

  override fun <D> highlight(vertices: Collection<VertexViewModel<D>>) {
    val customColor1 = Color(red = 235, green = 82, blue = 132) //235,82,132
    val customColor2 = Color(red = 251, green = 160, blue = 227) //251,160,227
    vertices
      .onEach {
        it.color = if (Random.nextBoolean()) customColor1 else customColor2
      }
  }

  private fun Pair<Double, Double>.rotate(pivot: Pair<Double, Double>, angle: Double): Pair<Double, Double> {
    val sin = sin(angle)
    val cos = cos(angle)

    val diff = first - pivot.first to second - pivot.second
    val rotated = Pair(
      diff.first * cos - diff.second * sin,
      diff.first * sin + diff.second * cos,
    )
    return rotated.first + pivot.first to rotated.second + pivot.second
  }
}