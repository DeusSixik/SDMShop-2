# `RichText`

Immutable multi-style rich text model supporting styled runs, fonts, brushes, gradients, and inline content spans.

- **Purpose:** Provides a unified, immutable representation of text and embedded inline atoms (icons, badges, images) for UI layout, line wrapping, and rendering.

### USE WHEN

- Rendering formatted labels, buttons, tooltips, or chat logs with mixed fonts, sizes, and colors.
- Embedding icons, badges, or procedural inline graphics directly into continuous text flows.
- Applying multi-stop gradients or custom brushes across text runs.

### CRITICAL RULES & GOTCHAS:

- RichText is strictly immutable. Methods like withBrush, withTextStyle, append, and slice return new instances.
- Inline content spans participate in line wrapping and alignment but do not receive direct UI input events.
- Plain string operations can use RichText.plain(String) or RichText.resolve(String) for automatic icon marker resolution.

- **Related:** `TextRun`, `RichTextSpan`, `InlineContentSpan`, `FontFace`, `TextBrush`, `TextEngine`

### EXAMPLES

#### Building Rich Text with Text and Inline Icons

```java
RichText text = RichText.builder()
        .append("Status: ")
        .icon("icon:ok", checkTexture, 12.0f)
        .color(ColorView.rgb(80, 220, 100))
        .append(" All systems online")
        .build();
```

#### Applying Linear Gradient Across Text

```java
RichText gradientTitle = RichText.gradient(
        "UniGUI Engine",
        Fonts.TITLE,
        18.0f,
        ColorView.rgb(255, 100, 100),
        ColorView.rgb(100, 150, 255),
        45.0f
);
```

