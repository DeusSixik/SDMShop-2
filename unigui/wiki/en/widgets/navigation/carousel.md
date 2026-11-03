---
title: "Carousel"
slug: "carousel"
category: "Widgets/Navigation"
locale: "en"
order: 0
---

# Carousel

Paged carousel container supporting horizontal or vertical card paging, animated slide transitions, navigation buttons, and indicator dots.

## Overview

Paged carousel container supporting horizontal or vertical card paging, animated slide transitions, navigation buttons, and indicator dots. Provides extensive layout configuration and interaction capabilities.

## Basic Usage

```java
Carousel carousel = new Carousel();
carousel.addPage(slide1).addPage(slide2);
```

## Related links

- [PageView](<page-view.md>): Retained page container that keeps exactly one page visible at a time.

## API

<div class="api-member-card" markdown="1">

### `Carousel()` {: #carousel data-toc-label="Carousel()" }

Constructs an empty Carousel with default header controls and dot indicators.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Carousel::</span>`addPage(Widget)` {: #addpagewidget data-toc-label="addPage(Widget)" }

Appends a slide page to the carousel.

| Parameter | Type | Description |
| --- | --- | --- |
| `page` | `Widget` | The page widget. |

Returns `Carousel`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Carousel::</span>`dots()` {: #dots data-toc-label="dots()" }

Returns the DotIndicator widget displaying carousel page dots.

Returns `DotIndicator`: The indicator dots container..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Carousel::</span>`header()` {: #header data-toc-label="header()" }

Returns the top header container hosting navigation buttons and indicators.

Returns `HBox`: The carousel header bar..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Carousel::</span>`indicator()` {: #indicator data-toc-label="indicator()" }

Returns the numeric page indicator label.

Returns `Label`: The indicator label widget..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Carousel::</span>`nextButton()` {: #nextbutton data-toc-label="nextButton()" }

Returns the navigation button stepping to the next slide.

Returns `Button`: The associated button component..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Carousel::</span>`onSelectionChanged(EventListener)` {: #onselectionchangedeventlistener data-toc-label="onSelectionChanged(EventListener)" }

Registers a listener invoked when the active carousel page changes.

| Parameter | Type | Description |
| --- | --- | --- |
| `listener` | `EventListener` | The event listener callback. |

Returns `EventSubscription`: The event subscription handle..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Carousel::</span>`pageView()` {: #pageview data-toc-label="pageView()" }

Returns the internal [PageView](<page-view.md>) managing slide presentation.

Returns `PageView`: The underlying [PageView](<page-view.md>) container..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Carousel::</span>`pages()` {: #pages data-toc-label="pages()" }

Returns an unmodifiable list of all slide pages in the carousel.

Returns `List`: An unmodifiable list of page widgets..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Carousel::</span>`previousButton()` {: #previousbutton data-toc-label="previousButton()" }

Returns the navigation button stepping to the previous slide.

Returns `Button`: The associated button component..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Carousel::</span>`selectRelative(int)` {: #selectrelativeint data-toc-label="selectRelative(int)" }

Steps the slide index by the specified relative delta.

| Parameter | Type | Description |
| --- | --- | --- |
| `delta` | `int` | The relative offset delta. |

Returns `Carousel`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Carousel::</span>`selectedIndex()` {: #selectedindex data-toc-label="selectedIndex()" }

Returns the index of the currently active slide.

Returns `int`: The result value..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Carousel::</span>`selectedIndex(int)` {: #selectedindexint data-toc-label="selectedIndex(int)" }

Switches to the specified slide index and dispatches a selection event.

| Parameter | Type | Description |
| --- | --- | --- |
| `selectedIndex` | `int` | The index position. |

Returns `Carousel`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Carousel::</span>`silentSelectedIndex(int)` {: #silentselectedindexint data-toc-label="silentSelectedIndex(int)" }

Switches to the specified slide index without triggering event callbacks.

| Parameter | Type | Description |
| --- | --- | --- |
| `selectedIndex` | `int` | The index position. |

Returns `Carousel`: This instance for chaining..

</div>

