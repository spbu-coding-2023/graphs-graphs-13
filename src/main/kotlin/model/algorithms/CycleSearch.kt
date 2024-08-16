package model.algorithms

import model.graph.Edge
import model.graph.UndirectedGraph
import model.graph.Vertex

class CycleSearch<D>(private val graph: UndirectedGraph<D>) {

    private fun getCyclePath(cyclePath: HashMap<Int, Int>, cycleVertexId: Int): HashMap<Int, Int> {

        var current: Int = cycleVertexId
        var next: Int
        val returnCyclePath = HashMap<Int, Int>()
        do {
            next = cyclePath.filter { it.key == current }.values.first()
            returnCyclePath[current] = next
            current = next
        } while (current != cycleVertexId)

        return returnCyclePath

    }

    private fun dfs(
        cycleVertexId: Int,
        currentVertexId: Int,
        visited: HashMap<Int, Boolean>,
        cyclePath: HashMap<Int, Int>
    ) {

        for (idAdjacency in graph.adjacency[currentVertexId]!!.keys) {

            if (visited[idAdjacency] == false) {
                visited[idAdjacency] = true
                cyclePath[currentVertexId] = idAdjacency
                dfs(cycleVertexId, idAdjacency, visited, cyclePath)
            } else if (idAdjacency == cycleVertexId && cyclePath[cycleVertexId] != currentVertexId) {
                cyclePath[currentVertexId] = idAdjacency
                return
            }
        }
    }

    private fun findAnyCycle(vertexId: Int): HashMap<Int, Int>? {

        val devCyclePath = HashMap<Int, Int>()
        val visited = hashMapOf<Int, Boolean>()
        for (idVertex in graph.vertices.keys) {
            visited[idVertex] = false
        }
        visited[vertexId] = true
        dfs(vertexId, vertexId, visited, devCyclePath)

        if (devCyclePath.filter { it.value == vertexId }.isEmpty()) {
            return null
        }
        return getCyclePath(devCyclePath, vertexId)

    }
    
    fun findCycle(vertex: Vertex<D>): UndirectedGraph<D>? {

        var returnCyclePath = hashMapOf<Int, Int>()
        var currentCyclePath: HashMap<Int, Int>?
        var minCycleSize: Int = Int.MAX_VALUE

        if (vertex.id !in graph.vertices.keys) {
            throw IllegalArgumentException("Vertex with id = ${vertex.id} doesn't exist in the graph")
        }
        val returnGraph = UndirectedGraph<D>()
        if (graph.adjacency[vertex.id]!!.size < 2) {
            return null
        } else { // we consider all possible cases of a cycle by choosing a pair of neighbors to minimize the found cycle
            val adjacencyOfVertex: MutableList<Int> = arrayListOf()
            graph.adjacency[vertex.id]!!.keys.forEach { adjacencyOfVertex.add(it) }
            val adjacencyWas: MutableList<Int> = arrayListOf()
            val removedEdges: MutableList<Edge<Int>> = arrayListOf()
            for (firstAdjacency in adjacencyOfVertex) {
                adjacencyWas.add(firstAdjacency)
                for (secondAdjacency in adjacencyOfVertex.filter { it !in adjacencyWas && it != vertex.id }) {

                    for (adjacency in graph.adjacency[vertex.id]!!.filter { it.key != firstAdjacency && it.key != secondAdjacency }) {
                        removedEdges.add(Edge(vertex.id to adjacency.key, adjacency.value))
                        graph.removeEdge(vertex.id to adjacency.key, adjacency.value)
                    }

                    currentCyclePath = findAnyCycle(vertex.id)
                    if (currentCyclePath != null && currentCyclePath.size < minCycleSize) {
                        minCycleSize = currentCyclePath.size
                        returnCyclePath = currentCyclePath
                    }

                    for (edge in removedEdges) {
                        graph.addEdge(edge.vertices, edge.weight)
                    }
                    removedEdges.clear()
                }
            }
        }

        if (minCycleSize == Int.MAX_VALUE) {
            return null
        }
        returnCyclePath.forEach { returnGraph.addVertex(it.key, graph.vertices[it.key]!!.data) }
        returnCyclePath.forEach { returnGraph.addEdge(it.key to it.value, graph.adjacency[it.key]!![it.value]) }
        return returnGraph

    }

}
