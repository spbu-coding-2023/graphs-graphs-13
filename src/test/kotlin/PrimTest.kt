import model.algorithms.Prim
import model.graph.UndirectedGraph
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import forTests.addVertices

class PrimTest {

    private val undirGraph = UndirectedGraph<Int>()
    private var primGraph = Prim(undirGraph)

    @Test
    fun `construction of MST from a graph consisting of a single vertex must be correct`() {

        addVertices(undirGraph, 1)

        assertEquals(0, primGraph.weightPrim())

    }

    @Test
    fun `construction of MST from 2 components must be correct`() {

        addVertices(undirGraph, 5)
        undirGraph.run {
            addEdge(1 to 2, 23)
            addEdge(2 to 3, 3)
            addEdge(1 to 3, 41)
            addEdge(4 to 5, 12)
        }

        assertEquals(38, primGraph.weightPrim())

    }

    @Test
    fun `construction of MST from tree must be correct`() { //when building a MST from a tree, we need to get the original tree

        addVertices(undirGraph, 4)

        undirGraph.run {
            addEdge(3 to 1, 3)
            addEdge(3 to 2, 8)
            addEdge(3 to 4, 21)
        }

        assertEquals(32, primGraph.weightPrim())

    }

    @Test
    fun `construction of MST from graph consisting of edges with equal weight must be correct`() {

        addVertices(undirGraph, 4)

        undirGraph.run {
            addEdge(1 to 2, 20)
            addEdge(1 to 4, 20)
            addEdge(3 to 2, 20)
            addEdge(3 to 4, 20)
        }

        assertEquals(60, primGraph.weightPrim())

    }

    @Test
    fun `construction of MST from non-trivial(more complex) graph must be correct`() {

        addVertices(undirGraph, 10)

        undirGraph.run {
            addEdge(1 to 2, 5)
            addEdge(2 to 3, 11)
            addEdge(3 to 4, 25)
            addEdge(4 to 8, 24)
            addEdge(1 to 4, 1)
            addEdge(2 to 5, 42)
            addEdge(5 to 3, 11)
            addEdge(5 to 4, 54)
            addEdge(8 to 9, 73)
            addEdge(6 to 7, 130)
            addEdge(7 to 9, 4)
            addEdge(8 to 7, 9)
            addEdge(8 to 10, 42)
        }

        assertEquals(237, primGraph.weightPrim())

    }

}
