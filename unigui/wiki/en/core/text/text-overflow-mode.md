---
title: "TextOverflowMode"
slug: "text-overflow-mode"
category: "Core/Text"
locale: "en"
order: 20
---

# TextOverflowMode

Enumeration configuring visual clipping, truncation, shrinking, or marquee scrolling when text exceeds available bounds.

## Overview

`TextOverflowMode` governs how UI widgets handle text content that does not fit into allocated layout dimensions. It controls visual presentation without altering underlying text layout metrics or plain text strings.

## Overflow Behaviors

- `VISIBLE`: [Text](<../../widgets/display/text.md>) renders fully and may spill past widget boundaries.
- `CLIP`: Scissor clips text strictly to the widget bounds.
- `ELLIPSIS`: Truncates overflowing single-line text and appends an ellipsis (`...`), guaranteeing the result fits.
- `SHRINK_TO_FIT`: Dynamically scales font size down until the entire line fits the available width.
- `MARQUEE_ON_HOVER`: Clipped when idle; smoothly scrolls back and forth when hovered by the cursor.

## Configuring Ellipsis Truncation for Labels

```java
LabelWidget label = new LabelWidget("Very long file name or item title");
label.overflow(TextOverflowMode.ELLIPSIS);
```

## Setting Marquee on Hover for Song Titles

```java
LabelWidget trackName = new LabelWidget("Artist - Super Long Track Name (Remix)");
trackName.overflow(TextOverflowMode.MARQUEE_ON_HOVER);
```

