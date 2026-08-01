# Архитектура Shop Edit Session

## Зачем это нужно

Shop Edit Session — это слой чернового редактирования магазина. Администратор меняет не live-магазин, а отдельную копию. После серии правок он запускает проверку и применяет изменения одним подтверждённым пакетом.

Это нужно, чтобы:

- не ломать текущий магазин для игроков во время настройки;
- делать пачку связанных изменений за раз;
- гарантировать порядок зависимостей, например сначала создать валюту, потом назначить её в MoneyCostComponent;
- валидировать весь draft перед применением;
- показывать предупреждения, если магазин изменён слишком сильно;
- в будущем добавить save draft, undo/redo, diff и историю изменений.

## Главная идея

Поток работы:

    Live Shop
      -> copy
    ShopEditSession / draftShop
      -> UI edits
    CommandLog
      -> local validation
    ValidationReport
      -> accept/send
    Server dry-run validation
      -> transaction apply
    Live Shop replacement / diff broadcast

Редактор всегда работает с draft-копией. Live-магазин меняется только после успешного применения batch на сервере.

## Основные сущности

### ShopEditSession

ShopEditSession — активная сессия редактирования.

Она должна хранить:

- sessionId — уникальный id сессии;
- baseRevision — версия магазина, от которой начали редактирование;
- author — администратор, который открыл сессию;
- draftShop — копия магазина;
- commands — список действий, сделанных в редакторе;
- createdAt и updatedAt;
- опционально name/description для сохранённых черновиков.

Примерная форма:

    public final class ShopEditSession {
        private final UUID sessionId;
        private final UUID baseRevision;
        private final UUID author;

        private ShopInstance draftShop;
        private final List<ShopEditCommand> commands;

        public ValidationReport validate() { ... }

        public ShopEditBatch buildBatch() { ... }
    }

UI должен получать именно draftShop или draft offer, а не live-объект.

### ShopEditCommand

ShopEditCommand — атомарное действие редактора.

Примеры команд:

- CreateCurrencyCommand;
- UpdateCurrencyCommand;
- RemoveCurrencyCommand;
- CreateCatalogCommand;
- SetCatalogCommand;
- CreateOfferCommand;
- AddComponentCommand;
- RemoveComponentCommand;
- UpdateComponentFieldCommand;
- UpdateItemRewardCommand.

Пример интерфейса:

    public interface ShopEditCommand {
        ResourceLocation type();

        void apply(ShopEditContext context);

        void validate(ShopEditContext context, ValidationReport report);
    }

Команда должна быть сериализуемой. Тогда её можно отправить на сервер, сохранить в draft-файл или переиграть позже.

### ShopEditContext

ShopEditContext — окружение, в котором команда применяется и валидируется.

Он может содержать:

- draftShop;
- draft currency registry;
- draft catalog registry;
- component registry;
- lookup по offer id;
- вспомогательные методы для поиска зависимостей.

Идея в том, чтобы команда не ходила напрямую в глобальные live-объекты.

### ValidationReport

ValidationReport — результат проверки draft и command batch.

Он должен хранить:

- errors — блокируют Accept/Send;
- warnings — требуют подтверждения;
- info — просто показываются в summary;
- changedObjects — что было изменено;
- diffSummary — насколько сильно изменился магазин.

Пример уровней:

    public enum ValidationLevel {
        INFO,
        WARNING,
        ERROR
    }

Примеры проверок:

- MoneyCostComponent.moneyId ссылается на несуществующую валюту;
- CatalogComponent ссылается на несуществующий каталог;
- offer не имеет reward-компонентов;
- offer не имеет cost-компонентов;
- удаляется валюта, которую используют товары;
- удаляется каталог, в котором есть товары;
- изменено слишком много offer-ов;
- удалено слишком много offer-ов;
- цена изменилась слишком сильно;
- есть дубликаты id;
- component field имеет невалидное значение.

## Порядок команд

Одна из главных причин делать command batch — порядок зависимостей.

Проблема:

    MoneyCostComponent.moneyId = emerald

Если offer загрузить раньше, чем валюта emerald будет создана, компонент окажется битым.

С command batch порядок можно сделать таким:

    1. CreateCurrencyCommand(emerald)
    2. CreateOfferCommand(...)
    3. AddComponentCommand(MoneyCostComponent.moneyId = emerald)

То же самое полезно для:

- каталогов;
- групп;
- кастомных registry-объектов;
- скриптов;
- зависимостей между компонентами.

## Клиентский pipeline

### 1. Открытие редактора

Когда администратор открывает editor:

    currentShop -> deep copy -> draftShop -> ShopEditSession

Все виджеты получают draft.

### 2. Изменение в UI

Любое изменение должно:

1. примениться к draftShop;
2. добавить команду в session.commands;
3. обновить preview.

Например:

    Админ создаёт валюту emerald
      -> draft registry получает emerald
      -> commands.add(CreateCurrencyCommand(emerald))

Или:

    Админ выбирает moneyId = emerald
      -> draft offer обновляется
      -> commands.add(UpdateComponentFieldCommand(...))

### 3. Preview

Preview должен рендерить только draft-данные.

После изменения можно вызывать:

    offerElement.refresh();

Важно: preview не должен читать live-магазин.

### 4. Validate

Перед отправкой:

    ValidationReport report = session.validate();

Если есть ERROR — Accept/Send заблокирован.

Если есть WARNING — показывается confirm modal.

### 5. Accept / Send

После подтверждения клиент отправляет ShopEditBatch:

    public final class ShopEditBatch {
        private final UUID sessionId;
        private final UUID baseRevision;
        private final List<ShopEditCommand> commands;
    }

## Серверный pipeline

Сервер не должен применять команды напрямую к live-магазину.

Правильный порядок:

    1. Проверить baseRevision.
    2. Создать копию текущего live-магазина.
    3. Применить commands к копии.
    4. Запустить server-side validation.
    5. Если есть ошибки — вернуть failure.
    6. Если всё хорошо — заменить live-магазин или применить diff.
    7. Разослать клиентам обновление.

Пример:

    ShopInstance copy = currentShop.copy();

    for (ShopEditCommand command : batch.commands()) {
        command.apply(copyContext);
    }

    ValidationReport report = validator.validate(copy);
    if (report.hasErrors()) {
        return failure(report);
    }

    shopManager.replaceShop(copy);

Если команда посередине падает, live-магазин остаётся старым.

## Revision и конфликты

У магазина желательно иметь revision:

    UUID revision;
    long revisionNumber;

Когда администратор открывает editor, session запоминает baseRevision.

Если live-магазин успел измениться до Accept, сервер может вернуть конфликт:

    Draft was based on revision A,
    but current shop revision is B.

На первом этапе лучше просто отклонять такую отправку и просить обновить draft.

Позже можно добавить merge.

## Diff и предупреждения

Перед Accept полезно показывать summary:

    Будет изменено:
    - создано валют: 2
    - изменено товаров: 14
    - удалено товаров: 3
    - изменено цен: 9
    - удалено компонентов: 5

Предупреждения:

- удалено больше 20% товаров;
- изменено больше 50% цен;
- удаляется валюта, которую используют товары;
- offer стал пустым;
- изменились catalog ids;
- batch содержит структурные изменения.

Это не всегда ошибка, но администратор должен явно подтвердить.

## Save Draft

Сессию можно сохранять как черновик.

Варианты хранения:

- только commandLog;
- snapshot draftShop;
- baseRevision + commandLog + optional draftSnapshot.

Лучший вариант:

    baseRevision + commandLog + optional draftSnapshot

commandLog даёт историю действий, draftSnapshot ускоряет открытие большого черновика.

## Undo / Redo

Есть два варианта.

Простой вариант:

- хранить base snapshot;
- при undo пересобирать draft заново от base + commands без последней команды.

Более сложный вариант:

    public interface ReversibleShopEditCommand extends ShopEditCommand {
        ShopEditCommand inverse(ShopEditContext contextBeforeApply);
    }

На первом этапе лучше делать простой replay-подход.

## Интеграция с текущими компонентами

### MoneyCostComponent

Проблема:

    moneyId может ссылаться на валюту, которой ещё нет.

Решение:

    CreateCurrencyCommand
    UpdateMoneyCostComponentCommand

Validator проверяет, что moneyId существует в draft currency registry.

### CatalogComponent

Проблема:

    id и uuid должны устанавливаться вместе.

Решение:

UI не должен показывать uuid как редактируемое поле. Пользователь выбирает catalog entry, а команда сама ставит id и uuid.

Пример:

    SetCatalogCommand(offerId, catalogId)

Внутри:

    component.setId(catalog.id());
    component.setUuid(catalog.uuid());

### Item / ItemStack

Для item selector-ов можно использовать команды:

    SetItemRewardCommand(offerId, itemStack)
    SetItemCostCommand(offerId, itemStack)

Validator проверяет:

- item существует;
- NBT валидный;
- amount больше 0.

## Минимальный план внедрения

### Этап 1. Core session

- создать ShopEditSession;
- создать ShopEditContext;
- создать ShopEditCommand;
- создать ValidationReport;
- сделать deep copy магазина.

### Этап 2. Command batch

- добавить CreateOfferCommand;
- добавить AddComponentCommand;
- добавить RemoveComponentCommand;
- добавить UpdateComponentFieldCommand;
- добавить CreateCurrencyCommand;
- добавить SetCatalogCommand.

### Этап 3. UI draft mode

- editor открывает draftShop;
- preview работает от draft;
- Refresh вызывает ShopOfferElement.refresh();
- Accept собирает ShopEditBatch.

### Этап 4. Validation

- локальная проверка draft;
- server-side dry-run;
- warning modal перед отправкой.

### Этап 5. Server transaction

- сервер принимает batch;
- применяет команды к копии;
- валидирует копию;
- заменяет live-shop;
- отправляет diff/snapshot клиентам.

### Этап 6. Draft persistence

- сохранить черновик;
- открыть черновик;
- удалить черновик;
- показать список черновиков.

## Главное правило

Редактор не должен менять live-магазин напрямую.

Все изменения должны идти через цепочку:

    UI -> ShopEditSession -> Commands -> Validation -> Server Transaction -> Live Shop

Такой подход делает редактор безопасным, предсказуемым и расширяемым.

