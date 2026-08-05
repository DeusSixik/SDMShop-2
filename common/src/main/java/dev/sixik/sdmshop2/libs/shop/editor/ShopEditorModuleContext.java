package dev.sixik.sdmshop2.libs.shop.editor;

public record ShopEditorModuleContext(ShopEditSession session) {

    public ShopEditDraftStore drafts() {
        return session.drafts();
    }
}
