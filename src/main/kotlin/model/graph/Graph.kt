package model.graph

abstract class Graph<D> {
  val adjacency = hashMapOf<Int, HashMap<Int, Int?>>() //
  val vertices = hashMapOf<Int, Vertex<D>>()
  val edges = mutableListOf<Edge<D>>()
  fun getVertices(): Collection<Vertex<D>> = vertices.values
  fun addVertex(id: Int, data: D) {
    if (vertices.containsKey(id)) {
      throw IllegalArgumentException("Vertex with id: $id already exists in the graph.")
    }
    vertices[id] = Vertex(id, data)
  }
}

