# Project description and plan

## Motivation
Why is this project useful or interesting, what problem are you trying to address, and why is a DSL an appropriate solution?

I used to use a wonderful program called [Lego Digital Designer](http://ldd.lego.com/en-us/) to assist in creating Legos. Input was all through the GUI, and one could purchase the construction or download a set of instructions. However, Lego no longer supports this program, so I would like to design a language which will assist Lego designers in constructing models. There currently exist no other tools to design models via text, so I hope to design a language that is easy to use and informative in its output.

The endgoal is having a simple language containing instructions that will generate a visual model. An external DSL is an appropriate solution since such a language should be easily parsable and interpretable, especially if we restrict the total set of bricks and possible placements of bricks. Bricks could easily be modeled in an IR, and instructions could easily be modeled using these bricks.

## Language domain
What is the domain that this language addresses, and why is the domain useful? Who will benefit from this language? Are there any other DSLs for this domain? If so, what are they, and how might they influence your language design and implementation?

The domain of the language is model construction, specifically using Lego bricks. One issue with any type of construction, whether it be car models, knitting, or Legos, is the visualization during construction without having the model. If I can design a tool that will help with on-the-fly visualization, I feel this could benefit any type of constructor, not just Lego creators. Legos, however, are very geometric and simplify the construction process. Additionally, I love Legos, so I want to focus on Lego constructions with this DSL.

There are no current DSLs for this type of construction of Legos. We did look at a DSL for assisting knitting, so I would be interested in seeing this tool. 

## Language design
If you had to capture your DSL's design in one sentence, what would it be? What constitutes a program in your language? What happens when a program runs? What kinds of input might a program take, and what kinds of output might it produce? Are there data or control structures that you know will be useful? What kinds of things might go wrong in a program in this domain (e.g., syntax errors, compile-time errors, run-time errors)? How might you design your language to prevent such errors or to clearly communicate the results of errors to the user?

### Designing and On-the-Fly Visualization of Lego models. 
A program in this langauge consists of a number of instructions. Instructions are composed of a brick or subset of instructions, along with a location in the grid where the brick should be placed. Bricks are composed of size and color. A subset of instructions can be encapsulated in a variable, which can be used in place of the subset of instructions. When a program is run, it will parse all the instructions into the IR. 

The input is simply lines of text, and the output is either a serialized 3D matrix or a set of instructions for building. If I just work with the 3D matrix, I plan to serialize input to JSON which will be passed to a program to render the matrix into layer-by-layer 2D representations.

Possible errors are using bricks, sizes, or colors that don't exist. These will be easy to catch in the syntax parsing, and I can recommend other possible suggestions. Other errors are placing bricks outside of a defined grid, so I can have some default behavior to handle these out-of-bounds errors. Using variables may create errors, but I am not sure what errors may arise from this. 

## Example computations
Describe some example computations in your DSL. These computations should describe what happens when a specific program in your DSL executes. Note that you shouldn't describe the syntax of the program. Rather you should describe some canonical examples that help someone else understand the kinds of things that your DSL will eventually be able to do.

```
Init 32x32 board
Tower:
  Place 2x2 red brick at 1,1
  Place 2x2 red brick at 1,1
  Place 2x2 red brick at 1,1
  Place 2x2 red brick at 1,1
  Place 2x2 red brick at 1,1
  Place 2x2 red brick at 1,1
  Place 1x1 red brick at 1,1
Place 2x2 red brick at 8,8
Place 4x2 blue brick at 6,5
Place Tower at 1,1
Place Tower at 30,30
```
When we place bricks on top of a spot where bricks already exist, we simply add a new layer. Therefore, we can easily visualize the layer-by-layer construction. This example also shows the use of variables.
