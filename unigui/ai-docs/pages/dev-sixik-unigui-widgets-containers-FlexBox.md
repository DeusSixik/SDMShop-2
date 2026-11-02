# `FlexBox` (extends `PanelWidget`)

Flex container configured directly via LayoutStyle, equivalent to CSS display: flex.

- **Purpose:** Provides full unconstrained flexbox layout without helper abstractions. Direction defaults to ROW.

### USE WHEN

- Require advanced flexbox properties (justifyContent, alignItems, alignContent, wrap) together.
- Build responsive adaptive layouts mimicking CSS flexbox specifications.

### CRITICAL RULES & GOTCHAS:

- Direction defaults to FlexDirection.ROW.
- Configure spacing and flow directly through layoutStyle().

- **Related:** `LinearBox`, `WrapPanel`, `LayoutStyle`

### LIFECYCLE

- `FlexBox()` — Creates an unconstrained FlexBox with ROW direction.

### EXAMPLES

#### Flex container setup

```java
FlexBox flex = new FlexBox();
flex.layout(s -> s.justifyContent(Justify.SPACE_BETWEEN));
```

