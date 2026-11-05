# `View` (extends `Box`)

Card-like visual container embedding a header Label and a vertical VBox content area.

- **Purpose:** Provides a ready-to-use titled card or panel with default background, border, padding, and vertical layout.

### USE WHEN

- Building inspector panels, debug tool windows, or self-contained card UI components.
- Need a quick card panel with a title header and vertical content stack without manual Box/VBox wiring.

### CRITICAL RULES & GOTCHAS:

- Widgets added via addContent(Widget) or content().addChild(Widget) are placed in the vertical VBox below the title.
- Inherits visual styling from Box, including background, border, corner radius, and shadow.
- Uses WidgetStyles.View.DEFAULT for default surface styling.

- **Related:** `Box`, `VBox`, `PanelWidget`

### CONTENT

- `title(String)` → View — Sets text of header label.
- `content()` → VBox — Returns internal VBox container hosting content widgets.
- `addContent(Widget)` → View — Appends widget to internal VBox below title.

### EXAMPLES

#### Card View with content

```java
View card = new View("Properties");
card.addContent(new Label("Status: Active"));
card.addContent(new Button("Save"));
```

