import model.algorithms.Prim
import model.graph.UndirectedGraph
import model.graph.DirectedGraph
import model.graph.Edge
import model.graph.Vertex
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

class PrimTest{ //4 TESTS

    @Test
    fun `a graph consists of two vertices and one edge`() {
        val initGraph = UndirectedGraph<Int>()

        initGraph.addVertex(1, 1)
        initGraph.addVertex(2, 2)

        initGraph.addEdge(1 to 2, 9)
        val mstGraph = MST<Int>(initGraph)
        val MSTree : UndirectedGraph<Int> = mstGraph.getMST()
        assertEquals( listOf(Edge<Int>(2 to 1, 9),Edge<Int>(1 to 2, 9)), MSTree.edges )
        assertEquals( setOf(Vertex<Int>(1,1),Vertex<Int>(2,2)), setOf( MSTree.vertices ) )

    }

    @Test
    fun `a triangle-shaped graph, checking for the content of cycles in the MSTree`() {
        val initGraph = UndirectedGraph<Int>()

        initGraph.addVertex(1, 1)
        initGraph.addVertex(2, 2)
        initGraph.addVertex(3,3)

        initGraph.addEdge(1 to 2, 10)
        initGraph.addEdge(1 to 3, 5)
        initGraph.addEdge(2 to 3, 1)

        val graph = MST<Int>(initGraph)
        val MSTree : UndirectedGraph<Int> = graph.getMST()
        val weightsEdgesOfMST = mutableListOf<Int>()
        (MSTree.edges).forEach{ weightsEdgesOfMST.add(it.weight!!) }
        assertEquals(2,weightsEdgesOfMST.size) // amount of edges
        assertEquals(setOf(1,5),setOf(weightsEdgesOfMST)) // edge values
        assertEquals( setOf(Vertex<Int>(1,1),Vertex<Int>(2,2),Vertex<Int>(3,3)), setOf( MSTree.vertices ) )

    }

    @Test
    fun `a graph in the form of a square with diagonals and a vertex in the middle`() {
        val initGraph = UndirectedGraph<Int>()

        initGraph.addVertex(1,1)
        initGraph.addVertex(2,2)
        initGraph.addVertex(3,3)
        initGraph.addVertex(4,4)
        initGraph.addVertex(5,5)

        initGraph.addEdge(1 to 2,9)
        initGraph.addEdge(1 to 4,3)
        initGraph.addEdge(1 to 5,0)
        initGraph.addEdge(2 to 4,13)
        initGraph.addEdge(2 to 3,47)
        initGraph.addEdge(3 to 4,31)
        initGraph.addEdge(3 to 5,2)
        initGraph.addEdge(5 to 4,6)

        val graph = MST<Int>(initGraph)
        val MSTree : UndirectedGraph<Int> = graph.getMST()
        val weightsEdgesOfMST = mutableListOf<Int>()
        (MSTree.edges).forEach{ weightsEdgesOfMST.add(it.weight!!) }
        assertEquals(setOf(0,2,3,9),setOf(weightsEdgesOfMST))
        assertEquals(true,weightsEdgesOfMST.groupBy { it }.values.map { it.size }.all{ it == 2 })

    }

    @Test
    fun `a graph in the form of a square with diagonals and a vertex in the middle with the same weight`() {
        val initGraph = UndirectedGraph<Int>()

        initGraph.addVertex(1,1)
        initGraph.addVertex(2,2)
        initGraph.addVertex(3,3)
        initGraph.addVertex(4,4)
        initGraph.addVertex(5,5)

        initGraph.addEdge(1 to 2,3)
        initGraph.addEdge(1 to 4,3)
        initGraph.addEdge(1 to 5,3)
        initGraph.addEdge(2 to 4,3)
        initGraph.addEdge(2 to 3,3)
        initGraph.addEdge(3 to 4,3)
        initGraph.addEdge(3 to 5,3)
        initGraph.addEdge(5 to 4,3)

        val graph = MST<Int>(initGraph)
        val MSTree : UndirectedGraph<Int> = graph.getMST()
        val weightsEdgesOfMST = mutableListOf<Int>()
        (MSTree.edges).forEach{ weightsEdgesOfMST.add(it.weight!!) }
        assertEquals(3,setOf(weightsEdgesOfMST))
        assertEquals(4,weightsEdgesOfMST.size)

    }

}