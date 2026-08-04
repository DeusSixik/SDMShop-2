package dev.sixik.sdmshop2.libs.shop.base;

import dev.sixik.sdmshop2.libs.shop.base.callbacks.ShopEntityCallbacks;
import dev.sixik.sdmshop2.libs.shop.components.api.IComponentType;
import dev.sixik.sdmshop2.libs.shop.components.api.ShopComponent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShopEntityInitializationTest {

    @Test
    void commonHookRunsBeforeClientAndServerSpecificHooks() {
        TrackingEntity entity = new TrackingEntity();

        entity.initializeServerOnlyComponents();

        assertEquals(1, entity.commonHooks);
        assertEquals(0, entity.clientHooks);
        assertEquals(1, entity.serverHooks);
        assertEquals(1, entity.marker.initCalls);

        entity.initializeClientOnlyComponents();

        assertEquals(2, entity.commonHooks);
        assertEquals(1, entity.clientHooks);
        assertEquals(1, entity.serverHooks);
        assertEquals(2, entity.marker.initCalls);
    }

    @Test
    void commonComponentAddedDuringInitializationIsInitializedOnce() {
        TrackingEntity entity = new TrackingEntity();

        entity.initializeClientOnlyComponents();

        assertEquals(1, entity.marker.initCalls);
        assertSame(entity, entity.marker.getRoot());
    }

    @Test
    void removedComponentCanBeAttachedToAnotherEntity() {
        ShopEntity firstEntity = new ShopEntity();
        ShopEntity secondEntity = new ShopEntity();
        MarkerComponent component = new MarkerComponent();

        firstEntity.addComponent(component);
        assertSame(firstEntity, component.getRoot());

        assertTrue(firstEntity.removeComponent(component));
        assertNull(component.getRoot());

        secondEntity.addComponent(component);
        assertSame(secondEntity, component.getRoot());
    }

    @Test
    void componentRemovedDuringInitializationIsNotInitializedAfterRemoval() {
        ShopEntity entity = new ShopEntity();
        MarkerComponent removed = new MarkerComponent();
        RemovingInitComponent remover = new RemovingInitComponent(removed);

        entity.addComponent(remover);
        entity.addComponent(removed);

        entity.initializeServerOnlyComponents();

        assertEquals(1, remover.initCalls);
        assertEquals(0, removed.initCalls);
        assertNull(removed.getRoot());
        assertFalse(entity.getComponents().contains(removed));
    }

    @Test
    void listenerChangesDoNotAffectCurrentCallbackDispatch() {
        ShopEntity entity = new ShopEntity();
        Counter first = new Counter();
        Counter second = new Counter();

        ShopEntityCallbacks.OnUpdate secondListener = updatedEntity -> second.value++;
        ShopEntityCallbacks.OnUpdate firstListener = updatedEntity -> {
            first.value++;
            updatedEntity.unsubscribeUpdate(secondListener);
        };

        entity.subscribeUpdate(secondListener);
        entity.subscribeUpdate(firstListener);

        entity.invokeUpdate(entity);

        assertEquals(1, first.value);
        assertEquals(1, second.value);
    }

    private static final class TrackingEntity extends ShopEntity {
        private int commonHooks;
        private int clientHooks;
        private int serverHooks;
        private MarkerComponent marker;

        @Override
        protected void customInitializeCommonComponents() {
            commonHooks++;
            if (!hasComponent(MarkerComponent.class)) {
                marker = addComponent(new MarkerComponent());
            }
        }

        @Override
        protected void customInitializeClientOnlyComponents() {
            clientHooks++;
        }

        @Override
        protected void customInitializeServerOnlyComponents() {
            serverHooks++;
        }
    }

    private static final class MarkerComponent extends ShopComponent {
        private int initCalls;

        @Override
        public void init() {
            initCalls++;
        }

        @Override
        public IComponentType<?> getType() {
            return null;
        }
    }

    private static final class RemovingInitComponent extends ShopComponent {
        private final ShopComponent componentToRemove;
        private int initCalls;

        private RemovingInitComponent(ShopComponent componentToRemove) {
            this.componentToRemove = componentToRemove;
        }

        @Override
        public void init() {
            initCalls++;
            getRoot().removeComponent(componentToRemove);
        }

        @Override
        public IComponentType<?> getType() {
            return null;
        }
    }

    private static final class Counter {
        private int value;
    }
}
