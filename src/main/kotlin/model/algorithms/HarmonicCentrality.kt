package model.algorithms

import model.graph.DirectedGraph
import model.graph.Graph
import model.graph.UndirectedGraph
import kotlin.math.roundToInt

class HarmonicCentrality<D>(private val graph: Graph<D>) {

    private fun roundTo(number: Double): Double {
        return (number * 10000.0).roundToInt() / 10000.0
    }

    private fun getIndexEdge(getGraph: Graph<D>, vertexId: Int): Double {

        var index: Double = 0.00

        getGraph.vertices.filter { it.key != vertexId }
            .forEach {
                if (Dijkstra(getGraph).findShortestPaths(vertexId, it.key).isNotEmpty()) {
                    index += 1.0 / ((Dijkstra(getGraph).findShortestPaths(vertexId, it.key)).size - 1)
                }
            }

        return roundTo(index / (getGraph.vertices.size - 1))

    }

    fun harmonicCentrality(): HashMap<Int, Double> {

        val centralityIndexes = HashMap<Int, Double>()

        val graphForCentrality: Graph<D> = if (graph is UndirectedGraph<D>) {
            UndirectedGraph<D>()
        } else {
            DirectedGraph<D>()
        }
        graph.vertices.forEach { graphForCentrality.addVertex(it.key, it.value.data) }
        graph.edges.forEach { graphForCentrality.addEdge(it.vertices, 1) }

        for (vertexId in graphForCentrality.vertices.keys) {
            centralityIndexes[vertexId] = getIndexEdge(graphForCentrality, vertexId)
        }

        return centralityIndexes

    }

}
