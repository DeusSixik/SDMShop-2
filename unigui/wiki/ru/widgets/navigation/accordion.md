---
title: "Accordion"
slug: "accordion"
category: "Виджеты/Навигация"
locale: "ru"
order: 0
---

# Accordion

Вертикальный контейнер аккордеона, управляющий стопкой раскрывающихся панелей [ExpandablePanel](<expandable-panel.md>) с поддержкой режима одиночного открытия.

## Обзор

Вертикальный контейнер аккордеона, управляющий стопкой раскрывающихся панелей [ExpandablePanel](<expandable-panel.md>) с поддержкой режима одиночного открытия. Предоставляет широкие возможности настройки компоновки и взаимодействия.

## Базовое использование

```java
Accordion accordion = new Accordion();
accordion.singleOpen(true);
accordion.addPanel(new ExpandablePanel("General"));
```

## Связанные ссылки

- [ExpandablePanel](<expandable-panel.md>): Сворачиваемая панель с заголовком, шевроном переключения и плавной анимацией высоты раскрытия.

## API

<div class="api-member-card" markdown="1">

### `Accordion()` {: #accordion data-toc-label="Accordion()" }

Создает пустой контейнер аккордеона Accordion.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Accordion::</span>`addPanel(ExpandablePanel)` {: #addpanelexpandablepanel data-toc-label="addPanel(ExpandablePanel)" }

Добавляет раскрывающуюся панель [ExpandablePanel](<expandable-panel.md>) в аккордеон.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `panel` | `ExpandablePanel` | Раскрывающаяся панель. |

Возвращает `Accordion`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Accordion::</span>`clearPanels()` {: #clearpanels data-toc-label="clearPanels()" }

Удаляет все панели из аккордеона.

Возвращает `Accordion`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Accordion::</span>`panels()` {: #panels data-toc-label="panels()" }

Возвращает неизменяемый список всех панелей аккордеона.

Возвращает `List`: Неизменяемый список раскрывающихся панелей..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Accordion::</span>`removePanel(ExpandablePanel)` {: #removepanelexpandablepanel data-toc-label="removePanel(ExpandablePanel)" }

Удаляет указанную панель [ExpandablePanel](<expandable-panel.md>) из аккордеона.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `panel` | `ExpandablePanel` | Раскрывающаяся панель. |

Возвращает `Accordion`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Accordion::</span>`singleOpen()` {: #singleopen data-toc-label="singleOpen()" }

Возвращает true, если при открытии одной панели автоматически закрываются остальные.

Возвращает `boolean`: true, если активен режим одиночного открытия..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Accordion::</span>`singleOpen(boolean)` {: #singleopenboolean data-toc-label="singleOpen(boolean)" }

Устанавливает, разрешено ли открывать только одну панель одновременно.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `singleOpen` | `boolean` | Разрешено ли оставлять открытой только одну панель. |

Возвращает `Accordion`: Текущий экземпляр для цепочки вызовов..

</div>

