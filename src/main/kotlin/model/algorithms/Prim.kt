package model.algorithms

import model.graph.UndirectedGraph

class Prim<D>(private val graph: UndirectedGraph<D>) {

    private fun getMST(graph: UndirectedGraph<D>): UndirectedGraph<D> {

        val graphMst = UndirectedGraph<D>()

        for (vertex in graph.vertices.keys) {
            graphMst.addVertex(vertex, graph.vertices[vertex]!!.data)
        }

        val priorityQueue = hashMapOf<Int, Pair<Int, Int?>>()
        for (idVertex in graph.vertices.keys) {
            priorityQueue[idVertex] = Int.MAX_VALUE to null
        }

        //init Prim's algorithm

        val rootIdVertex: Int = graph.vertices.keys.first()
        for (idAdjacency in graph.adjacency[rootIdVertex]!!.keys) { // неправильные соседи
            priorityQueue[idAdjacency] = graph.adjacency[rootIdVertex]!![idAdjacency]!! to rootIdVertex
        }
        priorityQueue.remove(rootIdVertex)

        //Prim's algorithm

        var fromQueuePrioritet: Int
        var fromQueueId: Int? = null
        while (priorityQueue.isNotEmpty()) {

            fromQueuePrioritet = Int.MAX_VALUE
            for (element in priorityQueue) {
                if (element.value.first <= fromQueuePrioritet) {
                    fromQueuePrioritet = element.value.first
                    fromQueueId = element.key
                }
            }

            for (adjacency in graph.adjacency[fromQueueId]!!.filter { it.key in priorityQueue.keys }) {
                if (graph.adjacency[fromQueueId]!![adjacency.key]!! < priorityQueue[adjacency.key]!!.first) {
                    priorityQueue.remove(adjacency.key)
                    priorityQueue[adjacency.key] = graph.adjacency[fromQueueId]!![adjacency.key]!! to fromQueueId
                }
            }

            graphMst.addEdge(fromQueueId!! to priorityQueue[fromQueueId]!!.second!!, fromQueuePrioritet)
            priorityQueue.remove(fromQueueId)

        }

        return graphMst

    }

    private fun dfs(
        idVertex: Int,
        visited: HashMap<Int, Boolean>,
        component: UndirectedGraph<D>
    ): UndirectedGraph<D> {

        visited[idVertex] = true
        for (idAdjacency in graph.adjacency[idVertex]!!.keys) {
            if (visited[idAdjacency] == false) {
                visited[idAdjacency] = true
                component.addVertex(idAdjacency, graph.vertices[idAdjacency]!!.data)
                dfs(idAdjacency, visited, component)
            }
        }
        for (edge in this.graph.edges) {
            if (edge.vertices.first in component.vertices && edge.vertices.second in component.vertices && edge !in component.edges) {
                component.addEdge(edge.vertices, edge.weight)
            }
        }
        return component
    }

    private fun privateTreePrim(): MutableList<UndirectedGraph<D>> {

        val returnListOfMST = mutableListOf<UndirectedGraph<D>>()

        val visited = hashMapOf<Int, Boolean>()
        for (vertexId in graph.vertices.keys) {
            visited[vertexId] = false
        }

        var initIndex: Int
        var component: UndirectedGraph<D>
        while (visited.values.contains(false)) {

            initIndex = visited.filterValues { !it }.keys.first()
            component = UndirectedGraph()
            component.addVertex(initIndex, graph.vertices[initIndex]!!.data)
            component = dfs(initIndex, visited, component)
            returnListOfMST.add(getMST(component))

        }

        return returnListOfMST

    }

    fun treePrim(): MutableList<UndirectedGraph<D>> {

        return privateTreePrim()

    }

    fun weightPrim(): Int {

        var treeWeight: Int = 0
        for (element in privateTreePrim()) {
            for (edge in element.edges) {

                if (edge.weight == null) {
                    throw IllegalArgumentException("Each edge of a weighted graph must have a weight: the edge with weight = 'null' isn't correct")
                } else {
                    treeWeight += edge.weight!!
                }

            }

        }

        return treeWeight / 2

    }

}
