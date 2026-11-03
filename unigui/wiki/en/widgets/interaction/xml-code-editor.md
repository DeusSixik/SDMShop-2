---
title: "XmlCodeEditor"
slug: "xml-code-editor"
category: "Widgets/Interaction"
locale: "en"
order: 270
---

# XmlCodeEditor

Specialized XML and UI layout code editor with schema validation, formatting, and tag autocompletion.

## Overview

`XmlCodeEditor` extends `CodeEditor` configured specifically for UniGUI XML layout files. Includes built-in XML parsing, hierarchy formatting, widget registry autocompletion, and XML syntax error squiggles.

## XML Layout Editor

```java
XmlCodeEditor xmlEditor = new XmlCodeEditor()
        .registry(widgetRegistry)
        .formatXml();
```

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">XmlCodeEditor::</span>`formatXml()` {: #formatxml data-toc-label="formatXml()" }

**Format XML**

Formats XML source with standard indentation and attribute line wrapping.

Returns `boolean`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">XmlCodeEditor::</span>`registry(XmlWidgetRegistry)` {: #registryxmlwidgetregistry data-toc-label="registry(XmlWidgetRegistry)" }

**Set [Widget](<../widget.md>) Registry**

Sets widget registry used for tag autocompletion and attribute schema validation.

| Parameter | Type | Description |
| --- | --- | --- |
| `registry` | `XmlWidgetRegistry` | — |

Returns `XmlCodeEditor`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">XmlCodeEditor::</span>`validateXml()` {: #validatexml data-toc-label="validateXml()" }

**Validate XML**

Executes synchronous XML schema and syntax validation.

Returns `boolean`.

</div>

