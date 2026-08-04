package dev.sixik.sdmshop2.libs.shop.client.cache;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import dev.sixik.sdmshop2.libs.sdmeconomy.CurrencyDraft;
import dev.sixik.sdmshop2.libs.shop.base.ShopInstance;
import dev.sixik.sdmshop2.libs.shop.editor.ShopEditHistoryEntry;
import dev.sixik.sdmshop2.libs.shop.editor.ShopEditSession;
import dev.sixik.sdmshop2.libs.shop.serializer.codec.FieldCodec;
import dev.sixik.sdmshop2.libs.shop.serializer.codec.FieldCodecs;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record PersistentEditSession(
        UUID sessionId,
        UUID baseRevision,
        UUID author,
        long createdAt,
        long updatedAt,
        ResourceLocation shopId,
        ShopInstance draftShop,
        List<ShopEditHistoryEntry> history,
        List<CurrencyDraft> draftCurrencies
) {

    public static final FieldCodec<PersistentEditSession> CODEC = FieldCodec.<PersistentEditSession>builder()
            .schema("persistent_edit_session")
            .json(
                    (json, key, value) -> {
                        if (value != null) {
                            json.add(key, toJsonElement(value));
                        }
                    },
                    (json, key, defaultValue) -> json.has(key)
                            ? fromJsonElement(json.get(key), defaultValue)
                            : defaultValue
            )
            .jsonElement(PersistentEditSession::toJsonElement, PersistentEditSession::fromJsonElement)
            .network(PersistentEditSession::writeNetwork, PersistentEditSession::readNetwork)
            .build();

    public PersistentEditSession {
        history = history == null ? List.of() : List.copyOf(history);
        draftCurrencies = draftCurrencies == null ? List.of() : List.copyOf(draftCurrencies);
    }

    public static PersistentEditSession fromSession(ShopEditSession session, ShopInstance draftShop) {
        if (session == null || draftShop == null) {
            return null;
        }

        return new PersistentEditSession(
                session.sessionId(),
                session.baseRevision(),
                session.author(),
                session.createdAt(),
                session.updatedAt(),
                draftShop.getId(),
                ShopEditSession.copyShop(draftShop),
                session.history(),
                session.currencyDrafts()
        );
    }

    public ShopEditSession restoreSession() {
        ShopInstance draft = ShopEditSession.copyShop(draftShop);
        return ShopEditSession.restore(sessionId, baseRevision, author, createdAt, updatedAt, draft, history, draftCurrencies);
    }

    private static JsonElement toJsonElement(PersistentEditSession value) {
        if (value == null || value.draftShop == null) {
            return JsonNull.INSTANCE;
        }

        JsonObject object = new JsonObject();
        object.addProperty("session_id", value.sessionId.toString());
        object.addProperty("base_revision", value.baseRevision.toString());
        object.addProperty("author", value.author.toString());
        object.addProperty("created_at", value.createdAt);
        object.addProperty("updated_at", value.updatedAt);
        object.addProperty("shop_id", value.shopId.toString());
        object.add("draft_shop", value.draftShop.serialize());

        JsonArray historyArray = new JsonArray();
        for (ShopEditHistoryEntry entry : value.history) {
            historyArray.add(historyToJson(entry));
        }
        object.add("history", historyArray);

        JsonArray currenciesArray = new JsonArray();
        for (CurrencyDraft draft : value.draftCurrencies) {
            currenciesArray.add(draft.toJson());
        }
        object.add("draft_currencies", currenciesArray);
        return object;
    }

    private static PersistentEditSession fromJsonElement(JsonElement element, PersistentEditSession defaultValue) {
        if (element == null || element.isJsonNull() || !element.isJsonObject()) {
            return defaultValue;
        }

        try {
            JsonObject object = element.getAsJsonObject();
            UUID sessionId = UUID.fromString(object.get("session_id").getAsString());
            UUID baseRevision = UUID.fromString(object.get("base_revision").getAsString());
            UUID author = UUID.fromString(object.get("author").getAsString());
            long createdAt = object.has("created_at") ? object.get("created_at").getAsLong() : 0L;
            long updatedAt = object.has("updated_at") ? object.get("updated_at").getAsLong() : createdAt;
            ResourceLocation shopId = ResourceLocation.tryParse(object.get("shop_id").getAsString());
            ShopInstance draftShop = ShopInstance.fromJson(object.get("draft_shop"));
            draftShop.setShouldSave(false);

            List<ShopEditHistoryEntry> history = new ArrayList<>();
            if (object.has("history") && object.get("history").isJsonArray()) {
                for (JsonElement historyElement : object.getAsJsonArray("history")) {
                    ShopEditHistoryEntry entry = historyFromJson(historyElement);
                    if (entry != null) {
                        history.add(entry);
                    }
                }
            }

            List<CurrencyDraft> draftCurrencies = new ArrayList<>();
            if (object.has("draft_currencies") && object.get("draft_currencies").isJsonArray()) {
                for (JsonElement currencyElement : object.getAsJsonArray("draft_currencies")) {
                    CurrencyDraft draft = CurrencyDraft.fromJson(currencyElement);
                    if (draft != null) {
                        draftCurrencies.add(draft);
                    }
                }
            }

            return new PersistentEditSession(sessionId, baseRevision, author, createdAt, updatedAt, shopId, draftShop, history, draftCurrencies);
        } catch (Exception ignored) {
            return defaultValue;
        }
    }

    private static JsonObject historyToJson(ShopEditHistoryEntry entry) {
        JsonObject object = new JsonObject();
        if (entry == null) {
            return object;
        }

        object.addProperty("timestamp", entry.timestamp());
        object.addProperty("action", entry.action());
        object.addProperty("entity_type", entry.entityType());
        if (entry.entityId() != null) {
            object.addProperty("entity_id", entry.entityId().toString());
        }
        if (entry.componentType() != null) {
            object.addProperty("component_type", entry.componentType().toString());
        }
        object.addProperty("summary", entry.summary());
        return object;
    }

    @Nullable
    private static ShopEditHistoryEntry historyFromJson(JsonElement element) {
        if (element == null || element.isJsonNull() || !element.isJsonObject()) {
            return null;
        }

        JsonObject object = element.getAsJsonObject();
        long timestamp = object.has("timestamp") ? object.get("timestamp").getAsLong() : 0L;
        String action = object.has("action") ? object.get("action").getAsString() : "";
        String entityType = object.has("entity_type") ? object.get("entity_type").getAsString() : "";
        UUID entityId = object.has("entity_id") && !object.get("entity_id").isJsonNull()
                ? FieldCodecs.UUID_CODEC.fromJsonElement(object.get("entity_id"), null)
                : null;
        ResourceLocation componentType = object.has("component_type") && !object.get("component_type").isJsonNull()
                ? ResourceLocation.tryParse(object.get("component_type").getAsString())
                : null;
        String summary = object.has("summary") ? object.get("summary").getAsString() : "";
        return new ShopEditHistoryEntry(timestamp, action, entityType, entityId, componentType, summary);
    }

    private static void writeNetwork(FriendlyByteBuf buf, PersistentEditSession value) {
        JsonElement json = toJsonElement(value);
        buf.writeUtf(json.toString());
    }

    private static PersistentEditSession readNetwork(FriendlyByteBuf buf) {
        return fromJsonElement(com.google.gson.JsonParser.parseString(buf.readUtf()), null);
    }
}
