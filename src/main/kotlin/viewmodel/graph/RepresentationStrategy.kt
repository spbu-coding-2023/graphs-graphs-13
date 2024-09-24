package viewmodel.graph

import viewmodel.graph.VertexViewModel

interface RepresentationStrategy {
  fun <D> place(width: Double, height: Double, vertices: Collection<VertexViewModel<D>>)
}