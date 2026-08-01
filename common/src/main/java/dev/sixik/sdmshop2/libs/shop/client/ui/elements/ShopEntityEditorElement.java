package dev.sixik.sdmshop2.libs.shop.client.ui.elements;

import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import dev.sixik.sdmshop2.libs.shop.base.ShopEntity;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.ThemeApi;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.WidgetContextRender;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.WidgetRender;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.containers.ContextMenuWidget;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.containers.ModalWidget;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class ShopEntityEditorElement extends ModalWidget implements WidgetContextRender {

    @Nullable
    @Getter
    private final ShopEntity shopEntity;

    protected final WidgetRender render;

    public static ShopEntityEditorElement open(Widget owner, @Nullable ShopEntity shopEntity) {
        return open(owner, shopEntity, null);
    }

    public static ShopEntityEditorElement open(Widget owner, @Nullable ShopEntity shopEntity, @Nullable Runnable onEdit) {
        return ModalWidget.open(owner, new ShopEntityEditorElement(owner, shopEntity));
    }

    protected ShopEntityEditorElement(Widget owner, @Nullable ShopEntity shopEntity) {
        this(owner, shopEntity, ThemeApi.getDefaultTheme(ThemeApi.Category.Editor).get());
    }

    protected ShopEntityEditorElement(Widget owner, @Nullable ShopEntity shopEntity, WidgetRender render) {
        this.shopEntity = shopEntity;

        this.render = Objects.requireNonNull(render, "render");
        this.render.constructor(this);
        this.render.addWidgets(this);

        Widget root = owner;
        while (root.getParent() != null) {
            root = root.getParent();
        }

        if (root instanceof WidgetGroup mainGroup) {
            attachedParent = mainGroup;
        } else
            throw new IllegalArgumentException("Owner widget its not a WidgetGroup");

        onSizeUpdate();
    }

    @Override
    public void onSizeUpdate() {
        calculateSize();
        super.onSizeUpdate();
    }

    @Override
    protected void recomputeLayout() {
        super.recomputeLayout();

        if(render == null) return;
        render.alightWidgets(this);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (render.mouseClicked(this, mouseX, mouseY, button)) {
            return true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    protected void calculateSize() {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft == null || minecraft.getWindow() == null) return;

        int screenWidth = Math.max(1, minecraft.getWindow().getGuiScaledWidth());
        int screenHeight = Math.max(1, minecraft.getWindow().getGuiScaledHeight());

        setPanelSize(screenWidth * 4/8, screenHeight * 4/5);
    }

    @Override
    public Widget getOwner() {
        return this;
    }
}
