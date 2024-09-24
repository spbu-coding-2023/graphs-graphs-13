package forTests

import model.graph.Graph

fun addVertices(graph: Graph<Int>, amount: Int) {

    var counter = amount
    while (counter > 0) {
        graph.addVertex(counter, counter)
        counter--
    }
}
