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
import dev.sixik.sdmshop2.libs.shop.client.screens.widgets.ShopEmptyWidget;
import dev.sixik.sdmshop2.libs.shop.client.screens_2.elements.ShopUiElement;
import dev.sixik.sdmshop2.libs.shop.client.screens_2.elements.tabs.ShopTabElement;
import dev.sixik.sdmshop2.libs.shop.client.textures.PixelBevelTexture;
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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

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

        tabsContent = new WidgetGroup();
        tabsContent.setLayout(Layout.NONE);
        tabsContent.setClientSideWidget();
        tabsContainer.addWidget(tabsContent);

        ctx.addWidget(tabsContainer);
        ctx.addWidget(currenciesContainer = new WidgetGroup());
        currenciesContainer.setBackground(PixelBevelTexture.panelLow());

        if (panel.getShopScreen().getCatalogComponents() != null) {
            panel.getShopScreen().getCatalogComponents().stream()
                    .sorted(Comparator.comparing(DefaultShopTabsPanelRender::sortKey))
                    .forEach(component -> tabsContent.addWidget(new ShopTabElement(component)));
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

            hBox.setSizeHeight(h);

            currencyRows.add(hBox);
            currenciesVBox.addWidget(hBox);
        }

        currenciesContainer.addWidget(moneyCategoryTitle);
        currenciesContainer.addWidgets(currenciesVBox);
    }

    protected static String sortKey(CatalogComponent component) {
        return component == null || component.getId() == null
                ? ""
                : component.getId().toLowerCase(Locale.ROOT);
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
        final Size root_size = root.getSize();
        if (root_size.width <= HORIZONTAL_PADDING * 2 || root_size.height <= VERTICAL_PADDING * 2 + SECTION_GAP) {
            for (Widget widget : collectTabWidgets(tabsContent)) {
                widget.setVisible(true);
                widget.setActive(true);
            }
            return;
        }

        final int contentWidth = Math.max(1, root_size.width - HORIZONTAL_PADDING * 2);
        final int contentHeight = Math.max(1, root_size.height - VERTICAL_PADDING * 2 - SECTION_GAP);
        final int tabsHeight = Math.max(1, contentHeight / 2);
        final int currenciesHeight = Math.max(1, contentHeight - tabsHeight);
        final int font_height = Minecraft.getInstance().font.lineHeight;

        tabsContainer.setSelfPosition(HORIZONTAL_PADDING, VERTICAL_PADDING);
        tabsContainer.setSize(contentWidth, tabsHeight);

        currenciesContainer.setSelfPosition(HORIZONTAL_PADDING, VERTICAL_PADDING + tabsHeight + SECTION_GAP);
        currenciesContainer.setSize(contentWidth, currenciesHeight);

        int currenciesWidth = Math.max(1, contentWidth - 4);


        moneyCategoryTitle.setSelfPosition(0, 2);
        moneyCategoryTitle.setSize(currenciesWidth, font_height);

        currenciesVBox.setSelfPosition(2 , 4 + moneyCategoryTitle.getSizeHeight());
        currenciesVBox.setSize(currenciesWidth, Math.max(1, currenciesHeight - 4));
        for (HorizontalContainer row : currencyRows) {
            row.setSize(currenciesWidth, Math.max(row.getSizeHeight(), font_height + 3));
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
}
