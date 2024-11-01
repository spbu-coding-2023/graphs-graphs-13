package model.algorithms

import model.graph.Graph

class BellmanFord(private val graph: Graph) {
    fun findShortestPath(src: Int, dest: Int): Pair<Int, List<Int>>? {
        val dist = mutableMapOf<Int, Int>().withDefault { Int.MAX_VALUE }
        val pred = mutableMapOf<Int, Int?>()
        dist[src] = 0

        for (i in 1 until graph.vertices.size) {
            for (edge in graph.edges) {
                val (u, v) = edge.vertices
                val weight = edge.weight ?: continue
                if (dist.getValue(u) != Int.MAX_VALUE && dist.getValue(u) + weight < dist.getValue(v)) {
                    dist[v] = dist.getValue(u) + weight
                    pred[v] = u
                }
            }
        }

        for (edge in graph.edges) {
            val (u, v) = edge.vertices
            val weight = edge.weight ?: continue
            if (dist.getValue(u) != Int.MAX_VALUE && dist.getValue(u) + weight < dist.getValue(v)) {
                println("Graph contains negative weight cycle")
                return null
            }
        }

        val path = mutableListOf<Int>()
        var current: Int? = dest
        while (current != null) {
            path.add(current)
            current = pred[current]
        }
        path.reverse()

        return if (dist[dest] != Int.MAX_VALUE) Pair(dist[dest]!!, path) else null
    }
}

