---
title: "View"
slug: "view"
category: "Widgets/Containers"
locale: "ru"
order: 160
---

# View

Контейнер в виде карточки, объединяющий визуальное оформление [Box](<box.md>) со встроенным заголовком и вертикальной компоновкой контента.

## Обзор

View расширяет `Box`, предоставляя готовую панель или карточку. Контейнер автоматически инициализирует внутренний `VBox`, содержащий заголовок `Label` и область для контента. Он часто используется в экранах отладки, демонстрационных панелях и карточках интерфейса.

## Стилизация и структура

Поскольку View наследует `Box`, он поддерживает полную визуальную отрисовку фона (сплошной цвет, градиент, текстуры), рамки, скругление углов и тени. Оформление по умолчанию загружается из `WidgetStyles.View.DEFAULT`.

## Создание карточки инспектора

```java
View card = new View("Properties");
card.addContent(new Label("Status: Active"));
card.addContent(new Button("Save"));
```

## Связанные ссылки

- [Box](<box.md>)
- [PanelWidget](<panel-widget.md>)
- [VBox](<vbox.md>)

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">View::</span>`title(String)` {: #titlestring data-toc-label="title(String)" }

**Задать заголовок**

Задаёт текст заголовка карточки.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `title` | `String` | строка текста заголовка |

Возвращает `View`: этот виджет для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">View::</span>`content()` {: #content data-toc-label="content()" }

**Получить контейнер контента**

Возвращает внутренний вертикальный контейнер [VBox](<vbox.md>), содержащий дочерние виджеты.

Возвращает `VBox`: внутренний контейнер [VBox](<vbox.md>).

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">View::</span>`addContent(Widget)` {: #addcontentwidget data-toc-label="addContent(Widget)" }

**Добавить виджет в контент**

Добавляет виджет во внутренний вертикальный контейнер содержимого под заголовком.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `widget` | `Widget` | виджет для добавления в содержимое view |

Возвращает `View`: этот виджет для fluent-настройки.

</div>

