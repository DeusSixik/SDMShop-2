package dev.sixik.sdmshop2.libs.platform.utils.eventbus;

@FunctionalInterface
public interface EventSubscription extends AutoCloseable {

    EventSubscription NOOP = () -> {
    };

    void unsubscribe();

    @Override
    default void close() {
        unsubscribe();
    }
}
