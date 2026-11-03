---
title: "DockLayoutSnapshot"
slug: "dock-layout-snapshot"
category: "Core/Виджеты/Докинг"
locale: "ru"
order: 0
---

# DockLayoutSnapshot

Неизменяемый снимок, представляющий рекурсивное дерево раскладки докинга и выбранные панели для сериализации и сохранения состояния.

## Обзор

`DockLayoutSnapshot` сохраняет снимок всей иерархии дерева докинга для персистентности.

### Процесс сохранения

1. Снятие: `DockLayoutSnapshot snapshot = dockingManager.snapshot();`
2. Сериализация: `String encoded = DockLayoutSnapshotCodec.encode(snapshot);`
3. Сохранение: Запись строки `encoded` в файл настроек или профиль проекта.
4. Восстановление: `dockingRoot.restoreLayout(snapshot, paneRegistry);`

## Создание и восстановление снимка раскладки

```java
DockLayoutSnapshot snapshot = DockLayoutSnapshot.from(dockingManager.rootNode(), "editor-1");
```

## Связанные ссылки

- [DockLayoutSnapshotCodec](<dock-layout-snapshot-codec.md>): Компактный строковый сериализатор и десериализатор формата DLS1 для сохранения раскладок докинга между перезапусками приложения.
- [DockingManager](<docking-manager.md>): Невизуальный контроллер раскладки, управляющий иерархическим деревом докинга, добавлением панелей, разделениями, порядком вкладок и снимками раскладки.

## API

<div class="api-member-card" markdown="1">

### `DockLayoutSnapshot(Node)` {: #docklayoutsnapshotnode data-toc-label="DockLayoutSnapshot(Node)" }

Создает снимок раскладки с указанным корневым узлом.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `root` | `Node` | Корневой сериализованный узел. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockLayoutSnapshot::</span>`from(DockNode)` {: #fromdocknode data-toc-label="from(DockNode)" }

Создает неизменяемый снимок из живой иерархии узлов [DockNode](<dock-node.md>).

| Параметр | Тип | Описание |
| --- | --- | --- |
| `root` | `DockNode` | Живой корневой узел докинга для снятия снимка. |

Возвращает `DockLayoutSnapshot`: Неизменяемый снимок иерархии докинга..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockLayoutSnapshot::</span>`from(DockNode, String)` {: #fromdocknode-string data-toc-label="from(DockNode, String)" }

Создает неизменяемый снимок из живой иерархии узлов [DockNode](<dock-node.md>) с идентификатором активной панели.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `root` | `DockNode` | Живой корневой узел докинга для снятия снимка. |
| `activePaneId` | `String` | Идентификатор активной панели докинга. |

Возвращает `DockLayoutSnapshot`: Неизменяемый снимок иерархии докинга..

</div>

