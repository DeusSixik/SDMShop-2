---
title: "DrawList"
slug: "draw-list"
category: "Core/Render"
locale: "en"
order: 20
---

# DrawList

Retained frame command buffer recording backend-neutral draw primitives, managing command recycling pools, and providing multi-channel layering.

## Overview

Widgets do not draw directly to the GPU. Instead, drawing operations emit `DrawCommand` instances into a `DrawList`. At the end of the frame, the active `RenderBackend` iterates over the list to perform state sorting, geometry batching, texture binding, and GPU draws.

## Command Lifecycle & Pooling

To prevent garbage collection overhead during high-frequency UI rendering, `DrawList` maintains a pool of recycled `DrawCommand` objects (up to 8,192 instances). Internal helpers obtain pooled commands via `obtain(DrawCommandType)` and return them during `clear()`.

## Multi-Channel Layering

Channels allow widgets to emit draw commands into separate virtual layers (e.g. background, content, overlay, tooltip) during a single traversal without reordering code. Calling `channelsSplit(n)` divides recording into channels, `channelsSetCurrent(i)` switches the active target, and `channelsMerge()` flattens all layers into consecutive commands.

## Vector Paths

`DrawList` owns a dedicated `VectorPath` builder accessible via `path()`. Procedural shapes, curves, and contours can be constructed incrementally into this shared path without allocating temporary geometry objects.

## Backend Consumption

Backends can access commands either through an unmodifiable `List<DrawCommand>` via `commands()` or via direct array access through `commandElements()` and `size()` for maximum iteration performance.

## Layering Background and Foreground with Channels

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

## Iterating Recorded Commands in a Backend Loop

```java
Object[] elements = drawList.commandElements();
int size = drawList.size();
for (int i = 0; i < size; i++) {
    DrawCommand cmd = (DrawCommand) elements[i];
    dispatchCommand(cmd);
}
```

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DrawList::</span>`add(DrawCommand)` {: #adddrawcommand data-toc-label="add(DrawCommand)" }

**add(DrawCommand)**

Appends a copy of the specified draw command to the current list or active channel.

| Parameter | Type | Description |
| --- | --- | --- |
| `command` | `DrawCommand` | Command to record; null is ignored. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DrawList::</span>`obtain(DrawCommandType)` {: #obtaindrawcommandtype data-toc-label="obtain(DrawCommandType)" }

**obtain(DrawCommandType)**

Obtains a recycled DrawCommand instance from the internal pool or instantiates a new one if empty.

| Parameter | Type | Description |
| --- | --- | --- |
| `type` | `DrawCommandType` | Type of command to configure. |

Returns `DrawCommand`: Reset DrawCommand instance ready for population..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DrawList::</span>`clear()` {: #clear data-toc-label="clear()" }

**clear()**

Clears all recorded commands, returns them to the recycling pool, resets channels, and clears the shared vector path.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DrawList::</span>`commands()` {: #commands data-toc-label="commands()" }

**commands()**

Returns an unmodifiable view of all recorded commands in the current draw list.

Returns `List`: Read-only view of recorded commands..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DrawList::</span>`commandElements()` {: #commandelements data-toc-label="commandElements()" }

**commandElements()**

Returns the raw fastutil backing array of commands for high-performance backend iteration loops.

Returns `Object[]`: Backing object array; read up to size() elements..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DrawList::</span>`size()` {: #size data-toc-label="size()" }

**size()**

Returns the total number of recorded commands.

Returns `int`: Command count..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DrawList::</span>`path()` {: #path data-toc-label="path()" }

**path()**

Returns the shared VectorPath builder owned by this draw list.

Returns `VectorPath`: Shared VectorPath instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DrawList::</span>`pathClear()` {: #pathclear data-toc-label="pathClear()" }

**pathClear()**

Resets the shared VectorPath builder, clearing all subpaths and vertices.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DrawList::</span>`channelsActive()` {: #channelsactive data-toc-label="channelsActive()" }

**channelsActive()**

Checks whether multi-channel recording is currently active.

Returns `boolean`: true if recording into separate channels..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DrawList::</span>`currentChannel()` {: #currentchannel data-toc-label="currentChannel()" }

**currentChannel()**

Returns the index of the currently active recording channel.

Returns `int`: Zero-based channel index..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DrawList::</span>`channelsSplit(int)` {: #channelssplitint data-toc-label="channelsSplit(int)" }

**channelsSplit(int)**

Splits command recording into the specified number of layered channels.

| Parameter | Type | Description |
| --- | --- | --- |
| `count` | `int` | Number of channels; values less than 1 are clamped to 1. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DrawList::</span>`channelsSetCurrent(int)` {: #channelssetcurrentint data-toc-label="channelsSetCurrent(int)" }

**channelsSetCurrent(int)**

Selects the active channel for subsequent recorded commands.

| Parameter | Type | Description |
| --- | --- | --- |
| `channel` | `int` | Target channel index, clamped to available range. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DrawList::</span>`channelsMerge()` {: #channelsmerge data-toc-label="channelsMerge()" }

**channelsMerge()**

Merges all channel commands back into the primary draw list in sequential channel order.

</div>

