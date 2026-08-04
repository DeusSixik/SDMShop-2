package dev.sixik.sdmshop2.libs.shop.components.api;

import com.lowdragmc.lowdraglib.gui.widget.Widget;
import dev.sixik.sdmshop2.SDMShop2;
import dev.sixik.sdmshop2.libs.shop.base.ShopEntity;
import dev.sixik.sdmshop2.libs.shop.serializer.ComponentSerializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

/**
 * Базовый класс для всех компонентов магазина.
 * Компоненты определяют поведение и данные сущностей магазина ({@link ShopEntity}).
 */
public abstract class ShopComponent {

    /**
     * Константа для обозначения пустого или неопределенного типа компонента.
     */
    public static ResourceLocation EMPTY = ResourceLocation.tryBuild("sdm", "null");
    private static final ComponentSerializer<ShopComponent> EMPTY_ADDITIONAL_SERIALIZER = ComponentSerializer.create();

    private ShopEntity root;
    private volatile boolean dirty;

    /**
     * Вызывается при инициализации компонента.
     * Используется для настройки начального состояния или связей.
     */
    public void init() { }

    /**
     * Определяет приоритет инициализации и обработки компонента.
     * Компоненты с меньшим значением приоритета обрабатываются раньше.
     *
     * @return Приоритет (по умолчанию 0)
     */
    public int priority() {
        return 0;
    }

    /**
     * Возвращает тип компонента, который содержит логику сериализации.
     *
     * @return Объект типа компонента
     */
    public abstract IComponentType<?> getType();

    /**
     * Возвращает категорию компонента для сортировоки и поиска
     */
    public ShopComponentCategory getCategory() {
        return ShopComponentCategory.MISC;
    }

    /**
     * Возвращает корневую сущность, которой принадлежит данный компонент.
     *
     * @return Корневая сущность
     */
    public final ShopEntity getRoot() {
        return root;
    }

    /**
     * Возвращает корневую сущность, приведенную к указанному типу.
     *
     * @param <T> Тип сущности
     * @return Корневая сущность
     */
    public final <T extends ShopEntity> T getRoots() {
        return (T) root;
    }

    /**
     * Устанавливает корневую сущность для компонента.
     * Может быть установлена только один раз.
     *
     * @param entity Корневая сущность
     */
    public final void setRoot(@Nullable ShopEntity entity) {
        if (entity == null) {
            this.root = null;
            return;
        }

        if (root != null && root != entity) {
            IComponentType<?> type = getType();
            String componentId = type == null ? getClass().getName() : String.valueOf(type.getId());
            throw new IllegalStateException("Component " + componentId + " is already attached to another ShopEntity");
        }

        this.root = entity;
    }

    /**
     * Определяет, должен ли компонент синхронизироваться с клиентом по сети.
     *
     * @return true, если компонент должен быть отправлен на клиент, иначе false
     */
    public boolean shouldSync() {
        return true;
    }

    public final boolean isDirty() {
        return dirty;
    }

    public final void markDirty() {
        dirty = true;
    }

    public final void clearDirty() {
        dirty = false;
    }

    public final boolean consumeDirty() {
        boolean wasDirty = dirty;
        dirty = false;
        return wasDirty;
    }

    /**
     * Проверяет, является ли переданный идентификатор идентификатором пустого компонента.
     *
     * @param id Идентификатор типа компонента
     * @return true, если это EMPTY, иначе false
     */
    public static boolean isEmpty(ResourceLocation id) {
        return EMPTY.equals(id);
    }

    /**
     * Оповещает родителя что компонент был изменён и нужно обновить данные
     */
    public final void invokeUpdate() {
        markDirty();
        ShopEntity root = getRoot();
        if(root == null) return;
        root.invokeUpdateComponent(root, this);
    }

    public ComponentSerializer<? extends ShopComponent> additionalSerializer() {
        return EMPTY_ADDITIONAL_SERIALIZER;
    }

    @Nullable
    @Environment(EnvType.CLIENT)
    public Widget createRender() {
        SDMShop2.LOGGER.error("Can't create render because {} didn't have implementation of method 'createRender'", getType().getId());
        return null;
    }
}
