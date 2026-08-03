package dev.sixik.sdmshop2.libs.shop.client.ui.elements;

import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.utils.Size;
import com.mojang.blaze3d.platform.Window;
import dev.sixik.sdmshop2.libs.shop.client.SDMShopClient;
import dev.sixik.sdmshop2.libs.shop.client.ui.widgets.base.ShopWidgetGroup;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.UIEventScope;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.ShopUIUtils;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.UIDisposable;
import dev.sixik.sdmshop2.libs.platform.utils.eventbus.DODEventBus;
import dev.sixik.sdmshop2.libs.platform.utils.eventbus.EventPtr;
import dev.sixik.sdmshop2.libs.platform.utils.eventbus.EventSubscription;
import dev.sixik.sdmshop2.libs.shop.client.ui.events.ShopUIEvents;
import dev.sixik.sdmshop2.libs.shop.client.ui.toast.ShopToasts;
import dev.sixik.sdmshop2.libs.shop.components.misc.CatalogComponent;
import dev.sixik.sdmshop2.libs.shop.components.misc.ShopOffersContainerComponent;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import org.jspecify.annotations.Nullable;

import java.util.Map;

public class ShopScreenElement extends ShopWidgetGroup implements UIDisposable {

    public static ShopScreenElement Instance;

    @Getter
    private final Minecraft minecraft;

    @Getter
    private final Window window;

    @Getter
    private ShopTabsPanelElement tabsPanel;

    @Getter
    private ShopOffersPanelElement shopOffersPanel;

    @Getter
    private ShopToolPanelElement toolPanel;

    @Getter
    private ObjectArrayList<CatalogComponent> catalogComponents;
    @Getter
    private ShopOffersContainerComponent entriesContainer;

    private final UIEventScope screenEventScope = new UIEventScope();
    private final Map<String, State> states = new Object2ObjectOpenHashMap<>();

    public ShopScreenElement() {
        this.minecraft = Minecraft.getInstance();
        this.window = minecraft.getWindow();
        Instance = this;
    }

    @Override
    public void initWidget() {
//        setBackground(new ColorRectAndBorderTexture());

        reloadShopData();
        listenScreen(ShopUIEvents.REFRESH_UI, this::handleRefresh);

        alightWidget();
        addWidget(toolPanel = new ShopToolPanelElement(this));
        addWidget(tabsPanel = new ShopTabsPanelElement(this));
        addWidget(shopOffersPanel = new ShopOffersPanelElement(this));
        ShopToasts.attachTo(this);
        listenScreen(ShopUIEvents.BUY_SHOP_ENTITY, event -> ShopPurchaseModalElement.open(this, event.entity(), event.money_group()));
        customInitWidget();
    }

    protected void handleRefresh(ShopUIEvents.RefreshUI event) {
        reloadShopData();

        if (!event.affectsCategories() || tabsPanel == null || tabsPanel.getSelectedCategory() == null) {
            return;
        }

        if (!hasCategory(tabsPanel.getSelectedCategory())) {
            ShopUIEvents.invokeSelectCategory(null);
        }
    }

    protected void reloadShopData() {
        if (SDMShopClient.Shop == null) {
            catalogComponents = new ObjectArrayList<>();
            entriesContainer = null;
            return;
        }

        SDMShopClient.Shop.getCategories().reindex();
        this.catalogComponents = SDMShopClient.Shop.getCategories().getCatalogsComponents();
        this.entriesContainer = SDMShopClient.Shop.getEntries();
    }

    protected boolean hasCategory(CatalogComponent category) {
        if (category == null || catalogComponents == null) {
            return false;
        }

        for (CatalogComponent current : catalogComponents) {
            if (sameCategory(current, category)) {
                return true;
            }
        }

        return false;
    }

    protected static boolean sameCategory(CatalogComponent first, CatalogComponent second) {
        if (first == second) {
            return true;
        }
        if (first == null || second == null) {
            return false;
        }
        if (first.getUuid() != null && second.getUuid() != null) {
            return first.getUuid().equals(second.getUuid());
        }

        return java.util.Objects.equals(first.getId(), second.getId());
    }

    @Override
    public void alightWidget() {
        setSize(window.getGuiScaledWidth(), window.getGuiScaledHeight());
    }

    protected void alightWidgets() {
       /*
            cur_* = Current N
            tsw_*  = TabsWidget
        */
        final Size cur_size = getSize();
        final int cur_w     = cur_size.width;
        final int cur_h     = cur_size.height;
        final int x_w_space = 4;
        final int y_h_space = 4;

        toolPanel.setSize(
                cur_w - x_w_space * 2,
                25
        );
        toolPanel.setSelfPosition(
                x_w_space,
                y_h_space / 2
        );

        final int tsw_w         = cur_w  / 4;
        final int tsw_h_offset  = (cur_h / 4);
        final int tsw_h         = cur_h - tsw_h_offset * 2;

        final var sp_widgets    = toolPanel.getWidgets();
        final var sp_size       = toolPanel.getSize();
        for (int i = 0; i < sp_widgets.size(); i++) {
            final Widget sp_widget = sp_widgets.get(i);
            sp_widget.setSelfPositionY(
                    (sp_size.height - sp_widget.getSize().height) / 2
            );
        }

        /*
            ep_* = OffersPanel
         */
        shopOffersPanel.setSize(
                cur_w  - tsw_w          - x_w_space  * 2,
                cur_h - sp_size.height - y_h_space  * 2
        );
        shopOffersPanel.setSelfPosition(
                tsw_w          + x_w_space,
                sp_size.height + y_h_space
        );

        final int tsw_x = 4;
        final int tsw_y = tsw_h_offset;
        tabsPanel.setSelfPosition(tsw_x, shopOffersPanel.getSelfPositionY());
        tabsPanel.setSize(
                tsw_w - 2,
                shopOffersPanel.getSizeHeight()
        );
    }

    @Override
    public void onScreenSizeUpdate(int screenWidth, int screenHeight) {
        super.onScreenSizeUpdate(screenWidth, screenHeight);
        alightWidget();
        alightWidgets();
        invokeAlightWidgets();
    }

    @Nullable
    public State getState(String id) {
        return states.get(id);
    }

    public void putState(String id, State state) {
        states.put(id, state);
    }

    public void updateState(String id, Object value) {
        State state = getState(id);
        if(state == null) return;
        state.setValue(value);
    }

    public void updateState(String id, State.Type type, Object defaultValue) {
        State state = getState(id);
        if(state == null) return;
        if(state.type != type)
            throw new IllegalArgumentException("Can't update state because '" + type.name() + " != " + state.type.name() + "' !");
        state.update(type, defaultValue);
    }

    public UIEventScope screenEventScope() {
        return screenEventScope;
    }

    public <Event> EventSubscription listenScreen(EventPtr<Event> event, DODEventBus.EventListener<Event> listener) {
        return screenEventScope.listen(event, listener);
    }

    @Override
    public void dispose() {
        screenEventScope.close();
        ShopUIUtils.disposeChildren(this);
        ShopToasts.clear();
        if (Instance == this) {
            Instance = null;
        }
    }

    public static class State {

        @Getter
        private Type type;

        @Getter
        private Object defaultValue;

        @Setter
        private @Nullable Object value;

        public State(Type type, Object defaultValue) {
            this(type, defaultValue, null);
        }

        public State(Type type, Object defaultValue, @Nullable Object value) {
            this.type = type;
            this.defaultValue = defaultValue;
            this.value = value;
        }

        public Object getValue() {
            return value == null ? defaultValue : value;
        }

        void update(Type type, Object defaultValue) {
            update(type, defaultValue, null);
        }

        void update(Type type, Object defaultValue, @Nullable Object value) {
            this.type = type;
            this.defaultValue = defaultValue;
            this.value = value;
        }

        public enum Type {
            String,
            Int,
            Bool,
            Double,
            Float,
            ShopEntity
        }
    }
}
