package dev.sixik.sdmshop2.libs.shop.client.ui.style;

import com.lowdragmc.lowdraglib.gui.texture.ColorRectTexture;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.utils.Size;
import dev.sixik.sdmshop2.libs.shop.base.ShopOffer;
import dev.sixik.sdmshop2.libs.shop.client.screens_2.elements.ShopUiElement;
import dev.sixik.sdmshop2.libs.shop.client.screens_2.elements.base.ShopDraggableScrollableWidgetGroup;
import dev.sixik.sdmshop2.libs.shop.client.textures.PixelBevelTexture;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.WidgetContextRender;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.WidgetRender;
import dev.sixik.sdmshop2.libs.shop.client.ui.elements.ShopOfferElement;
import dev.sixik.sdmshop2.libs.shop.client.ui.elements.ShopOffersPanelElement;
import dev.sixik.sdmshop2.libs.shop.components.misc.ShopOffersContainerComponent;

import java.util.ArrayList;
import java.util.List;

public class DefaultShopOffersPanelRender implements WidgetRender {

    private static final int DEFAULT_ITEM_HEIGHT = 90;
    private static final int SPACING = 5;
    private static final int PADDING = 5;
    private static final int SCROLLBAR_WIDTH = 3;

    @Override
    public void constructor(WidgetContextRender ctx) {
        Widget owner = ctx.getOwner();
        owner.setBackground(new PixelBevelTexture(
                PixelBevelTexture.PANEL_COLOR,
                PixelBevelTexture.LINE_LOW_COLOR,
                PixelBevelTexture.LINE_HIGH_COLOR,
                1.5f
        ));

        if (owner instanceof ShopDraggableScrollableWidgetGroup scrollableWidgetGroup) {
            scrollableWidgetGroup
                    .setYScrollBarWidth(SCROLLBAR_WIDTH)
                    .setYBarStyle(null, new ColorRectTexture(-1));
        }
    }

    @Override
    public void addWidgets(WidgetContextRender ctx) {
        if (!(ctx instanceof ShopOffersPanelElement panel)) {
            return;
        }

        final ShopOffersContainerComponent entriesContainer = panel.getShopScreen().getEntriesContainer();
        for (ShopOffer value : entriesContainer.getEntryMap().values()) {
            ctx.addWidget(new ShopOfferElement(value));
        }
    }

    @Override
    public void alightWidgets(WidgetContextRender ctx) {
        if (!(ctx.getOwner() instanceof WidgetGroup group)) {
            return;
        }

        List<Widget> offerWidgets = collectOfferWidgets(group);
        if (offerWidgets.isEmpty()) {
            return;
        }

        final ShopOfferElement firstOfferElement = offerWidgets.get(0) instanceof ShopOfferElement offerElement
                ? offerElement
                : null;
        final LayoutConstraints layoutConstraints = resolveLayoutConstraints(offerWidgets);

        int availableWidth = Math.max(1, group.getSizeWidth() - SCROLLBAR_WIDTH - PADDING * 2);
        int minWidth = layoutConstraints.minWidth();
        int preferredWidth = layoutConstraints.preferredWidth();
        int maxWidth = layoutConstraints.maxWidth();

        int columns = chooseColumns(availableWidth, offerWidgets.size(), minWidth, preferredWidth, maxWidth);

        int itemWidth = Math.max(1, (availableWidth - (columns - 1) * SPACING) / columns);
        itemWidth = Math.min(itemWidth, maxWidth);

        int itemHeight = firstOfferElement == null
                ? DEFAULT_ITEM_HEIGHT
                : firstOfferElement.getPreferredLayoutHeight(itemWidth);
        itemHeight = Math.max(1, itemHeight);

        int rowCount = Math.max(1, (offerWidgets.size() + columns - 1) / columns);
        int[] rowHeights = new int[rowCount];
        for (int i = 0; i < offerWidgets.size(); i++) {
            int height = itemHeight;
            if (offerWidgets.get(i) instanceof ShopOfferElement offerElement) {
                height = Math.max(1, offerElement.getPreferredLayoutHeight(itemWidth));
            }

            int row = i / columns;
            rowHeights[row] = Math.max(rowHeights[row], height);
        }

        int totalGridWidth = columns * itemWidth + (columns - 1) * SPACING;
        int startX = PADDING + Math.max(0, (availableWidth - totalGridWidth) / 2);

        int y = PADDING;
        for (int i = 0; i < offerWidgets.size(); i++) {
            Widget widget = offerWidgets.get(i);

            int row = i / columns;
            int col = i % columns;
            int x = startX + col * (itemWidth + SPACING);

            if (col == 0 && row > 0) {
                y += rowHeights[row - 1] + SPACING;
            }

            widget.setSize(itemWidth, rowHeights[row]);
            widget.setSelfPosition(x, y);

            if (widget instanceof ShopUiElement element) {
                element.alightWidget();
            }
        }
    }

    private List<Widget> collectOfferWidgets(WidgetGroup group) {
        List<Widget> offerWidgets = new ArrayList<>();
        for (Widget widget : group.getContainedWidgets(true)) {
            if (widget instanceof ShopOfferElement) {
                offerWidgets.add(widget);
            }
        }
        return offerWidgets;
    }

    private LayoutConstraints resolveLayoutConstraints(List<Widget> offerWidgets) {
        int minWidth = 120;
        int preferredWidth = 160;
        int maxWidth = 240;
        boolean hasOfferElement = false;

        for (Widget widget : offerWidgets) {
            if (!(widget instanceof ShopOfferElement offerElement)) {
                continue;
            }

            Size minSize = offerElement.getMinimumLayoutSize();
            Size preferredSize = offerElement.getPreferredLayoutSize();
            Size maxSize = offerElement.getMaximumLayoutSize();

            int elementMinWidth = Math.max(1, minSize.width);
            int elementPreferredWidth = Math.max(elementMinWidth, preferredSize.width);
            int elementMaxWidth = maxSize.width <= 0
                    ? Integer.MAX_VALUE
                    : Math.max(elementMinWidth, maxSize.width);

            if (!hasOfferElement) {
                minWidth = elementMinWidth;
                preferredWidth = elementPreferredWidth;
                maxWidth = elementMaxWidth;
                hasOfferElement = true;
                continue;
            }

            minWidth = Math.max(minWidth, elementMinWidth);
            preferredWidth = Math.max(preferredWidth, elementPreferredWidth);
            maxWidth = Math.min(maxWidth, elementMaxWidth);
        }

        preferredWidth = Math.max(minWidth, preferredWidth);
        maxWidth = maxWidth == Integer.MAX_VALUE ? maxWidth : Math.max(minWidth, maxWidth);
        return new LayoutConstraints(minWidth, preferredWidth, maxWidth);
    }

    private int chooseColumns(int availableWidth, int itemCount, int minWidth, int preferredWidth, int maxWidth) {
        int maxColumns = Math.max(1, Math.min(itemCount, (availableWidth + SPACING) / (minWidth + SPACING)));
        int bestColumns = 1;
        int bestScore = Integer.MAX_VALUE;

        for (int columns = 1; columns <= maxColumns; columns++) {
            int rawWidth = Math.max(1, (availableWidth - (columns - 1) * SPACING) / columns);
            if (rawWidth < minWidth) {
                continue;
            }

            int width = Math.min(rawWidth, maxWidth);
            int score = Math.abs(width - preferredWidth);

            if (width >= minWidth && width <= maxWidth) {
                score -= 8;
            }

            if (columns > 1 && width >= preferredWidth) {
                score -= columns;
            }

            if (score < bestScore) {
                bestScore = score;
                bestColumns = columns;
            }
        }

        return Math.max(1, bestColumns);
    }

    private record LayoutConstraints(int minWidth, int preferredWidth, int maxWidth) {
    }
}
