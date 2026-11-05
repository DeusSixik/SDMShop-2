---
title: "NodeGraphConnectionValidation"
slug: "node-graph-connection-validation"
category: "Core/Виджеты/Графы"
locale: "ru"
order: 0
---

# NodeGraphConnectionValidation

Результат валидации предлагаемого соединения между портами узлов.

## Обзор

Результат валидации предлагаемого соединения между портами узлов. Предоставляет широкие возможности настройки компоновки и взаимодействия.

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphConnectionValidation::</span>`accepted()` {: #accepted data-toc-label="accepted()" }

Создает успешный результат валидации соединения.

Возвращает `NodeGraphConnectionValidation`: Успешный результат валидации соединения..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphConnectionValidation::</span>`invalid(String)` {: #invalidstring data-toc-label="invalid(String)" }

Создает результат отклоненной валидации соединения с пояснением причины.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `reason` | `String` | Сообщение с причиной отклонения соединения. |

Возвращает `NodeGraphConnectionValidation`: Отклоненный результат валидации соединения..

</div>

