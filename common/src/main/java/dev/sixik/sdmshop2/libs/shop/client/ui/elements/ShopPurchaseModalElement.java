package dev.sixik.sdmshop2.libs.shop.client.ui.elements;

import dev.sixik.sdmshop2.libs.sdmeconomy.ICurrency;
import dev.sixik.sdmshop2.libs.sdmeconomy.SDMEconomyServiceClient;
import dev.sixik.sdmshop2.libs.shop.base.ShopEntity;
import dev.sixik.sdmshop2.libs.shop.base.ShopOffer;
import dev.sixik.sdmshop2.libs.shop.client.SDMShopClient;
import dev.sixik.sdmshop2.libs.shop.client.sound.ShopClientSounds;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.*;
import dev.sixik.sdmshop2.libs.shop.client.ui.events.ShopUIEvents;
import dev.sixik.sdmshop2.libs.shop.client.ui.toast.ShopToasts;
import dev.sixik.sdmshop2.libs.shop.components.ItemCostComponent;
import dev.sixik.sdmshop2.libs.shop.components.ItemTagCostComponent;
import dev.sixik.sdmshop2.libs.shop.components.api.CostComponent;
import dev.sixik.sdmshop2.libs.shop.components.misc.CatalogComponent;
import dev.sixik.sdmshop2.libs.shop.components.money.MoneyCostComponent;
import dev.sixik.sdmshop2.libs.shop.components.utils.ShopComponentsUtils;
import dev.sixik.sdmshop2.libs.shop.limiter.ShopLimiters;
import dev.sixik.sdmshop2.libs.shop.network.ShopNetworkManager;
import dev.sixik.sdmshop2.utils.ShopItemHelper;
import dev.sixik.unigui.api.layout.EdgeInsets;
import dev.sixik.unigui.api.widget.Widget;
import dev.sixik.unigui.widgets.containers.HBox;
import dev.sixik.unigui.widgets.containers.VBox;
import dev.sixik.unigui.widgets.display.Label;
import dev.sixik.unigui.widgets.feedback.OverlayLayer;
import dev.sixik.unigui.widgets.feedback.WindowWidget;
import dev.sixik.unigui.widgets.interaction.Button;
import dev.sixik.unigui.widgets.interaction.TextField;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ShopPurchaseModalElement extends WindowWidget implements
        ShopUiElement,
        WidgetContextRender,
        UIDisposable {

    public static final int DEFAULT_WIDTH = 320;
    public static final int DEFAULT_HEIGHT = 252;
    public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,##0.##");

    private ShopOffer offer;
    private final String moneyGroup;
    private List<CostComponent> costs;
    private final UIEventScope eventScope = new UIEventScope();

    private int quantity = 1;
    private int maxQuantity;
    private int limitQuantity = Integer.MAX_VALUE;
    private int balanceQuantity = Integer.MAX_VALUE;

    private boolean purchasing;
    private boolean loadingPrice = true;
    private boolean priceLoadFailed;
    private boolean disposed;

    private Component actionStatus = Component.empty();
    private Map<CostComponent, Double> unitPrices = Map.of();

    public static @Nullable ShopPurchaseModalElement open(Widget owner, ShopEntity entity, String moneyGroup) {
        if (!(entity instanceof ShopOffer offer)) {
            return null;
        }

        ShopPurchaseModalElement modal = new ShopPurchaseModalElement(offer, moneyGroup);
        OverlayLayer overlay = findOverlay(owner);
        if (overlay == null && ShopScreenElement.Instance != null) overlay = ShopScreenElement.Instance.getOverlay();
        if (overlay == null) return null;
        overlay.windows().register(modal);
        modal.openModal();
        return modal;
    }

    public ShopPurchaseModalElement(ShopOffer offer, String moneyGroup) {
        this(
                offer,
                moneyGroup,
                StyleApi.getDefaultStyle(StyleApi.Category.PurchaseModal).get()
        );
    }

    public ShopPurchaseModalElement(ShopOffer offer, String moneyGroup, WidgetRender render) {
        super(Component.translatable("shop.ui.purchase.title").getString(), new VBox());
        this.offer = Objects.requireNonNull(offer, "offer");
        this.moneyGroup = moneyGroup == null ? "" : moneyGroup;
        this.costs = resolveCosts(offer);

        modal(true).fixedModal(true).resizable(false).closeOnOutsideClick(false);
        layout(style -> style.fixed(DEFAULT_WIDTH, DEFAULT_HEIGHT));

        recomputeMaxQuantity();

        registerDefaultHandlers();
        refresh(false);
        refreshRenderState();
        loadServerPrice();
    }

    protected void registerDefaultHandlers() {
        listen(ShopUIEvents.REFRESH_UI, this::handleRefresh);
    }

    protected void handleRefresh(ShopUIEvents.RefreshUI event) {
        if (event.affectsCurrencies()) {
            recomputeMaxQuantity();
            refreshRenderState();
        }

        if (!shouldRefreshOffer(event)) {
            return;
        }

        if (!reloadOfferFromShop(true)) {
            return;
        }

        actionStatus = Component.empty();
        loadingPrice = true;
        priceLoadFailed = false;
        unitPrices = Map.of();
        recomputeMaxQuantity();
        refresh(event.relayout());
        refreshRenderState();
        loadServerPrice();
    }

    protected boolean shouldRefreshOffer(ShopUIEvents.RefreshUI event) {
        if (!event.affectsOffers()) {
            return false;
        }

        if (event.target() == ShopUIEvents.RefreshTarget.OFFER) {
            return event.affectsOffer(offer.getUUID());
        }

        if (event.target() == ShopUIEvents.RefreshTarget.CATEGORY) {
            return event.affectsCategory(getOfferCategory(offer));
        }

        return true;
    }

    protected boolean reloadOfferFromShop(boolean closeIfMissing) {
        ShopOffer currentOffer = SDMShopClient.Shop == null
                ? null
                : SDMShopClient.Shop.getEntries().getEntry(offer.getUUID());

        if (currentOffer == null) {
            if (closeIfMissing) {
                close();
                ShopToasts.warning(Component.translatable("shop.ui.toast.offer_missing"));
            }
            return false;
        }

        offer = currentOffer;
        costs = resolveCosts(currentOffer);
        return true;
    }

    protected List<CostComponent> resolveCosts(ShopOffer offer) {
        return ShopComponentsUtils.getCostComponentsByGroups(offer)
                .getOrDefault(this.moneyGroup, Collections.emptyList());
    }

    protected @Nullable CatalogComponent getOfferCategory(ShopOffer offer) {
        return offer.getComponent(CatalogComponent.class).orElse(null);
    }

    @Override public void alightWidget() { invalidate(dev.sixik.unigui.api.core.InvalidationFlags.LAYOUT); }

    public ShopPurchaseModalElement refresh() {
        return refresh(true);
    }

    public ShopPurchaseModalElement refresh(boolean relayout) {
        VBox body = new VBox();
        body.spacing(6.0f);
        body.layout(style -> style.fill().padding(EdgeInsets.all(8.0f)));
        for (CostComponent cost : costs) body.addChild(new Label(getCostLine(cost).getString()).wrap(true));
        body.addChild(new Label(getAvailabilityText().getString()));

        HBox quantityRow = new HBox();
        quantityRow.spacing(5.0f);
        Button minimum = new Button("Min");
        minimum.onClick(event -> setQuantity(1));
        Button minus = new Button("−");
        minus.onClick(event -> setQuantity(quantity - 1));
        TextField amount = new TextField(String.valueOf(quantity));
        amount.onTextChanged(event -> setQuantityFromInput(event.newText()));
        amount.layout(style -> style.expand());
        Button plus = new Button("+");
        plus.onClick(event -> setQuantity(quantity + 1));
        Button maximum = new Button("Max");
        maximum.onClick(event -> setQuantity(maxQuantity));
        quantityRow.addChild(minimum); quantityRow.addChild(minus); quantityRow.addChild(amount); quantityRow.addChild(plus); quantityRow.addChild(maximum);
        body.addChild(quantityRow);
        body.addChild(new Label(getTotalText().getString()).wrap(true));
        Component status = getStatusText();
        if (!status.getString().isBlank()) body.addChild(new Label(status.getString()).wrap(true));
        Button confirm = new Button(Component.translatable("shop.ui.purchase.confirm").getString());
        confirm.enabled(!purchasing && quantity > 0 && quantity <= maxQuantity);
        confirm.onClick(event -> confirmPurchase());
        body.addChild(confirm);
        content(body);
        if (relayout) alightWidget();
        return this;
    }

    public void refreshRenderState() {
        refresh(true);
    }

    public void setQuantity(int quantity) {
        actionStatus = Component.empty();

        if (maxQuantity <= 0) {
            this.quantity = 0;
        } else {
            this.quantity = Mth.clamp(quantity, 1, maxQuantity);
        }

        refreshRenderState();
    }

    public void setQuantityFromInput(String rawValue) {
        if (rawValue == null || rawValue.isBlank()) {
            return;
        }

        try {
            setQuantity(Integer.parseInt(rawValue));
        } catch (NumberFormatException ignored) {
            refreshRenderState();
        }
    }

    public void confirmPurchase() {
        if (purchasing || quantity <= 0 || quantity > maxQuantity) {
            return;
        }

        purchasing = true;
        actionStatus = Component.empty();
        refreshRenderState();

        ShopNetworkManager.purchaseOffer(offer, moneyGroup, quantity).whenComplete((success, throwable) -> {
            Minecraft.getInstance().execute(() -> {
                if (disposed) {
                    return;
                }

                purchasing = false;

                if (throwable != null) {
                    actionStatus = Component.translatable("shop.ui.purchase.status.failed_with_message", throwable.getMessage());
                    ShopClientSounds.playPurchaseFail();
                    ShopToasts.error(actionStatus);
                    refreshRenderState();
                    return;
                }

                if (Boolean.TRUE.equals(success)) {
                    ShopClientSounds.playPurchaseSuccess();
                    ShopToasts.success(Component.translatable("shop.ui.toast.purchase_complete"));
                    close();
                    return;
                }

                actionStatus = Component.translatable("shop.ui.purchase.status.failed");
                ShopClientSounds.playPurchaseFail();
                ShopToasts.error(actionStatus);
                refreshRenderState();
            });
        });
    }

    public Component getStatusText() {
        if (purchasing) {
            return Component.translatable("shop.ui.purchase.status.sending");
        }
        if (!actionStatus.getString().isEmpty()) {
            return actionStatus;
        }
        if (maxQuantity <= 0) {
            return Component.translatable("shop.ui.purchase.status.not_enough_balance_or_limit");
        }
        if (loadingPrice) {
            return Component.translatable("shop.ui.purchase.status.loading_actual_price");
        }
        if (priceLoadFailed) {
            return Component.translatable("shop.ui.purchase.status.local_price");
        }
        if (quantity > maxQuantity) {
            return Component.translatable("shop.ui.purchase.status.quantity_exceeds_limit");
        }

        return Component.empty();
    }

    public Component getCostLine(CostComponent cost) {
        double unit = getUnitAmount(cost);
        double total = unit * Math.max(0, quantity);
        String amount = formatCost(cost, total);
        String balance = getBalanceText(cost);

        if (balance.isEmpty()) {
            return Component.literal(amount);
        }

        return Component.translatable("shop.ui.purchase.balance_line", amount, balance);
    }

    public int getCostLineColor(CostComponent cost) {
        if (cost instanceof MoneyCostComponent moneyCost) {
            ICurrency currency = currency(moneyCost.getMoneyId());
            Player player = Minecraft.getInstance().player;

            if (currency != null && player != null) {
                double required = getUnitAmount(cost) * Math.max(0, quantity);
                if (SDMEconomyServiceClient.getBalance(currency, player).doubleValue() < required) {
                    return 0xFFFF7777;
                }
            }
        } else if (cost instanceof ItemCostComponent itemCost) {
            Player player = Minecraft.getInstance().player;
            int required = itemCost.requiredAmount(getUnitAmount(cost) * Math.max(0, quantity));
            if (player != null && required >= 0 && itemCost.countAvailable(player) < required) {
                return 0xFFFF7777;
            }
        } else if (cost instanceof ItemTagCostComponent itemTagCost) {
            Player player = Minecraft.getInstance().player;
            int required = itemTagCost.requiredAmount(getUnitAmount(cost) * Math.max(0, quantity));
            if (player != null && required >= 0 && ShopItemHelper.countItem(player.getInventory(), itemTagCost.getTagKey()) < required) {
                return 0xFFFF7777;
            }
        }

        return 0xFFFFFFFF;
    }

    public Component getTotalText() {
        if (costs.isEmpty()) {
            return Component.translatable("shop.ui.purchase.free");
        }

        StringBuilder expression = new StringBuilder();
        double numericTotal = 0.0D;
        CostComponent totalFormatCost = null;
        boolean sameMoneyCurrency = true;
        ResourceLocation firstMoneyId = null;

        for (CostComponent cost : costs) {
            double total = getUnitAmount(cost) * Math.max(0, quantity);

            if (!expression.isEmpty()) {
                expression.append(" + ");
            }

            expression.append(formatCost(cost, total));
            numericTotal += total;

            if (totalFormatCost == null) {
                totalFormatCost = cost;
            }

            if (cost instanceof MoneyCostComponent moneyCost) {
                if (firstMoneyId == null) {
                    firstMoneyId = moneyCost.getMoneyId();
                } else if (!firstMoneyId.equals(moneyCost.getMoneyId())) {
                    sameMoneyCurrency = false;
                }
            } else {
                sameMoneyCurrency = false;
            }
        }

        String total = sameMoneyCurrency && totalFormatCost != null
                ? formatCost(totalFormatCost, numericTotal)
                : DECIMAL_FORMAT.format(numericTotal);

        return Component.translatable("shop.ui.purchase.total_expression", expression.toString(), total);
    }

    public Component getAvailabilityText() {
        return Component.translatable("shop.ui.purchase.availability", formatAvailable(maxQuantity), formatCap(limitQuantity));
    }

    public double getUnitAmount(CostComponent cost) {
        Double actual = unitPrices.get(cost);
        return sanitize(actual == null ? cost.getBaseAmount() : actual);
    }

    public String formatCost(CostComponent cost, double amount) {
        if (cost instanceof MoneyCostComponent moneyCost) {
            ICurrency currency = currency(moneyCost.getMoneyId());
            if (currency != null) {
                return currency.format(BigDecimal.valueOf(amount));
            }
        } else if (cost instanceof ItemCostComponent itemCost) {
            return DECIMAL_FORMAT.format(Math.max(0, itemCost.requiredAmount(amount)));
        } else if (cost instanceof ItemTagCostComponent itemTagCost) {
            return DECIMAL_FORMAT.format(Math.max(0, itemTagCost.requiredAmount(amount)));
        }

        return DECIMAL_FORMAT.format(amount);
    }

    public String getBalanceText(CostComponent cost) {
        if (cost instanceof MoneyCostComponent moneyCost) {
            ICurrency currency = currency(moneyCost.getMoneyId());
            Player player = Minecraft.getInstance().player;

            if (currency != null && player != null) {
                return currency.format(SDMEconomyServiceClient.getBalance(currency, player));
            }
        } else if (cost instanceof ItemCostComponent itemCost) {
            Player player = Minecraft.getInstance().player;
            if (player != null) {
                return DECIMAL_FORMAT.format(itemCost.countAvailable(player));
            }
        } else if (cost instanceof ItemTagCostComponent itemTagCost) {
            Player player = Minecraft.getInstance().player;
            if (player != null) {
                return DECIMAL_FORMAT.format(ShopItemHelper.countItem(player.getInventory(), itemTagCost.getTagKey()));
            }
        }

        return "";
    }

    private void loadServerPrice() {
        ShopNetworkManager.getOfferPrice(offer, moneyGroup).whenComplete((prices, throwable) -> {
            Minecraft.getInstance().execute(() -> {
                if (disposed) {
                    return;
                }

                loadingPrice = false;
                priceLoadFailed = throwable != null || ((prices == null || prices.isEmpty()) && !costs.isEmpty());
                if (throwable != null) {
                    ShopToasts.warning(Component.translatable("shop.ui.toast.actual_price_load_failed"));
                }

                if (!priceLoadFailed && prices != null && !prices.isEmpty()) {
                    unitPrices = prices;
                }

                recomputeMaxQuantity();
                refreshRenderState();
            });
        });
    }

    private void recomputeMaxQuantity() {
        maxQuantity = Math.max(0, computeMaxQuantity());

        if (maxQuantity <= 0) {
            quantity = 0;
        } else if (quantity <= 0) {
            quantity = 1;
        } else {
            quantity = Mth.clamp(quantity, 1, maxQuantity);
        }
    }

    private int computeMaxQuantity() {
        Player player = Minecraft.getInstance().player;
        int max = Integer.MAX_VALUE;
        int limitMax = Integer.MAX_VALUE;
        int balanceMax = Integer.MAX_VALUE;

        if (player != null) {
            ShopLimiters.LimitSnapshot limitSnapshot = ShopLimiters.getSnapshot(offer, player);
            if (limitSnapshot.isLimited()) {
                int available = Math.max(0, limitSnapshot.getAvailable());
                limitMax = available;
                max = Math.min(max, available);
            }

            for (CostComponent cost : costs) {
                double unit = getUnitAmount(cost);

                if (cost instanceof MoneyCostComponent moneyCost) {
                    ICurrency currency = currency(moneyCost.getMoneyId());
                    if (currency == null || unit <= 0.0D) {
                        continue;
                    }

                    int affordable = (int) Math.floor(SDMEconomyServiceClient.getBalance(currency, player).doubleValue() / unit);
                    balanceMax = Math.min(balanceMax, Math.max(0, affordable));
                    max = Math.min(max, Math.max(0, affordable));
                } else if (cost instanceof ItemCostComponent itemCost) {
                    int required = itemCost.requiredAmount(unit);
                    if (required <= 0) {
                        continue;
                    }

                    int affordable = itemCost.countAvailable(player) / required;
                    balanceMax = Math.min(balanceMax, Math.max(0, affordable));
                    max = Math.min(max, Math.max(0, affordable));
                } else if (cost instanceof ItemTagCostComponent itemTagCost) {
                    int required = itemTagCost.requiredAmount(unit);
                    if (required <= 0) {
                        continue;
                    }

                    int affordable = ShopItemHelper.countItem(player.getInventory(), itemTagCost.getTagKey()) / required;
                    balanceMax = Math.min(balanceMax, Math.max(0, affordable));
                    max = Math.min(max, Math.max(0, affordable));
                }
            }
        }

        if (max == Integer.MAX_VALUE) {
            max = 999;
        }

        limitQuantity = limitMax;
        balanceQuantity = balanceMax;

        return Math.max(0, max);
    }

    private static String formatAvailable(int value) {
        return value >= 999 ? "999+" : String.valueOf(value);
    }

    private static Component formatCap(int value) {
        return value == Integer.MAX_VALUE
                ? Component.translatable("shop.ui.purchase.no_cap")
                : Component.literal(formatAvailable(value));
    }

    private static double sanitize(double value) {
        return Double.isFinite(value) && value > 0.0D ? value : 0.0D;
    }

    private @Nullable ICurrency currency(ResourceLocation id) {
        return SDMEconomyServiceClient.getCurrency(id);
    }

    public ShopOffer getOffer() {
        return offer;
    }

    public String getMoneyGroup() {
        return moneyGroup;
    }

    public List<CostComponent> getCosts() {
        return costs;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getMaxQuantity() {
        return maxQuantity;
    }

    public int getLimitQuantity() {
        return limitQuantity;
    }

    public int getBalanceQuantity() {
        return balanceQuantity;
    }

    public boolean isPurchasing() {
        return purchasing;
    }

    public boolean isLoadingPrice() {
        return loadingPrice;
    }

    public boolean isPriceLoadFailed() {
        return priceLoadFailed;
    }

    @Override
    public @Nullable ShopEntity getShopEntity() {
        return offer;
    }

    @Override
    public Widget getOwner() {
        return this;
    }

    @Override
    public UIEventScope eventScope() {
        return eventScope;
    }

    private static @Nullable OverlayLayer findOverlay(Widget owner) {
        Widget current = owner;
        while (current != null) {
            if (current instanceof OverlayLayer layer) return layer;
            current = current.parent();
        }
        return null;
    }

    @Override
    public ShopPurchaseModalElement close() {
        dispose();
        super.close();
        return this;
    }

    @Override
    public void dispose() {
        if (disposed) {
            return;
        }

        disposed = true;
        eventScope.close();
        super.dispose();
    }
}
