# Language design and implementation overview

## Language design

#### How does a user write programs in your language (e.g., do they type in commands, use a visual/graphical tool, speak, etc.)?

Input is taken in through a text file, which contains the list of variables and instructions. A wrapper might eventually be created that would allow the users to write all instructions and variables in a GUI, but input would still be taken in as lines of text.

#### What is the basic computation that your language performs (i.e., what is the computational model)?

The program takes in a set of variables and instructions. Instructions are composed of either individual Lego pieces or variables, and a location in a grid to place the piece/variable. Variables are simply a small set of instructions, which can be used to simplify the writing of instructions. 

Instructions are processed, line by line, placing each piece referenced in the line in an MxN matrix. The matrix dynamically grows upwards as more pieces are added on top of one another. 

The output from the program is the 3D matrix, which will be parsed by another DSL that will take this 3D matrix and output a visualization of the set.

#### What are the basic data structures in your DSL, if any? How does a the user create and manipulate data?

The two basic data structures are lists and a 3D matrix. Two lists are used to hold all variables and instructions, which are created from the parser-combinator's rep() function. 

The 3D matrix is a 2D matrix of a fixed size containing dynamic arrays. This allows the set to grow upwards, while remaining in a fixed-size grid.

There are also going to be mappings of colors to colors. These can be added as an additional parameter to a variable, and will change every color in the variable to its respective color in the mapping. This will simplify the creation process, so a user only needs to build a structure once, and can easily switch out certain colors in the structure.

#### What are the basic control structures in your DSL, if any? How does the user specify or manipulate control flow?

As of right now, there are no control structures. However, I may add the ability to have conditionals or for loops, which will simplify the design process. However, since my focus right now is designing a program that is easily usable by all, I don't want to add control structures yet.

#### What kind(s) of input does a program in your DSL require? What kind(s) of output does a program produce?

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
The input has changed slightly since the original syntax, which would have replaced curly braces with tabs. This would have simplified what the user had to type, but greatly increased the difficulty of parsing the program. Therefore, I have decided to go with curly braces for now.

One new idea I am toying with is having variables that can be assigned tuple coordinates. This would allow for a simplified system of placing pieces. For example:
```
a1 = (2,2)
Rook at a1
```
I also may want to simplify input with either for loops or systems to reference previous blocks or block placements. For example:
```
Pawn {
  2x2 Red Brick at 1,1
  2x2 Red Brick on top
  Place another
  for 1..10, place another
}
```
One final idea is adding a line to define the initial size of the grid. However, these do not currently exist as valid input.

The output will be a 3D matrix, represented as a serialized matrix in a JSON format. The following would be some input/output:

Input:
```
2x2 Red Brick at 1,1
2x2 Red Brick at 1,1
2x2 Red Brick at 3,3
```
Output:
```
{
  r1: {
        c1: [Red Brick, Red Brick]
        c2: [Red Brick, Red Brick]
        c3: []
        ...
      }
  r2: {
        c1: [Red Brick, Red Brick]
        c2: [Red Brick, Red Brick]
        ...
      }
  r3: {
        c1: []
        c2: []
        c3: [Red Brick]
        c4: [Red Brick]
        ...
      }
  r4: {
        ...
        c3: [Red Brick]
        c4: [Red Brick]
        ...
      }
  ...
}
```

#### Error handling: How can programs go wrong, and how does your language communicate those errors to the user?

If the input syntax is incorrect, the program will fail to parse the input and inform the user of where the error was. 

Referencing undefined variables would also crash the program. I may choose to implement a setting where the program can continue, and the instruction will simply not be executed.

All numerical values must be integers. All values must be in the grid. If a value is out of the grid, the program can either crash or continue on. Since a single piece may be partly in and partly out of the grid, I will need to calculate if a placement is valid for all parts of the brick.

I assume that all legal piece and variable placements are what the user desires. If something seems strange, I assume the user wanted to place the pieces in such a way, and will continue execution in this way.

#### What tool support (e.g., error-checking, development environments) does your project provide?

It will have error-checking during the parsing step. Additionally, the second DSL will allow for the Lego program to be visualized. Users can change small parts of the program and re-render the image in realtime, allowing for creation of a set without physically designing and tweaking it.

#### Are there any other DSLs for this domain? If so, what are they, and how does your language compare to these other languages?

There are no other DSLs for this domain. This DSL was based off the program "Lego Digital Designer." Instead of text input, the program allowed for the drag-and-drop of pieces into a creation. 

## Language implementation

#### Your choice of an internal vs. external implementation and how and why you made that choice.

This language should be an external DSL. I will need to parse some input and process it. Interally, I would have been unable to cleanly do all this.

#### Your choice of a host language and how and why you made that choice.

Scala's parser-combinator libraries that exist are both powerful and simple. As I will also be using maps and lists in the code, I can use Scala's high order functions for processing of the data structures.

#### Any significant syntax design decisions you've made and the reasons for those decisions.

The main choice was to move away from tabs and use curly braces. While this looks more like a programming language now, it simplified the parsing and allowed me to focus on bigger details. I believe this does not detract from the overall usability of the language.

The two things I plan to add before the end are the ability for dynamically sized grids and variable names for coordinates. Both will simplify the user's experience. I'd also like to add control structures and ways to reference previous bricks or placements in the grid, but these are features which can be implemented later.

#### An overview of the architecture of your system.

