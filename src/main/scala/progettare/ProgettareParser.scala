package progettare

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
 * Instruction := (Piece | VarName) Relative Position
 * Relative := "at" | "above" | "below"
 * Piece := MxN Color Part
 * Color := "Red" | "Yellow" | "Blue" | "Green" | ...
 * Part := "Brick" | ...
 * Position := X,Y
 */
object ProgettareParser extends JavaTokenParsers {

  // Offset all positions by subtracting 1,1, so the origin for the interpreter is 0,0
  val OffsetPosition = Position(1,1)

  def apply(s: String): ParseResult[AST] = parseAll(program, s)

  def program: Parser[Program] = varList~instructionList ^^ {case vL~iL => Program(vL,iL)}

  def varList: Parser[List[Var]] = rep(var_t)
  def var_t: Parser[Var] = varName~"{"~instructionList~"}" ^^ {case v~"{"~iL~"}" => Var(v, iL)}
  def varName: Parser[VarName] = "\\w+".r ^^ VarName

  def instructionList: Parser[List[Instruction]] = rep(instruction)
  def instruction: Parser[Instruction] = (piece~relative~position ^^ {case p~rel~pos =>
                                                                        Instruction(p,rel,pos - OffsetPosition)}
                                        | varName~relative~position ^^ {case v~rel~pos =>
                                                                        Instruction(v,rel,pos - OffsetPosition)} )
  def relative: Parser[Relative] = """at|above|below""".r ^^ Relative
  def piece: Parser[Piece] = wholeNumber~"x"~wholeNumber~color~part ^^
    {case m~"x"~n~c~p => Piece(m.toInt,n.toInt,c,p)}
  def color: Parser[Color] = "\\w+".r ^^ Color
  def part: Parser[Part] = "Brick".r ^^ (x => Part(x))
  def position: Parser[Position] = wholeNumber~","~wholeNumber ^^ {case x~","~y => Position(x.toInt,y.toInt)}

}
