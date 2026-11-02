package dev.sixik.sdmshop2.libs.shop.client.ui.elements;

import dev.sixik.sdmshop2.SDMShop2;
import dev.sixik.sdmshop2.libs.shop.base.ShopOffer;
import dev.sixik.sdmshop2.libs.shop.client.cache.ShopClientCache;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.ShopUIUtils;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.UIDisposable;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.UIEventScope;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.WidgetContextRender;
import dev.sixik.sdmshop2.libs.shop.client.ui.events.ShopUIEvents;
import dev.sixik.sdmshop2.libs.shop.client.ui.widgets.base.ShopWidgetGroup;
import dev.sixik.sdmshop2.libs.shop.components.api.CostComponent;
import dev.sixik.sdmshop2.libs.shop.components.api.RewardComponent;
import dev.sixik.sdmshop2.libs.shop.components.misc.NameComponent;
import dev.sixik.sdmshop2.libs.shop.editor.ShopEditSession;
import dev.sixik.unigui.api.layout.EdgeInsets;
import dev.sixik.unigui.api.layout.LayoutSize;
import dev.sixik.unigui.api.math.MutableColor;
import dev.sixik.unigui.api.widget.Widget;
import dev.sixik.unigui.widgets.containers.HBox;
import dev.sixik.unigui.widgets.containers.VBox;
import dev.sixik.unigui.widgets.display.Label;
import dev.sixik.unigui.widgets.interaction.Button;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.stream.Collectors;

/** A native UniGUI offer card. */
public class ShopOfferElement extends ShopWidgetGroup implements WidgetContextRender, UIDisposable {
    private final @Nullable ShopOffer shopEntity;
    private final @Nullable ShopEditSession editSession;
    private final @Nullable Runnable emptyAction;
    private final @Nullable Component emptyActionTitle;
    private final UIEventScope eventScope = new UIEventScope();

    public ShopOfferElement(@Nullable ShopOffer offer) { this(null, offer, null, null); }
    public ShopOfferElement(@Nullable ShopEditSession session, @Nullable ShopOffer offer) { this(session, offer, null, null); }
    public static ShopOfferElement editorAction(@Nullable ShopEditSession session, Component title, Runnable action) {
        return new ShopOfferElement(session, null, action, title);
    }

    private ShopOfferElement(@Nullable ShopEditSession session, @Nullable ShopOffer offer, @Nullable Runnable action, @Nullable Component title) {
        this.editSession = session;
        this.shopEntity = offer;
        this.emptyAction = action;
        this.emptyActionTitle = title;
        background(MutableColor.fromArgb(0xF0242730));
        border(MutableColor.fromArgb(0xFF4D566A));
        radius(6.0f);
        layout(style -> style.height(112.0f).flexNone());
        refresh(false);
    }

    public LayoutSize getMinimumLayoutSize() { return LayoutSize.of(120, 90); }
    public LayoutSize getPreferredLayoutSize() { return LayoutSize.of(220, 112); }
    public LayoutSize getMaximumLayoutSize() { return LayoutSize.of(Float.MAX_VALUE, 160); }
    public int getPreferredLayoutHeight(int width) { return Math.max(90, Math.min(160, Math.round(width * 0.5f))); }

    public ShopOfferElement refresh() { return refresh(true); }
    public ShopOfferElement refresh(boolean relayout) {
        clearChildren();
        VBox root = new VBox();
        root.spacing(5.0f);
        root.layout(style -> style.fill().padding(EdgeInsets.all(8.0f)));
        if (shopEntity == null) {
            Button action = new Button(getEmptyActionTitle().getString());
            action.layout(style -> style.fill());
            action.onClick(event -> { if (emptyAction != null) emptyAction.run(); });
            root.addChild(action);
            addChild(root);
            return this;
        }

        HBox header = new HBox();
        header.spacing(5.0f);
        Label title = new Label(resolveTitle()).wrap(true);
        title.layout(style -> style.expand());
        header.addChild(title);
        Button favorite = new Button(ShopClientCache.isFavorite(shopEntity.getUUID()) ? "★" : "☆");
        favorite.layout(style -> style.fixed(24, 22));
        favorite.onClick(event -> { ShopClientCache.toggleFavorite(shopEntity.getUUID()); refresh(false); });
        header.addChild(favorite);
        root.addChild(header);

        String costs = shopEntity.getComponents(CostComponent.class).stream()
                .map(cost -> cost.getDisplayName().getString() + " " + format(cost.getBaseAmount()))
                .collect(Collectors.joining("  •  "));
        if (!costs.isBlank()) root.addChild(new Label(costs).wrap(true));

        HBox actions = new HBox();
        actions.spacing(5.0f);
        actions.layout(style -> style.expand());
        if (isEditContextMenuEnabled()) {
            actions.addChild(button(Component.translatable("shop.ui.offer_element.context_menu.edit").getString(), () -> ShopUIUtils.createEditMenu(this, editSession, shopEntity, this::refresh)));
            actions.addChild(button(Component.translatable("shop.ui.common.duplicate").getString(), this::duplicateOffer));
            actions.addChild(button(Component.translatable("shop.ui.common.delete").getString(), () -> {
                if (ShopScreenElement.Instance != null) ShopScreenElement.Instance.deleteDraftOffer(shopEntity.getUUID());
            }));
        } else {
            Button buy = button(Component.translatable("shop.ui.offer_element.buy").getString(), () -> ShopUIEvents.invokeBuyShopEntity(shopEntity, ""));
            buy.layout(style -> style.expand());
            actions.addChild(buy);
        }
        root.addChild(actions);
        addChild(root);
        return this;
    }

    private Button button(String text, Runnable action) {
        Button button = new Button(text);
        button.onClick(event -> action.run());
        return button;
    }

    private String resolveTitle() {
        return shopEntity.getComponent(NameComponent.class).map(NameComponent::getName).filter(value -> !value.isBlank())
                .map(value -> I18n.exists(value) ? Component.translatable(value).getString() : value)
                .orElseGet(() -> shopEntity.getComponents(RewardComponent.class).stream().map(RewardComponent::getDisplayTitle)
                        .filter(Objects::nonNull).map(Component::getString).filter(value -> !value.isBlank()).findFirst().orElse(shopEntity.getUUID().toString()));
    }
    private static String format(double value) { return String.format(java.util.Locale.ROOT, "%,.2f", value).replaceAll("\\.00$", ""); }
    protected boolean isEditContextMenuEnabled() { return editSession != null && !editSession.closed(); }

    protected void copyOfferJsonToClipboard() {
        if (shopEntity != null) Minecraft.getInstance().keyboardHandler.setClipboard(SDMShop2.GSON.toJson(shopEntity.serialize()));
    }
    protected void duplicateOffer() {
        if (shopEntity == null || !isEditContextMenuEnabled() || ShopScreenElement.Instance == null) return;
        ShopOffersPanelElement panel = findOffersPanel();
        if (panel != null) panel.duplicateDraftOffer(shopEntity); else ShopScreenElement.Instance.duplicateDraftOffer(shopEntity.getUUID());
    }
    protected @Nullable ShopOffersPanelElement findOffersPanel() {
        Widget current = this;
        while (current != null) { if (current instanceof ShopOffersPanelElement panel) return panel; current = current.parent(); }
        return null;
    }

    @Override public @Nullable ShopOffer getShopEntity() { return shopEntity; }
    @Override public @Nullable ShopEditSession getEditSession() { return editSession; }
    public @Nullable Runnable getEmptyAction() { return emptyAction; }
    public Component getEmptyActionTitle() { return emptyActionTitle == null ? Component.translatable("shop.ui.offers.button.add_offer") : emptyActionTitle; }
    @Override public Widget getOwner() { return this; }
    @Override public UIEventScope eventScope() { return eventScope; }
    @Override public void dispose() { eventScope.close(); super.dispose(); }
}
