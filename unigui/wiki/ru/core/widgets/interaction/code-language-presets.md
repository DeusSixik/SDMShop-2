---
title: "CodeLanguagePresets"
slug: "code-language-presets"
category: "Core/Виджеты/Взаимодействие"
locale: "ru"
order: 290
---

# CodeLanguagePresets

Фабричный реестр, поставляющий токенизаторы и валидаторы для языковых пресетов.

## Обзор

`CodeLanguagePresets` содержит статические методы для применения готовых токенизаторов и правил валидации к экземпляру `CodeEditor`.

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">CodeLanguagePresets::</span>`apply(CodeEditor, CodeLanguagePreset)` {: #applycodeeditor-codelanguagepreset data-toc-label="apply(CodeEditor, CodeLanguagePreset)" }

**Применить пресет**

Применяет токенизатор, валидатор и стили выбранного пресета к редактору.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `editor` | `CodeEditor` | — |
| `preset` | `CodeLanguagePreset` | — |

</div>

