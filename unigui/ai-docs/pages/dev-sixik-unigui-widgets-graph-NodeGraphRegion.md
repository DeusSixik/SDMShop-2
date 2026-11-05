# `NodeGraphRegion`

Comment region on the graph canvas: colored frame with a title bar and optional comment text (shown on hover) that carries the nodes and regions inside it.

- **Purpose:** Groups and annotates parts of a node graph, like Blueprint comment boxes or Shader Graph groups.

### USE WHEN

- Wrap a set of nodes with a labelled colored area via `NodeGraph.addRegion`.
- Annotate a graph section with a longer comment text.

### CRITICAL RULES & GOTCHAS:

- Geometry is in world coordinates and follows pan and zoom.
- Only the title bar and the bottom-right corner are interactive; the body lets pointer events through.
- A node or region moves with the region only when its whole rectangle lies inside the region at drag start.
- The comment text is not drawn in the body: a speech-bubble icon marks the title bar and the text appears while the pointer is over it.

- **Related:** `NodeGraph`, `NodeGraphItem`

