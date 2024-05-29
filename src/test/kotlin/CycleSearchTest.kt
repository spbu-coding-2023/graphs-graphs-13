import model.algorithms.CycleSearch
import model.graph.UndirectedGraph
import model.graph.DirectedGraph
import model.graph.Edge
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

class CycleSearchTest { //6 TESTS

    //region UNDIRECTED GRAPH (3)

    @Test
    fun ` a triangle-shaped undirected graph wiht cycle `() {
        val initGraph = UndirectedGraph<Int>()

        initGraph.addVertex(1, 1)
        initGraph.addVertex(2, 2)
        initGraph.addVertex(3, 3)

        initGraph.addEdge(1 to 2, 10)
        initGraph.addEdge(2 to 3, 5)
        initGraph.addEdge(3 to 1, 1)

        val graph = CycleSearch<Int>(initGraph)
        val cycleEdges: List<Edge<Int>>? = graph.findCycle(1)
        assertEquals(true, cycleEdges != null)
        for (edge in initGraph.edges) {
            assertEquals(true, edge in cycleEdges!!)
        }
        for (edge in cycleEdges!!) {
            assertEquals(true, edge in initGraph.edges)
        }
    }

    @Test
    fun ` undirected graph consisted of 4 vertices without cycle`() {
        val initGraph = UndirectedGraph<Int>()

        initGraph.addVertex(1, 1)
        initGraph.addVertex(2, 2)
        initGraph.addVertex(3, 3)
        initGraph.addVertex(4, 4)

        initGraph.addEdge(1 to 2, 10)
        initGraph.addEdge(1 to 3, 5)
        initGraph.addEdge(1 to 4, 1)

        val graph = CycleSearch<Int>(initGraph)
        val cycleEdges: List<Edge<Int>>? = graph.findCycle(1)
        assertEquals(null, cycleEdges)

    }

    @Test
    fun ` the cycle of the undirected graph exists, but not around the input vertex `() {
        val initGraph = UndirectedGraph<Int>()

        initGraph.addVertex(1, 1)
        initGraph.addVertex(2, 2)
        initGraph.addVertex(3, 3)
        initGraph.addVertex(4, 4)

        initGraph.addEdge(1 to 2, 10)
        initGraph.addEdge(2 to 3, 5)
        initGraph.addEdge(3 to 4, 3)
        initGraph.addEdge(4 to 2, 1)

        val graph = CycleSearch<Int>(initGraph)
        val cycleEdges: List<Edge<Int>>? = graph.findCycle(1)
        assertEquals(null, cycleEdges)

    }

    //endregion

    //region DIRECTED GRAPH (3)

    @Test
    fun ` a triangle-shaped directed graph whith cycle `() {
        val initGraph = DirectedGraph<Int>()

        initGraph.addVertex(1, 1)
        initGraph.addVertex(2, 2)
        initGraph.addVertex(3, 3)

        initGraph.addEdge(1 to 2, 10)
        initGraph.addEdge(2 to 3, 5)
        initGraph.addEdge(3 to 1, 1)

        val graph = CycleSearch<Int>(initGraph)
        val cycleEdges: List<Edge<Int>>? = graph.findCycle(1)
        assertEquals(true, cycleEdges != null)
        for (edge in initGraph.edges) {
            assertEquals(true, edge in cycleEdges!!)
        }
        for (edge in cycleEdges!!) {
            assertEquals(true, edge in initGraph.edges)
        }
        // проверили равенство наборов ребер у первоначального графа и у цикла
    }

    @Test
    fun ` a triangle-shaped directed graph without cycle `() {
        val initGraph = DirectedGraph<Int>()

        initGraph.addVertex(1, 1)
        initGraph.addVertex(2, 2)
        initGraph.addVertex(3, 3)

        initGraph.addEdge(1 to 2, 10)
        initGraph.addEdge(1 to 3, 5)
        initGraph.addEdge(2 to 3, 1)

        val graph = CycleSearch<Int>(initGraph)
        val cycleEdges: List<Edge<Int>>? = graph.findCycle(1)
        assertEquals(null, cycleEdges)

    }

    @Test
    fun ` the cycle of the directed graph exists, but not around the input vertex `() {
        val initGraph = DirectedGraph<Int>()

        initGraph.addVertex(0, 0)
        initGraph.addVertex(1, 1)
        initGraph.addVertex(2, 2)
        initGraph.addVertex(3, 3)

        initGraph.addEdge(0 to 1, 10)
        initGraph.addEdge(1 to 2, 7)
        initGraph.addEdge(2 to 3, 5)
        initGraph.addEdge(3 to 1, 1)

        val graph = CycleSearch<Int>(initGraph)
        val cycleEdges: List<Edge<Int>>? = graph.findCycle(0)
        assertEquals(null, cycleEdges)

    }

    //endregion

}
