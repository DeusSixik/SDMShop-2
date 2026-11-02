package dev.sixik.sdmshop2.libs.shop.client.ui.elements;

import dev.sixik.sdmshop2.libs.platform.utils.eventbus.DODEventBus;
import dev.sixik.sdmshop2.libs.platform.utils.eventbus.EventPtr;
import dev.sixik.sdmshop2.libs.platform.utils.eventbus.EventSubscription;
import dev.sixik.sdmshop2.libs.shop.base.ShopEntity;
import dev.sixik.sdmshop2.libs.shop.client.ui.ShopScreenController;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.UIDisposable;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.UIEventScope;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.WidgetContextRender;
import dev.sixik.sdmshop2.libs.shop.client.ui.events.ShopUIEvents;
import dev.sixik.sdmshop2.libs.shop.client.ui.toast.ShopToasts;
import dev.sixik.sdmshop2.libs.shop.client.ui.widgets.base.ShopWidgetGroup;
import dev.sixik.sdmshop2.libs.shop.editor.ShopEditSession;
import dev.sixik.sdmshop2.utils.ShopUtils;
import dev.sixik.unigui.api.layout.EdgeInsets;
import dev.sixik.unigui.api.math.MutableColor;
import dev.sixik.unigui.api.widget.Widget;
import dev.sixik.unigui.widgets.containers.HBox;
import dev.sixik.unigui.widgets.interaction.Button;
import dev.sixik.unigui.widgets.interaction.TextField;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Search and editor actions rendered with native UniGUI controls. */
public class ShopToolPanelElement extends ShopWidgetGroup implements WidgetContextRender, UIDisposable {
    @Getter private final @NotNull ShopScreenElement screen;
    private final UIEventScope eventScope = new UIEventScope();

    public ShopToolPanelElement(@NotNull ShopScreenElement screen) {
        this.screen = screen;
        background(MutableColor.fromArgb(0xE51B1D24));
        border(MutableColor.fromArgb(0xFF444A58));
        radius(4.0f);
        buildUi();
    }

    private void buildUi() {
        HBox row = new HBox();
        row.spacing(6.0f);
        row.layout(style -> style.fill().padding(EdgeInsets.symmetric(4.0f, 3.0f)));

        TextField search = new TextField();
        search.placeholder(Component.translatable("shop.ui.tool_panel.search.placeholder").getString());
        search.onTextChanged(event -> ShopUIEvents.invokeSearchUpdate(event.newText()));
        search.layout(style -> style.expand());
        row.addChild(search);

        if (Minecraft.getInstance().player != null && ShopUtils.isPlayerAdmin(Minecraft.getInstance().player)) {
            Button mode = new Button(Component.translatable(screen.isEditorMode()
                    ? "shop.ui.tool_panel.button.view" : "shop.ui.tool_panel.button.edit").getString());
            mode.onClick(event -> {
                if (screen.isEditorMode()) ShopScreenController.openShop();
                else ShopScreenController.openShopEditor(this);
            });
            row.addChild(mode);
        }

        if (screen.isEditorMode()) {
            Button reset = new Button(Component.translatable("shop.ui.tool_panel.button.reset_session").getString());
            reset.onClick(event -> {
                if (screen.resetEditorSession()) ShopToasts.success(Component.translatable("shop.ui.toast.edit_session_reset"));
                else ShopToasts.error(Component.translatable("shop.ui.toast.edit_session_reset_failed"));
            });
            row.addChild(reset);

            Button save = new Button(Component.translatable("shop.ui.tool_panel.button.send_changes").getString());
            save.onClick(event -> screen.sendEditorChanges().thenAccept(success -> {
                if (success) ShopToasts.success(Component.translatable("shop.ui.toast.changes_sent"));
                else ShopToasts.error(Component.translatable("shop.ui.toast.changes_send_failed"));
            }));
            row.addChild(save);
        }
        addChild(row);
    }

    @Override public @Nullable ShopEntity getShopEntity() { return null; }
    @Override public Widget getOwner() { return this; }
    @Override public UIEventScope eventScope() { return eventScope; }
    @Override public UIEventScope screenEventScope() { return screen.screenEventScope(); }
    @Override public @Nullable ShopEditSession getEditSession() { return screen.getEditSession(); }
    public <Event> EventSubscription listenScreen(EventPtr<Event> event, DODEventBus.EventListener<Event> listener) {
        return screen.listenScreen(event, listener);
    }
    @Override public void dispose() { eventScope.close(); super.dispose(); }
}
