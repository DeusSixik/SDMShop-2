# `StyleAnimationDefinition` (extends `Record`)

Immutable named animation preset made of declarative property tweens.

- **Purpose:** Stores reusable animation data in a `StylePack` so style events can reference it by stable id.

### USE WHEN

- Create an animation preset for `StylePack.putAnimation` and bind it through `StyleDefinition.eventAnimation`.

### CRITICAL RULES & GOTCHAS:

- The definition only describes animation data; it does not start an animation by itself.
- The id must be non-blank and unique within its style pack.
- Null tweens are ignored and the stored tween list is immutable.
- Property and event ids should come from `StyleAnimationIds` or the target widget's animation constants.

- **Related:** `StylePack`, `StyleDefinition`, `StylePropertyTween`, `StyleAnimationIds`

### CREATION

- `of(String, StylePropertyTween[])` → StyleAnimationDefinition — Creates an immutable named preset from zero or more non-null property tweens.

### COMPOSITION

- `withTween(StylePropertyTween)` → StyleAnimationDefinition — Returns a new preset with one additional tween; null leaves the preset unchanged.

### EXAMPLES

#### Create and bind a press preset

```java
StyleAnimationDefinition press = StyleAnimationDefinition.of(
        "button.press",
        StylePropertyTween.currentTo(
                Button.AnimationProperties.SCALE,
                "0.96",
                TransitionSpec.of(0.12f, AnimationEasing.EASE_OUT).yoyo()));

pack.putAnimation(press)
        .put(buttonDefinition.eventAnimation(
                Button.AnimationEvents.ON_CLICK,
                press.id()));
```

