package dev.sixik.sdmshop2.libs.shop.client.ui.elements;

import com.lowdragmc.lowdraglib.gui.texture.ResourceTexture;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.utils.Size;
import dev.sixik.sdmshop2.libs.platform.utils.eventbus.DODEventBus;
import dev.sixik.sdmshop2.libs.platform.utils.eventbus.EventPtr;
import dev.sixik.sdmshop2.libs.platform.utils.eventbus.EventSubscription;
import dev.sixik.sdmshop2.libs.sdmeconomy.CurrencyDraft;
import dev.sixik.sdmshop2.libs.sdmeconomy.ICurrency;
import dev.sixik.sdmshop2.libs.shop.base.ShopEntity;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.*;
import dev.sixik.sdmshop2.libs.shop.client.ui.events.ShopUIEvents;
import dev.sixik.sdmshop2.libs.shop.client.ui.style.DefaultShopTabsPanelRender;
import dev.sixik.sdmshop2.libs.shop.client.ui.toast.ShopToasts;
import dev.sixik.sdmshop2.libs.shop.client.ui.widgets.SDMItemStackSelectorWidget;
import dev.sixik.sdmshop2.libs.shop.client.ui.widgets.base.ShopWidgetGroup;
import dev.sixik.sdmshop2.libs.shop.components.misc.CatalogComponent;
import dev.sixik.sdmshop2.libs.shop.editor.ShopEditSession;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.ButtonWidget;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.InputTextBox;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.TextLabel;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.containers.ModalWidget;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class ShopTabsPanelElement extends ShopWidgetGroup implements
        ShopUiElement, WidgetContextRender, UIDisposable {
    private static final int TEXTURE_PICKER_GRID_ITEM_SIZE = 24;
    private static final int TEXTURE_PICKER_GRID_GAP = 3;
    private static final int TEXTURE_PICKER_SCROLLBAR_WIDTH = 4;
    private static final int TEXTURE_PICKER_SCROLLBAR_PADDING = 2;


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
            ShopToasts.success(Component.translatable("shop.ui.toast.category_moved"));
        }
    }

    public void openRenameCategoryModal(CatalogComponent category) {
        if (!isEditorMode() || category == null) {
            return;
        }

        ModalWidget modal = new ModalWidget(280, 116)
                .setTitle(Component.translatable("shop.ui.category.rename.title"))
                .setCloseOnEsc(true)
                .setCloseOnOutsideClick(false);
        ModalWidget opened = ModalWidget.openNested(this, modal);
        if (opened == null) {
            return;
        }

        TextLabel label = new TextLabel(0, 4, opened.getContentWidth(), 20,
                Component.translatable("shop.ui.category.rename.name_label"));
        label.setAutoSize(false)
                .setAlignment(TextLabel.HorizontalAlignment.LEFT, TextLabel.VerticalAlignment.CENTER)
                .setColor(0xFFAEB4C6);
        opened.addWidget(label);

        InputTextBox input = new InputTextBox(0, 28, opened.getContentWidth(), 20);
        input.setCurrentStringSilently(category.getId());
        input.setPlaceholder(Component.translatable("shop.ui.category.rename.placeholder"));
        input.setMaxLength(128);
        input.setClientSideWidget();
        opened.addWidget(input);

        int buttonY = Math.max(56, opened.getContentHeight() - 24);
        int buttonWidth = Math.max(1, opened.getContentWidth() / 2 - 4);
        ButtonWidget cancel = createModalButton(0, buttonY, buttonWidth, 20, Component.translatable("shop.ui.common.cancel"), ignored -> opened.close());
        ButtonWidget rename = createModalButton(buttonWidth + 8, buttonY, buttonWidth, 20, Component.translatable("shop.ui.common.rename"), ignored -> {
            String value = input.getCurrentString() == null ? "" : input.getCurrentString().trim();
            if (value.isEmpty()) {
                ShopToasts.warning(Component.translatable("shop.ui.toast.category_name_empty"));
                return;
            }

            if (shopScreen.renameDraftCategory(category, value)) {
                opened.close();
                ShopToasts.success(Component.translatable("shop.ui.toast.category_renamed"));
            }
        });

        opened.addWidget(cancel);
        opened.addWidget(rename);
        input.setFocus(true);
    }

    public void openCategoryItemIconModal(CatalogComponent category) {
        if (!isEditorMode() || category == null) {
            return;
        }

        ModalWidget modal = new ModalWidget(300, 118)
                .setTitle(Component.translatable("shop.ui.category.icon.item.title"))
                .setCloseOnEsc(true)
                .setCloseOnOutsideClick(false);
        ModalWidget opened = ModalWidget.openNested(this, modal);
        if (opened == null) {
            return;
        }

        TextLabel label = new TextLabel(0, 4, opened.getContentWidth(), 20,
                Component.translatable("shop.ui.category.icon.item_label"));
        label.setAutoSize(false)
                .setAlignment(TextLabel.HorizontalAlignment.LEFT, TextLabel.VerticalAlignment.CENTER)
                .setColor(0xFFAEB4C6);
        opened.addWidget(label);

        SDMItemStackSelectorWidget itemSelector = new SDMItemStackSelectorWidget(0, 28, opened.getContentWidth(), false);
        itemSelector.setItemStack(category.hasItemIcon() ? category.getIconItem() : ItemStack.EMPTY);
        opened.addWidget(itemSelector);

        int buttonY = Math.max(58, opened.getContentHeight() - 24);
        int buttonWidth = Math.max(1, (opened.getContentWidth() - 12) / 3);
        ButtonWidget cancel = createModalButton(0, buttonY, buttonWidth, 20, Component.translatable("shop.ui.common.cancel"), ignored -> opened.close());
        ButtonWidget clear = createModalButton(buttonWidth + 6, buttonY, buttonWidth, 20, Component.translatable("shop.ui.common.clear"), ignored -> {
            if (shopScreen.updateDraftCategoryIcon(category, CatalogComponent.IconType.NONE, ItemStack.EMPTY, CatalogComponent.EMPTY_ICON_TEXTURE)) {
                opened.close();
                ShopToasts.success(Component.translatable("shop.ui.toast.category_icon_cleared"));
            }
        });
        ButtonWidget save = createModalButton((buttonWidth + 6) * 2, buttonY, buttonWidth, 20, Component.translatable("shop.ui.common.save"), ignored -> {
            ItemStack itemStack = itemSelector.getItemStack();
            if (itemStack == null || itemStack.isEmpty() || itemStack.getItem() == Items.AIR) {
                ShopToasts.warning(Component.translatable("shop.ui.toast.category_icon_item_empty"));
                return;
            }

            if (shopScreen.updateDraftCategoryIcon(category, CatalogComponent.IconType.ITEM, itemStack, CatalogComponent.EMPTY_ICON_TEXTURE)) {
                opened.close();
                ShopToasts.success(Component.translatable("shop.ui.toast.category_icon_updated"));
            }
        });

        opened.addWidget(cancel);
        opened.addWidget(clear);
        opened.addWidget(save);
    }

    public void openCategoryTextureIconModal(CatalogComponent category) {
        if (!isEditorMode() || category == null) {
            return;
        }

        ModalWidget modal = new ModalWidget(340, 280)
                .setTitle(Component.translatable("shop.ui.category.icon.texture.title"))
                .setCloseOnEsc(true)
                .setCloseOnOutsideClick(false);
        ModalWidget opened = ModalWidget.openNested(this, modal);
        if (opened == null) {
            return;
        }

        TextLabel label = new TextLabel(0, 4, opened.getContentWidth(), 20,
                Component.translatable("shop.ui.category.icon.texture_label"));
        label.setAutoSize(false)
                .setAlignment(TextLabel.HorizontalAlignment.LEFT, TextLabel.VerticalAlignment.CENTER)
                .setColor(0xFFAEB4C6);
        opened.addWidget(label);

        InputTextBox input = new InputTextBox(0, 28, opened.getContentWidth(), 20);
        input.setResourceLocationOnly();
        input.setMaxLength(256);
        input.setCurrentStringSilently(category.hasTextureIcon() ? category.getIconTexture().toString() : "");
        input.setPlaceholder(Component.translatable("shop.ui.category.icon.texture.placeholder"));
        input.setClientSideWidget();
        opened.addWidget(input);

        TextLabel searchLabel = new TextLabel(0, 52, opened.getContentWidth(), 16,
                Component.translatable("shop.ui.texture_selector.search_label"));
        searchLabel.setAutoSize(false)
                .setAlignment(TextLabel.HorizontalAlignment.LEFT, TextLabel.VerticalAlignment.CENTER)
                .setColor(0xFFAEB4C6);
        opened.addWidget(searchLabel);

        InputTextBox search = new InputTextBox(0, 70, opened.getContentWidth(), 20);
        search.setMaxLength(128);
        search.setPlaceholder(Component.translatable("shop.ui.texture_selector.search.placeholder"));
        search.setClientSideWidget();
        opened.addWidget(search);

        int buttonY = Math.max(58, opened.getContentHeight() - 24);
        int gridY = 94;
        int gridHeight = Math.max(1, buttonY - gridY - 6);
        TexturePickerGrid grid = new TexturePickerGrid(0, gridY, opened.getContentWidth(), gridHeight, selected -> {
            input.setCurrentStringSilently(selected.toString());
            input.setFocus(false);
            search.setFocus(false);
        });
        grid.setClientSideWidget();
        opened.addWidget(grid);

        List<ResourceLocation> allTextures = collectTextureIconResources();
        final Runnable[] rebuildTextureGrid = new Runnable[1];
        rebuildTextureGrid[0] = () -> rebuildTexturePickerGrid(grid, allTextures, search.getCurrentString());
        search.setTextResponder(ignored -> rebuildTextureGrid[0].run());
        rebuildTextureGrid[0].run();

        int buttonWidth = Math.max(1, (opened.getContentWidth() - 12) / 3);
        ButtonWidget cancel = createModalButton(0, buttonY, buttonWidth, 20, Component.translatable("shop.ui.common.cancel"), ignored -> opened.close());
        ButtonWidget clear = createModalButton(buttonWidth + 6, buttonY, buttonWidth, 20, Component.translatable("shop.ui.common.clear"), ignored -> {
            if (shopScreen.updateDraftCategoryIcon(category, CatalogComponent.IconType.NONE, ItemStack.EMPTY, CatalogComponent.EMPTY_ICON_TEXTURE)) {
                opened.close();
                ShopToasts.success(Component.translatable("shop.ui.toast.category_icon_cleared"));
            }
        });
        ButtonWidget save = createModalButton((buttonWidth + 6) * 2, buttonY, buttonWidth, 20, Component.translatable("shop.ui.common.save"), ignored -> {
            ResourceLocation texture = parseCategoryIconTexture(input.getCurrentString());
            if (texture == null) {
                ShopToasts.warning(Component.translatable("shop.ui.toast.invalid_category_icon_texture"));
                return;
            }

            if (shopScreen.updateDraftCategoryIcon(category, CatalogComponent.IconType.TEXTURE, ItemStack.EMPTY, texture)) {
                opened.close();
                ShopToasts.success(Component.translatable("shop.ui.toast.category_icon_updated"));
            }
        });

        opened.addWidget(cancel);
        opened.addWidget(clear);
        opened.addWidget(save);
        if (allTextures.isEmpty()) {
            input.setFocus(true);
        } else {
            search.setFocus(true);
        }
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
                .setTitle(Component.translatable("shop.ui.category.move.title"))
                .setCloseOnEsc(true)
                .setCloseOnOutsideClick(false);
        ModalWidget opened = ModalWidget.openNested(this, modal);
        if (opened == null) {
            return;
        }

        TextLabel label = new TextLabel(0, 4, opened.getContentWidth(), 20,
                Component.translatable("shop.ui.common.target_position", categories.size()));
        label.setAutoSize(false)
                .setAlignment(TextLabel.HorizontalAlignment.LEFT, TextLabel.VerticalAlignment.CENTER)
                .setColor(0xFFAEB4C6);
        opened.addWidget(label);

        InputTextBox input = new InputTextBox(0, 28, opened.getContentWidth(), 20);
        input.setNumbersOnly(1, categories.size());
        input.setCurrentStringSilently(currentIndex + 1);
        input.setPlaceholder(Component.translatable("shop.ui.common.position"));
        input.setClientSideWidget();
        opened.addWidget(input);

        int buttonY = Math.max(56, opened.getContentHeight() - 24);
        int buttonWidth = Math.max(1, opened.getContentWidth() / 2 - 4);
        ButtonWidget cancel = createModalButton(0, buttonY, buttonWidth, 20, Component.translatable("shop.ui.common.cancel"), ignored -> opened.close());
        ButtonWidget move = createModalButton(buttonWidth + 8, buttonY, buttonWidth, 20, Component.translatable("shop.ui.common.move"), ignored -> {
            int position;
            try {
                position = Integer.parseInt(input.getCurrentString().trim());
            } catch (Exception e) {
                ShopToasts.warning(Component.translatable("shop.ui.toast.invalid_position"));
                return;
            }

            if (shopScreen.moveDraftCategoryTo(category, position - 1)) {
                opened.close();
                ShopToasts.success(Component.translatable("shop.ui.toast.category_moved"));
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
                .setTitle(Component.translatable("shop.ui.category.delete.title"))
                .setCloseOnEsc(true)
                .setCloseOnOutsideClick(false);
        ModalWidget opened = ModalWidget.openNested(this, modal);
        if (opened == null) {
            return;
        }

        TextLabel warning = new TextLabel(0, 4, opened.getContentWidth(), offerCount > 0 ? 42 : 28,
                offerCount > 0
                        ? Component.translatable("shop.ui.category.delete.warning_with_offers", offerCount)
                        : Component.translatable("shop.ui.category.delete.warning_empty"));
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
                        Component.translatable("shop.ui.category.delete.move_to", target.getId()), ignored -> {
                            if (shopScreen.deleteDraftCategory(category, target)) {
                                opened.close();
                                ShopToasts.success(Component.translatable("shop.ui.toast.category_deleted_offers_moved"));
                            }
                        });
                opened.addWidget(moveButton);
                y += 22;
            }
        }

        int buttonY = Math.max(offerCount > 0 ? 72 : 48, opened.getContentHeight() - 24);
        int buttonWidth = Math.max(1, opened.getContentWidth() / 2 - 4);
        ButtonWidget cancel = createModalButton(0, buttonY, buttonWidth, 20, Component.translatable("shop.ui.common.cancel"), ignored -> opened.close());
        ButtonWidget delete = createModalButton(buttonWidth + 8, buttonY, buttonWidth, 20,
                Component.translatable(offerCount > 0 ? "shop.ui.common.delete_all" : "shop.ui.common.delete"), ignored -> {
                    if (shopScreen.deleteDraftCategory(category, null)) {
                        opened.close();
                        ShopToasts.success(Component.translatable("shop.ui.toast.category_deleted"));
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
                .setTitle(Component.translatable("shop.ui.currency.add.title"))
                .setCloseOnEsc(true)
                .setCloseOnOutsideClick(false);
        ModalWidget opened = ModalWidget.openNested(this, modal);
        if (opened == null) {
            return;
        }

        TextLabel label = new TextLabel(0, 4, opened.getContentWidth(), 22,
                Component.translatable("shop.ui.currency.add.type_label"));
        label.setAutoSize(false)
                .setColor(0xFFAEB4C6)
                .setAlignment(TextLabel.HorizontalAlignment.CENTER, TextLabel.VerticalAlignment.CENTER);
        opened.addWidget(label);

        int buttonY = Math.max(34, opened.getContentHeight() - 28);
        int buttonWidth = Math.max(1, opened.getContentWidth() / 2 - 4);
        ButtonWidget text = createModalButton(0, buttonY, buttonWidth, 22, Component.translatable("shop.ui.currency.type.text"), ignored -> {
            opened.close();
            openTextCurrencyModal(null, null);
        });
        ButtonWidget item = createModalButton(buttonWidth + 8, buttonY, buttonWidth, 22, Component.translatable("shop.ui.currency.type.item"), ignored -> {
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
            ShopToasts.warning(Component.translatable("shop.ui.toast.currency_delete_protected"));
            return false;
        }

        boolean deleted = shopScreen.deleteDraftCurrency(id);
        if (deleted) {
            ShopToasts.success(Component.translatable("shop.ui.toast.currency_deleted"));
        } else {
            ShopToasts.error(Component.translatable("shop.ui.toast.currency_delete_failed"));
        }
        return deleted;
    }

    private void openTextCurrencyModal(@Nullable ResourceLocation existingId, @Nullable CurrencyDraft draft) {
        boolean editing = existingId != null;
        ModalWidget modal = new ModalWidget(300, editing ? 184 : 208)
                .setTitle(Component.translatable(editing ? "shop.ui.currency.text.edit.title" : "shop.ui.currency.text.add.title"))
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
                    Component.translatable("shop.ui.currency.id_value", existingId));
            idLabel.setAutoSize(false)
                    .setColor(0xFFAEB4C6)
                    .setAlignment(TextLabel.HorizontalAlignment.LEFT, TextLabel.VerticalAlignment.CENTER);
            opened.addWidget(idLabel);
            idInput = null;
            y += 24;
        } else {
            TextLabel idLabel = new TextLabel(0, y, opened.getContentWidth(), 16, Component.translatable("shop.ui.currency.id_label"));
            idLabel.setAutoSize(false).setColor(0xFFAEB4C6);
            opened.addWidget(idLabel);
            y += 18;
            idInput = new InputTextBox(0, y, opened.getContentWidth(), 20);
            idInput.setResourceLocationOnly();
            idInput.setMaxLength(128);
            idInput.setPlaceholder(Component.translatable("shop.ui.currency.text.id_placeholder"));
            idInput.setClientSideWidget();
            opened.addWidget(idInput);
            y += 24;
        }

        TextLabel nameLabel = new TextLabel(0, y, opened.getContentWidth(), 16, Component.translatable("shop.ui.currency.display_name_label"));
        nameLabel.setAutoSize(false).setColor(0xFFAEB4C6);
        opened.addWidget(nameLabel);
        y += 18;

        InputTextBox nameInput = new InputTextBox(0, y, opened.getContentWidth(), 20);
        nameInput.setMaxLength(128);
        nameInput.setCurrentStringSilently(draft == null ? "" : draft.displayName());
        nameInput.setPlaceholder(Component.translatable("shop.ui.currency.display_name_placeholder"));
        nameInput.setClientSideWidget();
        opened.addWidget(nameInput);
        y += 24;

        TextLabel iconLabel = new TextLabel(0, y, opened.getContentWidth(), 16, Component.translatable("shop.ui.currency.icon_label"));
        iconLabel.setAutoSize(false).setColor(0xFFAEB4C6);
        opened.addWidget(iconLabel);
        y += 18;

        InputTextBox iconInput = new InputTextBox(0, y, opened.getContentWidth(), 20);
        iconInput.setMaxLength(128);
        iconInput.setCurrentStringSilently(draft == null ? "$" : draft.iconText());
        iconInput.setPlaceholder(Component.translatable("shop.ui.currency.icon_placeholder"));
        iconInput.setClientSideWidget();
        opened.addWidget(iconInput);

        int buttonY = Math.max(y + 30, opened.getContentHeight() - 22);
        int buttonWidth = Math.max(1, opened.getContentWidth() / 2 - 4);
        ButtonWidget cancel = createModalButton(0, buttonY, buttonWidth, 20, Component.translatable("shop.ui.common.cancel"), ignored -> opened.close());
        ButtonWidget save = createModalButton(buttonWidth + 8, buttonY, buttonWidth, 20,
                Component.translatable(editing ? "shop.ui.common.save" : "shop.ui.common.add"), ignored -> {
            ResourceLocation id = editing ? existingId : parseCurrencyId(idInput == null ? "" : idInput.getCurrentString());
            if (id == null) {
                ShopToasts.warning(Component.translatable("shop.ui.toast.invalid_currency_id"));
                return;
            }

            String name = nameInput.getCurrentString() == null ? "" : nameInput.getCurrentString().trim();
            if (name.isEmpty()) {
                ShopToasts.warning(Component.translatable("shop.ui.toast.currency_name_empty"));
                return;
            }

            if (shopScreen.upsertTextCurrency(id, name, iconInput.getCurrentString())) {
                opened.close();
                ShopToasts.success(Component.translatable(editing ? "shop.ui.toast.currency_updated" : "shop.ui.toast.currency_added"));
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
                .setTitle(Component.translatable(editing ? "shop.ui.currency.item.edit.title" : "shop.ui.currency.item.add.title"))
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
                    Component.translatable("shop.ui.currency.id_value", existingId));
            idLabel.setAutoSize(false)
                    .setColor(0xFFAEB4C6)
                    .setAlignment(TextLabel.HorizontalAlignment.LEFT, TextLabel.VerticalAlignment.CENTER);
            opened.addWidget(idLabel);
            idInput = null;
            y += 24;
        } else {
            TextLabel idLabel = new TextLabel(0, y, opened.getContentWidth(), 16, Component.translatable("shop.ui.currency.id_label"));
            idLabel.setAutoSize(false).setColor(0xFFAEB4C6);
            opened.addWidget(idLabel);
            y += 18;
            idInput = new InputTextBox(0, y, opened.getContentWidth(), 20);
            idInput.setResourceLocationOnly();
            idInput.setMaxLength(128);
            idInput.setPlaceholder(Component.translatable("shop.ui.currency.item.id_placeholder"));
            idInput.setClientSideWidget();
            opened.addWidget(idInput);
            y += 24;
        }

        TextLabel itemLabel = new TextLabel(0, y, opened.getContentWidth(), 16, Component.translatable("shop.ui.currency.item_label"));
        itemLabel.setAutoSize(false).setColor(0xFFAEB4C6);
        opened.addWidget(itemLabel);
        y += 18;

        SDMItemStackSelectorWidget itemSelector = new SDMItemStackSelectorWidget(0, y, opened.getContentWidth(), false);
        ItemStack initialStack = draft == null ? ItemStack.EMPTY : draft.itemStack();
        itemSelector.setItemStack(initialStack);
        opened.addWidget(itemSelector);

        int buttonY = Math.max(y + 30, opened.getContentHeight() - 22);
        int buttonWidth = Math.max(1, opened.getContentWidth() / 2 - 4);
        ButtonWidget cancel = createModalButton(0, buttonY, buttonWidth, 20, Component.translatable("shop.ui.common.cancel"), ignored -> opened.close());
        ButtonWidget save = createModalButton(buttonWidth + 8, buttonY, buttonWidth, 20,
                Component.translatable(editing ? "shop.ui.common.save" : "shop.ui.common.add"), ignored -> {
            ResourceLocation id = editing ? existingId : parseCurrencyId(idInput == null ? "" : idInput.getCurrentString());
            if (id == null) {
                ShopToasts.warning(Component.translatable("shop.ui.toast.invalid_currency_id"));
                return;
            }

            ItemStack itemStack = itemSelector.getItemStack();
            if (itemStack == null || itemStack.isEmpty() || itemStack.getItem() == Items.AIR) {
                ShopToasts.warning(Component.translatable("shop.ui.toast.currency_item_empty"));
                return;
            }

            if (shopScreen.upsertItemCurrency(id, itemStack)) {
                opened.close();
                ShopToasts.success(Component.translatable(editing ? "shop.ui.toast.currency_updated" : "shop.ui.toast.currency_added"));
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

    @Nullable
    private ResourceLocation parseCategoryIconTexture(String value) {
        String raw = value == null ? "" : value.trim();
        if (raw.isEmpty()) {
            return null;
        }
        return ResourceLocation.tryParse(raw);
    }

    private List<ResourceLocation> collectTextureIconResources() {
        List<ResourceLocation> textures = new ArrayList<>();
        try {
            Map<ResourceLocation, ?> resources = Minecraft.getInstance().getResourceManager().listResources(
                    "textures",
                    location -> location.getPath().endsWith(".png")
            );
            textures.addAll(resources.keySet());
        } catch (Exception ignored) {
        }

        textures.sort(Comparator.comparing(ResourceLocation::toString));
        return textures;
    }

    private void rebuildTexturePickerGrid(TexturePickerGrid grid, List<ResourceLocation> allTextures, String query) {
        String filter = query == null ? "" : query.trim().toLowerCase(Locale.ROOT);
        List<ResourceLocation> filteredTextures = new ArrayList<>();
        for (ResourceLocation texture : allTextures) {
            if (texture == null) {
                continue;
            }

            String id = texture.toString().toLowerCase(Locale.ROOT);
            if (filter.isEmpty() || id.contains(filter)) {
                filteredTextures.add(texture);
            }
        }
        grid.setTextures(filteredTextures);
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

    private class TexturePickerGrid extends Widget {

        private final List<ResourceLocation> textures = new ArrayList<>();
        private final Consumer<ResourceLocation> onSelect;
        private int scrollOffset;
        private boolean draggingScrollThumb;
        private double dragStartMouseY;
        private int dragStartScrollOffset;

        private TexturePickerGrid(int x, int y, int width, int height, Consumer<ResourceLocation> onSelect) {
            super(x, y, width, height);
            this.onSelect = onSelect;
        }

        private void setTextures(List<ResourceLocation> textureIds) {
            textures.clear();
            if (textureIds != null) {
                textures.addAll(textureIds);
            }

            scrollOffset = 0;
            draggingScrollThumb = false;
            clampScrollOffset();
        }

        @Override
        public void drawInBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
            int x = getPositionX();
            int y = getPositionY();
            int width = getSizeWidth();
            int height = getSizeHeight();

            graphics.fill(x, y, x + width, y + height, 0xFF14151C);
            graphics.fill(x, y, x + width, y + 1, 0xFF3D3D4E);
            graphics.fill(x, y + height - 1, x + width, y + height, 0xFF3D3D4E);
            graphics.fill(x, y, x + 1, y + height, 0xFF3D3D4E);
            graphics.fill(x + width - 1, y, x + width, y + height, 0xFF3D3D4E);

            graphics.enableScissor(x + 1, y + 1, x + width - 1, y + height - 1);
            try {
                drawVisibleTextures(graphics, mouseX, mouseY);
                if (textures.isEmpty()) {
                    graphics.drawString(
                            Minecraft.getInstance().font,
                            Component.translatable("shop.ui.texture_selector.empty"),
                            x + 6,
                            y + 6,
                            0xFFAAAAAA,
                            false
                    );
                }
            } finally {
                graphics.disableScissor();
            }

            drawScrollBar(graphics, mouseX, mouseY);
        }

        @Override
        public void drawInForeground(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
            ResourceLocation hovered = getTextureAt(mouseX, mouseY);
            if (hovered != null) {
                graphics.renderTooltip(Minecraft.getInstance().font, Component.literal(hovered.toString()), mouseX, mouseY);
            }
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (button != 0 || !isMouseOverElement(mouseX, mouseY)) {
                return false;
            }

            if (isMouseOverScrollBar(mouseX, mouseY)) {
                if (isMouseOverScrollThumb(mouseX, mouseY)) {
                    draggingScrollThumb = true;
                    dragStartMouseY = mouseY;
                    dragStartScrollOffset = scrollOffset;
                } else {
                    scrollToMouse(mouseY);
                }
                return true;
            }

            ResourceLocation clicked = getTextureAt(mouseX, mouseY);
            if (clicked != null) {
                Widget.playButtonClickSound();
                onSelect.accept(clicked);
                return true;
            }

            return true;
        }

        @Override
        public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
            if (!draggingScrollThumb || button != 0) {
                return false;
            }

            int maxScroll = getMaxScrollOffset();
            if (maxScroll <= 0) {
                scrollOffset = 0;
                return true;
            }

            int trackHeight = getScrollTrackHeight();
            int thumbHeight = getScrollThumbHeight();
            int movable = Math.max(1, trackHeight - thumbHeight);
            scrollOffset = dragStartScrollOffset + (int) Math.round((mouseY - dragStartMouseY) * maxScroll / (double) movable);
            clampScrollOffset();
            return true;
        }

        @Override
        public boolean mouseReleased(double mouseX, double mouseY, int button) {
            if (button == 0 && draggingScrollThumb) {
                draggingScrollThumb = false;
                return true;
            }
            return false;
        }

        @Override
        public boolean mouseWheelMove(double mouseX, double mouseY, double wheelDelta) {
            if (!isMouseOverElement(mouseX, mouseY) || getMaxScrollOffset() <= 0) {
                return false;
            }

            scrollOffset += wheelDelta < 0 ? rowPitch() * 2 : -rowPitch() * 2;
            clampScrollOffset();
            return true;
        }

        private void drawVisibleTextures(GuiGraphics graphics, int mouseX, int mouseY) {
            if (textures.isEmpty()) {
                return;
            }

            int columns = getColumns();
            int pitch = rowPitch();
            int firstRow = Math.max(0, scrollOffset / pitch);
            int lastRow = Math.min(getRows() - 1, (scrollOffset + getSizeHeight() + pitch - 1) / pitch);
            int startX = getGridStartX(columns);
            int baseX = getPositionX();
            int baseY = getPositionY();

            for (int row = firstRow; row <= lastRow; row++) {
                int itemY = baseY + row * pitch - scrollOffset;
                for (int col = 0; col < columns; col++) {
                    int index = row * columns + col;
                    if (index >= textures.size()) {
                        return;
                    }

                    int itemX = baseX + startX + col * pitch;
                    ResourceLocation texture = textures.get(index);
                    boolean hovered = isMouseOverTexture(mouseX, mouseY, itemX, itemY);

                    graphics.fill(itemX, itemY, itemX + TEXTURE_PICKER_GRID_ITEM_SIZE, itemY + TEXTURE_PICKER_GRID_ITEM_SIZE, hovered ? 0xFF34384A : 0xFF20222B);
                    graphics.fill(itemX, itemY, itemX + TEXTURE_PICKER_GRID_ITEM_SIZE, itemY + 1, hovered ? 0xFF7986CB : 0xFF3D3D4E);
                    graphics.fill(itemX, itemY + TEXTURE_PICKER_GRID_ITEM_SIZE - 1, itemX + TEXTURE_PICKER_GRID_ITEM_SIZE, itemY + TEXTURE_PICKER_GRID_ITEM_SIZE, hovered ? 0xFF7986CB : 0xFF3D3D4E);
                    graphics.fill(itemX, itemY, itemX + 1, itemY + TEXTURE_PICKER_GRID_ITEM_SIZE, hovered ? 0xFF7986CB : 0xFF3D3D4E);
                    graphics.fill(itemX + TEXTURE_PICKER_GRID_ITEM_SIZE - 1, itemY, itemX + TEXTURE_PICKER_GRID_ITEM_SIZE, itemY + TEXTURE_PICKER_GRID_ITEM_SIZE, hovered ? 0xFF7986CB : 0xFF3D3D4E);
                    new ResourceTexture(texture).draw(
                            graphics,
                            mouseX,
                            mouseY,
                            itemX + 2,
                            itemY + 2,
                            TEXTURE_PICKER_GRID_ITEM_SIZE - 4,
                            TEXTURE_PICKER_GRID_ITEM_SIZE - 4
                    );
                }
            }
        }

        private void drawScrollBar(GuiGraphics graphics, int mouseX, int mouseY) {
            if (!hasScrollBar()) {
                return;
            }

            int trackX = getScrollTrackX();
            int trackY = getPositionY();
            int trackHeight = getScrollTrackHeight();
            int thumbY = getScrollThumbY();
            int thumbHeight = getScrollThumbHeight();
            int thumbColor = isMouseOverScrollThumb(mouseX, mouseY) || draggingScrollThumb ? 0xFFAAB2C5 : 0xFF6D7485;

            graphics.fill(trackX, trackY, trackX + TEXTURE_PICKER_SCROLLBAR_WIDTH, trackY + trackHeight, 0x5530303A);
            graphics.fill(trackX, thumbY, trackX + TEXTURE_PICKER_SCROLLBAR_WIDTH, thumbY + thumbHeight, thumbColor);
        }

        @Nullable
        private ResourceLocation getTextureAt(double mouseX, double mouseY) {
            if (!isMouseOverElement(mouseX, mouseY) || textures.isEmpty() || isMouseOverScrollBar(mouseX, mouseY)) {
                return null;
            }

            int columns = getColumns();
            int pitch = rowPitch();
            int startX = getGridStartX(columns);
            int localX = (int) Math.floor(mouseX - getPositionX() - startX);
            int localY = (int) Math.floor(mouseY - getPositionY() + scrollOffset);
            if (localX < 0 || localY < 0) {
                return null;
            }

            int col = localX / pitch;
            int row = localY / pitch;
            if (col < 0 || col >= columns || localX % pitch >= TEXTURE_PICKER_GRID_ITEM_SIZE || localY % pitch >= TEXTURE_PICKER_GRID_ITEM_SIZE) {
                return null;
            }

            int index = row * columns + col;
            return index >= 0 && index < textures.size() ? textures.get(index) : null;
        }

        private boolean isMouseOverTexture(double mouseX, double mouseY, int itemX, int itemY) {
            return mouseX >= itemX
                    && mouseY >= itemY
                    && mouseX < itemX + TEXTURE_PICKER_GRID_ITEM_SIZE
                    && mouseY < itemY + TEXTURE_PICKER_GRID_ITEM_SIZE;
        }

        private void scrollToMouse(double mouseY) {
            int maxScroll = getMaxScrollOffset();
            int thumbHeight = getScrollThumbHeight();
            int movable = Math.max(1, getScrollTrackHeight() - thumbHeight);
            scrollOffset = (int) Math.round((mouseY - getPositionY() - thumbHeight / 2.0) * maxScroll / movable);
            clampScrollOffset();
        }

        private int getColumns() {
            int availableWidth = Math.max(1, getSizeWidth() - TEXTURE_PICKER_SCROLLBAR_WIDTH - TEXTURE_PICKER_SCROLLBAR_PADDING);
            return Math.max(1, availableWidth / rowPitch());
        }

        private int getRows() {
            int columns = getColumns();
            return textures.isEmpty() ? 0 : (int) Math.ceil(textures.size() / (double) columns);
        }

        private int getContentHeight() {
            int rows = getRows();
            return rows <= 0 ? 0 : rows * rowPitch() - TEXTURE_PICKER_GRID_GAP;
        }

        private int getMaxScrollOffset() {
            return Math.max(0, getContentHeight() - getSizeHeight());
        }

        private void clampScrollOffset() {
            scrollOffset = Math.max(0, Math.min(scrollOffset, getMaxScrollOffset()));
        }

        private boolean hasScrollBar() {
            return getMaxScrollOffset() > 0;
        }

        private int getGridStartX(int columns) {
            int availableWidth = Math.max(1, getSizeWidth() - TEXTURE_PICKER_SCROLLBAR_WIDTH - TEXTURE_PICKER_SCROLLBAR_PADDING);
            int contentWidth = columns * rowPitch() - TEXTURE_PICKER_GRID_GAP;
            return Math.max(1, (availableWidth - contentWidth) / 2);
        }

        private int rowPitch() {
            return TEXTURE_PICKER_GRID_ITEM_SIZE + TEXTURE_PICKER_GRID_GAP;
        }

        private int getScrollTrackX() {
            return getPositionX() + getSizeWidth() - TEXTURE_PICKER_SCROLLBAR_WIDTH;
        }

        private int getScrollTrackHeight() {
            return getSizeHeight();
        }

        private int getScrollThumbHeight() {
            int contentHeight = Math.max(1, getContentHeight());
            int trackHeight = getScrollTrackHeight();
            return Math.max(12, Math.min(trackHeight, (int) Math.round(trackHeight * (trackHeight / (double) contentHeight))));
        }

        private int getScrollThumbY() {
            int maxScroll = getMaxScrollOffset();
            if (maxScroll <= 0) {
                return getPositionY();
            }

            int movable = Math.max(1, getScrollTrackHeight() - getScrollThumbHeight());
            return getPositionY() + (int) Math.round(scrollOffset * movable / (double) maxScroll);
        }

        private boolean isMouseOverScrollBar(double mouseX, double mouseY) {
            return hasScrollBar()
                    && mouseX >= getScrollTrackX()
                    && mouseY >= getPositionY()
                    && mouseX < getScrollTrackX() + TEXTURE_PICKER_SCROLLBAR_WIDTH
                    && mouseY < getPositionY() + getScrollTrackHeight();
        }

        private boolean isMouseOverScrollThumb(double mouseX, double mouseY) {
            if (!hasScrollBar()) {
                return false;
            }

            int thumbY = getScrollThumbY();
            return mouseX >= getScrollTrackX()
                    && mouseY >= thumbY
                    && mouseX < getScrollTrackX() + TEXTURE_PICKER_SCROLLBAR_WIDTH
                    && mouseY < thumbY + getScrollThumbHeight();
        }
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
