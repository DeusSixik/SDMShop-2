# `Label` (extends `TextWidget`)

Text caption widget associated with an interactive focus target control or form input.

- **Purpose:** Displays accessible text captions that automatically delegate clicks to focus the target control.

### USE WHEN

- Form field captions (e.g. labeling TextField, Checkbox, Slider).
- Accessible UI layouts where clicking the label should activate the control.

### CRITICAL RULES & GOTCHAS:

- focusTarget determines which widget receives focus when clicked.
- Supports text wrapping and alignment within available bounds.

- **Related:** `TextWidget`, `TextField`, `Checkbox`, `Slider`

