package dev.sixik.sdmshop2.libs.shop.client.ui.root;

import dev.sixik.sdmshop2.libs.shop.client.SDMShopClient;
import dev.sixik.sdmshop2.libs.shop.client.cache.PersistentEditSession;
import dev.sixik.sdmshop2.libs.shop.client.cache.ShopClientCache;
import dev.sixik.sdmshop2.libs.shop.client.ui.ShopScreenController;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.ShopScreenElement;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.ShopUiContext;
import dev.sixik.sdmshop2.libs.shop.client.ui.editor.ShopEditorScreen;
import dev.sixik.sdmshop2.libs.shop.client.ui.editor.ShopEditorState;
import dev.sixik.sdmshop2.libs.shop.client.ui.editor.ShopExistingSessionModal;
import dev.sixik.sdmshop2.libs.shop.client.ui.theme.ShopStyles;
import dev.sixik.sdmshop2.libs.shop.client.ui.theme.ShopTheme;
import dev.sixik.sdmshop2.utils.ShopUtils;
import dev.sixik.unigui.api.layout.Align;
import dev.sixik.unigui.api.layout.FlexDirection;
import dev.sixik.unigui.api.layout.LayoutStyle;
import dev.sixik.unigui.api.widget.Widget;
import dev.sixik.unigui.widgets.containers.HBox;
import dev.sixik.unigui.widgets.containers.PanelWidget;
import dev.sixik.unigui.widgets.containers.WrapPanel;
import dev.sixik.unigui.widgets.interaction.Button;
import dev.sixik.unigui.widgets.interaction.TextField;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

import org.jetbrains.annotations.Nullable;

public class ShopToolPanel implements ShopScreenElement {
    /** Actions and overlays of the owning window; null uses the compatibility facade. */
    private final ShopUiContext context;

    /** Creates a standalone panel using the default window actions. */
    public ShopToolPanel() {
        this(null);
    }

    /**
     * Creates a panel for a specific window.
     *
     * @param context owning window, or null for legacy integration.
     */
    public ShopToolPanel(ShopUiContext context) {
        this.context = context;
    }

    /**
     * @return the owning window's actions.
     */
    private ShopUiContext context() {
        return context != null ? context : ShopScreenController.context();
    }

    private Button editBtn;
    private Button favBtn;
    private TextField searchInput;
    private boolean favoritesActive = false;

    @Override
    public Widget initWidget(PanelWidget root) {
        this.favoritesActive = false;
        HBox toolbar = new HBox();
        toolbar.layout(
                style ->
                        style.flexDirection(FlexDirection.ROW)
                                .alignItems(Align.CENTER)
                                .padding(4.0f, 6.0f)
                                .gap(6.0f)
                                .height(30.0f)
                                .flexNone());

        toolbar.themeEnabled(false);

        toolbar.boxVisualEnabled(true);
        toolbar.backgroundVisible(true);
        toolbar.background(ShopTheme.color(ShopTheme.BG_SURFACE));
        toolbar.borderVisible(true);
        toolbar.border(ShopTheme.color(ShopTheme.BORDER_SUBTLE));
        toolbar.borderWidth(ShopTheme.BORDER_WIDTH_THIN);
        toolbar.radius(ShopTheme.RADIUS_PANEL);

        Minecraft mc = Minecraft.getInstance();
        Player player = mc != null ? mc.player : null;
        boolean isAdmin = ShopUtils.isPlayerAdmin(player);

        if (isAdmin) {
            editBtn = createToolButton("Edit");
            editBtn.onClick(event -> handleEditToggle());
            toolbar.addChild(editBtn);
        } else {
            editBtn = null;
        }

        WrapPanel spacer = new WrapPanel();
        spacer.layout(LayoutStyle::expand);
        WrapPanel spacer2 = new WrapPanel();
        spacer2.layout(LayoutStyle::expand);

        HBox searchGroup = new HBox();
        searchGroup.layout(
                style ->
                        style.flexDirection(FlexDirection.ROW)
                                .alignItems(Align.CENTER)
                                .gap(4.0f)
                                .flexNone());

        searchInput = new TextField();
        searchInput.placeholder("Search offers...");
        searchInput.themeEnabled(false);
        searchInput.boxVisualEnabled(true);
        searchInput.backgroundVisible(true);
        searchInput.background(ShopTheme.color(ShopTheme.BG_INPUT));
        searchInput.borderVisible(true);
        searchInput.border(ShopTheme.color(ShopTheme.BORDER_SLOT));
        searchInput.borderWidth(ShopTheme.BORDER_WIDTH_THIN);
        searchInput.radius(ShopTheme.RADIUS_BUTTON);
        searchInput.textColor().setArgb(ShopTheme.TEXT_PRIMARY);
        searchInput.placeholderColor().setArgb(ShopTheme.TEXT_MUTED);
        searchInput.layout(style -> style.width(200.0f).height(20.0f).flexNone());
        searchInput.onTextChanged(
                event -> {
                    context().search(event.newText());
                });

        favBtn = createToolButton("★");
        favBtn.textColor(ShopTheme.color(ShopTheme.GOLD_PRIMARY));
        favBtn.layout(style -> style.fixed(20.0f, 20.0f).flexNone());
        favBtn.onClick(event -> context().toggleFavorites());

        searchGroup.addChild(searchInput);
        searchGroup.addChild(favBtn);

        toolbar.addChild(spacer);
        toolbar.addChild(searchGroup);
        toolbar.addChild(spacer2);

        root.addChild(toolbar);
        return toolbar;
    }

    public @Nullable Button getEditBtn() {
        return editBtn;
    }

    private void handleEditToggle() {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc != null ? mc.player : null;
        if (!ShopUtils.isPlayerAdmin(player)) {
            return;
        }
        PersistentEditSession saved = ShopClientCache.getEditSession();
        if (saved != null
                && saved.shopId() != null
                && SDMShopClient.Shop != null
                && !saved.shopId().equals(SDMShopClient.Shop.getId())) {
            context().openModal(new ShopExistingSessionModal(saved, SDMShopClient.Shop.getId()));
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
        ShopStyles.button(btn);
        btn.textColor(ShopTheme.color(ShopTheme.TEXT_SECONDARY));
        btn.textPadding(6.0f, 1.0f);
        btn.layout(style -> style.height(20.0f).flexNone());
        return btn;
    }

    @Override
    public int priority() {
        return -1;
    }
}
