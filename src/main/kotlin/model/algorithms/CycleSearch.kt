package model.algorithms

import model.graph.Edge
import model.graph.UndirectedGraph
import model.graph.Vertex

/**
 * The class [CycleSearch] implements the algorithm for finding a cycle around
 * selected vertex of the undirected graph.
 *
 * At the same time, the algorithm has some minimization of the desired cycle
 * by iterating through possible pairs of neighbors of the selected vertex
 * through which the desired cycle will pass.
 * @property [graph] a undirected graph for whose vertex we will search for a cycle
 * @constructor Creates a graph, based on [graph],for which it will be possible to apply
 * a cycle search algorithm around the vertex selected in it.
 */

class CycleSearch(private val graph: UndirectedGraph) {

    /**
     * This auxiliary function for the function [findAnyCycle] aims to get from
     * some hashmap with extra elements the hashmap the elements of which
     * accurately describe found cycle path.
     *
     * @param cyclePath hashmap with extra elements
     * @param cycleVertexId the index of the vertex around which the cycle must be found
     * @return cycle path
     * @receiver private fun [findAnyCycle]
     */
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

    /**Searches for a cycle by dfs algorithm and writes it into [cyclePath]
     *
     *
     * @param cycleVertexId the index of the vertex around which the cycle must be found
     * @param currentVertexId using for recording the [cyclePath]
     * @param visited stores information about which vertices of the graph
     * have already been processed by [dfs]
     * @param cyclePath hashmap consisting of elements that can be used to restore the found cycle path
     * by applying the auxiliary function [getCyclePath] to it in function [findAnyCycle].
     * @receiver private fun [findAnyCycle]
     */
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

    /** Finds any existing cycle in the graph around a given vertex
     *
     * @param vertexId the index of the vertex around which the cycle must be found
     * @return found cycle path or null if the cycle doesn't exist around vertex with id = [vertexId]
     * @receiver fun [findCycle]
     */
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

    /**
     * The function implements the algorithm for finding a cycle around
     * vertex with id = [vertex] of the undirected graph.
     *
     * The function searches for a cycle and performs some minimization of it.
     * During the cycle minimization we iterate through variants of cycle,
     * based on the choice of a pair of neighbors of [vertex] through which the cycle will pass.
     * Each of the found cycle variants is written to a variable 'currentCyclePath'
     * and its size compared with 'minCycleSize'.
     * @param vertex the vertex around which the cycle must be found
     * @return cycle path or null if it doesn't exist
     */
    fun findCycle(vertex: Vertex): UndirectedGraph? {

        var returnCyclePath = hashMapOf<Int, Int>()
        var currentCyclePath: HashMap<Int, Int>?
        var minCycleSize: Int = Int.MAX_VALUE
        val returnGraph = UndirectedGraph()
        if (graph.adjacency[vertex.id]!!.size < 2) {
            return null
        } else { // we consider all possible cases of a cycle by choosing a pair of neighbors to minimize the found cycle
            val adjacencyOfVertex: MutableList<Int> = arrayListOf()
            graph.adjacency[vertex.id]!!.keys.forEach { adjacencyOfVertex.add(it) }
            val adjacencyWas: MutableList<Int> = arrayListOf()
            val removedEdges: MutableList<Edge> = arrayListOf()
            for (firstAdjacency in adjacencyOfVertex) {
                adjacencyWas.add(firstAdjacency)
                for (secondAdjacency in adjacencyOfVertex.filter { it !in adjacencyWas && it != vertex.id }) {

                    for (adjacency in graph.adjacency[vertex.id]!!.filter { it.key != firstAdjacency && it.key != secondAdjacency }) {
                        removedEdges.add(Edge(vertex.id to adjacency.key, adjacency.value))
                        graph.removeEdge(vertex.id to adjacency.key)
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
