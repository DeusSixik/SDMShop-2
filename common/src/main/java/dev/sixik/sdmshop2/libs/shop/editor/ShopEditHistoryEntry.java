package dev.sixik.sdmshop2.libs.shop.editor;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

public record ShopEditHistoryEntry(
        long timestamp,
        String action,
        String entityType,
        @Nullable UUID entityId,
        @Nullable ResourceLocation componentType,
        String summary
) {

    public ShopEditHistoryEntry {
        action = Objects.requireNonNullElse(action, "unknown");
        entityType = Objects.requireNonNullElse(entityType, "unknown");
        summary = Objects.requireNonNullElse(summary, "");
    }

    public static ShopEditHistoryEntry create(
            String action,
            String entityType,
            @Nullable UUID entityId,
            @Nullable ResourceLocation componentType,
            String summary
    ) {
        return new ShopEditHistoryEntry(
                System.currentTimeMillis(),
                action,
                entityType,
                entityId,
                componentType,
                summary
        );
    }
}
