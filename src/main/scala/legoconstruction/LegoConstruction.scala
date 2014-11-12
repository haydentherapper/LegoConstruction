package legoconstruction

/**
 * Created by Hayden Blauzvern on 11/7/14.
 */
object LegoConstruction {

  def main(args: Array[String]) {
    val input = "Pawn {" +
                  "2x2 Red Brick at 1,1" +
                "}\n" +
                "Rook {" +
                  "2x2 Red Brick at 1,1" +
                "}\n" +
                "Pawn at 2,1\n" +
                "2x2 Yellow Brick at 2,1"
    println(LegoParser(input))
  }

}
