---
title: "MapMarker"
slug: "map-marker"
category: "Виджеты/Карта"
locale: "ru"
order: 0
---

# MapMarker

Базовая оболочка маркера для [MapCanvas](<map-canvas.md>) с поддержкой произвольного контента, текстовых меток, состояний выбора и подсветки.

## Обзор

`MapMarker` — нейтральная базовая оболочка для отображения маркеров на холсте `MapCanvas`.

Ключевые возможности:
- Размещение произвольных дочерних UI-виджетов или текстовых меток (`content`, `label`).
- Встроенные визуальные состояния выбора (`selected`) и целевой подсветки (`highlighted`).
- Прозрачный базовый стиль без лишних фонов и рамок для аккуратного отображения иконок и надписей.
- Управляется низкоуровневым объектом `AnchorWidget` для точного позиционирования.

## Создание и настройка маркера карты

```java
MapMarker marker = new MapMarker("Waypoint");
marker.selected(true);
```

## Связанные ссылки

- [AnchorWidget](<../../core/widgets/world/anchor-widget.md>): Пространственный якорь, привязывающий экранный UI-виджет к мировым координатам с масштабированием от зума, настройкой pivot и отсечением вне экрана.
- [MapCanvas](<map-canvas.md>): Интерактивный виджет 2D-карты, расширяющий [WorldCanvas](<../world/world-canvas.md>) поддержкой размеров карты, слоями текстур и сетки, проекцией координат и маркерами.
- [MapMarkerHandle](<../../core/widgets/map/map-marker-handle.md>): Дескриптор для удобной настройки положения, экранного размера, масштабирования от зума, опорной точки (pivot) и видимости маркера на [MapCanvas](<map-canvas.md>).

## API

<div class="api-member-card" markdown="1">

### `MapMarker()` {: #mapmarker data-toc-label="MapMarker()" }

Создает пустую оболочку маркера без стандартного оформления поверхности.

</div>

<div class="api-member-card" markdown="1">

### `MapMarker(String)` {: #mapmarkerstring data-toc-label="MapMarker(String)" }

Создает оболочку маркера с простой текстовой меткой.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `text` | `String` | Простая текстовая строка. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapMarker::</span>`anchor()` {: #anchor data-toc-label="anchor()" }

Возвращает низкоуровневый [AnchorWidget](<../../core/widgets/world/anchor-widget.md>), управляющий позиционированием этого маркера.

Возвращает `AnchorWidget`: Управляющий [AnchorWidget](<../../core/widgets/world/anchor-widget.md>)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapMarker::</span>`content()` {: #content data-toc-label="content()" }

Возвращает текущий виджет контента, вложенный в этот маркер.

Возвращает `Widget`: Виджет контента..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapMarker::</span>`content(Widget)` {: #contentwidget data-toc-label="content(Widget)" }

Заменяет виджет контента, вложенный в этот маркер.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `content` | `Widget` | Вложенный виджет контента. |

Возвращает `MapMarker`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapMarker::</span>`highlighted()` {: #highlighted data-toc-label="highlighted()" }

Возвращает true, если маркер в данный момент подсвечен.

Возвращает `boolean`: True, если активно, включено или успешно..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapMarker::</span>`highlighted(boolean)` {: #highlightedboolean data-toc-label="highlighted(boolean)" }

Устанавливает визуальное состояние подсветки маркера.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `highlighted` | `boolean` | Флаг состояния подсветки. |

Возвращает `MapMarker`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapMarker::</span>`label()` {: #label data-toc-label="label()" }

Возвращает виджет [Label](<../display/label.md>), если контентом является текстовая метка, или null в противном случае.

Возвращает `Label`: Экземпляр виджета [Label](<../display/label.md>) или null..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapMarker::</span>`label(String)` {: #labelstring data-toc-label="label(String)" }

Заменяет контент маркера на текстовый виджет [Label](<../display/label.md>).

| Параметр | Тип | Описание |
| --- | --- | --- |
| `text` | `String` | Простая текстовая строка. |

Возвращает `MapMarker`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapMarker::</span>`selected()` {: #selected data-toc-label="selected()" }

Возвращает true, если маркер в данный момент находится в выбранном состоянии.

Возвращает `boolean`: True, если активно, включено или успешно..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapMarker::</span>`selected(boolean)` {: #selectedboolean data-toc-label="selected(boolean)" }

Устанавливает визуальное состояние выбора маркера.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `selected` | `boolean` | Флаг состояния выбора. |

Возвращает `MapMarker`: Текущий экземпляр для цепочки вызовов..

</div>

