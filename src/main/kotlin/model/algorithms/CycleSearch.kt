package model.algorithms

import model.graph.Graph
import model.graph.Edge

class CycleSearch<D>(private val graph: Graph<D>) {

    fun findCycle(cycleVertexId: Int): List<Edge<D>>? {

        if (cycleVertexId !in graph.vertices.keys) {
            throw IllegalArgumentException("Vertex with index = $cycleVertexId doesn't exist in the graph. The cycle can't be found")
        }

        val visited = HashMap<Int, Boolean>()
        for (vertexId in graph.vertices.keys) {
            visited[vertexId] = false
        }
        val cycleWayFrom =
            HashMap<Int, Int>()
        val cycleIsFound = Array<Boolean>(1) { false }
        val returnList = mutableListOf<Edge<D>>()

        dfs(cycleVertexId, visited, cycleWayFrom, cycleVertexId, cycleIsFound, returnList)
        if (cycleIsFound[0]) {
            return returnList
        }
        return null

    }

    private fun dfs(
        vertexId: Int,
        visited: HashMap<Int, Boolean>,
        cycleWayFrom: HashMap<Int, Int>,
        cycleVertexId: Int,
        cycleIsFound: Array<Boolean>,
        returnList: MutableList<Edge<D>>
    ) {

        visited[vertexId] = true

        if (!cycleIsFound[0]) {
            for (adjacencyVertexId in graph.adjacency[vertexId]!!.keys) {

                if (visited[adjacencyVertexId] == false) {
                    cycleWayFrom[adjacencyVertexId] = vertexId
                    dfs(adjacencyVertexId, visited, cycleWayFrom, cycleVertexId, cycleIsFound, returnList)
                } else if (adjacencyVertexId == cycleVertexId && adjacencyVertexId != cycleWayFrom[vertexId]) {
                    cycleWayFrom[cycleVertexId] = vertexId
                    var id = cycleWayFrom.keys.last()
                    while (id != cycleVertexId) {
                        returnList.add(Edge<D>(cycleWayFrom[id]!! to id, graph.adjacency[cycleWayFrom[id]]!![id]))
                        id = cycleWayFrom[id]!!
                    }
                    returnList.add(Edge<D>(cycleWayFrom[id]!! to id, graph.adjacency[cycleWayFrom[id]]!![id]))
                    cycleIsFound[0] = true
                }

            }
        }
    }


}




