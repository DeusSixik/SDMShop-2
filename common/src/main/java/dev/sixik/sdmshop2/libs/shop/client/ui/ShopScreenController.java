package dev.sixik.sdmshop2.libs.shop.client.ui;

import dev.sixik.sdmshop2.libs.shop.base.ShopInstance;
import dev.sixik.sdmshop2.libs.shop.client.SDMShopClient;
import dev.sixik.sdmshop2.libs.shop.client.cache.PersistentEditSession;
import dev.sixik.sdmshop2.libs.shop.client.cache.ShopClientCache;
import dev.sixik.sdmshop2.libs.shop.client.ui.elements.ShopScreenElement;
import dev.sixik.sdmshop2.libs.shop.editor.ShopEditSession;
import dev.sixik.sdmshop2.utils.ShopClientUtils;
import dev.sixik.unigui.api.screen.UniScreen;
import dev.sixik.unigui.api.widget.Widget;
import dev.sixik.unigui.widgets.containers.HBox;
import dev.sixik.unigui.widgets.containers.VBox;
import dev.sixik.unigui.widgets.display.Label;
import dev.sixik.unigui.widgets.feedback.OverlayLayer;
import dev.sixik.unigui.widgets.feedback.WindowWidget;
import dev.sixik.unigui.widgets.interaction.Button;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/** Sole entry point for the UniGUI shop screen. */
public final class ShopScreenController {
    private ShopScreenController() { }

    public static void openShop() { openScreen(new ShopScreenElement()); }
    public static void openShopEditor() { openShopEditor(ShopScreenElement.Instance); }

    public static void openShopEditor(@Nullable Widget owner) {
        ShopInstance shop = SDMShopClient.Shop;
        if (shop == null || shop.isNull()) { openShop(); return; }
        PersistentEditSession persisted = ShopClientCache.getEditSession();
        if (persisted != null && persisted.shopId() != null) {
            if (persisted.shopId().equals(shop.getId())) { openPersistedEditor(persisted); return; }
            if (owner != null && openSessionConflictModal(owner, persisted, shop)) return;
        }
        openNewEditor(shop);
    }

    private static void openNewEditor(ShopInstance shop) {
        UUID author = Minecraft.getInstance().player == null ? UUID.randomUUID() : Minecraft.getInstance().player.getUUID();
        ShopEditSession session = ShopEditSession.forShop(shop, author);
        ShopClientCache.saveEditSession(session, session.draftShop(shop.getId()));
        openEditor(session, shop.getId());
    }

    private static void openPersistedEditor(PersistentEditSession persisted) { openEditor(persisted.restoreSession(), persisted.shopId()); }
    private static void openEditor(ShopEditSession session, ResourceLocation shopId) { openScreen(new ShopScreenElement(session, shopId)); }

    private static void openScreen(ShopScreenElement root) {
        UniScreen screen = new UniScreen(Component.translatable("shop.ui.title").getString(), root)
                .pauseGame(false)
                .scaleWithGameGui(true)
                .onClose(root::dispose);
        ShopClientUtils.openUniGuiScreen(screen);
    }

    private static boolean openSessionConflictModal(Widget owner, PersistentEditSession persisted, ShopInstance currentShop) {
        OverlayLayer overlay = findOverlay(owner);
        if (overlay == null) return false;
        VBox body = new VBox(); body.spacing(8.0f);
        body.addChild(new Label(Component.translatable("shop.ui.editor.session_conflict.warning").getString()).wrap(true));
        body.addChild(new Label(Component.translatable("shop.ui.editor.session_conflict.details", persisted.shopId(), currentShop.getId()).getString()).wrap(true));
        HBox actions = new HBox(); actions.spacing(8.0f);
        WindowWidget window = new WindowWidget(Component.translatable("shop.ui.editor.session_conflict.title").getString(), body)
                .modal(true).fixedModal(true).resizable(false).closeOnOutsideClick(false);
        window.layout(style -> style.fixed(360, 170));
        Button clear = new Button(Component.translatable("shop.ui.editor.session_conflict.clear_old").getString());
        clear.onClick(event -> { window.close(); ShopClientCache.clearEditSession(); openNewEditor(currentShop); });
        Button restore = new Button(Component.translatable("shop.ui.editor.session_conflict.continue_old").getString());
        restore.onClick(event -> { window.close(); openPersistedEditor(persisted); });
        actions.addChild(clear); actions.addChild(restore); body.addChild(actions);
        overlay.windows().register(window); window.openModal();
        return true;
    }

    private static @Nullable OverlayLayer findOverlay(Widget widget) {
        Widget current = widget;
        while (current != null) { if (current instanceof OverlayLayer overlay) return overlay; current = current.parent(); }
        return null;
    }
}
