package model.algorithms

import model.graph.*

class MST<D>(private val graph: UndirectedGraph<D>) { /
    /**get the graph, make from it the MST and return it*/
    fun getMST(): UndirectedGraph<D> {

        val newGraph = UndirectedGraph<D>()
        var minWeight: Int
        var addedVertex: Vertex<D>? = null
        var pairForAddedEdge: Pair<Int, Int>? = null
        for (vertexId in graph.vertices.keys) { // добавили рондомную вершинку
            graph.vertices[vertexId]?.let { newGraph.addVertex(vertexId, it.data) }
            break
        }
        while (newGraph.vertices.size != graph.vertices.size) {
            minWeight =
                Int.MAX_VALUE // в нашем приложении должно быть ограничение на значение веса ребра : < Int.MAX_VALUE
            for (vertex in newGraph.vertices) {
                for (edge in graph.adjacency[vertex.key]!!) {
                    if (!(graph.vertices[edge.key] in newGraph.vertices.values && vertex.value in newGraph.vertices.values)) { //избегаем появления циклов в новом графе, т.к. он должен быть деревом
                        if (edge.value == null) {
                            throw IllegalArgumentException ("Exists the edge without weight")
                        } else if (edge.value!! <= minWeight) {
                            minWeight = edge.value!!
                            pairForAddedEdge = vertex.key to edge.key
                            addedVertex = graph.vertices[edge.key]
                        }
                    }
                }
            }
            newGraph.addVertex(addedVertex!!.id, addedVertex.data)
            newGraph.addEdge(pairForAddedEdge!!, minWeight)
        }

        return newGraph
    }

}
