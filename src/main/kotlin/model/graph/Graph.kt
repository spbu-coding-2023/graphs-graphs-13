package model.graph

abstract class Graph {
  val adjacency = hashMapOf<Int, HashMap<Int, Int?>>()
  val vertices = hashMapOf<Int, Vertex>()
  val edges = mutableListOf<Edge>()

  fun getVertices(): Collection<Vertex> = vertices.values
  abstract fun addEdge(v: Pair<Int, Int>, w: Int?): String?
  abstract fun removeEdge(v: Pair<Int, Int>): String?
  abstract fun removeVertex(id: Int): String?
  fun addVertex(id: Int, data: String): String? {
    if (vertices.containsKey(id)) {
      return "Vertex with id: $id already exists in the graph."
    }
    vertices[id] = Vertex(id, data)
    adjacency[id] = hashMapOf()
    return null
  }

}
