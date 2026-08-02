package dev.sixik.sdmshop2.libs.shop.client.ui.api;

import dev.sixik.sdmshop2.libs.platform.utils.eventbus.DODEventBus;
import dev.sixik.sdmshop2.libs.platform.utils.eventbus.EventPtr;
import dev.sixik.sdmshop2.libs.platform.utils.eventbus.EventSubscription;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class UIEventScope implements AutoCloseable {

    private final List<EventSubscription> subscriptions = new ArrayList<>();
    private final boolean enabled;
    private boolean closed;

    public UIEventScope() {
        this(true);
    }

    private UIEventScope(boolean enabled) {
        this.enabled = enabled;
    }

    public static UIEventScope noop() {
        return new UIEventScope(false);
    }

    public synchronized <T> EventSubscription listen(EventPtr<T> event, DODEventBus.EventListener<T> listener) {
        Objects.requireNonNull(event, "event");
        Objects.requireNonNull(listener, "listener");

        if (!enabled || closed) {
            return EventSubscription.NOOP;
        }

        EventSubscription subscription = event.subscribe(listener);
        subscriptions.add(subscription);
        return subscription;
    }

    public synchronized void clear() {
        for (int i = subscriptions.size() - 1; i >= 0; i--) {
            subscriptions.get(i).close();
        }
        subscriptions.clear();
    }

    @Override
    public synchronized void close() {
        if (closed) {
            return;
        }

        closed = true;
        clear();
    }
}
