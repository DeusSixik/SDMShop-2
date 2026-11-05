# UniGUI Wiki

## Core / Reactive

- [Signal Binding](<core/reactive/widget-binding.md>): Connect widget properties to reusable typed signals with bidirectional updates.
## Widgets / Containers

- [Border](<widgets/containers/border.md>): Render-only decorative outline widget without child widgets.
- [Box](<widgets/containers/box.md>): Standard styled container combining background color, texture, shader, border, radius, and child layout.
## Core / Widgets / Containers

- [DockSide](<core/widgets/containers/dock-side.md>): Specifies which outer boundary edge (LEFT, RIGHT, TOP, BOTTOM) a child widget docks against in a DockPanel.
## Widgets / Data

- [VirtualTableView](<widgets/data/virtual-table-view.md>): High-performance virtualized grid/table view widget supporting column resizing, reordering, sorting, and cell editing.
## Core / Widgets / Data

- [VirtualTableColumn](<core/widgets/data/virtual-table-column.md>): Column configuration for VirtualTableView defining header text, width, alignments, and overflow mode.
## Widgets / Display

- [TextWidget](<widgets/display/text-widget.md>): Abstract foundation widget for text rendering components supporting plain strings, RichText, fonts, colors, and gradients.
- [Label](<widgets/display/label.md>): Text caption widget associated with an interactive focus target control or form input.
- [Text](<widgets/display/text.md>): Lightweight text display widget for static or dynamic strings and RichText.
- [TextBlock](<widgets/display/text-block.md>): Multiline paragraph text display widget supporting line wrapping, line height, and text alignments.
- [RichTextView](<widgets/display/rich-text-view.md>): Formatted rich text widget rendering styled runs, inline icons, color spans, and links.
- [Separator](<widgets/display/separator.md>): Horizontal or vertical divider line widget for visually grouping interface sections.
- [Shape](<widgets/display/shape.md>): Vector geometric shape widget supporting rectangles, rounded rectangles, circles, ovals, and strokes.
- [Path](<widgets/display/path.md>): Vector path widget drawing complex custom 2D shapes, bezier curves, and polylines.
- [TextureWidget](<widgets/display/texture-widget.md>): Base widget for rendering textures, UV regions, nine-slice borders, and image handles.
- [ImageView](<widgets/display/image-view.md>): Texture display widget presenting static images, icons, or animated sprites with scale modes.
- [CanvasWidget](<widgets/display/canvas-widget.md>): Procedural drawing surface widget invoking custom CanvasDrawCallback painting routines.
- [Sparkline](<widgets/display/sparkline.md>): Compact mini trendline and telemetry chart widget with fill gradient and hover data points.
- [Chart](<widgets/display/chart.md>): Data visualization chart widget supporting bar and line visualizations, axes, and interactive hover tooltips.
## Core / Widgets / Display

- [CanvasDrawCallback](<core/widgets/display/canvas-draw-callback.md>): Functional interface for drawing custom 2D graphics into a CanvasWidget.
## Core / Widgets / Feedback

- [OverlayHostAware](<core/widgets/feedback/overlay-host-aware.md>): Interface for widgets that dynamically calculate their position and layout within an overlay host bounds.
- [WindowManager](<core/widgets/feedback/window-manager.md>): Manager controlling floating window z-order, focus stack, active states, and desktop window arrangements.
## Widgets / Interaction

- [Button](<widgets/interaction/button.md>): Standard interactive push button with composable surface, icon, text, and rich state transitions.
- [HoldButton](<widgets/interaction/hold-button.md>): Confirmation button that requires holding down for a specified duration before triggering.
- [ToolButton](<widgets/interaction/tool-button.md>): Compact square toolbar button with icon, tooltip label, and toolbar styling.
- [IconButton](<widgets/interaction/icon-button.md>): Standalone graphic icon button without text.
- [ToggleButton](<widgets/interaction/toggle-button.md>): Two-state button that retains checked state upon clicking.
- [ToggleToolButton](<widgets/interaction/toggle-tool-button.md>): Two-state toolbar button combining ToolButton styling with ToggleButton mechanics.
- [ToggleSwitch](<widgets/interaction/toggle-switch.md>): Sliding switch control with smooth animated thumb transition and optional label.
- [Checkbox](<widgets/interaction/checkbox.md>): Square checkbox control supporting two-state and tri-state (indeterminate) modes.
- [RadioButton](<widgets/interaction/radio-button.md>): Circular option selector for mutually exclusive choices inside a RadioGroup.
- [TextInput](<widgets/interaction/text-input.md>): Base single-line text input with selection, caret navigation, clipboard integration, and reactive focus chrome.
- [TextField](<widgets/interaction/text-field.md>): Standard user-facing single-line editable text field component.
- [NumberField](<widgets/interaction/number-field.md>): Input field strictly accepting numeric values with range bounds, step increments, and drag adjustment.
- [PasswordField](<widgets/interaction/password-field.md>): Secure masked text input hiding characters with bullets or custom mask glyphs.
- [SearchField](<widgets/interaction/search-field.md>): Search input box with search icon, clear button, and debounced text change query dispatching.
- [TimeSpanField](<widgets/interaction/time-span-field.md>): Duration input field formatting time values like 1h 30m or 45s into java.time.Duration.
- [TextArea](<widgets/interaction/text-area.md>): Multi-line scrollable text editor with line wrapping, cursor navigation, selection, and scrollbar support.
- [Slider](<widgets/interaction/slider.md>): Range slider control allowing continuous or stepped value adjustment with draggable thumb.
- [ScrollBar](<widgets/interaction/scroll-bar.md>): Draggable scrollbar control for scrolling content along horizontal or vertical axes.
- [DropDownBox](<widgets/interaction/drop-down-box.md>): Collapsible container widget with an expandable header button and popup or inline content host.
- [ComboBox](<widgets/interaction/combo-box.md>): Selection control pairing a current selection button with a dropdown options list.
- [ColorPicker](<widgets/interaction/color-picker.md>): Interactive color selection control providing RGBA sliders, hex input, and popup palette.
- [DatePicker](<widgets/interaction/date-picker.md>): Date selection control featuring an ISO date text field and an interactive calendar popup.
- [TreeListPicker](<widgets/interaction/tree-list-picker.md>): Generic dropdown picker binding domain objects to displayed text options.
- [SearchableGridPickerWidget](<widgets/interaction/searchable-grid-picker-widget.md>): Abstract searchable dialog picker displaying items in a virtualized grid with preview, filter, and tooltip.
- [CodeEditor](<widgets/interaction/code-editor.md>): Rich code editor widget supporting syntax highlighting, diagnostics, line numbers, and autocompletion.
- [XmlCodeEditor](<widgets/interaction/xml-code-editor.md>): Specialized XML and UI layout code editor with schema validation, formatting, and tag autocompletion.
- [AdminConsole](<widgets/interaction/admin-console.md>): Interactive developer and administrative console widget with command execution and log output.
## Core / Widgets / Interaction

- [RadioGroup](<core/widgets/interaction/radio-group.md>): Controller managing mutual exclusivity and selection state across multiple RadioButton instances.
- [CodeLanguagePreset](<core/widgets/interaction/code-language-preset.md>): Enumeration of built-in language presets for syntax highlighting and validation.
- [CodeLanguagePresets](<core/widgets/interaction/code-language-presets.md>): Factory registry supplying tokenizers and validators for language presets.
- [CodeTokenizer](<core/widgets/interaction/code-tokenizer.md>): Functional interface converting source code text into colored syntax tokens.
- [CodeToken](<core/widgets/interaction/code-token.md>): Immutable range token associating character indices with a TokenStyle.
- [TokenStyle](<core/widgets/interaction/token-style.md>): Visual styling descriptor for syntax tokens including foreground color.
- [CodeValidator](<core/widgets/interaction/code-validator.md>): Functional interface performing static analysis and reporting code diagnostics.
- [CodeDiagnostic](<core/widgets/interaction/code-diagnostic.md>): Diagnostic report entry representing an error, warning, or hint at a specific code range.
- [CompletionProvider](<core/widgets/interaction/completion-provider.md>): Interface producing autocompletion proposals based on editor cursor context.
- [CompletionItem](<core/widgets/interaction/completion-item.md>): Individual autocompletion suggestion with rich label, insertion text, and replacement range.
- [CompletionContext](<core/widgets/interaction/completion-context.md>): Context record supplied to completion providers containing editor text and cursor position.
- [CodeTokenizationContext](<core/widgets/interaction/code-tokenization-context.md>): Context record passed to syntax tokenizers containing source text and language ID.
- [CodeValidationContext](<core/widgets/interaction/code-validation-context.md>): Context record passed to code validators containing document text and language ID.
- [AdminCommandRegistry](<core/widgets/interaction/admin-command-registry.md>): Central registry for console commands with argument completion, alias resolution, and handlers.
## Core / Widgets / Map

- [MapMarkerHandle](<core/widgets/map/map-marker-handle.md>): Handle for fluent configuration of marker positioning, screen sizing, zoom scaling, pivot, and visibility on MapCanvas.
- [MapProjection](<core/widgets/map/map-projection.md>): Axis-aligned linear projection engine converting between external world coordinates and MapCanvas internal map coordinates.
## Core / Widgets / Navigation

- [BreadcrumbItem](<core/widgets/navigation/breadcrumb-item.md>): Navigation item descriptor representing a step or crumb in a breadcrumb trail.
- [Menu](<core/widgets/navigation/menu.md>): Floating popup or dropdown menu widget hosting a vertical stack of menu items, command triggers, and separators.
- [MenuItem](<core/widgets/navigation/menu-item.md>): Selectable menu entry supporting regular actions, checkboxes, commands, keyboard shortcuts, or cascading submenus.
- [TreeViewNode](<core/widgets/navigation/tree-view-node.md>): Hierarchical node item in a TreeView holding text, custom icon, expansion state, and child nodes.
## Core / Debugging

- [DebugOverlaySettings](<core/debugging/debug-overlay-settings.md>): Configuration for placement, scaling, and sampling of the built-in performance overlay.
## Core / Text

- [DefaultFontRegistry](<core/text/default-font-registry.md>): Standard font registry loading TrueType and Type1 fonts from streams, files, or platform system fonts without Minecraft dependencies.
## Core / Widgets / DnD

- [DragController](<core/widgets/dnd/drag-controller.md>): Drag gesture controller attachable to any widget via composition to initiate drag-and-drop operations with ghost previews, threshold handling, and autoscrolling.
- [DragDropEvent](<core/widgets/dnd/drag-drop-event.md>): Routed event delivered to the target drop zone when the pointer is released with an accepted payload.
- [DragGhost](<core/widgets/dnd/drag-ghost.md>): Renderer for visual ghost representation of dragged elements displayed in the overlay layer beneath the cursor.
- [DragLeaveEvent](<core/widgets/dnd/drag-leave-event.md>): Routed event notified when the drag pointer exits a drop zone or when the gesture is aborted.
- [DragOutcome](<core/widgets/dnd/drag-outcome.md>): Represents the terminal outcome of a drag-and-drop gesture in UniGUI: successfully dropped, dropped without target, or cancelled.
- [DragOverEvent](<core/widgets/dnd/drag-over-event.md>): Routed event probing candidate drop zones beneath the pointer during drag movement and animation ticks.
- [DragPayload](<core/widgets/dnd/drag-payload.md>): Immutable transfer payload conveying identifier, type, preview text, and optional user object between drag source and drop target.
- [DragRoutedEvent](<core/widgets/dnd/drag-routed-event.md>): Abstract base class for routed drag-and-drop events conveying the active DragSession across widget hierarchies.
- [DragSession](<core/widgets/dnd/drag-session.md>): Live state and controller of an active drag gesture, managing pointer coordinates, active drop target, ghost positioning, insertion indicators, and autoscrolling.
- [DropController](<core/widgets/dnd/drop-controller.md>): Drop target controller attachable to any widget via composition to filter payloads and handle drag enter, over, leave, and drop events.
- [ReorderController](<core/widgets/dnd/reorder-controller.md>): Drag reordering controller attachable to container widgets (e.g. VBox, HBox, PanelWidget) supporting insertion lines and automated DOM reordering.
## Widgets / Containers

- [DockPanel](<widgets/containers/dock-panel.md>): Edge-docking layout container arranging child widgets along outer boundaries (left, right, top, bottom).
## Core / Widgets / Docking

- [DockArea](<core/widgets/docking/dock-area.md>): Target destination zone for docking operations: edge splits (LEFT, RIGHT, TOP, BOTTOM), tab groupings (CENTER, TAB), or detached floating windows (FLOAT).
- [DockColor](<core/widgets/docking/dock-color.md>): Semantic color roles used throughout DockTheme to style tabs, split bars, floating window chrome, and drop preview zones.
- [DockDragController](<core/widgets/docking/dock-drag-controller.md>): Handles drag-and-drop interactions for docking tabs within DockingRoot, computing drop previews, live tear-offs, and docking intents.
- [DockDropIntent](<core/widgets/docking/dock-drop-intent.md>): Resolved docking drop intention capturing the target area, preview bounds, and source/target pane identifiers.
- [DockingManager](<core/widgets/docking/docking-manager.md>): Non-visual layout controller managing the hierarchical docking tree, pane additions, splits, tab reordering, and snapshot serialization/restoration.
- [DockLayoutSnapshot](<core/widgets/docking/dock-layout-snapshot.md>): Immutable snapshot representing the recursive docking layout tree and active pane selection for serialization and layout persistence.
- [DockLayoutSnapshotCodec](<core/widgets/docking/dock-layout-snapshot-codec.md>): Compact string serializer and deserializer (DLS1 format) for persisting docking layouts across application restarts.
- [DockNode](<core/widgets/docking/dock-node.md>): Hierarchical docking tree node: either a LEAF containing tabbed DockPanes or a SPLIT node dividing two children horizontally or vertically.
- [DockPane](<core/widgets/docking/dock-pane.md>): Descriptor representing an individual dockable pane hosting arbitrary widget content, tab title, icon, dirty state, and close guards.
- [DockPaneKind](<core/widgets/docking/dock-pane-kind.md>): Classification of a docking pane: DOCUMENT (primary central workspace content) or TOOL (dockable side utility tool window).
- [DockPolicy](<core/widgets/docking/dock-policy.md>): Docking integration policy for windows: FULL (bidirectional docking and hosting), GUEST_ONLY (can dock into hosts but accepts no drops), or DISABLED.
- [DockSplitOrientation](<core/widgets/docking/dock-split-orientation.md>): Split layout orientation between adjacent dock nodes: HORIZONTAL (side-by-side) or VERTICAL (stacked top/bottom).
- [DockTheme](<core/widgets/docking/dock-theme.md>): Styling theme for the docking system configuring palette colors (DockColor), tab geometry, split handles, shadows, and window resize grips.
- [ResizeGripStyle](<core/widgets/docking/resize-grip-style.md>): Styling and rendering interface for bottom-right window resize grips, including preset TRIANGLE and LINES patterns.
## Widgets / Docking

- [DockingRoot](<widgets/docking/docking-root.md>): Root visual widget for the IDE docking system hosting dock tree nodes, tab strips, split handles, drop previews, and tear-off floating window integrations.
## Core / Render

- [DrawList](<core/render/draw-list.md>): Retained frame command buffer recording backend-neutral draw primitives, managing command recycling pools, and providing multi-channel layering.
- [DrawScope](<core/render/draw-scope.md>): Safe draw-list facade for widget renderers that translates high-level drawing calls into backend-neutral commands, manages transform pivot adjustments, and provides zero-allocation immediate primitives.
## Widgets / Effects

- [PostProcessingLayer](<widgets/effects/post-processing-layer.md>): Widget wrapper applying post-processing shader effects and rendering scales to its child content.
## Core / Events

- [EventEmitter](<core/events/event-emitter.md>): Contract for objects that can subscribe to and dispatch typed events.
- [EventScope](<core/events/event-scope.md>): Scoped lifecycle manager for event subscriptions.
- [FastEventBus](<core/events/fast-event-bus.md>): Ultra-fast, lock-free, zero-allocation event bus without reflection or hash-maps.
## Widgets / Feedback

- [ContextMenu](<widgets/feedback/context-menu.md>): Floating context menu container managing hierarchical items, action callbacks, and auto-dismiss behavior.
- [LoadingIndicator](<widgets/feedback/loading-indicator.md>): Highly configurable loading animation widget supporting ring, bar, pulse, and dots indicator modes.
- [NotificationView](<widgets/feedback/notification-view.md>): Rich notification card widget rendering severity icons, titles, messages, action buttons, and auto-dismiss progress.
- [OverlayLayer](<widgets/feedback/overlay-layer.md>): Full-screen overlay root managing modal backdrops, dialogs, popups, and layer-based hit testing.
- [Popup](<widgets/feedback/popup.md>): Anchored floating popup widget that positions itself relative to target widgets or screen coordinates.
- [ProgressBar](<widgets/feedback/progress-bar.md>): Linear progress bar widget displaying determinate or indeterminate completion state.
- [Spinner](<widgets/feedback/spinner.md>): Rotating progress spinner animation widget for indicating ongoing background operations.
- [Toast](<widgets/feedback/toast.md>): Non-intrusive brief notification banner supporting status icons, formatted text, and timed auto-dismiss.
- [Tooltip](<widgets/feedback/tooltip.md>): Hover-activated floating information bubble displaying helper text, rich layout, and delay timers.
- [WindowWidget](<widgets/feedback/window-widget.md>): Desktop-style floating window widget supporting title bar, dragging, resizing, collapse, modal dialogs, and docking integrations.
## Widgets / Containers

- [FlexBox](<widgets/containers/flex-box.md>): CSS-like flex container configured directly via LayoutStyle flexbox properties.
## Core / Widgets / Graph

- [HeaderModule](<core/widgets/graph/header-module.md>): Standard modular node title bar with an icon, title, and collapse control.
- [NodeGraphAlign](<core/widgets/graph/node-graph-align.md>): Alignment modes for arranging selected node items against their shared bounds.
- [NodeGraphClipboard](<core/widgets/graph/node-graph-clipboard.md>): Immutable buffer of selected registered node types and their internal wires for copy and paste.
- [NodeGraphConnection](<core/widgets/graph/node-graph-connection.md>): Represents a directional or bidirectional connection between two ports in a NodeGraph.
- [NodeGraphConnectionPolicy](<core/widgets/graph/node-graph-connection-policy.md>): Strategy interface defining rules and compatibility validation for forming connections between ports.
- [NodeGraphConnectionSnapshot](<core/widgets/graph/node-graph-connection-snapshot.md>): Serializable snapshot describing a connection between two node ports.
- [NodeGraphConnectionValidation](<core/widgets/graph/node-graph-connection-validation.md>): Validation result for proposed connections between node ports.
- [NodeGraphHistory](<core/widgets/graph/node-graph-history.md>): Undo and redo history for graph edits grouped into named transactions.
- [NodeGraphItem](<core/widgets/graph/node-graph-item.md>): Node item container placed in a NodeGraph canvas hosting a content widget and pin ports.
- [NodeGraphItemSnapshot](<core/widgets/graph/node-graph-item-snapshot.md>): Serializable snapshot capturing node item position, dimensions, capabilities, and ports.
- [NodeGraphPort](<core/widgets/graph/node-graph-port.md>): Pin/port descriptor on a NodeGraphItem connecting lines to other nodes.
- [NodeGraphPortKind](<core/widgets/graph/node-graph-port-kind.md>): Connection direction for a node-graph port: INPUT, OUTPUT, or BIDIRECTIONAL.
- [NodeGraphPortRef](<core/widgets/graph/node-graph-port-ref.md>): Identifies a specific port belonging to an item in a node graph.
- [NodeGraphPortSide](<core/widgets/graph/node-graph-port-side.md>): Visual placement edge for a node-graph port: LEFT, RIGHT, TOP, or BOTTOM.
- [NodeGraphPortSnapshot](<core/widgets/graph/node-graph-port-snapshot.md>): Serializable snapshot representing the state, geometry offset, and configuration of a node port.
- [NodeGraphRegion](<core/widgets/graph/node-graph-region.md>): A movable, resizable comment frame that groups nodes and regions on a graph canvas.
- [NodeGraphRegionSnapshot](<core/widgets/graph/node-graph-region-snapshot.md>): Serializable region state containing its label, comment, color, bounds, and interaction flags.
- [NodeGraphSelectionMode](<core/widgets/graph/node-graph-selection-mode.md>): Selection interaction modes for items and connections in a node graph: NONE, SINGLE, or MULTIPLE.
- [NodeGraphSnapshot](<core/widgets/graph/node-graph-snapshot.md>): Comprehensive serializable snapshot representing the entire node graph state, viewport, items, and connections.
- [NodeGraphViewport](<core/widgets/graph/node-graph-viewport.md>): Viewport transform state for node graph pan position and zoom factor.
- [NodeGraphWidgetResolver](<core/widgets/graph/node-graph-widget-resolver.md>): Factory callback to restore item content widgets when deserializing a node graph snapshot.
- [NodeLayoutContext](<core/widgets/graph/node-layout-context.md>): Layout services supplied to a node module for measuring widgets, placing them, and anchoring ports.
- [NodeModule](<core/widgets/graph/node-module.md>): Composable vertical layout module that owns widgets and ports inside a modular node.
- [NodePaintContext](<core/widgets/graph/node-paint-context.md>): Drawing services and node state supplied to a custom module background painter.
- [NodePaletteModel](<core/widgets/graph/node-palette-model.md>): Widget-free state model for node category browsing, ranked search, selection, and scrolling.
- [NodeStyle](<core/widgets/graph/node-style.md>): Per-node colors and geometry settings for modular graph nodes.
- [NodeType](<core/widgets/graph/node-type.md>): Reusable node-kind definition with identity, palette metadata, port sketches, and a node factory.
- [NodeTypeRegistry](<core/widgets/graph/node-type-registry.md>): Catalog of node types used by palettes, search, and restoring modular graph items.
- [PortRowModule](<core/widgets/graph/port-row-module.md>): Standard modular node row for input/output ports, labels, flow pins, and inline input editors.
- [PortShape](<core/widgets/graph/port-shape.md>): Marker geometries used to distinguish port types on the graph canvas.
- [PortType](<core/widgets/graph/port-type.md>): Immutable definition of a graph port's id, display name, color, marker shape, and flow behavior.
- [PortTypeRegistry](<core/widgets/graph/port-type-registry.md>): Port type catalog with colors, shapes, and directed data-conversion compatibility rules.
- [RerouteModule](<core/widgets/graph/reroute-module.md>): Compact pass-through node module for bending and branching graph wires.
- [SeparatorModule](<core/widgets/graph/separator-module.md>): Small horizontal divider module for separating groups of rows inside a node.
- [ValueEditor](<core/widgets/graph/value-editor.md>): Typed default-value editor contract for a node input, including UI, value access, and serialization.
- [ValueEditors](<core/widgets/graph/value-editors.md>): Factory of ready-made value editors for common node input types.
- [WidgetModule](<core/widgets/graph/widget-module.md>): Node module that embeds one arbitrary widget as a full-width row.
## Core / Widgets / Graph / Runtime

- [GraphExecutionException](<core/widgets/graph/runtime/graph-execution-exception.md>): Runtime exception raised when graph execution cannot complete safely.
- [GraphExecutor](<core/widgets/graph/runtime/graph-executor.md>): Runs a widget-free graph model by following execution-flow wires and evaluating data inputs on demand.
- [GraphIssue](<core/widgets/graph/runtime/graph-issue.md>): Validation diagnostic with severity, affected node and optional port, and a user-facing message.
- [GraphModel](<core/widgets/graph/runtime/graph-model.md>): Immutable, widget-free snapshot of nodes, ports, input values, and wires for validation or execution.
- [GraphResult](<core/widgets/graph/runtime/graph-result.md>): Result of a graph execution, including executed step count and the latest outputs of exec nodes.
- [GraphValidator](<core/widgets/graph/runtime/graph-validator.md>): Static checks for missing required inputs, invalid pure-data cycles, and unreachable execution nodes.
- [NodeBehavior](<core/widgets/graph/runtime/node-behavior.md>): Functional contract describing how a node computes values or executes a flow step.
- [NodeContext](<core/widgets/graph/runtime/node-context.md>): UI-free execution context for reading node inputs, publishing outputs, and controlling flow.
## Widgets / Graph

- [GraphView](<widgets/graph/graph-view.md>): Static or interactive diagram graph widget displaying connected nodes, edges, labels, and tooltips with custom renderers.
- [NodeBuilder](<widgets/graph/node-builder.md>): Fluent builder for assembling modular node items from headers, ports, editors, widgets, and custom modules.
- [NodeGraph](<widgets/graph/node-graph.md>): Interactive 2D node graph canvas supporting movable nodes, pin connections, pan, zoom, grid snapping, and selection.
- [NodePalette](<widgets/graph/node-palette.md>): Searchable overlay for browsing registered node types and creating nodes from a graph editor.
- [RegionEditor](<widgets/graph/region-editor.md>): Floating overlay editor for a graph region title or multiline comment.
## Widgets / Containers

- [GridBox](<widgets/containers/grid-box.md>): Multi-column grid container placing children into uniform cells or custom track templates.
- [HBox](<widgets/containers/hbox.md>): Horizontal linear container placing children left-to-right with uniform spacing.
## Core / Layout

- [LayoutStyle](<core/layout/layout-style.md>): Rules a parent uses to measure and place a widget: size, spacing, flex, grid, position, and overflow.
## Widgets / Containers

- [LinearBox](<widgets/containers/linear-box.md>): One-dimensional flex container arranging children sequentially along a main axis without wrapping.
## Widgets / Map

- [MapCanvas](<widgets/map/map-canvas.md>): Interactive 2D map widget extending WorldCanvas with map sizing, texture and grid layers, coordinate projection, and marker handles.
- [MapMarker](<widgets/map/map-marker.md>): Neutral base marker shell for MapCanvas supporting custom content, text labels, selection, and highlighting.
## Widgets / Navigation

- [Accordion](<widgets/navigation/accordion.md>): Vertical accordion container managing a stack of collapsible ExpandablePanel sections with optional single-panel open constraint.
- [Breadcrumb](<widgets/navigation/breadcrumb.md>): Interactive navigation trail widget displaying hierarchical breadcrumb items with customizable separators and selection handling.
- [Carousel](<widgets/navigation/carousel.md>): Paged carousel container supporting horizontal or vertical card paging, animated slide transitions, navigation buttons, and indicator dots.
- [ExpandablePanel](<widgets/navigation/expandable-panel.md>): Collapsible panel container with header bar, toggle chevron, and smooth expansion/collapse height animation.
- [MenuBar](<widgets/navigation/menu-bar.md>): Top-level horizontal menu bar hosting dropdown menus with command binding, keyboard accelerators, and popup coordination.
- [NavigationView](<widgets/navigation/navigation-view.md>): Sidebar navigation with collapsible categories of selectable items and a full-width accent selection.
- [PageView](<widgets/navigation/page-view.md>): Retained page container that keeps exactly one page visible at a time.
- [TabControl](<widgets/navigation/tab-control.md>): Tabbed navigation container with a scrollable header bar and switched content area.
- [ToolBar](<widgets/navigation/tool-bar.md>): Compact command toolbar with grouped buttons, separators, and command manager integration.
- [TreeList](<widgets/navigation/tree-list.md>): Specialized TreeView for hierarchical path navigation and directory listing.
- [TreeView](<widgets/navigation/tree-view.md>): Hierarchical tree container rendering collapsable nodes with custom icons, inline editing, and selection.
## Core / Textures

- [NineSlice](<core/textures/nine-slice.md>): Nine-patch scalable texture definition dividing an image into 4 unscaled corners, 4 single-axis edges, and 1 center area.
## Core / Render

- [Paint](<core/render/paint.md>): Styling descriptor for draw commands that configures color, stroke width, fill/stroke modes, Porter-Duff blending, and dashed lines.
## Widgets / Containers

- [PanelRowWidget](<widgets/containers/panel-row-widget.md>): Two-sided row container organizing widgets into left-aligned and right-aligned groups.
- [PanelWidget](<widgets/containers/panel-widget.md>): Base container for multi-child widgets managing hierarchy, deferred mutations, and rendering order.
## Examples / Demos

- [Retro Terminal](<examples/demos/retro-terminal.md>): A complete CRT-style command terminal built from UniGUI widgets, a custom AdminConsole, and a chained post-processing effect.
## Core / Render

- [RibbonMesh](<core/render/ribbon-mesh.md>): Procedural textured ribbon mesh generator along polylines for rendering roads, cables, traces, pipelines, and graph edges.
- [RibbonOptions](<core/render/ribbon-options.md>): Immutable value object configuring polyline ribbon mesh generation, corner miters, joint dots, and UV texture transforms.
## Core / Text

- [RichText](<core/text/rich-text.md>): Immutable multi-style rich text model supporting styled runs, fonts, brushes, gradients, and inline content spans.
## Widgets / Containers

- [ScrollView](<widgets/containers/scroll-view.md>): Scrollable viewport container hosting a single content widget with horizontal and vertical scroll support.
- [SettingRow](<widgets/containers/setting-row.md>): Specialized settings row holding a label on the left and an interactive control on the right.
## Core / Shaders

- [ShaderDrawOptions](<core/shaders/shader-draw-options.md>): Backend-facing configuration for shader draw commands governing blending, built-in uniforms, and vertex offsets.
- [ShaderHandle](<core/shaders/shader-handle.md>): Identifier and container for UI shaders, referencing external resource paths or embedding GLSL source code.
- [ShaderUniforms](<core/shaders/shader-uniforms.md>): Mutable collection of named shader parameters (floats, ints, vectors, colors) passed to GPU programs.
## Widgets / Containers

- [SplitPanel](<widgets/containers/split-panel.md>): Two-pane container separated by an interactive draggable splitter for dynamic resizing.
- [StackPanel](<widgets/containers/stack-panel.md>): Overlay container stacking all children within a shared rectangular slot.
## Core / Styling

- [Style](<core/styling/style.md>): Read-only typed style values with widget-state overrides and explicit fallback behavior.
- [MutableStyle](<core/styling/mutable-style.md>): Fluent mutable style builder with typed properties and state-specific overrides.
- [StyleDefinition](<core/styling/style-definition.md>): Immutable named style rule with selectors, renderer configuration, properties, and event animations.
- [StylePack](<core/styling/style-pack.md>): Editable theme containing reusable style rules, widget bindings, tokens, and animation presets.
- [ThemeTokens](<core/styling/theme-tokens.md>): Immutable design-token snapshot for palette, shapes, typography, and spacing.
## Widgets / Containers

- [SurfaceWidget](<widgets/containers/surface-widget.md>): Container with a composable Surface: background colors, multi-layer textures, shaders, borders, and corner radius.
## Core / Text

- [TextEngine](<core/text/text-engine.md>): Core text layout, line-wrapping, dimension measurement, and inline-content rendering engine.
- [TextOverflowMode](<core/text/text-overflow-mode.md>): Enumeration configuring visual clipping, truncation, shrinking, or marquee scrolling when text exceeds available bounds.
## Core / Textures

- [TextureGroup](<core/textures/texture-group.md>): Ordered composite collection of TextureLayer instances rendered sequentially within shared base bounds.
- [TextureHandle](<core/textures/texture-handle.md>): Backend-neutral texture resource interface providing dimensions, sampling options, and native object access.
- [TextureRegion](<core/textures/texture-region.md>): Immutable UV sub-rectangle of a parent texture, used to draw sprites from texture atlases.
- [AnimatedTexture](<core/textures/animated-texture.md>): Mutable sprite-sheet texture whose frame advances with game ticks, selected from a uniform cell grid.
- [TextureOptions](<core/textures/texture-options.md>): Immutable texture sampling configuration defining min/mag filtering, UV wrapping, mipmaps, and premultiplied alpha.
## Core / Debugging

- [UiDebugSnapshot](<core/debugging/ui-debug-snapshot.md>): Read-only per-frame timings, render counts, and texture-cache metrics.
## Core / PostEffects

- [UiLayerBounds](<core/posteffects/ui-layer-bounds.md>): Dimensions, position, and resolution scale for an offscreen post-processing layer target.
- [UiPostEffectBlendMode](<core/posteffects/ui-post-effect-blend-mode.md>): Blending behavior for a post-processing shader pass.
- [UiPostEffectChain](<core/posteffects/ui-post-effect-chain.md>): Immutable pipeline reference for post-processing UI layers.
- [UiPostEffectDefinition](<core/posteffects/ui-post-effect-definition.md>): Immutable specification of a post-processing effect with ordered shader passes.
- [UiPostEffectPass](<core/posteffects/ui-post-effect-pass.md>): Single shader pass configuration within a post-processing pipeline.
- [UiPostEffectRegistry](<core/posteffects/ui-post-effect-registry.md>): Registry repository storing named post-effect definitions.
- [UiPostEffectRenderer](<core/posteffects/ui-post-effect-renderer.md>): Rendering dispatcher with automatic fallback for post-processing UI layers.
- [UiPostEffectUniforms](<core/posteffects/ui-post-effect-uniforms.md>): Container for post-processing shader uniforms with static values and dynamic suppliers.
- [UiPostEffects](<core/posteffects/ui-post-effects.md>): Built-in catalog of standard post-processing effects and embedded shader sources.
## Core / Screens

- [UiRenderPolicy](<core/screens/ui-render-policy.md>): Frame-rate and invalidation policy controlling how frequently retained UI trees are rebuilt and rendered.
- [UniHudRender](<core/screens/uni-hud-render.md>): Minecraft-independent entry point for registering and managing in-game HUD layers.
- [UniRenderLayer](<core/screens/uni-render-layer.md>): Minecraft-independent configuration for an overlay or HUD UI layer.
- [UniRenderLayerRegistration](<core/screens/uni-render-layer-registration.md>): Backend-neutral handle for a registered render layer.
- [UniRenderTarget](<core/screens/uni-render-target.md>): Host rendering hook target for a neutral UniGUI render layer.
- [UniScreen](<core/screens/uni-screen.md>): Minecraft-independent description of a standalone screen/GUI window in UniGUI.
- [UniScreenOverlayRender](<core/screens/uni-screen-overlay-render.md>): Minecraft-independent entry point for overlays rendered on top of active GUI screens.
## Widgets / Containers

- [VBox](<widgets/containers/vbox.md>): Vertical linear container stacking children top-to-bottom with uniform spacing.
- [View](<widgets/containers/view.md>): Card-like visual container embedding a header Label and a vertical VBox content area.
## Widgets / Data

- [VirtualListView](<widgets/data/virtual-list-view.md>): Virtualized presentation of large item sets as a responsive grid or list.
## Core / Widgets

- [WidgetBase](<core/widgets/widget-base.md>): Shared widget foundation for layout, StylePack lookup, state, rendering, events, and animation.
## Widgets

- [Widget](<widgets/widget.md>): Base contract for any UI tree element: lifecycle, layout, rendering, input, and styling.
## Core / Widgets / World

- [AnchorLayer](<core/widgets/world/anchor-layer.md>): Layer managing screen-space UI widgets pinned to 3D/2D world coordinates inside WorldCanvas.
- [AnchorWidget](<core/widgets/world/anchor-widget.md>): Spatial anchor pinning a screen-space UI widget to 3D/2D world coordinates with zoom scaling, pivot, and viewport culling.
- [RibbonLayer](<core/widgets/world/ribbon-layer.md>): World canvas layer rendering textured or solid ribbon lines, paths, and waypoint routes in world coordinates.
- [WorldLayer](<core/widgets/world/world-layer.md>): Functional contract for background or decorative rendering layers drawn inside WorldCanvas.
## Widgets / World

- [WorldCanvas](<widgets/world/world-canvas.md>): Infinite 2D/3D world canvas supporting camera pan and zoom, coordinate transformations, background world layers, and pinned screen-space anchor widgets.
## Widgets / Containers

- [WrapPanel](<widgets/containers/wrap-panel.md>): Flow container wrapping children to a new row or column when space is exceeded.
