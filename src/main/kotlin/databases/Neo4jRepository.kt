package databases

import model.graph.DirectedGraph
import model.graph.Graph
import model.graph.UndirectedGraph
import org.neo4j.driver.*
import java.io.Closeable

class Neo4jRepository(uri: String, user: String, password: String) : Closeable {
  private val driver: Driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password))
  private val session: Session = driver.session()

  private fun addVertex(vertexId: Int, vertexData: String, transaction: Transaction) {
    transaction.run(
      "CREATE (:Vertex {id: \$id, data: \$data})",
      Values.parameters("id", vertexId, "data", vertexData)
    )
  }

  private fun addEdge(verticesId: Pair<Int, Int>, weight: Int?, transaction: Transaction) {
    transaction.run(
      "MATCH (v1:Vertex {id:\$id1}) MATCH (v2:Vertex {id:\$id2}) " +
              "MERGE (v1)-[r:EDGE${if (weight != null) " {weight: \$weight}" else ""}]->(v2)",
      Values.parameters("id1", verticesId.first, "id2", verticesId.second, "weight", weight)
    )
  }

  private fun clearDatabase(transaction: Transaction) {
    transaction.run("MATCH (n) DETACH DELETE n")
  }

  fun <D> addGraph(graph: Graph<D>) {
    val transaction = session.beginTransaction()
    try {
      clearDatabase(transaction)
      for ((id, data) in graph.vertices) {
        addVertex(id, data.toString(), transaction)
      }
      for (edge in graph.edges) {
        addEdge(edge.vertices, edge.weight, transaction)
      }
      transaction.commit()
    } catch (e: Exception) {
      transaction.rollback()
      throw e
    } finally {
      transaction.close()
    }
  }

  override fun close() {
    session.close()
    driver.close()
  }
}