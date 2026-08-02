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
import dev.sixik.sdmshop2.libs.shop.components.api.RewardComponent;
import dev.sixik.sdmshop2.libs.shop.components.api.ShopComponent;
import dev.sixik.sdmshop2.libs.shop.components.api.ShopComponentCategory;
import dev.sixik.sdmshop2.libs.shop.components.misc.NameComponent;
import dev.sixik.sdmshop2.libs.shop.components.misc.ShopOffersContainerComponent;
import dev.sixik.sdmshop2.libs.shop.components.money.MoneyCostComponent;
import dev.sixik.sdmshop2.libs.shop.components.utils.ShopComponentsUtils;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class DefaultShopOffersPanelRender implements WidgetRender {

    protected static final int DEFAULT_ITEM_HEIGHT = 90;
    protected static final int SPACING = 5;
    protected static final int PADDING = 5;
    protected static final int SCROLLBAR_WIDTH = 3;

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
        final String searchText = normalizeSearch(panel.getSearchText());
        List<ShopOffer> offers = entriesContainer.getEntryMap().values().stream()
                .map(OfferView::from)
                .filter(view -> searchText.isEmpty() || view.searchTitle().contains(searchText))
                .sorted(Comparator
                        .comparing(OfferView::sortTitle)
                        .thenComparing(view -> view.offer().getUUID()))
                .map(OfferView::offer)
                .collect(Collectors.toCollection(ArrayList::new));

        panel.applyCustomSort(offers);
        for (ShopOffer offer : offers) {
            ctx.addWidget(new ShopOfferElement(offer));
        }
    }

    protected static String normalizeSearch(String value) {
        return value == null ? "" : value.trim().toLowerCase(Locale.ROOT);
    }

    protected static String resolveTitle(ShopOffer offer) {
        return offer.getComponent(NameComponent.class)
                .map(NameComponent::getName)
                .filter(name -> !name.isBlank())
                .map(DefaultShopOffersPanelRender::resolveDisplayText)
                .orElseGet(() -> resolveRewardTitle(offer));
    }

    protected static String resolveRewardTitle(ShopOffer offer) {
        Map<ShopComponentCategory, ObjectList<ShopComponent>> components = ShopComponentsUtils.getComponentsByCategory(offer);
        ObjectList<ShopComponent> rewards = components.get(ShopComponentCategory.REWARD);
        if (rewards != null && !rewards.isEmpty() && rewards.get(0) instanceof RewardComponent rewardComponent) {
            Component display = rewardComponent.getDisplayTitle();
            if (display != null && !display.getString().isBlank()) {
                return display.getString();
            }
        }

        return offer.getUUID().toString();
    }

    protected static String resolveDisplayText(String value) {
        return I18n.exists(value) ? Component.translatable(value).getString() : value;
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

    protected List<Widget> collectOfferWidgets(WidgetGroup group) {
        List<Widget> offerWidgets = new ArrayList<>();
        for (Widget widget : group.getContainedWidgets(true)) {
            if (widget instanceof ShopOfferElement) {
                offerWidgets.add(widget);
            }
        }
        return offerWidgets;
    }

    protected LayoutConstraints resolveLayoutConstraints(List<Widget> offerWidgets) {
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

    protected int chooseColumns(int availableWidth, int itemCount, int minWidth, int preferredWidth, int maxWidth) {
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

    protected record LayoutConstraints(int minWidth, int preferredWidth, int maxWidth) {
    }

    protected record OfferView(ShopOffer offer, String title, String sortTitle, String searchTitle) {

        protected static OfferView from(ShopOffer offer) {
            String title = resolveTitle(offer);
            String normalized = title.toLowerCase(Locale.ROOT);
            return new OfferView(offer, title, normalized, normalized);
        }
    }
}
