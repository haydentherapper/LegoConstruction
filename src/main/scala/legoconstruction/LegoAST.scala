package legoconstruction

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

sealed abstract class AST

case class Program(varList: VarList, instructionList: InstructionList) extends AST
case class VarList(varList: List[Var]) extends AST
case class Var(varName: VarName, instructionList: InstructionList) extends AST
case class VarName(varName: String) extends AST
case class InstructionList(instructionList: List[Instruction]) extends AST
case class Instruction(piece: Piece, position: Position) extends AST
case class Piece(m: Int, n: Int, color: Color, part: Part) extends AST
case class Color(color: String) extends AST
case class Part(part: String) extends AST
case class Position(x: Int, y: Int)