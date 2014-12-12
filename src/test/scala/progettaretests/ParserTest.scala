package progettaretests

import org.scalatest.FunSpec
import progettare._

/**
 * Created by Hayden Blauzvern on 12/8/14.
 */

class ParserTest extends FunSpec {

  describe("A program") {
    describe("without variables and without instructions") {
      it("should output two empty lists") {
        val s =
          """
            |
          """.stripMargin
        assert(ProgettareParser(s).get == Program(List(), List()))
      }
    }
    describe("without variables") {
      it("should output a list of instructions") {
        val s =
          """2x2 Yellow Brick at 1,1
            |2x2 Red Brick at 2,2
            |2x2 Green Brick at 2,2
          """.stripMargin
        assert(ProgettareParser(s).get == Program(List(),
          List(
            Instruction(Piece(2, 2, Color("Yellow"), Part("Brick")), Relative("at"), Position(0, 0)),
            Instruction(Piece(2, 2, Color("Red"), Part("Brick")), Relative("at"), Position(1, 1)),
            Instruction(Piece(2, 2, Color("Green"), Part("Brick")), Relative("at"), Position(1, 1)))))
      }
    }
    describe("with variables and without instructions") {
      it("should output a list of variables with no instructions") {
        val s =
          """Pawn {
            | 2x2 Red Brick at 1,1
            |}
          """.stripMargin
        assert(ProgettareParser(s).get == Program(List(
          Var(VarName("Pawn"),
            List(Instruction(Piece(2, 2, Color("Red"), Part("Brick")), Relative("at"), Position(0, 0))))),
          List()))
      }
    }
    describe("with variables and with instructions") {
      describe("with only one variable and one instruction referencing the variable") {
        it("should output a list with the variable and one instruction") {
          val s =
            """Pawn {
              | 2x2 Red Brick at 1,1
              | }
              |
              | Pawn at 2,2
            """.stripMargin
          assert(ProgettareParser(s).get == Program(List(
            Var(VarName("Pawn"),
              List(Instruction(Piece(2, 2, Color("Red"), Part("Brick")), Relative("at"), Position(0, 0))))),
            List(Instruction(VarName("Pawn"), Relative("at"), Position(1, 1)))))
        }
      }
      describe("with multiple variables and instructions") {
        it("should output a list of variables and list of instructions") {
          val s =
            """Pawn {
              | 2x2 Red Brick at 1,1
              |}
              |
              |Rook {
              | 2x2 Red Brick at 2,2
              |}
              |
              |2x2 Yellow Brick at 1,1
              |Pawn at 6,1
              |Rook at 9,1
            """.stripMargin
          assert(ProgettareParser(s).get == Program(List(Var(VarName("Pawn"),
            List(Instruction(Piece(2, 2, Color("Red"), Part("Brick")), Relative("at"), Position(0, 0)))),
            Var(VarName("Rook"), List(Instruction(Piece(2, 2, Color("Red"), Part("Brick")), Relative("at"), Position(1, 1))))),
            List(Instruction(Piece(2, 2, Color("Yellow"), Part("Brick")), Relative("at"), Position(0, 0)),
              Instruction(VarName("Pawn"), Relative("at"), Position(5, 0)),
              Instruction(VarName("Rook"), Relative("at"), Position(8, 0)))))
        }
        it("should be handle to handle multiple instructions in a variable") {
          val s =
            """Pawn {
              | 2x2 Red Brick at 1,1
              | 2x2 Yellow Brick at 1,1
              | }
              |
              | Pawn at 6,1
            """.stripMargin
          assert(ProgettareParser(s).get == Program(
            List(Var(VarName("Pawn"),
              List(Instruction(Piece(2, 2, Color("Red"), Part("Brick")), Relative("at"), Position(0, 0)),
                Instruction(Piece(2, 2, Color("Yellow"), Part("Brick")), Relative("at"), Position(0, 0))))),
            List(Instruction(VarName("Pawn"), Relative("at"), Position(5, 0)))))
        }
      }
    }
    describe("with errors") {
      it("should throw an error") {
        val s =
          """ Var (
            | 2x2 Red Brick at 1,1
            |)
          """.stripMargin
        intercept[RuntimeException] {
          ProgettareParser(s).get
        }
      }
      it("should be unsuccessful") {
        val s =
          """ Var (
            | 2x2 Red Brick at 1,1
            |)
          """.stripMargin
        assert(!ProgettareParser(s).successful)
      }
    }
  }
}
