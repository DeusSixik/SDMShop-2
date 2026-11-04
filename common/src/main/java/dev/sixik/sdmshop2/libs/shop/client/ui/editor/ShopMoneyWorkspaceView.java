package dev.sixik.sdmshop2.libs.shop.client.ui.editor;

import dev.sixik.sdmshop2.libs.sdmeconomy.CurrencyDraft;
import dev.sixik.sdmshop2.libs.sdmeconomy.ICurrency;
import dev.sixik.sdmshop2.libs.sdmeconomy.SDMEconomyServiceClient;
import dev.sixik.sdmshop2.libs.sdmeconomy.custom_currency.ExternalItemCurrency;
import dev.sixik.sdmshop2.libs.sdmeconomy.icons.CurrencyIcon;
import dev.sixik.sdmshop2.libs.sdmeconomy.icons.IconType;
import dev.sixik.sdmshop2.libs.shop.client.ui.editor.unity.UnityIcons;
import dev.sixik.sdmshop2.libs.shop.client.ui.editor.unity.UnityStyles;
import dev.sixik.sdmshop2.libs.shop.client.ui.theme.ShopTheme;
import dev.sixik.sdmshop2.libs.shop.client.ui.widgets.ShopItemWidget;
import dev.sixik.unigui.api.core.FrameContext;
import dev.sixik.unigui.api.event.PointerPressedEvent;
import dev.sixik.unigui.api.input.PointerButton;
import dev.sixik.unigui.api.layout.*;
import dev.sixik.unigui.api.widget.Widget;
import dev.sixik.unigui.widgets.containers.Box;
import dev.sixik.unigui.widgets.containers.GridBox;
import dev.sixik.unigui.widgets.containers.LinearBox;
import dev.sixik.unigui.widgets.containers.ScrollView;
import dev.sixik.unigui.widgets.containers.VBox;
import dev.sixik.unigui.widgets.core.Orientation;
import dev.sixik.unigui.widgets.display.CanvasWidget;
import dev.sixik.unigui.widgets.display.ImageView;
import dev.sixik.unigui.widgets.display.Label;
import net.minecraft.resources.ResourceLocation;
import dev.sixik.unigui.api.render.SimpleTextureHandle;
import dev.sixik.unigui.widgets.feedback.ContextMenu;
import dev.sixik.unigui.widgets.feedback.OverlayLayer;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


/**
 * Центральная рабочая панель «Money» (список валют магазина).
 */
public class ShopMoneyWorkspaceView extends LinearBox {

    private final GridBox grid = new GridBox();
    private ScrollView scrollView;
    private final Map<Object, Widget> currencyCards = new HashMap<>();
    private Object pendingScrollCurrency;
    private ContextMenu activeCurrencyContextMenu;

    public ShopMoneyWorkspaceView() {
        super(Orientation.VERTICAL);
        spacing(0.0f);
        layout(style -> style.sizePercent(100.0f, 100.0f).flexGrow(1.0f).flexShrink(1.0f));

        grid.columns(3);
        grid.horizontalSpacing(8.0f);
        grid.verticalSpacing(8.0f);
        grid.layout(style -> style
                .gridTemplateColumns(GridTrack.fr(1.0f), GridTrack.fr(1.0f), GridTrack.fr(1.0f))
                .alignContent(AlignContent.START)
                .gap(8.0f)
                .widthPercent(100.0f)
                .padding(8.0f)
        );

        this.scrollView = new ScrollView(grid);
        this.scrollView.scrollStep(24.0f);
        this.scrollView.scrollbarGap(2.0f);
        this.scrollView.scrollbarTrackColor().set(UnityStyles.BORDER_DEFAULT);
        this.scrollView.scrollbarThumbColor().set(UnityStyles.TEXT_MUTED);
        this.scrollView.layout(style -> style.sizePercent(100.0f, 100.0f)
                .overflowX(Overflow.HIDDEN).overflowY(Overflow.AUTO)
                .flexGrow(1.0f).flexShrink(1.0f));
        addChild(this.scrollView);

        ShopEditorState.get().addListener(this::refreshView);
        refreshView();
    }

    public void refreshView() {
        grid.clearChildren();
        currencyCards.clear();

        // 1. Карточка "+ Add New Currency"
        VBox addCard = new VBox();
        addCard.spacing(4.0f);
        addCard.layout(style -> style
                .height(96.0f)
                .padding(6.0f)
                .alignItems(Align.CENTER)
                .justifyContent(Justify.CENTER)
                .flexNone()
        );
        addCard.boxVisualEnabled(true);
        addCard.backgroundVisible(true);
        addCard.background(UnityStyles.BG_PANEL);
        addCard.borderVisible(true);
        addCard.border(UnityStyles.SELECTION_BLUE);
        addCard.borderWidth(1.0f);
        addCard.radius(ShopTheme.RADIUS_CARD);

        Label plus = new Label("+");
        plus.color(UnityStyles.SELECTION_BLUE);
        Label addLbl = new Label("Add New Currency");
        addLbl.color(UnityStyles.TEXT_PRIMARY);

        addCard.addChild(plus);
        addCard.addChild(addLbl);
        addCard.on(PointerPressedEvent.TYPE, event -> {
            if (event.button() == PointerButton.PRIMARY) {
                CurrencyDraft draft = ShopEditorState.get().createNewCurrency();
                ShopEditorState.get().selectTarget(draft);
                refreshView();
                event.cancel();
            }
        });
        grid.addChild(addCard);

        Object currentTarget = ShopEditorState.get().getSelectedTarget();

        // 2. Стабильный, единый список валют (порядок не скачет при клике)
        ResourceLocation defaultId = new ResourceLocation("sdm", "coin");
        Set<ResourceLocation> serverIds = new LinkedHashSet<>();
        List<ResourceLocation> otherServerIds = new ArrayList<>();

        for (ICurrency cur : SDMEconomyServiceClient.getAllCurrencies().values()) {
            if (cur == null) continue;
            ResourceLocation id = cur.getId();
            if (ShopEditorState.get().isCurrencyDeleted(id)) continue;
            if (!id.equals(defaultId)) {
                otherServerIds.add(id);
            }
            serverIds.add(id);
        }

        // Сортировка существующих валют по ID для 100% стабильного порядка
        otherServerIds.sort(Comparator.comparing(ResourceLocation::toString));

        // Новые черновики валют (ещё не на сервере)
        List<ResourceLocation> newDraftIds = new ArrayList<>();
        if (ShopEditorState.get().getSession() != null) {
            for (CurrencyDraft d : ShopEditorState.get().getSession().currencyDrafts()) {
                if (d.kind() == CurrencyDraft.Kind.DELETE) continue;
                ResourceLocation id = d.id();
                if (!id.equals(defaultId) && !serverIds.contains(id)) {
                    newDraftIds.add(id);
                }
            }
        }

        // Итоговый единый список: дефолтная валюта первой, затем серверные, затем новые
        List<ResourceLocation> orderedIds = new ArrayList<>();
        if (!ShopEditorState.get().isCurrencyDeleted(defaultId)) {
            orderedIds.add(defaultId);
        }
        orderedIds.addAll(otherServerIds);
        orderedIds.addAll(newDraftIds);

        for (ResourceLocation id : orderedIds) {
            CurrencyDraft draft = ShopEditorState.get().findCurrencyDraft(id);
            ICurrency liveCur = SDMEconomyServiceClient.getCurrency(id);
            VBox card = buildCurrencyCard(id, draft, liveCur, currentTarget);
            grid.addChild(card);
        }
    }

    private VBox buildCurrencyCard(
            ResourceLocation id,
            @Nullable CurrencyDraft draft,
            @Nullable ICurrency liveCur,
            Object currentTarget
    ) {
        VBox card = new VBox();
        card.spacing(3.0f);
        card.layout(style -> style
                .height(96.0f)
                .padding(6.0f)
                .alignItems(Align.CENTER)
                .justifyContent(Justify.CENTER)
                .flexNone()
        );

        boolean isSelected = (currentTarget != null) && (
                currentTarget == draft ||
                currentTarget == liveCur ||
                (currentTarget instanceof CurrencyDraft cd && cd.id().equals(id)) ||
                (currentTarget instanceof ICurrency ic && ic.getId().equals(id))
        );

        card.boxVisualEnabled(true);
        card.backgroundVisible(true);
        card.background(isSelected ? UnityStyles.BG_HEADER_HOVER : UnityStyles.BG_PANEL);
        card.borderVisible(true);
        card.border(isSelected ? UnityStyles.SELECTION_BLUE : UnityStyles.BORDER_DEFAULT);
        card.borderWidth(isSelected ? 2.0f : 1.0f);
        card.radius(ShopTheme.RADIUS_CARD);

        // 1. Иконка
        Widget iconWidget = buildCardIconWidget(draft, liveCur);
        if (iconWidget != null) {
            card.addChild(iconWidget);
        }

        // 2. Название
        String displayName = draft != null ? draft.displayName() : (liveCur != null ? liveCur.getDisplayName().getString() : id.getPath());
        if (draft != null && liveCur != null) {
            displayName = displayName + " [Draft]";
        }
        Label nameLbl = new Label(displayName);
        nameLbl.color(isSelected ? UnityStyles.TEXT_BOLD : UnityStyles.TEXT_PRIMARY);
        card.addChild(nameLbl);

        // 3. ID
        Label idLbl = new Label(id.toString());
        idLbl.color(UnityStyles.TEXT_MUTED);
        card.addChild(idLbl);

        // 4. Обработчик клика (позиция в сетке не меняется)
        card.on(PointerPressedEvent.TYPE, event -> {
            if (event.button() == PointerButton.PRIMARY) {
                Object targetToSelect;
                if (draft != null) {
                    targetToSelect = draft;
                } else if (liveCur != null) {
                    targetToSelect = ShopEditorState.get().getOrCreateCurrencyDraft(liveCur);
                } else {
                    targetToSelect = id;
                }
                ShopEditorState.get().selectTarget(targetToSelect);
                refreshView();
                event.cancel();
            } else if (event.button() == PointerButton.SECONDARY) {
                Object targetToSelect = draft != null ? draft : liveCur;
                ShopEditorState.get().selectTarget(targetToSelect);
                openCurrencyContextMenu(event.rootX(), event.rootY(), targetToSelect != null ? targetToSelect : id);
                event.cancel();
            }
        });

        if (draft != null) currencyCards.put(draft, card);
        if (liveCur != null) currencyCards.put(liveCur, card);
        currencyCards.put(id, card);

        return card;
    }

    private Widget buildCardIconWidget(@Nullable CurrencyDraft draft, @Nullable ICurrency liveCur) {
        if (draft != null) {
            if (draft.kind() == CurrencyDraft.Kind.ITEM && draft.itemStack() != null && !draft.itemStack().isEmpty()) {
                ShopItemWidget itemSlot = new ShopItemWidget(draft.itemStack(), 20.0f).asSlot(24.0f, 24.0f);
                itemSlot.background().set(UnityStyles.BG_INPUT);
                itemSlot.borderWidth(1.0f);
                itemSlot.radius(3.0f);
                itemSlot.layout(s -> s.fixed(24.0f, 24.0f).flexNone());
                return itemSlot;
            } else if (draft.kind() == CurrencyDraft.Kind.TEXTURE && draft.iconTexture() != null) {
                String tex = draft.iconTexture().toString();
                ImageView img = new ImageView(new SimpleTextureHandle(tex, 24, 24, tex));
                img.layout(s -> s.size(24.0f, 24.0f).flexNone());
                return img;
            } else {
                Box badge = new Box();
                badge.boxVisualEnabled(true);
                badge.backgroundVisible(true);
                badge.background(UnityStyles.BG_INPUT);
                badge.borderVisible(true);
                badge.border(UnityStyles.BORDER_DEFAULT);
                badge.borderWidth(1.0f);
                badge.radius(3.0f);
                badge.layout(s -> s.size(24.0f, 24.0f).alignItems(Align.CENTER).justifyContent(Justify.CENTER).flexNone());
                Label sym = new Label(draft.iconText() != null && !draft.iconText().isBlank() ? draft.iconText() : "$");
                sym.color().set(UnityStyles.WARNING_YELLOW);
                badge.addChild(sym);
                return badge;
            }
        }

        if (liveCur instanceof ExternalItemCurrency eic && !eic.getItemType().isEmpty()) {
            ShopItemWidget itemSlot = new ShopItemWidget(eic.getItemType(), 20.0f).asSlot(24.0f, 24.0f);
            itemSlot.background().set(UnityStyles.BG_INPUT);
            itemSlot.borderWidth(1.0f);
            itemSlot.radius(3.0f);
            itemSlot.layout(s -> s.fixed(24.0f, 24.0f).flexNone());
            return itemSlot;
        }

        if (liveCur != null && liveCur.getIcon() != null) {
            CurrencyIcon curIcon = liveCur.getIcon();
            if (curIcon.type() == IconType.ITEM && curIcon.icon() instanceof ItemStack stack && !stack.isEmpty()) {
                ShopItemWidget itemSlot = new ShopItemWidget(stack, 20.0f).asSlot(24.0f, 24.0f);
                itemSlot.background().set(UnityStyles.BG_INPUT);
                itemSlot.borderWidth(1.0f);
                itemSlot.radius(3.0f);
                itemSlot.layout(s -> s.fixed(24.0f, 24.0f).flexNone());
                return itemSlot;
            } else if (curIcon.type() == IconType.TEXTURE && curIcon.icon() instanceof ResourceLocation loc) {
                String tex = loc.toString();
                ImageView img = new ImageView(new SimpleTextureHandle(tex, 24, 24, tex));
                img.layout(s -> s.size(24.0f, 24.0f).flexNone());
                return img;
            } else if (curIcon.type() == IconType.TEXTURE && curIcon.icon() instanceof String str && (str.contains(":") || str.contains("/") || str.endsWith(".png"))) {
                ImageView img = new ImageView(new SimpleTextureHandle(str, 24, 24, str));
                img.layout(s -> s.size(24.0f, 24.0f).flexNone());
                return img;
            }
        }

        // Text symbol fallback
        String symbol = "$";
        if (liveCur != null && liveCur.getIcon() != null && liveCur.getIcon().icon() instanceof String s && !s.isBlank()) {
            symbol = s;
        } else if (liveCur != null) {
            try {
                symbol = liveCur.format(java.math.BigDecimal.ONE).replaceAll("[0-9.,\\s]", "");
                if (symbol.isEmpty()) symbol = "$";
            } catch (Exception ignored) {}
        }
        Box badge = new Box();
        badge.boxVisualEnabled(true);
        badge.backgroundVisible(true);
        badge.background(UnityStyles.BG_INPUT);
        badge.borderVisible(true);
        badge.border(UnityStyles.BORDER_DEFAULT);
        badge.borderWidth(1.0f);
        badge.radius(3.0f);
        badge.layout(s -> s.size(24.0f, 24.0f).alignItems(Align.CENTER).justifyContent(Justify.CENTER).flexNone());
        Label sym = new Label(symbol);
        sym.color().set(UnityStyles.WARNING_YELLOW);
        badge.addChild(sym);
        return badge;
    }

    private void openCurrencyContextMenu(float x, float y, Object target) {
        OverlayLayer overlay = findOverlayLayer(this);
        if (overlay == null) return;

        if (activeCurrencyContextMenu != null) {
            overlay.removeOverlay(activeCurrencyContextMenu);
            activeCurrencyContextMenu = null;
        }

        float menuWidth = 140.0f;
        ContextMenu menu = new ContextMenu();
        UnityStyles.styleContextMenu(menu);

        boolean canDelete = !(target instanceof ICurrency cur && ShopEditorState.get().isDefaultCurrency(cur.getId()))
                && !(target instanceof CurrencyDraft draft && ShopEditorState.get().isDefaultCurrency(draft.id()));

        // 1. Duplicate
        UnityStyles.addContextMenuItem(menu, "Duplicate", true, menuWidth, () -> {
            if (target instanceof CurrencyDraft draft) {
                ShopEditorState.get().duplicateCurrency(draft);
            } else if (target instanceof ICurrency cur) {
                ShopEditorState.get().duplicateCurrency(cur);
            }
            refreshView();
        });

        // 2. Delete
        UnityStyles.addContextMenuItem(menu, "Delete", canDelete, menuWidth, () -> {
            if (target instanceof CurrencyDraft draft) {
                ShopEditorState.get().deleteCurrency(draft);
            } else if (target instanceof ICurrency cur) {
                ShopEditorState.get().deleteCurrency(cur);
            }
            refreshView();
        });

        menu.openAt(x, y);
        overlay.addOverlay(menu);
        activeCurrencyContextMenu = menu;
    }

    private static OverlayLayer findOverlayLayer(Widget start) {
        Widget current = start;
        while (current != null) {
            if (current instanceof OverlayLayer layer) return layer;
            current = current.parent();
        }
        return ShopEditorScreen.getActiveOverlay();
    }

    public void scrollToCurrency(Object target) {
        if (target == null) return;
        if (!scrollToCurrencyInternal(target)) {
            this.pendingScrollCurrency = target;
        }
    }

    private boolean scrollToCurrencyInternal(Object target) {
        if (scrollView == null) return false;
        Widget card = currencyCards.get(target);
        if (card != null) {
            float targetY = card.layoutBounds().y();
            if (targetY > 0.0f || card.layoutBounds().height() > 0.0f) {
                scrollView.scrollTo(0.0f, Math.max(0.0f, targetY - 16.0f));
                return true;
            }
        }
        return false;
    }

    @Override
    public void tick(FrameContext context) {
        super.tick(context);
        if (pendingScrollCurrency != null && scrollView != null) {
            if (scrollToCurrencyInternal(pendingScrollCurrency)) {
                pendingScrollCurrency = null;
            }
        }
    }
}
