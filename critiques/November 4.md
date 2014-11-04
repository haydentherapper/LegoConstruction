I really like this project!  I've heard of people using Lego Digital Designer, 
and didn't realize that it's no longer being supported -- that's quite a shame.
If this DSL can fill that niche, I think plenty of people would be happy to use
it.

I think your main design decisions are going to be in making the IR.  It seems
that you're planning to use blocks as the base unit, which makes sense.
However, I wonder what kind of data structure you plan to put the blocks in.  A
graph of some sort might work, or perhaps just a giant 3D primitive array where
each 3-tuple index refers to a cube in space.

I also see that your example only has blocks that differ in 2D sizes; you might
have to start thinking about variable height blocks, or blocks with non-integer
dimensions.  Even if you start with only rectangular blocks with fixed height
(which I think is a good idea), you'll probably want to design your language
such that further functionality  in terms of block shape and size are easy to
add later.

I think you should probably focus very little on the GUI, at least for this
class.  For stretch goals, it seems like extending modeling functionality would
be more interesting from a design perspective, so you might want to keep doing
that instead of working on the GUI.
