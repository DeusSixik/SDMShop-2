---
title: "SearchField"
slug: "search-field"
category: "Widgets/Interaction"
locale: "en"
order: 150
---

# SearchField

Search input box with search icon, clear button, and debounced text change query dispatching.

## Overview

`SearchField` enhances `TextInput` with an integrated leading search icon, an interactive trailing clear button, and automatic debouncing (`searchChangeDebounceSeconds`) to avoid querying on every rapid keystroke.

## Debounced Search Filter

```java
SearchField search = new SearchField()
        .placeholder("Filter items...")
        .searchChangeDebounceSeconds(0.2f);
search.onSearchChanged(e -> filterGrid(e.newQuery()));
```

## Related links

- [TextInput](<text-input.md>)

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">SearchField::</span>`clear()` {: #clear data-toc-label="clear()" }

**Clear Search Field**

Clears query text and resets search state.

Returns `SearchField`: this widget for fluent configuration.

</div>

