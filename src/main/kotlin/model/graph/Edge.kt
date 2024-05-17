package model.graph

data class Edge(val vertices: Pair<Int, Int>, var weight: Int?) {
  fun incident(v: Int) = (v == vertices.first || v == vertices.second)
}
