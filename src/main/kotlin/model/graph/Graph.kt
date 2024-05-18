package model.graph

class Graph<D> {
  var isDirected: Boolean = false
  val vertices = mutableListOf<Vertex<D>>()
  val edges = mutableListOf<Edge<D>>()

  fun addVertex(d: D) = vertices.add(Vertex(d))
  fun addEdge(v: Pair<D, D>, w: Int?) = edges.add(Edge(Pair(Vertex(v.first), Vertex(v.second)), w))
