package progettare

import scala.math

/**
 * Created by Hayden Blauzvern on 11/12/14.
 */
object ProgettareInterpreter {

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
    case Instruction(p: Piece, rel: Relative, pos: Position) => placePiece(p, pos + varPosition, rel)
    case Instruction(v: VarName, rel: Relative, pos: Position) =>
      val instructionList = varNameToInstructionList(varList, v)
      instructionList.foreach(i => evalInstruction(i, varList, pos))
  }

  // First find the max height where we can place the Piece
  // Next place each section of the Piece in the grid, filling space if necessary
  def placePiece(p:Piece, pos:Position, rel: Relative, mat:Array[Array[Array[Int]]] = matrixColor): Unit = {
    var finalPos = -1
    for (x <- pos.x to pos.x+p.m-1) {
      for (y <- pos.y to pos.y+p.n-1) {
        
        rel match {
          // Find first avaiable position, searching from the bottom upwards
          case Relative("at") =>  {
            val curMaxPos = findFirstAvailablePos(mat(x)(y).toList)
            if (curMaxPos > finalPos) {
              finalPos = curMaxPos
            }  
          }

          // Find position based on maximum height of each space below
          case Relative("above") => {
            if (mat(x)(y).length > finalPos) {
              finalPos = mat(x)(y).length
            }
          }

          // Find first avaiable position, searching from the max height downwards
          case Relative("below") => {

            
          }  
        }
        
        
      }
    }

    for (x <- pos.x to pos.x+p.m-1) {
      for (y <- pos.y to pos.y+p.n-1) {
        var zDim: Array[Int] = mat(x)(y)
        while (zDim.length <= finalPos) {
          zDim = zDim :+ EMPTY_PIECE
        }
        zDim(finalPos) = mapping(p.color.toString)
        matrixColor(x)(y) = zDim
      }
    }
  }

  // Returns the max height of the first available position
  // where a piece could be placed
  def findFirstAvailablePos(list:List[Int]): Int = {
    def findEmpty(list:List[Int]): Int = list match {
      case Nil => 0
      case EMPTY_PIECE::rest => 0
      case x::rest => 1 + findEmpty(rest)
    }
    math.min(list.length, findEmpty(list))
  }

  def findLastAvailablePos(list:List[Int]): Int = {
    def findEmpty(list:List[Int]): Int = list match {
      case Nil => 0
      case EMPTY_PIECE::rest => 0
      case x::rest => 1 + findEmpty(rest)
    }
    math.max(-1, findEmpty(list)) // fix this to catch error if nothing can be placed below
  }

  def varNameToInstructionList(varList:List[Var], varName:VarName): List[Instruction] = {
    varList.foreach(v => if (v.varName == varName) return v.instructionList)
    null
  }

}
