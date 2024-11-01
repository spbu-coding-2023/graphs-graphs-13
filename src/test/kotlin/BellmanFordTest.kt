import kotlin.test.assertNull
import kotlin.test.assertEquals
import model.algorithms.BellmanFord
import model.graph.DirectedGraph
import model.graph.UndirectedGraph
import org.junit.jupiter.api.Test

class BellmanFordTest {
    private val dirGraph = DirectedGraph()
    private val undirGraph = UndirectedGraph()
    private val bellmanFordDirected = BellmanFord(dirGraph)
    private val bellmanFordUndirected = BellmanFord(undirGraph)

    init {
        // Добавление вершин для направленного графа
        dirGraph.addVertex(1, "A")
        dirGraph.addVertex(2, "B")
        dirGraph.addVertex(3, "C")
        dirGraph.addVertex(4, "D")

        // Добавление рёбер для направленного графа
        dirGraph.addEdge(Pair(1, 2), 4)
        dirGraph.addEdge(Pair(1, 3), 2)
        dirGraph.addEdge(Pair(2, 3), 5)
        dirGraph.addEdge(Pair(2, 4), 10)
        dirGraph.addEdge(Pair(3, 4), 3)

        // Добавление вершин для ненаправленного графа
        undirGraph.addVertex(1, "A")
        undirGraph.addVertex(2, "B")
        undirGraph.addVertex(3, "C")
        undirGraph.addVertex(4, "D")

        // Добавление рёбер для ненаправленного графа
        undirGraph.addEdge(Pair(1, 2), 1)
        undirGraph.addEdge(Pair(1, 3), 4)
        undirGraph.addEdge(Pair(2, 3), 2)
        undirGraph.addEdge(Pair(2, 4), 7)
        undirGraph.addEdge(Pair(3, 4), 3)
    }

    @Test
    fun testShortestPathDirectedGraph() {
        val result = bellmanFordDirected.findShortestPath(1, 4)
        assertEquals(Pair(5, listOf(1, 3, 4)), result)
    }

    @Test
    fun testShortestPathUndirectedGraph() {
        val result = bellmanFordUndirected.findShortestPath(1, 4)
        assertEquals(Pair(6, listOf(1, 2, 3, 4)), result)
    }
    @Test
    fun testNegativeWeightCycle() {
        dirGraph.addEdge(Pair(4, 1), -8) // Добавляем отрицательный цикл
        val result = bellmanFordDirected.findShortestPath(1, 4)
        assertNull(result) // Должно вернуть null из-за отрицательного цикла
    }

    @Test
    fun testShortestPathWithNegativeWeights() {
        dirGraph.addEdge(Pair(2, 3), -3) // Изменяем вес ребра
        val result = bellmanFordDirected.findShortestPath(1, 4)
        assertEquals(Pair(4, listOf(1, 2, 3, 4)), result)
    }
}
