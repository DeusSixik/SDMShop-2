---
title: "TreeListPicker"
slug: "tree-list-picker"
category: "Widgets/Interaction"
locale: "ru"
order: 240
---

# TreeListPicker

Типизированный выпадающий список для выбора объектов предметной области с провайдером названий.

## Обзор

`TreeListPicker<T>` расширяет `ComboBox` и связывает список типизированных объектов `List<T>` с пользовательской функцией форматирования названий `labelProvider`.

## Выбор типизированных объектов

```java
TreeListPicker<PlayerProfile> picker = new TreeListPicker<PlayerProfile>()
        .values(players)
        .labelProvider(PlayerProfile::displayName);
```

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeListPicker::</span>`labelProvider(Function)` {: #labelproviderfunction data-toc-label="labelProvider(Function)" }

**Задать провайдер названий**

Устанавливает функцию преобразования объектов в строковые метки.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `labelProvider` | `Function` | — |

Возвращает `TreeListPicker`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeListPicker::</span>`selectedValue()` {: #selectedvalue data-toc-label="selectedValue()" }

**Получить выбранный объект**

Возвращает экземпляр текущего выбранного объекта или null.

Возвращает `Object`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeListPicker::</span>`values(List)` {: #valueslist data-toc-label="values(List)" }

**Задать объекты**

Устанавливает список типизированных объектов, доступных для выбора.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `values` | `List` | — |

Возвращает `TreeListPicker`.

</div>

