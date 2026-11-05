# `Carousel` (extends `LinearBox`)

Rotating carousel container displaying slides with previous/next navigation buttons and dot page indicators.

### API

- `Carousel()` — Constructs an empty Carousel with default header controls and dot indicators.
- `addPage(Widget)` → Carousel — Appends a slide page to the carousel.
- `dots()` → DotIndicator — Returns the DotIndicator widget displaying carousel page dots.
- `header()` → HBox — Returns the top header container hosting navigation buttons and indicators.
- `indicator()` → Label — Returns the numeric page indicator label.
- `nextButton()` → Button — Returns the navigation button stepping to the next slide.
- `onSelectionChanged(EventListener<? super SelectionChangedEvent>)` → EventSubscription — Registers a listener invoked when the active carousel page changes.
- `pageView()` → PageView — Returns the internal PageView managing slide presentation.
- `pages()` → List<Widget> — Returns an unmodifiable list of all slide pages in the carousel.
- `previousButton()` → Button — Returns the navigation button stepping to the previous slide.
- `selectRelative(int)` → Carousel — Steps the slide index by the specified relative delta.
- `selectedIndex()` → int — Returns the index of the currently active slide.
- `selectedIndex(int)` → Carousel — Switches to the specified slide index and dispatches a selection event.
- `silentSelectedIndex(int)` → Carousel (default: `0`) — Switches to the specified slide index without triggering event callbacks.

