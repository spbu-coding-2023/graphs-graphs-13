package viewmodel.graph

interface RepresentationStrategy {
  fun place(width: Double, height: Double, vertices: Collection<VertexViewModel>)
}