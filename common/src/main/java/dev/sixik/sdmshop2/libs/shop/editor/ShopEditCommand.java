package dev.sixik.sdmshop2.libs.shop.editor;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import net.minecraft.resources.ResourceLocation;

public interface ShopEditCommand {

    ResourceLocation type();

    void apply(ShopEditContext context);

    default void validate(ShopEditContext context, ShopEditValidationReport report) {
    }

    default JsonElement serialize() {
        return JsonNull.INSTANCE;
    }
}
