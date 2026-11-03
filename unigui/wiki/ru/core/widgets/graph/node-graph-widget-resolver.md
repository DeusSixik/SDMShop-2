---
title: "NodeGraphWidgetResolver"
slug: "node-graph-widget-resolver"
category: "Core/Виджеты/Графы"
locale: "ru"
order: 0
---

# NodeGraphWidgetResolver

Фабричный колбэк для восстановления виджетов содержимого узлов при десериализации снимка графа.

## Обзор

Фабричный колбэк для восстановления виджетов содержимого узлов при десериализации снимка графа. Предоставляет широкие возможности настройки компоновки и взаимодействия.

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphWidgetResolver::</span>`resolve(String, String)` {: #resolvestring-string data-toc-label="resolve(String, String)" }

Разрешает или создает экземпляр виджета содержимого для десериализованного узла.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `itemId` | `String` | Идентификатор элемента. |
| `contentType` | `String` | Параметр contentType. |

Возвращает `Widget`: Разрешенный виджет содержимого..

</div>

