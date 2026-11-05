package dev.sixik.sdmshop2.libs.shop.client.ui.root;

import dev.sixik.sdmshop2.libs.sdmeconomy.CurrencyDraft;
import dev.sixik.sdmshop2.libs.sdmeconomy.ICurrency;
import dev.sixik.sdmshop2.libs.sdmeconomy.SDMEconomyServiceClient;
import dev.sixik.sdmshop2.libs.sdmeconomy.custom_currency.ExternalItemCurrency;
import dev.sixik.sdmshop2.libs.sdmeconomy.icons.CurrencyIcon;
import dev.sixik.sdmshop2.libs.sdmeconomy.icons.IconType;
import dev.sixik.sdmshop2.libs.shop.client.ui.editor.ShopEditorState;
import dev.sixik.sdmshop2.libs.shop.client.ui.theme.ShopTheme;
import dev.sixik.sdmshop2.libs.shop.client.ui.widgets.ShopItemWidget;
import dev.sixik.unigui.api.layout.Align;
import dev.sixik.unigui.api.layout.Justify;
import dev.sixik.unigui.api.render.SimpleTextureHandle;
import dev.sixik.unigui.api.widget.Widget;
import dev.sixik.unigui.widgets.containers.Box;
import dev.sixik.unigui.widgets.containers.HBox;
import dev.sixik.unigui.widgets.containers.LinearBox;
import dev.sixik.unigui.widgets.core.Orientation;
import dev.sixik.unigui.widgets.display.ImageView;
import dev.sixik.unigui.widgets.display.Label;
import dev.sixik.unigui.widgets.display.Separator;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import org.jetbrains.annotations.Nullable;

import java.util.*;

/** Reusable player-balance view, including unsaved editor currency previews. */
public final class ShopCurrencyPanel extends LinearBox {
    /** Creates a vertical currency balance panel. */
    public ShopCurrencyPanel() {
        super(Orientation.VERTICAL);
    }

    public void refreshCurrencies() {
        this.clearChildren();
        Label yourMoneyLabel =
                new Label(Component.translatable("shop.ui.tabs.money_title").getString());
        yourMoneyLabel.color(ShopTheme.color(ShopTheme.GOLD_PRIMARY));
        this.addChild(yourMoneyLabel);
        Separator divider = new Separator();
        divider.orientation(Orientation.HORIZONTAL);
        divider.thickness(1);
        divider.color().setArgb(ShopTheme.GOLD_MUTED);
        divider.layout(style -> style.widthPercent(100).height(1).margin(1, 0).flexNone());
        this.addChild(divider);
        Minecraft mc = Minecraft.getInstance();
        Player player = mc != null ? mc.player : null;
        if (player == null) return;
        boolean isEditor = ShopEditorState.get().isEditorMode();
        Set<ResourceLocation> processedIds = new HashSet<>();
        if (isEditor) {
            for (CurrencyDraft draft : ShopEditorState.get().getCurrencyDrafts()) {
                if (draft.kind() == CurrencyDraft.Kind.DELETE) continue;
                processedIds.add(draft.id());
                ICurrency liveCur = SDMEconomyServiceClient.getCurrency(draft.id());
                addCurrencyRow(player, draft.id(), draft.displayName(), draft, liveCur);
            }
        }
        for (ICurrency currency : SDMEconomyServiceClient.getAllCurrencies().values()) {
            if (currency == null) continue;
            ResourceLocation id = currency.getId();
            if (isEditor) {
                if (processedIds.contains(id)) continue;
                if (ShopEditorState.get().isCurrencyDeleted(id)) continue;
            }
            addCurrencyRow(player, id, currency.getDisplayName().getString(), null, currency);
        }
    }

    private void addCurrencyRow(
            Player player,
            ResourceLocation currencyId,
            String displayName,
            @Nullable CurrencyDraft draft,
            @Nullable ICurrency currency) {
        HBox row = new HBox();
        row.layout(
                style ->
                        style.alignItems(Align.CENTER)
                                .justifyContent(Justify.SPACE_BETWEEN)
                                .height(18.0f)
                                .widthPercent(100.0f)
                                .flexNone());
        HBox leftPart = new HBox();
        leftPart.layout(style -> style.alignItems(Align.CENTER).gap(4.0f).flexNone());
        Widget iconWidget = buildCurrencyIconWidget(draft, currency);
        if (iconWidget != null) {
            leftPart.addChild(iconWidget);
        }
        Label nameLabel = new Label(displayName);
        nameLabel.color(ShopTheme.color(ShopTheme.TEXT_SECONDARY));
        leftPart.addChild(nameLabel);
        java.math.BigDecimal balance = java.math.BigDecimal.ZERO;
        if (currency != null) {
            java.math.BigDecimal b = SDMEconomyServiceClient.getBalance(currency, player);
            if (b != null) balance = b;
        }
        String balanceStr = balance.stripTrailingZeros().toPlainString();
        Label balanceLabel = new Label(balanceStr);
        // Нулевой баланс приглушён, ненулевой выделен золотом
        balanceLabel.color(
                ShopTheme.color(
                        balance.signum() == 0 ? ShopTheme.TEXT_MUTED : ShopTheme.TEXT_GOLD));
        row.addChild(leftPart);
        row.addChild(balanceLabel);
        this.addChild(row);
    }

    private Widget buildCurrencyIconWidget(
            @Nullable CurrencyDraft draft, @Nullable ICurrency currency) {
        // 1. Check draft if present
        if (draft != null) {
            if (draft.kind() == CurrencyDraft.Kind.ITEM
                    && draft.itemStack() != null
                    && !draft.itemStack().isEmpty()) {
                ShopItemWidget itemWidget = new ShopItemWidget(draft.itemStack(), 14.0f);
                itemWidget.decorations(false);
                itemWidget.layout(s -> s.fixed(14.0f, 14.0f).flexNone());
                return itemWidget;
            } else if (draft.kind() == CurrencyDraft.Kind.TEXTURE && draft.iconTexture() != null) {
                String tex = draft.iconTexture().toString();
                ImageView img = new ImageView(new SimpleTextureHandle(tex, 14, 14, tex));
                img.layout(s -> s.size(14.0f, 14.0f).flexNone());
                return img;
            } else if (draft.kind() == CurrencyDraft.Kind.TEXT) {
                String sym = draft.iconText();
                if (sym == null || sym.isBlank()) sym = "$";
                return createTextIconBadge(sym);
            }
        }
        // 2. Check external item currency
        if (currency instanceof ExternalItemCurrency eic && !eic.getItemType().isEmpty()) {
            ShopItemWidget itemWidget = new ShopItemWidget(eic.getItemType(), 14.0f);
            itemWidget.decorations(false);
            itemWidget.layout(s -> s.fixed(14.0f, 14.0f).flexNone());
            return itemWidget;
        }
        // 3. Check registered currency icon
        if (currency != null) {
            CurrencyIcon curIcon = currency.getIcon();
            if (curIcon != null) {
                if (curIcon.type() == IconType.ITEM) {
                    ItemStack iconStack =
                            curIcon.icon() instanceof ItemStack s
                                    ? s
                                    : (curIcon.icon() instanceof Item it
                                            ? it.getDefaultInstance()
                                            : null);
                    if (iconStack != null && !iconStack.isEmpty()) {
                        ShopItemWidget itemWidget = new ShopItemWidget(iconStack, 14.0f);
                        itemWidget.decorations(false);
                        itemWidget.layout(s -> s.fixed(14.0f, 14.0f).flexNone());
                        return itemWidget;
                    }
                } else if (curIcon.type() == IconType.TEXTURE) {
                    if (curIcon.icon() instanceof ResourceLocation loc) {
                        String tex = loc.toString();
                        ImageView img = new ImageView(new SimpleTextureHandle(tex, 14, 14, tex));
                        img.layout(s -> s.size(14.0f, 14.0f).flexNone());
                        return img;
                    } else if (curIcon.icon() instanceof String str) {
                        if (str.contains(":") || str.contains("/") || str.endsWith(".png")) {
                            ImageView img =
                                    new ImageView(new SimpleTextureHandle(str, 14, 14, str));
                            img.layout(s -> s.size(14.0f, 14.0f).flexNone());
                            return img;
                        } else {
                            return createTextIconBadge(str);
                        }
                    }
                } else if (curIcon.type() == IconType.TEXT) {
                    String sym = curIcon.icon() instanceof String str ? str : "$";
                    return createTextIconBadge(sym);
                }
            }
            // Fallback: extract symbol from currency formatting
            String sym = "$";
            try {
                sym = currency.format(java.math.BigDecimal.ONE).replaceAll("[0-9.,\\s]", "");
                if (sym.isBlank()) sym = "$";
            } catch (Exception ignored) {
            }
            return createTextIconBadge(sym);
        }
        // 4. Default fallback: Gold Nugget
        ShopItemWidget itemWidget = new ShopItemWidget(new ItemStack(Items.GOLD_NUGGET), 14.0f);
        itemWidget.decorations(false);
        itemWidget.layout(s -> s.fixed(14.0f, 14.0f).flexNone());
        return itemWidget;
    }

    private Box createTextIconBadge(String symbol) {
        Box badge = new Box();
        badge.layout(
                s ->
                        s.size(14.0f, 14.0f)
                                .alignItems(Align.CENTER)
                                .justifyContent(Justify.CENTER)
                                .flexNone());
        Label iconLbl = new Label(symbol);
        iconLbl.color(ShopTheme.color(ShopTheme.GOLD_PRIMARY));
        badge.addChild(iconLbl);
        return badge;
    }
}
