package dev.sixik.sdmshop2.libs.shop.client.ui.elements;

import com.google.gson.JsonParser;
import dev.sixik.sdmshop2.SDMShop2;
import dev.sixik.sdmshop2.libs.shop.base.ObjectIdGetter;
import dev.sixik.sdmshop2.libs.shop.base.ShopEntity;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.UIDisposable;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.UIEventScope;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.WidgetContextRender;
import dev.sixik.sdmshop2.libs.shop.client.ui.toast.ShopToasts;
import dev.sixik.sdmshop2.libs.shop.components.api.ShopComponent;
import dev.sixik.sdmshop2.libs.shop.components.api.ShopComponentRegistry;
import dev.sixik.sdmshop2.libs.shop.components.api.IComponentType;
import dev.sixik.sdmshop2.libs.shop.components.misc.CatalogComponent;
import dev.sixik.sdmshop2.libs.shop.editor.ShopEditSession;
import dev.sixik.unigui.api.layout.EdgeInsets;
import dev.sixik.unigui.api.widget.Widget;
import dev.sixik.unigui.widgets.containers.HBox;
import dev.sixik.unigui.widgets.containers.VBox;
import dev.sixik.unigui.widgets.display.Label;
import dev.sixik.unigui.widgets.feedback.OverlayLayer;
import dev.sixik.unigui.widgets.feedback.WindowWidget;
import dev.sixik.unigui.widgets.interaction.Button;
import dev.sixik.unigui.widgets.interaction.TextArea;
import dev.sixik.unigui.widgets.interaction.TextField;
import lombok.Getter;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;

/**
 * Framework-neutral entity editor. The canonical component JSON remains
 * editable, so every registered component is supported without legacy field
 * widgets or per-component adapters.
 */
public class ShopEntityEditorElement extends WindowWidget implements WidgetContextRender, UIDisposable {
    private final @Nullable ShopEntity shopEntity;
    @Getter private final @Nullable ShopEditSession editSession;
    private final UIEventScope eventScope = new UIEventScope();
    private final @Nullable Runnable onEdit;
    private final boolean allowComponentDelete;
    private boolean callbackInvoked;
    private TextArea jsonEditor;

    public static ShopEntityEditorElement open(Widget owner, @Nullable ShopEntity entity) { return open(owner, null, entity, null, true); }
    public static ShopEntityEditorElement open(Widget owner, @Nullable ShopEntity entity, @Nullable Runnable onEdit) { return open(owner, null, entity, onEdit, true); }
    public static ShopEntityEditorElement open(Widget owner, @Nullable ShopEntity entity, @Nullable Runnable onEdit, boolean allowDelete) { return open(owner, null, entity, onEdit, allowDelete); }
    public static ShopEntityEditorElement open(Widget owner, @Nullable ShopEditSession session, @Nullable ShopEntity entity) { return open(owner, session, entity, null, true); }
    public static ShopEntityEditorElement open(Widget owner, @Nullable ShopEditSession session, @Nullable ShopEntity entity, @Nullable Runnable onEdit) { return open(owner, session, entity, onEdit, true); }
    public static ShopEntityEditorElement open(Widget owner, @Nullable ShopEditSession session, @Nullable ShopEntity entity, @Nullable Runnable onEdit, boolean allowDelete) {
        OverlayLayer overlay = findOverlay(owner);
        if (overlay == null && ShopScreenElement.Instance != null) overlay = ShopScreenElement.Instance.getOverlay();
        if (overlay == null) return null;
        ShopEntityEditorElement editor = new ShopEntityEditorElement(session, entity, onEdit, allowDelete);
        overlay.windows().register(editor);
        editor.openModal();
        return editor;
    }

    private ShopEntityEditorElement(@Nullable ShopEditSession session, @Nullable ShopEntity entity, @Nullable Runnable onEdit, boolean allowDelete) {
        super(Component.translatable("shop.ui.editor.title").getString(), new VBox());
        this.editSession = session;
        this.shopEntity = entity;
        this.onEdit = onEdit;
        this.allowComponentDelete = allowDelete;
        modal(true).fixedModal(true).resizable(true).minWindowSize(420, 300);
        layout(style -> style.fixed(620, 480));
        refreshEditorContent();
    }

    public void refreshEditorContent() {
        VBox body = new VBox();
        body.spacing(7.0f);
        body.layout(style -> style.fill().padding(EdgeInsets.all(8.0f)));
        body.addChild(new Label(Component.translatable("shop.ui.editor.components").getString()));
        if (shopEntity != null) {
            for (ShopComponent component : shopEntity.getComponents()) {
                HBox row = new HBox();
                row.spacing(4.0f);
                Label name = new Label(component.getType().getTranslation().getString());
                name.layout(style -> style.expand());
                row.addChild(name);
                Button up = new Button("↑"); up.enabled(canMoveComponent(component, -1)); up.onClick(event -> moveComponent(component, -1));
                Button down = new Button("↓"); down.enabled(canMoveComponent(component, 1)); down.onClick(event -> moveComponent(component, 1));
                Button duplicate = new Button("⧉"); duplicate.enabled(canEditComponent(component)); duplicate.onClick(event -> duplicateComponent(component));
                Button remove = new Button("×"); remove.enabled(canDeleteComponent(component)); remove.onClick(event -> removeComponent(component));
                row.addChild(up); row.addChild(down); row.addChild(duplicate); row.addChild(remove);
                body.addChild(row);
            }
            Button add = new Button(Component.translatable("shop.ui.editor.add_component").getString());
            add.enabled(editSession != null && !editSession.closed());
            add.onClick(event -> openAddComponentModal());
            body.addChild(add);
        }
        body.addChild(new Label(Component.translatable("shop.ui.editor.json_hint").getString()).wrap(true));
        jsonEditor = new TextArea(shopEntity == null ? "[]" : SDMShop2.GSON.toJson(shopEntity.serialize()));
        jsonEditor.layout(style -> style.expand());
        body.addChild(jsonEditor);

        HBox actions = new HBox();
        actions.spacing(6.0f);
        Button copy = new Button(Component.translatable("shop.ui.offer_element.context_menu.copy").getString());
        copy.onClick(event -> net.minecraft.client.Minecraft.getInstance().keyboardHandler.setClipboard(jsonEditor.text()));
        Button apply = new Button(Component.translatable("shop.ui.common.save").getString());
        apply.onClick(event -> applyJson());
        actions.addChild(copy); actions.addChild(apply);
        body.addChild(actions);
        content(body);
    }

    private void openAddComponentModal() {
        if (shopEntity == null || editSession == null || editSession.closed()) return;
        VBox rows = new VBox();
        rows.spacing(4.0f);
        WindowWidget dialog = new WindowWidget(Component.translatable("shop.ui.editor.add_component").getString(), rows)
                .modal(true).fixedModal(true).resizable(true).minWindowSize(320, 240);
        dialog.layout(style -> style.fixed(420, 360));
        ShopComponentRegistry.getTypes().values().stream()
                .filter(IComponentType::showInEditor)
                .sorted(Comparator.comparing(type -> type.getId().toString()))
                .forEach(type -> {
                    Button button = new Button(type.getTranslation().getString());
                    button.onClick(event -> {
                        ShopComponent component = type.createDefault();
                        shopEntity.addComponent(component);
                        record("component.add", component, "Added component " + type.getId());
                        dialog.close();
                        refreshEditorContent();
                    });
                    rows.addChild(button);
                });
        OverlayLayer overlay = findOverlay(this);
        if (overlay != null) { overlay.windows().register(dialog); dialog.openModal(); }
    }

    private void applyJson() {
        if (shopEntity == null || editSession == null || editSession.closed()) return;
        try {
            shopEntity.deserialize(JsonParser.parseString(jsonEditor.text()));
            editSession.recordHistory("entity.json_edit", shopEntity.getClass().getSimpleName(),
                    shopEntity instanceof ObjectIdGetter getter ? getter.getUUID() : null, null, "Edited entity component JSON");
            ShopToasts.success(Component.translatable("shop.ui.toast.component_updated"));
            if (onEdit != null) onEdit.run();
            callbackInvoked = true;
            refreshEditorContent();
        } catch (Exception exception) {
            ShopToasts.error(Component.literal(exception.getMessage() == null ? "Invalid component JSON" : exception.getMessage()));
        }
    }

    public int getComponentCount() { return shopEntity == null ? 0 : shopEntity.getComponents().size(); }
    public boolean canMoveComponent(@Nullable ShopComponent component, int offset) {
        if (!canEditComponent(component) || offset == 0) return false;
        int index = shopEntity.indexOfComponent(component), target = index + offset;
        return index >= 0 && target >= 0 && target < getComponentCount();
    }
    public void moveComponent(@Nullable ShopComponent component, int offset) { if (canMoveComponent(component, offset)) moveComponentTo(component, shopEntity.indexOfComponent(component) + offset); }
    public boolean duplicateComponent(@Nullable ShopComponent component) {
        if (!canEditComponent(component)) return false;
        ShopComponent copy = ShopComponentRegistry.fromJson(ShopComponentRegistry.toJson(component).deepCopy());
        shopEntity.addComponent(copy);
        shopEntity.moveComponentToIndex(copy, shopEntity.indexOfComponent(component) + 1);
        record("component.duplicate", component, "Duplicated component " + component.getType().getId());
        refreshEditorContent();
        return true;
    }
    public boolean canDeleteComponent(@Nullable ShopComponent component) { return canEditComponent(component) && allowComponentDelete && !(component instanceof CatalogComponent); }
    public boolean removeComponent(@Nullable ShopComponent component) {
        if (!canDeleteComponent(component)) { ShopToasts.warning(Component.translatable("shop.ui.toast.component_delete_protected")); return false; }
        record("component.delete", component, "Deleted component " + component.getType().getId());
        boolean removed = shopEntity.removeComponent(component);
        if (removed) refreshEditorContent();
        return removed;
    }
    public boolean moveComponentTo(@Nullable ShopComponent component, int targetIndex) {
        if (!canEditComponent(component)) return false;
        int current = shopEntity.indexOfComponent(component);
        int target = Math.max(0, Math.min(targetIndex, getComponentCount() - 1));
        if (!shopEntity.moveComponentToIndex(component, target)) return false;
        record("component.move", component, "Moved component from " + (current + 1) + " to " + (target + 1));
        refreshEditorContent();
        return true;
    }
    public void openMoveComponentModal(@Nullable ShopComponent component) {
        if (!canEditComponent(component)) return;
        VBox body = new VBox(); body.spacing(5.0f);
        TextField target = new TextField(String.valueOf(shopEntity.indexOfComponent(component) + 1));
        Button move = new Button(Component.translatable("shop.ui.common.move").getString());
        WindowWidget dialog = new WindowWidget(Component.translatable("shop.ui.component.move.title").getString(), body).modal(true).fixedModal(true).resizable(false);
        dialog.layout(style -> style.fixed(260, 120));
        move.onClick(event -> { try { if (moveComponentTo(component, Integer.parseInt(target.text()) - 1)) dialog.close(); } catch (NumberFormatException ignored) { } });
        body.addChild(target); body.addChild(move);
        OverlayLayer overlay = findOverlay(this); if (overlay != null) { overlay.windows().register(dialog); dialog.openModal(); }
    }

    private void record(String action, ShopComponent component, String summary) {
        if (editSession != null && !editSession.closed()) editSession.recordHistory(action, shopEntity.getClass().getSimpleName(),
                shopEntity instanceof ObjectIdGetter getter ? getter.getUUID() : null, component.getType().getId(), summary);
    }
    protected boolean canEditComponent(@Nullable ShopComponent component) {
        return component != null && shopEntity != null && shopEntity.indexOfComponent(component) >= 0 && editSession != null && !editSession.closed();
    }

    @Override public ShopEntityEditorElement close() {
        if (!callbackInvoked && onEdit != null) { callbackInvoked = true; onEdit.run(); }
        return (ShopEntityEditorElement) super.close();
    }
    @Override public @Nullable ShopEntity getShopEntity() { return shopEntity; }
    @Override public Widget getOwner() { return this; }
    @Override public UIEventScope eventScope() { return eventScope; }
    @Override public void dispose() { eventScope.close(); super.dispose(); }

    private static @Nullable OverlayLayer findOverlay(Widget owner) {
        Widget current = owner;
        while (current != null) { if (current instanceof OverlayLayer layer) return layer; current = current.parent(); }
        return null;
    }
}
