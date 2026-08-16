package dev.sixik.sdmshop2.libs.shop.client.ui;

import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import dev.sixik.sdmshop2.libs.shop.base.ShopInstance;
import dev.sixik.sdmshop2.libs.shop.client.SDMShopClient;
import dev.sixik.sdmshop2.libs.shop.client.cache.PersistentEditSession;
import dev.sixik.sdmshop2.libs.shop.client.cache.ShopClientCache;
import dev.sixik.sdmshop2.libs.shop.client.ui.elements.ShopScreenElement;
import dev.sixik.sdmshop2.libs.shop.editor.ShopEditSession;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.ButtonWidget;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.TextLabel;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.containers.ModalWidget;
import dev.sixik.sdmshop2.utils.ShopClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public final class ShopScreenController {

    public static void openShop() {
        ShopClientUtils.openWidget(defaultGui());
    }

    public static void openShopEditor() {
        openShopEditor(ShopScreenElement.Instance);
    }

    public static void openShopEditor(@Nullable Widget owner) {
        ShopInstance shop = SDMShopClient.Shop;
        if (shop == null || shop.isNull()) {
            ShopClientUtils.openWidget(defaultGui());
            return;
        }

        PersistentEditSession persisted = ShopClientCache.getEditSession();
        if (persisted != null && persisted.shopId() != null) {
            if (persisted.shopId().equals(shop.getId())) {
                openPersistedEditor(persisted);
                return;
            }

            if (owner != null) {
                openSessionConflictModal(owner, persisted, shop);
                return;
            }
        }

        openNewEditor(shop);
    }

    private static void openNewEditor(ShopInstance shop) {
        UUID author = Minecraft.getInstance().player == null
                ? UUID.randomUUID()
                : Minecraft.getInstance().player.getUUID();
        ShopEditSession session = ShopEditSession.forShop(shop, author);
        ShopClientCache.saveEditSession(session, session.draftShop(shop.getId()));
        openEditor(session, shop.getId());
    }

    private static void openPersistedEditor(PersistentEditSession persisted) {
        ShopEditSession session = persisted.restoreSession();
        openEditor(session, persisted.shopId());
    }

    private static void openEditor(ShopEditSession session, ResourceLocation shopId) {
        ShopClientUtils.openWidget(new ShopScreenElement(session, shopId));
    }

    private static void openSessionConflictModal(Widget owner, PersistentEditSession persisted, ShopInstance currentShop) {
        ModalWidget modal = new ModalWidget(310, 138)
                .setTitle(Component.translatable("shop.ui.editor.session_conflict.title"))
                .setCloseOnEsc(true)
                .setCloseOnOutsideClick(false);
        ModalWidget opened = ModalWidget.openNested(owner, modal);
        if (opened == null) {
            return;
        }

        TextLabel warning = new TextLabel(0, 4, opened.getContentWidth(), 42,
                Component.translatable("shop.ui.editor.session_conflict.warning"));
        warning.setAutoSize(false)
                .setWrapText(true)
                .setColor(0xFFFFC95A)
                .setAlignment(TextLabel.HorizontalAlignment.CENTER, TextLabel.VerticalAlignment.CENTER);
        opened.addWidget(warning);

        TextLabel details = new TextLabel(0, 48, opened.getContentWidth(), 28,
                Component.translatable("shop.ui.editor.session_conflict.details", persisted.shopId(), currentShop.getId()));
        details.setAutoSize(false)
                .setWrapText(true)
                .setColor(0xFFAEB4C6)
                .setAlignment(TextLabel.HorizontalAlignment.CENTER, TextLabel.VerticalAlignment.CENTER);
        opened.addWidget(details);

        int buttonY = Math.max(82, opened.getContentHeight() - 24);
        int buttonWidth = Math.max(1, opened.getContentWidth() / 2 - 5);

        ButtonWidget clear = new ButtonWidget(0, buttonY, buttonWidth, 20, Component.translatable("shop.ui.editor.session_conflict.clear_old"), ignored -> {
            opened.close();
            ShopClientCache.clearEditSession();
            openNewEditor(currentShop);
        });
        clear.setClientSideWidget();
        clear.setTextPadding(4);
        clear.setMinTextScale(0.35f);

        ButtonWidget restore = new ButtonWidget(buttonWidth + 10, buttonY, buttonWidth, 20, Component.translatable("shop.ui.editor.session_conflict.continue_old"), ignored -> {
            opened.close();
            openPersistedEditor(persisted);
        });
        restore.setClientSideWidget();
        restore.setTextPadding(4);
        restore.setMinTextScale(0.35f);

        opened.addWidget(clear);
        opened.addWidget(restore);
    }

    private static WidgetGroup defaultGui() {
        return new ShopScreenElement();
    }
}
