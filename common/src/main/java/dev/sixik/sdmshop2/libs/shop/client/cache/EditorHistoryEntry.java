package dev.sixik.sdmshop2.libs.shop.client.cache;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import dev.sixik.sdmshop2.libs.shop.serializer.codec.FieldCodec;
import dev.sixik.sdmshop2.libs.shop.serializer.codec.FieldCodecs;
import net.minecraft.network.FriendlyByteBuf;

import java.util.Objects;
import java.util.UUID;

public record EditorHistoryEntry(
        long timestamp,
        String entityType,
        UUID entityId,
        String summary,
        String sessionId
) {

    public static final FieldCodec<EditorHistoryEntry> CODEC = FieldCodec.<EditorHistoryEntry>builder()
            .schema("editor_history_entry")
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
            .jsonElement(EditorHistoryEntry::toJsonElement, EditorHistoryEntry::fromJsonElement)
            .network(EditorHistoryEntry::writeNetwork, EditorHistoryEntry::readNetwork)
            .build();

    public EditorHistoryEntry {
        entityType = entityType == null ? "" : entityType;
        summary = summary == null ? "" : summary;
        sessionId = sessionId == null ? "" : sessionId;
    }

    public boolean matchesEntity(EditorHistoryEntry other) {
        if (other == null) {
            return false;
        }
        if (entityId != null && other.entityId != null) {
            return Objects.equals(entityType, other.entityType) && Objects.equals(entityId, other.entityId);
        }
        return Objects.equals(entityType, other.entityType) && Objects.equals(summary, other.summary);
    }

    private static JsonElement toJsonElement(EditorHistoryEntry value) {
        if (value == null) {
            return JsonNull.INSTANCE;
        }

        JsonObject object = new JsonObject();
        object.addProperty("timestamp", value.timestamp);
        object.addProperty("entity_type", value.entityType);
        if (value.entityId != null) {
            object.addProperty("entity_id", value.entityId.toString());
        }
        object.addProperty("summary", value.summary);
        object.addProperty("session_id", value.sessionId);
        return object;
    }

    private static EditorHistoryEntry fromJsonElement(JsonElement element, EditorHistoryEntry defaultValue) {
        if (element == null || element.isJsonNull() || !element.isJsonObject()) {
            return defaultValue;
        }

        JsonObject object = element.getAsJsonObject();
        long timestamp = object.has("timestamp") ? object.get("timestamp").getAsLong() : 0L;
        String entityType = object.has("entity_type") ? object.get("entity_type").getAsString() : "";
        UUID entityId = null;
        if (object.has("entity_id") && !object.get("entity_id").isJsonNull()) {
            entityId = FieldCodecs.UUID_CODEC.fromJsonElement(object.get("entity_id"), null);
        }
        String summary = object.has("summary") ? object.get("summary").getAsString() : "";
        String sessionId = object.has("session_id") ? object.get("session_id").getAsString() : "";

        return new EditorHistoryEntry(timestamp, entityType, entityId, summary, sessionId);
    }

    private static void writeNetwork(FriendlyByteBuf buf, EditorHistoryEntry value) {
        EditorHistoryEntry safeValue = value == null
                ? new EditorHistoryEntry(0L, "", null, "", "")
                : value;

        buf.writeVarLong(safeValue.timestamp);
        buf.writeUtf(safeValue.entityType);
        buf.writeBoolean(safeValue.entityId != null);
        if (safeValue.entityId != null) {
            buf.writeUUID(safeValue.entityId);
        }
        buf.writeUtf(safeValue.summary);
        buf.writeUtf(safeValue.sessionId);
    }

    private static EditorHistoryEntry readNetwork(FriendlyByteBuf buf) {
        long timestamp = buf.readVarLong();
        String entityType = buf.readUtf();
        UUID entityId = buf.readBoolean() ? buf.readUUID() : null;
        String summary = buf.readUtf();
        String sessionId = buf.readUtf();
        return new EditorHistoryEntry(timestamp, entityType, entityId, summary, sessionId);
    }
}
