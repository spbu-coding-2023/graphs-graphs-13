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

  override fun removeEdge(v: Pair<Int, Int>, w: Int?) {

    if (!vertices.containsKey(v.first)) {
      throw IllegalArgumentException("Vertex with id: ${v.first} not exists in the graph.")
    }
    if (!vertices.containsKey(v.second)) {
      throw IllegalArgumentException("Vertex with id: ${v.second} not exists in the graph.")
    }
    if (v.second !in adjacency[v.first]!!.keys) {
      throw IllegalArgumentException("Edge from ${v.first} to ${v.second} not exists in the graph.")
    }

    edges.remove(Edge(Pair(v.first, v.second), w))
    adjacency[v.first]!!.remove(v.second)

  }

  override fun removeVertex(id: Int) {
    if (!vertices.containsKey(id)) {
      throw IllegalArgumentException("Vertex with id: $id doesn't exist in the graph.")
    }
    if(adjacency[id]!!.isNotEmpty()){
      for(idAdjacency in adjacency[id]!!.keys) {
        removeEdge(id to idAdjacency, adjacency[id]!![idAdjacency])
      }
    }
    if(adjacency.filterValues{ it.keys.contains(id)}.isNotEmpty()){
      for(adj in adjacency.filterValues{ it.keys.contains(id)}) {
        removeEdge(adj.key to id, adjacency[adj.key]!![id])
      }
    }
    vertices.remove(id)
  }

}
