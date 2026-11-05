---
title: "DropDownBox"
slug: "drop-down-box"
category: "Widgets/Interaction"
locale: "en"
order: 200
---

# DropDownBox

Collapsible container widget with an expandable header button and popup or inline content host.

## Overview

`DropDownBox` manages an expandable content area triggered by a toggleable header button. Supports both inline layout expansion and overlay popup layers, with automatic arrow indicators and scroll containment.

## Basic DropDownBox

```java
DropDownBox dropdown = new DropDownBox()
        .headerText("Advanced Settings")
        .content(new VBox().children(setting1, setting2))
        .useOverlay();
```

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DropDownBox::</span>`close()` {: #close data-toc-label="close()" }

**Close Dropdown**

Collapses dropdown content.

Returns `DropDownBox`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DropDownBox::</span>`content()` {: #content data-toc-label="content()" }

**Get Content [Widget](<../widget.md>)**

Returns currently hosted content widget.

Returns `Widget`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DropDownBox::</span>`content(Widget)` {: #contentwidget data-toc-label="content(Widget)" }

**Set Content [Widget](<../widget.md>)**

Sets child content widget hosted in dropdown.

| Parameter | Type | Description |
| --- | --- | --- |
| `content` | `Widget` | — |

Returns `DropDownBox`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DropDownBox::</span>`contentHost()` {: #contenthost data-toc-label="contentHost()" }

**Content Host**

Returns container hosting dropdown content widget.

Returns `Box`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DropDownBox::</span>`contentScroll()` {: #contentscroll data-toc-label="contentScroll()" }

**Content [ScrollView](<../containers/scroll-view.md>)**

Returns [ScrollView](<../containers/scroll-view.md>) wrapping dropdown content.

Returns `ScrollView`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DropDownBox::</span>`dropDownWidth()` {: #dropdownwidth data-toc-label="dropDownWidth()" }

**Get DropDown Width**

Returns explicit dropdown popup width or 0 for auto.

Returns `float`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DropDownBox::</span>`dropDownWidth(float)` {: #dropdownwidthfloat data-toc-label="dropDownWidth(float)" }

**Set DropDown Width**

Sets explicit dropdown popup width.

| Parameter | Type | Description |
| --- | --- | --- |
| `width` | `float` | — |

Returns `DropDownBox`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DropDownBox::</span>`headerButton()` {: #headerbutton data-toc-label="headerButton()" }

**Header [Button](<button.md>)**

Returns internal header [Button](<button.md>) widget.

Returns `Button`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DropDownBox::</span>`headerText()` {: #headertext data-toc-label="headerText()" }

**Get Header [Text](<../display/text.md>)**

Returns header button text.

Returns `String`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DropDownBox::</span>`headerText(String)` {: #headertextstring data-toc-label="headerText(String)" }

**Set Header [Text](<../display/text.md>)**

Sets header button plain text label.

| Parameter | Type | Description |
| --- | --- | --- |
| `headerText` | `String` | — |

Returns `DropDownBox`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DropDownBox::</span>`maxContentHeight()` {: #maxcontentheight data-toc-label="maxContentHeight()" }

**Max Content Height**

Returns maximum dropdown scroll height.

Returns `float`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DropDownBox::</span>`maxContentHeight(float)` {: #maxcontentheightfloat data-toc-label="maxContentHeight(float)" }

**Set Max Content Height**

Sets maximum dropdown scroll height before scrolling is enabled.

| Parameter | Type | Description |
| --- | --- | --- |
| `height` | `float` | — |

Returns `DropDownBox`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DropDownBox::</span>`open()` {: #open data-toc-label="open()" }

**Open Dropdown**

Expands dropdown content.

Returns `DropDownBox`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DropDownBox::</span>`opened()` {: #opened data-toc-label="opened()" }

**Is Opened**

Returns whether dropdown content is currently expanded.

Returns `boolean`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DropDownBox::</span>`richHeaderText()` {: #richheadertext data-toc-label="richHeaderText()" }

**Get Rich Header [Text](<../display/text.md>)**

Returns formatted header button rich text.

Returns `RichText`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DropDownBox::</span>`richHeaderText(RichText)` {: #richheadertextrichtext data-toc-label="richHeaderText(RichText)" }

**Set Rich Header [Text](<../display/text.md>)**

Sets formatted header button rich text.

| Parameter | Type | Description |
| --- | --- | --- |
| `headerText` | `RichText` | — |

Returns `DropDownBox`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DropDownBox::</span>`toggle()` {: #toggle data-toc-label="toggle()" }

**Toggle Dropdown**

Toggles dropdown expanded state.

Returns `DropDownBox`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DropDownBox::</span>`useInline()` {: #useinline data-toc-label="useInline()" }

**Use Inline Mode**

Switches dropdown to expand inline within parent layout.

Returns `DropDownBox`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DropDownBox::</span>`useOverlay()` {: #useoverlay data-toc-label="useOverlay()" }

**Use Overlay Mode**

Switches dropdown to popup overlay layer above other widgets.

Returns `DropDownBox`.

</div>

