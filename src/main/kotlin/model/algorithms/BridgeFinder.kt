package model.algorithms

import model.graph.Graph

class BridgeFinder<D> {
    private var time = 0
    private val NIL = -1

    fun findBridges(graph: Graph<D>): List<Pair<Int, Int>> {
        val visited = BooleanArray(graph.vertices.size)
        val disc = IntArray(graph.vertices.size)
        val low = IntArray(graph.vertices.size)
        val parent = IntArray(graph.vertices.size)
        val bridges = mutableListOf<Pair<Int, Int>>()

        for (i in 0 until graph.vertices.size) {
            parent[i] = NIL
            visited[i] = false
        }

        for (i in 0 until graph.vertices.size) {
            if (!visited[i]) {
                bridgeUtil(i, visited, disc, low, parent, graph, bridges)
            }
        }

        return bridges
    }

    private fun bridgeUtil(u: Int, visited: BooleanArray, disc: IntArray, low: IntArray, parent: IntArray, graph: Graph<D>, bridges: MutableList<Pair<Int, Int>>) {
        visited[u] = true
        disc[u] = ++time
        low[u] = time

        val neighbors = graph.adjacency[u] ?: return
        for (v in neighbors.keys) {
            if (!visited[v]) {
                parent[v] = u
                bridgeUtil(v, visited, disc, low, parent, graph, bridges)

                low[u] = minOf(low[u], low[v])

                if (low[v] > disc[u]) {
                    bridges.add(Pair(u, v))
                }
            } else if (v != parent[u]) {
                low[u] = minOf(low[u], disc[v])
            }
        }
    }
}
