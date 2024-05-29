package model.graph

class DirectedGraph<D>() : Graph<D>() {
  override fun addEdge(v: Pair<Int, Int>, w: Int?) {
    // добавить проверку на повторку ребра
    if (!vertices.containsKey(v.first)) {
      throw IllegalArgumentException("Vertex with id: ${v.first} not exists in the graph.")
    }
    if (!vertices.containsKey(v.second)) {
      throw IllegalArgumentException("Vertex with id: ${v.second} not exists in the graph.")
    }
    edges.add(Edge(Pair(v.first, v.second), w))
    adjacency.getOrPut(v.first) { hashMapOf() }[v.second] = w
  }
}