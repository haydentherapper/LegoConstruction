package progettare

/**
 * Created by Hayden Blauzvern on 11/7/14.
 */
object Progettare {

  val mapping:Map[String, String] = Map("0" -> Console.WHITE,
                                        "1" -> Console.RED,
                                        "2" -> Console.YELLOW,
                                        "3" -> Console.BLUE,
                                        "4" -> Console.GREEN,
                                        "5" -> Console.BLACK)

  def main(args: Array[String]) {
    println("Please enter the name of the file containing your instructions:")
    val fileName = scala.io.StdIn.readLine()
    val program = scala.io.Source.fromFile(fileName)
    val lines = program.mkString
    program.close()

    ProgettareParser(lines) match {
      case ProgettareParser.Success(t, _) => println(colorPrint(ProgettareInterpreter.eval(t).deep.mkString("\n")))
      case e: ProgettareParser.NoSuccess => println(e.msg)
    }
  }

  def colorPrint(string:String): String = {
    string.map(f => mapping.get(f.toString) match {
      case Some(color) => color + f
      case None => Console.RESET + f
    }).mkString
  }

}
