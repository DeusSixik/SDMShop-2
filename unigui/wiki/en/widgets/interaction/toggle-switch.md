---
title: "ToggleSwitch"
slug: "toggle-switch"
category: "Widgets/Interaction"
locale: "en"
order: 70
---

# ToggleSwitch

Sliding switch control with smooth animated thumb transition and optional label.

## Overview

`ToggleSwitch` provides a modern sliding toggle switch. When clicked, the circular thumb smoothly glides across the rounded pill track.

## Settings Switch

```java
ToggleSwitch darkTheme = new ToggleSwitch("Dark Theme")
        .checked(true);
darkTheme.onCheckedChanged(e -> applyTheme(e.newValue()));
```

## Related links

- [Checkbox](<checkbox.md>)
- [ToggleButton](<toggle-button.md>)

