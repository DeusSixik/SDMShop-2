package dev.sixik.sdmshop2.libs.shop.client.ui.editor;

import dev.sixik.sdmshop2.libs.sdmeconomy.ICurrency;
import dev.sixik.sdmshop2.libs.sdmeconomy.SDMEconomyServiceClient;
import dev.sixik.sdmshop2.libs.shop.base.ShopOffer;
import dev.sixik.sdmshop2.libs.shop.client.config.constructors.ComponentConfigAccess;
import dev.sixik.sdmshop2.libs.shop.client.ui.editor.unity.UnityIcons;
import dev.sixik.sdmshop2.libs.shop.client.ui.theme.ShopEditorStyles;
import dev.sixik.sdmshop2.libs.shop.components.api.IComponentType;
import dev.sixik.sdmshop2.libs.shop.components.api.ShopComponent;
import dev.sixik.sdmshop2.libs.shop.components.misc.CatalogComponent;
import dev.sixik.unigui.api.event.EventPhase;
import dev.sixik.unigui.api.event.PointerPressedEvent;
import dev.sixik.unigui.api.event.PointerReleasedEvent;
import dev.sixik.unigui.api.input.PointerButton;
import dev.sixik.unigui.api.layout.Align;
import dev.sixik.unigui.api.layout.Overflow;
import dev.sixik.unigui.api.math.ColorView;
import dev.sixik.unigui.api.math.RectView;
import dev.sixik.unigui.api.render.DrawScope;
import dev.sixik.unigui.api.text.TextOverflowMode;
import dev.sixik.unigui.api.widget.Visibility;
import dev.sixik.unigui.api.widget.Widget;
import dev.sixik.unigui.api.widget.render.WidgetRender;
import dev.sixik.unigui.widgets.containers.Box;
import dev.sixik.unigui.widgets.containers.HBox;
import dev.sixik.unigui.widgets.containers.LinearBox;
import dev.sixik.unigui.widgets.containers.VBox;
import dev.sixik.unigui.widgets.core.Orientation;
import dev.sixik.unigui.widgets.display.CanvasWidget;
import dev.sixik.unigui.widgets.display.Label;
import dev.sixik.unigui.widgets.dnd.DragController;
import dev.sixik.unigui.widgets.dnd.DragPayload;
import dev.sixik.unigui.widgets.feedback.ContextMenu;
import dev.sixik.unigui.widgets.feedback.OverlayLayer;
import dev.sixik.unigui.widgets.interaction.Button;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Секция компонента в инспекторе магазина в стиле Unity Inspector. Поддерживает drag-to-reorder,
 * контекстное меню (ПКМ / ⋮), сворачивание и редактирование полей.
 */
public class ShopComponentSection extends LinearBox {

    private final ShopOffer offer;
    private final ShopComponent component;
    private final IComponentType<?> type;

    private OverlayLayer overlayLayer;
    private static ContextMenu activeContextMenu = null;
    private DragController dragController;
    private boolean draggable = true;

    private boolean expanded = true;
    private Consumer<Boolean> onExpandChanged;
    private final CanvasWidget arrowCanvas = new CanvasWidget();
    private final VBox bodyHost = new VBox();
    private Button menuButton;
    private Button deleteButton;

    public ShopComponentSection(ShopOffer offer, ShopComponent component) {
        this(offer, component, null, true);
    }

    public ShopComponentSection(
            ShopOffer offer, ShopComponent component, OverlayLayer overlayLayer) {
        this(offer, component, overlayLayer, true);
    }

    public ShopComponentSection(
            ShopOffer offer,
            ShopComponent component,
            OverlayLayer overlayLayer,
            boolean initialExpanded) {
        super(Orientation.VERTICAL);
        this.offer = offer;
        this.component = component;
        this.type = component.getType();
        this.overlayLayer = overlayLayer;
        this.expanded = initialExpanded;

        spacing(0.0f);
        layout(
                style ->
                        style.widthPercent(100.0f)
                                .flexNone()
                                .margin(0.0f)
                                .overflowX(Overflow.HIDDEN));

        boxVisualEnabled(false);

        buildHeader();
        buildBody();
        addChild(ShopEditorStyles.separatorLine());
    }

    public boolean isDraggable() {
        return draggable;
    }

    public ShopComponent getComponent() {
        return component;
    }

    public ShopOffer getOffer() {
        return offer;
    }

    private void buildHeader() {
        Box headerSurface = new Box();
        headerSurface.themeEnabled(false);
        headerSurface.backgroundVisible(true);
        headerSurface.background().set(ShopEditorStyles.BG_HEADER);
        headerSurface.borderVisible(true);
        headerSurface.borderColor().set(ShopEditorStyles.BORDER_SUBTLE);
        headerSurface.borderWidth(0.5f);
        headerSurface.layout(
                style ->
                        style.widthPercent(100.0f)
                                .height(ShopEditorStyles.COMPONENT_HEADER_HEIGHT)
                                .padding(6.0f, 0.0f, 6.0f, 0.0f)
                                .flexNone());

        HBox headerRow = new HBox();
        headerRow.spacing(5.0f);
        headerRow.layout(style -> style.sizePercent(100.0f, 100.0f).alignItems(Align.CENTER));

        // Стрелка сворачивания / разворачивания (аккуратный векторный треугольник)
        arrowCanvas.layout(
                style -> style.fixed(10.0f, ShopEditorStyles.COMPONENT_HEADER_HEIGHT).flexNone());
        arrowCanvas.onDraw(
                context -> {
                    float x = arrowCanvas.layoutBounds().x();
                    float y = arrowCanvas.layoutBounds().y();
                    float height = arrowCanvas.layoutBounds().height();
                    float cx = x + 5.0f;
                    float cy = y + height * 0.5f;

                    context.pathClear();
                    if (expanded) {
                        // Стрелка вниз ▼
                        context.pathLineTo(cx - 3.5f, cy - 2.0f);
                        context.pathLineTo(cx + 3.5f, cy - 2.0f);
                        context.pathLineTo(cx, cy + 2.5f);
                    } else {
                        // Стрелка вправо ▶
                        context.pathLineTo(cx - 1.5f, cy - 4.0f);
                        context.pathLineTo(cx + 2.5f, cy);
                        context.pathLineTo(cx - 1.5f, cy + 4.0f);
                    }
                    context.pathFillConvex(ShopEditorStyles.TEXT_PRIMARY);
                });

        arrowCanvas.on(
                PointerPressedEvent.TYPE,
                event -> {
                    if (event.button() == PointerButton.PRIMARY) {
                        toggle();
                        event.cancel();
                    }
                });
        headerRow.addChild(arrowCanvas);

        // Заголовок компонента (с поддержкой бегущей строки при наведении и автоподгонкой)
        String idText =
                type != null ? type.getId().toString() : component.getClass().getSimpleName();
        Label titleLabel = new Label(idText);
        titleLabel.color().set(ShopEditorStyles.TEXT_BOLD);
        titleLabel.overflowMode(TextOverflowMode.MARQUEE_ON_HOVER);
        titleLabel.layout(
                style ->
                        style.height(ShopEditorStyles.COMPONENT_HEADER_HEIGHT)
                                .expand()
                                .margin(0.0f, 0.0f, 4.0f, 0.0f));
        headerRow.addChild(titleLabel);

        // Правые инструменты заголовка: 3 точки контекстного меню + кнопка удаления (✕)
        HBox tools = new HBox();
        tools.spacing(4.0f);
        tools.layout(
                style ->
                        style.height(ShopEditorStyles.COMPONENT_HEADER_HEIGHT)
                                .alignItems(Align.CENTER)
                                .flexNone());

        // Кнопка меню (⋮) как в Unity Inspector
        menuButton =
                createHeaderToolButton(
                        (draw, btn) -> {
                            RectView bounds = btn.layoutBounds();
                            float cx = bounds.x() + bounds.width() * 0.5f;
                            float cy = bounds.y() + bounds.height() * 0.5f;
                            float r = 1.2f;
                            ColorView col =
                                    btn.hovered()
                                            ? ShopEditorStyles.TEXT_PRIMARY
                                            : ShopEditorStyles.TEXT_MUTED;
                            draw.circleFilled(cx - r, cy - 4.0f - r, r * 2.0f, r * 2.0f, col);
                            draw.circleFilled(cx - r, cy - r, r * 2.0f, r * 2.0f, col);
                            draw.circleFilled(cx - r, cy + 4.0f - r, r * 2.0f, r * 2.0f, col);
                        },
                        () -> {
                            RectView b = menuButton.layoutBounds();
                            openContextMenu(b.x() + b.width() - 175.0f, b.y() + b.height() + 2.0f);
                        });
        tools.addChild(menuButton);

        // Кнопка быстрого удаления компонента (✕)
        if (ShopEditorState.get().canDeleteComponent(offer, component)) {
            deleteButton =
                    UnityIcons.createCloseButton(
                            () -> {
                                ShopEditorState.get().removeComponent(offer, component);
                            });
            tools.addChild(deleteButton);
        } else {
            deleteButton = null;
        }

        headerRow.addChild(tools);
        headerSurface.addChild(headerRow);

        // 1. Drag-and-drop перетаскивание секции за заголовок (как в Unity Inspector):
        dragController =
                DragController.attach(headerSurface)
                        .dragged(this)
                        .payload(
                                () ->
                                        DragPayload.of(
                                                type != null
                                                        ? type.getId().toString()
                                                        : component.getClass().getSimpleName(),
                                                "unity.component",
                                                type != null
                                                        ? type.getId().toString()
                                                        : component.getClass().getSimpleName(),
                                                this))
                        .lockGhostAxis(Orientation.VERTICAL)
                        .threshold(4.0f)
                        .filter(
                                event -> {
                                    if (!draggable) return false;
                                    Widget target = event.target();
                                    if (target == arrowCanvas
                                            || isDescendantOf(target, arrowCanvas)) return false;
                                    if (tools == target || isDescendantOf(target, tools))
                                        return false;
                                    return true;
                                });

        // 2. Клик левой кнопкой по свободной области заголовка переключает foldout
        headerSurface.on(
                PointerReleasedEvent.TYPE,
                event -> {
                    if (event.phase() == EventPhase.CAPTURE) return;
                    if (event.button() != PointerButton.PRIMARY) return;
                    if (event.isCancelled()) return;
                    if (dragController != null && dragController.dragging()) return;

                    Widget target = event.target();
                    if (target == arrowCanvas || isDescendantOf(target, arrowCanvas)) return;
                    if (tools == target || isDescendantOf(target, tools)) return;

                    toggle();
                    event.cancel();
                });

        // 3. Клик правой кнопкой мыши (ПКМ) открывает контекстное меню компонентов
        headerSurface.on(
                PointerPressedEvent.TYPE,
                event -> {
                    if (event.button() == PointerButton.SECONDARY) {
                        openContextMenu(event.rootX(), event.rootY());
                        event.cancel();
                    }
                });

        addChild(headerSurface);
    }

    private void buildBody() {
        bodyHost.spacing(3.0f);
        bodyHost.layout(
                style ->
                        style.widthPercent(100.0f)
                                .padding(8.0f, 4.0f, 8.0f, 4.0f)
                                .flexNone()
                                .overflowX(Overflow.HIDDEN));
        bodyHost.themeEnabled(false);
        bodyHost.boxVisualEnabled(false);
        bodyHost.visibility(expanded ? Visibility.VISIBLE : Visibility.COLLAPSED);

        // ПКМ по телу компонента также открывает контекстное меню компонента
        bodyHost.on(
                PointerPressedEvent.TYPE,
                event -> {
                    if (event.button() == PointerButton.SECONDARY) {
                        openContextMenu(event.rootX(), event.rootY());
                        event.cancel();
                    }
                });

        populateComponentProperties();

        addChild(bodyHost);
    }

    private void populateComponentProperties() {
        ObjectArrayList<ComponentConfigAccess.CachedField> fields;
        try {
            fields = ComponentConfigAccess.getCachedFields(component.getClass());
        } catch (Exception e) {
            Label errorLabel = new Label("Failed to load fields: " + e.getMessage());
            errorLabel.color().set(ShopEditorStyles.AXIS_X);
            bodyHost.addChild(errorLabel);
            return;
        }

        if (fields == null || fields.isEmpty()) {
            Label noFields =
                    new Label(
                            type != null
                                    ? type.getId().getPath() + " has no configurable parameters."
                                    : "No configurable parameters.");
            noFields.color().set(ShopEditorStyles.TEXT_MUTED);
            noFields.layout(style -> style.height(ShopEditorStyles.ROW_HEIGHT).margin(2.0f, 0.0f));
            bodyHost.addChild(noFields);
            return;
        }

        for (ComponentConfigAccess.CachedField field : fields) {
            try {
                Object currentValue = field.getter().invoke(component);
                String labelText = resolveFieldLabel(field.translationKey());

                Class<?> type = field.type();
                if (type == String.class) {
                    if (field.options() != null) {
                        List<String> options = resolveOptions(field.options().provider());
                        bodyHost.addChild(
                                ShopPropertyRows.optionsRow(
                                        labelText,
                                        (String) currentValue,
                                        options,
                                        field.options().allowCustom(),
                                        newVal -> updateFieldValue(field, newVal)));
                    } else {
                        bodyHost.addChild(
                                ShopPropertyRows.stringRow(
                                        labelText,
                                        (String) currentValue,
                                        newVal -> updateFieldValue(field, newVal)));
                    }
                } else if (type == ResourceLocation.class) {
                    if (field.options() != null) {
                        List<String> options = resolveOptions(field.options().provider());
                        String strVal = currentValue != null ? currentValue.toString() : "";
                        bodyHost.addChild(
                                ShopPropertyRows.optionsRow(
                                        labelText,
                                        strVal,
                                        options,
                                        field.options().allowCustom(),
                                        newVal ->
                                                updateFieldValue(
                                                        field, ResourceLocation.tryParse(newVal))));
                    } else {
                        String strVal = currentValue != null ? currentValue.toString() : "";
                        bodyHost.addChild(
                                ShopPropertyRows.stringRow(
                                        labelText,
                                        strVal,
                                        newVal ->
                                                updateFieldValue(
                                                        field, ResourceLocation.tryParse(newVal))));
                    }
                } else if (type == int.class || type == Integer.class) {
                    int val = currentValue != null ? ((Number) currentValue).intValue() : 0;
                    bodyHost.addChild(
                            ShopPropertyRows.numberRow(
                                    labelText,
                                    val,
                                    true,
                                    field.numberRange(),
                                    newVal -> updateFieldValue(field, (int) newVal)));
                } else if (type == long.class || type == Long.class) {
                    long val = currentValue != null ? ((Number) currentValue).longValue() : 0L;
                    bodyHost.addChild(
                            ShopPropertyRows.numberRow(
                                    labelText,
                                    val,
                                    true,
                                    field.numberRange(),
                                    newVal -> updateFieldValue(field, (long) newVal)));
                } else if (type == double.class || type == Double.class) {
                    double val = currentValue != null ? ((Number) currentValue).doubleValue() : 0.0;
                    bodyHost.addChild(
                            ShopPropertyRows.numberRow(
                                    labelText,
                                    val,
                                    false,
                                    field.numberRange(),
                                    newVal -> updateFieldValue(field, newVal)));
                } else if (type == float.class || type == Float.class) {
                    float val = currentValue != null ? ((Number) currentValue).floatValue() : 0.0f;
                    bodyHost.addChild(
                            ShopPropertyRows.numberRow(
                                    labelText,
                                    val,
                                    false,
                                    field.numberRange(),
                                    newVal -> updateFieldValue(field, (float) newVal)));
                } else if (type == boolean.class || type == Boolean.class) {
                    boolean val = currentValue != null && (Boolean) currentValue;
                    bodyHost.addChild(
                            ShopPropertyRows.booleanRow(
                                    labelText, val, newVal -> updateFieldValue(field, newVal)));
                } else if (type.isEnum()) {
                    bodyHost.addChild(
                            ShopPropertyRows.enumRow(
                                    labelText,
                                    (Enum<?>) currentValue,
                                    type,
                                    newVal -> updateFieldValue(field, newVal)));
                } else if (type == ItemStack.class) {
                    ItemStack val = currentValue instanceof ItemStack s ? s : ItemStack.EMPTY;
                    bodyHost.addChild(
                            ShopPropertyRows.itemStackRow(
                                    labelText, val, newVal -> updateFieldValue(field, newVal)));
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    private void updateFieldValue(ComponentConfigAccess.CachedField field, Object newValue) {
        try {
            field.setter().invoke(component, newValue);
            ShopEditorState.get().markDirty();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private List<String> resolveOptions(String provider) {
        List<String> list = new ArrayList<>();
        if ("sdm:catalog_ids".equals(provider)) {
            var shop = ShopEditorState.get().getDraftShop();
            if (shop != null && shop.getCategories() != null) {
                for (CatalogComponent cat : shop.getCategories().getCatalogsComponents()) {
                    if (cat.getId() != null && !cat.getId().isBlank()) {
                        list.add(cat.getId());
                    }
                }
            }
        } else if ("sdm:money_ids".equals(provider)) {
            for (ICurrency currency : SDMEconomyServiceClient.getAllCurrencies().values()) {
                if (!ShopEditorState.get().isCurrencyDeleted(currency.getId())) {
                    list.add(currency.getId().toString());
                }
            }
            if (ShopEditorState.get().getSession() != null) {
                for (dev.sixik.sdmshop2.libs.sdmeconomy.CurrencyDraft draft :
                        ShopEditorState.get().getSession().currencyDrafts()) {
                    if (draft.kind() != dev.sixik.sdmshop2.libs.sdmeconomy.CurrencyDraft.Kind.DELETE
                            && !list.contains(draft.id().toString())) {
                        list.add(draft.id().toString());
                    }
                }
            }
        }
        return list;
    }

    private String resolveFieldLabel(String key) {
        if (key == null || key.isBlank()) return "";
        if (I18n.exists(key)) {
            return Component.translatable(key).getString();
        }
        int lastDot = key.lastIndexOf('.');
        return lastDot >= 0 ? key.substring(lastDot + 1) : key;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public ShopComponentSection expanded(boolean expanded) {
        if (this.expanded != expanded) {
            this.expanded = expanded;
            this.bodyHost.visibility(this.expanded ? Visibility.VISIBLE : Visibility.COLLAPSED);
            this.arrowCanvas.invalidate(2);
            if (this.onExpandChanged != null) {
                this.onExpandChanged.accept(this.expanded);
            }
        }
        return this;
    }

    public ShopComponentSection onExpandChanged(Consumer<Boolean> listener) {
        this.onExpandChanged = listener;
        return this;
    }

    public void toggle() {
        expanded(!this.expanded);
    }

    public void openContextMenu(float x, float y) {
        OverlayLayer layer = findOverlayLayer();
        if (layer == null) return;

        if (activeContextMenu != null) {
            try {
                activeContextMenu.close();
            } catch (Exception ignored) {
            }
            activeContextMenu = null;
        }

        float menuWidth = 175.0f;
        ContextMenu menu = new ContextMenu();
        ShopEditorStyles.styleContextMenu(menu);

        int index = offer.indexOfComponent(component);
        int total = offer.getComponents().size();
        boolean canMoveUp = index > 0;
        boolean canMoveDown = index >= 0 && index < total - 1;
        boolean canDelete = ShopEditorState.get().canDeleteComponent(offer, component);

        ShopEditorStyles.addContextMenuItem(
                menu,
                "Move Up",
                canMoveUp,
                menuWidth,
                () -> {
                    ShopEditorState.get().moveComponent(offer, component, -1);
                });
        ShopEditorStyles.addContextMenuItem(
                menu,
                "Move Down",
                canMoveDown,
                menuWidth,
                () -> {
                    ShopEditorState.get().moveComponent(offer, component, 1);
                });
        ShopEditorStyles.addContextMenuSeparator(menu, menuWidth);
        ShopEditorStyles.addContextMenuItem(
                menu,
                "Duplicate Component",
                true,
                menuWidth,
                () -> {
                    ShopEditorState.get().duplicateComponent(offer, component);
                });
        ShopEditorStyles.addContextMenuSeparator(menu, menuWidth);
        ShopEditorStyles.addContextMenuItem(
                menu,
                "Remove Component",
                canDelete,
                menuWidth,
                () -> {
                    ShopEditorState.get().removeComponent(offer, component);
                });

        menu.openAt(x, y);
        layer.addOverlay(menu);
        activeContextMenu = menu;
    }

    private OverlayLayer findOverlayLayer() {
        if (this.overlayLayer != null) return this.overlayLayer;
        Widget current = this;
        while (current != null) {
            if (current instanceof OverlayLayer layer) return layer;
            current = current.parent();
        }
        return null;
    }

    private static boolean isDescendantOf(Widget child, Widget ancestor) {
        if (child == null || ancestor == null) return false;
        Widget current = child;
        while (current != null) {
            if (current == ancestor) return true;
            current = current.parent();
        }
        return false;
    }

    private static Button createHeaderToolButton(
            BiConsumer<DrawScope, Button> iconRenderer, Runnable onClick) {
        Button btn = new Button();
        btn.themeEnabled(false);
        btn.boxVisualEnabled(false);
        btn.focusRingVisible(false);
        btn.focusable(false);
        btn.layout(
                style ->
                        style.size(16.0f, 16.0f)
                                .alignSelf(Align.CENTER)
                                .flexGrow(0.0f)
                                .flexShrink(0.0f));

        btn.renderer(
                WidgetRender.of(
                        Button.class,
                        (draw, b) -> {
                            RectView bounds = b.layoutBounds();
                            float x = bounds.x();
                            float y = bounds.y();
                            float w = bounds.width();
                            float h = bounds.height();
                            if (w <= 0.0f || h <= 0.0f) return;

                            if (b.pressed()) {
                                draw.roundedRectFilled(
                                        x, y, w, h, 2.0f, ShopEditorStyles.BG_WORKSPACE);
                            } else if (b.hovered()) {
                                draw.roundedRectFilled(
                                        x, y, w, h, 2.0f, ShopEditorStyles.BG_HEADER_HOVER);
                            }

                            iconRenderer.accept(draw, b);
                        }));

        if (onClick != null) {
            btn.onClick(e -> onClick.run());
        }

        return btn;
    }
}
