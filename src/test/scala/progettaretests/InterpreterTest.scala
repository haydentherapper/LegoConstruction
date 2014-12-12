package progettaretests

import org.scalatest.{BeforeAndAfter, FunSpec}
import progettare.ProgettareInterpreter.{MatrixObject, matrix}
import progettare._

/**
 * Created by Hayden Blauzvern on 12/11/14.
 */
class InterpreterTest extends FunSpec with BeforeAndAfter {

  def evaluate(s: String, matrix: Array[Array[Array[MatrixObject]]]): Boolean = {
    val parseResult = ProgettareParser(s).get
    ProgettareInterpreter.eval(parseResult).deep == matrix.deep
  }

  def reset(matrix: Array[Array[Array[MatrixObject]]]) = {
    for(x <- 0 until matrix.length;
        y <- 0 until matrix(0).length) {
      matrix(x)(y) = Array()
    }
  }

  after {
    reset(matrix)
  }

  describe("A program") {
    describe("without instructions") {
      it("should output an empty 3D matrix") {
        val matrix = Array.fill(32,32){Array[MatrixObject]()}
        val s =
          """
            |
          """.stripMargin
        assert(evaluate(s, matrix))
      }
    }
    describe("with instructions and without variables") {
      describe("with simple instructions") {
        it("should stack the pieces in the z-axis direction") {
          val matrix = Array.fill(32,32){Array[MatrixObject]()}
          matrix(0)(0) = Array(MatrixObject(1), MatrixObject(1), MatrixObject(1))
          val s =
          """1x1 Red Brick at 1,1
            |1x1 Red Brick at 1,1
            |1x1 Red Brick at 1,1
          """.stripMargin
          assert(evaluate(s, matrix))
        }
      }
      describe("with instructions with different grid positions") {
        it("should place 1x1 bricks across the matrix") {
          val matrix = Array.fill(32,32){Array[MatrixObject]()}
          matrix(0)(0) = Array(MatrixObject(1))
          matrix(4)(4) = Array(MatrixObject(1))
          matrix(9)(9) = Array(MatrixObject(1))
          val s =
            """1x1 Red Brick at 1,1
              |1x1 Red Brick at 5,5
              |1x1 Red Brick at 10,10
            """.stripMargin
          assert(evaluate(s, matrix))
        }
      }
      describe("with instruction with different sized bricks") {
        it("should place 2x2 bricks on top of each other") {
          val matrix = Array.fill(32,32){Array[MatrixObject]()}
          matrix(0)(0) = Array(MatrixObject(1), MatrixObject(1))
          matrix(0)(1) = Array(MatrixObject(1), MatrixObject(1))
          matrix(1)(0) = Array(MatrixObject(1), MatrixObject(1))
          matrix(1)(1) = Array(MatrixObject(1), MatrixObject(1))
          val s =
            """2x2 Red Brick at 1,1
              |2x2 Red Brick at 1,1
            """.stripMargin
          assert(evaluate(s, matrix))
        }
      }
      describe("with instructions that stack bricks offset") {
        it("should place 2x2 bricks at the corners and have empty pieces") {
          val matrix = Array.fill(32,32){Array[MatrixObject]()}
          matrix(0)(0) = Array(MatrixObject(1))
          matrix(0)(1) = Array(MatrixObject(1))
          matrix(1)(0) = Array(MatrixObject(1))
          matrix(1)(1) = Array(MatrixObject(1), MatrixObject(1))
          matrix(1)(2) = Array(MatrixObject(), MatrixObject(1))
          matrix(2)(1) = Array(MatrixObject(), MatrixObject(1))
          matrix(2)(2) = Array(MatrixObject(), MatrixObject(1))
          val s =
            """2x2 Red Brick at 1,1
              |2x2 Red Brick at 2,2
            """.stripMargin
          assert(evaluate(s, matrix))
        }
      }
      describe("with instructions that use other relative preposition") {
        it("should fill empty piece space using \"below\"") {
          val matrix = Array.fill(32,32){Array[MatrixObject]()}
          matrix(0)(0) = Array(MatrixObject(1))
          matrix(0)(1) = Array(MatrixObject(1))
          matrix(1)(0) = Array(MatrixObject(1))
          matrix(1)(1) = Array(MatrixObject(1), MatrixObject(1))
          matrix(1)(2) = Array(MatrixObject(), MatrixObject(1))
          matrix(2)(1) = Array(MatrixObject(), MatrixObject(1))
          matrix(2)(2) = Array(MatrixObject(1), MatrixObject(1))
          matrix(2)(3) = Array(MatrixObject(1))
          matrix(3)(2) = Array(MatrixObject(1))
          matrix(3)(3) = Array(MatrixObject(1))
          val s =
            """2x2 Red Brick at 1,1
              |2x2 Red Brick at 2,2
              |2x2 Red Brick below 3,3
            """.stripMargin
          assert(evaluate(s, matrix))
        }
        it("should fill place the piece higher up using \"above\"") {
          val matrix = Array.fill(32,32){Array[MatrixObject]()}
          matrix(0)(0) = Array(MatrixObject(1))
          matrix(0)(1) = Array(MatrixObject(1))
          matrix(1)(0) = Array(MatrixObject(1))
          matrix(1)(1) = Array(MatrixObject(1), MatrixObject(1))
          matrix(1)(2) = Array(MatrixObject(), MatrixObject(1))
          matrix(2)(1) = Array(MatrixObject(), MatrixObject(1))
          matrix(2)(2) = Array(MatrixObject(0), MatrixObject(1), MatrixObject(1))
          matrix(2)(3) = Array(MatrixObject(0),MatrixObject(0),MatrixObject(1))
          matrix(3)(2) = Array(MatrixObject(0),MatrixObject(0),MatrixObject(1))
          matrix(3)(3) = Array(MatrixObject(0),MatrixObject(0),MatrixObject(1))
          val s =
            """2x2 Red Brick at 1,1
              |2x2 Red Brick at 2,2
              |2x2 Red Brick above 3,3
            """.stripMargin
          assert(evaluate(s, matrix))
        }
      }
    }
    describe("with instructions and variables") {
      describe("with the variable placed in the upper-left-most corner") {
        it("should place the variable in the grid") {
          val matrix = Array.fill(32,32){Array[MatrixObject]()}
          matrix(0)(0) = Array(MatrixObject(1), MatrixObject(1))
          val s =
            """Var {
              | 1x1 Red Brick at 1,1
              | 1x1 Red Brick at 1,1
              |}
              |
              |Var at 1,1
            """.stripMargin
          assert(evaluate(s, matrix))
        }
      }
      describe("with the variable offset in the instruction") {
        it("should place the variable relative to the offset") {
          val matrix = Array.fill(32,32){Array[MatrixObject]()}
          matrix(1)(1) = Array(MatrixObject(1), MatrixObject(1))
          val s =
            """Var {
              | 1x1 Red Brick at 1,1
              | 1x1 Red Brick at 1,1
              |}
              |
              |Var at 2,2
            """.stripMargin
          assert(evaluate(s, matrix))
        }
      }
      describe("placing the variable with \"at\"") {
        it("should place the variable in the first available spot from the base upwards") {
          val matrix = Array.fill(32,32){Array[MatrixObject]()}
          matrix(0)(0) = Array(MatrixObject(1), MatrixObject(1), MatrixObject(1), MatrixObject(1))
          matrix(0)(1) = Array(MatrixObject(1), MatrixObject(1), MatrixObject(), MatrixObject(1))
          matrix(0)(2) = Array(MatrixObject(), MatrixObject(1), MatrixObject(1))
          val s =
            """Var {
              | 1x2 Red Brick at 1,1
              | 1x1 Red Brick at 1,1
              | 1x1 Red Brick at 1,1
              | 1x2 Red Brick at 1,1
              |}
              |VarToPlace {
              | 1x2 Red Brick at 1,1
              | 1x1 Red Brick at 1,2
              |}
              |
              |Var at 1,1
              |VarToPlace at 1,2
            """.stripMargin
          assert(evaluate(s, matrix))
        }
      }
      describe("placing the variable with \"below\"") {
        it("should place the variable in the first available spot the highest point downwards") {
          val matrix = Array.fill(32,32){Array[MatrixObject]()}
          matrix(0)(0) = Array(MatrixObject(1), MatrixObject(1), MatrixObject(1), MatrixObject(1))
          matrix(0)(1) = Array(MatrixObject(1), MatrixObject(), MatrixObject(1), MatrixObject(1))
          matrix(0)(2) = Array(MatrixObject(), MatrixObject(), MatrixObject(1), MatrixObject(1))
          val s =
            """Var {
              | 1x2 Red Brick at 1,1
              | 1x1 Red Brick at 1,1
              | 1x1 Red Brick at 1,1
              | 1x2 Red Brick at 1,1
              |}
              |VarToPlace {
              | 1x2 Red Brick at 1,1
              | 1x1 Red Brick at 1,2
              |}
              |
              |Var at 1,1
              |VarToPlace below 1,2
            """.stripMargin
          assert(evaluate(s, matrix))
        }
        it("should place the variable even if there is only enough space and no extra space") {
          val matrix = Array.fill(32,32){Array[MatrixObject]()}
          matrix(0)(0) = Array(MatrixObject(1), MatrixObject(1), MatrixObject(1), MatrixObject(1))
          matrix(0)(1) = Array(MatrixObject(1), MatrixObject(1), MatrixObject(1), MatrixObject(1))
          matrix(0)(2) = Array(MatrixObject(), MatrixObject(1), MatrixObject(1))
          val s =
            """Var {
              | 1x2 Red Brick at 1,1
              | 1x1 Red Brick at 1,1
              | 1x1 Red Brick at 1,1
              | 1x2 Red Brick at 1,1
              |}
              |VarToPlace {
              | 1x2 Red Brick at 1,1
              | 1x2 Red Brick at 1,1
              |}
              |
              |Var at 1,1
              |VarToPlace below 1,2
            """.stripMargin
          assert(evaluate(s, matrix))
        }
      }
      describe("placing the variable with \"above\"") {
        it("should place the variable in the highest available cell") {
          val matrix = Array.fill(32,32){Array[MatrixObject]()}
          matrix(0)(0) = Array(MatrixObject(1), MatrixObject(1), MatrixObject(1), MatrixObject(1))
          matrix(0)(1) = Array(MatrixObject(1), MatrixObject(), MatrixObject(), MatrixObject(1), MatrixObject(1))
          matrix(0)(2) = Array(MatrixObject(), MatrixObject(), MatrixObject(), MatrixObject(), MatrixObject(1), MatrixObject(1))
          val s =
            """Var {
              | 1x2 Red Brick at 1,1
              | 1x1 Red Brick at 1,1
              | 1x1 Red Brick at 1,1
              | 1x2 Red Brick at 1,1
              |}
              |VarToPlace {
              | 1x2 Red Brick at 1,1
              | 1x1 Red Brick at 1,2
              |}
              |
              |Var at 1,1
              |VarToPlace above 1,2
            """.stripMargin
          assert(evaluate(s, matrix))
        }
      }
    }
    describe("with errors") {
      describe("with undefined variables") {
        it("should throw an exception") {
          val matrix = Array.fill(32,32){Array[MatrixObject]()}
          val s =
            """ UnknownVar at 2,2
            """.stripMargin
          intercept[QuietException] {
            assert(evaluate(s, matrix))
          }
        }
      }
      describe("with out-of-bounds placement") {
        describe("of a piece") {
          it("should throw an exception") {
            val matrix = Array.fill(32,32){Array[MatrixObject]()}
            val s =
              """ 1x1 Red Brick at 33,33
              """.stripMargin
            intercept[QuietException] {
              assert(evaluate(s, matrix))
            }
          }
        }
        describe("of a piece that is too big") {
          it("should throw an exception") {
            val matrix = Array.fill(32,32){Array[MatrixObject]()}
            val s =
              """ 33x33 Red Brick at 1,1
              """.stripMargin
            intercept[QuietException] {
              assert(evaluate(s, matrix))
            }
          }
        }
        describe("of a variable") {
          it("should throw an exception") {
            val matrix = Array.fill(32,32){Array[MatrixObject]()}
            val s =
              """ Var {
                 | 2x2 Red Brick at 33,33
                 |}
                 |Var at 1,1
              """.stripMargin
            intercept[QuietException] {
              assert(evaluate(s, matrix))
            }
          }
        }
      }
      describe("with an undefined color color") {
        it("should throw an exception") {
          val matrix = Array.fill(32,32){Array[MatrixObject]()}
          val s =
            """ 1x1 Violet Brick at 1,1
            """.stripMargin
          intercept[QuietException] {
            assert(evaluate(s, matrix))
          }
        }
      }
    }
  }

}
