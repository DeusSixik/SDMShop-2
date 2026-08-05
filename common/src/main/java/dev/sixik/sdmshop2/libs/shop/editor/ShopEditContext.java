package dev.sixik.sdmshop2.libs.shop.editor;

import java.util.Optional;

public final class ShopEditContext {

    private final ShopEditSession session;

    public ShopEditContext(ShopEditSession session) {
        this.session = session;
    }

    public ShopEditSession session() {
        return session;
    }

    public ShopEditDraftStore drafts() {
        return session.drafts();
    }

    public <T> Optional<T> draft(ShopEditTarget<T> target) {
        return session.draft(target);
    }

    public <T> void setDraft(ShopEditTarget<T> target, T value) {
        session.setDraft(target, value);
    }

    public void markDirty() {
        session.markDirty();
    }
}
