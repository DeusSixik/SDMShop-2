---
title: "NodeGraphConnectionPolicy"
slug: "node-graph-connection-policy"
category: "Core/Виджеты/Графы"
locale: "ru"
order: 0
---

# NodeGraphConnectionPolicy

Интерфейс политики валидации и правил формирования соединений между портами.

## Обзор

Интерфейс политики валидации и правил формирования соединений между портами. Предоставляет широкие возможности настройки компоновки и взаимодействия.

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphConnectionPolicy::</span>`validate(NodeGraph, NodeGraphPortRef, NodeGraphPortRef)` {: #validatenodegraph-nodegraphportref-nodegraphportref data-toc-label="validate(NodeGraph, NodeGraphPortRef, NodeGraphPortRef)" }

Проверяет допустимость создания соединения между исходным и целевым портами.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `graph` | `NodeGraph` | Параметр graph. |
| `from` | `NodeGraphPortRef` | Ссылка на исходный порт. |
| `to` | `NodeGraphPortRef` | Ссылка на целевой порт. |

Возвращает `NodeGraphConnectionValidation`: Результат валидации соединения..

</div>

