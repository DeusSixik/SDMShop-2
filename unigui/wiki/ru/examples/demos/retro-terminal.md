---
title: "Ретро-терминал"
slug: "retro-terminal"
category: "Examples/Demos"
locale: "ru"
order: 10
---

# Ретро-терминал

Полноценный терминал в стиле CRT, собранный из виджетов UniGUI, собственного [AdminConsole](<../../widgets/interaction/admin-console.md>) и цепочки post-processing эффектов.

![Иконка ретро-терминала](<../../../assets/demos/retro-terminal/00-screen.png>)

## Что мы собираем

В этом guide мы собираем `RetroTerminalScreen` как переносимый `UniScreen`. Разберём фабрику экрана, полноразмерное дерево виджетов, настройку `AdminConsole`, типизированную регистрацию команд и CRT-цепочку шейдеров.

Для воспроизводимых кадров в `TestMod` нажмите **Retro Wiki** в title или pause screen. Используйте `Ctrl+Alt+F1` для базового экрана, `Ctrl+Alt+F2` для слоистого root, `Ctrl+Alt+F3` для консоли, `Ctrl+Alt+F4` для вывода команд, `Ctrl+Alt+F5` для автодополнения и `Ctrl+Alt+F6` для effects preset.

## 1. Создайте экран

Начните с `DefaultUIContext` и `UnityLikeUIScaleProvider`. Провайдер масштаба сохраняет читаемость терминала при разных размерах окна. Возвращайте `UniScreen`, а не открывайте его внутри фабрики: так построение можно тестировать, а host сам решает, когда вызвать `openGui()`.

![Готовый экран ретро-терминала](<../../../assets/demos/retro-terminal/01-screen.png>)

## 2. Постройте дерево виджетов

Используйте полноразмерный `StackPanel`, затем добавьте фон, рамку-vignette и консоль отдельными слоями. Корнем, который возвращает экран, будет `OverlayLayer`. Layout задаётся процентными размерами, padding и flex-правилами; координаты Minecraft не нужны.

![Слоистый корень терминала](<../../../assets/demos/retro-terminal/02-layered-root.png>)

## 3. Настройте [AdminConsole](<../../widgets/interaction/admin-console.md>)

Наследуйтесь от `AdminConsole`, когда стандартной консоли недостаточно для визуального стиля. Переопределите hooks настройки root, header, prompt, input field, output labels, completion popup и scroll view. Жизненный цикл оставьте базовому классу, меняя только внешний вид и содержимое.

![Настроенная консоль](<../../../assets/demos/retro-terminal/03-console.png>)

## 4. Зарегистрируйте команды

Простые команды регистрируются через `registerCommand`, а для типизированных аргументов и подсказок используйте `AdminCommandRegistry.command(...)`. В примере есть `status`, `scan`, `players` и `teleport`; callback'и добавляют строки через API консоли и не обращаются напрямую к renderer.

![Вывод команд терминала](<../../../assets/demos/retro-terminal/02-commands.png>)

![Popup автодополнения команды](<../../../assets/demos/retro-terminal/03-autocomplete.png>)

## 5. Добавьте CRT-эффект

Оберните корень виджетов в `PostProcessingLayer` и назначьте `UiPostEffectChain` из проходов terminal, bloom и frame. Uniform-параметры задаются при создании экрана; дерево UI остаётся независимым от backend, который исполняет проходы. Backend выбранной платформы должен поддерживать post-processing, а host-проект — включать три shader-ресурса, указанные в passes.

![CRT post-processing эффект](<../../../assets/demos/retro-terminal/04-effects.png>)

## Фабрика экрана

```java
public static void open() {
    createScreen().openGui();
}

public static UniScreen createScreen() {
    DefaultUIContext context = new DefaultUIContext();
    UnityLikeUIScaleProvider scaleProvider = new UnityLikeUIScaleProvider()
            .referenceResolution(1920.0f, 1080.0f)
            .matchBalanced()
            .scaleRange(0.75f, 12.0f)
            .userScale(3.0f);
    context.scaleProvider(scaleProvider);

    RetroConsole console = new RetroConsole();
    PostProcessingLayer processedRoot = new PostProcessingLayer();
    processedRoot.layout(style -> style.sizePercent(100.0f, 100.0f));
    processedRoot.postEffect(retroEffect(scaleProvider));
    processedRoot.addChild(root(console));
    UniScreen screen = new UniScreen("Retro Terminal", processedRoot, context)
            .scaleWithGameGui(false)
            .renderPolicy(UiRenderPolicy.continuous());
    console.onCloseRequested(event -> screen.closeGui());
    return screen;
}
```

## Слоистый корень

```java
private static Widget root(RetroConsole console) {
    StackPanel stack = new StackPanel();
    stack.layout(style -> style.sizePercent(100.0f, 100.0f).overflow(Overflow.HIDDEN));

    Box background = new Box()
            .themeEnabled(false)
            .backgroundVisible(true)
            .borderVisible(false)
            .background(MutableColor.rgba(0.004f, 0.017f, 0.006f, 1.0f));
    background.layout(style -> style.sizePercent(100.0f, 100.0f)
            .align(Alignment.STRETCH, Alignment.STRETCH));

    Box vignette = new Box()
            .themeEnabled(false)
            .backgroundVisible(true)
            .borderVisible(true)
            .radius(0.0f)
            .background(MutableColor.rgba(0.020f, 0.055f, 0.020f, 0.42f))
            .border(MutableColor.rgba(0.52f, 0.72f, 0.38f, 0.35f));
    vignette.layout(style -> style.sizePercent(100.0f, 100.0f));

    stack.addChild(background);
    stack.addChild(vignette);
    stack.addChild(console);
    return new OverlayLayer(stack);
}
```

## Настройка консоли

```java
private static final class RetroConsole extends AdminConsole {
    @Override
    protected void configureInputField(ConsoleInputField inputField) {
        inputField.placeholder("enter command...");
        inputField.textColor().set(GREEN);
        inputField.borderColor().set(BORDER);
        inputField.layout(style -> style.height(30.0f)
                .flexGrow(1.0f).flexShrink(1.0f));
    }
}
```

## Типизированная команда с подсказками

```java
registerCommand(AdminCommandRegistry.command("teleport", "Teleport player to coordinates")
        .argument("player", "Known player", () -> SAMPLE_PLAYERS)
        .customArgument("x", "X coordinate", COORDINATES)
        .customArgument("y", "Y coordinate", COORDINATES)
        .customArgument("z", "Z coordinate", COORDINATES)
        .executor((console, call) -> {
            List<String> args = call.arguments();
            if (args.size() < 4) {
                console.appendError("Usage: teleport <player> <x> <y> <z>");
                return;
            }
            console.appendInfo("Teleport queued: " + args.get(0) + " -> "
                    + args.get(1) + " " + args.get(2) + " " + args.get(3));
        })
        .build());
```

## CRT post-processing цепочка

```java
private static UiPostEffectChain retroEffect(UIScaleProvider scaleProvider) {
    UiPostEffectPass terminal = UiPostEffectPass.shader("unigui_testmod:retro_terminal")
            .uniforms(uniforms -> uniforms
                    .vec3("fontColor", 0.78f, 1.00f, 0.43f)
                    .floatValue("scanlineStrength", 0.78f)
                    .floatSupplier("UiScale", () -> UIScaleProvider.sanitize(
                            scaleProvider == null ? 1.0f : scaleProvider.scale())));
    UiPostEffectPass bloom = UiPostEffectPass.shader("unigui_testmod:retro_terminal_bloom");
    UiPostEffectPass frame = UiPostEffectPass.shader("unigui_testmod:retro_terminal_frame");
    return UiPostEffectChain.of(List.of(terminal, bloom, frame));
}
```

