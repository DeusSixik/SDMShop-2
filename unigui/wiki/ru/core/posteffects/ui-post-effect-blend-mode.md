---
title: "UiPostEffectBlendMode"
slug: "ui-post-effect-blend-mode"
category: "Core/PostEffects"
locale: "ru"
order: 70
---

# UiPostEffectBlendMode

Режим наложения и смешивания для шейдерного прохода пост-эффекта.

## Обзор

`UiPostEffectBlendMode` указывает бэкенду режим наложения результата шейдерного прохода в целевой буфер:

- `REPLACE`: Прямая перезапись пикселей целевого буфера без прозрачности. Стандартный режим для промежуточных проходов.
- `ALPHA_BLEND`: Стандартное смешивание с учётом альфа-канала (`SRC_ALPHA, ONE_MINUS_SRC_ALPHA`).
- `ADDITIVE`: Аддитивное смешивание для эффектов свечения, лучей и бликов.

## Выбор режима смешивания

```java
UiPostEffectBlendMode mode = UiPostEffectBlendMode.REPLACE;
boolean blend = mode.blendEnabled();
```

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiPostEffectBlendMode::</span>`blendEnabled()` {: #blendenabled data-toc-label="blendEnabled()" }

**blendEnabled()**

Возвращает true, если бэкенд должен включить альфа-смешивание.

Возвращает `boolean`: True, если смешивание активно..

</div>

