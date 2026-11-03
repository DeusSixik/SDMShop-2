---
title: "AnchorLayer"
slug: "anchor-layer"
category: "Core/Виджеты/Мир"
locale: "ru"
order: 0
---

# AnchorLayer

Слой управления экранными UI-виджетами, привязанными к мировым координатам внутри [WorldCanvas](<../../../widgets/world/world-canvas.md>).

## Обзор

`AnchorLayer` управляет коллекцией экранных UI-виджетов, привязанных к мировым координатам внутри `WorldCanvas`.

Основные задачи:
- Добавление, поиск и удаление привязанных виджетов (`add`, `anchor`, `remove`, `clear`).
- Размещение интерактивных виджетов (кнопок, меток, панелей), сохраняющих экранный ввод при перемещении камеры.
- Эффективное кэширование снимка якорей для быстрого рендеринга и маршрутизации событий.

## Добавление привязанного виджета на холст мира

```java
AnchorWidget anchor = canvas.anchorLayer().add("flag", worldX, worldY, new Button("Waypoint"))
    .screenSize(80.0f, 24.0f)
    .pivot(0.5f, 1.0f);
```

## Связанные ссылки

- [AnchorWidget](<anchor-widget.md>): Пространственный якорь, привязывающий экранный UI-виджет к мировым координатам с масштабированием от зума, настройкой pivot и отсечением вне экрана.
- [WorldCanvas](<../../../widgets/world/world-canvas.md>): Холст бесконечного 2D/3D пространства с поддержкой панорамирования, зума камеры, преобразования координат, фоновых слоев и привязанных экранных виджетов.

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnchorLayer::</span>`add(String, float, float, Widget)` {: #addstring-float-float-widget data-toc-label="add(String, float, float, Widget)" }

Добавляет UI-виджет, привязанный к указанным мировым координатам.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `id` | `String` | Уникальный строковый идентификатор. |
| `worldX` | `float` | Мировая координата X. |
| `worldY` | `float` | Мировая координата Y. |
| `widget` | `Widget` | UI-виджет для привязки в пространстве мировых координат. |

Возвращает `AnchorWidget`: Созданный или найденный [AnchorWidget](<anchor-widget.md>)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnchorLayer::</span>`anchor(String)` {: #anchorstring data-toc-label="anchor(String)" }

Находит [AnchorWidget](<anchor-widget.md>) по его строковому идентификатору или возвращает null, если не найден.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `id` | `String` | Уникальный строковый идентификатор. |

Возвращает `AnchorWidget`: Созданный или найденный [AnchorWidget](<anchor-widget.md>)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnchorLayer::</span>`anchors()` {: #anchors data-toc-label="anchors()" }

Возвращает неизменяемый список всех активных якорей в этом слое.

Возвращает `List`: Неизменяемый список активных [AnchorWidget](<anchor-widget.md>)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnchorLayer::</span>`clear()` {: #clear data-toc-label="clear()" }

Удаляет и отсоединяет все якоря из этого слоя.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnchorLayer::</span>`remove(AnchorWidget)` {: #removeanchorwidget data-toc-label="remove(AnchorWidget)" }

Удаляет указанный [AnchorWidget](<anchor-widget.md>) из этого слоя.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `anchor` | `AnchorWidget` | Экземпляр [AnchorWidget](<anchor-widget.md>) для удаления. |

Возвращает `boolean`: True, если активно, включено или успешно..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnchorLayer::</span>`remove(String)` {: #removestring data-toc-label="remove(String)" }

Удаляет привязанный виджет по его строковому идентификатору.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `id` | `String` | Уникальный строковый идентификатор. |

Возвращает `boolean`: True, если активно, включено или успешно..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnchorLayer::</span>`size()` {: #size data-toc-label="size()" }

Возвращает количество активных якорей в этом слое.

Возвращает `int`: Числовое количество..

</div>

