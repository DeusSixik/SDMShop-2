# Вики UniGUI

## Core / Reactive

- [Привязка сигналов](<core/reactive/widget-binding.md>): Связывает свойства виджетов с типизированными сигналами и синхронизирует изменения в обе стороны.
## Widgets / Containers

- [Border](<widgets/containers/border.md>): Render-only декоративная рамка без дочерних виджетов.
- [Box](<widgets/containers/box.md>): Стандартный визуальный контейнер: цвет фона, текстура, шейдер, рамка, радиус и дочерние виджеты.
## Core / Виджеты / Контейнеры

- [DockSide](<core/widgets/containers/dock-side.md>): Определяет внешнюю сторону (LEFT, RIGHT, TOP, BOTTOM), к которой прикрепляется дочерний виджет в DockPanel.
## Widgets / Data

- [VirtualTableView](<widgets/data/virtual-table-view.md>): Высокопроизводительная виртуализированная таблица с изменением размера колонок, перетаскиванием, сортировкой и редактированием ячеек.
## Core / Виджеты / Данные

- [VirtualTableColumn](<core/widgets/data/virtual-table-column.md>): Конфигурация столбца для VirtualTableView с заголовком, шириной, выравниванием и режимом переполнения текста.
## Widgets / Display

- [TextWidget](<widgets/display/text-widget.md>): Базовый абстрактный виджет отображения текста, поддерживающий строки, RichText, шрифты, градиенты и кисти.
- [Label](<widgets/display/label.md>): Текстовая метка, связанная с интерактивным элементом ввода или управления.
- [Text](<widgets/display/text.md>): Легковесный виджет отображения статических или динамических строк и RichText.
- [TextBlock](<widgets/display/text-block.md>): Виджет отображения многострочных абзацев текста с поддержкой переноса, межстрочного интервала и выравнивания.
- [RichTextView](<widgets/display/rich-text-view.md>): Виджет форматированного RichText текста со стилизованными фрагментами, иконками, цветами и ссылками.
- [Separator](<widgets/display/separator.md>): Горизонтальный или вертикальный разделитель для визуального зонирования интерфейса.
- [Shape](<widgets/display/shape.md>): Виджет векторных геометрических фигур: прямоугольников, скруглений, кругов, овалов и контуров.
- [Path](<widgets/display/path.md>): Виджет векторного пути для произвольных 2D-кривых Безье, ломаных линий и контуров.
- [TextureWidget](<widgets/display/texture-widget.md>): Базовый виджет для отрисовки текстур, UV-координат, 9-slice рамок и дескрипторов изображений.
- [ImageView](<widgets/display/image-view.md>): Виджет отображения текстур, представляющий статические картинки, иконки или спрайты.
- [CanvasWidget](<widgets/display/canvas-widget.md>): Процедурный холст для рисования произвольной графики через CanvasDrawCallback.
- [Sparkline](<widgets/display/sparkline.md>): Компактный микрографик трендов и телеметрии с градиентной заливкой и точками при наведении.
- [Chart](<widgets/display/chart.md>): Виджет визуализации данных с поддержкой столбчатых и линейных графиков, координатных осей и подсказок.
## Core / Виджеты / Отображение

- [CanvasDrawCallback](<core/widgets/display/canvas-draw-callback.md>): Функциональный интерфейс для пользовательской 2D-отрисовки на CanvasWidget.
## Core / Виджеты / Обратная связь

- [OverlayHostAware](<core/widgets/feedback/overlay-host-aware.md>): Интерфейс для виджетов, динамически рассчитывающих свое положение и компоновку в границах оверлея.
- [WindowManager](<core/widgets/feedback/window-manager.md>): Менеджер окон, управляющий z-порядком, стеком фокуса, активными состояниями и расположением окон.
## Widgets / Interaction

- [Button](<widgets/interaction/button.md>): Стандартная интерактивная кнопка с композируемой поверхностью, иконкой, текстом и переходами состояний.
- [HoldButton](<widgets/interaction/hold-button.md>): Кнопка подтверждения, требующая удержания нажатой в течение заданного времени.
- [ToolButton](<widgets/interaction/tool-button.md>): Компактная квадратная кнопка панели инструментов с иконкой, подсказкой и стилем тулбара.
- [IconButton](<widgets/interaction/icon-button.md>): Кнопка с графической иконкой без текстовой подписи.
- [ToggleButton](<widgets/interaction/toggle-button.md>): Двухпозиционная кнопка, сохраняющая состояние выбора при нажатии.
- [ToggleToolButton](<widgets/interaction/toggle-tool-button.md>): Двухпозиционная кнопка панели инструментов, объединяющая стиль ToolButton и функционал ToggleButton.
- [ToggleSwitch](<widgets/interaction/toggle-switch.md>): Скользящий переключатель с плавной анимацией бегунка и опциональной подписью.
- [Checkbox](<widgets/interaction/checkbox.md>): Квадратный флажок с поддержкой двухпозиционного и трёхпозиционного (неопределённого) режимов.
- [RadioButton](<widgets/interaction/radio-button.md>): Круглый переключатель для взаимоисключающего выбора внутри RadioGroup.
- [TextInput](<widgets/interaction/text-input.md>): Базовое однострочное поле ввода текста с выделением, перемещением курсора, буфером обмена и фокусной рамкой.
- [TextField](<widgets/interaction/text-field.md>): Стандартное однострочное редактируемое текстовое поле.
- [NumberField](<widgets/interaction/number-field.md>): Поле ввода чисел с поддержкой ограничений диапазона, шага изменения и настройки перетаскиванием.
- [PasswordField](<widgets/interaction/password-field.md>): Защищённое поле ввода пароля, маскирующее символы точками или указанным глифом.
- [SearchField](<widgets/interaction/search-field.md>): Поле поиска с иконкой лупы, кнопкой очистки крестиком и дебаунсом отправки запросов.
- [TimeSpanField](<widgets/interaction/time-span-field.md>): Поле ввода временных интервалов, форматирующее значения вида 1h 30m или 45s в java.time.Duration.
- [TextArea](<widgets/interaction/text-area.md>): Многострочный текстовый редактор с переносом строк, навигацией курсора, выделением и полосой прокрутки.
- [Slider](<widgets/interaction/slider.md>): Элемент управления диапазоном значений с перетаскиваемым бегунком и шагом изменения.
- [ScrollBar](<widgets/interaction/scroll-bar.md>): Интерактивная полоса прокрутки для прокрутки содержимого по вертикали или горизонтали.
- [DropDownBox](<widgets/interaction/drop-down-box.md>): Раскрывающийся контейнер с кнопкой-заголовком и выпадающим списком в режиме popup или inline.
- [ComboBox](<widgets/interaction/combo-box.md>): Элемент выбора значения из выпадающего списка с возможностью прокрутки опций.
- [ColorPicker](<widgets/interaction/color-picker.md>): Интерактивный компонент выбора цвета с ползунками каналов RGBA, вводом hex и всплывающей палитрой.
- [DatePicker](<widgets/interaction/date-picker.md>): Виджет выбора даты с текстовым полем ввода ISO-формата и интерактивным всплывающим календарем.
- [TreeListPicker](<widgets/interaction/tree-list-picker.md>): Типизированный выпадающий список для выбора объектов предметной области с провайдером названий.
- [SearchableGridPickerWidget](<widgets/interaction/searchable-grid-picker-widget.md>): Абстрактный диалоговый компонент выбора элементов в виртуализированной сетке с поисковой фильтрацией, предпросмотром и подсказками.
- [CodeEditor](<widgets/interaction/code-editor.md>): Продвинутый редактор кода с подсветкой синтаксиса, диагностикой ошибок, нумерацией строк и автодополнением.
- [XmlCodeEditor](<widgets/interaction/xml-code-editor.md>): Специализированный редактор XML/XAML разметки интерфейсов с валидацией схемы, форматированием и автодополнением тегов.
- [AdminConsole](<widgets/interaction/admin-console.md>): Интерактивная консоль разработчика и администратора с выполнением команд, историей и выводом логов.
## Core / Виджеты / Взаимодействие

- [RadioGroup](<core/widgets/interaction/radio-group.md>): Контроллер, управляющий взаимоисключающим выбором и состоянием группы радиокнопок.
- [CodeLanguagePreset](<core/widgets/interaction/code-language-preset.md>): Перечисление встроенных пресетов языков для подсветки синтаксиса и валидации.
- [CodeLanguagePresets](<core/widgets/interaction/code-language-presets.md>): Фабричный реестр, поставляющий токенизаторы и валидаторы для языковых пресетов.
- [CodeTokenizer](<core/widgets/interaction/code-tokenizer.md>): Функциональный интерфейс для разбиения исходного кода на токены с цветовыми стилями.
- [CodeToken](<core/widgets/interaction/code-token.md>): Неизменяемый токен диапазона символов, связывающий индексы с визуальным стилем TokenStyle.
- [TokenStyle](<core/widgets/interaction/token-style.md>): Дескриптор визуального стиля для синтаксических токенов, включая цвет текста.
- [CodeValidator](<core/widgets/interaction/code-validator.md>): Функциональный интерфейс статического анализа текста, формирующий диагностические сообщения об ошибках.
- [CodeDiagnostic](<core/widgets/interaction/code-diagnostic.md>): Запись диагностики, представляющая ошибку, предупреждение или подсказку в заданном диапазоне кода.
- [CompletionProvider](<core/widgets/interaction/completion-provider.md>): Интерфейс формирования предложений автодополнения на основе контекста курсора.
- [CompletionItem](<core/widgets/interaction/completion-item.md>): Элемент предложения автодополнения со стилизованной меткой, вставляемым текстом и диапазоном замены.
- [CompletionContext](<core/widgets/interaction/completion-context.md>): Контекстная запись, передаваемая провайдерам автодополнения, содержащая текст редактора и позицию курсора.
- [CodeTokenizationContext](<core/widgets/interaction/code-tokenization-context.md>): Контекстная запись, передаваемая токенизаторам, содержащая исходный текст и идентификатор языка.
- [CodeValidationContext](<core/widgets/interaction/code-validation-context.md>): Контекстная запись, передаваемая валидаторам кода, содержащая текст документа и идентификатор языка.
- [AdminCommandRegistry](<core/widgets/interaction/admin-command-registry.md>): Центральный реестр команд консоли с поддержкой автодополнения аргументов, псевдонимов и обработчиков.
## Core / Виджеты / Карта

- [MapMarkerHandle](<core/widgets/map/map-marker-handle.md>): Дескриптор для удобной настройки положения, экранного размера, масштабирования от зума, опорной точки (pivot) и видимости маркера на MapCanvas.
- [MapProjection](<core/widgets/map/map-projection.md>): Движок линейной проекции для преобразования между внешними мировыми координатами и внутренними координатами карты MapCanvas.
## Core / Виджеты / Навигация

- [BreadcrumbItem](<core/widgets/navigation/breadcrumb-item.md>): Дескриптор навигационного элемента, представляющий шаг или крошку в цепочке навигации.
- [Menu](<core/widgets/navigation/menu.md>): Всплывающее или выпадающее меню, содержащее вертикальный список командных пунктов меню и разделителей.
- [MenuItem](<core/widgets/navigation/menu-item.md>): Выбираемый элемент меню, поддерживающий обычные действия, флажки, команды, горячие клавиши или каскадные подменю.
- [TreeViewNode](<core/widgets/navigation/tree-view-node.md>): Узел иерархической структуры TreeView с текстом, иконкой, состоянием раскрытия и дочерними узлами.
## Core / Debugging

- [DebugOverlaySettings](<core/debugging/debug-overlay-settings.md>): Настройки положения, масштаба и выборки встроенного оверлея производительности.
## Core / Text

- [DefaultFontRegistry](<core/text/default-font-registry.md>): Стандартный реестр шрифтов, загружающий TrueType и Type1 шрифты из файлов, потоков или системных шрифтов ОС без зависимостей от Minecraft.
## Core / Виджеты / Drag-and-Drop

- [DragController](<core/widgets/dnd/drag-controller.md>): Контроллер жеста перетаскивания, подключаемый к любому виджету через композицию для инициации drag-and-drop с ghost-предпросмотром, порогом смещения и автопрокруткой.
- [DragDropEvent](<core/widgets/dnd/drag-drop-event.md>): Маршрутизируемое событие, доставляемое в целевую зону сброса при отпускании указателя с принятым payload.
- [DragGhost](<core/widgets/dnd/drag-ghost.md>): Отрисовщик визуального ghost-представления перетаскиваемых элементов, отображаемый в слое оверлея под курсором.
- [DragLeaveEvent](<core/widgets/dnd/drag-leave-event.md>): Маршрутизируемое событие, уведомляющее о выходе указателя перетаскивания из зоны сброса или прерывании жеста.
- [DragOutcome](<core/widgets/dnd/drag-outcome.md>): Описывает конечный результат жеста drag-and-drop в UniGUI: успешный сброс, сброс без цели или отмена.
- [DragOverEvent](<core/widgets/dnd/drag-over-event.md>): Маршрутизируемое событие, опрашивающее зоны сброса под указателем в процессе перемещения перетаскивания и тиков анимации.
- [DragPayload](<core/widgets/dnd/drag-payload.md>): Неизменяемый контейнер передаваемых данных (payload), переносящий идентификатор, тип, текст предпросмотра и пользовательский объект между источником и целью сброса.
- [DragRoutedEvent](<core/widgets/dnd/drag-routed-event.md>): Абстрактный базовый класс для маршрутизируемых событий drag-and-drop, передающих активную сессию DragSession по иерархии виджетов.
- [DragSession](<core/widgets/dnd/drag-session.md>): Состояние и контроллер активного жеста перетаскивания, управляющий координатами указателя, активной целью сброса, позиционированием ghost, индикаторами вставки и автопрокруткой.
- [DropController](<core/widgets/dnd/drop-controller.md>): Контроллер цели сброса, подключаемый к любому виджету через композицию для фильтрации payload и обработки событий drag enter, over, leave и drop.
- [ReorderController](<core/widgets/dnd/reorder-controller.md>): Контроллер переупорядочивания при перетаскивании, подключаемый к контейнерам (VBox, HBox, PanelWidget) с поддержкой линий вставки и автоматического изменения порядка элементов.
## Widgets / Containers

- [DockPanel](<widgets/containers/dock-panel.md>): Контейнер разметки, прикрепляющий дочерние виджеты к внешним границам (слева, справа, сверху, снизу).
## Core / Виджеты / Докинг

- [DockArea](<core/widgets/docking/dock-area.md>): Целевая зона назначения для операций докинга: краевые разделения (LEFT, RIGHT, TOP, BOTTOM), группировка во вкладки (CENTER, TAB) или плавающие окна (FLOAT).
- [DockColor](<core/widgets/docking/dock-color.md>): Семантические роли цветов темы DockTheme для стилизации вкладок, разделителей, обрамления плавающих окон и зон предпросмотра сброса.
- [DockDragController](<core/widgets/docking/dock-drag-controller.md>): Обрабатывает жесты drag-and-drop для вкладок докинга внутри DockingRoot, вычисляя предпросмотр сброса, отрыв в окно и намерения докинга.
- [DockDropIntent](<core/widgets/docking/dock-drop-intent.md>): Разрешенное намерение сброса докинга, содержащее целевую область, границы предпросмотра и идентификаторы исходной и целевой панелей.
- [DockingManager](<core/widgets/docking/docking-manager.md>): Невизуальный контроллер раскладки, управляющий иерархическим деревом докинга, добавлением панелей, разделениями, порядком вкладок и снимками раскладки.
- [DockLayoutSnapshot](<core/widgets/docking/dock-layout-snapshot.md>): Неизменяемый снимок, представляющий рекурсивное дерево раскладки докинга и выбранные панели для сериализации и сохранения состояния.
- [DockLayoutSnapshotCodec](<core/widgets/docking/dock-layout-snapshot-codec.md>): Компактный строковый сериализатор и десериализатор формата DLS1 для сохранения раскладок докинга между перезапусками приложения.
- [DockNode](<core/widgets/docking/dock-node.md>): Иерархический узел дерева докинга: листовой узел (LEAF) со вкладками DockPane или узел разделения (SPLIT), делящий детей горизонтально или вертикально.
- [DockPane](<core/widgets/docking/dock-pane.md>): Дескриптор отдельной панели докинга, содержащей виджет содержимого, заголовок вкладки, иконку, признак несохраненных правок и стражи закрытия.
- [DockPaneKind](<core/widgets/docking/dock-pane-kind.md>): Классификация панели докинга: DOCUMENT (основное содержимое рабочей области) или TOOL (вспомогательное инструментальное окно).
- [DockPolicy](<core/widgets/docking/dock-policy.md>): Политика интеграции докинга для окон: FULL (полноценный докинг и прием сброса), GUEST_ONLY (может стыковаться, но не принимает сброс) или DISABLED.
- [DockSplitOrientation](<core/widgets/docking/dock-split-orientation.md>): Ориентация разделения между соседними узлами докинга: HORIZONTAL (бок о бок) или VERTICAL (друг над другом).
- [DockTheme](<core/widgets/docking/dock-theme.md>): Тема оформления системы докинга, настраивающая палитру цветов (DockColor), геометрию вкладок, разделители, тени и ручки изменения размера окон.
- [ResizeGripStyle](<core/widgets/docking/resize-grip-style.md>): Интерфейс стилизации и отрисовки угловых ручек изменения размера окна, включая встроенные стили TRIANGLE и LINES.
## Виджеты / Докинг

- [DockingRoot](<widgets/docking/docking-root.md>): Корневой визуальный виджет системы докинга IDE, объединяющий узлы дерева, полосы вкладок, разделители, предпросмотр сброса и отрыв в плавающие окна.
## Core / Render

- [DrawList](<core/render/draw-list.md>): Буфер команд рисования одного кадра, записывающий аппаратно-независимые примитивы, переиспользующий команды из пула и предоставляющий многослойную запись (channels).
- [DrawScope](<core/render/draw-scope.md>): Безопасный draw-list фасад для рендереров виджетов: транслирует высокоуровневые вызовы отрисовки в backend-нейтральные команды, автоматически корректирует pivot трансформаций и предоставляет garbage-free примитивы.
## Виджеты / Эффекты

- [PostProcessingLayer](<widgets/effects/post-processing-layer.md>): Обертка виджета, применяющая шейдерные эффекты постобработки и масштаб рендеринга к дочернему контенту.
## Core / Events

- [EventEmitter](<core/events/event-emitter.md>): Контракт для объектов, способных регистрировать слушателей и рассылать типизированные события.
- [EventScope](<core/events/event-scope.md>): Контейнер управления жизненным циклом подписок на события.
- [FastEventBus](<core/events/fast-event-bus.md>): Сверхбыстрый lock-free event bus без рефлексии, хэш-мап и аллокаций памяти при диспетчеризации.
## Виджеты / Обратная связь

- [ContextMenu](<widgets/feedback/context-menu.md>): Всплывающее контекстное меню, управляющее иерархическими элементами, обработчиками действий и автозакрытием.
- [LoadingIndicator](<widgets/feedback/loading-indicator.md>): Виджет индикатора загрузки с поддержкой режимов кольца, полосы, пульсации и точек.
- [NotificationView](<widgets/feedback/notification-view.md>): Виджет карточки уведомления с поддержкой уровней важности, заголовков, текста, кнопок действий и шкалы таймера.
- [OverlayLayer](<widgets/feedback/overlay-layer.md>): Полноэкранный слой оверлеев, управляющий модальными подложками, диалогами, всплывающими окнами и проверкой попаданий.
- [Popup](<widgets/feedback/popup.md>): Всплывающий виджет, позиционируемый относительно целевых элементов интерфейса или экранных координат.
- [ProgressBar](<widgets/feedback/progress-bar.md>): Линейный индикатор прогресса, отображающий детерминированное или неопределенное состояние выполнения.
- [Spinner](<widgets/feedback/spinner.md>): Вращающийся индикатор прогресса (спиннер) для отображения фоновых операций.
- [Toast](<widgets/feedback/toast.md>): Краткое всплывающее уведомление (тост) со значками статуса, форматированным текстом и автоматическим скрытием.
- [Tooltip](<widgets/feedback/tooltip.md>): Всплывающая подсказка (тултип), отображающая вспомогательный текст, форматированный контент и таймеры задержки.
- [WindowWidget](<widgets/feedback/window-widget.md>): Оконный виджет в стиле рабочего стола с поддержкой заголовка, перетаскивания, изменения размера, сворачивания и модальности.
## Widgets / Containers

- [FlexBox](<widgets/containers/flex-box.md>): Flex-контейнер, настраиваемый напрямую через свойства LayoutStyle, аналог CSS display: flex.
## Core / Виджеты / Графы

- [HeaderModule](<core/widgets/graph/header-module.md>): Стандартная полоса заголовка модульного узла с иконкой, названием и управлением сворачиванием.
- [NodeGraphAlign](<core/widgets/graph/node-graph-align.md>): Режимы выравнивания выбранных элементов графа по общим границам.
- [NodeGraphClipboard](<core/widgets/graph/node-graph-clipboard.md>): Неизменяемый буфер выбранных зарегистрированных типов узлов и внутренних соединений для копирования.
- [NodeGraphConnection](<core/widgets/graph/node-graph-connection.md>): Соединение между двумя портами узлов в NodeGraph.
- [NodeGraphConnectionPolicy](<core/widgets/graph/node-graph-connection-policy.md>): Интерфейс политики валидации и правил формирования соединений между портами.
- [NodeGraphConnectionSnapshot](<core/widgets/graph/node-graph-connection-snapshot.md>): Сериализуемый снимок соединения между двумя портами узлов.
- [NodeGraphConnectionValidation](<core/widgets/graph/node-graph-connection-validation.md>): Результат валидации предлагаемого соединения между портами узлов.
- [NodeGraphHistory](<core/widgets/graph/node-graph-history.md>): История undo/redo изменений графа, сгруппированных в именованные транзакции.
- [NodeGraphItem](<core/widgets/graph/node-graph-item.md>): Элемент-узел на холсте NodeGraph, содержащий виджет содержимого и порты подключения.
- [NodeGraphItemSnapshot](<core/widgets/graph/node-graph-item-snapshot.md>): Сериализуемый снимок позиции, размеров, возможностей и портов элемента-узла.
- [NodeGraphPort](<core/widgets/graph/node-graph-port.md>): Порт подключения на элементе NodeGraphItem для связывания линиями с другими узлами.
- [NodeGraphPortKind](<core/widgets/graph/node-graph-port-kind.md>): Направление соединения для порта узла: INPUT, OUTPUT или BIDIRECTIONAL.
- [NodeGraphPortRef](<core/widgets/graph/node-graph-port-ref.md>): Ссылка на конкретный порт узла в графе узлов.
- [NodeGraphPortSide](<core/widgets/graph/node-graph-port-side.md>): Сторона визуального размещения порта узла: LEFT, RIGHT, TOP или BOTTOM.
- [NodeGraphPortSnapshot](<core/widgets/graph/node-graph-port-snapshot.md>): Сериализуемый снимок состояния, геометрического смещения и настроек порта узла.
- [NodeGraphRegion](<core/widgets/graph/node-graph-region.md>): Перемещаемая и изменяемая область-комментарий для группировки узлов на холсте графа.
- [NodeGraphRegionSnapshot](<core/widgets/graph/node-graph-region-snapshot.md>): Сериализуемое состояние области: заголовок, комментарий, цвет, границы и флаги взаимодействия.
- [NodeGraphSelectionMode](<core/widgets/graph/node-graph-selection-mode.md>): Режимы выделения элементов и соединений в графе: NONE, SINGLE или MULTIPLE.
- [NodeGraphSnapshot](<core/widgets/graph/node-graph-snapshot.md>): Полный сериализуемый снимок состояния графа узлов, области просмотра, элементов и соединений.
- [NodeGraphViewport](<core/widgets/graph/node-graph-viewport.md>): Состояние области просмотра холста графа узлов: позиция панорамирования и масштаб.
- [NodeGraphWidgetResolver](<core/widgets/graph/node-graph-widget-resolver.md>): Фабричный колбэк для восстановления виджетов содержимого узлов при десериализации снимка графа.
- [NodeLayoutContext](<core/widgets/graph/node-layout-context.md>): Сервисы раскладки модуля для измерения и размещения виджетов, а также привязки портов.
- [NodeModule](<core/widgets/graph/node-module.md>): Композиционный вертикальный модуль раскладки, владеющий виджетами и портами внутри узла.
- [NodePaintContext](<core/widgets/graph/node-paint-context.md>): Контекст рисования и состояния узла для отрисовки фона пользовательского модуля.
- [NodePaletteModel](<core/widgets/graph/node-palette-model.md>): Модель без виджетов для просмотра категорий, ранжированного поиска, выбора и прокрутки палитры узлов.
- [NodeStyle](<core/widgets/graph/node-style.md>): Настройки цветов и геометрии отдельного модульного узла графа.
- [NodeType](<core/widgets/graph/node-type.md>): Повторно используемое описание вида узла: идентификатор, метаданные палитры, эскизы портов и фабрика.
- [NodeTypeRegistry](<core/widgets/graph/node-type-registry.md>): Каталог типов узлов для палитры, поиска и восстановления модульных элементов графа.
- [PortRowModule](<core/widgets/graph/port-row-module.md>): Стандартная строка модульного узла для портов, подписей, потоковых пинов и inline-редакторов.
- [PortShape](<core/widgets/graph/port-shape.md>): Формы маркеров, различающие типы портов на холсте графа.
- [PortType](<core/widgets/graph/port-type.md>): Неизменяемое описание ID, имени, цвета, формы маркера и потокового режима типа порта графа.
- [PortTypeRegistry](<core/widgets/graph/port-type-registry.md>): Каталог типов портов с цветами, формами и направленными правилами совместимости типов данных.
- [RerouteModule](<core/widgets/graph/reroute-module.md>): Компактный сквозной модуль-узел для изгиба и ветвления проводов графа.
- [SeparatorModule](<core/widgets/graph/separator-module.md>): Небольшой модуль горизонтального разделителя между группами строк узла.
- [ValueEditor](<core/widgets/graph/value-editor.md>): Контракт типизированного редактора значения по умолчанию входа узла: UI, доступ к значению и сериализация.
- [ValueEditors](<core/widgets/graph/value-editors.md>): Фабрика готовых редакторов значений для распространённых типов входов узла.
- [WidgetModule](<core/widgets/graph/widget-module.md>): Модуль узла, встраивающий произвольный виджет отдельной строкой на всю ширину.
## Core / Виджеты / Графы / Исполнение

- [GraphExecutionException](<core/widgets/graph/runtime/graph-execution-exception.md>): Исключение времени выполнения, возникающее, когда граф нельзя безопасно завершить.
- [GraphExecutor](<core/widgets/graph/runtime/graph-executor.md>): Запускает модель графа без виджетов, переходя по потоковым связям и вычисляя входы по требованию.
- [GraphIssue](<core/widgets/graph/runtime/graph-issue.md>): Диагностика проверки графа с уровнем важности, затронутым узлом и необязательным портом.
- [GraphModel](<core/widgets/graph/runtime/graph-model.md>): Неизменяемый снимок графа без виджетов: узлы, порты, значения входов и связи для проверки или исполнения.
- [GraphResult](<core/widgets/graph/runtime/graph-result.md>): Результат исполнения графа: число шагов и последние выходные значения исполняемых узлов.
- [GraphValidator](<core/widgets/graph/runtime/graph-validator.md>): Статические проверки обязательных входов, циклов чистых data-узлов и недостижимых исполняемых узлов.
- [NodeBehavior](<core/widgets/graph/runtime/node-behavior.md>): Функциональный контракт вычисления значений узла или выполнения шага потока.
- [NodeContext](<core/widgets/graph/runtime/node-context.md>): Контекст исполнения без UI для чтения входов узла, публикации выходов и управления потоком.
## Виджеты / Графы

- [GraphView](<widgets/graph/graph-view.md>): Виджет статической или интерактивной диаграммы графа с узлами, ребрами, подписями и всплывающими подсказками.
- [NodeBuilder](<widgets/graph/node-builder.md>): Fluent-сборщик модульного элемента-узла из заголовка, портов, редакторов, виджетов и пользовательских модулей.
- [NodeGraph](<widgets/graph/node-graph.md>): Интерактивный холст 2D-графа узлов с перемещением блоков, соединениями портов, панорамированием, масштабированием и рамкой выделения.
- [NodePalette](<widgets/graph/node-palette.md>): Поисковая overlay-палитра типов узлов для создания узлов в редакторе графа.
- [RegionEditor](<widgets/graph/region-editor.md>): Всплывающий overlay-редактор заголовка или многострочного комментария области графа.
## Widgets / Containers

- [GridBox](<widgets/containers/grid-box.md>): Сетка с равномерным делением на колонки или настраиваемыми шаблонами треков.
- [HBox](<widgets/containers/hbox.md>): Горизонтальный линейный контейнер, размещающий детей слева направо с единым шагом отступа.
## Core / Layout

- [LayoutStyle](<core/layout/layout-style.md>): Правила, по которым родитель измеряет и размещает виджет: размер, отступы, flex, grid, позиция и переполнение.
## Widgets / Containers

- [LinearBox](<widgets/containers/linear-box.md>): Одномерный flex-контейнер, последовательно размещающий детей вдоль главной оси без переноса строк.
## Виджеты / Карта

- [MapCanvas](<widgets/map/map-canvas.md>): Интерактивный виджет 2D-карты, расширяющий WorldCanvas поддержкой размеров карты, слоями текстур и сетки, проекцией координат и маркерами.
- [MapMarker](<widgets/map/map-marker.md>): Базовая оболочка маркера для MapCanvas с поддержкой произвольного контента, текстовых меток, состояний выбора и подсветки.
## Виджеты / Навигация

- [Accordion](<widgets/navigation/accordion.md>): Вертикальный контейнер аккордеона, управляющий стопкой раскрывающихся панелей ExpandablePanel с поддержкой режима одиночного открытия.
- [Breadcrumb](<widgets/navigation/breadcrumb.md>): Интерактивная цепочка навигации («хлебные крошки»), отображающая иерархические элементы с настраиваемыми разделителями и обработкой выбора.
- [Carousel](<widgets/navigation/carousel.md>): Контейнер карусели со страницами, поддерживающий горизонтальное или вертикальное перелистывание, анимацию переходов, кнопки навигации и индикаторы.
- [ExpandablePanel](<widgets/navigation/expandable-panel.md>): Сворачиваемая панель с заголовком, шевроном переключения и плавной анимацией высоты раскрытия.
- [MenuBar](<widgets/navigation/menu-bar.md>): Горизонтальная строка меню верхнего уровня, содержащая выпадающие меню с привязкой к командам и координацией всплывающих списков.
- [NavigationView](<widgets/navigation/navigation-view.md>): Боковая навигация со сворачиваемыми группами пунктов и акцентным выделением выбранного пункта на всю ширину.
- [PageView](<widgets/navigation/page-view.md>): Контейнер страниц с сохранением состояния, отображающий ровно одну страницу за раз.
- [TabControl](<widgets/navigation/tab-control.md>): Вкладочный навигационный контейнер с прокручиваемой строкой заголовков и переключаемой областью содержимого.
- [ToolBar](<widgets/navigation/tool-bar.md>): Компактная панель инструментов с кнопками команд, разделителями и интеграцией с CommandManager.
- [TreeList](<widgets/navigation/tree-list.md>): Специализированный древовидный список TreeView для иерархической навигации по путям и файловой структуре.
- [TreeView](<widgets/navigation/tree-view.md>): Иерархический древовидный контейнер со сворачиваемыми узлами, кастомными иконками, инлайн-редактированием и выбором.
## Core / Textures

- [NineSlice](<core/textures/nine-slice.md>): Определение масштабируемой 9-patch текстуры: 4 неизменяемых угла, 4 растягиваемых края и 1 центральная область.
## Core / Render

- [Paint](<core/render/paint.md>): Дескриптор стиля команд отрисовки, настраивающий цвет, толщину обводки, режимы заливки/контура, Porter-Duff смешивание и пунктирные линии.
## Widgets / Containers

- [PanelRowWidget](<widgets/containers/panel-row-widget.md>): Двусторонняя строка, выравнивающая виджеты по левому и правому краям.
- [PanelWidget](<widgets/containers/panel-widget.md>): Базовый контейнер для виджетов с несколькими детьми: управление иерархией, отложенные мутации и порядок отрисовки.
## Examples / Demos

- [Ретро-терминал](<examples/demos/retro-terminal.md>): Полноценный терминал в стиле CRT, собранный из виджетов UniGUI, собственного AdminConsole и цепочки post-processing эффектов.
## Core / Render

- [RibbonMesh](<core/render/ribbon-mesh.md>): Генератор процедурной текстурированной ленты вдоль полилиний для отрисовки дорог, кабелей, трасс, трубопроводов и связей графов.
- [RibbonOptions](<core/render/ribbon-options.md>): Неизменяемый объект настроек для генерации полигональной ленты, угловых стыков, круглых заглушек и UV-трансформаций текстуры.
## Core / Text

- [RichText](<core/text/rich-text.md>): Неизменяемая модель форматированного текста, объединяющая стилизованные текстовые фрагменты, шрифты, градиенты и inline-элементы.
## Widgets / Containers

- [ScrollView](<widgets/containers/scroll-view.md>): Контейнер области прокрутки (viewport), вмещающий один дочерний виджет и управляющий горизонтальным и вертикальным скроллингом.
- [SettingRow](<widgets/containers/setting-row.md>): Специализированная строка настроек с текстовой подписью слева и интерактивным элементом управления справа.
## Core / Shaders

- [ShaderDrawOptions](<core/shaders/shader-draw-options.md>): Параметры команд отрисовки шейдеров: управление блендингом, стандартными uniforms и смещением вершин.
- [ShaderHandle](<core/shaders/shader-handle.md>): Идентификатор и контейнер UI-шейдеров, ссылающийся на путь к ресурсу или содержащий встроенный GLSL-код.
- [ShaderUniforms](<core/shaders/shader-uniforms.md>): Изменяемая коллекция именованных параметров шейдера (float, int, векторы, цвета), передаваемых в GPU-программы.
## Widgets / Containers

- [SplitPanel](<widgets/containers/split-panel.md>): Двухсекционный контейнер, разделённый интерактивным перемещаемым разделителем.
- [StackPanel](<widgets/containers/stack-panel.md>): Контейнер наложения (overlay), размещающий всех детей в одном общем слоте.
## Core / Styling

- [Style](<core/styling/style.md>): Неизменяемые типизированные значения стиля с override-ами состояний и явным fallback.
- [MutableStyle](<core/styling/mutable-style.md>): Fluent-сборщик изменяемого стиля с типизированными свойствами и override-ами состояний.
- [StyleDefinition](<core/styling/style-definition.md>): Неизменяемое именованное правило с селекторами, renderer-ом, свойствами и анимациями событий.
- [StylePack](<core/styling/style-pack.md>): Редактируемая тема с переиспользуемыми правилами, привязками виджетов, токенами и анимациями.
- [ThemeTokens](<core/styling/theme-tokens.md>): Неизменяемый снимок design-токенов палитры, форм, типографики и отступов.
## Widgets / Containers

- [SurfaceWidget](<widgets/containers/surface-widget.md>): Контейнер с композируемой поверхностью: фон, многослойные текстуры, шейдеры, рамка и радиус скругления.
## Core / Text

- [TextEngine](<core/text/text-engine.md>): Базовый движок компоновки текста, переноса строк, расчёта метрик и отрисовки встроенного inline-контента.
- [TextOverflowMode](<core/text/text-overflow-mode.md>): Перечисление режимов обработки текста, превышающего доступные границы виджета (обрезка, многоточие, сжатие, бегущая строка).
## Core / Textures

- [TextureGroup](<core/textures/texture-group.md>): Упорядоченная композитная коллекция слоёв TextureLayer, отрисовываемых последовательно в общих границах.
- [TextureHandle](<core/textures/texture-handle.md>): Backend-нейтральный интерфейс текстурного ресурса, предоставляющий размеры, параметры сэмплирования и доступ к нативному объекту.
- [TextureRegion](<core/textures/texture-region.md>): Неизменяемая прямоугольная UV-область внутри родительской текстуры для отрисовки спрайтов из атласов.
- [AnimatedTexture](<core/textures/animated-texture.md>): Изменяемая текстура sprite sheet, кадры которой сменяются по игровым тикам из равномерной сетки ячеек.
- [TextureOptions](<core/textures/texture-options.md>): Неизменяемая конфигурация сэмплирования текстур: min/mag фильтрация, wrap-режимы UV, mipmaps и premultiplied alpha.
## Core / Debugging

- [UiDebugSnapshot](<core/debugging/ui-debug-snapshot.md>): Неизменяемый снимок времени кадра, объёма рендера и работы кэша текстур.
## Core / PostEffects

- [UiLayerBounds](<core/posteffects/ui-layer-bounds.md>): Размеры, положение и масштаб разрешения для закадрового буфера слоя пост-эффектов.
- [UiPostEffectBlendMode](<core/posteffects/ui-post-effect-blend-mode.md>): Режим наложения и смешивания для шейдерного прохода пост-эффекта.
- [UiPostEffectChain](<core/posteffects/ui-post-effect-chain.md>): Неизменяемая ссылка на конвейер пост-эффектов для UI-слоя.
- [UiPostEffectDefinition](<core/posteffects/ui-post-effect-definition.md>): Неизменяемое описание пост-эффекта с упорядоченными шейдерными проходами.
- [UiPostEffectPass](<core/posteffects/ui-post-effect-pass.md>): Конфигурация одного шейдерного прохода внутри конвейера пост-эффектов.
- [UiPostEffectRegistry](<core/posteffects/ui-post-effect-registry.md>): Реестр для регистрации и поиска именованных дефиниций пост-эффектов.
- [UiPostEffectRenderer](<core/posteffects/ui-post-effect-renderer.md>): Диспетчер отрисовки с автоматическим откатом для слоев с пост-эффектами.
- [UiPostEffectUniforms](<core/posteffects/ui-post-effect-uniforms.md>): Контейнер параметров uniforms пост-эффекта со статическими и динамическими поставщиками значений.
- [UiPostEffects](<core/posteffects/ui-post-effects.md>): Каталог стандартных пост-эффектов и встроенных шейдеров для слоев UniGUI.
## Core / Screens

- [UiRenderPolicy](<core/screens/ui-render-policy.md>): Политика частоты обновления и рендера retained UI, управляющая пересборкой кадра по таймингу или сигналам инвалидации.
- [UniHudRender](<core/screens/uni-hud-render.md>): Точка входа для регистрации и управления слоями внутриигрового HUD.
- [UniRenderLayer](<core/screens/uni-render-layer.md>): Платформо-независимая конфигурация слоя оверлея или HUD.
- [UniRenderLayerRegistration](<core/screens/uni-render-layer-registration.md>): Дескриптор зарегистрированного слоя отрисовки с возможностью динамического управления.
- [UniRenderTarget](<core/screens/uni-render-target.md>): Целевой хост-контекст отрисовки для слоя UniGUI.
- [UniScreen](<core/screens/uni-screen.md>): Платформо-независимое описание полноэкранного GUI-окна UniGUI.
- [UniScreenOverlayRender](<core/screens/uni-screen-overlay-render.md>): Точка входа для оверлеев, отрисовываемых поверх открытых игровых экранов.
## Widgets / Containers

- [VBox](<widgets/containers/vbox.md>): Вертикальный линейный контейнер, размещающий детей сверху вниз с единым шагом отступа.
- [View](<widgets/containers/view.md>): Контейнер в виде карточки, объединяющий визуальное оформление Box со встроенным заголовком и вертикальной компоновкой контента.
## Widgets / Data

- [VirtualListView](<widgets/data/virtual-list-view.md>): Виртуализированное представление больших наборов элементов в виде адаптивной сетки или списка.
## Core / Виджеты

- [WidgetBase](<core/widgets/widget-base.md>): Общая основа виджетов для layout, StylePack, состояния, рендера, событий и анимаций.
## Widgets

- [Widget](<widgets/widget.md>): Базовый контракт любого элемента UI-дерева: жизненный цикл, компоновка, отрисовка, ввод и стили.
## Core / Виджеты / Мир

- [AnchorLayer](<core/widgets/world/anchor-layer.md>): Слой управления экранными UI-виджетами, привязанными к мировым координатам внутри WorldCanvas.
- [AnchorWidget](<core/widgets/world/anchor-widget.md>): Пространственный якорь, привязывающий экранный UI-виджет к мировым координатам с масштабированием от зума, настройкой pivot и отсечением вне экрана.
- [RibbonLayer](<core/widgets/world/ribbon-layer.md>): Слой WorldCanvas для отрисовки текстурированных или сплошных лент, маршрутов и полилиний в мировых координатах.
- [WorldLayer](<core/widgets/world/world-layer.md>): Функциональный контракт для фоновых или декоративных слоев отрисовки внутри WorldCanvas.
## Виджеты / Мир

- [WorldCanvas](<widgets/world/world-canvas.md>): Холст бесконечного 2D/3D пространства с поддержкой панорамирования, зума камеры, преобразования координат, фоновых слоев и привязанных экранных виджетов.
## Widgets / Containers

- [WrapPanel](<widgets/containers/wrap-panel.md>): Потоковый flow-контейнер с переносом детей на новую строку или колонку при нехватке места.
