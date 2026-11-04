package dev.sixik.sdmshop2.libs.shop.client.ui.root;

import dev.sixik.sdmshop2.libs.shop.client.SDMShopClient;
import dev.sixik.sdmshop2.libs.shop.client.cache.PersistentEditSession;
import dev.sixik.sdmshop2.libs.shop.client.cache.ShopClientCache;
import dev.sixik.sdmshop2.libs.shop.client.ui.ShopScreenController;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.ShopScreenElement;
import dev.sixik.sdmshop2.libs.shop.client.ui.editor.ShopEditorScreen;
import dev.sixik.sdmshop2.libs.shop.client.ui.editor.ShopEditorState;
import dev.sixik.sdmshop2.libs.shop.client.ui.editor.ShopExistingSessionModal;
import dev.sixik.sdmshop2.libs.shop.client.ui.theme.ShopTheme;
import dev.sixik.unigui.api.layout.Align;
import dev.sixik.unigui.api.layout.FlexDirection;
import dev.sixik.unigui.api.layout.LayoutStyle;
import dev.sixik.unigui.api.widget.Widget;
import dev.sixik.unigui.widgets.containers.HBox;
import dev.sixik.unigui.widgets.containers.PanelWidget;
import dev.sixik.unigui.widgets.containers.WrapPanel;
import dev.sixik.unigui.widgets.interaction.Button;
import dev.sixik.unigui.widgets.interaction.TextField;

public class ShopToolPanel implements ShopScreenElement {

    private Button editBtn;
    private Button favBtn;
    private TextField searchInput;
    private boolean favoritesActive = false;

    @Override
    public Widget initWidget(PanelWidget root) {
        this.favoritesActive = false;
        HBox toolbar = new HBox();
        toolbar.layout(style -> style
                .flexDirection(FlexDirection.ROW)
                .alignItems(Align.CENTER)
                .padding(4.0f, 6.0f)
                .gap(6.0f)
                .height(30.0f)
                .flexNone()
        );

        toolbar.boxVisualEnabled(true);
        toolbar.backgroundVisible(true);
        toolbar.background(ShopTheme.color(ShopTheme.BG_SURFACE));
        toolbar.borderVisible(true);
        toolbar.border(ShopTheme.color(ShopTheme.BORDER_SUBTLE));
        toolbar.borderWidth(ShopTheme.BORDER_WIDTH_THIN);
        toolbar.radius(ShopTheme.RADIUS_PANEL);

        editBtn = createToolButton("Edit");
        editBtn.onClick(event -> handleEditToggle());

        WrapPanel spacer = new WrapPanel();
        spacer.layout(LayoutStyle::expand);
        WrapPanel spacer2 = new WrapPanel();
        spacer2.layout(LayoutStyle::expand);

        HBox searchGroup = new HBox();
        searchGroup.layout(style -> style
                .flexDirection(FlexDirection.ROW)
                .alignItems(Align.CENTER)
                .gap(4.0f)
                .flexNone()
        );

        searchInput = new TextField();
        searchInput.placeholder("Search offers...");
        searchInput.boxVisualEnabled(true);
        searchInput.backgroundVisible(true);
        searchInput.background(ShopTheme.color(ShopTheme.BG_INPUT));
        searchInput.borderVisible(true);
        searchInput.border(ShopTheme.color(ShopTheme.BORDER_SLOT));
        searchInput.borderWidth(ShopTheme.BORDER_WIDTH_THIN);
        searchInput.radius(ShopTheme.RADIUS_BUTTON);
        searchInput.textColor().setArgb(ShopTheme.TEXT_PRIMARY);
        searchInput.placeholderColor().setArgb(ShopTheme.TEXT_MUTED);
        searchInput.layout(style -> style
                .width(200.0f)
                .height(20.0f)
                .flexNone()
        );
        searchInput.onTextChanged(event -> {
            ShopScreenController.onSearchQueryChanged(event.newText());
        });

        favBtn = createToolButton("★");
        favBtn.textColor(ShopTheme.color(ShopTheme.GOLD_PRIMARY));
        favBtn.layout(style -> style.fixed(20.0f, 20.0f).flexNone());
        favBtn.onClick(event -> ShopScreenController.toggleFavoritesFirst());

        searchGroup.addChild(searchInput);
        searchGroup.addChild(favBtn);

        toolbar.addChild(editBtn);
        toolbar.addChild(spacer);
        toolbar.addChild(searchGroup);
        toolbar.addChild(spacer2);

        root.addChild(toolbar);
        return toolbar;
    }

    private void handleEditToggle() {
        PersistentEditSession saved = ShopClientCache.getEditSession();
        if (saved != null && saved.shopId() != null && SDMShopClient.Shop != null
                && !saved.shopId().equals(SDMShopClient.Shop.getId())) {
            ShopScreenController.openModal(new ShopExistingSessionModal(saved, SDMShopClient.Shop.getId()));
        } else {
            ShopEditorState.get().startEditor(false);
            ShopEditorScreen.open();
        }
    }

    public void setFavoritesActive(boolean active) {
        this.favoritesActive = active;
        if (favBtn != null) {
            if (active) {
                favBtn.background(ShopTheme.color(ShopTheme.BG_CARD_HOVER));
                favBtn.border(ShopTheme.color(ShopTheme.GOLD_PRIMARY));
            } else {
                favBtn.background(ShopTheme.color(ShopTheme.BG_CARD));
                favBtn.border(ShopTheme.color(ShopTheme.BORDER_SUBTLE));
            }
        }
    }

    private Button createToolButton(String text) {
        Button btn = new Button(text);
        btn.themeEnabled(false);
        btn.backgroundVisible(true);
        btn.background(ShopTheme.color(ShopTheme.BG_CARD));
        btn.hoverBackground(ShopTheme.color(ShopTheme.BG_CARD_HOVER));
        btn.borderVisible(true);
        btn.border(ShopTheme.color(ShopTheme.BORDER_SUBTLE));
        btn.borderWidth(ShopTheme.BORDER_WIDTH_THIN);
        btn.radius(ShopTheme.RADIUS_BUTTON);
        btn.textColor(ShopTheme.color(ShopTheme.TEXT_SECONDARY));
        btn.textPadding(6.0f, 1.0f);
        btn.layout(style -> style
                .height(20.0f)
                .flexNone()
        );
        return btn;
    }

    @Override
    public int priority() {
        return -1;
    }
}
