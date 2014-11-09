package legoconstruction

import scala.util.parsing.combinator._

/**
 * Created by Hayden Blauzvern on 11/9/14.
 *
 * The grammar is as specified:
 *
 * Program := VarList InstructionList
 * VarList := Var*
 * Var := VarName "{" InstructionList "}"
 * VarName := String
 * InstructionList := Instruction*
 * Instruction := Piece "at" Position
 * Piece := MxN Color Part
 * Color := "Red" | "Yellow" | "Blue" | "Green" | ...
 * Part := "Brick" | ...
 * Position := X,Y
 */
object LegoParser extends JavaTokenParsers {


  def part: Parser[Part] = "Brick".r ^^ {case x => Part(x)}
  def position: Parser[Position] = decimalNumber~decimalNumber ^^ {case x~y => Position(x.toInt,y.toInt)}

}
