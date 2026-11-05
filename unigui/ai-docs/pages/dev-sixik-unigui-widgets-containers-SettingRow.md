# `SettingRow` (extends `PanelRowWidget`)

Specialized settings row holding a label on the left and an interactive control on the right.

- **Purpose:** Streamlines creating options dialogs, settings panes, and parameter forms with automatic label focus forwarding.

### USE WHEN

- Building configuration forms with key-value or label-control rows.
- Creating preferences screens where clicking on the label should automatically focus the control widget.

### CRITICAL RULES & GOTCHAS:

- The left slot is reserved for an integrated Label, while the right slot holds the interactive control widget.
- Clicking the label automatically focuses the right-side control via label.focusTarget(control).
- For multiple widgets per side, use PanelRowWidget directly instead of SettingRow.

- **Related:** `PanelRowWidget`, `VBox`, `Box`

### COMPONENTS

- `label()` → Label — Returns the live Label instance.
- `labelText(String)` → SettingRow — Sets plain text for setting label.
- `control(Widget)` → SettingRow — Sets right-side control widget and wires focus redirection.

### SIZING

- `controlWidth(float)` → SettingRow (default: `auto`) — Sets preferred width for right control group.

### EXAMPLES

#### Setting row for slider control

```java
SettingRow row = new SettingRow("Master Volume", volumeSlider).controlWidth(120.0f);
```

