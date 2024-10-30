package forTests

import model.graph.Graph

fun addVertices(graph: Graph, amount: Int) {

    var counter = amount
    while (counter > 0) {
        graph.addVertex(counter, counter.toString())
        counter--
    }
}
