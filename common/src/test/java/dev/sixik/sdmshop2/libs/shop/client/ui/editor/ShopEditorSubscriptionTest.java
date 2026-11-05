package dev.sixik.sdmshop2.libs.shop.client.ui.editor;

import dev.sixik.sdmshop2.libs.shop.client.ui.api.ShopUiSubscription;
import org.junit.jupiter.api.Test;
import java.util.concurrent.atomic.AtomicInteger;
import static org.junit.jupiter.api.Assertions.*;

class ShopEditorSubscriptionTest {
    @Test void subscribe_sameCallbackTwice_closingOnePreservesOther() {
        ShopEditorState state = ShopEditorState.create();
        AtomicInteger calls = new AtomicInteger();
        Runnable callback = calls::incrementAndGet;
        ShopUiSubscription first = state.subscribe(callback);
        ShopUiSubscription second = state.subscribe(callback);
        state.notifyListeners();
        assertEquals(2, calls.get());
        first.close(); first.close();
        state.notifyListeners();
        assertEquals(3, calls.get());
        second.close();
        state.notifyListeners();
        assertEquals(3, calls.get());
    }

    @Test void independentState_selectionAndListeners_doNotLeakBetweenSessions() {
        ShopEditorState first = ShopEditorState.create();
        ShopEditorState second = ShopEditorState.create();
        AtomicInteger calls = new AtomicInteger();
        try (ShopUiSubscription ignored = first.subscribe(calls::incrementAndGet)) {
            second.selectTarget("other");
            assertNull(first.getSelectedTarget());
            assertEquals(0, calls.get());
            first.selectTarget("first");
            assertEquals(1, calls.get());
        }
        first.selectTarget(null);
        assertEquals(1, calls.get());
    }
}
