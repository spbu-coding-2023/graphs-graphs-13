package model.graph

class Graph<D> {
  var isDirected: Boolean = false
  val vertices: MutableList<Vertex<D>> = mutableListOf()
  val edges: MutableList<Edge> = mutableListOf()

  fun addVertex(id: Int, data: D) = vertices.add(Vertex(id, data))
  fun addEdge(v: Pair<Int, Int>, w: Int?) = edges.add(Edge(v, w))
}
