import model.algorithms.Dijkstra
import model.graph.UndirectedGraph
import model.graph.DirectedGraph
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.assertFailsWith

class DijkstraTest {
  private val dirGraph = DirectedGraph()
  private val undirGraph = UndirectedGraph()
  private val shortestPathDirected = Dijkstra(dirGraph)
  private val shortestPathUndirected = Dijkstra(undirGraph)

  //region Exceptions
  @Test
  fun `having a negative edge weight in a path should throw an exception`() {
    dirGraph.addVertex(1, "A")
    dirGraph.addVertex(2, "B")
    dirGraph.addVertex(3, "C")
    dirGraph.addEdge(Pair(1, 3), 5)
    dirGraph.addEdge(Pair(3, 2), -2)
    assertEquals(null to "Edge with negative weights in Dijkstra's algorithm.", shortestPathDirected.findShortestPaths(1, 2))
  }

  @Test
  fun `having a null edge weight in a path should throw an exception`() {
    dirGraph.addVertex(1, "A")
    dirGraph.addVertex(2, "B")
    dirGraph.addVertex(3, "C")
    dirGraph.addEdge(Pair(1, 3), 5)
    dirGraph.addEdge(Pair(3, 2), null)
    assertEquals(null to "Edge without weights in Dijkstra's algorithm.", shortestPathDirected.findShortestPaths(1, 2))
  }

  @Test
  fun `searching for a path from vertices that are not in the graph should throw an exception`() {
    dirGraph.addVertex(1, "A")
    dirGraph.addVertex(2, "B")
    dirGraph.addVertex(3, "C")
    dirGraph.addEdge(Pair(1, 3), 5)
    dirGraph.addEdge(Pair(3, 2), 2)
    assertEquals(null to "The vertex doesn't exist in the graph.", shortestPathDirected.findShortestPaths(4, 5))
  }

  //endregion
  //region DirectedGraph
  @Test
  fun `searching for a non-existent path in a directed graph should be correct`() {
    dirGraph.addVertex(1, "A")
    dirGraph.addVertex(2, "B")
    dirGraph.addVertex(3, "C")
    dirGraph.addVertex(4, "D")
    dirGraph.addEdge(Pair(1, 2), 50)
    dirGraph.addEdge(Pair(2, 3), 5)
    dirGraph.addEdge(Pair(3, 1), 100)
    dirGraph.addEdge(Pair(4, 3), 15)
    assertEquals(listOf<Int>() to null, shortestPathDirected.findShortestPaths(1, 4))
  }

  @Test
  fun `searching for an existing path in a directed graph without bidirectional edges should be correct`() {
    dirGraph.addVertex(1, "A")
    dirGraph.addVertex(2, "B")
    dirGraph.addVertex(3, "C")
    dirGraph.addVertex(4, "D")
    dirGraph.addVertex(5, "E")
    dirGraph.addVertex(6, "F")
    dirGraph.addVertex(7, "G")
    dirGraph.addVertex(8, "H")
    dirGraph.addVertex(9, "I")
    dirGraph.addEdge(Pair(1, 2), 5)
    dirGraph.addEdge(Pair(1, 3), 10)
    dirGraph.addEdge(Pair(1, 4), 15)
    dirGraph.addEdge(Pair(2, 5), 20)
    dirGraph.addEdge(Pair(3, 5), 50)
    dirGraph.addEdge(Pair(4, 6), 200)
    dirGraph.addEdge(Pair(6, 5), 120)
    dirGraph.addEdge(Pair(6, 8), 1)
    dirGraph.addEdge(Pair(5, 7), 7)
    dirGraph.addEdge(Pair(7, 8), 54)
    dirGraph.addEdge(Pair(8, 9), 21)
    assertEquals(listOf(1, 2, 5, 7, 8, 9) to null, shortestPathDirected.findShortestPaths(1, 9))
  }

  @Test
  fun `searching for an existing path in a directed graph with bidirectional edges should be correct`() {
    dirGraph.addVertex(1, "A")
    dirGraph.addVertex(2, "B")
    dirGraph.addVertex(3, "C")
    dirGraph.addVertex(4, "D")
    dirGraph.addVertex(5, "E")
    dirGraph.addVertex(6, "F")
    dirGraph.addVertex(7, "G")
    dirGraph.addVertex(8, "H")
    dirGraph.addVertex(9, "I")
    dirGraph.addEdge(Pair(1, 2), 5)
    dirGraph.addEdge(Pair(1, 3), 10)
    dirGraph.addEdge(Pair(1, 4), 15)
    dirGraph.addEdge(Pair(2, 5), 20)
    dirGraph.addEdge(Pair(3, 5), 50)
    dirGraph.addEdge(Pair(4, 6), 200)
    dirGraph.addEdge(Pair(6, 5), 120)
    dirGraph.addEdge(Pair(6, 8), 1)
    dirGraph.addEdge(Pair(5, 7), 7)
    dirGraph.addEdge(Pair(7, 8), 54)
    dirGraph.addEdge(Pair(8, 9), 21)
    dirGraph.addEdge(Pair(4, 1), 15)
    dirGraph.addEdge(Pair(5, 6), 120)
    dirGraph.addEdge(Pair(7, 5), 7)
    assertEquals(listOf(1, 2, 5, 7, 8, 9) to null, shortestPathDirected.findShortestPaths(1, 9))
  }

  //endregion
  //region UnDirectedGraph
  @Test
  fun `searching for a non-existent path in a undirected graph should be correct`() {
    undirGraph.addVertex(1, "A")
    undirGraph.addVertex(2, "B")
    undirGraph.addVertex(3, "C")
    undirGraph.addVertex(4, "D")
    undirGraph.addVertex(5, "E")
    undirGraph.addVertex(6, "F")
    undirGraph.addEdge(Pair(1, 3), 5)
    undirGraph.addEdge(Pair(3, 2), 10)
    undirGraph.addEdge(Pair(1, 2), 15)
    undirGraph.addEdge(Pair(6, 5), 20)
    undirGraph.addEdge(Pair(5, 4), 50)
    assertEquals(listOf<String>() to null, shortestPathUndirected.findShortestPaths(2, 5))
  }

  @Test
  fun `searching for an existing path in the undirected graph should be correct`() {
    undirGraph.addVertex(1, "A")
    undirGraph.addVertex(2, "B")
    undirGraph.addVertex(3, "C")
    undirGraph.addVertex(4, "D")
    undirGraph.addVertex(5, "E")
    undirGraph.addVertex(6, "F")
    undirGraph.addVertex(7, "G")
    undirGraph.addVertex(8, "H")
    undirGraph.addVertex(9, "I")
    undirGraph.addVertex(10, "J")
    undirGraph.addVertex(11, "K")
    undirGraph.addEdge(Pair(1, 2), 50)
    undirGraph.addEdge(Pair(1, 3), 5)
    undirGraph.addEdge(Pair(3, 4), 100)
    undirGraph.addEdge(Pair(3, 5), 15)
    undirGraph.addEdge(Pair(5, 6), 5)
    undirGraph.addEdge(Pair(4, 6), 20)
    undirGraph.addEdge(Pair(4, 7), 20)
    undirGraph.addEdge(Pair(4, 8), 50)
    undirGraph.addEdge(Pair(8, 9), 34)
    undirGraph.addEdge(Pair(9, 10), 80)
    undirGraph.addEdge(Pair(2, 11), 5)
    assertEquals(listOf(1, 3, 5, 6, 4, 8, 9) to null, shortestPathUndirected.findShortestPaths(1, 9))
  }
  //endregion
}
