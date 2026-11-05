# `DrawList`

Retained frame command buffer recording backend-neutral draw primitives, managing command recycling pools, and providing multi-channel layering.

- **Purpose:** Decouples widget rendering logic from the physical rendering backend by recording immutable draw commands into an optimized list with pooling and channel layers.

### USE WHEN

- Widgets or renderers need to record low-level DrawCommands directly.
- Draw operations require multi-layer ordering (e.g. background behind text rendered earlier in code) via channels.
- RenderBackends iterate over recorded frame commands to batch and submit GPU draws.

### CRITICAL RULES & GOTCHAS:

- Commands obtained via obtain(DrawCommandType) belong to the pool and must never be held past the next clear() call.
- When using channels, always call channelsMerge() before the frame ends to flatten channel commands into the primary list.
- commandElements() returns the raw array of the fastutil list: only elements from index 0 to size() - 1 are valid.

- **Related:** `DrawCommand`, `DrawScope`, `RenderBackend`, `VectorPath`

### EXAMPLES

#### Layering Background and Foreground with Channels

```java
drawList.channelsSplit(2);
drawList.channelsSetCurrent(1);
// Draw foreground content first in code
drawList.add(foregroundCommand);
drawList.channelsSetCurrent(0);
// Draw background behind content
drawList.add(backgroundCommand);
// Merge layers in 0..N order
drawList.channelsMerge();
```

#### Iterating Recorded Commands in a Backend Loop

```java
Object[] elements = drawList.commandElements();
int size = drawList.size();
for (int i = 0; i < size; i++) {
    DrawCommand cmd = (DrawCommand) elements[i];
    dispatchCommand(cmd);
}
```

