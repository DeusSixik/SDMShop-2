package dev.sixik.sdmshop2.libs.shop.client.ui.editor;

import dev.sixik.sdmshop2.libs.shop.client.cache.PersistentEditSession;
import dev.sixik.sdmshop2.libs.shop.client.ui.ShopScreenController;
import dev.sixik.sdmshop2.libs.shop.client.ui.editor.unity.UnityIcons;
import dev.sixik.sdmshop2.libs.shop.client.ui.theme.ShopEditorStyles;
import dev.sixik.unigui.api.layout.Align;
import dev.sixik.unigui.api.layout.Justify;
import dev.sixik.unigui.api.layout.LayoutContext;
import dev.sixik.unigui.api.layout.PositionType;
import dev.sixik.unigui.api.math.MutableColor;
import dev.sixik.unigui.api.math.MutableRect;
import dev.sixik.unigui.api.math.RectView;
import dev.sixik.unigui.widgets.containers.Box;
import dev.sixik.unigui.widgets.containers.HBox;
import dev.sixik.unigui.widgets.containers.VBox;
import dev.sixik.unigui.widgets.display.Label;
import dev.sixik.unigui.widgets.feedback.OverlayHostAware;
import dev.sixik.unigui.widgets.feedback.OverlayLayer;
import dev.sixik.unigui.widgets.interaction.Button;

import net.minecraft.resources.ResourceLocation;

/**
 * Модальное окно разрешения конфликта сессий. Отображается, если в локальном кэше сохранена сессия
 * для другого магазина.
 */
public class ShopExistingSessionModal extends Box implements OverlayHostAware {

    private static final float MODAL_WIDTH = 420.0f;
    private static final float MODAL_HEIGHT = 190.0f;

    private final PersistentEditSession savedSession;
    private final ResourceLocation currentShopId;
    private final VBox window = new VBox();

    public ShopExistingSessionModal(
            PersistentEditSession savedSession, ResourceLocation currentShopId) {
        this.savedSession = savedSession;
        this.currentShopId = currentShopId;

        themeEnabled(false);
        boxVisualEnabled(true);
        backgroundVisible(true);
        background(new MutableColor(0.0f, 0.0f, 0.0f, 0.70f));

        layout(style -> style.position(PositionType.ABSOLUTE).inset(0.0f).fill());

        buildWindow();
    }

    @Override
    public void measure(LayoutContext context) {
        super.measure(context);
        window.measure(new LayoutContext(MODAL_WIDTH, MODAL_HEIGHT));
    }

    @Override
    public void arrangeInHost(RectView hostBounds) {
        this.arrange(hostBounds);

        float hostW = hostBounds.width();
        float hostH = hostBounds.height();
        float modalX = hostBounds.x() + (hostW - MODAL_WIDTH) * 0.5f;
        float modalY = hostBounds.y() + (hostH - MODAL_HEIGHT) * 0.5f;

        window.layout(style -> style.fixed(MODAL_WIDTH, MODAL_HEIGHT));
        window.arrange(new MutableRect(modalX, modalY, MODAL_WIDTH, MODAL_HEIGHT));
    }

    private void buildWindow() {
        window.spacing(14.0f);
        window.layout(
                style ->
                        style.fixed(MODAL_WIDTH, MODAL_HEIGHT)
                                .padding(12.0f)
                                .alignItems(Align.CENTER)
                                .justifyContent(Justify.SPACE_BETWEEN)
                                .flexNone());

        window.themeEnabled(false);
        window.boxVisualEnabled(true);
        window.backgroundVisible(true);
        window.background().set(ShopEditorStyles.BG_WINDOW);
        window.borderVisible(true);
        window.border(ShopEditorStyles.BORDER_DEFAULT);
        window.borderWidth(1.0f);
        window.radius(4.0f);

        // 1. Шапка: Заголовок + ✕
        HBox header = new HBox();
        header.layout(
                style ->
                        style.widthPercent(100.0f)
                                .height(20.0f)
                                .alignItems(Align.CENTER)
                                .justifyContent(Justify.SPACE_BETWEEN)
                                .flexNone());

        Label title = new Label("Existing Edit Session");
        title.color().set(ShopEditorStyles.TEXT_BOLD);

        Button closeBtn = UnityIcons.createCloseButton(this::closeSelf);

        header.addChild(title);
        header.addChild(closeBtn);
        window.addChild(header);

        // 2. Тело сообщения
        VBox messageBox = new VBox();
        messageBox.spacing(8.0f);
        messageBox.layout(style -> style.alignItems(Align.CENTER));

        Label warning = new Label("A saved edit session exists for another shop.");
        warning.color().set(ShopEditorStyles.AUDIO_GOLD);

        String savedStr =
                savedSession != null && savedSession.shopId() != null
                        ? savedSession.shopId().toString()
                        : "unknown";
        String currStr = currentShopId != null ? currentShopId.toString() : "unknown";
        Label details = new Label("Saved: " + savedStr + " | Current: " + currStr);
        details.color().set(ShopEditorStyles.TEXT_LABEL);

        messageBox.addChild(warning);
        messageBox.addChild(details);
        window.addChild(messageBox);

        // 3. Кнопки (Clear Old / Continue Old)
        HBox buttonRow = new HBox();
        buttonRow.spacing(14.0f);
        buttonRow.layout(
                style ->
                        style.widthPercent(100.0f)
                                .justifyContent(Justify.CENTER)
                                .alignItems(Align.CENTER)
                                .flexNone());

        Button clearOldBtn = new Button("Clear Old");
        ShopEditorStyles.styleButton(clearOldBtn);
        clearOldBtn.layout(style -> style.fixed(120.0f, 24.0f));
        clearOldBtn.onClick(
                event -> {
                    ShopEditorState.get().startEditor(true);
                    closeSelf();
                    ShopEditorScreen.open();
                });

        Button continueOldBtn = new Button("Continue Old");
        ShopEditorStyles.styleButton(continueOldBtn);
        continueOldBtn.layout(style -> style.fixed(120.0f, 24.0f));
        continueOldBtn.onClick(
                event -> {
                    ShopEditorState.get().startEditor(false);
                    closeSelf();
                    ShopEditorScreen.open();
                });

        buttonRow.addChild(clearOldBtn);
        buttonRow.addChild(continueOldBtn);
        window.addChild(buttonRow);

        addChild(window);
    }

    private void closeSelf() {
        if (parent() instanceof OverlayLayer ol) {
            ol.removeOverlay(this);
        } else {
            ShopScreenController.closeModal(this);
        }
    }
}
