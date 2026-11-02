package dev.sixik.sdmshop2.libs.shop.client.ui.elements;

import dev.sixik.sdmshop2.libs.platform.utils.eventbus.DODEventBus;
import dev.sixik.sdmshop2.libs.platform.utils.eventbus.EventPtr;
import dev.sixik.sdmshop2.libs.platform.utils.eventbus.EventSubscription;
import dev.sixik.sdmshop2.libs.sdmeconomy.CurrencyDraft;
import dev.sixik.sdmshop2.libs.sdmeconomy.ICurrency;
import dev.sixik.sdmshop2.libs.shop.base.ShopEntity;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.UIDisposable;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.UIEventScope;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.WidgetContextRender;
import dev.sixik.sdmshop2.libs.shop.client.ui.events.ShopUIEvents;
import dev.sixik.sdmshop2.libs.shop.client.ui.toast.ShopToasts;
import dev.sixik.sdmshop2.libs.shop.client.ui.widgets.base.ShopWidgetGroup;
import dev.sixik.sdmshop2.libs.shop.components.misc.CatalogComponent;
import dev.sixik.sdmshop2.libs.shop.editor.ShopEditSession;
import dev.sixik.unigui.api.layout.EdgeInsets;
import dev.sixik.unigui.api.math.MutableColor;
import dev.sixik.unigui.api.widget.Widget;
import dev.sixik.unigui.widgets.containers.HBox;
import dev.sixik.unigui.widgets.containers.ScrollView;
import dev.sixik.unigui.widgets.containers.VBox;
import dev.sixik.unigui.widgets.display.Label;
import dev.sixik.unigui.widgets.feedback.WindowWidget;
import dev.sixik.unigui.widgets.interaction.Button;
import dev.sixik.unigui.widgets.interaction.TextField;
import dev.sixik.unigui.widgets.interaction.ToggleButton;
import lombok.Getter;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

/** Category and currency navigation implemented with UniGUI. */
public class ShopTabsPanelElement extends ShopWidgetGroup implements WidgetContextRender, UIDisposable {
    @Getter private final @NotNull ShopScreenElement screen;
    @Getter private @Nullable CatalogComponent selectedCategory;
    private final UIEventScope eventScope = new UIEventScope();
    private final VBox categoryRows = new VBox();
    private final VBox currencyRows = new VBox();
    private final Set<ResourceLocation> currencyFilters = new LinkedHashSet<>();

    public ShopTabsPanelElement(@NotNull ShopScreenElement screen) {
        this.screen = screen;
        background(MutableColor.fromArgb(0xE51B1D24));
        border(MutableColor.fromArgb(0xFF444A58));
        radius(5.0f);
        buildUi();
        listenScreen(ShopUIEvents.REFRESH_UI, event -> {
            if (event.affectsCategories() || event.affectsCurrencies()) refresh(false);
        });
        listenScreen(ShopUIEvents.SELECT_CATEGORY, event -> {
            selectedCategory = event.selected();
            refresh(false);
        });
    }

    private void buildUi() {
        VBox root = new VBox();
        root.spacing(5.0f);
        root.layout(style -> style.fill().padding(EdgeInsets.all(5.0f)));
        root.addChild(new Label(Component.translatable("shop.ui.tabs.categories").getString()));

        categoryRows.spacing(3.0f);
        ScrollView categoryScroll = new ScrollView(categoryRows).overlayScrollBars(true);
        categoryScroll.layout(style -> style.expand());
        root.addChild(categoryScroll);

        root.addChild(new Label(Component.translatable("shop.ui.tabs.currencies").getString()));
        currencyRows.spacing(3.0f);
        ScrollView currencyScroll = new ScrollView(currencyRows).overlayScrollBars(true);
        currencyScroll.layout(style -> style.height(150.0f).flexNone());
        root.addChild(currencyScroll);
        addChild(root);
        refresh(false);
    }

    public boolean setSelectedCategory(@Nullable CatalogComponent category) {
        if (sameCategory(selectedCategory, category)) return false;
        selectedCategory = category;
        ShopUIEvents.invokeSelectCategory(category);
        refresh(false);
        return true;
    }

    public boolean isSelectedCategory(@Nullable CatalogComponent category) { return sameCategory(selectedCategory, category); }
    public boolean isEditorMode() { return screen.isEditorMode(); }
    public int getCategoryCount() { return screen.getOrderedCategories().size(); }

    public void createDraftCategory() {
        CatalogComponent created = screen.createDraftCategory();
        setSelectedCategory(created);
        openRenameCategoryModal(created);
    }

    public boolean canMoveCategory(CatalogComponent category, int offset) {
        List<CatalogComponent> categories = screen.getOrderedCategories();
        int index = indexOfCategory(categories, category);
        return index >= 0 && index + offset >= 0 && index + offset < categories.size();
    }

    public void moveCategory(CatalogComponent category, int offset) {
        if (screen.moveDraftCategory(category, offset)) refresh(false);
    }

    public void openRenameCategoryModal(CatalogComponent category) {
        openTextDialog(Component.translatable("shop.ui.category.rename.title").getString(), category.getId(), value -> {
            if (!screen.renameDraftCategory(category, value)) {
                ShopToasts.error(Component.translatable("shop.ui.toast.category_rename_failed"));
            }
            refresh(false);
        });
    }

    public void openCategoryItemIconModal(CatalogComponent category) {
        String current = category.hasItemIcon()
                ? BuiltInRegistries.ITEM.getKey(category.getIconItem().getItem()).toString() : "minecraft:diamond";
        openTextDialog(Component.translatable("shop.ui.category.icon.item.title").getString(), current, value -> {
            ResourceLocation id = ResourceLocation.tryParse(value.trim());
            Item item = id == null ? Items.AIR : BuiltInRegistries.ITEM.get(id);
            if (item != Items.AIR) screen.updateDraftCategoryIcon(category, CatalogComponent.IconType.ITEM, item.getDefaultInstance(), null);
            else ShopToasts.error(Component.literal("Unknown item: " + value));
            refresh(false);
        });
    }

    public void openCategoryTextureIconModal(CatalogComponent category) {
        String current = category.hasTextureIcon() ? category.getIconTexture().toString() : "";
        openTextDialog(Component.translatable("shop.ui.category.icon.texture.title").getString(), current, value -> {
            ResourceLocation id = ResourceLocation.tryParse(value);
            if (id != null) screen.updateDraftCategoryIcon(category, CatalogComponent.IconType.TEXTURE, null, id);
            refresh(false);
        });
    }

    public void openMoveCategoryModal(CatalogComponent category) {
        List<CatalogComponent> categories = screen.getMoveTargetCategories(category);
        VBox rows = new VBox();
        rows.spacing(4.0f);
        WindowWidget window = modal(Component.translatable("shop.ui.category.move.title").getString(), rows, 280, 220);
        for (CatalogComponent target : categories) {
            Button button = new Button(target.getId());
            button.onClick(event -> {
                screen.moveDraftCategoryTo(category, indexOfCategory(screen.getOrderedCategories(), target));
                window.close();
                refresh(false);
            });
            rows.addChild(button);
        }
    }

    public void openDeleteCategoryModal(CatalogComponent category) {
        VBox body = new VBox();
        body.spacing(6.0f);
        body.addChild(new Label(Component.translatable("shop.ui.category.delete.warning", screen.countOffersInCategory(category)).getString()).wrap(true));
        WindowWidget window = modal(Component.translatable("shop.ui.category.delete.title").getString(), body, 300, 150);
        Button delete = new Button(Component.translatable("shop.ui.common.delete").getString());
        delete.onClick(event -> {
            if (screen.deleteDraftCategory(category, null)) setSelectedCategory(null);
            window.close();
            refresh(false);
        });
        body.addChild(delete);
    }

    public void openCreateCurrencyModal() {
        VBox body = new VBox(); body.spacing(6.0f);
        WindowWidget window = modal(Component.translatable("shop.ui.currency.add").getString(), body, 280, 140);
        Button text = new Button("Text currency");
        text.onClick(event -> { window.close(); openTextCurrencyModal(null, null); });
        Button item = new Button("Item currency");
        item.onClick(event -> { window.close(); openItemCurrencyModal(null, null); });
        body.addChild(text); body.addChild(item);
    }
    public void openEditCurrencyModal(ResourceLocation id, ICurrency currency) {
        CurrencyDraft draft = CurrencyDraft.fromCurrency(currency);
        if (draft != null && draft.kind() == CurrencyDraft.Kind.ITEM) openItemCurrencyModal(id, draft);
        else openTextCurrencyModal(id, draft);
    }
    public boolean deleteCurrency(ResourceLocation id) { boolean changed = screen.deleteDraftCurrency(id); if (changed) refresh(false); return changed; }

    private void openTextCurrencyModal(@Nullable ResourceLocation existingId, @Nullable CurrencyDraft draft) {
        VBox body = new VBox();
        body.spacing(5.0f);
        TextField id = new TextField(existingId == null ? "sdm:currency" : existingId.toString());
        TextField name = new TextField(draft == null ? "Currency" : draft.displayName());
        TextField icon = new TextField(draft == null ? "$" : draft.iconText());
        body.addChild(new Label("Id")); body.addChild(id);
        body.addChild(new Label(Component.translatable("shop.ui.currency.name").getString())); body.addChild(name);
        body.addChild(new Label(Component.translatable("shop.ui.currency.icon").getString())); body.addChild(icon);
        WindowWidget window = modal(Component.translatable("shop.ui.currency.editor.title").getString(), body, 320, 220);
        Button save = new Button(Component.translatable("shop.ui.common.save").getString());
        save.onClick(event -> {
            ResourceLocation parsed = ResourceLocation.tryParse(id.text().trim());
            if (parsed != null && screen.upsertTextCurrency(parsed, name.text(), icon.text())) {
                window.close();
                refresh(false);
            } else ShopToasts.error(Component.translatable("shop.ui.toast.currency_save_failed"));
        });
        body.addChild(save);
    }

    private void openItemCurrencyModal(@Nullable ResourceLocation existingId, @Nullable CurrencyDraft draft) {
        VBox body = new VBox(); body.spacing(5.0f);
        TextField id = new TextField(existingId == null ? "sdm:item_currency" : existingId.toString());
        String initialItem = draft != null && !draft.itemStack().isEmpty()
                ? BuiltInRegistries.ITEM.getKey(draft.itemStack().getItem()).toString() : "minecraft:diamond";
        TextField itemId = new TextField(initialItem);
        body.addChild(new Label("Id")); body.addChild(id);
        body.addChild(new Label("Item")); body.addChild(itemId);
        WindowWidget window = modal(Component.translatable("shop.ui.currency.editor.title").getString(), body, 320, 180);
        Button save = new Button(Component.translatable("shop.ui.common.save").getString());
        save.onClick(event -> {
            ResourceLocation currencyId = ResourceLocation.tryParse(id.text().trim());
            ResourceLocation parsedItem = ResourceLocation.tryParse(itemId.text().trim());
            Item item = parsedItem == null ? Items.AIR : BuiltInRegistries.ITEM.get(parsedItem);
            if (currencyId != null && item != Items.AIR && screen.upsertItemCurrency(currencyId, item.getDefaultInstance())) {
                window.close(); refresh(false);
            } else ShopToasts.error(Component.translatable("shop.ui.toast.currency_save_failed"));
        });
        body.addChild(save);
    }

    public ShopTabsPanelElement refresh() { return refresh(true); }
    public ShopTabsPanelElement refresh(boolean relayout) {
        categoryRows.clearChildren();
        ToggleButton all = categoryButton(Component.translatable("shop.ui.tabs.all").getString(), null);
        categoryRows.addChild(all);
        for (CatalogComponent category : screen.getOrderedCategories()) categoryRows.addChild(categoryRow(category));
        if (isEditorMode()) {
            Button add = new Button(Component.translatable("shop.ui.category.add").getString());
            add.onClick(event -> createDraftCategory());
            categoryRows.addChild(add);
        }

        currencyRows.clearChildren();
        for (ICurrency currency : screen.getEditorCurrencies().values()) {
            HBox container = new HBox(); container.spacing(3.0f);
            ToggleButton row = new ToggleButton(currency.getDisplayName().getString()).checked(currencyFilters.contains(currency.getId()));
            row.layout(style -> style.expand());
            row.onCheckedChanged(event -> {
                if (event.newValue()) currencyFilters.add(currency.getId()); else currencyFilters.remove(currency.getId());
                ShopUIEvents.invokeCurrencyFilterUpdate(currencyFilters);
            });
            container.addChild(row);
            if (isEditorMode()) {
                Button edit = new Button("✎"); edit.onClick(event -> openEditCurrencyModal(currency.getId(), currency));
                Button delete = new Button("×"); delete.enabled(screen.canDeleteCurrency(currency.getId())); delete.onClick(event -> deleteCurrency(currency.getId()));
                container.addChild(edit); container.addChild(delete);
            }
            currencyRows.addChild(container);
        }
        if (isEditorMode()) {
            Button addCurrency = new Button(Component.translatable("shop.ui.currency.add").getString());
            addCurrency.onClick(event -> openCreateCurrencyModal());
            currencyRows.addChild(addCurrency);
        }
        return this;
    }

    private ToggleButton categoryButton(String text, @Nullable CatalogComponent category) {
        ToggleButton button = new ToggleButton(text).checked(isSelectedCategory(category));
        button.onClick(event -> setSelectedCategory(category));
        return button;
    }

    private Widget categoryRow(CatalogComponent category) {
        HBox row = new HBox(); row.spacing(3.0f);
        ToggleButton select = categoryButton(category.getId(), category);
        select.layout(style -> style.expand());
        row.addChild(select);
        if (isEditorMode()) {
            Button up = new Button("↑"); up.enabled(canMoveCategory(category, -1)); up.onClick(event -> moveCategory(category, -1));
            Button down = new Button("↓"); down.enabled(canMoveCategory(category, 1)); down.onClick(event -> moveCategory(category, 1));
            Button rename = new Button("✎"); rename.onClick(event -> openRenameCategoryModal(category));
            Button item = new Button("I"); item.onClick(event -> openCategoryItemIconModal(category));
            Button texture = new Button("T"); texture.onClick(event -> openCategoryTextureIconModal(category));
            Button delete = new Button("×"); delete.onClick(event -> openDeleteCategoryModal(category));
            row.addChild(up); row.addChild(down); row.addChild(rename); row.addChild(item); row.addChild(texture); row.addChild(delete);
        }
        return row;
    }

    private void openTextDialog(String title, String initial, Consumer<String> apply) {
        VBox body = new VBox();
        body.spacing(6.0f);
        TextField value = new TextField(initial == null ? "" : initial);
        body.addChild(value);
        WindowWidget window = modal(title, body, 300, 130);
        Button save = new Button(Component.translatable("shop.ui.common.save").getString());
        save.onClick(event -> { apply.accept(value.text()); window.close(); });
        body.addChild(save);
    }

    private WindowWidget modal(String title, Widget content, float width, float height) {
        WindowWidget window = new WindowWidget(title, content).modal(true).fixedModal(true).resizable(false);
        window.layout(style -> style.fixed(width, height));
        screen.getOverlay().windows().register(window);
        return window.openModal();
    }

    private int indexOfCategory(List<CatalogComponent> categories, CatalogComponent category) {
        for (int i = 0; i < categories.size(); i++) if (sameCategory(categories.get(i), category)) return i;
        return -1;
    }

    protected static boolean sameCategory(@Nullable CatalogComponent first, @Nullable CatalogComponent second) {
        if (first == second) return true;
        if (first == null || second == null) return false;
        if (first.getUuid() != null && second.getUuid() != null) return first.getUuid().equals(second.getUuid());
        return java.util.Objects.equals(first.getId(), second.getId());
    }

    @Override public @Nullable ShopEntity getShopEntity() { return null; }
    @Override public Widget getOwner() { return this; }
    @Override public UIEventScope eventScope() { return eventScope; }
    @Override public UIEventScope screenEventScope() { return screen.screenEventScope(); }
    @Override public @Nullable ShopEditSession getEditSession() { return screen.getEditSession(); }
    public <Event> EventSubscription listenScreen(EventPtr<Event> event, DODEventBus.EventListener<Event> listener) { return screen.listenScreen(event, listener); }
    @Override public void dispose() { eventScope.close(); super.dispose(); }
}
