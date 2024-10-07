package databases

import model.graph.DirectedGraph
import model.graph.Graph
import model.graph.UndirectedGraph
import org.neo4j.driver.*
import java.io.Closeable

class Neo4jRepository(uri: String, user: String, password: String) : Closeable {
  // bolt://localhost:7687
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

  fun saveGraph(graph: Graph) {
    val transaction = session.beginTransaction()
    try {
      transaction.run("MATCH (n) DETACH DELETE n")
      for ((id, vertex) in graph.vertices) {
        addVertex(id, vertex.data, transaction)
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

  fun loadGraph() : Graph {
    val graph = DirectedGraph()
    val transaction = session.beginTransaction()
    try {
      val verticesResult = transaction.run("MATCH (v:Vertex) RETURN v.id AS id, v.data AS data")
      while (verticesResult.hasNext()) {
        val record = verticesResult.next()
        val id = record.get("id").asInt()
        val data = record.get("data").asString()
        graph.addVertex(id, data)
      }
      val edgesResult = transaction.run("MATCH (v1:Vertex)-[r:EDGE]->(v2:Vertex) " +
              "RETURN v1.id AS id1, v2.id AS id2, r.weight AS weight")
      while (edgesResult.hasNext()) {
        val record = edgesResult.next()
        val id1 = record.get("id1").asInt()
        val id2 = record.get("id2").asInt()
        val weight = if (record.containsKey("weight")) record.get("weight").asInt() else null
        graph.addEdge(Pair(id1, id2), weight)
      }
      transaction.commit()
    } catch (e: Exception) {
      transaction.rollback()
      throw e
    } finally {
      transaction.close()
    }
    return graph
  }

  override fun close() {
    session.close()
    driver.close()
  }
}