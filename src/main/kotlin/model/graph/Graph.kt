package model.graph

abstract class Graph {
  val adjacency = hashMapOf<Int, HashMap<Int, Int?>>()
  val vertices = hashMapOf<Int, Vertex>()
  val edges = mutableListOf<Edge>()

  fun getVertices(): Collection<Vertex> = vertices.values
  abstract fun addEdge(v: Pair<Int, Int>, w: Int?)
  abstract fun removeEdge(v: Pair<Int, Int>)
  abstract fun removeVertex(id: Int)
  fun addVertex(id: Int, data: String) {
    if (vertices.containsKey(id)) {
      throw IllegalArgumentException("Vertex with id: $id already exists in the graph.")
    }
    vertices[id] = Vertex(id, data)
    adjacency[id] = hashMapOf()
  }

}
