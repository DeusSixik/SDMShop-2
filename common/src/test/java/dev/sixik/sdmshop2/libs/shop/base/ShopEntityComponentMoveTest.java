package dev.sixik.sdmshop2.libs.shop.base;

import dev.sixik.sdmshop2.libs.shop.components.api.IComponentType;
import dev.sixik.sdmshop2.libs.shop.components.api.ShopComponent;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class ShopEntityComponentMoveTest {

    @Test
    void movesComponentByOffsetAndIndex() {
        ShopEntity entity = new ShopEntity();
        TestComponent first = new TestComponent("first");
        TestComponent second = new TestComponent("second");
        TestComponent third = new TestComponent("third");
        entity.addComponent(first);
        entity.addComponent(second);
        entity.addComponent(third);

        assertTrue(entity.moveComponent(second, -1));
        assertOrder(entity, second, first, third);
        assertEquals(0, entity.indexOfComponent(second));

        assertTrue(entity.moveComponentToIndex(second, 2));
        assertOrder(entity, first, third, second);

        assertFalse(entity.moveComponent(second, 0));
        assertFalse(entity.moveComponentToIndex(second, 10));
        assertOrder(entity, first, third, second);
    }

    @Test
    void movesComponentAroundAnotherComponent() {
        ShopEntity entity = new ShopEntity();
        TestComponent first = new TestComponent("first");
        TestComponent second = new TestComponent("second");
        TestComponent third = new TestComponent("third");
        entity.addComponent(first);
        entity.addComponent(second);
        entity.addComponent(third);

        assertTrue(entity.moveComponentAfter(first, third));
        assertOrder(entity, second, third, first);

        assertTrue(entity.moveComponentBefore(first, second));
        assertOrder(entity, first, second, third);

        assertTrue(entity.moveComponentToComponent(third, second));
        assertOrder(entity, first, third, second);
    }

    @Test
    void swapsComponentsAndKeepsRoots() {
        ShopEntity entity = new ShopEntity();
        TestComponent first = new TestComponent("first");
        TestComponent second = new TestComponent("second");
        TestComponent third = new TestComponent("third");
        entity.addComponent(first);
        entity.addComponent(second);
        entity.addComponent(third);

        assertTrue(entity.swapComponents(first, third));
        assertOrder(entity, third, second, first);

        assertSame(entity, first.getRoot());
        assertSame(entity, second.getRoot());
        assertSame(entity, third.getRoot());
    }

    @Test
    void movingComponentInvalidatesTypedCacheAndInvokesUpdateCallbacks() {
        ShopEntity entity = new ShopEntity();
        TestComponent first = new TestComponent("first");
        TestComponent second = new TestComponent("second");
        entity.addComponent(first);
        entity.addComponent(second);

        List<TestComponent> cachedBeforeMove = entity.getComponents(TestComponent.class);
        AtomicInteger componentUpdates = new AtomicInteger();
        AtomicInteger entityUpdates = new AtomicInteger();
        entity.subscribeUpdateComponent((updatedEntity, component) -> componentUpdates.incrementAndGet());
        entity.subscribeUpdate(updatedEntity -> entityUpdates.incrementAndGet());

        assertTrue(entity.moveComponent(second, -1));

        List<TestComponent> cachedAfterMove = entity.getComponents(TestComponent.class);
        assertOrder(entity, second, first);
        assertEquals(List.of(first, second), cachedBeforeMove);
        assertEquals(List.of(second, first), cachedAfterMove);
        assertEquals(1, componentUpdates.get());
        assertEquals(1, entityUpdates.get());
    }

    private static void assertOrder(ShopEntity entity, TestComponent... expected) {
        assertEquals(List.of(expected), entity.getComponents());
    }

    private static final class TestComponent extends ShopComponent {
        private final String name;

        private TestComponent(String name) {
            this.name = name;
        }

        @Override
        public IComponentType<?> getType() {
            return null;
        }
    }
}
