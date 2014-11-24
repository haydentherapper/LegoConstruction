package legoconstruction

/**
 * Created by Hayden Blauzvern on 11/7/14.
 */
object LegoConstruction {

  def main(args: Array[String]) {

    println("Please enter the name of the file containing your instructions:")
    val fileName = scala.io.StdIn.readLine()
    val program = scala.io.Source.fromFile(fileName)
    val lines = program.mkString
    println(lines)
    program.close()

    LegoParser(lines) match {
      case LegoParser.Success(t, _) => println(LegoInterpreter.eval(t).deep.mkString("\n"))
      case e: LegoParser.NoSuccess => println(e.msg)
    }
  }

}
