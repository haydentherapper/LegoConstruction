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
 * Instruction := (Piece | VarName) "at" Position (Maybe I should have at/above/below)
 * Piece := MxN Color Part
 * Color := "Red" | "Yellow" | "Blue" | "Green" | ...
 * Part := "Brick" | ...
 * Position := X,Y
 */
object ProgettareParser extends JavaTokenParsers {

  def apply(s: String): ParseResult[AST] = parseAll(program, s)

  def program: Parser[Program] = varList~instructionList ^^ {case vL~iL => Program(vL,iL)}

  def varList: Parser[List[Var]] = rep(var_t)
  def var_t: Parser[Var] = varName~"{"~instructionList~"}" ^^ {case v~"{"~iL~"}" => Var(v, iL)}
  def varName: Parser[VarName] = "\\w+".r ^^ VarName

  def instructionList: Parser[List[Instruction]] = rep(instruction)
  def instruction: Parser[Instruction] = (piece~"at"~position ^^ {case p~"at"~pos => Instruction(p,pos)}
                                        | varName~"at"~position ^^ {case v~"at"~pos => Instruction(v,pos)} )
  def piece: Parser[Piece] = wholeNumber~"x"~wholeNumber~color~part ^^
    {case m~"x"~n~c~p => Piece(m.toInt,n.toInt,c,p)}
  def color: Parser[Color] = "\\w+".r ^^ Color
  def part: Parser[Part] = "Brick".r ^^ (x => Part(x))
  def position: Parser[Position] = wholeNumber~","~wholeNumber ^^ {case x~","~y => Position(x.toInt,y.toInt)}

}
