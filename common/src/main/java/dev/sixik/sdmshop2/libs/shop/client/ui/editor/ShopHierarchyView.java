package dev.sixik.sdmshop2.libs.shop.client.ui.editor;

import dev.sixik.sdmshop2.libs.sdmeconomy.CurrencyDraft;
import dev.sixik.sdmshop2.libs.sdmeconomy.ICurrency;
import dev.sixik.sdmshop2.libs.sdmeconomy.SDMEconomyServiceClient;
import dev.sixik.sdmshop2.libs.shop.base.ShopInstance;
import dev.sixik.sdmshop2.libs.shop.base.ShopOffer;
import dev.sixik.sdmshop2.libs.shop.client.ui.editor.unity.UnityCreateButton;
import dev.sixik.sdmshop2.libs.shop.client.ui.editor.unity.UnityIcons;
import dev.sixik.sdmshop2.libs.shop.client.ui.editor.unity.UnityStyles;
import dev.sixik.sdmshop2.libs.shop.client.ui.root.ShopCatalogElement;
import dev.sixik.sdmshop2.libs.shop.components.misc.CatalogComponent;
import dev.sixik.unigui.api.event.PointerPressedEvent;
import dev.sixik.unigui.api.input.PointerButton;
import dev.sixik.unigui.api.layout.Align;
import dev.sixik.unigui.api.layout.Overflow;
import dev.sixik.unigui.api.math.RectView;
import dev.sixik.unigui.api.render.DrawScope;
import dev.sixik.unigui.widgets.containers.HBox;
import dev.sixik.unigui.widgets.containers.LinearBox;
import dev.sixik.unigui.widgets.containers.ScrollView;
import dev.sixik.unigui.widgets.core.Orientation;
import dev.sixik.unigui.widgets.feedback.ContextMenu;
import dev.sixik.unigui.widgets.feedback.OverlayLayer;
import dev.sixik.unigui.widgets.interaction.Button;
import dev.sixik.unigui.widgets.interaction.TextInput;
import dev.sixik.unigui.widgets.navigation.TreeView;
import dev.sixik.unigui.widgets.navigation.TreeViewNode;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

/**
 * Панель Hierarchy в стиле Unity:
 * Дерево объектов магазина (Категории, Офферы, Валюты) с поиском и кнопкой быстрого создания (+).
 */
public class ShopHierarchyView extends LinearBox {

    private final TreeView treeView = new TreeView();
    private TreeViewNode rootShopNode;
    private TreeViewNode categoriesGroupNode;
    private TreeViewNode offersGroupNode;
    private TreeViewNode currenciesGroupNode;

    private final Map<TreeViewNode, Object> nodeObjectMap = new HashMap<>();
    private final Map<Object, TreeViewNode> objectNodeMap = new HashMap<>();
    private final Map<TreeViewNode, UnityIcons.ShopObjectType> nodeTypeMap = new HashMap<>();

    private TextInput searchInput;
    private ContextMenu activeCreateMenu;
    private ContextMenu activeItemContextMenu;
    private String currentSearch = "";
    private Consumer<String> onTabSwitchRequested;

    public ShopHierarchyView() {
        super(Orientation.VERTICAL);
        spacing(0.0f);
        layout(style -> style.sizePercent(100.0f, 100.0f).flexGrow(1.0f).flexShrink(1.0f));

        // 1. Верхняя панель: кнопка создания (+) и строка поиска
        HBox topBar = new HBox();
        topBar.spacing(4.0f);
        topBar.layout(style -> style.widthPercent(100.0f).height(24.0f)
                .padding(4.0f, 2.0f, 4.0f, 2.0f).alignItems(Align.CENTER)
                .flexGrow(0.0f).flexShrink(0.0f));

        UnityCreateButton createBtn = new UnityCreateButton();
        createBtn.onClick(e -> openCreateMenu(createBtn));
        topBar.addChild(createBtn);

        searchInput = new TextInput("");
        searchInput.placeholder("Search...");
        UnityStyles.styleTextInput(searchInput);
        searchInput.layout(style -> style.flexGrow(1.0f).height(20.0f));
        searchInput.onTextChanged(e -> {
            this.currentSearch = e.newText().trim().toLowerCase(Locale.ROOT);
            rebuildTree();
        });
        topBar.addChild(searchInput);

        addChild(topBar);
        addChild(UnityStyles.separatorLine());

        // 2. Настройка TreeView со стилями Unity
        UnityStyles.styleTreeView(treeView);
        treeView.layout(style -> style.widthPercent(100.0f).flexGrow(0.0f).flexShrink(0.0f));

        // Кастомный рендерер иконок строк дерева
        treeView.renderer((draw, widget) -> {
            if (widget instanceof TreeView.TreeRowButton row) {
                row.renderRowVisual(draw);
                TreeViewNode node = row.node();
                UnityIcons.ShopObjectType objType = nodeTypeMap.get(node);
                if (objType != null) {
                    RectView b = row.layoutBounds();
                    float iconSize = 12.0f;
                    float iconX = b.x() + 4.0f;
                    float iconY = b.y() + (b.height() - iconSize) * 0.5f;
                    UnityIcons.drawGameObjectIcon(draw, objType, iconX, iconY, iconSize);
                }
            }
        });

        // Слушатель выбора в дереве
        treeView.onSelectionChanged(e -> {
            TreeViewNode selected = treeView.selectedNode();
            if (selected != null) {
                Object target = nodeObjectMap.get(selected);
                if (target != null) {
                    ShopEditorState.get().selectTarget(target);
                    if (target instanceof ShopOffer && onTabSwitchRequested != null) {
                        onTabSwitchRequested.accept("catalog");
                    } else if (target instanceof CatalogComponent && onTabSwitchRequested != null) {
                        onTabSwitchRequested.accept("category");
                    } else if ((target instanceof ICurrency || target instanceof CurrencyDraft) && onTabSwitchRequested != null) {
                        onTabSwitchRequested.accept("money");
                    }
                }
            }
        });

        // Обработка контекстного меню по ПКМ на строках дерева
        treeView.onRowCreated(row -> {
            row.on(PointerPressedEvent.TYPE, event -> {
                if (event.button() == PointerButton.SECONDARY) {
                    TreeViewNode node = row.node();
                    if (node != null) {
                        treeView.select(node);
                        Object target = nodeObjectMap.get(node);
                        if (target != null) {
                            openItemContextMenu(event.rootX(), event.rootY(), target);
                            event.cancel();
                        }
                    }
                }
            });
        });

        ScrollView scroll = new ScrollView(treeView);
        scroll.scrollStep(18.0f);
        scroll.overlayScrollBars(false);
        scroll.scrollbarGap(2.0f);
        scroll.scrollbarTrackColor().set(UnityStyles.BORDER_DEFAULT);
        scroll.scrollbarThumbColor().set(UnityStyles.TEXT_MUTED);
        scroll.layout(style -> style.widthPercent(100.0f).heightPercent(100.0f)
                .overflowX(Overflow.AUTO).overflowY(Overflow.AUTO)
                .flexGrow(1.0f).flexShrink(1.0f));
        addChild(scroll);

        ShopEditorState.get().addListener(this::syncWithState);
        rebuildTree();
    }

    public void setOnTabSwitchRequested(Consumer<String> listener) {
        this.onTabSwitchRequested = listener;
    }

    public void rebuildTree() {
        if (activeItemContextMenu != null) {
            try {
                activeItemContextMenu.close();
            } catch (Exception ignored) {}
            activeItemContextMenu = null;
        }

        treeView.clearRoots();
        nodeObjectMap.clear();
        objectNodeMap.clear();
        nodeTypeMap.clear();

        ShopInstance shop = ShopEditorState.get().getDraftShop();
        String shopName = shop != null ? (shop.getId() != null ? shop.getId().toString() : "Shop") : "SDM Shop";

        rootShopNode = treeView.addRoot(shopName);
        rootShopNode.expanded(true);
        nodeTypeMap.put(rootShopNode, UnityIcons.ShopObjectType.SHOP);

        // 1. Узел Categories
        categoriesGroupNode = rootShopNode.addChild("Categories");
        categoriesGroupNode.expanded(true);
        nodeTypeMap.put(categoriesGroupNode, UnityIcons.ShopObjectType.CATEGORIES_FOLDER);

        if (shop != null && shop.getCategories() != null) {
            for (CatalogComponent cat : shop.getCategories().getCatalogsComponents()) {
                String id = cat.getId() != null ? cat.getId() : "unnamed";
                if (!currentSearch.isEmpty() && !id.toLowerCase(Locale.ROOT).contains(currentSearch)) {
                    continue;
                }
                TreeViewNode catNode = categoriesGroupNode.addChild(id);
                nodeObjectMap.put(catNode, cat);
                objectNodeMap.put(cat, catNode);
                nodeTypeMap.put(catNode, UnityIcons.ShopObjectType.CATEGORY);
            }
        }

        // 2. Узел Offers
        offersGroupNode = rootShopNode.addChild("Offers");
        offersGroupNode.expanded(true);
        nodeTypeMap.put(offersGroupNode, UnityIcons.ShopObjectType.OFFERS_FOLDER);

        if (shop != null && shop.getEntries() != null) {
            for (ShopOffer offer : shop.getEntries().getEntryMap().values()) {
                String title = ShopCatalogElement.resolveTitle(offer);
                if (!currentSearch.isEmpty()) {
                    boolean match = title.toLowerCase(Locale.ROOT).contains(currentSearch);
                    if (!match && offer.getUUID() != null) {
                        match = offer.getUUID().toString().toLowerCase(Locale.ROOT).contains(currentSearch);
                    }
                    if (!match) continue;
                }
                TreeViewNode offerNode = offersGroupNode.addChild(title);
                nodeObjectMap.put(offerNode, offer);
                objectNodeMap.put(offer, offerNode);
                nodeTypeMap.put(offerNode, UnityIcons.ShopObjectType.OFFER);
            }
        }

        // 3. Узел Money (Currencies)
        currenciesGroupNode = rootShopNode.addChild("Money");
        currenciesGroupNode.expanded(true);
        nodeTypeMap.put(currenciesGroupNode, UnityIcons.ShopObjectType.CURRENCIES_FOLDER);

        for (ICurrency currency : SDMEconomyServiceClient.getAllCurrencies().values()) {
            if (ShopEditorState.get().isCurrencyDeleted(currency.getId())) {
                continue;
            }
            if (ShopEditorState.get().hasCurrencyDraft(currency.getId())) {
                continue;
            }
            String name = currency.getDisplayName().getString();
            if (!currentSearch.isEmpty() && !name.toLowerCase(Locale.ROOT).contains(currentSearch)) {
                continue;
            }
            TreeViewNode curNode = currenciesGroupNode.addChild(name);
            nodeObjectMap.put(curNode, currency);
            objectNodeMap.put(currency, curNode);
            nodeTypeMap.put(curNode, UnityIcons.ShopObjectType.CURRENCY);
        }

        if (ShopEditorState.get().getSession() != null) {
            for (CurrencyDraft draft : ShopEditorState.get().getSession().currencyDrafts()) {
                if (draft.kind() == CurrencyDraft.Kind.DELETE) {
                    continue;
                }
                if (!currentSearch.isEmpty() && !draft.displayName().toLowerCase(Locale.ROOT).contains(currentSearch)) {
                    continue;
                }
                TreeViewNode draftNode = currenciesGroupNode.addChild(draft.displayName() + " [Draft]");
                nodeObjectMap.put(draftNode, draft);
                objectNodeMap.put(draft, draftNode);
                nodeTypeMap.put(draftNode, UnityIcons.ShopObjectType.CURRENCY);
            }
        }

        syncSelection();
    }

    public void selectNodeFor(@Nullable Object target) {
        if (target == null) return;
        TreeViewNode node = objectNodeMap.get(target);
        if (node != null) {
            treeView.select(node);
        }
    }

    private void syncWithState() {
        rebuildTree();
    }

    private void syncSelection() {
        Object target = ShopEditorState.get().getSelectedTarget();
        if (target != null) {
            TreeViewNode node = objectNodeMap.get(target);
            if (node != null && treeView.selectedNode() != node) {
                treeView.select(node);
            }
        }
    }

    private void openCreateMenu(Button anchor) {
        OverlayLayer overlay = findOverlayLayer(this);
        if (overlay == null) return;

        if (activeCreateMenu != null) {
            activeCreateMenu.close();
            activeCreateMenu = null;
        }

        ContextMenu menu = new ContextMenu();
        UnityStyles.styleContextMenu(menu);
        float menuWidth = 140.0f;

        UnityStyles.addContextMenuItem(menu, "Create Offer", true, menuWidth, () -> {
            ShopOffer offer = ShopEditorState.get().createNewOffer();
            rebuildTree();
            selectNodeFor(offer);
            if (onTabSwitchRequested != null) onTabSwitchRequested.accept("catalog");
        });
        UnityStyles.addContextMenuItem(menu, "Create Category", true, menuWidth, () -> {
            CatalogComponent cat = ShopEditorState.get().createNewCategory();
            rebuildTree();
            selectNodeFor(cat);
            if (onTabSwitchRequested != null) onTabSwitchRequested.accept("category");
        });
        UnityStyles.addContextMenuItem(menu, "Create Currency", true, menuWidth, () -> {
            CurrencyDraft cur = ShopEditorState.get().createNewCurrency();
            rebuildTree();
            selectNodeFor(cur);
            if (onTabSwitchRequested != null) onTabSwitchRequested.accept("money");
        });

        RectView bounds = anchor.layoutBounds();
        menu.openAt(bounds.x(), bounds.y() + bounds.height() + 2.0f);
        overlay.addOverlay(menu);
        activeCreateMenu = menu;
    }

    private void openItemContextMenu(float x, float y, Object target) {
        OverlayLayer overlay = findOverlayLayer(this);
        if (overlay == null) return;

        if (activeItemContextMenu != null) {
            try {
                activeItemContextMenu.close();
                overlay.removeOverlay(activeItemContextMenu);
            } catch (Exception ignored) {}
            activeItemContextMenu = null;
        }

        float menuWidth = 140.0f;
        ContextMenu menu = new ContextMenu();
        UnityStyles.styleContextMenu(menu);

        boolean canDelete = !(target instanceof ICurrency cur && ShopEditorState.get().isDefaultCurrency(cur.getId()))
                && !(target instanceof CurrencyDraft draft && ShopEditorState.get().isDefaultCurrency(draft.id()));
        boolean canDuplicate = true;

        // 1. Delete
        UnityStyles.addContextMenuItem(menu, "Delete", canDelete, menuWidth, () -> {
            if (target instanceof ShopOffer offer) {
                ShopEditorState.get().deleteOffer(offer);
            } else if (target instanceof CatalogComponent cat) {
                ShopEditorState.get().deleteCategory(cat);
            } else if (target instanceof CurrencyDraft draft) {
                ShopEditorState.get().deleteCurrency(draft);
            } else if (target instanceof ICurrency cur) {
                ShopEditorState.get().deleteCurrency(cur);
            }
            rebuildTree();
        });

        // 2. Duplicate
        UnityStyles.addContextMenuItem(menu, "Duplicate", canDuplicate, menuWidth, () -> {
            Object duplicated = null;
            if (target instanceof ShopOffer offer) {
                duplicated = ShopEditorState.get().duplicateOffer(offer);
            } else if (target instanceof CatalogComponent cat) {
                duplicated = ShopEditorState.get().duplicateCategory(cat);
            } else if (target instanceof CurrencyDraft draft) {
                duplicated = ShopEditorState.get().duplicateCurrency(draft);
            } else if (target instanceof ICurrency cur) {
                duplicated = ShopEditorState.get().duplicateCurrency(cur);
            }
            rebuildTree();
            if (duplicated != null) {
                selectNodeFor(duplicated);
            }
        });

        UnityStyles.addContextMenuSeparator(menu, menuWidth);

        // 3. Move To
        UnityStyles.addContextMenuItem(menu, "Move To", true, menuWidth, () -> {
            ShopEditorScreen.navigateTo(target);
        });

        menu.openAt(x, y);
        overlay.addOverlay(menu);
        activeItemContextMenu = menu;
    }

    private static OverlayLayer findOverlayLayer(dev.sixik.unigui.api.widget.Widget start) {
        dev.sixik.unigui.api.widget.Widget current = start;
        while (current != null) {
            if (current instanceof OverlayLayer layer) return layer;
            current = current.parent();
        }
        return null;
    }
}
