---
title: "TreeListPicker"
slug: "tree-list-picker"
category: "Widgets/Interaction"
locale: "en"
order: 240
---

# TreeListPicker

Generic dropdown picker binding domain objects to displayed text options.

## Overview

`TreeListPicker<T>` extends `ComboBox` to associate strongly typed items `List<T>` with a custom string formatting function `labelProvider`.

## Typed Object Picker

```java
TreeListPicker<PlayerProfile> picker = new TreeListPicker<PlayerProfile>()
        .values(players)
        .labelProvider(PlayerProfile::displayName);
```

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeListPicker::</span>`labelProvider(Function)` {: #labelproviderfunction data-toc-label="labelProvider(Function)" }

**Set [Label](<../display/label.md>) Provider**

Sets function converting domain objects to displayed string labels.

| Parameter | Type | Description |
| --- | --- | --- |
| `labelProvider` | `Function` | — |

Returns `TreeListPicker`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeListPicker::</span>`selectedValue()` {: #selectedvalue data-toc-label="selectedValue()" }

**Get Selected Value**

Returns currently selected domain object instance or null.

Returns `Object`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeListPicker::</span>`values(List)` {: #valueslist data-toc-label="values(List)" }

**Set Values**

Sets list of domain objects available for selection.

| Parameter | Type | Description |
| --- | --- | --- |
| `values` | `List` | — |

Returns `TreeListPicker`.

</div>

