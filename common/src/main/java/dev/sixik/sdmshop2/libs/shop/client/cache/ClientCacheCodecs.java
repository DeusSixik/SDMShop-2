package dev.sixik.sdmshop2.libs.shop.client.cache;

import dev.sixik.sdmshop2.libs.shop.serializer.codec.FieldCodec;
import dev.sixik.sdmshop2.libs.shop.serializer.codec.FieldCodecs;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.UUID;

public final class ClientCacheCodecs {

    public static final FieldCodec<List<UUID>> FAVORITES = FieldCodecs.list(FieldCodecs.UUID_CODEC);
    public static final FieldCodec<List<ResourceLocation>> COMPONENT_FAVORITES = FieldCodecs.list(FieldCodecs.RESOURCE_LOCATION);
    public static final FieldCodec<List<EditorHistoryEntry>> EDITOR_HISTORY = FieldCodecs.list(EditorHistoryEntry.CODEC);
    public static final FieldCodec<PersistentEditSession> EDIT_SESSION = PersistentEditSession.CODEC;

    private ClientCacheCodecs() {
    }
}
