package model.graph

class UndirectedGraph() : Graph() {
  override fun addEdge(v: Pair<Int, Int>, w: Int?): String? {
    if (!vertices.containsKey(v.first)) {
      return "Vertex with id: ${v.first} not exists in the graph."
    }
    if (!vertices.containsKey(v.second)) {
      return "Vertex with id: ${v.second} not exists in the graph."
    }
    if (v.first == v.second) {
      return "Can't add edge from vertex to itself."
    }
    edges.add(Edge(Pair(v.first, v.second), w))
    edges.add(Edge(Pair(v.second, v.first), w))
    adjacency.getOrPut(v.first) { hashMapOf() }[v.second] = w
    adjacency.getOrPut(v.second) { hashMapOf() }[v.first] = w
    return null
  }

  override fun removeEdge(v: Pair<Int, Int>): String? {

    if (!vertices.containsKey(v.first)) {
      return "Vertex with id: ${v.first} not exists in the graph."
    }
    if (!vertices.containsKey(v.second)) {
      return "Vertex with id: ${v.second} not exists in the graph."
    }
    if (v.second !in adjacency[v.first]!!.keys) {
      return "Edge from ${v.first} to ${v.second} not exists in the graph."
    }
    edges.find { it.vertices == Pair(v.first, v.second) }?.let { edge ->
      edges.remove(edge)
    }
    edges.find { it.vertices == Pair(v.second, v.first) }?.let { edge ->
      edges.remove(edge)
    }
    adjacency[v.first]!!.remove(v.second)
    adjacency[v.second]!!.remove(v.first)
    return null
  }

  override fun removeVertex(id: Int): String? {
    if (!vertices.containsKey(id)) {
      return "Vertex with id: $id doesn't exist in the graph."
    }
    if (adjacency[id]!!.isNotEmpty()) {
      for (idAdjacency in adjacency[id]!!.keys) {
        removeEdge(id to idAdjacency)
      }
    }
    vertices.remove(id)
    return null
  }

}
