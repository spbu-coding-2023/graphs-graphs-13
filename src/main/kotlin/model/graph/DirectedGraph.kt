package model.graph

class DirectedGraph<D>() : Graph<D>() {
  fun addEdge(v: Pair<Int, Int>, w: Int?) {
    if (!vertices.containsKey(v.first)) {
      throw IllegalArgumentException("Vertex with id: ${v.first} not exists in the graph.")
    }
    if (!vertices.containsKey(v.second)) {
      throw IllegalArgumentException("Vertex with id: ${v.second} not exists in the graph.")
    }
    //Переделать этот кринж...!!!
//    if (!(successors.containsKey(vertices[v.first]) && successors[vertices[v.first]]!!.containsKey(vertices[v.second]))) { //refactoring!!!!!!!!!
//      throw IllegalArgumentException("The edge between vertices ${v.first} and ${v.second} already exists in the graph.")
//    }
//    if (!(successors.containsKey(vertices[v.second]) && successors[vertices[v.second]]!!.containsKey(vertices[v.first]) && w != successors[vertices[v.second]]!![vertices[v.first]])) { //refactoring!!!!!!!!!
//      throw IllegalArgumentException("The weight between two vertices in both directions should be the same.")
//    }
    edges.add(Edge(Pair(v.first, v.second), w))
    adjacency.getOrPut(v.first) { hashMapOf() }[v.second] = w
  }
}