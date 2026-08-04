package dev.sixik.sdmshop2.libs.shop.client.ui.style;

import com.lowdragmc.lowdraglib.gui.editor.ColorPattern;
import com.lowdragmc.lowdraglib.gui.util.ClickData;
import com.lowdragmc.lowdraglib.gui.widget.DraggableScrollableWidgetGroup;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.gui.widget.layout.Layout;
import com.lowdragmc.lowdraglib.utils.Size;
import com.google.gson.JsonObject;
import dev.sixik.sdmshop2.libs.shop.base.ObjectIdGetter;
import dev.sixik.sdmshop2.libs.shop.base.ShopEntity;
import dev.sixik.sdmshop2.libs.shop.base.ShopOffer;
import dev.sixik.sdmshop2.libs.shop.client.config.component_selector.ModalComponentSelectionMenu;
import dev.sixik.sdmshop2.libs.shop.client.config.constructors.ComponentConfigWidgetConstructor;
import dev.sixik.sdmshop2.libs.shop.client.ui.textures.ColorRectAndBorderTexture;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.ShopUIUtils;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.WidgetContextRender;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.WidgetRender;
import dev.sixik.sdmshop2.libs.shop.client.ui.elements.ShopOfferElement;
import dev.sixik.sdmshop2.libs.shop.components.api.ShopComponent;
import dev.sixik.sdmshop2.libs.shop.components.api.ShopComponentRegistry;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.ButtonWidget;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.containers.ModalWidget;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;

public class DefaultEditMenuRender implements WidgetRender {

    protected static final int PANEL_PADDING = 2;
    protected static final int SCROLLBAR_WIDTH = 4;
    protected static final int MIN_CONTENT_WIDTH = 140;
    protected static final int COLUMN_GAP = 4;
    protected static final int ACTION_PANEL_WIDTH = 94;
    protected static final int MIN_ACTION_PANEL_WIDTH = 68;
    protected static final int MIN_PREVIEW_WIDTH = 96;
    protected static final int MAX_PREVIEW_HEIGHT = 200;
    protected static final int PREVIEW_EDGE_GAP = 4;

    public DefaultEditMenuRender() { }

    protected WidgetGroup container;
    protected DraggableScrollableWidgetGroup scrollPanel;
    protected WidgetGroup contentWrapper;
    protected ShopEntity renderedEntity;
    protected int renderedContentWidth = -1;

    protected WidgetGroup actionContainer;
    protected DraggableScrollableWidgetGroup actionScrollPanel;
    protected WidgetGroup actionWrapper;
    protected ShopEntity renderedActionEntity;
    protected int renderedActionWidth = -1;

    protected WidgetGroup previewContainer;
    protected ShopOfferElement previewElement;
    protected ShopEntity previewEntity;

    @Override
    public void constructor(WidgetContextRender ctx) {

    }

    @Override
    public void addWidgets(WidgetContextRender ctx) {
        container = new WidgetGroup();
        container.setLayout(Layout.NONE);
        container.setBackground(new ColorRectAndBorderTexture());
        ctx.addWidget(container);

        scrollPanel = new DraggableScrollableWidgetGroup();
        scrollPanel.setScrollWheelDirection(DraggableScrollableWidgetGroup.ScrollWheelDirection.VERTICAL);
        scrollPanel.setLayout(Layout.NONE);
        scrollPanel.setYScrollBarWidth(SCROLLBAR_WIDTH);
        scrollPanel.setYBarStyle(null, ColorPattern.WHITE.rectTexture().setRadius(2));
        scrollPanel.setClientSideWidget();
        container.addWidget(scrollPanel);

        contentWrapper = new WidgetGroup(0, 0, MIN_CONTENT_WIDTH, 0);
        contentWrapper.setLayout(Layout.VERTICAL_LEFT);
        contentWrapper.setLayoutPadding(2);
        contentWrapper.setDynamicSized(true);
        scrollPanel.addWidget(contentWrapper);

        actionContainer = new WidgetGroup();
        actionContainer.setLayout(Layout.NONE);
        actionContainer.setBackground(new ColorRectAndBorderTexture());
        ctx.addWidget(actionContainer);

        actionScrollPanel = new DraggableScrollableWidgetGroup();
        actionScrollPanel.setScrollWheelDirection(DraggableScrollableWidgetGroup.ScrollWheelDirection.VERTICAL);
        actionScrollPanel.setLayout(Layout.NONE);
        actionScrollPanel.setYScrollBarWidth(SCROLLBAR_WIDTH);
        actionScrollPanel.setYBarStyle(null, ColorPattern.WHITE.rectTexture().setRadius(2));
        actionScrollPanel.setClientSideWidget();
        actionContainer.addWidget(actionScrollPanel);

        actionWrapper = new WidgetGroup(0, 0, MIN_ACTION_PANEL_WIDTH, 0);
        actionWrapper.setLayout(Layout.VERTICAL_LEFT);
        actionWrapper.setLayoutPadding(2);
        actionWrapper.setDynamicSized(true);
        actionScrollPanel.addWidget(actionWrapper);

        previewContainer = new WidgetGroup();
        previewContainer.setLayout(Layout.NONE);
        previewContainer.setBackground(new ColorRectAndBorderTexture());
        if (ctx.getOwner() instanceof ModalWidget modal) {
            modal.getPanel().addWidget(previewContainer);
        } else {
            ctx.addWidget(previewContainer);
        }

        rebuildContent(ctx, MIN_CONTENT_WIDTH);
        rebuildActions(ctx, MIN_ACTION_PANEL_WIDTH);
        rebuildPreview(ctx);
    }

    @Override
    public void alightWidgets(WidgetContextRender ctx) {
        final Widget root = ctx.getOwner();

        final ModalWidget modal = (ModalWidget) root;
        final WidgetGroup modal_panel = modal.getPanel();
        final Size root_size = modal_panel.getSize();

        final int h = root_size.getHeight();

        final ButtonWidget closeButton = modal.getCloseButton();

        final int panelH = closeButton == null ? 0 : closeButton.getSelfPositionY() + closeButton.getSizeHeight();
        final int contentY = panelH + 2;
        final int contentHeight = Math.max(1, h - panelH - 12);
        final int availableWidth = Math.max(1, root_size.getWidth() - PANEL_PADDING * 2);

        int actionWidth = Math.min(ACTION_PANEL_WIDTH, Math.max(MIN_ACTION_PANEL_WIDTH, availableWidth / 5));
        int containerWidth = availableWidth - actionWidth - COLUMN_GAP;

        if (containerWidth < MIN_CONTENT_WIDTH) {
            int deficit = MIN_CONTENT_WIDTH - containerWidth;

            final int actionShrink = Math.min(deficit, Math.max(0, actionWidth - MIN_ACTION_PANEL_WIDTH));
            actionWidth -= actionShrink;
            deficit -= actionShrink;

            containerWidth = Math.max(1, availableWidth - actionWidth - COLUMN_GAP);
        }

        containerWidth = Math.max(1, containerWidth - 8);
        final int containerHeight = Math.max(1, h - panelH - 12);
        container.setSize(containerWidth, containerHeight);
        container.setSelfPosition(PANEL_PADDING, contentY);

        final int scrollWidth = Math.max(1, containerWidth - PANEL_PADDING * 2);
        final int scrollHeight = Math.max(1, containerHeight - PANEL_PADDING * 2);
        scrollPanel.setSelfPosition(PANEL_PADDING, PANEL_PADDING);
        scrollPanel.setSize(scrollWidth, scrollHeight);

        final int contentWidth = Math.max(MIN_CONTENT_WIDTH, scrollWidth - SCROLLBAR_WIDTH - PANEL_PADDING);
        if (contentWidth != renderedContentWidth || renderedEntity != ctx.getShopEntity()) {
            rebuildContent(ctx, contentWidth);
        } else {
            contentWrapper.setSizeWidth(contentWidth);
        }

        final int actionX = container.getSelfPositionX() + containerWidth + COLUMN_GAP;
        actionContainer.setSize(actionWidth, contentHeight);
        actionContainer.setSelfPosition(actionX, contentY);

        final int actionScrollWidth = Math.max(1, actionWidth - PANEL_PADDING * 2);
        final int actionScrollHeight = Math.max(1, contentHeight - PANEL_PADDING * 2);
        actionScrollPanel.setSelfPosition(PANEL_PADDING, PANEL_PADDING);
        actionScrollPanel.setSize(actionScrollWidth, actionScrollHeight);

        final int actionContentWidth = Math.max(1, actionScrollWidth - SCROLLBAR_WIDTH - PANEL_PADDING);
        if (actionContentWidth != renderedActionWidth || renderedActionEntity != ctx.getShopEntity()) {
            rebuildActions(ctx, actionContentWidth);
        } else {
            actionWrapper.setSizeWidth(actionContentWidth);
        }

        final int panelRight = modal_panel.getSelfPositionX() + root_size.getWidth();
        final int screenWidth = Math.max(1, modal.getSizeWidth());
        final int previewAvailableWidth = screenWidth - panelRight - PREVIEW_EDGE_GAP * 2;
        final boolean showPreview = previewAvailableWidth >= MIN_PREVIEW_WIDTH && ctx.getShopEntity() instanceof ShopOffer;
        previewContainer.setVisible(showPreview);
        previewContainer.setActive(false);

        if (showPreview) {
            final int previewWidth = Math.max(1, previewAvailableWidth);
            final int previewHeight = Math.max(1, Math.min(MAX_PREVIEW_HEIGHT, contentHeight));
            final int previewX = root_size.getWidth() + PREVIEW_EDGE_GAP;
            previewContainer.setSize(previewWidth, previewHeight);
            previewContainer.setSelfPosition(previewX, contentY);

            if (previewEntity != ctx.getShopEntity()) {
                rebuildPreview(ctx);
            }
            alightPreview();
        } else {
            previewContainer.setSize(1, 1);
            previewContainer.setSelfPosition(root_size.getWidth() + PREVIEW_EDGE_GAP, contentY);
            if (previewElement != null || previewEntity != ctx.getShopEntity()) {
                rebuildPreview(ctx);
            }
        }
    }

    protected void rebuildContent(WidgetContextRender ctx, int contentWidth) {
        if (contentWrapper == null) return;

        renderedEntity = ctx.getShopEntity();
        renderedContentWidth = Math.max(1, contentWidth);

        ShopUIUtils.disposeChildren(contentWrapper);
        contentWrapper.clearAllWidgets();
        contentWrapper.setSizeWidth(renderedContentWidth);
        contentWrapper.setSelfPosition(0, 0);

        if (renderedEntity != null) {
            ComponentConfigWidgetConstructor.createShopOfferWidget(contentWrapper, renderedEntity, renderedContentWidth);
        }
    }

    public void refreshContent(WidgetContextRender ctx) {
        int previousScrollY = scrollPanel == null ? 0 : scrollPanel.getScrollYOffset();
        if (scrollPanel != null) {
            scrollPanel.setScrollYOffset(0);
        }

        rebuildContent(ctx, renderedContentWidth > 0 ? renderedContentWidth : MIN_CONTENT_WIDTH);

        if (scrollPanel != null && contentWrapper != null) {
            int maxScrollY = Math.max(0, contentWrapper.getSizeHeight() - scrollPanel.getSizeHeight());
            scrollPanel.setScrollYOffset(Math.min(previousScrollY, maxScrollY));
        }

        rebuildActions(ctx, renderedActionWidth > 0 ? renderedActionWidth : MIN_ACTION_PANEL_WIDTH);
        refreshPreview(ctx);
        alightWidgets(ctx);
    }

    protected void rebuildActions(WidgetContextRender ctx, int actionWidth) {
        if (actionWrapper == null) return;

        renderedActionEntity = ctx.getShopEntity();
        renderedActionWidth = Math.max(1, actionWidth);

        ShopUIUtils.disposeChildren(actionWrapper);
        actionWrapper.clearAllWidgets();
        actionWrapper.setSizeWidth(renderedActionWidth);
        actionWrapper.setSelfPosition(0, 0);

        final ButtonWidget addComponent = createActionButton(
                renderedActionWidth,
                Component.translatable("shop.ui.editor.button.add_component"),
                ignored -> openComponentSelector(ctx)
        );
        addComponent.setActive(renderedActionEntity != null);
        actionWrapper.addWidget(addComponent);

        final ButtonWidget refreshPreview = createActionButton(
                renderedActionWidth,
                Component.translatable("shop.ui.common.refresh"),
                ignored -> {
                    rebuildContent(ctx, renderedContentWidth);
                    refreshPreview(ctx);
                }
        );
        refreshPreview.setActive(renderedActionEntity != null);
        actionWrapper.addWidget(refreshPreview);
    }

    protected ButtonWidget createActionButton(int width, Component text, Consumer<ClickData> onPress) {
        final ButtonWidget button = new ButtonWidget(0, 0, Math.max(1, width), 20, text, onPress);
        button.setClientSideWidget();
        button.setButtonColors(0xFF2A2A36, 0xFF3D3D4E);
        button.setHoverColors(0xFF34384A, 0xFF7986CB);
        button.setClickedColors(0xFF202331, 0xFF7986CB);
        button.setTextColor(0xFFFFFFFF);
        button.setDisabledTextColor(0xFF8A8A8A);
        return button;
    }

    protected void openComponentSelector(WidgetContextRender ctx) {
        if (renderedEntity == null) {
            return;
        }

        final Widget owner = ctx.getOwner();
        final WidgetGroup parent;
        if (owner instanceof ModalWidget modal && modal.getAttachedParent() != null) {
            parent = modal.getAttachedParent();
        } else if (owner instanceof WidgetGroup ownerGroup) {
            parent = ownerGroup;
        } else {
            return;
        }

        ModalComponentSelectionMenu.showComponentSelector(parent, component -> addComponent(ctx, component));
    }

    protected void addComponent(WidgetContextRender ctx, ShopComponent component) {
        if (component == null || renderedEntity == null) {
            return;
        }

        renderedEntity.addComponent(component);
        if (ctx.getEditSession() != null) {
            ctx.getEditSession().recordHistory(
                    "component.add",
                    renderedEntity.getClass().getSimpleName(),
                    renderedEntity instanceof dev.sixik.sdmshop2.libs.shop.base.ObjectIdGetter idGetter ? idGetter.getUUID() : null,
                    component.getType().getId(),
                    "Added component " + component.getType().getId()
            );
        }
        ComponentConfigWidgetConstructor.invokeUpdate(component, false);
        rebuildContent(ctx, renderedContentWidth);
        rebuildActions(ctx, renderedActionWidth);
        refreshPreview(ctx);
    }

    public boolean duplicateComponent(WidgetContextRender ctx, ShopComponent component) {
        ShopEntity entity = ctx == null ? renderedEntity : ctx.getShopEntity();
        if (entity == null || component == null || entity.indexOfComponent(component) < 0) {
            return false;
        }

        JsonObject json = ShopComponentRegistry.toJson(component).deepCopy();
        ShopComponent copy = ShopComponentRegistry.fromJson(json);
        int targetIndex = entity.indexOfComponent(component) + 1;
        entity.addComponent(copy);
        entity.moveComponentToIndex(copy, targetIndex);

        if (ctx != null && ctx.getEditSession() != null && !ctx.getEditSession().closed()) {
            ctx.getEditSession().recordHistory(
                    "component.duplicate",
                    entity.getClass().getSimpleName(),
                    entity instanceof ObjectIdGetter idGetter ? idGetter.getUUID() : null,
                    component.getType().getId(),
                    "Duplicated component " + component.getType().getId()
            );
        }

        ComponentConfigWidgetConstructor.invokeUpdate(copy, false);
        refreshContent(ctx);
        return true;
    }

    protected void refreshPreview(WidgetContextRender ctx) {
        if (previewElement != null && previewEntity == ctx.getShopEntity()) {
            previewElement.refresh(false);
        } else {
            rebuildPreview(ctx);
        }

        alightPreview();
    }

    protected void rebuildPreview(WidgetContextRender ctx) {
        if (previewContainer == null) return;

        previewEntity = ctx.getShopEntity();
        previewElement = null;
        ShopUIUtils.disposeChildren(previewContainer);
        previewContainer.clearAllWidgets();

        if (previewEntity instanceof ShopOffer offer) {
            previewElement = new ShopOfferElement(offer);
            previewElement.setClientSideWidget();
            previewElement.setActive(false);
            previewContainer.addWidget(previewElement);
        }
    }

    protected void alightPreview() {
        if (previewContainer == null || previewElement == null || !previewContainer.isVisible()) {
            return;
        }

        final int previewWidth = Math.max(1, previewContainer.getSizeWidth() - PANEL_PADDING * 2);
        final int previewHeight = Math.max(1, previewContainer.getSizeHeight() - PANEL_PADDING * 2);
        previewElement.setSelfPosition(PANEL_PADDING, PANEL_PADDING);
        previewElement.setSize(previewWidth, previewHeight);
        previewElement.alightWidget();
    }

}
