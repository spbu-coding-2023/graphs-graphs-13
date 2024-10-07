package model.graph

class UndirectedGraph() : Graph() {
  override fun addEdge(v: Pair<Int, Int>, w: Int?) {
    // добавить проверку на повторку ребра
    if (!vertices.containsKey(v.first)) {
      throw IllegalArgumentException("Vertex with id: ${v.first} not exists in the graph.")
    }
    if (!vertices.containsKey(v.second)) {
      throw IllegalArgumentException("Vertex with id: ${v.second} not exists in the graph.")
    }
    if (v.first == v.second) {
      throw IllegalArgumentException("Can't add edge from vertex to itself.")
    }
    edges.add(Edge(Pair(v.first, v.second), w))
    edges.add(Edge(Pair(v.second, v.first), w))
    adjacency.getOrPut(v.first) { hashMapOf() }[v.second] = w
    adjacency.getOrPut(v.second) { hashMapOf() }[v.first] = w
  }

  override fun removeEdge(v: Pair<Int, Int>) {

    if (!vertices.containsKey(v.first)) {
      throw IllegalArgumentException("Vertex with id: ${v.first} not exists in the graph.")
    }
    if (!vertices.containsKey(v.second)) {
      throw IllegalArgumentException("Vertex with id: ${v.second} not exists in the graph.")
    }
    if (v.second !in adjacency[v.first]!!.keys) {
      throw IllegalArgumentException("Edge from ${v.first} to ${v.second} not exists in the graph.")
    }
    edges.find { it.vertices == Pair(v.first, v.second) }?.let { edge ->
      edges.remove(edge)
    }
    edges.find { it.vertices == Pair(v.second, v.first) }?.let { edge ->
      edges.remove(edge)
    }
    adjacency[v.first]!!.remove(v.second)
    adjacency[v.second]!!.remove(v.first)

  }

  override fun removeVertex(id: Int) {
    if (!vertices.containsKey(id)) {
      throw IllegalArgumentException("Vertex with id: $id doesn't exist in the graph.")
    }
    if (adjacency[id]!!.isNotEmpty()) {
      for (idAdjacency in adjacency[id]!!.keys) {
        removeEdge(id to idAdjacency)
      }
    }
    vertices.remove(id)
  }

}
