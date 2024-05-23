package model.graph

class UndirectedGraph<D>() : Graph<D>() {
  fun addEdge(v: Pair<Int, Int>, w: Int?) {
    if (!vertices.containsKey(v.first)) {
      throw IllegalArgumentException("Vertex with id: ${v.first} not exists in the graph.")
    }
    if (!vertices.containsKey(v.second)) {
      throw IllegalArgumentException("Vertex with id: ${v.second} not exists in the graph.")
    }
    if (v.first == v.second) {
      throw IllegalArgumentException("Can't add edge from vertex to itself.")
    }
    // Переделать этот кринж...!!
//    if (!(adjacency.containsKey(vertices[v.first]) && adjacency[vertices[v.first]]!!.containsKey(vertices[v.second]))) {
//      throw IllegalArgumentException("The edge between vertices ${v.first} and ${v.second} already exists in the graph.")
//    }
    edges.add(Edge(Pair(v.first, v.second), w))
    edges.add(Edge(Pair(v.second, v.first), w))
    adjacency.getOrPut(v.first) { hashMapOf() }[v.second] = w
    adjacency.getOrPut(v.second) { hashMapOf() }[v.first] = w
  }
}