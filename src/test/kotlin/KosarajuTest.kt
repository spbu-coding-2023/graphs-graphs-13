import model.algorithms.Kosaraju
import model.graph.DirectedGraph
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

class KosarajuTest {
  private val dirGraph = DirectedGraph()
  private val scc = Kosaraju(dirGraph)

  @Test
  fun `searching for scc in a graph without vertices should be correct`() {
    assertEquals(listOf<List<Int>>(), scc.findStronglyConnectedComponents())
  }

  @Test
  fun `searching for scc in a graph without edges should be correct`() {
    dirGraph.addVertex(1, "A")
    dirGraph.addVertex(2, "B")
    dirGraph.addVertex(3, "C")
    assertEquals(listOf(listOf(3), listOf(2), listOf(1)), scc.findStronglyConnectedComponents())
  }

  @Test
  fun `searching for scc in a graph with bidirectional edges should be correct`() {
    dirGraph.addVertex(1, "A")
    dirGraph.addVertex(2, "B")
    dirGraph.addVertex(3, "C")
    dirGraph.addVertex(4, "D")
    dirGraph.addVertex(5, "E")
    dirGraph.addVertex(6, "F")
    dirGraph.addVertex(7, "G")
    dirGraph.addVertex(8, "H")
    dirGraph.addVertex(9, "I")
    dirGraph.addEdge(Pair(1, 2), 1)
    dirGraph.addEdge(Pair(2, 3), 45)
    dirGraph.addEdge(Pair(3, 1), 3)
    dirGraph.addEdge(Pair(4, 5), 12)
    dirGraph.addEdge(Pair(5, 4), -23)
    dirGraph.addEdge(Pair(4, 2), 4)
    dirGraph.addEdge(Pair(5, 6), 34)
    dirGraph.addEdge(Pair(6, 8), -12)
    dirGraph.addEdge(Pair(8, 9), null)
    dirGraph.addEdge(Pair(9, 7), 0)
    dirGraph.addEdge(Pair(7, 6), null)
    assertEquals(listOf(listOf(5, 4), listOf(8, 9, 7, 6), listOf(2, 3, 1)), scc.findStronglyConnectedComponents())
  }

  @Test
  fun `searching for scc in a graph without bidirectional edges should be correct`() {
    dirGraph.addVertex(1, "A")
    dirGraph.addVertex(2, "B")
    dirGraph.addVertex(3, "C")
    dirGraph.addVertex(4, "D")
    dirGraph.addVertex(5, "E")
    dirGraph.addVertex(6, "F")
    dirGraph.addVertex(7, "G")
    dirGraph.addEdge(Pair(1, 2), null)
    dirGraph.addEdge(Pair(2, 3), 34)
    dirGraph.addEdge(Pair(3, 4), 2)
    dirGraph.addEdge(Pair(4, 1), 1)
    dirGraph.addEdge(Pair(5, 6), 11)
    dirGraph.addEdge(Pair(6, 7), -2)
    dirGraph.addEdge(Pair(7, 2), 3)
    assertEquals(listOf(listOf(5), listOf(6), listOf(7), listOf(2, 3, 4, 1)), scc.findStronglyConnectedComponents())
  }

  @Test
  fun `searching for scc in a disconnected graph should be correct`() {
    dirGraph.addVertex(1, "A")
    dirGraph.addVertex(2, "B")
    dirGraph.addVertex(3, "C")
    dirGraph.addVertex(4, "D")
    dirGraph.addVertex(5, "E")
    dirGraph.addEdge(Pair(1, 2), 1)
    dirGraph.addEdge(Pair(2, 3), -1)
    dirGraph.addEdge(Pair(3, 1), 20)
    dirGraph.addEdge(Pair(4, 5), 34)
    assertEquals(listOf(listOf(4), listOf(5), listOf(2, 3, 1)), scc.findStronglyConnectedComponents())
  }

  @Test
  fun `searching for scc in a full graph should be correct`() {
    dirGraph.addVertex(1, "A")
    dirGraph.addVertex(2, "B")
    dirGraph.addVertex(3, "C")
    dirGraph.addEdge(Pair(1, 2), 1)
    dirGraph.addEdge(Pair(1, 3), -1)
    dirGraph.addEdge(Pair(2, 1), 1)
    dirGraph.addEdge(Pair(2, 3), 9)
    dirGraph.addEdge(Pair(3, 1), -1)
    dirGraph.addEdge(Pair(3, 2), 9)
    assertEquals(listOf(listOf(3, 2, 1)), scc.findStronglyConnectedComponents())
  }

  @Test
  fun `searching for scc in a graph without cycles should be correct`() {
    dirGraph.addVertex(1, "A")
    dirGraph.addVertex(2, "B")
    dirGraph.addVertex(3, "C")
    dirGraph.addVertex(4, "D")
    dirGraph.addVertex(5, "E")
    dirGraph.addEdge(Pair(1, 2), 1)
    dirGraph.addEdge(Pair(1, 3), -1)
    dirGraph.addEdge(Pair(2, 4), 1)
    dirGraph.addEdge(Pair(4, 5), 9)
    assertEquals(listOf(listOf(1), listOf(3), listOf(2), listOf(4), listOf(5)), scc.findStronglyConnectedComponents())
  }
}
