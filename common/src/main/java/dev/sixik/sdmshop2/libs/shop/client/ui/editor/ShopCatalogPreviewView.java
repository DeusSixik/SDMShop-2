package dev.sixik.sdmshop2.libs.shop.client.ui.editor;

import dev.sixik.sdmshop2.libs.shop.base.ShopInstance;
import dev.sixik.sdmshop2.libs.shop.base.ShopOffer;
import dev.sixik.sdmshop2.libs.shop.client.ui.editor.unity.UnityStyles;
import dev.sixik.sdmshop2.libs.shop.client.ui.root.ShopCatalogElement;
import dev.sixik.sdmshop2.libs.shop.client.ui.theme.ShopTheme;
import dev.sixik.unigui.api.core.FrameContext;
import dev.sixik.unigui.api.event.PointerPressedEvent;
import dev.sixik.unigui.api.input.PointerButton;
import dev.sixik.unigui.api.layout.*;
import dev.sixik.unigui.api.widget.Widget;
import dev.sixik.unigui.widgets.containers.GridBox;
import dev.sixik.unigui.widgets.containers.HBox;
import dev.sixik.unigui.widgets.containers.LinearBox;
import dev.sixik.unigui.widgets.containers.ScrollView;
import dev.sixik.unigui.widgets.containers.VBox;
import dev.sixik.unigui.widgets.core.Orientation;
import dev.sixik.unigui.widgets.display.Label;
import dev.sixik.unigui.widgets.dnd.DragController;
import dev.sixik.unigui.widgets.dnd.DragPayload;
import dev.sixik.unigui.widgets.dnd.DropController;
import dev.sixik.unigui.widgets.feedback.ContextMenu;
import dev.sixik.unigui.widgets.feedback.OverlayLayer;
import dev.sixik.unigui.widgets.interaction.TextInput;

import java.util.*;

/**
 * Центральная рабочая панель «Catalog» (предпросмотр офферов магазина).
 * Поддерживает Drag-and-Drop переупорядочивание предложений, контекстное меню (ПКМ)
 * и отключение звезды избранного в режиме предпросмотра.
 */
public class ShopCatalogPreviewView extends LinearBox {

    private static ContextMenu activeContextMenu = null;

    private final GridBox itemsGrid = new GridBox();
    private final List<ShopCatalogElement> currentCards = new ArrayList<>();
    private ScrollView scrollView;
    private float pendingScrollY = -1.0f;
    private long lastRenderedRevision = -1;
    private String searchQuery = "";

    public ShopCatalogPreviewView() {
        super(Orientation.VERTICAL);
        spacing(0.0f);
        layout(style -> style.sizePercent(100.0f, 100.0f).flexGrow(1.0f).flexShrink(1.0f));

        // Верхняя панель фильтрации
        HBox topBar = new HBox();
        topBar.spacing(6.0f);
        topBar.layout(style -> style.widthPercent(100.0f).height(26.0f)
                .padding(4.0f, 6.0f).alignItems(Align.CENTER).flexGrow(0.0f).flexShrink(0.0f));

        TextInput filterInput = new TextInput("");
        filterInput.placeholder("Filter offers...");
        UnityStyles.styleTextInput(filterInput);
        filterInput.layout(style -> style.width(220.0f).height(20.0f).flexNone());
        filterInput.onTextChanged(e -> {
            this.searchQuery = e.newText().trim().toLowerCase(Locale.ROOT);
            refreshGrid();
        });
        topBar.addChild(filterInput);

        addChild(topBar);
        addChild(UnityStyles.separatorLine());

        // Сетка предложений
        itemsGrid.columns(3);
        itemsGrid.horizontalSpacing(8.0f);
        itemsGrid.verticalSpacing(8.0f);
        itemsGrid.layout(style -> style
                .gridTemplateColumns(GridTrack.fr(1.0f), GridTrack.fr(1.0f), GridTrack.fr(1.0f))
                .alignContent(AlignContent.START)
                .gap(8.0f)
                .widthPercent(100.0f)
                .padding(8.0f)
        );

        this.scrollView = new ScrollView(itemsGrid);
        this.scrollView.scrollStep(24.0f);
        this.scrollView.scrollbarGap(2.0f);
        this.scrollView.scrollbarTrackColor().set(UnityStyles.BORDER_DEFAULT);
        this.scrollView.scrollbarThumbColor().set(UnityStyles.TEXT_MUTED);
        this.scrollView.layout(style -> style.sizePercent(100.0f, 100.0f)
                .overflowX(Overflow.HIDDEN).overflowY(Overflow.AUTO)
                .flexGrow(1.0f).flexShrink(1.0f));
        addChild(this.scrollView);

        ShopEditorState.get().addListener(this::onEditorStateChanged);
        refreshGrid();
    }

    private void onEditorStateChanged() {
        long currentRev = ShopEditorState.get().getRevision();
        if (currentRev != lastRenderedRevision) {
            refreshGrid();
        } else {
            updateCardSelections();
        }
    }

    public void updateCardSelections() {
        Object currentTarget = ShopEditorState.get().getSelectedTarget();
        for (ShopCatalogElement card : currentCards) {
            boolean isSelected = card.getOffer() == currentTarget;
            card.setSelected(isSelected);
            if (isSelected) {
                card.border(UnityStyles.SELECTION_BLUE);
                card.borderWidth(2.0f);
            } else {
                card.border(ShopTheme.color(ShopTheme.BORDER_SUBTLE));
                card.borderWidth(ShopTheme.BORDER_WIDTH_THIN);
            }
        }
    }

    public void refreshGrid() {
        this.lastRenderedRevision = ShopEditorState.get().getRevision();
        float prevScrollY = scrollView != null ? scrollView.scrollY() : 0.0f;
        itemsGrid.clearChildren();
        currentCards.clear();

        // 1. Первая карточка — "+ Add New Offer" с поддержкой Drop (перемещение в самое начало)
        itemsGrid.addChild(createAddOfferCard());

        ShopInstance shop = ShopEditorState.get().getDraftShop();
        if (shop == null || shop.getEntries() == null) return;

        Map<UUID, ShopOffer> map = shop.getEntries().getEntryMap();
        if (map == null || map.isEmpty()) return;

        Object currentTarget = ShopEditorState.get().getSelectedTarget();

        for (ShopOffer offer : map.values()) {
            String title = ShopCatalogElement.resolveTitle(offer);
            if (!searchQuery.isEmpty()) {
                boolean matches = title.toLowerCase(Locale.ROOT).contains(searchQuery);
                if (!matches && offer.getUUID() != null) {
                    matches = offer.getUUID().toString().toLowerCase(Locale.ROOT).contains(searchQuery);
                }
                if (!matches) continue;
            }

            ShopCatalogElement card = new ShopCatalogElement(offer, c -> {
                ShopEditorState.get().selectTarget(offer);
            });
            // В режиме Preview отключаем звездочку избранного
            card.showFavorite(false);

            boolean isSelected = offer == currentTarget;
            card.setSelected(isSelected);
            if (isSelected) {
                card.border(UnityStyles.SELECTION_BLUE);
                card.borderWidth(2.0f);
            }

            // Drag and Drop: перетаскивание карточек товаров
            String cardTitle = title;
            DragController.attach(card)
                    .dragged(card)
                    .payload(() -> DragPayload.of(
                            offer.getUUID().toString(),
                            "shop.offer",
                            cardTitle,
                            offer
                    ))
                    .threshold(4.0f)
                    .sourceOpacity(0.4f)
                    .ghost(card)
                    .ghostOpacity(0.8f)
                    .onDragStart(session -> {
                        ShopEditorState.get().selectTarget(offer);
                    });

            DropController.attach(card)
                    .accepts("shop.offer")
                    .onDragEnter(e -> {
                        card.border(UnityStyles.SELECTION_BLUE);
                        card.borderWidth(2.0f);
                    })
                    .onDragLeave(e -> {
                        boolean selected = card.getOffer() == ShopEditorState.get().getSelectedTarget();
                        if (selected) {
                            card.border(UnityStyles.SELECTION_BLUE);
                            card.borderWidth(2.0f);
                        } else {
                            card.border(ShopTheme.color(ShopTheme.BORDER_SUBTLE));
                            card.borderWidth(ShopTheme.BORDER_WIDTH_THIN);
                        }
                    })
                    .onDrop(e -> {
                        DragPayload payload = e.payload();
                        if (payload != null && payload.matchesType("shop.offer")) {
                            ShopOffer draggedOffer = payload.data(ShopOffer.class);
                            if (draggedOffer != null && !draggedOffer.getUUID().equals(offer.getUUID())) {
                                ShopInstance shopInstance = ShopEditorState.get().getDraftShop();
                                if (shopInstance != null && shopInstance.getEntries() != null) {
                                    List<ShopOffer> offers = new ArrayList<>(shopInstance.getEntries().getEntryMap().values());
                                    int targetIdx = -1;
                                    for (int i = 0; i < offers.size(); i++) {
                                        if (offers.get(i).getUUID().equals(offer.getUUID())) {
                                            targetIdx = i;
                                            break;
                                        }
                                    }
                                    if (targetIdx >= 0) {
                                        ShopEditorState.get().moveOfferToIndex(draggedOffer, targetIdx);
                                    }
                                }
                            }
                        }
                    });

            // Контекстное меню по ПКМ
            card.on(PointerPressedEvent.TYPE, event -> {
                if (event.button() == PointerButton.SECONDARY) {
                    ShopEditorState.get().selectTarget(offer);
                    openContextMenu(event.rootX(), event.rootY(), offer);
                    event.cancel();
                }
            });

            currentCards.add(card);
            itemsGrid.addChild(card);
        }

        if (scrollView != null && prevScrollY > 0.0f) {
            scrollView.scrollTo(0.0f, prevScrollY);
            pendingScrollY = prevScrollY;
        } else {
            pendingScrollY = -1.0f;
        }
    }

    private ShopOffer pendingScrollOffer;

    public void scrollToOffer(ShopOffer offer) {
        if (offer == null) return;
        if (!scrollToOfferInternal(offer)) {
            this.pendingScrollOffer = offer;
        }
    }

    private boolean scrollToOfferInternal(ShopOffer offer) {
        if (scrollView == null) return false;
        for (ShopCatalogElement card : currentCards) {
            if (card.getOffer() == offer || (card.getOffer() != null && offer.getUUID() != null && offer.getUUID().equals(card.getOffer().getUUID()))) {
                float targetY = card.layoutBounds().y();
                if (targetY > 0.0f || card.layoutBounds().height() > 0.0f) {
                    scrollView.scrollTo(0.0f, Math.max(0.0f, targetY - 16.0f));
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void tick(FrameContext context) {
        super.tick(context);
        if (pendingScrollY >= 0.0f && scrollView != null) {
            scrollView.scrollTo(0.0f, pendingScrollY);
            pendingScrollY = -1.0f;
        }
        if (pendingScrollOffer != null && scrollView != null) {
            if (scrollToOfferInternal(pendingScrollOffer)) {
                pendingScrollOffer = null;
            }
        }
    }

    private void openContextMenu(float x, float y, ShopOffer offer) {
        OverlayLayer layer = findOverlayLayer();
        if (layer == null) return;

        if (activeContextMenu != null) {
            try {
                activeContextMenu.close();
            } catch (Exception ignored) {}
            activeContextMenu = null;
        }

        ShopInstance shop = ShopEditorState.get().getDraftShop();
        if (shop == null || shop.getEntries() == null) return;

        List<ShopOffer> offers = new ArrayList<>(shop.getEntries().getEntryMap().values());
        int index = offers.indexOf(offer);
        int total = offers.size();
        boolean canMoveUp = index > 0;
        boolean canMoveDown = index >= 0 && index < total - 1;

        float menuWidth = 160.0f;
        ContextMenu menu = new ContextMenu();
        UnityStyles.styleContextMenu(menu);

        UnityStyles.addContextMenuItem(menu, "Move Up", canMoveUp, menuWidth, () -> {
            ShopEditorState.get().moveOffer(offer, -1);
        });
        UnityStyles.addContextMenuItem(menu, "Move Down", canMoveDown, menuWidth, () -> {
            ShopEditorState.get().moveOffer(offer, 1);
        });
        UnityStyles.addContextMenuSeparator(menu, menuWidth);
        UnityStyles.addContextMenuItem(menu, "Duplicate", true, menuWidth, () -> {
            ShopEditorState.get().duplicateOffer(offer);
        });
        UnityStyles.addContextMenuSeparator(menu, menuWidth);
        UnityStyles.addContextMenuItem(menu, "Delete", true, menuWidth, () -> {
            ShopEditorState.get().deleteOffer(offer);
        });

        menu.openAt(x, y);
        layer.addOverlay(menu);
        activeContextMenu = menu;
    }

    private OverlayLayer findOverlayLayer() {
        Widget current = this;
        while (current != null) {
            if (current instanceof OverlayLayer layer) return layer;
            current = current.parent();
        }
        return ShopEditorScreen.getActiveOverlay();
    }

    private VBox createAddOfferCard() {
        VBox card = new VBox();
        card.spacing(4.0f);
        card.layout(style -> style
                .height(96.0f)
                .flexNone()
                .padding(6.0f, 8.0f)
                .alignItems(Align.CENTER)
                .justifyContent(Justify.CENTER)
        );

        card.boxVisualEnabled(true);
        card.backgroundVisible(true);
        card.background(UnityStyles.BG_PANEL);
        card.borderVisible(true);
        card.border(UnityStyles.SELECTION_BLUE);
        card.borderWidth(1.0f);
        card.radius(ShopTheme.RADIUS_CARD);

        Label plus = new Label("+");
        plus.color(UnityStyles.SELECTION_BLUE);
        Label addLbl = new Label("Add New Offer");
        addLbl.color(UnityStyles.TEXT_PRIMARY);

        card.addChild(plus);
        card.addChild(addLbl);

        card.on(PointerPressedEvent.TYPE, event -> {
            if (event.button() == PointerButton.PRIMARY) {
                ShopOffer newOffer = ShopEditorState.get().createNewOffer();
                ShopEditorState.get().selectTarget(newOffer);
                refreshGrid();
                event.cancel();
            }
        });

        // Drop на карточку Add Offer переносит товар на первую позицию
        DropController.attach(card)
                .accepts("shop.offer")
                .onDragEnter(e -> {
                    card.border(UnityStyles.SELECTION_BLUE);
                    card.borderWidth(2.0f);
                })
                .onDragLeave(e -> {
                    card.border(UnityStyles.SELECTION_BLUE);
                    card.borderWidth(1.0f);
                })
                .onDrop(e -> {
                    DragPayload payload = e.payload();
                    if (payload != null && payload.matchesType("shop.offer")) {
                        ShopOffer draggedOffer = payload.data(ShopOffer.class);
                        if (draggedOffer != null) {
                            ShopEditorState.get().moveOfferToIndex(draggedOffer, 0);
                        }
                    }
                });

        return card;
    }
}
