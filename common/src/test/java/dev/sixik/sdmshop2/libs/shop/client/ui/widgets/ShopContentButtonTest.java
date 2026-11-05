package dev.sixik.sdmshop2.libs.shop.client.ui.widgets;

import dev.sixik.sdmshop2.libs.shop.client.ui.root.ShopCatalogElement;
import dev.sixik.sdmshop2.libs.shop.client.ui.root.ShopRootPanel;
import dev.sixik.sdmshop2.libs.shop.register.ShopRegister;
import dev.sixik.unigui.api.event.PointerEnteredEvent;
import dev.sixik.unigui.api.event.PointerExitedEvent;
import dev.sixik.unigui.api.input.MouseCursor;
import dev.sixik.unigui.api.layout.LayoutContext;
import dev.sixik.unigui.api.math.MutableRect;
import dev.sixik.unigui.api.widget.Widget;
import dev.sixik.unigui.impl.input.TransformHitTester;
import dev.sixik.unigui.widgets.containers.HBox;
import dev.sixik.unigui.widgets.containers.PanelWidget;
import dev.sixik.unigui.widgets.display.ImageView;
import dev.sixik.unigui.widgets.display.Label;
import dev.sixik.unigui.widgets.interaction.Button;
import net.minecraft.world.item.Items;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class ShopContentButtonTest {
    @BeforeAll static void initialize() { ShopRegister.init(); }

    @Test void categoryButton_overTextAndIcon_targetsButtonAndUpdatesHover() {
        Label label = new Label("Category");
        HBox row = new HBox();
        ShopItemWidget icon = new ShopItemWidget(Items.DIAMOND.getDefaultInstance(), 14);
        row.addChild(icon);
        row.addChild(label);
        ShopRootPanel.CategoryButton button = new ShopRootPanel.CategoryButton(row, label);
        assertDecorativeTargets(button, row, icon, label);
        assertSame(label, button.getLabel());
    }

    @Test void paymentOption_overNestedAmountAndIcon_targetsOption() {
        HBox row = new HBox();
        HBox nested = new HBox();
        Label amount = new Label("100");
        nested.addChild(amount);
        row.addChild(nested);
        ShopCatalogElement.CostOptionButton button = new ShopCatalogElement.CostOptionButton(row);
        assertDecorativeTargets(button, row, nested, amount);
    }

    @Test void favorite_overStarImage_targetsClickableButtonWithPointerCursor() {
        ShopCatalogElement card = new ShopCatalogElement(UUID.randomUUID(), "Offer",
                List.of(Items.DIAMOND.getDefaultInstance()), List.of(), 0, false, null);
        card.showFavorite(true);
        flushTree(card);
        Button favorite = findStarButton(card);
        assertNotNull(favorite);
        favorite.measure(new LayoutContext(12, 12));
        favorite.arrange(new MutableRect(0, 0, 12, 12));
        favorite.children().get(0).arrange(new MutableRect(0, 0, 12, 12));
        Widget target = new TransformHitTester().hitTest(favorite, 6, 6).orElseThrow().widget();
        assertSame(favorite, target);
        assertEquals(MouseCursor.POINTER, target.mouseCursorAt(6, 6));
        card.dispose();
    }

    private static void assertDecorativeTargets(ShopContentButton button, Widget... decorations) {
        button.measure(new LayoutContext(160, 24));
        button.arrange(new MutableRect(0, 0, 160, 24));
        // Force each decoration beneath the tested point, so the test cannot pass by missing it.
        for (Widget decoration : decorations) decoration.arrange(new MutableRect(10, 2, 80, 18));
        Widget target = new TransformHitTester().hitTest(button, 20, 10).orElseThrow().widget();
        assertSame(button, target);
        assertEquals(MouseCursor.POINTER, target.mouseCursorAt(20, 10));
        target.handle(new PointerEnteredEvent(target, 20, 10, 20, 10, 0));
        assertTrue(button.hovered());
        target.handle(new PointerExitedEvent(target, 200, 10, 200, 10, 0));
        assertFalse(button.hovered());
        for (Widget decoration : decorations) assertTrue(decoration.enabled());
        button.dispose();
    }

    private static Button findStarButton(Widget widget) {
        if (widget instanceof Button button && button.children().stream().anyMatch(ImageView.class::isInstance)) return button;
        for (Widget child : widget.children()) {
            Button found = findStarButton(child);
            if (found != null) return found;
        }
        return null;
    }

    private static void flushTree(Widget widget) {
        if (widget instanceof PanelWidget panel) panel.applyQueuedMutations();
        for (Widget child : widget.children()) flushTree(child);
    }
}
