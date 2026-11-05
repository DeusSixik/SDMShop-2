---
title: "SearchField"
slug: "search-field"
category: "Widgets/Interaction"
locale: "ru"
order: 150
---

# SearchField

Поле поиска с иконкой лупы, кнопкой очистки крестиком и дебаунсом отправки запросов.

## Обзор

`SearchField` дополняет `TextInput` встроенной иконкой лупы, интерактивной кнопкой быстрой очистки поля и автоматическим дебаунсом (`searchChangeDebounceSeconds`) для предотвращения частых запросов при быстром наборе.

## Фильтр поиска с задержкой

```java
SearchField search = new SearchField()
        .placeholder("Поиск по элементам...")
        .searchChangeDebounceSeconds(0.2f);
search.onSearchChanged(e -> filterGrid(e.newQuery()));
```

## Связанные ссылки

- [TextInput](<text-input.md>)

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">SearchField::</span>`clear()` {: #clear data-toc-label="clear()" }

**Очистить поле поиска**

Очищает введённый поисковый запрос.

Возвращает `SearchField`: этот виджет для fluent-настройки.

</div>

