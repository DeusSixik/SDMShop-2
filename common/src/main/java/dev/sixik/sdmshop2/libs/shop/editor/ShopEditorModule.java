package dev.sixik.sdmshop2.libs.shop.editor;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public interface ShopEditorModule {

    ResourceLocation id();

    default Component title() {
        return Component.literal(id().toString());
    }

    default int priority() {
        return 1000;
    }

    default boolean supports(ShopEditSession session) {
        return true;
    }

    default void onSessionCreated(ShopEditorModuleContext context) {
    }

    default void validate(ShopEditSession session, ShopEditValidationReport report) {
    }
}
