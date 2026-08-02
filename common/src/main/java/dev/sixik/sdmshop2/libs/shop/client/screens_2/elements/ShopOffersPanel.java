package dev.sixik.sdmshop2.libs.shop.client.screens_2.elements;

import com.lowdragmc.lowdraglib.gui.texture.ColorRectTexture;
import com.lowdragmc.lowdraglib.utils.Size;
import dev.sixik.sdmshop2.libs.shop.base.ShopOffer;
import dev.sixik.sdmshop2.libs.shop.client.screens_2.elements.base.ShopDraggableScrollableWidgetGroup;
import dev.sixik.sdmshop2.libs.shop.client.ui.elements.ShopOfferElement;
import dev.sixik.sdmshop2.libs.shop.components.misc.ShopOffersContainerComponent;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class ShopOffersPanel extends ShopDraggableScrollableWidgetGroup implements ShopUiElement {

    private static final int DEFAULT_ITEM_HEIGHT = 90;
    private static final int SPACING = 5;
    private static final int PADDING = 5;
    private static final int SCROLLBAR_WIDTH = 3;

    @Getter
    protected final @NotNull ShopScreen shopScreen;

    public ShopOffersPanel(@NotNull ShopScreen shopScreen) {
        this.shopScreen = shopScreen;
        setBackground(new ColorRectTexture(0xff1fafa0).setRadius(6));
    }

    @Override
    public void initWidget() {
        setYScrollBarWidth(SCROLLBAR_WIDTH).setYBarStyle(null, new ColorRectTexture(-1));
        super.initWidget();
    }

    @Override
    public void alightWidget() {
        rebuildOffers();

        if (!initialized) return;

        for (int i = 0; i < widgets.size(); i++) {
            if (widgets.get(i) instanceof ShopUiElement element) {
                element.alightWidget();
            }
        }
    }

    @Override
    protected void alightWidgets() {
        alightWidget();
    }

    public void rebuildOffers() {
        clearAllWidgets();

        final ShopOffersContainerComponent entriesContainer = shopScreen.getEntriesContainer();
        for (ShopOffer value : entriesContainer.getEntryMap().values()) {
            addWidget(new ShopOfferElement(value));
        }

        alightOffers();
    }

    public void alightOffers() {
        if (!initialized || widgets.isEmpty()) return;

        final ShopOfferElement firstOfferElement = widgets.get(0) instanceof ShopOfferElement offerElement
                ? offerElement
                : null;
        final LayoutConstraints layoutConstraints = resolveLayoutConstraints();

        int availableWidth = Math.max(1, getSizeWidth() - SCROLLBAR_WIDTH - PADDING * 2);
        int minWidth = layoutConstraints.minWidth();
        int preferredWidth = layoutConstraints.preferredWidth();
        int maxWidth = layoutConstraints.maxWidth();

        int columns = chooseColumns(availableWidth, widgets.size(), minWidth, preferredWidth, maxWidth);

        int itemWidth = Math.max(1, (availableWidth - (columns - 1) * SPACING) / columns);
        itemWidth = Math.min(itemWidth, maxWidth);

        int itemHeight = firstOfferElement == null
                ? DEFAULT_ITEM_HEIGHT
                : firstOfferElement.getPreferredLayoutHeight(itemWidth);
        itemHeight = Math.max(1, itemHeight);

        int rowCount = Math.max(1, (widgets.size() + columns - 1) / columns);
        int[] rowHeights = new int[rowCount];
        for (int i = 0; i < widgets.size(); i++) {
            int height = itemHeight;
            if (widgets.get(i) instanceof ShopOfferElement offerElement) {
                height = Math.max(1, offerElement.getPreferredLayoutHeight(itemWidth));
            }

            int row = i / columns;
            rowHeights[row] = Math.max(rowHeights[row], height);
        }

        int totalGridWidth = columns * itemWidth + (columns - 1) * SPACING;
        int startX = PADDING + Math.max(0, (availableWidth - totalGridWidth) / 2);

        int y = PADDING;
        for (int i = 0; i < widgets.size(); i++) {
            var widget = widgets.get(i);

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

    private LayoutConstraints resolveLayoutConstraints() {
        int minWidth = 120;
        int preferredWidth = 160;
        int maxWidth = 240;
        boolean hasOfferElement = false;

        for (var widget : widgets) {
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
