package databases

import com.google.gson.Gson
import model.graph.DirectedGraph
import model.graph.Graph
import model.graph.UndirectedGraph
import java.awt.FileDialog
import java.awt.Frame
import java.io.File
import java.nio.file.Files

class FileSystem {

    /** From-to Json-conversation functions:
     * with the help of the Google's Gson library, it is possible
     * to get an object from a file and vice versa:
     * to get a file from an object **/

    private fun fromGsonConversation(jsonString: String, graph: Graph): Graph {

        val gson = Gson()
        if (graph is UndirectedGraph) {
            return gson.fromJson(jsonString, UndirectedGraph()::class.java)
        }

        return gson.fromJson(jsonString, DirectedGraph()::class.java)

    }

    private fun toGsonConversation(graph: Graph): String {

        val gson = Gson()
        return gson.toJson(graph)

    }

    /**If the graph is successfully saved the graph to a file,
     * the function returns null, in case of a save error,
     * it returns an error message**/

    fun saveGraph(graph: Graph): String? {

        val frame = Frame()
        val fileDialog= FileDialog(frame, "Save your Json file:", FileDialog.SAVE)
        fileDialog.setFile("*.json")
        fileDialog.isVisible = true

        if( fileDialog.file == null) { // case: click the "cross" or "cancel" buttons
            frame.dispose()
            return null
        }

        if(graph is UndirectedGraph) {
            fileDialog.file = "undir" + fileDialog.file
        }else {
            fileDialog.file = "dir" + fileDialog.file
        }
        val savedFile = File(fileDialog.directory, fileDialog.file)
        try {
            savedFile.writeText(toGsonConversation(graph))
            frame.dispose()
            return null
        } catch (e: Exception) {
            frame.dispose()
            return "To Gson conversation error:\n" +
                    (e.message?.substringAfter("Exception: ") ?:
                    "incorrect converted graph")
        }

    }

    /**The function returns pair, the fist element of that is
     * opened graph in successful case and null in failure case and second
     * element is an error message in failure case
     * and null in successful case.**/

    fun openGraph(): Pair<Graph?,String?> {
        var graph: Graph
        val frame = Frame()
        val fileDialog= FileDialog(frame, "Open your Json file:", FileDialog.LOAD)
        fileDialog.setFile("*.json")
        fileDialog.isVisible = true

        val fileName = fileDialog.file
        val directory = fileDialog.directory
        if( fileName == null) { // case: click the "cross" or "cancel" buttons
            frame.dispose()
            return null to null
        }
        if(fileName.startsWith("dir")) {
            graph = DirectedGraph()
        }else if(fileName.startsWith("undir")) {
            graph = UndirectedGraph()
        }else {
            return null to "Incorrect name of the selected file: it must begin with 'dir' or 'undir' ."
        }

        val openedFile = File(directory, fileName)

        try {

            graph = fromGsonConversation(
                Files.readString(openedFile.toPath()), graph
            )
            frame.dispose()
            return graph to null

        } catch (e: Exception) {
            frame.dispose()
            return null to "The contents of the file are incorrect:\n" +
                    (e.message?.substringAfter("Exception: ") ?:
                    "fix this file or try to open another one")
        }
    }
}
