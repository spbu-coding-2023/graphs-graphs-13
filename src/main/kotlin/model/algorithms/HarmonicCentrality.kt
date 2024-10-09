package model.algorithms

import model.graph.DirectedGraph
import model.graph.Graph
import model.graph.UndirectedGraph
import kotlin.math.roundToInt

/** The class [HarmonicCentrality] implements the normalized harmonic centrality algorithm for
 * a graph of any kind. For implementation using the Dijkstra algorithm.
 *
 * Harmonic centrality is a kind of closeness centrality, but the difference is that
 * this algorithm is relevant not only for connected graphs, but also for disconnected ones.
 * The harmonic centrality index for each vertex of the graph is calculated using the formula:
 *     Index = sum(1/the length of the shortest path to i-th vertex),
 *     for i in 1..n-1, where 'n' is amount of graph vertices.
 * So that the index value lies in the interval from 0 to 1, we use the normalization of the centrality index
 * by dividing the result by n, where 'n' is amount of graph vertices:
 *     Index = sum(1/the length of the shortest path to i-th vertex) / n,
 *     for i in 1..n-1, where 'n' is amount of graph vertices.
 * In this case the length of the path is the amount of edges of this path.
 *
 * @property [graph] a graph (of any kind) for the vertices of which it is necessary to calculate the centrality index
 * @constructor Creates a graph, based on [graph], for which the algorithm for calculating the
 * normalized harmonic centrality can be applied.
 */

class HarmonicCentrality(private val graph: Graph) {

    /**
     * This auxiliary function for the function [getIndex], that rounds
     * the value [number] to the 4th digit after the decimal point.
     *
     * @return result of rounding
     * @receiver [getIndex]
     */
    private fun roundTo(number: Double): Double {
        return (number * 10000.0).roundToInt() / 10000.0
    }

    /**
     * This function calculate the centrality index for vertex with id = [vertexId].
     *
     * @param graph graph for the vertex of which we calculate the centrality index.
     * But weight of each graph edge is 1, what is used for applying Dijkstra algorithm.
     * @param vertexId the index of the vertex for which calculating the centrality index.
     * @return centrality index of vertex with id = [vertexId]
     * @receiver [harmonicCentrality]
     */
    private fun getIndex(graph: Graph, vertexId: Int): Double {

        var index = 0.00

        graph.vertices.filter { it.key != vertexId }
            .forEach {
                if (Dijkstra(graph).findShortestPaths(vertexId, it.key).first!!.isNotEmpty()) {
                    index += 1.0 / ((Dijkstra(graph).findShortestPaths(vertexId, it.key)).first!!.size - 1)
                }
            }

        return roundTo(index / (graph.vertices.size - 1))

    }

    /**
     * This function calculates the centrality index for each graph vertex.
     *
     * @return hashmap, where key = vertex id; value =  centrality index.
     */
    fun harmonicCentrality(): HashMap<Int, Double> {

        val centralityIndexes = HashMap<Int, Double>()

        val graphForCentrality: Graph = if (graph is UndirectedGraph) {
            UndirectedGraph()
        } else {
            DirectedGraph()
        }
        graph.vertices.forEach { graphForCentrality.addVertex(it.key, it.value.data) }
        graph.edges.forEach { graphForCentrality.addEdge(it.vertices, 1) }

        for (vertexId in graphForCentrality.vertices.keys) {
            centralityIndexes[vertexId] = getIndex(graphForCentrality, vertexId)
        }

        return centralityIndexes

    }

}
