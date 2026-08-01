package dev.sixik.sdmshop2.libs.platform.utils.eventbus;

public class EventPtrInvoker<T> extends EventPtr<T> {

    public EventPtrInvoker(DODEventBus.EventType<T> eventType) {
        super(eventType);
    }

    public void invoke(T event) {
        DODEventBus.DEFAULT_BUS.fire(eventType, event);
    }
}
