## Edge case

We spent most of Monday's class talking about this issue. The
conclusion we came to was that in the array that currently holds
colors, instead there should be cell objects. Each cell would have
variables for its connectivity status with respect to each of its
neighbors as well as its color. This allows the language to use
connectedness to determine when a placed variable should have some
components fall and when it should not.

## General comments

Your code looks good! I would say, though, that your interpreter
should probably be more decomposed. Right now it's a little
god-objecty, because there are several functions with very little
relation to each other. Split them out into, say, traits, and then
have the Interpreter object extend those traits.

Also, rather than nested fors, use the

```
for {
	a <- foo
	b <- bar
	c <- baz
} {
 zoom()
}
```

syntax.

Apart from that, things look good :) We covered everything major in
our discussion on Monday.
