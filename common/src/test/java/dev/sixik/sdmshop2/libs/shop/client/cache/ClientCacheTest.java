package dev.sixik.sdmshop2.libs.shop.client.cache;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ClientCacheTest {

    @TempDir
    Path tempDir;

    @Test
    void savesAndLoadsGenericValues() throws Exception {
        Path file = tempDir.resolve("client_cache.json");
        UUID offerId = UUID.randomUUID();
        EditorHistoryEntry entry = new EditorHistoryEntry(
                123L,
                "dev.sixik.sdmshop2.libs.shop.base.ShopOffer",
                offerId,
                "Test offer",
                "session"
        );

        ClientCache cache = ClientCache.load(file);
        cache.set(ClientCacheKeys.FAVORITES, ClientCacheCodecs.FAVORITES, List.of(offerId));
        cache.set(ClientCacheKeys.EDITOR_HISTORY, ClientCacheCodecs.EDITOR_HISTORY, List.of(entry));
        cache.saveNow();

        assertTrue(Files.exists(file));

        ClientCache loaded = ClientCache.load(file);
        assertEquals(List.of(offerId), loaded.get(ClientCacheKeys.FAVORITES, ClientCacheCodecs.FAVORITES, List.of()));
        assertEquals(List.of(entry), loaded.get(ClientCacheKeys.EDITOR_HISTORY, ClientCacheCodecs.EDITOR_HISTORY, List.of()));
        assertFalse(loaded.isDirty());
    }

    @Test
    void returnsDefaultForMissingOrMalformedValue() throws Exception {
        Path file = tempDir.resolve("client_cache.json");
        Files.writeString(file, "{\"favorites\": \"not-an-array\"}");

        ClientCache loaded = ClientCache.load(file);

        assertEquals(List.of(), loaded.get(ClientCacheKeys.FAVORITES, ClientCacheCodecs.FAVORITES, List.of()));
        assertFalse(loaded.isDirty());
    }
}
