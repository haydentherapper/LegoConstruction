package legoconstruction

import scala.math

/**
 * Created by Hayden Blauzvern on 11/12/14.
 */
object LegoInterpreter {

  val EMPTY_PIECE = 0

  val matrixColor = Array.fill(32,32){Array[Int]()}
  val matrixObject = Array.fill(32,32){Array[String]()}
  val mapping:Map[String, Int] = Map("Empty" -> EMPTY_PIECE,
                                      "Color(Red)" -> 1,
                                      "Color(Yellow)" -> 2,
                                      "Color(Blue)" -> 3,
                                      "Color(Green)" -> 4,
                                      "Color(Black)" -> 5)

  def eval(ast: AST): Array[Array[Array[Int]]] = ast match {
    case Program(x:List[Var], y:List[Instruction]) =>
      y.foreach(i => evalInstruction(i, x))
      matrixColor
  }

  def evalInstruction(i:Instruction, varList:List[Var], varPosition: Position = Position(0,0)): Unit = i match {
    case Instruction(p: Piece, pos: Position) => placePieceInMatrix(p, pos + varPosition)
    case Instruction(v: VarName, pos: Position) =>
      val instructionList = varNameToInstructionList(varList, v)
      instructionList.foreach(i => evalInstruction(i, varList, pos))
  }

  // First find the max height where we can place the Piece
  // Next place each section of the Piece in the grid, filling space if necessary
  def placePieceInMatrix(p:Piece, pos:Position, mat:Array[Array[Array[Int]]] = matrixColor): Unit = {
    var maxHeight = 0
    for (x <- pos.x to pos.x+p.m-1) {
      for (y <- pos.y to pos.y+p.n-1) {
        val curMax = maxHeightPlacement(mat(x)(y).toList)
        if (curMax > maxHeight) {
          maxHeight = curMax
        }
      }
    }

    for (x <- pos.x to pos.x+p.m-1) {
      for (y <- pos.y to pos.y+p.n-1) {
        var zDim: Array[Int] = mat(x)(y)
        while (zDim.length <= maxHeight) {
          zDim = zDim :+ EMPTY_PIECE
        }
        zDim(maxHeight) = mapping(p.color.toString)
        matrixColor(x)(y) = zDim
      }
    }
  }

  // Returns the max height where a piece could be placed
  def maxHeightPlacement(list:List[Int]): Int = {
    def findEmpty(list:List[Int]): Int = list match {
      case Nil => 0
      case -1::rest => 0
      case x::rest => 1 + findEmpty(rest)
    }
    math.min(list.length, findEmpty(list))
  }

  def varNameToInstructionList(varList:List[Var], varName:VarName): List[Instruction] = {
    varList.foreach(v => if (v.varName == varName) return v.instructionList)
    null
  }

}
