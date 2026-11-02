# `TextureClock`

Global tick source driving AnimatedTexture frame selection; defaults to wall-clock time at 20 ticks per second.

- **Purpose:** Lets render backends substitute the real game tick counter while keeping core backend-neutral.

### USE WHEN

- Bind animated textures to the game tick counter (pauses with the game) from a backend.
- Make animation frame selection deterministic in tests by installing a fixed tick source.

### CRITICAL RULES & GOTCHAS:

- The source must be cheap and monotonic: it is read once per animated texture draw command.
- `source(null)` restores the wall-clock default.

- **Related:** `AnimatedTexture`, `TextureHandle`

