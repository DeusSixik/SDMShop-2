package dev.sixik.sdmshop2.libs.shop.client.ui.style;

import com.lowdragmc.lowdraglib.gui.texture.ColorRectTexture;
import com.lowdragmc.lowdraglib.gui.texture.TransformTexture;
import com.lowdragmc.lowdraglib.gui.widget.DraggableScrollableWidgetGroup;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.gui.widget.layout.Layout;
import com.lowdragmc.lowdraglib.utils.Size;
import dev.sixik.sdmshop2.libs.sdmeconomy.ICurrency;
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
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.ButtonWidget;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.TextLabel;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.containers.ContextMenuWidget;
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
    protected DraggableScrollableWidgetGroup currenciesScrollContainer;
    protected VerticalContainer currenciesVBox;
    protected TextLabel moneyCategoryTitle;
    protected ButtonWidget addCurrencyButton;

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

        tabsContent.addWidget(new ShopTabElement(panel, null).setSelected(panel.getSelectedCategory() == null));

        if (panel.getShopScreen().getCatalogComponents() != null) {
            List<CatalogComponent> categories = panel.getShopScreen().getCatalogComponents().stream()
                    .sorted(DefaultShopTabsPanelRender::compareCategories)
                    .toList();

            categories.forEach(component -> tabsContent.addWidget(
                    new ShopTabElement(panel, component).setSelected(panel.isSelectedCategory(component))
            ));
        }

        if (panel.isEditorMode()) {
            tabsContent.addWidget(createAddCategoryButton(panel));
        }

        moneyCategoryTitle = new TextLabel(Component.literal("You money")).setAutoSize(false)
                .setWrapText(false)
                .setMaxLines(1)
                .setLineSpacing(0)
                .setPadding(0)
                .scaleToFit()
                .setAlignment(TextLabel.HorizontalAlignment.CENTER, TextLabel.VerticalAlignment.CENTER);

        currenciesVBox = new VerticalContainer();
        currenciesVBox.setDynamicSized(false);
        currenciesVBox.setSpacing(2);

        currenciesScrollContainer = new DraggableScrollableWidgetGroup();
        currenciesScrollContainer.setScrollWheelDirection(DraggableScrollableWidgetGroup.ScrollWheelDirection.VERTICAL);
        currenciesScrollContainer.setLayout(Layout.NONE);
        currenciesScrollContainer.setYScrollBarWidth(SCROLLBAR_WIDTH);
        currenciesScrollContainer.setYBarStyle(null, new ColorRectTexture(-1));
        currenciesScrollContainer.setClientSideWidget();
        currenciesScrollContainer.addWidget(currenciesVBox);

        currencyRows.clear();
        currencyPriceWidgets.clear();

        final Minecraft minecraft = Minecraft.getInstance();
        final Font font = minecraft.font;
        final int textureSize = font.lineHeight + 3;

        List<Map.Entry<ResourceLocation, ICurrency>> currencies = new ArrayList<>(
                (panel.isEditorMode() ? panel.getShopScreen().getEditorCurrencies() : SDMEconomyServiceClient.getAllCurrencies()).entrySet()
        );
        currencies.sort(Map.Entry.comparingByKey(Comparator.comparing(ResourceLocation::toString)));

        for (Map.Entry<ResourceLocation, ICurrency> entry : currencies) {
            ResourceLocation id = entry.getKey();
            ICurrency value = entry.getValue();
            int h = font.lineHeight;

            final HorizontalContainer hBox = panel.isEditorMode()
                    ? new CurrencyRow(panel, id, value)
                    : new HorizontalContainer();
            final TransformTexture texture = ShopUtils.getCurrencyTexture(value);

            hBox.alignBottom();
            if (!panel.isEditorMode()) {
                hBox.pushLastElementToEnd();
            }
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

            if (!panel.isEditorMode()) {
                final PriceWidget money_count = new PriceWidget()
                        .setPriceText(value.format(SDMEconomyServiceClient.getBalance(value, minecraft.player)))
                        .alignRight()
                        .alignBottom()
                        .autoSize();
                hBox.addWidget(money_count);
                currencyPriceWidgets.put(value.getId(), money_count);
            }

            hBox.setSizeHeight(h);

            currencyRows.add(hBox);
            currenciesVBox.addWidget(hBox);
        }

        if (panel.isEditorMode()) {
            addCurrencyButton = createAddCurrencyButton(panel);
        }

        currenciesContainer.addWidget(moneyCategoryTitle);
        currenciesContainer.addWidget(currenciesScrollContainer);
        if (addCurrencyButton != null) {
            currenciesContainer.addWidget(addCurrencyButton);
        }
    }

    public boolean refreshCurrencies() {
        if (currencyPriceWidgets.isEmpty()) {
            return false;
        }

        final Minecraft minecraft = Minecraft.getInstance();
        final Map<ResourceLocation, ICurrency> currencies = SDMEconomyServiceClient.getAllCurrencies();
        if (!currencyPriceWidgets.keySet().equals(currencies.keySet())) {
            return false;
        }

        for (Map.Entry<ResourceLocation, ICurrency> entry : currencies.entrySet()) {
            PriceWidget widget = currencyPriceWidgets.get(entry.getKey());
            if (widget == null) {
                return false;
            }

            widget.setPriceText(entry.getValue().format(SDMEconomyServiceClient.getBalance(entry.getValue(), minecraft.player))).autoSize();
        }

        return true;
    }

    public int getTabsScrollY() {
        return tabsContainer == null ? 0 : tabsContainer.getScrollYOffset();
    }

    public void restoreTabsScrollY() {
        restoreTabsScrollY(getTabsScrollY());
    }

    public void restoreTabsScrollY(int previousScrollY) {
        if (tabsContainer == null || tabsContent == null) {
            return;
        }

        int maxScrollY = Math.max(0, tabsContent.getSizeHeight() - tabsContainer.getSizeHeight());
        tabsContainer.setScrollYOffset(Math.min(Math.max(0, previousScrollY), maxScrollY));
    }

    public int getCurrenciesScrollY() {
        return currenciesScrollContainer == null ? 0 : currenciesScrollContainer.getScrollYOffset();
    }

    public void restoreCurrenciesScrollY(int previousScrollY) {
        if (currenciesScrollContainer == null || currenciesVBox == null) {
            return;
        }

        int maxScrollY = Math.max(0, currenciesVBox.getSizeHeight() - currenciesScrollContainer.getSizeHeight());
        currenciesScrollContainer.setScrollYOffset(Math.min(Math.max(0, previousScrollY), maxScrollY));
    }

    protected static int compareCategories(CatalogComponent first, CatalogComponent second) {
        int order = Integer.compare(first == null ? 0 : first.getOrder(), second == null ? 0 : second.getOrder());
        if (order != 0) {
            return order;
        }

        return sortKey(first).compareTo(sortKey(second));
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

    protected ButtonWidget createAddCategoryButton(ShopTabsPanelElement panel) {
        ButtonWidget button = new ButtonWidget();
        button.setText(Component.literal("+ Category"));
        button.setButtonTexture(PixelBevelTexture.panel());
        button.setHoverTexture(new PixelBevelTexture(0xFF111624, PixelBevelTexture.ACCENT_LOW_COLOR, PixelBevelTexture.ACCENT_HIGH_COLOR, 0.7f));
        button.setClickedTexture(PixelBevelTexture.accent().pressed());
        button.setOnClick(ignored -> panel.createDraftCategory());
        button.setClientSideWidget();
        return button;
    }

    protected ButtonWidget createAddCurrencyButton(ShopTabsPanelElement panel) {
        ButtonWidget button = new ButtonWidget();
        button.setText(Component.literal("+ Currency"));
        button.setTextPadding(4);
        button.setMinTextScale(0.35f);
        button.setButtonTexture(PixelBevelTexture.panel());
        button.setHoverTexture(new PixelBevelTexture(0xFF111624, PixelBevelTexture.ACCENT_LOW_COLOR, PixelBevelTexture.ACCENT_HIGH_COLOR, 0.7f));
        button.setClickedTexture(PixelBevelTexture.accent().pressed());
        button.setOnClick(ignored -> panel.openCreateCurrencyModal());
        button.setClientSideWidget();
        return button;
    }

    @Override
    public void alightWidgets(WidgetContextRender ctx) {
        if (!(ctx instanceof ShopTabsPanelElement panel) || !(ctx.getOwner() instanceof WidgetGroup)) {
            return;
        }

        if (tabsContainer == null || tabsContent == null || currenciesContainer == null
                || currenciesScrollContainer == null || currenciesVBox == null) {
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
        int previousCurrencyScrollY = getCurrenciesScrollY();
        moneyCategoryTitle.setSelfPosition(0, 2);
        moneyCategoryTitle.setSize(currenciesWidth, fontHeight);

        int footerHeight = addCurrencyButton == null ? 0 : TAB_HEIGHT;
        int footerGap = addCurrencyButton == null ? 0 : 4;
        int scrollY = 4 + moneyCategoryTitle.getSizeHeight();
        int scrollHeight = Math.max(1, currenciesHeight - scrollY - footerHeight - footerGap - 2);
        int currencyViewportWidth = Math.max(1, currenciesWidth - SCROLLBAR_WIDTH - 2);

        currenciesScrollContainer.setSelfPosition(2, scrollY);
        currenciesScrollContainer.setSize(currenciesWidth, scrollHeight);

        currenciesVBox.setSelfPosition(0, 0);
        for (HorizontalContainer row : currencyRows) {
            row.setSize(currencyViewportWidth, Math.max(row.getSizeHeight(), fontHeight + 3));
        }
        int rowsHeight = 0;
        for (HorizontalContainer row : currencyRows) {
            rowsHeight += row.getSizeHeight();
        }
        if (!currencyRows.isEmpty()) {
            rowsHeight += Math.max(0, currencyRows.size() - 1) * 2;
        }
        currenciesVBox.setSize(currencyViewportWidth, Math.max(scrollHeight, rowsHeight));

        if (addCurrencyButton != null) {
            addCurrencyButton.setSelfPosition(2, Math.max(scrollY + scrollHeight + footerGap, currenciesHeight - footerHeight - 2));
            addCurrencyButton.setSize(currenciesWidth, footerHeight);
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
        restoreTabsScrollY();
        restoreCurrenciesScrollY(previousCurrencyScrollY);
    }

    protected List<Widget> collectTabWidgets(WidgetGroup group) {
        List<Widget> tabWidgets = new ArrayList<>();
        for (Widget widget : group.widgets) {
            tabWidgets.add(widget);
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

    protected static class CurrencyRow extends HorizontalContainer {

        protected final ShopTabsPanelElement panel;
        protected final ResourceLocation id;
        protected final ICurrency currency;

        protected CurrencyRow(ShopTabsPanelElement panel, ResourceLocation id, ICurrency currency) {
            this.panel = panel;
            this.id = id;
            this.currency = currency;
            setHoverTooltips(currency.getDisplayName().copy().append(Component.literal(" §8(" + id + ")")));
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (button == 1 && panel != null && panel.isEditorMode() && isMouseOverElement(mouseX, mouseY)) {
                openContextMenu((int) mouseX, (int) mouseY);
                return true;
            }

            return super.mouseClicked(mouseX, mouseY, button);
        }

        protected void openContextMenu(int mouseX, int mouseY) {
            ContextMenuWidget menu = new ContextMenuWidget(mouseX, mouseY, 128);
            menu.setScale(0.75f);
            menu.addItem(Component.literal("Edit"), () -> panel.openEditCurrencyModal(id, currency))
                    .addSeparator()
                    .addItem(Component.literal("Delete"), panel.getShopScreen().canDeleteCurrency(id), () -> panel.deleteCurrency(id));
            ContextMenuWidget.open(this, menu);
        }
    }
}
