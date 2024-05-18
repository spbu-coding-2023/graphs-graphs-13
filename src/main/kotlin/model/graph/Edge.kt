package model.graph

data class Edge<D>(val vertices: Pair<Vertex<D>, Vertex<D>>, var weight: Int?) {
  fun incident(v: Vertex<D>) = (v == vertices.first || v == vertices.second)
}
