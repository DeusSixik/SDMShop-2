package dev.sixik.sdmshop2.libs.shop.client.ui.editor.preset;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import dev.sixik.sdmshop2.libs.shop.base.ShopOffer;
import dev.sixik.sdmshop2.libs.shop.components.misc.CatalogComponent;
import dev.sixik.sdmshop2.libs.shop.components.misc.NameComponent;
import dev.sixik.sdmshop2.libs.shop.serializer.codec.FieldCodec;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.Items;

import java.util.UUID;

public record ShopOfferPreset(
        String id, String name, String description, boolean builtIn, JsonObject templateJson) {

    public static final FieldCodec<ShopOfferPreset> CODEC =
            FieldCodec.<ShopOfferPreset>builder()
                    .schema("shop_offer_preset")
                    .json(
                            (json, key, value) -> {
                                if (value != null) {
                                    json.add(key, toJsonElement(value));
                                }
                            },
                            (json, key, defaultValue) ->
                                    json.has(key)
                                            ? fromJsonElement(json.get(key), defaultValue)
                                            : defaultValue)
                    .jsonElement(ShopOfferPreset::toJsonElement, ShopOfferPreset::fromJsonElement)
                    .network(ShopOfferPreset::writeNetwork, ShopOfferPreset::readNetwork)
                    .build();

    public ShopOfferPreset {
        id = id == null ? UUID.randomUUID().toString() : id;
        name = name == null ? "Custom Preset" : name;
        description = description == null ? "" : description;
        templateJson = templateJson == null ? new JsonObject() : templateJson;
    }

    public ShopOffer createOffer() {
        UUID newUuid = UUID.randomUUID();
        ShopOffer offer = ShopOffer.create(newUuid, false);
        if (templateJson != null && templateJson.size() > 0) {
            try {
                JsonObject copy = templateJson.deepCopy();
                copy.addProperty("uuid", newUuid.toString());
                offer.deserialize(copy);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (offer.getComponent(NameComponent.class).isEmpty()) {
            offer.addComponent(new NameComponent(name.isBlank() ? "New Offer" : name));
        }
        if (offer.getComponent(CatalogComponent.class).isEmpty()) {
            CatalogComponent cat = new CatalogComponent("resources");
            cat.setIconType(CatalogComponent.IconType.ITEM);
            cat.setIconItem(Items.DIAMOND.getDefaultInstance());
            offer.addComponent(cat);
        }

        return offer;
    }

    private static JsonElement toJsonElement(ShopOfferPreset value) {
        if (value == null) {
            return JsonNull.INSTANCE;
        }

        JsonObject object = new JsonObject();
        object.addProperty("id", value.id);
        object.addProperty("name", value.name);
        object.addProperty("description", value.description);
        object.addProperty("built_in", value.builtIn);
        if (value.templateJson != null) {
            object.add("template", value.templateJson);
        }
        return object;
    }

    private static ShopOfferPreset fromJsonElement(
            JsonElement element, ShopOfferPreset defaultValue) {
        if (element == null || element.isJsonNull() || !element.isJsonObject()) {
            return defaultValue;
        }

        JsonObject object = element.getAsJsonObject();
        String id =
                object.has("id") ? object.get("id").getAsString() : UUID.randomUUID().toString();
        String name = object.has("name") ? object.get("name").getAsString() : "Custom Preset";
        String description =
                object.has("description") ? object.get("description").getAsString() : "";
        boolean builtIn = object.has("built_in") && object.get("built_in").getAsBoolean();
        JsonObject template =
                object.has("template") && object.get("template").isJsonObject()
                        ? object.getAsJsonObject("template")
                        : new JsonObject();

        return new ShopOfferPreset(id, name, description, builtIn, template);
    }

    private static void writeNetwork(FriendlyByteBuf buf, ShopOfferPreset value) {
        ShopOfferPreset safe =
                value == null
                        ? new ShopOfferPreset(
                                UUID.randomUUID().toString(), "", "", false, new JsonObject())
                        : value;
        buf.writeUtf(safe.id);
        buf.writeUtf(safe.name);
        buf.writeUtf(safe.description);
        buf.writeBoolean(safe.builtIn);
        buf.writeUtf(safe.templateJson != null ? safe.templateJson.toString() : "{}");
    }

    private static ShopOfferPreset readNetwork(FriendlyByteBuf buf) {
        String id = buf.readUtf();
        String name = buf.readUtf();
        String desc = buf.readUtf();
        boolean builtIn = buf.readBoolean();
        String jsonStr = buf.readUtf();
        JsonObject template;
        try {
            template = JsonParser.parseString(jsonStr).getAsJsonObject();
        } catch (Exception e) {
            template = new JsonObject();
        }
        return new ShopOfferPreset(id, name, desc, builtIn, template);
    }
}
