package dev.sixik.sdmshop2.libs.shop.base;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import dev.sixik.sdmshop2.libs.shop.base.callbacks.ShopEntityCallbacks;
import dev.sixik.sdmshop2.libs.shop.components.api.ShopComponent;
import dev.sixik.sdmshop2.libs.shop.components.api.ShopComponentRegistry;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectLists;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import lombok.Getter;
import net.minecraft.network.FriendlyByteBuf;

import java.util.*;

/**
 * Базовая сущность в архитектуре Entity-Component System (ECS) магазина.
 * Выступает в роли контейнера для компонентов ({@link ShopComponent}), которые
 * определяют логику и свойства объекта. Управляет инициализацией, хранением,
 * кэшированием и сериализацией компонентов.
 */
public class ShopEntity implements ShopEntityCallbackSupport {

    private enum InitializationSide {
        CLIENT,
        SERVER
    }

    private boolean initialized = false;

    private final ObjectArrayList<ShopComponent> components = new ObjectArrayList<>();
    private final Map<Class<?>, ObjectList<ShopComponent>> componentCache = new Reference2ObjectOpenHashMap<>();

    @Getter
    private ObjectArrayList<ShopEntityCallbacks.OnAddComponent> addComponentListeners;
    @Getter
    private ObjectArrayList<ShopEntityCallbacks.OnRemoveComponent> removeComponentListeners;
    @Getter
    private ObjectArrayList<ShopEntityCallbacks.OnUpdate> updateListeners;
    @Getter
    private ObjectArrayList<ShopEntityCallbacks.OnComponentUpdate> updateComponentListeners;

    public ShopEntity() {
    }

    /**
     * Инициализирует все текущие компоненты сущности.
     * Вызывает метод {@link ShopComponent#init()} для каждого компонента.
     */
    private void initComponents() {
        final ShopComponent[] snapshot = components.toArray(new ShopComponent[0]);
        for (ShopComponent component : snapshot) {
            if (component.getRoot() == this) {
                component.init();
            }
        }
        initialized = true;
    }

    /**
     * Добавляет новый компонент к сущности с учетом его приоритета выполнения.
     * Если сущность уже инициализирована, автоматически вызывает метод init() у компонента.
     * Сбрасывает кэш компонентов.
     *
     * @param component Добавляемый компонент
     * @param <T>       Тип компонента
     * @return Добавленный компонент для цепочечных вызовов
     */
    public final <T extends ShopComponent> T addComponent(T component) {
        component.setRoot(this);

        final Object[] primitiveArray = components.elements();
        final int size = components.size();

        int i = 0;
        while (i < size && ((ShopComponent) primitiveArray[i]).priority() <= component.priority()) {
            i++;
        }

        components.add(i, component);
        componentCache.clear();

        if (initialized)
            component.init();

        invokeAddComponent(this, component);
        return component;
    }

    /**
     * Удаляет конкретный экземпляр компонента из сущности.
     * Очищает кэш компонентов и вызывает события удаления.
     *
     * @param component Компонент, который нужно удалить
     * @return true, если компонент был найден и удален, иначе false
     */
    public final boolean removeComponent(ShopComponent component) {
        if (components.remove(component)) {
            componentCache.clear();
            component.setRoot(null);

            onRemoveComponent(component);
            invokeRemoveComponent(this, component);
            return true;
        }
        return false;
    }

    /**
     * Удаляет все компоненты указанного типа.
     * Очищает кэш компонентов и вызывает события удаления для каждого удаленного компонента.
     *
     * @param type Класс компонентов, которые нужно удалить
     * @return true, если хотя бы один компонент был удален, иначе false
     */
    public final boolean removeComponent(Class<?> type) {
        boolean removed = false;
        final Object[] array = components.elements();

        for (int i = components.size() - 1; i >= 0; i--) {
            ShopComponent c = (ShopComponent) array[i];
            if (type.isInstance(c)) {
                components.remove(i);
                componentCache.clear();
                c.setRoot(null);

                invokeRemoveComponent(this, c);
                removed = true;
            }
        }
        return removed;
    }

    /**
     * Возвращает индекс компонента в текущем порядке сущности.
     *
     * <p>Индекс соответствует порядку в {@link #getComponents()} и порядку сериализации.
     * Если компонент не принадлежит этой сущности, возвращается {@code -1}.</p>
     *
     * @param component компонент, индекс которого нужно найти
     * @return индекс компонента или {@code -1}, если компонент не найден
     */
    public final int indexOfComponent(ShopComponent component) {
        return component == null ? -1 : components.indexOf(component);
    }

    /**
     * Перемещает компонент на указанное смещение относительно его текущей позиции.
     *
     * <p>Например, {@code offset = -1} поднимает компонент на одну позицию вверх,
     * а {@code offset = 1} опускает на одну позицию вниз. Индекс автоматически
     * ограничивается границами списка.</p>
     *
     * @param component компонент, который нужно переместить
     * @param offset смещение относительно текущего индекса
     * @return {@code true}, если порядок компонентов изменился
     */
    public final boolean moveComponent(ShopComponent component, int offset) {
        int currentIndex = indexOfComponent(component);
        if (currentIndex < 0 || offset == 0) {
            return false;
        }

        return moveComponentToIndex(component, currentIndex + offset);
    }

    /**
     * Перемещает компонент на конкретный индекс.
     *
     * <p>Метод нужен для редакторов, где пользователь может перетащить компонент
     * в произвольную позицию. В отличие от {@link #addComponent(ShopComponent)},
     * этот метод не сортирует компонент по {@link ShopComponent#priority()}:
     * порядок задаётся явно пользователем.</p>
     *
     * <p>После успешного перемещения очищается cache компонентов и вызывается
     * событие обновления компонента/сущности. Root у компонента не меняется.</p>
     *
     * @param component компонент, который нужно переместить
     * @param targetIndex желаемый индекс; значения вне диапазона будут зажаты в границы списка
     * @return {@code true}, если порядок компонентов изменился
     */
    public final boolean moveComponentToIndex(ShopComponent component, int targetIndex) {
        int currentIndex = indexOfComponent(component);
        if (currentIndex < 0 || components.size() <= 1) {
            return false;
        }

        int clampedIndex = clampComponentIndex(targetIndex);
        if (currentIndex == clampedIndex) {
            return false;
        }

        components.remove(currentIndex);
        components.add(clampedIndex, component);
        componentCache.clear();
        invokeUpdateComponent(this, component);
        return true;
    }

    /**
     * Перемещает компонент на место другого компонента.
     *
     * <p>Целевой компонент сдвигается вправо/вниз, а перемещаемый компонент становится
     * на его индекс. Для вставки после целевого компонента используйте
     * {@link #moveComponentAfter(ShopComponent, ShopComponent)}.</p>
     *
     * @param component компонент, который нужно переместить
     * @param target компонент, на место которого нужно вставить {@code component}
     * @return {@code true}, если порядок компонентов изменился
     */
    public final boolean moveComponentToComponent(ShopComponent component, ShopComponent target) {
        int targetIndex = indexOfComponent(target);
        if (targetIndex < 0 || component == target) {
            return false;
        }

        return moveComponentToIndex(component, targetIndex);
    }

    /**
     * Перемещает компонент перед указанным целевым компонентом.
     *
     * @param component компонент, который нужно переместить
     * @param target компонент, перед которым нужно вставить {@code component}
     * @return {@code true}, если порядок компонентов изменился
     */
    public final boolean moveComponentBefore(ShopComponent component, ShopComponent target) {
        return moveComponentToComponent(component, target);
    }

    /**
     * Перемещает компонент после указанного целевого компонента.
     *
     * @param component компонент, который нужно переместить
     * @param target компонент, после которого нужно вставить {@code component}
     * @return {@code true}, если порядок компонентов изменился
     */
    public final boolean moveComponentAfter(ShopComponent component, ShopComponent target) {
        int targetIndex = indexOfComponent(target);
        if (targetIndex < 0 || component == target) {
            return false;
        }

        return moveComponentToIndex(component, targetIndex + 1);
    }

    /**
     * Меняет два компонента местами.
     *
     * <p>Это удобно для UI-кнопок вида "поменять с соседним" или drag-and-drop,
     * когда нужно именно swap, а не вставка с последующим сдвигом остальных элементов.</p>
     *
     * @param first первый компонент
     * @param second второй компонент
     * @return {@code true}, если компоненты найдены и порядок изменился
     */
    public final boolean swapComponents(ShopComponent first, ShopComponent second) {
        int firstIndex = indexOfComponent(first);
        int secondIndex = indexOfComponent(second);
        if (firstIndex < 0 || secondIndex < 0 || firstIndex == secondIndex) {
            return false;
        }

        components.set(firstIndex, second);
        components.set(secondIndex, first);
        componentCache.clear();
        invokeUpdateComponent(this, first);
        invokeUpdateComponent(this, second);
        return true;
    }

    private int clampComponentIndex(int index) {
        if (components.isEmpty()) {
            return 0;
        }

        return Math.max(0, Math.min(index, components.size() - 1));
    }

    /**
     * Проверяет наличие компонента указанного типа у сущности.
     *
     * @param type класс искомого типа компонента
     * @return {@code true}, если компонент найден
     */
    public final boolean hasComponent(Class<?> type) {
        return !getComponents(type).isEmpty();
    }

    /**
     * Возвращает неизменяемый список всех компонентов сущности.
     *
     * @return Список всех компонентов
     */
    public final ObjectList<ShopComponent> getComponents() {
        return ObjectLists.unmodifiable(components);
    }

    /**
     * Возвращает неизменяемый список всех компонентов указанного типа.
     * Использует ленивое кэширование: фильтрация происходит только при первом запросе
     * данного класса, после чего результат сохраняется в {@code componentCache}.
     *
     * @param type Класс искомого типа компонентов
     * @param <T>  Тип компонентов
     * @return Список компонентов, соответствующих указанному типу (или пустой список)
     */
    @SuppressWarnings("unchecked")
    public final <T> ObjectList<T> getComponents(Class<T> type) {
        /*
            Ленивое кэширование. Фильтруем только при первом запросе конкретного типа.
         */
        return (ObjectList<T>) componentCache.computeIfAbsent(type, k -> {
            final ObjectArrayList<ShopComponent> filtered = new ObjectArrayList<>();
            final Object[] primitiveComponents = components.elements();
            for (int i = 0; i < components.size(); i++) {
                ShopComponent c = (ShopComponent) primitiveComponents[i];
                if (k.isInstance(c)) {
                    filtered.add(c);
                }
            }

            /*
                Используем emptyList() для экономии памяти, если компонентов нет
             */
            return filtered.isEmpty() ? ObjectLists.emptyList() : ObjectLists.unmodifiable(filtered);
        });
    }

    /**
     * Возвращает первый найденный компонент указанного типа, обернутый в {@link Optional}.
     *
     * @param type Класс искомого типа компонента
     * @param <T>  Тип компонента
     * @return Optional с компонентом, если он найден, иначе Optional.empty()
     */
    public final <T> Optional<T> getComponent(Class<T> type) {
        List<T> list = getComponents(type);
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    /**
     * Сериализует все компоненты сущности в {@link JsonArray}.
     * Использует {@link ShopComponentRegistry#toJson(ShopComponent)} для каждого компонента.
     *
     * @return Массив JSON с данными компонентов
     */
    public final JsonArray serializeComponents() {
        JsonArray compArray = new JsonArray();

        final Object[] refArray = components.elements();
        final int size = components.size();

        for (int i = 0; i < size; i++) {
            compArray.add(ShopComponentRegistry.toJson((ShopComponent) refArray[i]));
        }

        return compArray;
    }

    /**
     * Десериализует компоненты из {@link JsonElement}.
     *
     * @param element Элемент JSON (ожидается JsonArray)
     */
    public final void deserializeComponents(JsonElement element) {
        deserializeComponents(element.getAsJsonArray());
    }

    /**
     * Десериализует компоненты из {@link JsonArray}.
     * Очищает текущие компоненты и кэш, затем загружает новые.
     * После десериализации вызывает {@link #initializeServerOnlyComponents()}.
     *
     * @param array Массив JSON с данными компонентов
     */
    public final void deserializeComponents(JsonArray array) {
        components.clear();
        componentCache.clear();
        initialized = false;

        for (JsonElement compJson : array) {
            final ShopComponent component = ShopComponentRegistry.fromJson(compJson.getAsJsonObject());
            component.setRoot(this);
            components.add(component);
        }

        initializeServerOnlyComponents();
    }

    /**
     * Сериализует сущность. По умолчанию сериализует только компоненты.
     *
     * @return Элемент JSON с данными сущности
     */
    public JsonElement serialize() {
        return serializeComponents();
    }

    /**
     * Десериализует сущность. По умолчанию десериализует только компоненты.
     *
     * @param element Элемент JSON с данными сущности
     */
    public void deserialize(JsonElement element) {
        deserializeComponents(element);
    }

    /**
     * Сериализует компоненты для передачи по сети.
     * Отправляет только те компоненты, для которых {@link ShopComponent#shouldSync()} возвращает true.
     *
     * @param buf Буфер сетевого пакета
     */
    public final void serializeComponentsNetwork(FriendlyByteBuf buf) {
        final ObjectArrayList<ShopComponent> syncList = new ObjectArrayList<>();
        final Object[] comps = components.elements();
        final int compSize = components.size();
        for (int i = 0; i < compSize; i++) {
            final ShopComponent component = (ShopComponent) comps[i];
            if (component.shouldSync()) {
                syncList.add(component);
            }
        }

        final Object[] primitiveSyncList = syncList.elements();
        final int syncSize = syncList.size();
        buf.writeVarInt(syncSize);
        for (int i = 0; i < syncSize; i++) {
            ShopComponentRegistry.toNetwork(buf, (ShopComponent) primitiveSyncList[i]);
        }
    }

    /**
     * Десериализует компоненты из сетевого буфера.
     * Очищает текущие компоненты и кэш, затем считывает новые.
     * После десериализации вызывает {@link #initializeClientOnlyComponents()}.
     *
     * @param buf Буфер сетевого пакета
     */
    public final void deserializeComponentsNetwork(FriendlyByteBuf buf) {
        int count = buf.readVarInt();

        components.clear();
        componentCache.clear();
        initialized = false;
        for (int i = 0; i < count; i++) {
            final ShopComponent component = ShopComponentRegistry.fromNetwork(buf);
            component.setRoot(this);
            components.add(component);
        }

        initializeClientOnlyComponents();
    }

    /**
     * Сериализует сущность для передачи по сети.
     * По умолчанию сериализует только компоненты.
     *
     * @param buf Буфер сетевого пакета
     */
    public void serializeNetwork(FriendlyByteBuf buf) {
        serializeComponentsNetwork(buf);
    }

    /**
     * Десериализует сущность из сетевого буфера.
     * По умолчанию десериализует только компоненты.
     *
     * @param buf Буфер сетевого пакета
     */
    public void deserializeNetwork(FriendlyByteBuf buf) {
        deserializeComponentsNetwork(buf);
    }

    /**
     * Выполняет клиентскую инициализацию компонентов.
     * Сначала добавляет общие компоненты через {@link #customInitializeCommonComponents()},
     * затем клиентские через {@link #customInitializeClientOnlyComponents()} и вызывает {@link #initComponents()}.
     */
    protected final void initializeClientOnlyComponents() {
        initializeComponents(InitializationSide.CLIENT);
    }

    /**
     * Метод для переопределения в наследниках.
     * Используется для добавления компонентов, которые должны быть только на стороне клиента.
     */
    protected void customInitializeClientOnlyComponents() {
    }

    /**
     * Выполняет серверную инициализацию компонентов.
     * Сначала добавляет общие компоненты через {@link #customInitializeCommonComponents()},
     * затем серверные через {@link #customInitializeServerOnlyComponents()} и вызывает {@link #initComponents()}.
     */
    public final void initializeServerOnlyComponents() {
        initializeComponents(InitializationSide.SERVER);
    }

    private void initializeComponents(InitializationSide side) {
        initialized = false;
        customInitializeCommonComponents();

        if (side == InitializationSide.CLIENT) {
            customInitializeClientOnlyComponents();
        } else {
            customInitializeServerOnlyComponents();
        }

        initComponents();
    }

    /**
     * Хук для компонентов, которые должны существовать и на клиенте, и на сервере.
     *
     * <p>Используйте его для системных/default-компонентов сущности. Метод должен быть идемпотентным:
     * перед добавлением компонента проверяйте {@link #hasComponent(Class)}, потому что инициализация может
     * запускаться после десериализации или пересборки объекта.</p>
     */
    protected void customInitializeCommonComponents() {
    }

    /**
     * Метод для переопределения в наследниках.
     * Используется для добавления компонентов, которые должны быть только на стороне сервера.
     */
    protected void customInitializeServerOnlyComponents() {
    }

    /**
     * Хук для переопределения в наследниках.
     * Вызывается при успешном добавлении компонента.
     *
     * @param component Добавленный компонент
     */
    protected void onAddComponent(ShopComponent component) {
    }

    /**
     * Хук для переопределения в наследниках.
     * Вызывается при успешном удалении компонента.
     *
     * @param component Удаленный компонент
     */
    protected void onRemoveComponent(ShopComponent component) {
    }

    /**
     * Хук для переопределения в наследниках.
     * Вызывается при обновлении компонента.
     *
     * @param component Компонент который обновился
     */
    protected void onUpdateComponent(ShopComponent component) {
    }

    /**
     * Хук для переопределения в наследниках.
     * Вызывается при обновлении {@link ShopEntity}
     */
    protected void onUpdate() {
    }

    @Override
    public void subscribeAddComponent(ShopEntityCallbacks.OnAddComponent callback) {
        if (addComponentListeners == null) {
            addComponentListeners = new ObjectArrayList<>();
        }
        addComponentListeners.add(callback);
    }

    @Override
    public void subscribeRemoveComponent(ShopEntityCallbacks.OnRemoveComponent callback) {
        if (removeComponentListeners == null) {
            removeComponentListeners = new ObjectArrayList<>();
        }
        removeComponentListeners.add(callback);
    }

    @Override
    public void subscribeUpdateComponent(ShopEntityCallbacks.OnComponentUpdate callback) {
        if (updateComponentListeners == null) {
            updateComponentListeners = new ObjectArrayList<>();
        }
        updateComponentListeners.add(callback);
    }

    @Override
    public void subscribeUpdate(ShopEntityCallbacks.OnUpdate callback) {
        if (updateListeners == null) {
            updateListeners = new ObjectArrayList<>();
        }
        updateListeners.add(callback);
    }

    @Override
    public void unsubscribeAddComponent(ShopEntityCallbacks.OnAddComponent callback) {
        if (addComponentListeners == null) return;
        addComponentListeners.remove(callback);
        if (addComponentListeners.isEmpty()) {
            addComponentListeners = null;
        }
    }

    @Override
    public void unsubscribeRemoveComponent(ShopEntityCallbacks.OnRemoveComponent callback) {
        if (removeComponentListeners == null) return;
        removeComponentListeners.remove(callback);
        if (removeComponentListeners.isEmpty()) {
            removeComponentListeners = null;
        }
    }

    @Override
    public void unsubscribeUpdateComponent(ShopEntityCallbacks.OnComponentUpdate callback) {
        if (updateComponentListeners == null) return;
        updateComponentListeners.remove(callback);
        if (updateComponentListeners.isEmpty()) {
            updateComponentListeners = null;
        }
    }

    @Override
    public void unsubscribeUpdate(ShopEntityCallbacks.OnUpdate callback) {
        if (updateListeners == null) return;
        updateListeners.remove(callback);
        if (updateListeners.isEmpty()) {
            updateListeners = null;
        }
    }

    @Override
    public void invokeUpdateComponent(ShopEntity entity, ShopComponent component) {
        onUpdateComponent(component);
        ShopEntityCallbackSupport.super.invokeUpdateComponent(entity, component);
        invokeUpdate(entity);
    }

    @Override
    public void invokeAddComponent(ShopEntity entity, ShopComponent component) {
        onAddComponent(component);
        ShopEntityCallbackSupport.super.invokeAddComponent(entity, component);
        invokeUpdate(entity);
    }

    @Override
    public void invokeRemoveComponent(ShopEntity entity, ShopComponent component) {
        onRemoveComponent(component);
        ShopEntityCallbackSupport.super.invokeRemoveComponent(entity, component);
        invokeUpdate(entity);
    }
}
