---
title: "DockLayoutSnapshotCodec"
slug: "dock-layout-snapshot-codec"
category: "Core/Виджеты/Докинг"
locale: "ru"
order: 0
---

# DockLayoutSnapshotCodec

Компактный строковый сериализатор и десериализатор формата DLS1 для сохранения раскладок докинга между перезапусками приложения.

## Обзор

`DockLayoutSnapshotCodec` выполняет сериализацию и десериализацию `DockLayoutSnapshot` в компактный строковый формат `DLS1`.

Формат компактно кодирует рекурсивные узлы сплитов, пропорции разделения, активные вкладки и списки идентификаторов панелей.

## Сериализация и десериализация снимка раскладки

```java
String encoded = DockLayoutSnapshotCodec.encode(snapshot);
DockLayoutSnapshot restored = DockLayoutSnapshotCodec.decode(encoded);
```

## Связанные ссылки

- [DockLayoutSnapshot](<dock-layout-snapshot.md>): Неизменяемый снимок, представляющий рекурсивное дерево раскладки докинга и выбранные панели для сериализации и сохранения состояния.
- [DockingManager](<docking-manager.md>): Невизуальный контроллер раскладки, управляющий иерархическим деревом докинга, добавлением панелей, разделениями, порядком вкладок и снимками раскладки.

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockLayoutSnapshotCodec::</span>`decode(String)` {: #decodestring data-toc-label="decode(String)" }

Десериализует строку формата DLS1 обратно в экземпляр снимка [DockLayoutSnapshot](<dock-layout-snapshot.md>).

| Параметр | Тип | Описание |
| --- | --- | --- |
| `encoded` | `String` | Закодированная строка снимка формата DLS1. |

Возвращает `DockLayoutSnapshot`: Декодированный экземпляр снимка раскладки [DockLayoutSnapshot](<dock-layout-snapshot.md>)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockLayoutSnapshotCodec::</span>`encode(DockLayoutSnapshot)` {: #encodedocklayoutsnapshot data-toc-label="encode(DockLayoutSnapshot)" }

Сериализует снимок раскладки в компактную строку формата DLS1.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `snapshot` | `DockLayoutSnapshot` | Снимок раскладки для кодирования. |

Возвращает `String`: Компактная строковая репрезентация формата DLS1..

</div>

