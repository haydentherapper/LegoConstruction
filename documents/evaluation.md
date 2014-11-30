# Preliminary evaluation

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
