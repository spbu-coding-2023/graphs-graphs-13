import model.algorithms.CycleSearch
import model.graph.UndirectedGraph
import model.graph.Vertex
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import forTests.addVertices
import kotlin.test.assertFailsWith

class CycleSearchTest {

    private var graph = UndirectedGraph()
    private var cycleGraph = CycleSearch(graph)

    @Test
    fun `search for a cycle at a single vertex must be correct`() {

        addVertices(graph, 1)
        assertEquals(null, cycleGraph.findCycle(graph.vertices[1]!!))

    }

    @Test
    fun `search for a cycle at the graph that is a tree must be correct`() {

        addVertices(graph, 4)
        graph.run {
            addEdge(1 to 2, 23)
            addEdge(2 to 3, 12)
            addEdge(4 to 2, 4)
        }
        assertEquals(null, cycleGraph.findCycle(graph.vertices[1]!!))

    }

    @Test
    fun `search for a cycle at the graph that is a cycle must be correct`() {

        addVertices(graph, 3)
        graph.run {
            addEdge(1 to 2, 23)
            addEdge(2 to 3, 12)
            addEdge(1 to 3, 4)
        }
        assertEquals(true,cycleGraph.findCycle(graph.vertices[1]!!) != null)

    }

    @Test
    fun `search for a cycle at the graph around vertex with many adjacency must be correct`() {

        addVertices(graph, 6)
        graph.run {
            addEdge(1 to 2, 1)
            addEdge(2 to 3, 2)
            addEdge(2 to 5, 5)
            addEdge(2 to 6, 7)
            addEdge(3 to 4, 3)
            addEdge(4 to 5, 4)
            addEdge(5 to 6, 6)
        }
        assertEquals(true, cycleGraph.findCycle(graph.vertices[2]!!) != null)

    }

    @Test
    fun `search for a cycle at the graph containing cycle, but around vertex without cycle must be correct`() {

        addVertices(graph, 5)
        graph.run {
            addEdge(1 to 2, 13)
            addEdge(2 to 3, 21)
            addEdge(3 to 1, 67)
            addEdge(3 to 4, 41)
            addEdge(4 to 5, 10)
        }
        assertEquals(null, cycleGraph.findCycle(graph.vertices[4]!!))

    }

}
