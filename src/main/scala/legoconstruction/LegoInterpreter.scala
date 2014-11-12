package legoconstruction

/**
 * Created by Hayden Blauzvern on 11/12/14.
 */
object LegoInterpreter {

  val matrix = Array.ofDim[Array[Int]](32,32)

  def eval(ast: AST): Array[Array[Int]] = ast match {
    case _ => null
  }

}
