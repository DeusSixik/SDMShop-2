package dev.sixik.sdmshop2.libs.shop.client.ui.elements;

import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.utils.Size;
import dev.sixik.sdmshop2.libs.sdmeconomy.CurrencyDraft;
import dev.sixik.sdmshop2.libs.sdmeconomy.ICurrency;
import dev.sixik.sdmshop2.libs.shop.base.ShopEntity;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.ShopUiElement;
import dev.sixik.sdmshop2.libs.shop.client.ui.widgets.base.ShopWidgetGroup;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.ShopUIUtils;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.StyleApi;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.UIDisposable;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.UIEventScope;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.WidgetContextRender;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.WidgetRender;
import dev.sixik.sdmshop2.libs.shop.client.ui.events.ShopUIEvents;
import dev.sixik.sdmshop2.libs.shop.client.ui.style.DefaultShopTabsPanelRender;
import dev.sixik.sdmshop2.libs.shop.client.ui.toast.ShopToasts;
import dev.sixik.sdmshop2.libs.shop.components.misc.CatalogComponent;
import dev.sixik.sdmshop2.libs.shop.editor.ShopEditSession;
import dev.sixik.sdmshop2.libs.platform.utils.eventbus.DODEventBus;
import dev.sixik.sdmshop2.libs.platform.utils.eventbus.EventPtr;
import dev.sixik.sdmshop2.libs.platform.utils.eventbus.EventSubscription;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.ButtonWidget;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.InputTextBox;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.TextLabel;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.containers.ModalWidget;
import dev.sixik.sdmshop2.libs.shop.client.ui.widgets.SDMItemStackSelectorWidget;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class ShopTabsPanelElement extends ShopWidgetGroup implements
        ShopUiElement, WidgetContextRender, UIDisposable {

    @Getter
    @Setter
    public int widgetSpace = 4;

    @Getter
    protected final @NotNull ShopScreenElement shopScreen;

    protected final WidgetRender render;
    protected final UIEventScope eventScope = new UIEventScope();
    protected boolean defaultHandlersRegistered;
    @Getter
    protected @Nullable CatalogComponent selectedCategory;

    public ShopTabsPanelElement(@NotNull ShopScreenElement screen) {
        this(screen, StyleApi.getDefaultStyle(StyleApi.Category.TabsPanel).get());
    }

    public ShopTabsPanelElement(@NotNull ShopScreenElement screen, WidgetRender render) {
        this.shopScreen = screen;
        this.render = Objects.requireNonNull(render, "render");

        this.render.constructor(this);
        registerDefaultHandlers();
        refresh(false);
    }

    protected void registerDefaultHandlers() {
        if (defaultHandlersRegistered) {
            return;
        }

        defaultHandlersRegistered = true;
        listenScreen(ShopUIEvents.REFRESH_UI, event -> {
            if (event.affectsCategories()) {
                refresh(initialized && event.relayout());
                return;
            }

            if (event.affectsCurrencies()) {
                if (render instanceof DefaultShopTabsPanelRender tabsRender && tabsRender.refreshCurrencies()) {
                    return;
                }

                refresh(initialized && event.relayout());
            }
        });
        listenScreen(ShopUIEvents.SELECT_CATEGORY, event -> {
            if (!setSelectedCategory(event.selected())) {
                return;
            }

            if (render instanceof DefaultShopTabsPanelRender tabsRender) {
                tabsRender.updateSelectedTabs(this);
            } else {
                refresh(initialized);
            }
        });
    }

    public boolean setSelectedCategory(@Nullable CatalogComponent selectedCategory) {
        if (sameCategory(this.selectedCategory, selectedCategory)) {
            return false;
        }

        this.selectedCategory = selectedCategory;
        return true;
    }

    public boolean isSelectedCategory(@Nullable CatalogComponent category) {
        return sameCategory(selectedCategory, category);
    }

    public boolean isEditorMode() {
        return shopScreen.isEditorMode();
    }

    public void createDraftCategory() {
        shopScreen.createDraftCategory();
    }

    public int getCategoryCount() {
        return shopScreen.getOrderedCategories().size();
    }

    public boolean canMoveCategory(CatalogComponent category, int offset) {
        List<CatalogComponent> categories = shopScreen.getOrderedCategories();
        int index = indexOfCategory(categories, category);
        int target = index + offset;
        return index >= 0 && target >= 0 && target < categories.size();
    }

    public void moveCategory(CatalogComponent category, int offset) {
        if (shopScreen.moveDraftCategory(category, offset)) {
            ShopToasts.success(Component.literal("Category moved"));
        }
    }

    public void openRenameCategoryModal(CatalogComponent category) {
        if (!isEditorMode() || category == null) {
            return;
        }

        ModalWidget modal = new ModalWidget(280, 116)
                .setTitle(Component.literal("Rename Category"))
                .setCloseOnEsc(true)
                .setCloseOnOutsideClick(false);
        ModalWidget opened = ModalWidget.openNested(this, modal);
        if (opened == null) {
            return;
        }

        TextLabel label = new TextLabel(0, 4, opened.getContentWidth(), 20,
                Component.literal("Name or lang key:"));
        label.setAutoSize(false)
                .setAlignment(TextLabel.HorizontalAlignment.LEFT, TextLabel.VerticalAlignment.CENTER)
                .setColor(0xFFAEB4C6);
        opened.addWidget(label);

        InputTextBox input = new InputTextBox(0, 28, opened.getContentWidth(), 20);
        input.setCurrentStringSilently(category.getId());
        input.setPlaceholder(Component.literal("Test or my.key.text"));
        input.setMaxLength(128);
        input.setClientSideWidget();
        opened.addWidget(input);

        int buttonY = Math.max(56, opened.getContentHeight() - 24);
        int buttonWidth = Math.max(1, opened.getContentWidth() / 2 - 4);
        ButtonWidget cancel = createModalButton(0, buttonY, buttonWidth, 20, Component.literal("Cancel"), ignored -> opened.close());
        ButtonWidget rename = createModalButton(buttonWidth + 8, buttonY, buttonWidth, 20, Component.literal("Rename"), ignored -> {
            String value = input.getCurrentString() == null ? "" : input.getCurrentString().trim();
            if (value.isEmpty()) {
                ShopToasts.warning(Component.literal("Category name is empty"));
                return;
            }

            if (shopScreen.renameDraftCategory(category, value)) {
                opened.close();
                ShopToasts.success(Component.literal("Category renamed"));
            }
        });

        opened.addWidget(cancel);
        opened.addWidget(rename);
        input.setFocus(true);
    }

    public void openMoveCategoryModal(CatalogComponent category) {
        if (!isEditorMode() || category == null) {
            return;
        }

        List<CatalogComponent> categories = shopScreen.getOrderedCategories();
        int currentIndex = indexOfCategory(categories, category);
        if (currentIndex < 0 || categories.size() <= 1) {
            return;
        }

        ModalWidget modal = new ModalWidget(260, 116)
                .setTitle(Component.literal("Move Category"))
                .setCloseOnEsc(true)
                .setCloseOnOutsideClick(false);
        ModalWidget opened = ModalWidget.openNested(this, modal);
        if (opened == null) {
            return;
        }

        TextLabel label = new TextLabel(0, 4, opened.getContentWidth(), 20,
                Component.literal("Target position: 1 - " + categories.size()));
        label.setAutoSize(false)
                .setAlignment(TextLabel.HorizontalAlignment.LEFT, TextLabel.VerticalAlignment.CENTER)
                .setColor(0xFFAEB4C6);
        opened.addWidget(label);

        InputTextBox input = new InputTextBox(0, 28, opened.getContentWidth(), 20);
        input.setNumbersOnly(1, categories.size());
        input.setCurrentStringSilently(currentIndex + 1);
        input.setPlaceholder(Component.literal("Position"));
        input.setClientSideWidget();
        opened.addWidget(input);

        int buttonY = Math.max(56, opened.getContentHeight() - 24);
        int buttonWidth = Math.max(1, opened.getContentWidth() / 2 - 4);
        ButtonWidget cancel = createModalButton(0, buttonY, buttonWidth, 20, Component.literal("Cancel"), ignored -> opened.close());
        ButtonWidget move = createModalButton(buttonWidth + 8, buttonY, buttonWidth, 20, Component.literal("Move"), ignored -> {
            int position;
            try {
                position = Integer.parseInt(input.getCurrentString().trim());
            } catch (Exception e) {
                ShopToasts.warning(Component.literal("Invalid position"));
                return;
            }

            if (shopScreen.moveDraftCategoryTo(category, position - 1)) {
                opened.close();
                ShopToasts.success(Component.literal("Category moved"));
            }
        });

        opened.addWidget(cancel);
        opened.addWidget(move);
        input.setFocus(true);
    }

    public void openDeleteCategoryModal(CatalogComponent category) {
        if (!isEditorMode() || category == null) {
            return;
        }

        int offerCount = shopScreen.countOffersInCategory(category);
        List<CatalogComponent> moveTargets = shopScreen.getMoveTargetCategories(category);
        int height = offerCount > 0
                ? Math.min(260, 116 + moveTargets.size() * 22)
                : 112;

        ModalWidget modal = new ModalWidget(310, height)
                .setTitle(Component.literal("Delete Category"))
                .setCloseOnEsc(true)
                .setCloseOnOutsideClick(false);
        ModalWidget opened = ModalWidget.openNested(this, modal);
        if (opened == null) {
            return;
        }

        TextLabel warning = new TextLabel(0, 4, opened.getContentWidth(), offerCount > 0 ? 42 : 28,
                Component.literal(offerCount > 0
                        ? "This category contains " + offerCount + " offer(s). Move them or delete together?"
                        : "Delete empty category?"));
        warning.setAutoSize(false)
                .setWrapText(true)
                .setColor(0xFFFFC95A)
                .setAlignment(TextLabel.HorizontalAlignment.CENTER, TextLabel.VerticalAlignment.CENTER);
        opened.addWidget(warning);

        if (offerCount > 0) {
            int y = 50;
            for (CatalogComponent target : moveTargets) {
                if (y + 20 > opened.getContentHeight() - 28) {
                    break;
                }

                ButtonWidget moveButton = createModalButton(0, y, opened.getContentWidth(), 20,
                        Component.literal("Move to " + target.getId()), ignored -> {
                            if (shopScreen.deleteDraftCategory(category, target)) {
                                opened.close();
                                ShopToasts.success(Component.literal("Category deleted; offers moved"));
                            }
                        });
                opened.addWidget(moveButton);
                y += 22;
            }
        }

        int buttonY = Math.max(offerCount > 0 ? 72 : 48, opened.getContentHeight() - 24);
        int buttonWidth = Math.max(1, opened.getContentWidth() / 2 - 4);
        ButtonWidget cancel = createModalButton(0, buttonY, buttonWidth, 20, Component.literal("Cancel"), ignored -> opened.close());
        ButtonWidget delete = createModalButton(buttonWidth + 8, buttonY, buttonWidth, 20,
                Component.literal(offerCount > 0 ? "Delete All" : "Delete"), ignored -> {
                    if (shopScreen.deleteDraftCategory(category, null)) {
                        opened.close();
                        ShopToasts.success(Component.literal("Category deleted"));
                    }
                });

        opened.addWidget(cancel);
        opened.addWidget(delete);
    }

    public void openCreateCurrencyModal() {
        if (!isEditorMode()) {
            return;
        }

        ModalWidget modal = new ModalWidget(260, 118)
                .setTitle(Component.literal("Add Currency"))
                .setCloseOnEsc(true)
                .setCloseOnOutsideClick(false);
        ModalWidget opened = ModalWidget.openNested(this, modal);
        if (opened == null) {
            return;
        }

        TextLabel label = new TextLabel(0, 4, opened.getContentWidth(), 22,
                Component.literal("Choose currency type:"));
        label.setAutoSize(false)
                .setColor(0xFFAEB4C6)
                .setAlignment(TextLabel.HorizontalAlignment.CENTER, TextLabel.VerticalAlignment.CENTER);
        opened.addWidget(label);

        int buttonY = Math.max(34, opened.getContentHeight() - 28);
        int buttonWidth = Math.max(1, opened.getContentWidth() / 2 - 4);
        ButtonWidget text = createModalButton(0, buttonY, buttonWidth, 22, Component.literal("Text"), ignored -> {
            opened.close();
            openTextCurrencyModal(null, null);
        });
        ButtonWidget item = createModalButton(buttonWidth + 8, buttonY, buttonWidth, 22, Component.literal("Item"), ignored -> {
            opened.close();
            openItemCurrencyModal(null, null);
        });

        opened.addWidget(text);
        opened.addWidget(item);
    }

    public void openEditCurrencyModal(ResourceLocation id, ICurrency currency) {
        if (!isEditorMode() || id == null || currency == null) {
            return;
        }

        CurrencyDraft draft = CurrencyDraft.fromCurrency(currency);
        if (draft != null && draft.kind() == CurrencyDraft.Kind.ITEM) {
            openItemCurrencyModal(id, draft);
        } else {
            openTextCurrencyModal(id, draft);
        }
    }

    public boolean deleteCurrency(ResourceLocation id) {
        if (!isEditorMode() || id == null || !shopScreen.canDeleteCurrency(id)) {
            ShopToasts.warning(Component.literal("This currency cannot be deleted"));
            return false;
        }

        boolean deleted = shopScreen.deleteDraftCurrency(id);
        if (deleted) {
            ShopToasts.success(Component.literal("Currency deleted"));
        } else {
            ShopToasts.error(Component.literal("Failed to delete currency"));
        }
        return deleted;
    }

    private void openTextCurrencyModal(@Nullable ResourceLocation existingId, @Nullable CurrencyDraft draft) {
        boolean editing = existingId != null;
        ModalWidget modal = new ModalWidget(300, editing ? 184 : 208)
                .setTitle(Component.literal(editing ? "Edit Text Currency" : "Add Text Currency"))
                .setCloseOnEsc(true)
                .setCloseOnOutsideClick(false);
        ModalWidget opened = ModalWidget.openNested(this, modal);
        if (opened == null) {
            return;
        }

        int y = 4;
        final InputTextBox idInput;
        if (editing) {
            TextLabel idLabel = new TextLabel(0, y, opened.getContentWidth(), 20,
                    Component.literal("ID: " + existingId));
            idLabel.setAutoSize(false)
                    .setColor(0xFFAEB4C6)
                    .setAlignment(TextLabel.HorizontalAlignment.LEFT, TextLabel.VerticalAlignment.CENTER);
            opened.addWidget(idLabel);
            idInput = null;
            y += 24;
        } else {
            TextLabel idLabel = new TextLabel(0, y, opened.getContentWidth(), 16, Component.literal("ID:"));
            idLabel.setAutoSize(false).setColor(0xFFAEB4C6);
            opened.addWidget(idLabel);
            y += 18;
            idInput = new InputTextBox(0, y, opened.getContentWidth(), 20);
            idInput.setResourceLocationOnly();
            idInput.setMaxLength(128);
            idInput.setPlaceholder(Component.literal("sdm:coins"));
            idInput.setClientSideWidget();
            opened.addWidget(idInput);
            y += 24;
        }

        TextLabel nameLabel = new TextLabel(0, y, opened.getContentWidth(), 16, Component.literal("Display name or lang key:"));
        nameLabel.setAutoSize(false).setColor(0xFFAEB4C6);
        opened.addWidget(nameLabel);
        y += 18;

        InputTextBox nameInput = new InputTextBox(0, y, opened.getContentWidth(), 20);
        nameInput.setMaxLength(128);
        nameInput.setCurrentStringSilently(draft == null ? "" : draft.displayName());
        nameInput.setPlaceholder(Component.literal("Coins or shop.component.misc.catalog.uuid"));
        nameInput.setClientSideWidget();
        opened.addWidget(nameInput);
        y += 24;

        TextLabel iconLabel = new TextLabel(0, y, opened.getContentWidth(), 16, Component.literal("Icon text or texture:"));
        iconLabel.setAutoSize(false).setColor(0xFFAEB4C6);
        opened.addWidget(iconLabel);
        y += 18;

        InputTextBox iconInput = new InputTextBox(0, y, opened.getContentWidth(), 20);
        iconInput.setMaxLength(128);
        iconInput.setCurrentStringSilently(draft == null ? "$" : draft.iconText());
        iconInput.setPlaceholder(Component.literal("$ or minecraft:textures/..."));
        iconInput.setClientSideWidget();
        opened.addWidget(iconInput);

        int buttonY = Math.max(y + 30, opened.getContentHeight() - 22);
        int buttonWidth = Math.max(1, opened.getContentWidth() / 2 - 4);
        ButtonWidget cancel = createModalButton(0, buttonY, buttonWidth, 20, Component.literal("Cancel"), ignored -> opened.close());
        ButtonWidget save = createModalButton(buttonWidth + 8, buttonY, buttonWidth, 20,
                Component.literal(editing ? "Save" : "Add"), ignored -> {
            ResourceLocation id = editing ? existingId : parseCurrencyId(idInput == null ? "" : idInput.getCurrentString());
            if (id == null) {
                ShopToasts.warning(Component.literal("Invalid currency id"));
                return;
            }

            String name = nameInput.getCurrentString() == null ? "" : nameInput.getCurrentString().trim();
            if (name.isEmpty()) {
                ShopToasts.warning(Component.literal("Currency name is empty"));
                return;
            }

            if (shopScreen.upsertTextCurrency(id, name, iconInput.getCurrentString())) {
                opened.close();
                ShopToasts.success(Component.literal(editing ? "Currency updated" : "Currency added"));
            }
        });

        opened.addWidget(cancel);
        opened.addWidget(save);
        if (!editing && idInput != null) {
            idInput.setFocus(true);
        } else {
            nameInput.setFocus(true);
        }
    }

    private void openItemCurrencyModal(@Nullable ResourceLocation existingId, @Nullable CurrencyDraft draft) {
        boolean editing = existingId != null;
        ModalWidget modal = new ModalWidget(300, editing ? 150 : 174)
                .setTitle(Component.literal(editing ? "Edit Item Currency" : "Add Item Currency"))
                .setCloseOnEsc(true)
                .setCloseOnOutsideClick(false);
        ModalWidget opened = ModalWidget.openNested(this, modal);
        if (opened == null) {
            return;
        }

        int y = 4;
        final InputTextBox idInput;
        if (editing) {
            TextLabel idLabel = new TextLabel(0, y, opened.getContentWidth(), 20,
                    Component.literal("ID: " + existingId));
            idLabel.setAutoSize(false)
                    .setColor(0xFFAEB4C6)
                    .setAlignment(TextLabel.HorizontalAlignment.LEFT, TextLabel.VerticalAlignment.CENTER);
            opened.addWidget(idLabel);
            idInput = null;
            y += 24;
        } else {
            TextLabel idLabel = new TextLabel(0, y, opened.getContentWidth(), 16, Component.literal("ID:"));
            idLabel.setAutoSize(false).setColor(0xFFAEB4C6);
            opened.addWidget(idLabel);
            y += 18;
            idInput = new InputTextBox(0, y, opened.getContentWidth(), 20);
            idInput.setResourceLocationOnly();
            idInput.setMaxLength(128);
            idInput.setPlaceholder(Component.literal("sdm:diamond"));
            idInput.setClientSideWidget();
            opened.addWidget(idInput);
            y += 24;
        }

        TextLabel itemLabel = new TextLabel(0, y, opened.getContentWidth(), 16, Component.literal("Item:"));
        itemLabel.setAutoSize(false).setColor(0xFFAEB4C6);
        opened.addWidget(itemLabel);
        y += 18;

        SDMItemStackSelectorWidget itemSelector = new SDMItemStackSelectorWidget(0, y, opened.getContentWidth(), false);
        ItemStack initialStack = draft == null ? ItemStack.EMPTY : draft.itemStack();
        itemSelector.setItemStack(initialStack);
        opened.addWidget(itemSelector);

        int buttonY = Math.max(y + 30, opened.getContentHeight() - 22);
        int buttonWidth = Math.max(1, opened.getContentWidth() / 2 - 4);
        ButtonWidget cancel = createModalButton(0, buttonY, buttonWidth, 20, Component.literal("Cancel"), ignored -> opened.close());
        ButtonWidget save = createModalButton(buttonWidth + 8, buttonY, buttonWidth, 20,
                Component.literal(editing ? "Save" : "Add"), ignored -> {
            ResourceLocation id = editing ? existingId : parseCurrencyId(idInput == null ? "" : idInput.getCurrentString());
            if (id == null) {
                ShopToasts.warning(Component.literal("Invalid currency id"));
                return;
            }

            ItemStack itemStack = itemSelector.getItemStack();
            if (itemStack == null || itemStack.isEmpty() || itemStack.getItem() == Items.AIR) {
                ShopToasts.warning(Component.literal("Currency item is empty"));
                return;
            }

            if (shopScreen.upsertItemCurrency(id, itemStack)) {
                opened.close();
                ShopToasts.success(Component.literal(editing ? "Currency updated" : "Currency added"));
            }
        });

        opened.addWidget(cancel);
        opened.addWidget(save);
        if (!editing && idInput != null) {
            idInput.setFocus(true);
        }
    }

    @Nullable
    private ResourceLocation parseCurrencyId(String value) {
        String raw = value == null ? "" : value.trim();
        if (raw.isEmpty()) {
            return null;
        }
        return raw.contains(":") ? ResourceLocation.tryParse(raw) : ResourceLocation.tryBuild("sdm", raw);
    }

    private ButtonWidget createModalButton(int x, int y, int width, int height, Component text, java.util.function.Consumer<com.lowdragmc.lowdraglib.gui.util.ClickData> action) {
        ButtonWidget button = new ButtonWidget(x, y, width, height, text, action);
        button.setClientSideWidget();
        button.setTextPadding(4);
        button.setMinTextScale(0.35f);
        return button;
    }

    private int indexOfCategory(List<CatalogComponent> categories, CatalogComponent category) {
        if (categories == null || category == null) {
            return -1;
        }

        for (int i = 0; i < categories.size(); i++) {
            if (sameCategory(categories.get(i), category)) {
                return i;
            }
        }
        return -1;
    }

    protected static boolean sameCategory(@Nullable CatalogComponent first, @Nullable CatalogComponent second) {
        if (first == second) {
            return true;
        }
        if (first == null || second == null) {
            return false;
        }
        if (first.getUuid() != null && second.getUuid() != null) {
            return first.getUuid().equals(second.getUuid());
        }

        return Objects.equals(first.getId(), second.getId());
    }

    @Override
    public void initWidget() {
        super.initWidget();
    }

    @Override
    public void alightWidget() {
        if (!initialized) return;
        render.alightWidgets(this);
    }

    @Override
    protected void alightWidgets() {
        alightWidget();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (render.mouseClicked(this, mouseX, mouseY, button)) {
            return true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void setSize(Size size) {
        super.setSize(size);
        if (!getContainedWidgets(true).isEmpty()) {
            alightWidget();
        }
    }

    public ShopTabsPanelElement refresh() {
        return refresh(true);
    }

    public ShopTabsPanelElement refresh(boolean relayout) {
        int previousScrollY = render instanceof DefaultShopTabsPanelRender tabsRender
                ? tabsRender.getTabsScrollY()
                : 0;
        int previousCurrenciesScrollY = render instanceof DefaultShopTabsPanelRender tabsRender
                ? tabsRender.getCurrenciesScrollY()
                : 0;
        eventScope.clear();
        ShopUIUtils.disposeChildren(this);
        clearAllWidgets();
        render.addWidgets(this);

        if (relayout) {
            alightWidget();
            if (render instanceof DefaultShopTabsPanelRender tabsRender) {
                tabsRender.restoreTabsScrollY(previousScrollY);
                tabsRender.restoreCurrenciesScrollY(previousCurrenciesScrollY);
            }
        }

        return this;
    }

    @Override
    public @Nullable ShopEntity getShopEntity() {
        return null;
    }

    @Override
    public Widget getOwner() {
        return this;
    }

    @Override
    public UIEventScope eventScope() {
        return eventScope;
    }

    @Override
    public UIEventScope screenEventScope() {
        return shopScreen.screenEventScope();
    }

    @Override
    public @Nullable ShopEditSession getEditSession() {
        return shopScreen.getEditSession();
    }

    public <Event> EventSubscription listenScreen(EventPtr<Event> event, DODEventBus.EventListener<Event> listener) {
        return shopScreen.listenScreen(event, listener);
    }

    @Override
    public void dispose() {
        eventScope.close();
        ShopUIUtils.disposeChildren(this);
    }
}
