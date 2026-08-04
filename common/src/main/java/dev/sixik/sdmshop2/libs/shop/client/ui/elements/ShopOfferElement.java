package dev.sixik.sdmshop2.libs.shop.client.ui.elements;

import com.lowdragmc.lowdraglib.utils.Size;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import dev.sixik.sdmshop2.SDMShop2;
import dev.sixik.sdmshop2.libs.shop.base.ShopOffer;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.ShopUiElement;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.ShopUIUtils;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.WidgetContextRender;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.WidgetRender;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.StyleApi;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.UIDisposable;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.UIEventScope;
import dev.sixik.sdmshop2.libs.shop.editor.ShopEditSession;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.containers.ContextMenuWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class ShopOfferElement extends WidgetGroup implements ShopUiElement, WidgetContextRender, UIDisposable {

    @Nullable
    private final ShopOffer shopEntity;
    @Nullable
    private final ShopEditSession editSession;
    @Nullable
    private final Runnable emptyAction;
    @Nullable
    private final Component emptyActionTitle;

    protected final WidgetRender render;
    protected final UIEventScope eventScope = new UIEventScope();

    public ShopOfferElement(@Nullable ShopOffer shopEntity) {
        this(null, shopEntity, null, null, StyleApi.getDefaultStyle(StyleApi.Category.Offers).get());
    }

    public ShopOfferElement(@Nullable ShopEditSession editSession, @Nullable ShopOffer shopEntity) {
        this(editSession, shopEntity, null, null, StyleApi.getDefaultStyle(StyleApi.Category.Offers).get());
    }

    public static ShopOfferElement editorAction(@Nullable ShopEditSession editSession, Component title, Runnable action) {
        return new ShopOfferElement(editSession, null, action, title, StyleApi.getDefaultStyle(StyleApi.Category.Offers).get());
    }

    public ShopOfferElement(@Nullable ShopOffer shopEntity, WidgetRender render) {
        this(null, shopEntity, null, null, render);
    }

    public ShopOfferElement(@Nullable ShopEditSession editSession, @Nullable ShopOffer shopEntity, WidgetRender render) {
        this(editSession, shopEntity, null, null, render);
    }

    protected ShopOfferElement(
            @Nullable ShopEditSession editSession,
            @Nullable ShopOffer shopEntity,
            @Nullable Runnable emptyAction,
            @Nullable Component emptyActionTitle,
            WidgetRender render
    ) {
        this.shopEntity = shopEntity;
        this.editSession = editSession;
        this.emptyAction = emptyAction;
        this.emptyActionTitle = emptyActionTitle;
        this.render = Objects.requireNonNull(render, "render");

        this.render.constructor(this);
        refresh(false);
    }

    @Override
    public void alightWidget() {
        render.alightWidgets(this);
    }

    public Size getMinimumLayoutSize() {
        return render.getMinimumSize(this);
    }

    public Size getPreferredLayoutSize() {
        return render.getPreferredSize(this);
    }

    public Size getMaximumLayoutSize() {
        return render.getMaximumSize(this);
    }

    public int getPreferredLayoutHeight(int width) {
        return render.getPreferredHeight(this, width);
    }

    @Override
    public void setSize(Size size) {
        super.setSize(size);
        if (!getContainedWidgets(true).isEmpty()) {
            alightWidget();
        }
    }

    /**
     * Пересобирает внутренние виджеты карточки из текущего состояния {@link ShopOffer}.
     * Удобно вызывать после редактирования компонентов, чтобы обновить текст, цену, предметы и бейджи.
     *
     * @return этот же виджет для цепочки вызовов
     */
    public ShopOfferElement refresh() {
        return refresh(true);
    }

    /**
     * Пересобирает внутренние виджеты карточки и опционально сразу применяет layout.
     *
     * @param relayout если true, после пересборки сразу вызывается {@link #alightWidget()}
     * @return этот же виджет для цепочки вызовов
     */
    public ShopOfferElement refresh(boolean relayout) {
        eventScope.clear();
        ShopUIUtils.disposeChildren(this);
        clearAllWidgets();
        render.addWidgets(this);

        if (relayout) {
            alightWidget();
        }

        return this;
    }

    @Override
    public UIEventScope eventScope() {
        return eventScope;
    }

    @Override
    public void dispose() {
        eventScope.close();
        ShopUIUtils.disposeChildren(this);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (render.mouseClicked(this, mouseX, mouseY, button)) {
            return true;
        }

        if (button != 1 || !isEditContextMenuEnabled()) {
            return super.mouseClicked(mouseX, mouseY, button);
        }

        if(!isMouseOverElement(mouseX, mouseY)) {
            return false;
        }

        ContextMenuWidget menuWidget = new ContextMenuWidget((int) mouseX, (int) mouseY, 120);
        menuWidget.setScale(0.7f);

        addDefaultContextMenu(menuWidget);
        render.addContext(menuWidget);

        if(!menuWidget.getContainedWidgets(true).isEmpty())
            ContextMenuWidget.open(this, menuWidget);

        return true;
    }

    protected void addDefaultContextMenu(ContextMenuWidget menuWidget) {
        if (shopEntity == null || !isEditContextMenuEnabled()) {
            return;
        }

        menuWidget
                .addItem(Component.translatable("shop.ui.offer_element.context_menu.edit"), () -> {
                    ShopUIUtils.createEditMenu(this, editSession, shopEntity, this::refresh);
                }).addSeparator()
                .addItem(Component.translatable("shop.ui.offer_element.context_menu.copy"), () -> {
                    copyOfferJsonToClipboard();
                }).addSeparator()
                .addItem(Component.translatable("shop.ui.common.duplicate"), () -> {
                    duplicateOffer();
                }).addSeparator()
                .addItem(Component.translatable("shop.ui.offer_element.context_menu.delete"), () -> {
                    if (isEditContextMenuEnabled() && ShopScreenElement.Instance != null) {
                        ShopScreenElement.Instance.deleteDraftOffer(shopEntity.getUUID());
                    }
                });
    }

    protected boolean isEditContextMenuEnabled() {
        return editSession != null && !editSession.closed();
    }

    protected void copyOfferJsonToClipboard() {
        if (shopEntity == null) {
            return;
        }

        String formattedJson = SDMShop2.GSON.toJson(shopEntity.serialize());
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.keyboardHandler.setClipboard(formattedJson);
        if (minecraft.player != null) {
            minecraft.player.sendSystemMessage(Component.translatable("client.shop.component.editor.copied"));
        }
    }

    protected void duplicateOffer() {
        if (shopEntity == null || !isEditContextMenuEnabled() || ShopScreenElement.Instance == null) {
            return;
        }

        ShopOffersPanelElement panel = findOffersPanel();
        if (panel != null) {
            panel.duplicateDraftOffer(shopEntity);
            return;
        }

        ShopScreenElement.Instance.duplicateDraftOffer(shopEntity.getUUID());
    }

    protected @Nullable ShopOffersPanelElement findOffersPanel() {
        Widget current = this;
        while (current != null) {
            if (current instanceof ShopOffersPanelElement panel) {
                return panel;
            }
            current = current.getParent();
        }

        return null;
    }

    @Override
    @Nullable
    public ShopOffer getShopEntity() {
        return shopEntity;
    }

    @Override
    public @Nullable ShopEditSession getEditSession() {
        return editSession;
    }

    @Nullable
    public Runnable getEmptyAction() {
        return emptyAction;
    }

    public Component getEmptyActionTitle() {
        return emptyActionTitle == null ? Component.translatable("shop.ui.offers.button.add_offer") : emptyActionTitle;
    }

    @Override
    public Widget getOwner() {
        return this;
    }
}
