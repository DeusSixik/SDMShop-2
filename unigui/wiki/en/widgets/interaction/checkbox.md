---
title: "Checkbox"
slug: "checkbox"
category: "Widgets/Interaction"
locale: "en"
order: 80
---

# Checkbox

Square checkbox control supporting two-state and tri-state (indeterminate) modes.

## Overview

`Checkbox` renders a square checkmark box with animated check/indeterminate mark and companion text label. In tri-state mode, it cycles through CHECKED, UNCHECKED, and INDETERMINATE.

## Terms Agreement Checkbox

```java
Checkbox agreeBox = new Checkbox("I accept the terms and conditions")
        .checked(false);
agreeBox.onStateChanged(e -> submitBtn.enabled(e.newState() == CheckboxState.CHECKED));
```

## Related links

- [RadioButton](<radio-button.md>)
- [ToggleSwitch](<toggle-switch.md>)

