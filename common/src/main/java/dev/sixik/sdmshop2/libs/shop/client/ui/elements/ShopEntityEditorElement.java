package dev.sixik.sdmshop2.libs.shop.client.ui.elements;

import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import dev.sixik.sdmshop2.libs.shop.base.ObjectIdGetter;
import dev.sixik.sdmshop2.libs.shop.base.ShopEntity;
import dev.sixik.sdmshop2.libs.shop.client.config.constructors.ComponentConfigWidgetConstructor;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.*;
import dev.sixik.sdmshop2.libs.shop.client.ui.style.DefaultEditMenuRender;
import dev.sixik.sdmshop2.libs.shop.client.ui.toast.ShopToasts;
import dev.sixik.sdmshop2.libs.shop.components.api.ShopComponent;
import dev.sixik.sdmshop2.libs.shop.editor.ShopEditSession;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.ButtonWidget;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.InputTextBox;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.TextLabel;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.containers.ModalWidget;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class ShopEntityEditorElement extends ModalWidget implements WidgetContextRender, UIDisposable {

    @Nullable
    @Getter
    private final ShopEntity shopEntity;
    @Nullable
    @Getter
    private final ShopEditSession editSession;

    protected final WidgetRender render;
    protected final UIEventScope eventScope = new UIEventScope();
    @Nullable
    protected final Runnable onEdit;
    protected boolean editCallbackInvoked;

    public static ShopEntityEditorElement open(Widget owner, @Nullable ShopEntity shopEntity) {
        return open(owner, shopEntity, null);
    }

    public static ShopEntityEditorElement open(Widget owner, @Nullable ShopEntity shopEntity, @Nullable Runnable onEdit) {
        return ModalWidget.open(owner, new ShopEntityEditorElement(owner, shopEntity, onEdit));
    }

    public static ShopEntityEditorElement open(Widget owner, @Nullable ShopEditSession editSession, @Nullable ShopEntity shopEntity) {
        return open(owner, editSession, shopEntity, null);
    }

    public static ShopEntityEditorElement open(Widget owner, @Nullable ShopEditSession editSession, @Nullable ShopEntity shopEntity, @Nullable Runnable onEdit) {
        return ModalWidget.open(owner, new ShopEntityEditorElement(owner, editSession, shopEntity, onEdit));
    }

    protected ShopEntityEditorElement(Widget owner, @Nullable ShopEntity shopEntity) {
        this(owner, shopEntity, (Runnable) null);
    }

    protected ShopEntityEditorElement(Widget owner, @Nullable ShopEntity shopEntity, @Nullable Runnable onEdit) {
        this(owner, null, shopEntity, StyleApi.getDefaultStyle(StyleApi.Category.Editor).get(), onEdit);
    }

    protected ShopEntityEditorElement(Widget owner, @Nullable ShopEditSession editSession, @Nullable ShopEntity shopEntity, @Nullable Runnable onEdit) {
        this(owner, editSession, shopEntity, StyleApi.getDefaultStyle(StyleApi.Category.Editor).get(), onEdit);
    }

    protected ShopEntityEditorElement(Widget owner, @Nullable ShopEntity shopEntity, WidgetRender render) {
        this(owner, null, shopEntity, render, null);
    }

    protected ShopEntityEditorElement(Widget owner, @Nullable ShopEntity shopEntity, WidgetRender render, @Nullable Runnable onEdit) {
        this(owner, null, shopEntity, render, onEdit);
    }

    protected ShopEntityEditorElement(Widget owner, @Nullable ShopEditSession editSession, @Nullable ShopEntity shopEntity, WidgetRender render, @Nullable Runnable onEdit) {
        this.shopEntity = shopEntity;
        this.editSession = editSession;
        this.onEdit = onEdit;

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
    public ShopEntityEditorElement close() {
        final boolean shouldInvokeCallback = !editCallbackInvoked && onEdit != null && (getParent() != null || attachedParent != null);
        dispose();
        super.close();

        if (shouldInvokeCallback) {
            editCallbackInvoked = true;
            onEdit.run();
        }

        return this;
    }

    @Override
    public UIEventScope eventScope() {
        return eventScope;
    }

    @Override
    public void dispose() {
        eventScope.close();
        ShopUIUtils.disposeChildren(this);
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

    public int getComponentCount() {
        return shopEntity == null ? 0 : shopEntity.getComponents().size();
    }

    public boolean canMoveComponent(@Nullable ShopComponent component, int offset) {
        if (!canEditComponent(component) || offset == 0) {
            return false;
        }

        int index = shopEntity.indexOfComponent(component);
        int target = index + offset;
        return index >= 0 && target >= 0 && target < getComponentCount();
    }

    public void moveComponent(@Nullable ShopComponent component, int offset) {
        if (!canEditComponent(component) || offset == 0) {
            return;
        }

        moveComponentTo(component, shopEntity.indexOfComponent(component) + offset);
    }

    public boolean duplicateComponent(@Nullable ShopComponent component) {
        if (!canEditComponent(component)) {
            return false;
        }

        if (render instanceof DefaultEditMenuRender editMenuRender && editMenuRender.duplicateComponent(this, component)) {
            ShopToasts.success(Component.translatable("shop.ui.toast.component_duplicated"));
            return true;
        }

        return false;
    }

    public boolean moveComponentTo(@Nullable ShopComponent component, int targetIndex) {
        if (!canEditComponent(component)) {
            return false;
        }

        int currentIndex = shopEntity.indexOfComponent(component);
        if (currentIndex < 0) {
            return false;
        }

        int clampedTarget = Math.max(0, Math.min(targetIndex, getComponentCount() - 1));
        if (!shopEntity.moveComponentToIndex(component, clampedTarget)) {
            return false;
        }

        if (editSession != null && !editSession.closed()) {
            editSession.recordHistory(
                    "component.move",
                    shopEntity.getClass().getSimpleName(),
                    shopEntity instanceof ObjectIdGetter idGetter ? idGetter.getUUID() : null,
                    component.getType().getId(),
                    "Moved component " + component.getType().getId() + " from " + (currentIndex + 1) + " to " + (clampedTarget + 1)
            );
        }

        ComponentConfigWidgetConstructor.invokeUpdate(component, false);
        refreshEditorContent();
        ShopToasts.success(Component.translatable("shop.ui.toast.component_moved"));
        return true;
    }

    public void openMoveComponentModal(@Nullable ShopComponent component) {
        if (!canEditComponent(component) || getComponentCount() <= 1) {
            return;
        }

        int currentIndex = shopEntity.indexOfComponent(component);
        if (currentIndex < 0) {
            return;
        }

        ModalWidget modal = new ModalWidget(260, 116)
                .setTitle(Component.translatable("shop.ui.component.move.title"))
                .setCloseOnEsc(true)
                .setCloseOnOutsideClick(false);
        ModalWidget opened = ModalWidget.openNested(this, modal);
        if (opened == null) {
            return;
        }

        TextLabel label = new TextLabel(0, 4, opened.getContentWidth(), 20,
                Component.translatable("shop.ui.common.target_position", getComponentCount()));
        label.setAutoSize(false)
                .setAlignment(TextLabel.HorizontalAlignment.LEFT, TextLabel.VerticalAlignment.CENTER)
                .setColor(0xFFAEB4C6);
        opened.addWidget(label);

        InputTextBox input = new InputTextBox(0, 28, opened.getContentWidth(), 20);
        input.setNumbersOnly(1, getComponentCount());
        input.setCurrentStringSilently(currentIndex + 1);
        input.setPlaceholder(Component.translatable("shop.ui.common.position"));
        input.setClientSideWidget();
        opened.addWidget(input);

        int buttonY = Math.max(56, opened.getContentHeight() - 24);
        int buttonWidth = Math.max(1, opened.getContentWidth() / 2 - 4);
        ButtonWidget cancel = createModalButton(0, buttonY, buttonWidth, 20, Component.translatable("shop.ui.common.cancel"), ignored -> opened.close());
        ButtonWidget move = createModalButton(buttonWidth + 8, buttonY, buttonWidth, 20, Component.translatable("shop.ui.common.move"), ignored -> {
            int position;
            try {
                position = Integer.parseInt(input.getCurrentString().trim());
            } catch (Exception e) {
                ShopToasts.warning(Component.translatable("shop.ui.toast.invalid_position"));
                return;
            }

            if (moveComponentTo(component, position - 1)) {
                opened.close();
            }
        });

        opened.addWidget(cancel);
        opened.addWidget(move);
        input.setFocus(true);
    }

    public void refreshEditorContent() {
        if (render instanceof DefaultEditMenuRender editMenuRender) {
            editMenuRender.refreshContent(this);
            return;
        }

        render.alightWidgets(this);
    }

    protected boolean canEditComponent(@Nullable ShopComponent component) {
        return component != null
                && shopEntity != null
                && shopEntity.indexOfComponent(component) >= 0
                && editSession != null
                && !editSession.closed();
    }

    private ButtonWidget createModalButton(int x, int y, int width, int height, Component text, java.util.function.Consumer<com.lowdragmc.lowdraglib.gui.util.ClickData> action) {
        ButtonWidget button = new ButtonWidget(x, y, width, height, text, action);
        button.setClientSideWidget();
        button.setTextPadding(4);
        button.setMinTextScale(0.35f);
        return button;
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
