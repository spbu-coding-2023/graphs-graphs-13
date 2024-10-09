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

  /** Saves the graph in Neo4j, in case of an error, returns a
   *  string with error information, otherwise null */
  fun saveGraph(graph: Graph): String? {
    val typeGraph = if (graph is DirectedGraph) "DIRECTED" else "UNDIRECTED"
    val transaction = session.beginTransaction()
    try {
      transaction.run("MATCH (n) DETACH DELETE n")
      for ((id, vertex) in graph.vertices) {
        addVertex(id, vertex.data, transaction)
      }
      for (edge in graph.edges) {
        addEdge(edge.vertices, edge.weight, transaction)
      }
      transaction.run("CREATE (g:Graph {type: '${typeGraph}'})")
      transaction.commit()
      return null
    } catch (e: Exception) {
      transaction.rollback()
      return "Failed to save graph (transaction error)"
    } finally {
      transaction.close()
    }
  }

  /** The function loads the graph from neo4j, if unsuccessful, returns an error message.
   * @return pair graph (Graph?) and error string (String?), if loading is successful,
   * returns graph and null string, if error returns null graph and error string
   */
  fun loadGraph(): Pair<Graph?, String?> {
    val graph: Graph
    val transaction = session.beginTransaction()
    try {
      val graphTypeResult = transaction.run("MATCH (g:Graph) RETURN g.type AS type LIMIT 1")
      val graphType = if (graphTypeResult.hasNext()) {
        val record = graphTypeResult.next()
        record.get("type").asString()
      } else {
        return (null to "Graph type not found in the database")
      }
      graph = when (graphType) {
        "DIRECTED" -> DirectedGraph()
        "UNDIRECTED" -> UndirectedGraph()
        else -> return (null to "Unknown graph type: $graphType")
      }
      val verticesResult = transaction.run("MATCH (v:Vertex) RETURN v.id AS id, v.data AS data")
      while (verticesResult.hasNext()) {
        val record = verticesResult.next()
        val id = record.get("id").asInt()
        val data = record.get("data").asString()
        graph.addVertex(id, data)
      }
      val edgesResult = transaction.run(
        "MATCH (v1:Vertex)-[r:EDGE]->(v2:Vertex) " +
                "RETURN v1.id AS id1, v2.id AS id2, r.weight AS weight"
      )
      while (edgesResult.hasNext()) {
        val record = edgesResult.next()
        val id1 = record.get("id1").asInt()
        val id2 = record.get("id2").asInt()
        val weight = if (record.containsKey("weight")) record.get("weight").asInt() else null
        graph.addEdge(Pair(id1, id2), weight)
      }
      transaction.commit()
      return (graph to null)
    } catch (e: Exception) {
      transaction.rollback()
      return null to "Failed to load graph (transaction error)"
    } finally {
      transaction.close()
    }
  }

  override fun close() {
    session.close()
    driver.close()
  }
}