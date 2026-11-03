---
title: "Signal Binding"
slug: "widget-binding"
category: "Core/Reactive"
locale: "en"
order: 70
---

# Signal Binding

Connect widget properties to reusable typed signals with bidirectional updates.

## Binding model

Call `widget.bind(key, signal)` to connect a supported widget property to a signal. Binding is bidirectional: the signal's current value is applied immediately, later signal changes update the widget, and user edits update the signal. Built-in keys are `BindingKeys.TEXT` for `TextInput` and its subclasses, and `BindingKeys.CHECKED` for `ToggleButton` and its subclasses.

A key is matched by **object identity**, so declare a custom key once and reuse it: `static final BindingData<BooleanSignal> MY_CHECKBOX_VALUE = new BindingData<>(BooleanSignal.class);`. A custom `WidgetBase` subclass handles its keys in `createBinding(...)` and returns `null` for unknown keys. Unsupported keys are ignored; passing a source of the wrong type is an error. Rebinding the same key replaces the previous connection, and `WidgetBase.dispose()` closes active bindings.

Signal callbacks run synchronously on the thread that calls `set()`. Change UI-bound signals on the UI thread.

## Share model state between controls

```java
StringSignal name = new StringSignal("Ada");
TextField editor = new TextField();
TextField mirror = new TextField();
editor.bind(BindingKeys.TEXT, name);
mirror.bind(BindingKeys.TEXT, name);

BooleanSignal enabled = new BooleanSignal(true);
Checkbox checkbox = new Checkbox("Enabled");
ToggleButton toggle = new ToggleButton("Enabled");
checkbox.bind(BindingKeys.CHECKED, enabled);
toggle.bind(BindingKeys.CHECKED, enabled);
```

