# `BindingKeys`

Standard keys for binding common widget properties to signals.

- **Purpose:** Provides reusable identity keys accepted by built-in widget bind implementations.

### CRITICAL RULES & GOTCHAS:

- Binding is bidirectional: signal updates change the widget, and widget changes update the signal.
- Binding immediately applies the signal's current value to the widget.
- Signal callbacks run synchronously on the thread that calls set(); update UI signals on the UI thread.

- **Related:** `BindingData`, `StringSignal`, `BooleanSignal`, `WidgetBase`

### EXAMPLES

#### Bind text and checked state

```java
StringSignal name = new StringSignal("Ada");
TextField field = new TextField();
field.bind(BindingKeys.TEXT, name);

BooleanSignal enabled = new BooleanSignal(true);
Checkbox checkbox = new Checkbox("Enabled");
checkbox.bind(BindingKeys.CHECKED, enabled);
```

