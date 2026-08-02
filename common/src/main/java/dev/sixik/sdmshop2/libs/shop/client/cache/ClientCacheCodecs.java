package dev.sixik.sdmshop2.libs.shop.client.cache;

import dev.sixik.sdmshop2.libs.shop.serializer.codec.FieldCodec;
import dev.sixik.sdmshop2.libs.shop.serializer.codec.FieldCodecs;

import java.util.List;
import java.util.UUID;

public final class ClientCacheCodecs {

    public static final FieldCodec<List<UUID>> FAVORITES = FieldCodecs.list(FieldCodecs.UUID_CODEC);
    public static final FieldCodec<List<EditorHistoryEntry>> EDITOR_HISTORY = FieldCodecs.list(EditorHistoryEntry.CODEC);

    private ClientCacheCodecs() {
    }
}
