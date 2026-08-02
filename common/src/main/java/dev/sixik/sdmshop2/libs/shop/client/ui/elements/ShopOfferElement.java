package dev.sixik.sdmshop2.libs.shop.client.ui.elements;

import com.lowdragmc.lowdraglib.utils.Size;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import dev.sixik.sdmshop2.libs.shop.base.ShopOffer;
import dev.sixik.sdmshop2.libs.shop.client.screens_2.elements.ShopUiElement;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.ShopUIUtils;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.WidgetContextRender;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.WidgetRender;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.ThemeApi;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.containers.ContextMenuWidget;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class ShopOfferElement extends WidgetGroup implements ShopUiElement, WidgetContextRender {

    @Nullable
    private final ShopOffer shopEntity;

    protected final WidgetRender render;

    protected final EnumMap<CallbackType, Consumer<?>> callbacks;

    public ShopOfferElement(@Nullable ShopOffer shopEntity) {
        this(shopEntity, ThemeApi.getDefaultTheme(ThemeApi.Category.Offers).get());
    }

    public ShopOfferElement(@Nullable ShopOffer shopEntity, WidgetRender render) {
        this.shopEntity = shopEntity;
        this.render = Objects.requireNonNull(render, "render");
        this.callbacks = new EnumMap<>(CallbackType.class);
        this.callbacks.put(CallbackType.InvokeBuy, (money_group) -> onBuy((String) money_group));

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
        clearAllWidgets();
        render.addWidgets(this);

        if (relayout) {
            alightWidget();
        }

        return this;
    }

    @Override
    public Map<CallbackType, Consumer<?>> getCallbacks() {
        return callbacks;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (render.mouseClicked(this, mouseX, mouseY, button)) {
            return true;
        }

        if (button != 1) {
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
        menuWidget
                .addItem(Component.translatable("shop.ui.offer_element.context_menu.edit"), () -> {
                    ShopUIUtils.createEditMenu(this, shopEntity, this::refresh);
                }).addSeparator()
                .addItem(Component.translatable("shop.ui.offer_element.context_menu.copy"), () -> {

                }).addSeparator()
                .addItem(Component.translatable("shop.ui.offer_element.context_menu.delete"), () -> {

                });
    }

    @Override
    @Nullable
    public ShopOffer getShopEntity() {
        return shopEntity;
    }

    @Override
    public Widget getOwner() {
        return this;
    }

    public void onBuy(String money_group) {}
}
