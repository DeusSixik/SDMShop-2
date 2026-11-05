package dev.sixik.sdmshop2.libs.shop.client.ui.api;

/** A UI listener registration that can be released without checked exceptions. */
@FunctionalInterface
public interface ShopUiSubscription extends AutoCloseable {
    /** Releases this registration. Calling this method repeatedly is safe. */
    @Override
    void close();
}
