# `Slider` (extends `Box`)

Range slider control allowing continuous or stepped value adjustment with draggable thumb.

- **Purpose:** Provides an interactive slider input for numerical values within min and max bounds.

### USE WHEN

- Adjust continuous float or stepped numerical parameters.
- Settings screens for audio volume, field of view, or brightness.

### CRITICAL RULES & GOTCHAS:

- Value is automatically clamped between min and max.
- When step > 0, values are quantized to multiples of step.

- **Related:** `ScrollBar`, `NumberField`, `SliderValueChangedEvent`

