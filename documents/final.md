# Progettare: Designing and Visualizing Block Constructions

## Introduction

### Motivation

I used to use a wonderful program called [Lego Digital Designer](http://ldd.lego.com/en-us/) to assist in designing and creating Legos. Input was all through a GUI, and one could purchase the construction or download a set of instructions. However, Lego no longer supports this program, so I would like to design a language which will assist Lego designers in constructing models. There currently exist no other tools to design models via text, so I hope to design a language that is easy to use and informative in its output.

The endgoal is having a simple language containing instructions that will generate a visual model. An external DSL is an appropriate solution since such a language should be easily parsable and interpretable, especially if we restrict the total set of bricks and possible placements of bricks. Bricks could easily be modeled in an IR, and instructions could easily be modeled using these bricks.

### Language domain

The domain of the language is model construction, specifically using Lego bricks. One issue with any type of construction, whether it be car models, knitting, or Legos, is the visualization during construction without having the model. If I can design a tool that will help with on-the-fly visualization, I feel this could benefit any type of constructor, not just Lego creators. Legos, however, are very geometric and simplify the construction process. Additionally, I love Legos, so I want to focus on Lego constructions with this DSL.

### Essence

This tool should be easily usable by any expert of building block construction. Any user, whether they are a programmer or not, should hopefully be able to learn how to design construction with text. Likewise, the output must be extremely useful. A user should be able to render their creations and view them on-the-fly. A user should be able to tweak one small instructions and instantaneously see the difference. This is what sets this tool apart from any other construction tools: Quick and responsive to changes in the model, with an intuitive text-based input.



## Language design

### Input 

To write a program, input is taken in through a text file, which contains the list of variables and instructions. The program takes the path of the text file and parses and interprets the input. 

### Syntax

Syntax was an important design choice. I wanted to maximize readibility and simplicity, while minimizing the learning curve for the program. I designed my language around two key concepts. First, I wanted the program to not look like a typical computer program. When designing variables, which are lists of instructions, I debated using tabs or curly braces to indicate the scope of the variable instructions. While tabs would be cleaner and look more natural, I would have had to write a lexer and parser myself, and I wanted to focus on the bigger picture. Therefore, I chose to use curly braces for scope. 

The second key concept is designing a language that allows users to write the program much like they would design a construction. Variable construction should be natural. Placing pieces and variables in a grid should be natural. A user does not place a piece one by one, referencing the index in the grid. A user should be able to place a small construction in the total grid with as little reference to the "low-level" indices as possible. I wanted to design a language where the user could stay at the "high-level" for the majority of the time.

### Computation

The program takes in a list of variables and and a list of instructions. Instructions are composed of either individual pieces or variables, the way to insert the piece or variable, and a location in a grid to place the piece or variable. A piece is composed of a size and a color. A variable is a small list of instructions with relative locations, used to encapsulate a list into an easily accessible and repeatable object. 

Instructions are processed, line by line, placing either a piece or variable an MxN matrix. The matrix dynamically grows upwards as more pieces are added on top of one another. Variables are stored in an environment accessible by the interpreter.

The output from the program is the 3D matrix, which will be parsed by another DSL that will take this 3D matrix and output a visualization of the construction.

### Data structures

The two basic data structures types are List and a 3D matrix of type Array[Array[Array[MatrixObject]]]. There is also a Map that holds a mapping of Color to an integer value. Two List objects are used to hold all variables and instructions, which are created from the parser-combinator's rep() function. This consumes all matching occurences of a parser until it is exhausted.

The 3D matrix is a 2D matrix of a fixed size containing dynamic arrays. This allows the construction to grow upwards, while remaining in a fixed-size grid. The matrix contains a MatrixObject class, which is wrapper for the Color as an integer and the soon-to-be implemented connectedness booleans. I plan to modify this class into traits that can be mixed in.

For additional features, there will also be mappings of Color to Color. These can be added as an additional parameter to a variable, and will change every color in the variable to its respective color in the mapping. This will simplify the creation process, so a user only needs to build a structure once, and can easily switch out certain colors in the structure.

### Control structures

As of right now, there are no control structures. However, I want to add the ability to have conditionals or for loops, which will simplify the design process. However, since my focus right now is designing a program that is easily usable by all, I don't want to add control structures yet.

### Input and output

The input is a program, which is composed of a list of variables and a list of instructions. The following is an example of a program:
```
Tower {
  2x2 Red Brick at 1,1
  2x2 Red Brick at 1,1
  2x2 Red Brick at 1,1
  2x2 Red Brick at 1,1
}
Base {
  4x4 Black Brick at 1,1
}
FullTower {
  Base at 1,1
  Tower at 2,2
}

FullTower at 1,1
FullTower at 29,1
FullTower at 1,29
FullTower at 29,29
2x2 Yellow Brick at 10,10
```

For additional features, I'd like to have variables that can be assigned to tuple coordinates. This would allow for a simplified system of placing pieces. For example:
```
a1 = (2,2)
Rook at a1
```
I also want to simplify input with either for loops or systems to reference previous blocks or block placements. For example:
```
Pawn {
  2x2 Red Brick at 1,1
  2x2 Red Brick on top
  Place another
  for 1..10, place another
}
```
Finally, I would like to add the ability to define the initial size of the grid. However, these features do not currently exist as valid input.

The output is currently a a 3D matrix. This is meant to be an intermediary between the two DSLs. The following would be some input/output:

Input:
```
2x2 Red Brick at 1,1
2x2 Red Brick at 1,1
2x2 Red Brick at 3,3
```
Output:
```
Array(Array(MatrixObject(1), MatrixObject(1)), Array(MatrixObject(1), MatrixObject(1)), Array(), Array(), ... ,),
Array(Array(MatrixObject(1), MatrixObject(1)), Array(MatrixObject(1), MatrixObject(1)), Array(), ... ,),
...
```

### Error handling

If the input syntax is incorrect, the program will fail to parse the input and inform the user with the default parser-combinator errors. I debated having the program continue on after a failed parse, but it is easier for now to simply fail and show where the error is. I do this with all error checking, where I simply end execution.

Referencing undefined variables will also end execution. There is also a subset of allowed colors, and the program will crash if the color is undefined.

All numerical values must be integers. All values must be in the grid. If a value is out of the grid, the program can either crash. Since a single piece may be partly in and partly out of the grid, I catch if a placement is valid for all parts of the brick, and crash otherwise.

I assume that all legal piece and variable placements are what the user desires. If something seems strange, I assume the user wanted to place the pieces in such a way, and will continue execution in this way.

Finally, the program will catch the error if the user passes an invalid file to the main program.

### Tool support

I would like to add interactive error checking at some point, but that does not currently exist. Additionally, when I put this in the web, I'd like to add basic IDE functionality, such as auto-completion and basic templating for the program.

### Other DSLs in the domain

There are no other DSLs for this domain. This DSL was based off the program "Lego Digital Designer." Instead of text input, the program allowed for the drag-and-drop of pieces into a creation. 

There is also Chrome's [Build With Chrome](https://www.buildwithchrome.com/), a tool for visually designing sets on the web. It has a very small subset of bricks, but is useful for creations. I may choose to interface with this and use this tool to be the output from my DSL. I found a blog where someone had reverse engineered Build With Chrome so one could upload models. I would like to build on his script and tweak it for my 3D matrix output.



## Language implementation

### Host language

Scala's parser-combinator libraries are both powerful and simple. As I will also be using maps and lists in the code, I can use Scala's high order functions for processing the data in these structures. I also wanted to learn more about the hybrid OO and functional programming language, especially learning how to use traits to my benefit. 

### Internal vs. External DSL

This language should be an external DSL. I will need to parse some input and process it. Interally, I would have been unable to cleanly do all this. I do believe this was the best design choice. I wanted to make it so non-programmers could easily use the DSL. In an external language, I control the language much more than an internal langauge, so I can simplify the input.

### Architecure overview

There are four parts to this system: The initial set up, the AST, the parser, and the interpreter. 

The set up will be in a main function that will take in the text file and produce the result. 

The AST encodes the grammar. This includes case classes for Program, Var, and Instruction, along with other wrappers for strings. 

The parser uses JavaTokenParsers to parse the input. It uses the AST classes as wrappers for output. Since the grammar is relatively simple, the parse is relatively clean. I use rep() to consume all instances of variables and instructions, which produces a List() of the respective AST.

Finally, the interpreter will figure out where all parsed input must go in the grid. It will build the 3D array. It must determine everything that stacks on top of each other and the relative placement of pieces and variables in respect to the grid. It will also determine the details, such as how to deal with pieces that are offset when stacked, such as placing a 2x2 brick at the corner of another 2x2 brick, and then placing an "empty" piece below in the space that is left. The matrix will then be passed back to the main function to be used as output.

### "Parsing" and Parsing

I will cover the parsing and the main fuction here. 

First, a user inputs a text file name to the program. The program checks if the file exists, and then continues by passing the raw input to the ProgettareParser.

The parser relies on the AST, which I will talk about in the next section. My grammar is as follows:
```
Program := VarList InstructionList
VarList := Var*
Var := VarName "{" InstructionList "}"
VarName := String
InstructionList := Instruction*
Instruction := (Piece | VarName) Relative Position
Relative := "at" | "above" | "below"
Piece := MxN Color Part
Color := "Red" | "Yellow" | "Blue" | "Green" | ...
Part := "Brick" | ...
Position := X,Y
```
This is a relatively simple grammar. It also does not depend on whitespace. Any instance of '*' means to use rep() to consume all instances of a certain parser. 

Currently, the parser takes any valid string for VarName, Color, or Part. The interpreter will catch if there are any issues. 

An important thing that is not evident is the way the parser handles X and Y coordinates. All positions are offset by (1,1), since I have the users input grid indicies starting at 1,1 instead of 0,0. This is once again to minimize the amount of programming knowledge a user needs.

### IR

The IR is some number of case classes, one for each instruction in the grammar. A Program is composed of a List[Var] and List[Instruction]. Var is composed of a VarName and List[Instruction]. A VarName is just a string. This is true of multiple case classes, where Relative, Part and Color also simply wrap strings in case classes. I also have a Position case class with two methods, '+' and '-', so I may subtract and add positions, since many positions re relative of others.

### Execution

First, the program saves the list of variables from the parse. Then, the program evaluates each instruction.

evalInstruction() processes Instruction objects that contain either a Piece or a Variable. For a Piece, the program passes evaluation off to placePiece(). For a Variable, the program creates a variable matrix, and then passes that too placeVariable().

For both placePiece() and placeVariable(), the execution is relatively the same. I would like to condense these functions into one, since we can treat a single piece as a "variable," but that will be later in the code cleanup. For now, I will discuss the execution of placeVariable(), as placePiece() operates in the same ways.

placeVariable() will search for a fit, tracking the z-axis height at which a fit works. For the first step in execution, the first height to search at is determined. For each relative placement:
* "at" - Search from the base upwards, placing the variable in the first empty spot
* "above" - Find the highest place in the 3D matrix underneath the dynamic matrix, and place
* "below" - Find the highest place, move the search down the max height of the dynamic matrix, and search downwards

We define emptiness as no intersections between the dynamic variable matrix and the total matrix. No intersections occur when the index in the total matrix is currently unoccupied or the index is occupied by an "Empty" piece. This is a piece with no color, meant to be a void block that can be filled. We search for emptiness by checking for no intersections between the two matrices. If one is found, the search moves upwards for "at" or downwards for "below" or "above." 

Once a position is found, we add to the array, for each x,y coordinate, the new matrix value. We fill any space between this matrix value and the original height of the array with Empty pieces, since this empty space can be occupied at some point. During this whole process, we catch any out-of-bounds error that occurs.

I'll also add how the dynamic variable matrix is created. We first create a 1x1 matrix with an empty Array(). Then, for each instruction in the variable, we attempt to place it in the dynamic matrix. If the matrix is too small, in either dimension, we scale the matrix to the proper dimension and then evaluate the instruction under the new dynamic matrix. To create a new matrix, I simply create one of the right size, then copy over all elements. This was discussed with Philip as the simplest operation to accomplish this.

## Evaluation

### DSL-ness

This language is meant to be a DSL. It should never evolve into a Turing complete language, usable in an everyday context. It is much like Context Free, which is to be used only to generate artwork programmatically. This language is meant to be used only for constructing sets with building blocks. 

### What works well? What are you particularly pleased with?

I am pleased at how easy and clean parsing the input was. Everything works well, including edge cases. When designing the product, I put a lot of thought into how to work with the edge cases. Instead of having conditionals to handle certain cases, I worked them into the main body of each function, which resulted in simplified functions. 

Also, I believe this program does indeed fill the niche that I wanted to fill. One can design a set with text, and get the output of a matrix that will be used to render the set in 3D.

### What could be improved? For example, how could the user's experience be better? How might your implementation be simpler or more cohesive?

There are a couple things on my list that I would like to implement. First, and this is simple, I would like to allow users to map variable names to coordinates, such as a1 = 1,1, so users can make reference to variables instead of raw coordinates.

I also would like to add two things for variables: The ability to map colors to colors, as I've mentioned before, and rotations of matrices.

Finally, I need to add error checking. The program will gracefully catch errors, but not give useful messages.

### Re-visit your evaluation plan from the beginning of the project. Which tools have you used to evaluate the quality of your design? What have you learned from these evaluations? Have you made any significant changes as a result of these tools, the critiques, or user tests?

The only tool I have used so far is Scala. I used JavaTokenParsers, which worked very well. I plan to learn about Unity and Povray for the 3D rendering. The critiques have been helpful, as people have answered my questions about how to handle things. However, I haven't had much need for outside tools, yet.

### Where did you run into trouble and why? For example, did you come up with some syntax that you found difficult to implement, given your host language choice? Did you want to support multiple features, but you had trouble getting them to play well together?

The first thing I changed was the syntax. I wanted to implement a lexer/parser for tabs instead of curly braces, but found this really hard. I therefore went with curly braces. 

I also had/am having difficulty with dynamically created matrices for the variables. Right now, I am implementing a way to reference pieces above or below the previous placement, which makes it so I can design any set, although it would be verbose. Next, I am working on creating dynamic grids for variables, and placing that whole object in the grid. There are some very annoying edge cases, such as checking for connectedness amongst pieces, but that will be dealt with later.

### What's left to accomplish before the end of the project?

As I mentioned, implementing these relative placements, and then designing the dynamic grids for variables. I need error checking also. I would like to implement some of the extra user features too.
