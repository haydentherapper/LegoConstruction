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
    case _ => throw new QuietException("Bad evaluation")
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
    try {
      var finalPos = -1
      for (x <- pos.x until pos.x + p.m;
           y <- pos.y until pos.y + p.n) {
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

      for (x <- pos.x until pos.x + p.m;
           y <- pos.y until pos.y + p.n) {
        var zDim: Array[MatrixObject] = mat(x)(y)
        while (zDim.length <= finalPos) {
          zDim = zDim :+ MatrixObject()
        }
        try {
          mapping(p.color.toString)
        } catch {
          case e: java.util.NoSuchElementException => throw new QuietException("Color \"" + p.color.color + "\" not found")
        }
        zDim(finalPos) = MatrixObject(color = mapping(p.color.toString))
        mat(x)(y) = zDim
      }
    } catch {
      case e: Exception => throw new QuietException("Piece placed out of bounds or too large for grid")
    }
  }

  def placeVariable(varMatrix:Array[Array[Array[MatrixObject]]],
                    pos:Position,
                    rel:Relative,
                    mat:Array[Array[Array[MatrixObject]]]): Unit = {
    try {
      // Assume rel="at" for now
      var curZAxis = -1
      var searchForFit = true

      // Search for starting z axs
      rel match {
        case Relative("at") => curZAxis = 0
        case Relative("above") => {
          for (x <- 0 until varMatrix.length;
               y <- 0 until varMatrix(x).length;
               z <- 0 until varMatrix(x)(y).length) {
            if (mat(x + pos.x)(y + pos.y).length > curZAxis) curZAxis = mat(x + pos.x)(y + pos.y).length
          }
        }
        case Relative("below") => {
          var maxHeight = 0
          for (x <- 0 until varMatrix.length;
               y <- 0 until varMatrix(x).length;
               z <- 0 until varMatrix(x)(y).length) {
            if (mat(x + pos.x)(y + pos.y).length > curZAxis) curZAxis = mat(x + pos.x)(y + pos.y).length
            if (varMatrix(x)(y).length > maxHeight) maxHeight = varMatrix(x)(y).length
          }
          curZAxis -= maxHeight
        }
      }

      // Next, search for a fit based on the relative position
      while (searchForFit) {
        var allPiecesFit = true

        for (x <- 0 until varMatrix.length;
             y <- 0 until varMatrix(x).length;
             z <- curZAxis until varMatrix(x)(y).length + curZAxis) {
          allPiecesFit &= (z >= mat(x + pos.x)(y + pos.y).length || mat(x + pos.x)(y + pos.y)(z).color == EmptyPiece)
        }

        // TO DO: Add error checking for if curZAxis goes negative
        // TO DO: Do I actually need to have "above" go lower at any point?
        if (allPiecesFit) {
          searchForFit = false
        } else {
          rel match {
            case Relative("at") => curZAxis += 1
            case Relative("below") | Relative("above") => curZAxis -= 1
          }
        }
      }

      for (x <- 0 until varMatrix.length;
           y <- 0 until varMatrix(x).length;
           z <- 0 until varMatrix(x)(y).length) {
        var zDim: Array[MatrixObject] = mat(x + pos.x)(y + pos.y)
        while (zDim.length <= z + curZAxis) {
          zDim = zDim :+ MatrixObject()
        }
        zDim(z + curZAxis) = MatrixObject(color = varMatrix(x)(y)(z).color)
        mat(x + pos.x)(y + pos.y) = zDim
      }
    } catch {
      case e: Exception => throw new QuietException("Variable placed out of bounds or too large for grid")
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

  // TO DO: Returning -1 means the piece is not found, and that needs to be caught
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
    throw new QuietException("Variable not found")
  }

  def createVarMatrix(instructionList: List[Instruction]): Array[Array[Array[MatrixObject]]] = {
    var dynamicMatrix = Array.fill(1,1){Array[MatrixObject]()}
    instructionList.foreach({
      // The position plus the piece size -1 gives us if a piece is too big for the matrix
      // -1 accounts for the piece itself. If we place a 1x1 piece at 0,0, we only need
      // to resize once a piece larger than 1x1 is placed.
      case i @ Instruction(p: Piece, rel: Relative, pos: Position)
        if pos.x + p.m - 1 >= dynamicMatrix.length
          && pos.y + p.n - 1 < dynamicMatrix(0).length => {
        dynamicMatrix = copyMat(dynamicMatrix, pos.x + p.m, dynamicMatrix(0).length)
        evalInstruction(i, mat = dynamicMatrix)
      }

      case i @ Instruction(p: Piece, rel: Relative, pos: Position)
        if pos.x + p.m - 1 < dynamicMatrix.length
          && pos.y + p.n - 1 >= dynamicMatrix(0).length => {
        dynamicMatrix = copyMat(dynamicMatrix, dynamicMatrix.length, pos.y + p.n)
        evalInstruction(i, mat = dynamicMatrix)
      }

      case i @ Instruction(p: Piece, rel: Relative, pos: Position)
        if pos.x + p.m - 1 >= dynamicMatrix.length
          && pos.y + p.n -1 >= dynamicMatrix(0).length => {
        dynamicMatrix = copyMat(dynamicMatrix, pos.x + p.m, pos.y + p.n)
        evalInstruction(i, mat = dynamicMatrix)
      }

      case Instruction(v: VarName, rel: Relative, pos: Position) => throw new Exception("Unsupported")

      case i => evalInstruction(i, mat = dynamicMatrix)
    })
    dynamicMatrix
  }

  def copyMat(mat: Array[Array[Array[MatrixObject]]],
              x: Int,
              y: Int): Array[Array[Array[MatrixObject]]] = {
    val copyMatrix = Array.fill(x,y){Array[MatrixObject]()}
    for (i <- 0 until mat.length;
         j <- 0 until mat(0).length) {
      copyMatrix(i)(j) = mat(i)(j)
    }
    copyMatrix
  }

}
