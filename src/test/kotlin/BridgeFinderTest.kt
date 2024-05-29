import kotlin.test.assertEquals
import model.algorithms.BridgeFinder
import model.graph.UndirectedGraph
import org.junit.jupiter.api.Test

class BridgeFinderTest {
    private val undirGraph = UndirectedGraph<String>()
    private val bridgeFinder = BridgeFinder<String>()

    init {
        // Добавление вершин для ненаправленного графа
        undirGraph.addVertex(0, "A")
        undirGraph.addVertex(1, "B")
        undirGraph.addVertex(2, "C")
        undirGraph.addVertex(3, "D")
        undirGraph.addVertex(4, "E")
    }

    @Test
    fun testNoBridges() {
        undirGraph.addEdge(Pair(0, 1), 1)
        undirGraph.addEdge(Pair(1, 2), 1)
        undirGraph.addEdge(Pair(2, 0), 1)
        undirGraph.addEdge(Pair(1, 3), 1)
        undirGraph.addEdge(Pair(3, 4), 1)
        undirGraph.addEdge(Pair(4, 1), 1)

        val bridges = bridgeFinder.findBridges(undirGraph)
        assertEquals(emptyList<Pair<Int, Int>>(), bridges)
    }

    @Test
    fun testOneBridge() {
        undirGraph.addEdge(Pair(0, 1), 1)
        undirGraph.addEdge(Pair(1, 2), 1)
        undirGraph.addEdge(Pair(2, 3), 1)
        undirGraph.addEdge(Pair(3, 4), 1)

        val bridges = bridgeFinder.findBridges(undirGraph)
        assertEquals(listOf(Pair(0, 1), Pair(1, 2), Pair(2, 3), Pair(3, 4)), bridges.sortedWith(compareBy({ it.first }, { it.second })))
    }

    @Test
    fun testMultipleBridges() {
        undirGraph.addEdge(Pair(0, 1), 1)
        undirGraph.addEdge(Pair(1, 2), 1)
        undirGraph.addEdge(Pair(2, 3), 1)
        undirGraph.addEdge(Pair(3, 4), 1)
        undirGraph.addEdge(Pair(1, 3), 1)

        val bridges = bridgeFinder.findBridges(undirGraph)
        assertEquals(listOf(Pair(0, 1), Pair(3, 4)), bridges.sortedWith(compareBy({ it.first }, { it.second })))
    }

    @Test
    fun testDisconnectedGraph() {
        undirGraph.addVertex(5, "F")
        undirGraph.addEdge(Pair(0, 1), 1)
        undirGraph.addEdge(Pair(2, 3), 1)
        undirGraph.addEdge(Pair(3, 4), 1)

        val bridges = bridgeFinder.findBridges(undirGraph)
        assertEquals(listOf(Pair(0, 1), Pair(2, 3), Pair(3, 4)), bridges.sortedWith(compareBy({ it.first }, { it.second })))
    }
}
