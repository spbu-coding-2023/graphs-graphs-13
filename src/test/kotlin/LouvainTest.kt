import model.algorithms.Louvain
import model.graph.UndirectedGraph
import model.graph.DirectedGraph
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

class LouvainTest {
  private val dirGraph = DirectedGraph<String>()
  private val communityDir = Louvain(dirGraph)
  private val undirGraph = UndirectedGraph<String>()
  private val communityUndir = Louvain(undirGraph)

  //region Special cases
  @Test
  fun `detected communities in a graph without vertices should be correct`() {
    assertEquals(listOf<Set<Int>>(), communityUndir.detectCommunities())
  }

  @Test
  fun `detected communities in a graph without edges should be correct`() {
    undirGraph.addVertex(1, "A")
    undirGraph.addVertex(2, "B")
    undirGraph.addVertex(3, "C")
    assertEquals(listOf(setOf(1), setOf(2), setOf(3)), communityUndir.detectCommunities())
  }

  //endregion
  //region DirectedGraph
  @Test
  fun `detected communities in a directed graph of cycle should be correct`() {
    dirGraph.addVertex(1, "A")
    dirGraph.addVertex(2, "B")
    dirGraph.addVertex(3, "C")
    dirGraph.addVertex(4, "D")
    dirGraph.addVertex(5, "E")
    dirGraph.addVertex(6, "F")
    dirGraph.addEdge(Pair(1, 2), 4)
    dirGraph.addEdge(Pair(2, 3), 13)
    dirGraph.addEdge(Pair(3, 4), 1)
    dirGraph.addEdge(Pair(4, 5), null)
    dirGraph.addEdge(Pair(5, 6), null)
    dirGraph.addEdge(Pair(6, 1), 0)
    assertEquals(listOf(setOf(1, 2, 3, 4, 5, 6)), communityDir.detectCommunities())
  }

  @Test
  fun `detected communities in a directed graph of cycle with bidirectional edges should be correct`() {
    dirGraph.addVertex(1, "A")
    dirGraph.addVertex(2, "B")
    dirGraph.addVertex(3, "C")
    dirGraph.addVertex(4, "D")
    dirGraph.addVertex(5, "E")
    dirGraph.addVertex(6, "F")
    dirGraph.addEdge(Pair(1, 2), 4)
    dirGraph.addEdge(Pair(2, 3), 13)
    dirGraph.addEdge(Pair(3, 4), 1)
    dirGraph.addEdge(Pair(4, 5), null)
    dirGraph.addEdge(Pair(5, 6), null)
    dirGraph.addEdge(Pair(6, 5), null)
    dirGraph.addEdge(Pair(6, 1), 0)
    assertEquals(listOf(setOf(1, 2, 3, 4), setOf(5, 6)), communityDir.detectCommunities())
  }

  @Test
  fun `detected communities in a directed graph of cycles connected by an edge should be correct`() {
    dirGraph.addVertex(1, "A")
    dirGraph.addVertex(2, "B")
    dirGraph.addVertex(3, "C")
    dirGraph.addVertex(4, "D")
    dirGraph.addVertex(5, "E")
    dirGraph.addVertex(6, "F")
    dirGraph.addEdge(Pair(1, 2), null)
    dirGraph.addEdge(Pair(2, 3), 1)
    dirGraph.addEdge(Pair(1, 3), 4)
    dirGraph.addEdge(Pair(3, 4), 3)
    dirGraph.addEdge(Pair(4, 5), 1)
    dirGraph.addEdge(Pair(5, 6), 0)
    dirGraph.addEdge(Pair(4, 6), 12)
    assertEquals(listOf(setOf(1, 2, 3), setOf(4, 5, 6)), communityDir.detectCommunities())
  }

  @Test
  fun `detected communities in a disconnected directed graph should be correct`() {
    dirGraph.addVertex(1, "A")
    dirGraph.addVertex(2, "B")
    dirGraph.addVertex(3, "C")
    dirGraph.addVertex(4, "D")
    dirGraph.addVertex(5, "E")
    dirGraph.addVertex(6, "F")
    dirGraph.addVertex(7, "G")
    dirGraph.addVertex(8, "H")
    dirGraph.addVertex(9, "I")
    dirGraph.addEdge(Pair(1, 2), 4)
    dirGraph.addEdge(Pair(2, 1), 4)
    dirGraph.addEdge(Pair(2, 3), 12)
    dirGraph.addEdge(Pair(3, 4), 3)
    dirGraph.addEdge(Pair(4, 3), 56)
    dirGraph.addEdge(Pair(5, 6), null)
    dirGraph.addEdge(Pair(7, 8), 0)
    dirGraph.addEdge(Pair(8, 9), -19)
    dirGraph.addEdge(Pair(9, 7), 2)
    assertEquals(listOf(setOf(1, 2), setOf(3, 4), setOf(5, 6), setOf(7, 8, 9)), communityDir.detectCommunities())
  }

  //endregion
  //region UndirectedGraph
  @Test
  fun `detected communities in a graph of cycles connected by an edge should be correct`() {
    undirGraph.addVertex(1, "A")
    undirGraph.addVertex(2, "B")
    undirGraph.addVertex(3, "C")
    undirGraph.addVertex(4, "D")
    undirGraph.addVertex(5, "E")
    undirGraph.addVertex(6, "F")
    undirGraph.addEdge(Pair(1, 2), null)
    undirGraph.addEdge(Pair(2, 3), 1)
    undirGraph.addEdge(Pair(1, 3), 4)
    undirGraph.addEdge(Pair(3, 4), 3)
    undirGraph.addEdge(Pair(4, 5), 1)
    undirGraph.addEdge(Pair(5, 6), 0)
    undirGraph.addEdge(Pair(4, 6), 12)
    assertEquals(listOf(setOf(1, 2, 3), setOf(4, 5, 6)), communityUndir.detectCommunities())
  }

  @Test
  fun `detected communities in a graph of cycle should be correct`() {
    undirGraph.addVertex(1, "A")
    undirGraph.addVertex(2, "B")
    undirGraph.addVertex(3, "C")
    undirGraph.addVertex(4, "D")
    undirGraph.addVertex(5, "E")
    undirGraph.addVertex(6, "F")
    undirGraph.addEdge(Pair(1, 2), 4)
    undirGraph.addEdge(Pair(2, 3), 13)
    undirGraph.addEdge(Pair(3, 4), 1)
    undirGraph.addEdge(Pair(4, 5), null)
    undirGraph.addEdge(Pair(5, 6), null)
    undirGraph.addEdge(Pair(6, 1), 0)
    assertEquals(listOf(setOf(1, 2, 3, 4, 5, 6)), communityUndir.detectCommunities())
  }

  @Test
  fun `detected communities in a disconnected graph should be correct`() {
    undirGraph.addVertex(1, "A")
    undirGraph.addVertex(2, "B")
    undirGraph.addVertex(3, "C")
    undirGraph.addVertex(4, "D")
    undirGraph.addVertex(5, "E")
    undirGraph.addVertex(6, "F")
    undirGraph.addVertex(7, "G")
    undirGraph.addVertex(8, "H")
    undirGraph.addVertex(9, "I")
    undirGraph.addEdge(Pair(1, 2), 4)
    undirGraph.addEdge(Pair(2, 3), 12)
    undirGraph.addEdge(Pair(3, 4), 3)
    undirGraph.addEdge(Pair(4, 3), 56)
    undirGraph.addEdge(Pair(5, 6), null)
    undirGraph.addEdge(Pair(7, 8), 0)
    undirGraph.addEdge(Pair(8, 9), -19)
    undirGraph.addEdge(Pair(9, 7), 2)
    assertEquals(listOf(setOf(1, 2, 3, 4), setOf(5, 6), setOf(7, 8, 9)), communityUndir.detectCommunities())
  }
  //endregion
}
