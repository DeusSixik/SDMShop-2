package dev.sixik.sdmshop2.libs.shop.client.ui;

import dev.sixik.sdmshop2.libs.shop.client.ui.api.ShopScreenElement;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.ShopUiContext;
import dev.sixik.sdmshop2.libs.shop.register.ShopRegister;
import dev.sixik.unigui.api.widget.Widget;
import dev.sixik.unigui.widgets.containers.PanelWidget;
import dev.sixik.unigui.widgets.containers.Box;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import dev.sixik.sdmshop2.libs.shop.base.ShopInstance;
import dev.sixik.sdmshop2.libs.shop.client.SDMShopClient;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import static org.junit.jupiter.api.Assertions.*;

class ShopScreenApiTest {
    private ShopInstance previousShop;
    @BeforeAll static void initialize() { ShopRegister.init(); }
    @BeforeEach void isolateShop() { previousShop = SDMShopClient.Shop; SDMShopClient.Shop = null; }
    @AfterEach void restoreShop() { SDMShopClient.Shop = previousShop; }

    @Test void builder_reused_createsIndependentWidgetsAndContexts() {
        List<ShopUiContext> contexts = new ArrayList<>();
        ShopScreen.Builder builder = ShopScreen.builder().element(context -> {
            contexts.add(context);
            return root -> { Box box = new Box(); root.addChild(box); return box; };
        });
        try (ShopScreen first = builder.build(); ShopScreen second = builder.build()) {
            assertNotSame(first.rootPanel(), second.rootPanel());
            assertSame(first, contexts.get(0));
            assertSame(second, contexts.get(1));
            assertSame(first.createScreen(), first.createScreen());
            assertNotSame(first.overlay(), second.createScreen().root());
            first.toggleFavorites();
            assertTrue(first.rootPanel().isFavoritesFirst());
            assertFalse(second.rootPanel().isFavoritesFirst());
        }
    }

    @Test void contributions_priorityTies_keepRegistrationOrderAndDisposeOnceInReverse() {
        List<String> operations = new ArrayList<>();
        ShopScreen screen = ShopScreen.builder()
                .element(context -> element("a", 5, operations))
                .element(context -> element("b", -5, operations))
                .element(context -> element("c", 5, operations)).build();
        screen.createScreen();
        assertEquals(List.of("mount b", "mount a", "mount c"), operations);
        screen.close();
        screen.close();
        assertEquals(List.of("mount b", "mount a", "mount c", "dispose c", "dispose a", "dispose b"), operations);
        assertNull(screen.overlay());
        assertThrows(IllegalStateException.class, screen::createScreen);
    }

    @Test void mountFailure_releasesContributionsAndDiscardsOverlay() {
        AtomicInteger disposed = new AtomicInteger();
        ShopScreen screen = ShopScreen.builder().element(context -> new ShopScreenElement() {
            @Override public Widget initWidget(PanelWidget root) { throw new IllegalStateException("failure"); }
            @Override public void dispose() { disposed.incrementAndGet(); }
        }).build();
        assertThrows(IllegalStateException.class, screen::createScreen);
        assertEquals(1, disposed.get());
        assertNull(screen.overlay());
    }

    private static ShopScreenElement element(String name, int priority, List<String> operations) {
        return new ShopScreenElement() {
            @Override public Widget initWidget(PanelWidget root) {
                operations.add("mount " + name);
                Box box = new Box(); root.addChild(box); return box;
            }
            @Override public int priority() { return priority; }
            @Override public void dispose() { operations.add("dispose " + name); }
        };
    }
}
