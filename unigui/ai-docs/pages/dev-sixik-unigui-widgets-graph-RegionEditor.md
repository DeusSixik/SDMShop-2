# `RegionEditor` (extends `Box`, implements `OverlayHostAware`, implements `OverlayDismissable`)

Small floating text editor for a graph region's title (single line) or comment (multi-line), opened over an OverlayLayer.

- **Purpose:** Lets users rename a region and write its comment without leaving the canvas.

### USE WHEN

- NodeGraph opens it for F2 and for the region context menu; open it yourself with `NodeGraph.openRegionEditor`.

### CRITICAL RULES & GOTCHAS:

- Enter applies a title, Ctrl+Enter applies a comment; Escape cancels; a click outside applies.
- It is an overlay: it only works inside an OverlayLayer and removes itself when closed.
- The commit callback runs after the editor has closed.

- **Related:** `NodeGraph`, `NodeGraphRegion`, `OverlayLayer`

