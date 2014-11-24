# Lego Construction
### Designing and visualizing Lego constructions

This project aims to allow users to render visual representations of building blocks on the fly.
This will be accomplished in two stages, through two DSLs:

* Input of the list of instructions and output of a 3D matrix representing the 3D building space
* Input of the 3D matrix and output as a 3D visual rendering of the construction

This repository is of the first DSL.

### Running the program

```
sbt run
sbt compile
```
Then, enter the name of the text file containing the instructions. A sample one is given at samples/sample.in.

### Input to the program

Input currently is given as a list of variables and instructions. The following represents valid input:

```
Pawn {
 2x2 Red Brick at 0,0
}

Rook {
 2x2 Red Brick at 1,1
 2x2 Yellow Brick at 2,2
}

2x2 Yellow Brick at 0,0
2x2 Red Brick at 1,1
2x2 Black Brick at 1,1
Pawn at 5,0
Rook at 8,0
```
