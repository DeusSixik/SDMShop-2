package dev.sixik.sdmshop2.libs.shop.client.ui.theme;

import com.lowdragmc.lowdraglib.gui.editor.ColorPattern;
import com.lowdragmc.lowdraglib.gui.widget.DraggableScrollableWidgetGroup;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.gui.widget.layout.Layout;
import com.lowdragmc.lowdraglib.utils.Size;
import dev.sixik.sdmshop2.libs.shop.base.ShopEntity;
import dev.sixik.sdmshop2.libs.shop.client.config.constructors.ComponentConfigWidgetConstructor;
import dev.sixik.sdmshop2.libs.shop.client.textures.ColorRectAndBorderTexture;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.WidgetContextRender;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.WidgetRender;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.ButtonWidget;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.containers.ModalWidget;

public class DefaultEditMenuRender implements WidgetRender {

    private static final int PANEL_PADDING = 2;
    private static final int SCROLLBAR_WIDTH = 4;
    private static final int MIN_CONTENT_WIDTH = 140;

    public DefaultEditMenuRender() { }

    private WidgetGroup container;
    private DraggableScrollableWidgetGroup scrollPanel;
    private WidgetGroup contentWrapper;
    private ShopEntity renderedEntity;
    private int renderedContentWidth = -1;

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

        rebuildContent(ctx, MIN_CONTENT_WIDTH);
    }

    @Override
    public void alightWidgets(WidgetContextRender ctx) {
        final Widget root = ctx.getOwner();

        final ModalWidget modal = (ModalWidget) root;
        final WidgetGroup modal_panel = modal.getPanel();
        final Size root_size = modal_panel.getSize();

        final int w = root_size.getWidth() / 2;
        final int h = root_size.getHeight();

        final ButtonWidget closeButton = modal.getCloseButton();

        final int panelH = closeButton == null ? 0 : closeButton.getSelfPositionY() + closeButton.getSizeHeight();
        final int containerWidth = Math.max(1, w - 2);
        final int containerHeight = Math.max(1, h - panelH - 12);
        container.setSize(containerWidth, containerHeight);
        container.setSelfPosition(2, panelH + 2);

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
    }

    private void rebuildContent(WidgetContextRender ctx, int contentWidth) {
        if (contentWrapper == null) return;

        renderedEntity = ctx.getShopEntity();
        renderedContentWidth = Math.max(1, contentWidth);

        contentWrapper.clearAllWidgets();
        contentWrapper.setSizeWidth(renderedContentWidth);
        contentWrapper.setSelfPosition(0, 0);

        if (renderedEntity != null) {
            ComponentConfigWidgetConstructor.createShopOfferWidget(contentWrapper, renderedEntity, renderedContentWidth);
        }
    }

}
