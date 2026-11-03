---
title: "XmlCodeEditor"
slug: "xml-code-editor"
category: "Widgets/Interaction"
locale: "ru"
order: 270
---

# XmlCodeEditor

Специализированный редактор XML/XAML разметки интерфейсов с валидацией схемы, форматированием и автодополнением тегов.

## Обзор

`XmlCodeEditor` расширяет `CodeEditor` и предварительно настроен для файлов разметки интерфейсов UniGUI. Включает встроенный XML-парсер, автоформатирование отступов, автодополнение тегов из реестра виджетов и подсветку синтаксических ошибок.

## Редактор разметки XML

```java
XmlCodeEditor xmlEditor = new XmlCodeEditor()
        .registry(widgetRegistry)
        .formatXml();
```

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">XmlCodeEditor::</span>`formatXml()` {: #formatxml data-toc-label="formatXml()" }

**Форматировать XML**

Форматирует XML-код с нормализацией отступов и выравниванием атрибутов.

Возвращает `boolean`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">XmlCodeEditor::</span>`registry(XmlWidgetRegistry)` {: #registryxmlwidgetregistry data-toc-label="registry(XmlWidgetRegistry)" }

**Задать реестр виджетов**

Устанавливает реестр виджетов для автодополнения тегов и валидации атрибутов.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `registry` | `XmlWidgetRegistry` | — |

Возвращает `XmlCodeEditor`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">XmlCodeEditor::</span>`validateXml()` {: #validatexml data-toc-label="validateXml()" }

**Валидация XML**

Выполняет синхронную проверку синтаксиса и схемы XML.

Возвращает `boolean`.

</div>

