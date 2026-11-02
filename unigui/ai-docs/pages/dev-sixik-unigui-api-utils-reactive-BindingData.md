# `BindingData`

Identity key that names one widget binding and its required source type.

- **Purpose:** Lets WidgetBase subclasses add property bindings through the shared bind(key, signal) API.

### CRITICAL RULES & GOTCHAS:

- BindingData keys are matched by object identity; declare a static final key and reuse that instance.
- The key's Class must match the source type expected by the binding implementation.

- **Related:** `BindingKeys`, `SignalSubscription`, `WidgetBase`

### EXAMPLES

#### Declare a custom binding key

```java
static final BindingData<BooleanSignal> MY_CHECKBOX_VALUE =
        new BindingData<>(BooleanSignal.class);
```

