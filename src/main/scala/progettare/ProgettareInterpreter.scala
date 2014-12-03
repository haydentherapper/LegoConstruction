package progettare

/**
 * Created by Hayden Blauzvern on 11/12/14.
 */
object ProgettareInterpreter {

  case class MatrixObject(color:Int = EmptyPiece,
                          xCon:(Boolean,Boolean) = (false,false),
                          yCon:(Boolean,Boolean) = (false,false),
                          zCon:(Boolean,Boolean) = (false,false))

  val EmptyPiece = 0

  val matrix = Array.fill(32,32){Array[MatrixObject]()}

  var varList:List[Var] = List()

  val mapping:Map[String, Int] = Map("Empty" -> EmptyPiece,
                                      "Color(Red)" -> 1,
                                      "Color(Yellow)" -> 2,
                                      "Color(Blue)" -> 3,
                                      "Color(Green)" -> 4,
                                      "Color(Black)" -> 5)

  def eval(ast: AST): Array[Array[Array[MatrixObject]]] = ast match {
    case Program(vList:List[Var], iList:List[Instruction]) =>
      varList = vList
      iList.foreach(evalInstruction(_))
      matrix
  }

  def evalInstruction(i:Instruction,
                      mat:Array[Array[Array[MatrixObject]]] = matrix): Unit = i match {
    case Instruction(p: Piece, rel: Relative, pos: Position) => placePiece(p, pos, rel, mat)
    case Instruction(v: VarName, rel: Relative, pos: Position) =>
      placeVariable(createVarMatrix(varNameToInstructionList(v)), pos, rel, mat)
  }

  // First find the max height where we can place the Piece
  // Next place each section of the Piece in the grid, filling space if necessary
  def placePiece(p:Piece,
                 pos:Position,
                 rel: Relative,
                 mat:Array[Array[Array[MatrixObject]]]): Unit = {
    var finalPos = -1
    for (x <- pos.x until pos.x+p.m) {
      for (y <- pos.y until pos.y+p.n) {
        
        rel match {
          // Find first avaiable position, searching from the bottom upwards
          case Relative("at") => {
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
            val curMaxPos = findLastAvailablePos(mat(x)(y).toList)
            if (curMaxPos > finalPos) {
              finalPos = curMaxPos
            }
          }
        }
        
      }
    }

    for (x <- pos.x until pos.x+p.m) {
      for (y <- pos.y until pos.y+p.n) {
        var zDim: Array[MatrixObject] = mat(x)(y)
        while (zDim.length <= finalPos) {
          zDim = zDim :+ MatrixObject()
        }
        zDim(finalPos) = MatrixObject(color = mapping(p.color.toString))
        mat(x)(y) = zDim
      }
    }
  }

  def placeVariable(varMatrix:Array[Array[Array[MatrixObject]]],
                    pos:Position,
                    rel:Relative,
                    mat:Array[Array[Array[MatrixObject]]]): Unit = {
    // Assume rel="at" for now
    var curZAxis = 0
    var searchForFit = true

    while(searchForFit) {
      var allPiecesFit = true

      for (x <- 0 until varMatrix.length) {
        for (y <- 0 until varMatrix(x).length) {
          for (z <- curZAxis until varMatrix(x)(y).length + curZAxis) {
            allPiecesFit &= (z >= mat(x)(y).length || mat(x)(y)(z).color == EmptyPiece)
          }
        }
      }

      if (allPiecesFit) {
        searchForFit = false
        println(curZAxis)
      } else {
        curZAxis += 1
      }
    }
  }

  // Returns the max height of the first available position
  // where a piece could be placed
  def findFirstAvailablePos(list:List[MatrixObject]): Int = {
    def findEmpty(list:List[MatrixObject]): Int = list match {
      case Nil => 0
      case x::rest if x.color == EmptyPiece  => 0
      case x::rest => 1 + findEmpty(rest)
    }
    math.min(list.length, findEmpty(list)) // do I need this?
  }

  def findLastAvailablePos(list:List[MatrixObject]): Int = {
    def findEmpty(list:List[MatrixObject]): Int = list match {
      case Nil => -1
      case rest:+x if x.color == EmptyPiece => -1
      case rest:+x => -1 + findEmpty(rest)
    }
    list.length + findEmpty(list)
  }

  def varNameToInstructionList(varName:VarName): List[Instruction] = {
    varList.foreach(v => if (v.varName == varName) return v.instructionList)
    null
  }

  def createVarMatrix(instructionList:List[Instruction]): Array[Array[Array[MatrixObject]]] = {
    var dynamicMatrix = Array.fill(1,1){Array[MatrixObject]()}
    instructionList.foreach(i => i match{
      case Instruction(_, rel: Relative, pos: Position)
        if pos.x >= dynamicMatrix.length
          || pos.y >= dynamicMatrix(0).length => {
        dynamicMatrix = copyMat(dynamicMatrix, pos.x + 1, pos.y + 1) //If x=2, we need an array of size 3
        evalInstruction(i, mat = dynamicMatrix)
      }
      case _ =>
        println(i.position.x + " " + i.position.y)
        println(dynamicMatrix.deep.mkString("\n"))
        println()
        evalInstruction(i, mat = dynamicMatrix)
    })

    // TODO: Delete
    println(dynamicMatrix.deep.mkString("\n"))
    println("\n")

    dynamicMatrix
  }

  def copyMat(mat:Array[Array[Array[MatrixObject]]],
              x:Int,
              y:Int): Array[Array[Array[MatrixObject]]] = {
    val copyMatrix = Array.fill(x,y){Array[MatrixObject]()}
    for (i <- 0 until mat.length) {
      for (j <- 0 until mat(0).length) {
        copyMatrix(i)(j) = mat(i)(j)
      }
    }
    copyMatrix
  }

}
