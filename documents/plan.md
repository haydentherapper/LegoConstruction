# Project plan

## Language evaluation
How will you know that your language has accomplished its goals? What tools, techniques, or measurements will you use to evaluate your language design? What tools or practices will you use to ensure that your language implementation is of high quality?

I want my language to be used. First, I would love to give my tool to the Lego subreddit and find out if the language is usable. My main worries is verbosity: How can I minimize the amount of input needed? I want to get input from designers.

One measurement is speed of replication. What I mean by this is how fast can someone recreate one of my constructions given my layer-by-layer generations vs. either standard instructions or by simply looking at the model. If I find that designers are able to easily replicate a creation, then we know the tool has measurable value in terms of time.

## Implementation plan
How much time do you think you'll need for the various components of your language (e.g., finding a host language, implementing the semantics, the parser, tools, etc)? Provide a brief schedule (e.g., with a deliverable every week) for your project.

Finding a host language will be simple. I plan to use a language that has parsing, JSON serialization and easy GUI design. I planned to use either Scala or Java, but I will also look at python since I know there are many libraries. Based on what host langauge I choose, I will dive into the parser as I work on and flesh out the semantics, and then build the interpreter/serializer. Finally, I will need to create the visualization, which while not connected to the DSL, will possibly take the longest time. The following is my hope for the time line:
* Nov 9: Host language chosen, semantics designed, parser researched
* Nov 16: Semantics and parser work
* Nov 23: Parser and interpreter/serialization work
* Nov 30: Eating turkey and pumpkin pie, working on interpreter, beginning GUI
* Month of December: Cleaning up project, designing GUI
