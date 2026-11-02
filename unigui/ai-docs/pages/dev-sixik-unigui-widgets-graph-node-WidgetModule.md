# `WidgetModule` (implements `NodeModule`)

Node strip hosting one arbitrary widget stretched to the node width.

- **Purpose:** Embeds previews, sliders, editors or any widget as a node row without writing a custom module.

### USE WHEN

- Show a Shader Graph preview tile inside a node.
- Embed a multiline text field or curve editor in a node body.

### CRITICAL RULES & GOTCHAS:

- Height comes from the widget's preferred height or `minHeight`, whichever is larger.
- Text editors never size the node from their content: typing must not resize the node. Use `size(w, h)` to override.
- The widget is zoomed together with the node.

- **Related:** `NodeModule`, `NodeBuilder`

