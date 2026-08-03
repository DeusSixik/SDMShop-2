package dev.sixik.sdmshop2.libs.shop.client.ui.style;

import com.lowdragmc.lowdraglib.gui.texture.ColorRectTexture;
import com.lowdragmc.lowdraglib.gui.texture.TransformTexture;
import com.lowdragmc.lowdraglib.gui.widget.DraggableScrollableWidgetGroup;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.gui.widget.layout.Layout;
import com.lowdragmc.lowdraglib.utils.Size;
import dev.sixik.sdmshop2.libs.sdmeconomy.IExternalCurrency;
import dev.sixik.sdmshop2.libs.sdmeconomy.SDMEconomyServiceClient;
import dev.sixik.sdmshop2.libs.shop.client.ui.widgets.ShopEmptyWidget;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.ShopUiElement;
import dev.sixik.sdmshop2.libs.shop.client.ui.elements.ShopTabElement;
import dev.sixik.sdmshop2.libs.shop.client.ui.textures.PixelBevelTexture;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.WidgetContextRender;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.WidgetRender;
import dev.sixik.sdmshop2.libs.shop.client.ui.elements.ShopTabsPanelElement;
import dev.sixik.sdmshop2.libs.shop.components.misc.CatalogComponent;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.PriceWidget;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.TextLabel;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.containers.HorizontalContainer;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.containers.VerticalContainer;
import dev.sixik.sdmshop2.utils.ShopUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DefaultShopTabsPanelRender implements WidgetRender {

    protected static final int HORIZONTAL_PADDING = 6;
    protected static final int VERTICAL_PADDING = 6;
    protected static final int TAB_HEIGHT = 24;
    protected static final int SECTION_GAP = 6;
    protected static final int SCROLLBAR_WIDTH = 6;

    protected DraggableScrollableWidgetGroup tabsContainer;
    protected WidgetGroup tabsContent;
    protected WidgetGroup currenciesContainer;
    protected VerticalContainer currenciesVBox;
    protected TextLabel moneyCategoryTitle;

    protected final List<HorizontalContainer> currencyRows = new ArrayList<>();
    protected final Map<ResourceLocation, PriceWidget> currencyPriceWidgets = new LinkedHashMap<>();

    @Override
    public void constructor(WidgetContextRender ctx) {
        Widget owner = ctx.getOwner();
        owner.setBackground(new PixelBevelTexture(
                PixelBevelTexture.PANEL_COLOR,
                PixelBevelTexture.LINE_LOW_COLOR,
                PixelBevelTexture.LINE_HIGH_COLOR,
                1.5f
        ));
    }

    @Override
    public void addWidgets(WidgetContextRender ctx) {
        if (!(ctx instanceof ShopTabsPanelElement panel)) {
            return;
        }

        tabsContainer = new DraggableScrollableWidgetGroup();
        tabsContainer.setScrollWheelDirection(DraggableScrollableWidgetGroup.ScrollWheelDirection.VERTICAL);
        tabsContainer.setLayout(Layout.NONE);
        tabsContainer.setYScrollBarWidth(SCROLLBAR_WIDTH);
        tabsContainer.setYBarStyle(null, new ColorRectTexture(-1));
        tabsContainer.setClientSideWidget();

        tabsContent = new ScrollClippedWidgetGroup();
        tabsContent.setLayout(Layout.NONE);
        tabsContent.setClientSideWidget();
        tabsContainer.addWidget(tabsContent);

        ctx.addWidget(tabsContainer);
        ctx.addWidget(currenciesContainer = new WidgetGroup());
        currenciesContainer.setBackground(PixelBevelTexture.panelLow());

        tabsContent.addWidget(new ShopTabElement(null).setSelected(panel.getSelectedCategory() == null));

        if (panel.getShopScreen().getCatalogComponents() != null) {
            List<CatalogComponent> categories = panel.getShopScreen().getCatalogComponents().stream()
                    .sorted(Comparator.comparing(DefaultShopTabsPanelRender::sortKey))
                    .toList();

            categories.forEach(component -> tabsContent.addWidget(
                    new ShopTabElement(component).setSelected(panel.isSelectedCategory(component))
            ));
        }

        moneyCategoryTitle = new TextLabel(Component.literal("You money")).setAutoSize(false)
                .setWrapText(false)
                .setMaxLines(1)
                .setLineSpacing(0)
                .setPadding(0)
                .scaleToFit()
                .setAlignment(TextLabel.HorizontalAlignment.CENTER, TextLabel.VerticalAlignment.CENTER);

        currenciesVBox = new VerticalContainer();
        currenciesVBox.setDynamicSized(true);
        currencyRows.clear();
        currencyPriceWidgets.clear();

        final Minecraft minecraft = Minecraft.getInstance();
        final Font font = minecraft.font;
        final int textureSize = font.lineHeight + 3;

        for (IExternalCurrency value : SDMEconomyServiceClient.getAllCurrencies().values()) {
            int h = font.lineHeight;

            final HorizontalContainer hBox = new HorizontalContainer();
            final TransformTexture texture = ShopUtils.getCurrencyTexture(value);

            hBox.alignBottom();
            hBox.pushLastElementToEnd();
            hBox.setDynamicSized(false);
            hBox.setSpacing(4);
            hBox.setPadding(2, 0);

            if(texture != null) {
                final Widget widget = new ShopEmptyWidget().setBackground(texture);
                widget.setSize(textureSize, textureSize);
                hBox.addWidget(widget);
                h = textureSize;
            }

            final TextLabel name = new TextLabel(value.getDisplayName());
            hBox.addWidget(name);

            final PriceWidget money_count = new PriceWidget()
                    .setPriceText(value.format(value.getBalance(minecraft.player)))
                    .alignRight()
                    .alignBottom()
                    .autoSize();
            hBox.addWidget(money_count);
            currencyPriceWidgets.put(value.getId(), money_count);

            hBox.setSizeHeight(h);

            currencyRows.add(hBox);
            currenciesVBox.addWidget(hBox);
        }

        currenciesContainer.addWidget(moneyCategoryTitle);
        currenciesContainer.addWidgets(currenciesVBox);
    }

    public boolean refreshCurrencies() {
        if (currencyPriceWidgets.isEmpty()) {
            return false;
        }

        final Minecraft minecraft = Minecraft.getInstance();
        final Map<ResourceLocation, IExternalCurrency> currencies = SDMEconomyServiceClient.getAllCurrencies();
        if (!currencyPriceWidgets.keySet().equals(currencies.keySet())) {
            return false;
        }

        for (Map.Entry<ResourceLocation, IExternalCurrency> entry : currencies.entrySet()) {
            PriceWidget widget = currencyPriceWidgets.get(entry.getKey());
            if (widget == null) {
                return false;
            }

            widget.setPriceText(entry.getValue().format(entry.getValue().getBalance(minecraft.player))).autoSize();
        }

        return true;
    }

    protected static String sortKey(CatalogComponent component) {
        return component == null || component.getId() == null
                ? ""
                : component.getId().toLowerCase(Locale.ROOT);
    }

    public void updateSelectedTabs(ShopTabsPanelElement panel) {
        if (tabsContent == null) {
            return;
        }

        for (Widget widget : collectTabWidgets(tabsContent)) {
            if (widget instanceof ShopTabElement tab) {
                tab.setSelected(panel.isSelectedCategory(tab.getComponent()));
            }
        }
    }

    @Override
    public void alightWidgets(WidgetContextRender ctx) {
        if (!(ctx instanceof ShopTabsPanelElement panel) || !(ctx.getOwner() instanceof WidgetGroup)) {
            return;
        }

        if (tabsContainer == null || tabsContent == null || currenciesContainer == null) {
            return;
        }

        final Widget root = ctx.getOwner();
        final Size rootSize = root.getSize();
        if (rootSize.width <= HORIZONTAL_PADDING * 2 || rootSize.height <= VERTICAL_PADDING * 2 + SECTION_GAP) {
            for (Widget widget : collectTabWidgets(tabsContent)) {
                widget.setVisible(true);
                widget.setActive(true);
            }
            return;
        }

        final int contentWidth = Math.max(1, rootSize.width - HORIZONTAL_PADDING * 2);
        final int contentHeight = Math.max(1, rootSize.height - VERTICAL_PADDING * 2 - SECTION_GAP);
        final int tabsHeight = Math.max(1, contentHeight / 2);
        final int currenciesHeight = Math.max(1, contentHeight - tabsHeight);
        final int fontHeight = Minecraft.getInstance().font.lineHeight;

        tabsContainer.setSelfPosition(HORIZONTAL_PADDING, VERTICAL_PADDING);
        tabsContainer.setSize(contentWidth, tabsHeight);

        currenciesContainer.setSelfPosition(HORIZONTAL_PADDING, VERTICAL_PADDING + tabsHeight + SECTION_GAP);
        currenciesContainer.setSize(contentWidth, currenciesHeight);

        int currenciesWidth = Math.max(1, contentWidth - 4);


        moneyCategoryTitle.setSelfPosition(0, 2);
        moneyCategoryTitle.setSize(currenciesWidth, fontHeight);

        currenciesVBox.setSelfPosition(2 , 4 + moneyCategoryTitle.getSizeHeight());
        currenciesVBox.setSize(currenciesWidth, Math.max(1, currenciesHeight - 4));
        for (HorizontalContainer row : currencyRows) {
            row.setSize(currenciesWidth, Math.max(row.getSizeHeight(), fontHeight + 3));
        }

        int tabViewportWidth = Math.max(1, tabsContainer.getSizeWidth() - SCROLLBAR_WIDTH - HORIZONTAL_PADDING);
        int tabViewportHeight = Math.max(1, tabsContainer.getSizeHeight());
        tabsContent.setSelfPosition(0, 0);
        tabsContent.setSizeWidth(tabViewportWidth);

        List<Widget> tabWidgets = collectTabWidgets(tabsContent);
        int y = VERTICAL_PADDING;
        for (Widget widget : tabWidgets) {
            widget.setVisible(true);
            widget.setActive(true);
            widget.setSelfPosition(0, y);
            widget.setSize(tabViewportWidth, TAB_HEIGHT);

            if (widget instanceof ShopUiElement element) {
                element.alightWidget();
            }

            y += TAB_HEIGHT + panel.getWidgetSpace();
        }

        tabsContent.setSize(tabViewportWidth, Math.max(tabViewportHeight, y + VERTICAL_PADDING - panel.getWidgetSpace()));
    }

    protected List<Widget> collectTabWidgets(WidgetGroup group) {
        List<Widget> tabWidgets = new ArrayList<>();
        for (Widget widget : group.widgets) {
            if (widget instanceof ShopTabElement) {
                tabWidgets.add(widget);
            }
        }

        return tabWidgets;
    }

    protected static boolean isInsideScrollViewport(Widget widget, double mouseX, double mouseY) {
        Widget current = widget;
        while (current != null) {
            if (current instanceof DraggableScrollableWidgetGroup scroll) {
                return mouseX >= scroll.getPositionX()
                        && mouseY >= scroll.getPositionY()
                        && mouseX < scroll.getPositionX() + scroll.getSizeWidth()
                        && mouseY < scroll.getPositionY() + scroll.getSizeHeight();
            }

            current = current.getParent();
        }

        return true;
    }

    protected static class ScrollClippedWidgetGroup extends WidgetGroup {

        protected ScrollClippedWidgetGroup() {
            super(0, 0, 1, 1);
        }

        @Override
        public Widget getHoverElement(double mouseX, double mouseY) {
            return isInsideScrollViewport(this, mouseX, mouseY) ? super.getHoverElement(mouseX, mouseY) : null;
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            return isInsideScrollViewport(this, mouseX, mouseY) && super.mouseClicked(mouseX, mouseY, button);
        }

        @Override
        public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
            return isInsideScrollViewport(this, mouseX, mouseY) && super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
        }

        @Override
        public boolean mouseReleased(double mouseX, double mouseY, int button) {
            return isInsideScrollViewport(this, mouseX, mouseY) && super.mouseReleased(mouseX, mouseY, button);
        }

        @Override
        public boolean mouseWheelMove(double mouseX, double mouseY, double wheelDelta) {
            return isInsideScrollViewport(this, mouseX, mouseY) && super.mouseWheelMove(mouseX, mouseY, wheelDelta);
        }
    }
}
