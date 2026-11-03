---
title: "TreeList"
slug: "tree-list"
category: "Виджеты/Навигация"
locale: "ru"
order: 0
---

# TreeList

Специализированный древовидный список [TreeView](<tree-view.md>) для иерархической навигации по путям и файловой структуре.

## Обзор

Специализированный древовидный список [TreeView](<tree-view.md>) для иерархической навигации по путям и файловой структуре. Предоставляет широкие возможности настройки компоновки и взаимодействия.

## Базовое использование

```java
TreeList treeList = new TreeList();
treeList.addPath("src", "main", "java");
treeList.addPath("src", "test", "java");
```

## Связанные ссылки

- [TreeView](<tree-view.md>): Иерархический древовидный контейнер со сворачиваемыми узлами, кастомными иконками, инлайн-редактированием и выбором.

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeList::</span>`addPath(String[])` {: #addpathstring data-toc-label="addPath(String[])" }

Добавляет иерархический путь меток в структуру дерева, создавая промежуточные папки.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `labels` | `String[]` | Метки иерархического пути. |

Возвращает `TreeList`: Текущий экземпляр для цепочки вызовов..

</div>

