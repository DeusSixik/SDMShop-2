---
title: "TimeSpanField"
slug: "time-span-field"
category: "Widgets/Interaction"
locale: "en"
order: 160
---

# TimeSpanField

Duration input field formatting time values like 1h 30m or 45s into java.time.Duration.

## Overview

`TimeSpanField` provides convenient human-readable duration input (e.g. `2h 15m`, `30s`, `1d`), parsing and formatting directly with `java.time.Duration`.

## Cooldown Duration Input

```java
TimeSpanField cooldown = new TimeSpanField()
        .value(Duration.ofMinutes(5));
```

## Related links

- [TextField](<text-field.md>)

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TimeSpanField::</span>`value(Duration)` {: #valueduration data-toc-label="value(Duration)" }

**Set Duration Value**

Sets the duration value as java.time.Duration.

| Parameter | Type | Description |
| --- | --- | --- |
| `value` | `Duration` | duration instance |

Returns `TimeSpanField`: this widget for fluent configuration.

</div>

