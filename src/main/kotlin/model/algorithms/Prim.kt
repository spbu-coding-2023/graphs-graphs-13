package model.algorithms

import model.graph.UndirectedGraph

/**
 * The class [Prim] implements the Prim's algorithm for construction Minimum spanning tree (MST) from
 * undirected weighted graph.
 *
 * At the same time, if the graph consists of several connectivity components,
 * the algorithm constructs a forest, each tree of which is MST.
 *
 * @property [graph] a undirected weighted graph, whose MST we want to construct
 * @constructor Creates a graph, based on [graph], to which the following functions can be applied:
 * function [treePrim] : returns MST for [graph]
 * function [weightPrim] : returns the weight of received MST
 */

class Prim(private val graph: UndirectedGraph) {

    /**
     * This function implements Prim's algorithm for
     * constuction MST for transmitted connectivity component
     *
     * @param graph a connectivity component,
     * @return MST of connectivity component
     * @receiver fun [treePrim]
     */
    private fun getMST(graph: UndirectedGraph): UndirectedGraph {

        val graphMst = UndirectedGraph()

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

    /**
     * The function finds the connectivity component
     * and returns it to apply the Prim's algorithm to it
     *
     * @param idVertex init vertex for dfs
     * @param visited stores information about which vertices of the graph
     * have already been processed by the Prim's algorithm
     * @param component found connectivity component of the graph
     * @return found connectivity component
     * @receiver fun [treePrim]
     */
    private fun dfs(
        idVertex: Int,
        visited: HashMap<Int, Boolean>,
        component: UndirectedGraph
    ): UndirectedGraph {

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

    /**
     * The function finds the connectivity components of the graph
     * and then builds MST for each of them
     *
     * @return list of MST for each connectivity component of graph
     * @receiver  fun [weightPrim]
     */
    fun treePrim(): MutableList<UndirectedGraph> {

        val returnListOfMST = mutableListOf<UndirectedGraph>()

        val visited = hashMapOf<Int, Boolean>()
        for (vertexId in graph.vertices.keys) {
            visited[vertexId] = false
        }

        var initIndex: Int
        var component: UndirectedGraph
        while (visited.values.contains(false)) {

            initIndex = visited.filterValues { !it }.keys.first()
            component = UndirectedGraph()
            component.addVertex(initIndex, graph.vertices[initIndex]!!.data)
            component = dfs(initIndex, visited, component)
            returnListOfMST.add(getMST(component))

        }

        return returnListOfMST

    }

    /**
     * Counts the weight of received MST
     *
     * @return weight of MST
     */
    fun weightPrim(): Int {

        var treeWeight: Int = 0
        for (element in treePrim()) {
            for (edge in element.edges) {

                if (edge.weight != null) {
                    treeWeight += edge.weight!!
                }

            }

        }

        return treeWeight / 2

    }

}
