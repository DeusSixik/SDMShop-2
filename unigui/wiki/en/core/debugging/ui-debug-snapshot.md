---
title: "UiDebugSnapshot"
slug: "ui-debug-snapshot"
category: "Core/Debugging"
locale: "en"
order: 20
---

# UiDebugSnapshot

Read-only per-frame timings, render counts, and texture-cache metrics.

## Overview

Obtain `UiDebugSnapshot` from `UIContext#debugCounters().snapshot()`. The snapshot belongs to one captured frame and contains sanitized frame timing, command and batch counts, plus texture-cache activity. GPU timing can be `-1` when the active backend cannot measure it; other counters and timings are non-negative.

## Read frame metrics

```java
UiDebugSnapshot snapshot = context.debugCounters().snapshot();
if (snapshot.frameGpuMillis() >= 0.0f) {
    logGpuTime(snapshot.frameGpuMillis());
}
logRenderLoad(snapshot.drawCommandCount(), snapshot.batchCount());
```

## API

### Frame

| Method | Description |
| --- | --- |
| [`frameIndex()`](#frameindex) | Returns the frame index represented by this snapshot. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">UiDebugSnapshot::</span>`frameIndex()` {: #frameindex data-toc-label="frameIndex()" }

**frameIndex()**

Returns the frame index represented by this snapshot.

Returns `long`: Captured frame index.

</div>

### Frame Timing

| Method | Description |
| --- | --- |
| [`framesPerSecond()`](#framespersecond) | Returns measured frames per second, or zero when unavailable. |
| [`frameTotalMillis()`](#frametotalmillis) | Returns total wall-clock time for the frame. |
| [`frameGpuMillis()`](#framegpumillis) | Returns GPU frame time when the backend exposes it. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">UiDebugSnapshot::</span>`framesPerSecond()` {: #framespersecond data-toc-label="framesPerSecond()" }

**framesPerSecond()**

Returns measured frames per second, or zero when unavailable.

Returns `float`: Non-negative frames-per-second value.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">UiDebugSnapshot::</span>`frameTotalMillis()` {: #frametotalmillis data-toc-label="frameTotalMillis()" }

**frameTotalMillis()**

Returns total wall-clock time for the frame.

Returns `float`: Non-negative duration in milliseconds.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">UiDebugSnapshot::</span>`frameGpuMillis()` {: #framegpumillis data-toc-label="frameGpuMillis()" }

**frameGpuMillis()**

Returns GPU frame time when the backend exposes it.

Returns `float`: GPU duration in milliseconds, or -1 when unavailable.

</div>

### Render Work

| Method | Description |
| --- | --- |
| [`drawCommandCount()`](#drawcommandcount) | Returns the number of draw-list commands recorded for the frame. |
| [`batchCount()`](#batchcount) | Returns the number of backend batches recorded for the frame. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">UiDebugSnapshot::</span>`drawCommandCount()` {: #drawcommandcount data-toc-label="drawCommandCount()" }

**drawCommandCount()**

Returns the number of draw-list commands recorded for the frame.

Returns `int`: Non-negative draw-command count.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">UiDebugSnapshot::</span>`batchCount()` {: #batchcount data-toc-label="batchCount()" }

**batchCount()**

Returns the number of backend batches recorded for the frame.

Returns `int`: Non-negative batch count.

</div>

### Texture Cache

| Method | Description |
| --- | --- |
| [`lastTextureCacheMissReason()`](#lasttexturecachemissreason) | Returns the latest normalized texture-cache miss reason. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">UiDebugSnapshot::</span>`lastTextureCacheMissReason()` {: #lasttexturecachemissreason data-toc-label="lastTextureCacheMissReason()" }

**lastTextureCacheMissReason()**

Returns the latest normalized texture-cache miss reason.

Returns `String`: Miss reason, or NONE when no reason is available.

</div>

