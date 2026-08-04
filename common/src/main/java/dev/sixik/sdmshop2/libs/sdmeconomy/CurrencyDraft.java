package dev.sixik.sdmshop2.libs.sdmeconomy;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.sixik.sdmshop2.libs.sdmeconomy.custom_currency.ExternalItemCurrency;
import dev.sixik.sdmshop2.libs.sdmeconomy.custom_currency.SimpleTextCurrency;
import dev.sixik.sdmshop2.libs.sdmeconomy.icons.CurrencyIcon;
import dev.sixik.sdmshop2.libs.sdmeconomy.icons.IconType;
import dev.sixik.sdmshop2.libs.shop.serializer.codec.FieldCodecs;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public record CurrencyDraft(
        ResourceLocation id,
        Kind kind,
        String displayName,
        String iconText,
        ItemStack itemStack
) {

    public CurrencyDraft {
        Objects.requireNonNull(id, "id");
        kind = kind == null ? Kind.TEXT : kind;
        displayName = displayName == null || displayName.isBlank() ? id.toString() : displayName.trim();
        iconText = iconText == null || iconText.isBlank() ? "$" : iconText.trim();
        itemStack = itemStack == null ? ItemStack.EMPTY : itemStack.copyWithCount(1);
    }

    public static CurrencyDraft text(ResourceLocation id, String displayName, String iconText) {
        return new CurrencyDraft(id, Kind.TEXT, displayName, iconText, ItemStack.EMPTY);
    }

    public static CurrencyDraft item(ResourceLocation id, ItemStack itemStack) {
        ItemStack safeStack = itemStack == null ? ItemStack.EMPTY : itemStack.copyWithCount(1);
        return new CurrencyDraft(id, Kind.ITEM, safeStack.getHoverName().getString(), "", safeStack);
    }

    public ICurrency toCurrency() {
        return kind == Kind.ITEM
                ? toExternalCurrency()
                : toStoredCurrency();
    }

    public IStoredCurrency toStoredCurrency() {
        return new SimpleTextCurrency(id, displayName, iconText);
    }

    public IExternalCurrency toExternalCurrency() {
        return new ExternalItemCurrency(id, itemStack);
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("id", id.toString());
        json.addProperty("kind", kind.name().toLowerCase(java.util.Locale.ROOT));
        json.addProperty("display_name", displayName);
        json.addProperty("icon_text", iconText);
        if (kind == Kind.ITEM) {
            json.add("item", FieldCodecs.ITEM_STACK_ID_NBT.toJsonElement(itemStack));
        }
        return json;
    }

    @Nullable
    public static CurrencyDraft fromJson(JsonElement element) {
        if (element == null || element.isJsonNull() || !element.isJsonObject()) {
            return null;
        }

        try {
            JsonObject json = element.getAsJsonObject();
            ResourceLocation id = json.has("id") ? ResourceLocation.tryParse(json.get("id").getAsString()) : null;
            if (id == null) {
                return null;
            }

            Kind kind = json.has("kind") ? Kind.fromName(json.get("kind").getAsString()) : Kind.TEXT;
            String displayName = json.has("display_name") ? json.get("display_name").getAsString() : id.toString();
            String iconText = json.has("icon_text") ? json.get("icon_text").getAsString() : "$";
            ItemStack itemStack = kind == Kind.ITEM && json.has("item")
                    ? FieldCodecs.ITEM_STACK_ID_NBT.fromJsonElement(json.get("item"), ItemStack.EMPTY)
                    : ItemStack.EMPTY;
            return new CurrencyDraft(id, kind, displayName, iconText, itemStack);
        } catch (Exception ignored) {
            return null;
        }
    }

    public void writeNetwork(FriendlyByteBuf buf) {
        buf.writeResourceLocation(id);
        buf.writeEnum(kind);
        buf.writeUtf(displayName);
        buf.writeUtf(iconText);
        buf.writeItem(itemStack);
    }

    public static CurrencyDraft readNetwork(FriendlyByteBuf buf) {
        ResourceLocation id = buf.readResourceLocation();
        Kind kind = buf.readEnum(Kind.class);
        String displayName = buf.readUtf();
        String iconText = buf.readUtf();
        ItemStack itemStack = buf.readItem();
        return new CurrencyDraft(id, kind, displayName, iconText, itemStack);
    }

    public static void writeList(FriendlyByteBuf buf, List<CurrencyDraft> drafts) {
        List<CurrencyDraft> safeDrafts = drafts == null ? List.of() : drafts;
        buf.writeVarInt(safeDrafts.size());
        for (CurrencyDraft draft : safeDrafts) {
            draft.writeNetwork(buf);
        }
    }

    public static List<CurrencyDraft> readList(FriendlyByteBuf buf) {
        int size = buf.readVarInt();
        List<CurrencyDraft> drafts = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            drafts.add(readNetwork(buf));
        }
        return drafts;
    }

    public static CurrencyDraft fromCurrency(ICurrency currency) {
        if (currency == null || currency.getId() == null) {
            return null;
        }

        if (currency instanceof ExternalItemCurrency itemCurrency) {
            return item(currency.getId(), itemCurrency.getItemType());
        }

        if (currency instanceof SimpleTextCurrency textCurrency) {
            return text(currency.getId(), textCurrency.getDisplayNameValue(), textCurrency.getIconText());
        }

        CurrencyIcon icon = currency.getIcon();
        if (icon != null && icon.type() == IconType.ITEM) {
            Object iconValue = icon.icon();
            if (iconValue instanceof ItemStack stack && !stack.isEmpty()) {
                return item(currency.getId(), stack);
            }
            if (iconValue instanceof Item item && item != Items.AIR) {
                return item(currency.getId(), item.getDefaultInstance());
            }
        }

        String iconText = "$";
        if (icon != null && icon.type() == IconType.TEXTURE && icon.icon() instanceof String text) {
            iconText = text;
        }
        return text(currency.getId(), currency.getDisplayName().getString(), iconText);
    }

    public enum Kind {
        TEXT,
        ITEM;

        public static Kind fromName(String value) {
            if (value == null) {
                return TEXT;
            }

            for (Kind kind : values()) {
                if (kind.name().equalsIgnoreCase(value)) {
                    return kind;
                }
            }
            return TEXT;
        }
    }
}
