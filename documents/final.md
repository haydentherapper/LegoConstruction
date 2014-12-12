# Progettare: Designing and Visualizing Block Constructions

## Introduction

### Motivation

I used to use a wonderful program called [Lego Digital Designer](http://ldd.lego.com/en-us/) to assist in designing and creating Legos. Input was all through a GUI, and one could purchase the construction or download a set of instructions. However, Lego no longer supports this program, so I would like to design a language which will assist Lego designers in constructing models. There currently exist no other tools to design models via text, so I hope to design a language that is easy to use and informative in its output.

The endgoal is having a simple language containing instructions that will generate a visual model. An external DSL is an appropriate solution since such a language should be easily parsable and interpretable, especially if we restrict the total set of bricks and possible placements of bricks. Bricks could easily be modeled in an IR, and instructions could easily be modeled using these bricks.

### Language domain

The domain of the language is model construction, specifically using Lego bricks. One issue with any type of construction, whether it be car models, knitting, or Legos, is the visualization during construction without having the model. If I can design a tool that will help with on-the-fly visualization, I feel this could benefit any type of constructor, not just Lego creators. Legos, however, are very geometric and simplify the construction process. Additionally, I love Legos, so I want to focus on Lego constructions with this DSL.

### Essence

This tool should be easily usable by any expert of building block construction. Any user, whether they are a programmer or not, should hopefully be able to learn how to design construction with text. Likewise, the output must be extremely useful. A user should be able to render their creations and view them on-the-fly. A user should be able to tweak one small instructions and instantaneously see the difference. This is what sets this tool apart from any other construction tools: Quick and responsive to changes in the model, with an intuitive text-based input.

