package dev.sixik.sdmshop2.libs.shop.client.ui.api;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import dev.sixik.sdmshop2.libs.shop.base.ShopOffer;
import dev.sixik.sdmshop2.libs.shop.client.ui.modal.ShopPurchaseModal;
import dev.sixik.sdmshop2.libs.shop.client.ui.root.ShopCatalogElement;
import dev.sixik.sdmshop2.libs.shop.client.ui.widgets.ShopBadge;
import dev.sixik.sdmshop2.libs.shop.client.ui.widgets.ShopComponentPanel;
import dev.sixik.sdmshop2.libs.shop.client.ui.widgets.ShopItemWidget;
import dev.sixik.sdmshop2.libs.shop.client.ui.widgets.ShopRenderWidgets;
import dev.sixik.sdmshop2.libs.shop.components.CommandRewardComponent;
import dev.sixik.sdmshop2.libs.shop.components.ItemRewardComponent;
import dev.sixik.sdmshop2.libs.shop.components.api.CostComponent;
import dev.sixik.sdmshop2.libs.shop.components.api.IComponentType;
import dev.sixik.sdmshop2.libs.shop.components.api.ShopComponent;
import dev.sixik.sdmshop2.libs.shop.components.promo.effects.DiscountComponent;
import dev.sixik.sdmshop2.libs.shop.register.ShopRegister;
import dev.sixik.unigui.api.layout.LayoutContext;
import dev.sixik.unigui.api.math.MutableRect;
import dev.sixik.unigui.api.widget.Widget;
import dev.sixik.unigui.widgets.containers.Box;
import dev.sixik.unigui.widgets.containers.PanelWidget;
import dev.sixik.unigui.widgets.containers.ScrollView;
import dev.sixik.unigui.widgets.containers.VBox;
import dev.sixik.unigui.widgets.display.Label;
import dev.sixik.unigui.widgets.display.Separator;
import dev.sixik.unigui.widgets.feedback.OverlayLayer;
import dev.sixik.unigui.widgets.interaction.Button;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

class ShopComponentRenderingTest {
    @BeforeAll
    static void initialize() {
        ShopRegister.init();
    }

    @Test
    void model_customNonRewardComponent_defersRenderUntilMountAndSharesNoWidgets() {
        ShopOffer offer = ShopOffer.create(UUID.randomUUID(), false);
        AddonComponent component = new AddonComponent("addon content", false);
        offer.addComponent(component);
        offer.addComponent(new AddonComponent("hidden", true));
        ShopCatalogItem model = new ShopCatalogItem(offer);
        assertTrue(component.widgets.isEmpty(), "Constructing a model must not create widgets");
        ShopCatalogElement card = new ShopCatalogElement(model, false, null);
        assertEquals(1, component.widgets.size());
        ShopPurchaseModal modal = new ShopPurchaseModal(card, () -> null);
        assertEquals(2, component.widgets.size());
        assertNotSame(component.widgets.get(0), component.widgets.get(1));
        assertEquals(List.of("addon content"), markerTexts(card));
        assertEquals(List.of("addon content"), markerTexts(modal));
        modal.close();
        modal.close();
        assertEquals(1, component.widgets.get(1).disposals);
        assertEquals(0, component.widgets.get(0).disposals);
        card.dispose();
        assertEquals(1, component.widgets.get(0).disposals);
    }

    @Test
    void components_multipleCustomTypes_keepOrderAndSkipNullWithoutFakeBarrier() {
        ShopOffer offer = ShopOffer.create(UUID.randomUUID(), false);
        offer.addComponent(new AddonComponent("first", false));
        offer.addComponent(new AddonComponent("invisible", true));
        offer.addComponent(new AddonComponent("third", false));
        ShopCatalogElement card = new ShopCatalogElement(new ShopCatalogItem(offer), false, null);
        assertEquals(List.of("first", "third"), markerTexts(card));
        assertTrue(
                find(card, ShopItemWidget.class).isEmpty(),
                "Non-item offers must not get a barrier reward");
        card.measure(new LayoutContext(250, 1000));
        assertEquals(46, find(card, ScrollView.class).get(0).desiredSize().height(), 0.01f);
        card.dispose();
    }

    @Test
    void customCost_usesCreateRenderAndCanPayWithoutBuiltInTypeRecognition() {
        AddonCost cost = new AddonCost();
        cost.setGroupId("addon_payment");
        ShopOffer offer = ShopOffer.create(UUID.randomUUID(), false);
        offer.addComponent(cost);
        ShopCatalogItem model = new ShopCatalogItem(offer);
        assertTrue(model.renderElements.isEmpty(), "Costs belong in payment groups");
        assertEquals("addon_payment", model.costGroups.get(0).groupId());
        ShopCatalogElement card = new ShopCatalogElement(model, false, null);
        assertEquals(List.of("addon payment"), markerTexts(card));
        Player player = mock(Player.class);
        cost.allowed = true;
        assertTrue(ShopPurchasePolicy.canPurchase(null, model.costGroups, 0, player));
        assertEquals(7, cost.checkedAmount, 0, "The exact component amount must reach canPay");
        cost.allowed = false;
        assertFalse(ShopPurchasePolicy.canPurchase(null, model.costGroups, 0, player));
        ShopPurchaseModal modal = new ShopPurchaseModal(card, () -> null);
        assertEquals(List.of("addon payment"), markerTexts(modal));
        Marker previous = cost.widgets.get(0);
        card.refreshPrices();
        assertEquals(
                1, previous.disposals, "Replacing a payment view must release add-on resources");
        modal.close();
        card.dispose();
        for (Marker widget : cost.widgets) assertEquals(1, widget.disposals);
    }

    @Test
    void panel_nestedItemTooltip_detachesFromActualHostAndDisposesAddonOnce() {
        Marker resource = new Marker("nested");
        ShopItemWidget item = new ShopItemWidget(Items.DIAMOND.getDefaultInstance(), 16);
        ShopComponentPanel panel =
                new ShopComponentPanel(
                        List.of(
                                () -> {
                                    VBox wrapper = new VBox();
                                    wrapper.addChild(resource);
                                    wrapper.addChild(item);
                                    return wrapper;
                                }));
        OverlayLayer host = new OverlayLayer();
        panel.attachTooltips(host);
        host.applyQueuedMutations();
        assertNotNull(item.getTooltip());
        panel.dispose();
        panel.dispose();
        host.applyQueuedMutations();
        assertNull(item.getTooltip());
        assertTrue(host.children().isEmpty());
        assertEquals(1, resource.disposals);
    }

    @Test
    void panel_reusedMountedWidget_rejectsBrokenFactoryWithoutDisposingOtherView() {
        Marker widget = new Marker("cached");
        ShopComponentPanel first = new ShopComponentPanel(List.of(() -> widget));
        assertThrows(
                IllegalStateException.class, () -> new ShopComponentPanel(List.of(() -> widget)));
        assertEquals(0, widget.disposals);
        first.dispose();
        assertEquals(1, widget.disposals);
    }

    @Test
    void paymentMenu_addonWidgets_renderForEveryGroupAndDisposeOnSelection() {
        ShopOffer offer = ShopOffer.create(UUID.randomUUID(), false);
        List<AddonCost> costs = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            AddonCost cost = new AddonCost();
            cost.setGroupId("addon_" + i);
            offer.addComponent(cost);
            costs.add(cost);
        }
        ShopCatalogElement card = new ShopCatalogElement(new ShopCatalogItem(offer), false, null);
        OverlayLayer host = new OverlayLayer();
        card.attachTooltips(host);
        Button dropdown = (Button) card.getPricesBox().children().get(1);
        dropdown.click();
        List<ShopCatalogElement.CostOptionButton> options =
                find(host, ShopCatalogElement.CostOptionButton.class);
        assertEquals(3, options.size());
        for (ShopCatalogElement.CostOptionButton option : options)
            assertEquals(List.of("addon payment"), markerTexts(option));
        options.get(1).click();
        assertEquals(1, card.getActiveCostGroupIndex());
        card.dispose();
        for (AddonCost cost : costs)
            for (Marker widget : cost.widgets) assertEquals(1, widget.disposals);
    }

    @Test
    void panel_factoryFailure_disposesWidgetsAlreadyCreated() {
        Marker resource = new Marker("resource");
        assertThrows(
                IllegalArgumentException.class,
                () ->
                        new ShopComponentPanel(
                                List.of(
                                        () -> resource,
                                        () -> {
                                            throw new IllegalArgumentException("broken addon");
                                        })));
        assertEquals(1, resource.disposals);
    }

    @Test
    void builtInComponents_itemCommandAndDiscount_ownTheirPresentation() {
        ShopItemWidget item =
                (ShopItemWidget) new ItemRewardComponent(Items.DIAMOND, 5).createRender();
        assertEquals(5, item.item().getCount());
        Label command =
                (Label) new CommandRewardComponent("/time set day", "Advance time").createRender();
        assertEquals("Advance time", command.text());
        ShopBadge discount = (ShopBadge) new DiscountComponent(0.25).createRender();
        assertEquals("-25%", find(discount, Label.class).get(0).text());
        assertNull(new DiscountComponent(0).createRender());
    }

    @Test
    void panel_multipleElements_arrangesThemInOneHorizontalRow() {
        Box first = new Box();
        Box second = new Box();
        first.layout(style -> style.fixed(36, 36).flexNone());
        second.layout(style -> style.fixed(36, 36).flexNone());
        ShopComponentPanel panel = new ShopComponentPanel(List.of(() -> first, () -> second));
        panel.measure(new LayoutContext(200, 200));
        panel.arrange(new MutableRect(0, 0, 200, panel.desiredSize().height()));
        assertTrue(
                second.layoutBounds().x()
                        >= first.layoutBounds().x() + first.layoutBounds().width());
        assertEquals(first.layoutBounds().y(), second.layoutBounds().y(), 0.01f);
        assertEquals(36, panel.desiredSize().height(), 0.01f);
        panel.dispose();
    }

    @Test
    void card_discountAndAddonBadge_mountBetweenContentAndPayment() {
        ShopCatalogItem model =
                new ShopCatalogItem(
                        UUID.randomUUID(),
                        "ALL",
                        "Offer",
                        List.of(
                                () -> ShopRenderWidgets.item(Items.BREAD.getDefaultInstance()),
                                () -> ShopRenderWidgets.item(Items.STONE.getDefaultInstance()),
                                () -> new DiscountComponent(0.25).createRender(),
                                () -> ShopRenderWidgets.badge("Addon")),
                        List.of());
        ShopCatalogElement card = new ShopCatalogElement(model, false, null);
        List<ShopBadge> badges = find(card, ShopBadge.class);
        assertEquals(2, badges.size());
        Widget separatorRow = card.children().get(2);
        assertSame(separatorRow, badges.get(0).parent().parent());
        assertSame(badges.get(0).parent(), badges.get(1).parent());
        assertEquals(
                List.of("-25%", "Addon"),
                badges.stream().map(badge -> find(badge, Label.class).get(0).text()).toList());
        card.measure(new LayoutContext(240, 400));
        card.arrange(new MutableRect(0, 0, 240, card.desiredSize().height()));
        List<ShopItemWidget> items = find(card, ShopItemWidget.class);
        assertEquals(2, items.size());
        assertTrue(items.get(1).layoutBounds().x() > items.get(0).layoutBounds().x());
        assertEquals(items.get(0).layoutBounds().y(), items.get(1).layoutBounds().y(), 0.01f);
        assertTrue(
                badges.get(0).layoutBounds().y()
                        >= items.get(0).layoutBounds().y() + items.get(0).layoutBounds().height());
        assertTrue(
                badges.get(0).layoutBounds().y() + badges.get(0).layoutBounds().height()
                        <= card.children().get(3).layoutBounds().y());
        card.dispose();
    }

    private static List<String> markerTexts(Widget root) {
        return find(root, Marker.class).stream().map(marker -> marker.text).toList();
    }

    @Test
    void card_manyItems_keepsFixedHeightAndScrollsHorizontallyAfterResize() {
        List<ShopRenderElement> elements = new ArrayList<>();
        for (int i = 0; i < 17; i++)
            elements.add(() -> ShopRenderWidgets.item(Items.STONE.getDefaultInstance()));
        elements.add(() -> ShopRenderWidgets.badge("Badge"));
        ShopCatalogElement card =
                new ShopCatalogElement(
                        new ShopCatalogItem(
                                UUID.randomUUID(), "ALL", "Many items", elements, List.of()),
                        false,
                        null);
        find(card, ShopItemWidget.class);
        float originalHeight = -1;
        for (float width : new float[] {240, 120, 200}) {
            card.measure(new LayoutContext(width, 2000));
            card.arrange(new MutableRect(0, 0, width, card.desiredSize().height()));
            if (originalHeight < 0) originalHeight = card.desiredSize().height();
            assertEquals(originalHeight, card.desiredSize().height(), 0.01f);
            ScrollView viewport = find(card, ScrollView.class).get(0);
            assertEquals(46, viewport.layoutBounds().height(), 0.01f);
            assertTrue(viewport.layoutBounds().x() >= 0);
            assertTrue(
                    viewport.layoutBounds().x() + viewport.layoutBounds().width() <= width + 0.01f);
            assertTrue(viewport.maxScrollX() > 0, "All overflow must be accessible by scrolling");
            List<ShopItemWidget> items = find(card, ShopItemWidget.class);
            assertEquals(17, items.size());
            for (ShopItemWidget item : items) {
                assertEquals(items.get(0).layoutBounds().y(), item.layoutBounds().y(), 0.01f);
                assertEquals(36, item.layoutBounds().width(), 0.01f);
            }
            viewport.scrollTo(Float.MAX_VALUE, 0);
            assertEquals(viewport.maxScrollX(), viewport.scrollX(), 0.01f);
            assertEquals(0, viewport.scrollY(), 0.01f);
            ShopBadge badge = find(card, ShopBadge.class).get(0);
            Separator line = find(card, Separator.class).get(0);
            assertTrue(
                    badge.layoutBounds().y()
                            >= viewport.layoutBounds().y() + viewport.layoutBounds().height());
            assertTrue(
                    badge.layoutBounds().y() + badge.layoutBounds().height()
                            < line.layoutBounds().y());
        }
        card.dispose();
    }

    @Test
    void card_singleAndMultipleProducts_areCenteredWhenTheyFit() {
        for (int count : new int[] {1, 2, 3}) {
            List<ShopRenderElement> elements = new ArrayList<>();
            for (int i = 0; i < count; i++)
                elements.add(() -> ShopRenderWidgets.item(Items.BREAD.getDefaultInstance()));
            ShopCatalogElement card =
                    new ShopCatalogElement(
                            new ShopCatalogItem(
                                    UUID.randomUUID(), "ALL", "Centered", elements, List.of()),
                            false,
                            null);
            List<ShopItemWidget> items = find(card, ShopItemWidget.class);
            card.measure(new LayoutContext(240, 400));
            card.arrange(new MutableRect(0, 0, 240, card.desiredSize().height()));
            ScrollView viewport = find(card, ScrollView.class).get(0);
            float firstX = items.get(0).layoutBounds().x();
            var last = items.get(count - 1).layoutBounds();
            assertEquals(
                    viewport.layoutBounds().x() + viewport.layoutBounds().width() / 2,
                    (firstX + last.x() + last.width()) / 2,
                    0.01f);
            assertEquals(0, viewport.maxScrollX(), 0.01f);
            card.dispose();
        }
    }

    @Test
    void card_manyBadges_wrapOnSeparatorWithoutCoveringPayment() {
        List<ShopRenderElement> elements = new ArrayList<>();
        elements.add(() -> ShopRenderWidgets.item(Items.BREAD.getDefaultInstance()));
        for (int i = 0; i < 10; i++) elements.add(() -> ShopRenderWidgets.badge("Badge"));
        ShopCatalogElement card =
                new ShopCatalogElement(
                        new ShopCatalogItem(
                                UUID.randomUUID(), "ALL", "Badges", elements, List.of()),
                        false,
                        null);
        List<ShopBadge> badges = find(card, ShopBadge.class);
        card.measure(new LayoutContext(140, 2000));
        card.arrange(new MutableRect(0, 0, 140, card.desiredSize().height()));
        assertEquals(10, badges.size());
        assertTrue(badges.get(9).layoutBounds().y() > badges.get(0).layoutBounds().y());
        for (ShopBadge badge : badges) {
            assertTrue(badge.layoutBounds().x() + badge.layoutBounds().width() <= 140.01f);
            assertTrue(
                    badge.layoutBounds().y() + badge.layoutBounds().height()
                            <= card.children().get(3).layoutBounds().y());
        }
        card.dispose();
    }

    private static <T extends Widget> List<T> find(Widget root, Class<T> type) {
        if (root instanceof PanelWidget panel) panel.applyQueuedMutations();
        List<T> found = new ArrayList<>();
        if (type.isInstance(root)) found.add(type.cast(root));
        for (Widget child : root.children()) found.addAll(find(child, type));
        return found;
    }

    private static final class Marker extends Box {
        final String text;
        int disposals;

        Marker(String text) {
            this.text = text;
            layout(style -> style.height(70).width(120).flexNone());
        }

        @Override
        public void dispose() {
            disposals++;
            super.dispose();
        }
    }

    private static final class AddonComponent extends ShopComponent {
        final String text;
        final boolean hidden;
        final List<Marker> widgets = new ArrayList<>();

        AddonComponent(String text, boolean hidden) {
            this.text = text;
            this.hidden = hidden;
        }

        @Override
        public IComponentType<?> getType() {
            return ItemRewardComponent.TYPE;
        }

        @Override
        public Widget createRender() {
            if (hidden) return null;
            Marker widget = new Marker(text);
            widgets.add(widget);
            return widget;
        }
    }

    private static final class AddonCost extends CostComponent {
        final List<Marker> widgets = new ArrayList<>();
        boolean allowed;
        double checkedAmount;

        @Override
        public IComponentType<?> getType() {
            return ItemRewardComponent.TYPE;
        }

        @Override
        public Widget createRender() {
            Marker widget = new Marker("addon payment");
            widgets.add(widget);
            return widget;
        }

        @Override
        public Widget getRenderIcon() {
            throw new AssertionError("The addon owns the full render");
        }

        @Override
        public double getBaseAmount() {
            return 7;
        }

        @Override
        public boolean canPay(Player player, double amount) {
            checkedAmount = amount;
            return allowed;
        }

        @Override
        public void pay(Player player, double amount) {}
    }
}
