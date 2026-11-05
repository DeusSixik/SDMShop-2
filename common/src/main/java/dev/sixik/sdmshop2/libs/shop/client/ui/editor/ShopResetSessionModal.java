package dev.sixik.sdmshop2.libs.shop.client.ui.editor;

import dev.sixik.sdmshop2.libs.shop.client.ui.ShopScreenController;
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
import net.minecraft.client.resources.language.I18n;

/** Модальное окно подтверждения сброса несохраненной сессии редактора. */
public class ShopResetSessionModal extends Box implements OverlayHostAware {

    private static final float MODAL_WIDTH = 380.0f;
    private static final float MODAL_HEIGHT = 180.0f;

    private final VBox window = new VBox();

    public ShopResetSessionModal() {
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
        window.spacing(12.0f);
        window.layout(
                style ->
                        style.fixed(MODAL_WIDTH, MODAL_HEIGHT)
                                .padding(14.0f)
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

        // Заголовок
        Label title = new Label(I18n.get("shop.ui.tool_panel.reset_session.title"));
        title.color().set(ShopEditorStyles.TEXT_BOLD);
        title.layout(style -> style.alignSelf(Align.CENTER));
        window.addChild(title);

        // Текст предупреждения
        int changeCount = ShopEditorState.get().getChangeCount();
        VBox messageBox = new VBox();
        messageBox.spacing(4.0f);
        messageBox.layout(style -> style.alignItems(Align.CENTER));

        Label line1 = new Label(I18n.get("shop.ui.tool_panel.reset_session.line1", String.valueOf(changeCount)));
        line1.color().set(ShopEditorStyles.TEXT_PRIMARY);
        Label line2 = new Label(I18n.get("shop.ui.tool_panel.reset_session.line2"));
        line2.color().set(ShopEditorStyles.TEXT_LABEL);
        Label line3 = new Label(I18n.get("shop.ui.tool_panel.reset_session.line3"));
        line3.color().set(ShopEditorStyles.TEXT_MUTED);

        messageBox.addChild(line1);
        messageBox.addChild(line2);
        messageBox.addChild(line3);
        window.addChild(messageBox);

        // Кнопки
        HBox buttonRow = new HBox();
        buttonRow.spacing(12.0f);
        buttonRow.layout(
                style ->
                        style.widthPercent(100.0f)
                                .justifyContent(Justify.CENTER)
                                .alignItems(Align.CENTER)
                                .flexNone());

        Button cancelBtn = new Button(I18n.get("shop.ui.common.cancel"));
        ShopEditorStyles.styleButton(cancelBtn);
        cancelBtn.layout(style -> style.fixed(110.0f, 24.0f));
        cancelBtn.onClick(event -> closeSelf());

        Button resetBtn = new Button(I18n.get("shop.ui.tool_panel.button.reset_session"));
        ShopEditorStyles.styleButton(resetBtn);
        resetBtn.textColor().set(ShopEditorStyles.AXIS_X);
        resetBtn.layout(style -> style.fixed(110.0f, 24.0f));
        resetBtn.onClick(
                event -> {
                    ShopEditorState.get().resetSession();
                    closeSelf();
                });

        buttonRow.addChild(cancelBtn);
        buttonRow.addChild(resetBtn);
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
