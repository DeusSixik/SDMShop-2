---
title: "GraphExecutor"
slug: "graph-executor"
category: "Core/Виджеты/Графы/Исполнение"
locale: "ru"
order: 0
---

# GraphExecutor

Запускает модель графа без виджетов, переходя по потоковым связям и вычисляя входы по требованию.

## Обзор

Зарегистрируйте `NodeBehavior` для каждого исполняемого типа узла, затем вызовите `fire(model, startNodeId)` на узле-событии. Чистые поведения вычисляют значения по запросу; исполняемые идут по потоковым связям и могут продолжаться по одному или нескольким выходам. Если граф редактируется пользователем, сначала выполните проверку. Настраиваемый лимит шагов останавливает бесконечный потоковый цикл.

## Регистрация поведений и запуск графа

```java
GraphExecutor executor = new GraphExecutor()
        .register("event.start", NodeBehavior.exec(context -> context.continueWith("next")))
        .register("value.add", NodeBehavior.pure(context ->
                context.output("sum", context.number("left") + context.number("right"))));
GraphResult result = executor.fire(graph.model(), "start");
```

