package dev.sixik.sdmshop2.libs.shop.client.ui.editor;

import dev.sixik.sdmshop2.libs.shop.base.ShopInstance;
import dev.sixik.sdmshop2.libs.shop.client.cache.ClientCache;
import dev.sixik.sdmshop2.libs.shop.client.cache.ShopClientCache;
import dev.sixik.sdmshop2.libs.shop.client.ui.editor.unity.UnityStyles;
import dev.sixik.sdmshop2.libs.shop.editor.ShopEditSession;
import dev.sixik.sdmshop2.libs.shop.register.ShopRegister;
import dev.sixik.unigui.api.screen.UniScreen;
import dev.sixik.unigui.api.widget.Widget;
import dev.sixik.unigui.widgets.containers.HBox;
import dev.sixik.unigui.widgets.containers.StackPanel;
import dev.sixik.unigui.widgets.containers.VBox;
import dev.sixik.unigui.widgets.docking.DockPane;
import dev.sixik.unigui.widgets.docking.DockingRoot;
import dev.sixik.unigui.widgets.feedback.OverlayLayer;
import dev.sixik.unigui.widgets.interaction.Button;
import net.minecraft.resources.ResourceLocation;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ShopEditorScreenTest {

    private ShopInstance shop;

    @BeforeAll
    static void initAll() {
        ShopRegister.init();
        Path testCachePath = Path.of("build/tmp/test_client_cache_editor.json");
        ShopClientCache.setCache(ClientCache.load(testCachePath));
    }

    @AfterAll
    static void tearDownAll() {
        ShopClientCache.setCache(null);
    }

    @BeforeEach
    void setUp() {
        shop = ShopInstance.createManager(new ResourceLocation("sdm", "test_editor_shop"), true);
        ShopEditSession session = ShopEditSession.forShop(shop, UUID.randomUUID());
        ShopEditorState.get().startSession(session, shop);
    }

    @AfterEach
    void tearDown() {
        ShopEditorScreen.close();
    }

    @Test
    void testCreateScreenInitializesPanesAndToolbar() {
        UniScreen screen = ShopEditorScreen.createScreen();
        assertNotNull(screen);

        DockingRoot dockingRoot = ShopEditorScreen.getDockingRoot();
        assertNotNull(dockingRoot);

        Map<String, DockPane> registry = ShopEditorScreen.getPaneRegistry();
        assertEquals(5, registry.size());
        assertTrue(registry.containsKey("hierarchy"));
        assertTrue(registry.containsKey("catalog"));
        assertTrue(registry.containsKey("category"));
        assertTrue(registry.containsKey("money"));
        assertTrue(registry.containsKey("inspector"));

        assertNotNull(dockingRoot.findPane("hierarchy"));
        assertNotNull(dockingRoot.findPane("catalog"));
        assertNotNull(dockingRoot.findPane("category"));
        assertNotNull(dockingRoot.findPane("money"));
        assertNotNull(dockingRoot.findPane("inspector"));
    }

    @Test
    void testToolbarHasFileWindowMenusAndNormalSendChangesColor() {
        UniScreen screen = ShopEditorScreen.createScreen();
        Widget root = screen.root();
        assertTrue(root instanceof StackPanel);

        OverlayLayer overlay = ShopEditorScreen.getActiveOverlay();
        assertNotNull(overlay);
        Widget content = overlay.content();
        assertTrue(content instanceof VBox);
        VBox editorVBox = (VBox) content;
        editorVBox.applyQueuedMutations();

        Widget firstChild = editorVBox.children().get(0);
        assertTrue(firstChild instanceof HBox, "Top toolbar should be first child");
        HBox toolbar = (HBox) firstChild;
        toolbar.applyQueuedMutations();

        Button fileBtn = null;
        Button windowBtn = null;
        Button sendChangesBtn = null;
        Button resetBtn = null;
        Button closeBtn = null;

        for (Widget w : toolbar.children()) {
            if (w instanceof Button btn) {
                String text = btn.text();
                if ("File".equals(text)) fileBtn = btn;
                else if ("Window".equals(text)) windowBtn = btn;
                else if ("Send Changes".equals(text)) sendChangesBtn = btn;
                else if ("Reset Session".equals(text)) resetBtn = btn;
                else if ("Close".equals(text)) closeBtn = btn;
            }
        }

        assertNotNull(fileBtn, "Toolbar must contain 'File' menu button");
        assertNotNull(windowBtn, "Toolbar must contain 'Window' menu button");
        assertNotNull(resetBtn, "Toolbar must contain 'Reset Session' button");
        assertNotNull(closeBtn, "Toolbar must contain 'Close' button");
        assertNotNull(sendChangesBtn, "Toolbar must contain 'Send Changes' button");

        // Verify Send Changes text color is normal TEXT_PRIMARY and NOT blue SELECTION_BLUE
        assertEquals(UnityStyles.TEXT_PRIMARY.toArgbInt(), sendChangesBtn.textColor().toArgbInt(),
                "Send Changes button must have normal text color (TEXT_PRIMARY)");
        assertNotEquals(UnityStyles.SELECTION_BLUE.toArgbInt(), sendChangesBtn.textColor().toArgbInt(),
                "Send Changes button must NOT have blue text color");
    }

    @Test
    void testRestoreDefaultLayout() {
        ShopEditorScreen.createScreen();
        DockingRoot dockingRoot = ShopEditorScreen.getDockingRoot();
        assertNotNull(dockingRoot);

        // Call restoreDefaultLayout
        ShopEditorScreen.restoreDefaultLayout();

        assertNotNull(dockingRoot.findPane("hierarchy"));
        assertNotNull(dockingRoot.findPane("catalog"));
        assertNotNull(dockingRoot.findPane("category"));
        assertNotNull(dockingRoot.findPane("money"));
        assertNotNull(dockingRoot.findPane("inspector"));
    }

    @Test
    void testRestoreOrFocusPane() {
        ShopEditorScreen.createScreen();
        DockingRoot dockingRoot = ShopEditorScreen.getDockingRoot();
        assertNotNull(dockingRoot);

        // Focusing open pane does not throw and keeps pane present
        ShopEditorScreen.restoreOrFocusPane("inspector");
        assertNotNull(dockingRoot.findPane("inspector"));

        ShopEditorScreen.restoreOrFocusPane("hierarchy");
        assertNotNull(dockingRoot.findPane("hierarchy"));

        ShopEditorScreen.restoreOrFocusPane("catalog");
        assertNotNull(dockingRoot.findPane("catalog"));
    }
}
