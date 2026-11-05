---
title: "View"
slug: "view"
category: "Widgets/Containers"
locale: "en"
order: 160
---

# View

Card-like visual container embedding a header [Label](<../display/label.md>) and a vertical [VBox](<vbox.md>) content area.

## Overview

View extends `Box` to provide a ready-to-use card or panel container. It automatically initializes an internal `VBox` containing a header `Label` and a flexible content area. It is widely used in debug screens, demo panels, and HUD cards.

## Styling & Structure

Because View extends `Box`, it inherits full visual background rendering (solid color, gradient, textures), borders, corner radiuses, and shadows. Default styling is loaded from `WidgetStyles.View.DEFAULT`.

## Creating an Inspector Card

```java
View card = new View("Properties");
card.addContent(new Label("Status: Active"));
card.addContent(new Button("Save"));
```

## Related links

- [Box](<box.md>)
- [PanelWidget](<panel-widget.md>)
- [VBox](<vbox.md>)

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">View::</span>`title(String)` {: #titlestring data-toc-label="title(String)" }

**Set Title**

Sets the text of the header label.

| Parameter | Type | Description |
| --- | --- | --- |
| `title` | `String` | title text string |

Returns `View`: this widget for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">View::</span>`content()` {: #content data-toc-label="content()" }

**Get Content Container**

Returns the internal [VBox](<vbox.md>) container hosting the content widgets.

Returns `VBox`: internal [VBox](<vbox.md>) container instance.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">View::</span>`addContent(Widget)` {: #addcontentwidget data-toc-label="addContent(Widget)" }

**Add Content [Widget](<../widget.md>)**

Appends a widget to the internal vertical content container below the title.

| Parameter | Type | Description |
| --- | --- | --- |
| `widget` | `Widget` | widget to append to view content |

Returns `View`: this widget for fluent configuration.

</div>

