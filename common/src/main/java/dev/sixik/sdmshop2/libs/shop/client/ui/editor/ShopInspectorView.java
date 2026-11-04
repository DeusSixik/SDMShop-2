package dev.sixik.sdmshop2.libs.shop.client.ui.editor;

import dev.sixik.sdmshop2.libs.sdmeconomy.CurrencyDraft;
import dev.sixik.sdmshop2.libs.sdmeconomy.ICurrency;
import dev.sixik.sdmshop2.libs.shop.base.ShopOffer;
import dev.sixik.sdmshop2.libs.shop.client.ui.editor.unity.UnityIcons;
import dev.sixik.sdmshop2.libs.shop.client.ui.editor.unity.UnityStyles;
import dev.sixik.sdmshop2.libs.shop.components.api.ShopComponent;
import dev.sixik.sdmshop2.libs.shop.components.misc.CatalogComponent;
import dev.sixik.sdmshop2.libs.shop.components.misc.NameComponent;
import dev.sixik.unigui.api.core.FrameContext;
import dev.sixik.unigui.api.layout.Align;
import dev.sixik.unigui.api.layout.Overflow;
import dev.sixik.unigui.api.text.TextOverflowMode;
import dev.sixik.unigui.widgets.containers.Box;
import dev.sixik.unigui.widgets.containers.HBox;
import dev.sixik.unigui.widgets.containers.LinearBox;
import dev.sixik.unigui.widgets.containers.ScrollView;
import dev.sixik.unigui.widgets.containers.VBox;
import dev.sixik.unigui.widgets.core.Orientation;
import dev.sixik.unigui.widgets.display.CanvasWidget;
import dev.sixik.unigui.widgets.display.Label;
import dev.sixik.unigui.widgets.dnd.ReorderController;
import dev.sixik.unigui.widgets.feedback.OverlayLayer;
import dev.sixik.unigui.widgets.interaction.Button;
import dev.sixik.unigui.widgets.interaction.Checkbox;
import dev.sixik.unigui.widgets.interaction.TextInput;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;

/**
 * Панель Inspector в стиле Unity Dark Inspector (1 в 1 с редактором Unity).
 * Отображает свойства и компоненты выбранного объекта (ShopOffer, CatalogComponent, CurrencyDraft).
 */
public class ShopInspectorView extends LinearBox {

    private final VBox scrollContent = new VBox();
    private final OverlayLayer overlayLayer;
    private ScrollView scrollView;
    private Object lastTarget;
    private float pendingScrollY = -1.0f;
    private final Map<ShopComponent, Boolean> componentExpandedState = new WeakHashMap<>();

    public ShopInspectorView(OverlayLayer overlayLayer) {
        super(Orientation.VERTICAL);
        this.overlayLayer = overlayLayer;
        spacing(0.0f);
        themeEnabled(false);
        backgroundVisible(true);
        background().set(UnityStyles.BG_WINDOW);
        layout(style -> style.sizePercent(100.0f, 100.0f).flexGrow(1.0f).flexShrink(1.0f));

        scrollContent.spacing(0.0f);
        scrollContent.themeEnabled(false);
        scrollContent.backgroundVisible(true);
        scrollContent.background().set(UnityStyles.BG_WINDOW);
        scrollContent.layout(style -> style.widthPercent(100.0f)
                .padding(0.0f, 0.0f, 0.0f, 16.0f)
                .flexGrow(0.0f).flexShrink(0.0f));

        this.scrollView = new ScrollView(scrollContent);
        this.scrollView.scrollStep(18.0f);
        this.scrollView.overlayScrollBars(false);
        this.scrollView.scrollbarGap(2.0f);
        this.scrollView.scrollbarTrackColor().set(UnityStyles.BORDER_DEFAULT);
        this.scrollView.scrollbarThumbColor().set(UnityStyles.TEXT_MUTED);
        this.scrollView.layout(style -> style.sizePercent(100.0f, 100.0f)
                .overflowX(Overflow.HIDDEN).overflowY(Overflow.AUTO)
                .flexGrow(1.0f).flexShrink(1.0f));
        addChild(this.scrollView);

        ShopEditorState.get().addListener(this::refreshInspector);
        refreshInspector();
    }

    public void refreshInspector() {
        float prevScrollY = scrollView != null ? scrollView.scrollY() : 0.0f;
        Object target = ShopEditorState.get().getSelectedTarget();
        boolean sameTarget = Objects.equals(target, lastTarget);
        lastTarget = target;

        scrollContent.clearChildren();

        if (target instanceof ShopOffer offer) {
            buildOfferInspector(offer);
        } else if (target instanceof CatalogComponent category) {
            buildCategoryInspector(category);
        } else if (target instanceof CurrencyDraft currency) {
            buildCurrencyDraftInspector(currency);
        } else if (target instanceof ICurrency currency) {
            buildCurrencyInspector(currency);
        } else {
            buildEmptyPlaceholder();
        }

        if (sameTarget && scrollView != null && prevScrollY > 0.0f) {
            scrollView.scrollTo(0.0f, prevScrollY);
            pendingScrollY = prevScrollY;
        } else {
            pendingScrollY = -1.0f;
        }
    }

    @Override
    public void tick(FrameContext context) {
        super.tick(context);
        if (pendingScrollY >= 0.0f && scrollView != null) {
            scrollView.scrollTo(0.0f, pendingScrollY);
            pendingScrollY = -1.0f;
        }
    }

    private void buildOfferInspector(ShopOffer offer) {
        // --- 1. Шапка объекта в стиле Unity Inspector ---
        VBox goHeader = new VBox();
        goHeader.spacing(4.0f);
        goHeader.themeEnabled(false);
        goHeader.backgroundVisible(true);
        goHeader.background().set(UnityStyles.BG_WINDOW);
        goHeader.layout(style -> style.widthPercent(100.0f)
                .padding(8.0f, 6.0f, 8.0f, 6.0f)
                .overflowX(Overflow.HIDDEN)
                .flexGrow(0.0f).flexShrink(0.0f));

        HBox topRow = new HBox();
        topRow.spacing(6.0f);
        topRow.layout(style -> style.widthPercent(100.0f).height(20.0f)
                .alignItems(Align.CENTER).overflowX(Overflow.HIDDEN).flexGrow(0.0f).flexShrink(0.0f));

        // Иконка 3D синего куба Unity
        CanvasWidget cubeIcon = UnityIcons.gameObjectCube(16.0f);
        topRow.addChild(cubeIcon);

        // Чекбокс активности
        Checkbox activeCheck = new Checkbox("");
        activeCheck.checked(true);
        UnityStyles.styleCheckbox(activeCheck);
        topRow.addChild(activeCheck);

        // Поле ввода имени оффера
        NameComponent nameComp = offer.getComponent(NameComponent.class).orElse(null);
        String nameStr = nameComp != null ? nameComp.getName() : "Unnamed Offer";
        TextInput nameInput = new TextInput(nameStr);
        UnityStyles.styleTextInput(nameInput);
        nameInput.layout(style -> style.height(18.0f).flexGrow(1.0f).flexShrink(1.0f).minWidth(0.0f));
        nameInput.onTextChanged(e -> {
            String newName = e.newText();
            if (nameComp != null) {
                try {
                    var f = NameComponent.class.getDeclaredField("name");
                    f.setAccessible(true);
                    f.set(nameComp, newName);
                } catch (Exception ignored) {}
            } else {
                offer.addComponent(new NameComponent(newName));
            }
            ShopEditorState.get().markDirty();
        });
        topRow.addChild(nameInput);

        Button deleteBtn = new Button("Delete");
        UnityStyles.styleButton(deleteBtn);
        deleteBtn.textColor().set(UnityStyles.AXIS_X);
        deleteBtn.layout(style -> style.height(18.0f).padding(6.0f, 0.0f).flexNone());
        deleteBtn.onClick(e -> ShopEditorState.get().deleteOffer(offer));
        topRow.addChild(deleteBtn);

        goHeader.addChild(topRow);

        // Строка Tag / Layer
        HBox metaRow = new HBox();
        metaRow.spacing(8.0f);
        metaRow.layout(style -> style.widthPercent(100.0f).height(16.0f)
                .alignItems(Align.CENTER).flexGrow(0.0f).flexShrink(0.0f));

        Label tagLbl = new Label("Tag: Offer");
        tagLbl.color().set(UnityStyles.TEXT_MUTED);
        Label uuidLbl = new Label("UUID: " + offer.getUUID().toString().substring(0, 8) + "...");
        uuidLbl.color().set(UnityStyles.TEXT_MUTED);

        metaRow.addChild(tagLbl);
        metaRow.addChild(uuidLbl);
        goHeader.addChild(metaRow);

        scrollContent.addChild(goHeader);
        scrollContent.addChild(UnityStyles.separatorLine());

        // --- 2. Секции компонентов с Drag-and-drop перетаскиванием (Unity DnD) ---
        VBox componentsHost = new VBox();
        componentsHost.spacing(0.0f);
        componentsHost.themeEnabled(false);
        componentsHost.boxVisualEnabled(false);
        componentsHost.layout(style -> style.widthPercent(100.0f).flexGrow(0.0f).flexShrink(0.0f));

        for (ShopComponent component : offer.getComponents()) {
            boolean isExp = componentExpandedState.getOrDefault(component, true);
            ShopComponentSection section = new ShopComponentSection(offer, component, overlayLayer, isExp);
            section.onExpandChanged(exp -> componentExpandedState.put(component, exp));
            componentsHost.addChild(section);
        }

        ReorderController.attach(componentsHost)
                .accepts("unity.component")
                .orientation(Orientation.VERTICAL)
                .indicator(UnityStyles.BORDER_INPUT_FOCUS, 2.0f, 6.0f)
                .itemFilter(child -> child instanceof ShopComponentSection sec && sec.isDraggable())
                .onReorder((from, to) -> {
                    if (from != null && to != null && !from.equals(to)) {
                        List<ShopComponent> list = offer.getComponents();
                        if (from >= 0 && from < list.size()) {
                            ShopComponent comp = list.get(from);
                            ShopEditorState.get().moveComponentToIndex(offer, comp, to);
                        }
                    }
                });

        scrollContent.addChild(componentsHost);

        // --- 3. Большая кнопка «Add Component» внизу ---
        VBox addCompHost = new VBox();
        addCompHost.layout(style -> style.widthPercent(100.0f)
                .padding(12.0f, 0.0f, 12.0f, 0.0f)
                .alignItems(Align.CENTER).flexGrow(0.0f).flexShrink(0.0f));

        Button addCompBtn = new Button("Add Component");
        UnityStyles.styleButton(addCompBtn);
        addCompBtn.layout(style -> style.width(200.0f).height(24.0f));
        addCompBtn.onClick(event -> {
            openComponentPicker(offer);
        });

        addCompHost.addChild(addCompBtn);
        scrollContent.addChild(addCompHost);
    }

    private void openComponentPicker(ShopOffer offer) {
        OverlayLayer targetOverlay = overlayLayer != null ? overlayLayer : findOverlayLayer(this);
        if (targetOverlay != null) {
            for (dev.sixik.unigui.api.widget.Widget w : targetOverlay.children()) {
                if (w instanceof ShopComponentPickerModal) return;
            }
            targetOverlay.addOverlay(new ShopComponentPickerModal(offer));
        }
    }

    private static OverlayLayer findOverlayLayer(dev.sixik.unigui.api.widget.Widget start) {
        dev.sixik.unigui.api.widget.Widget current = start;
        while (current != null) {
            if (current instanceof OverlayLayer layer) return layer;
            current = current.parent();
        }
        return null;
    }

    private void buildCategoryInspector(CatalogComponent category) {
        VBox header = new VBox();
        header.spacing(4.0f);
        header.themeEnabled(false);
        header.backgroundVisible(true);
        header.background().set(UnityStyles.BG_WINDOW);
        header.layout(style -> style.widthPercent(100.0f).padding(8.0f, 6.0f).flexNone().overflowX(Overflow.HIDDEN));

        HBox topRow = new HBox();
        topRow.spacing(6.0f);
        topRow.layout(style -> style.widthPercent(100.0f).height(20.0f).alignItems(Align.CENTER).overflowX(Overflow.HIDDEN));

        CanvasWidget icon = UnityIcons.gameObjectIcon(14.0f, () -> UnityIcons.ShopObjectType.CATEGORY);
        topRow.addChild(icon);

        Label title = new Label("Category: " + category.getId());
        title.color().set(UnityStyles.TEXT_BOLD);
        title.overflowMode(TextOverflowMode.MARQUEE_ON_HOVER);
        title.layout(s -> s.height(18.0f).expand().margin(0.0f, 0.0f, 4.0f, 0.0f));
        topRow.addChild(title);

        Button deleteBtn = new Button("Delete");
        UnityStyles.styleButton(deleteBtn);
        deleteBtn.textColor().set(UnityStyles.AXIS_X);
        deleteBtn.layout(style -> style.height(18.0f).padding(6.0f, 0.0f).flexNone());
        deleteBtn.onClick(e -> ShopEditorState.get().deleteCategory(category));
        topRow.addChild(deleteBtn);

        header.addChild(topRow);
        scrollContent.addChild(header);
        scrollContent.addChild(UnityStyles.separatorLine());

        VBox props = new VBox();
        props.spacing(3.0f);
        props.themeEnabled(false);
        props.boxVisualEnabled(false);
        props.layout(style -> style.widthPercent(100.0f).padding(8.0f, 4.0f, 8.0f, 4.0f).flexGrow(0.0f).flexShrink(0.0f).overflowX(Overflow.HIDDEN));

        props.addChild(ShopPropertyRows.stringRow("Category ID", category.getId(), id -> {
            category.setId(id);
            ShopEditorState.get().markDirty();
        }));

        props.addChild(ShopPropertyRows.numberRow("Order", category.getOrder(), true, null, val -> {
            category.setOrder((int) val);
            ShopEditorState.get().markDirty();
        }));

        props.addChild(ShopPropertyRows.enumRow("Icon Type", category.getIconType(), CatalogComponent.IconType.class, val -> {
            category.setIconType((CatalogComponent.IconType) val);
            ShopEditorState.get().markDirty();
        }));

        props.addChild(ShopPropertyRows.itemStackRow("Icon Item", category.getIconItem(), item -> {
            category.setIconItem(item);
            ShopEditorState.get().markDirty();
        }));

        props.addChild(ShopPropertyRows.stringRow("Icon Texture", category.getIconTexture() != null ? category.getIconTexture().toString() : "", tex -> {
            category.setIconTexture(net.minecraft.resources.ResourceLocation.tryParse(tex));
            ShopEditorState.get().markDirty();
        }));

        scrollContent.addChild(props);
    }

    private void buildCurrencyDraftInspector(CurrencyDraft currency) {
        VBox header = new VBox();
        header.spacing(4.0f);
        header.themeEnabled(false);
        header.backgroundVisible(true);
        header.background().set(UnityStyles.BG_WINDOW);
        header.layout(style -> style.widthPercent(100.0f).padding(8.0f, 6.0f).flexNone().overflowX(Overflow.HIDDEN));

        HBox topRow = new HBox();
        topRow.spacing(6.0f);
        topRow.layout(style -> style.widthPercent(100.0f).height(20.0f).alignItems(Align.CENTER).overflowX(Overflow.HIDDEN));

        CanvasWidget icon = UnityIcons.gameObjectIcon(14.0f, () -> UnityIcons.ShopObjectType.CURRENCY);
        topRow.addChild(icon);

        String currName = currency.displayName() != null && !currency.displayName().isBlank()
                ? currency.displayName()
                : (currency.id() != null ? currency.id().toString() : "Draft");
        Label title = new Label("Currency: " + currName);
        title.color().set(UnityStyles.TEXT_BOLD);
        title.overflowMode(TextOverflowMode.MARQUEE_ON_HOVER);
        title.layout(s -> s.height(18.0f).expand().margin(0.0f, 0.0f, 4.0f, 0.0f));
        topRow.addChild(title);

        Button deleteBtn = new Button("Delete");
        UnityStyles.styleButton(deleteBtn);
        deleteBtn.textColor().set(UnityStyles.AXIS_X);
        deleteBtn.layout(style -> style.height(18.0f).padding(6.0f, 0.0f).flexNone());
        deleteBtn.onClick(e -> ShopEditorState.get().deleteCurrency(currency));
        topRow.addChild(deleteBtn);

        header.addChild(topRow);
        scrollContent.addChild(header);
        scrollContent.addChild(UnityStyles.separatorLine());

        VBox props = new VBox();
        props.spacing(3.0f);
        props.themeEnabled(false);
        props.boxVisualEnabled(false);
        props.layout(style -> style.widthPercent(100.0f).padding(8.0f, 4.0f, 8.0f, 4.0f).flexGrow(0.0f).flexShrink(0.0f).overflowX(Overflow.HIDDEN));

        props.addChild(ShopPropertyRows.stringRow("Currency ID", currency.id().toString(), null));
        props.addChild(ShopPropertyRows.stringRow("Display Name", currency.displayName(), null));
        props.addChild(ShopPropertyRows.stringRow("Icon Symbol", currency.iconText(), null));

        scrollContent.addChild(props);
    }

    private void buildCurrencyInspector(ICurrency currency) {
        VBox header = new VBox();
        header.spacing(4.0f);
        header.themeEnabled(false);
        header.backgroundVisible(true);
        header.background().set(UnityStyles.BG_WINDOW);
        header.layout(style -> style.widthPercent(100.0f).padding(8.0f, 6.0f).flexNone().overflowX(Overflow.HIDDEN));

        HBox topRow = new HBox();
        topRow.spacing(6.0f);
        topRow.layout(style -> style.widthPercent(100.0f).height(20.0f).alignItems(Align.CENTER).overflowX(Overflow.HIDDEN));

        CanvasWidget icon = UnityIcons.gameObjectIcon(14.0f, () -> UnityIcons.ShopObjectType.CURRENCY);
        topRow.addChild(icon);

        Label title = new Label("Currency: " + currency.getDisplayName().getString());
        title.color().set(UnityStyles.TEXT_BOLD);
        title.overflowMode(TextOverflowMode.MARQUEE_ON_HOVER);
        title.layout(s -> s.height(18.0f).expand().margin(0.0f, 0.0f, 4.0f, 0.0f));
        topRow.addChild(title);

        header.addChild(topRow);
        scrollContent.addChild(header);
        scrollContent.addChild(UnityStyles.separatorLine());

        VBox props = new VBox();
        props.spacing(3.0f);
        props.themeEnabled(false);
        props.boxVisualEnabled(false);
        props.layout(style -> style.widthPercent(100.0f).padding(8.0f, 4.0f, 8.0f, 4.0f).flexGrow(0.0f).flexShrink(0.0f).overflowX(Overflow.HIDDEN));

        props.addChild(ShopPropertyRows.stringRow("ID", currency.getId().toString(), null));
        props.addChild(ShopPropertyRows.stringRow("Display Name", currency.getDisplayName().getString(), null));

        scrollContent.addChild(props);
    }

    private void buildEmptyPlaceholder() {
        VBox emptyBox = new VBox();
        emptyBox.layout(style -> style.widthPercent(100.0f).padding(40.0f, 10.0f).alignItems(Align.CENTER));

        Label label = new Label("Select an offer or category");
        label.color().set(UnityStyles.TEXT_MUTED);
        Label subLabel = new Label("to inspect and edit properties.");
        subLabel.color().set(UnityStyles.TEXT_MUTED);

        emptyBox.addChild(label);
        emptyBox.addChild(subLabel);
        scrollContent.addChild(emptyBox);
    }
}
