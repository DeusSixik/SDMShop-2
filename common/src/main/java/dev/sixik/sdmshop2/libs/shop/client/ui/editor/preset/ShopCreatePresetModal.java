package dev.sixik.sdmshop2.libs.shop.client.ui.editor.preset;

import dev.sixik.sdmshop2.libs.shop.base.ShopOffer;
import dev.sixik.sdmshop2.libs.shop.client.ui.ShopScreenController;
import dev.sixik.sdmshop2.libs.shop.client.ui.editor.ShopEditorState;
import dev.sixik.sdmshop2.libs.shop.client.ui.editor.unity.UnityIcons;
import dev.sixik.sdmshop2.libs.shop.client.ui.editor.unity.UnityStyles;
import dev.sixik.sdmshop2.libs.shop.client.ui.root.ShopCatalogElement;
import dev.sixik.sdmshop2.libs.shop.client.ui.editor.ShopToastHelper;
import dev.sixik.unigui.api.layout.Align;
import dev.sixik.unigui.api.layout.Justify;
import dev.sixik.unigui.api.layout.LayoutContext;
import dev.sixik.unigui.api.layout.PositionType;
import dev.sixik.unigui.api.math.MutableColor;
import dev.sixik.unigui.api.math.MutableRect;
import dev.sixik.unigui.api.math.RectView;
import dev.sixik.unigui.widgets.containers.Box;
import dev.sixik.unigui.widgets.containers.HBox;
import dev.sixik.unigui.widgets.containers.ScrollView;
import dev.sixik.unigui.widgets.containers.VBox;
import dev.sixik.unigui.widgets.display.Label;
import dev.sixik.unigui.widgets.feedback.OverlayHostAware;
import dev.sixik.unigui.widgets.feedback.OverlayLayer;
import dev.sixik.unigui.widgets.interaction.Button;
import dev.sixik.unigui.widgets.interaction.TextField;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Модальное окно управления и создания пресетов товаров (Offer Presets).
 */
public class ShopCreatePresetModal extends Box implements OverlayHostAware {

    private static final float MODAL_WIDTH = 420.0f;
    private static final float MODAL_HEIGHT = 380.0f;

    private final ShopOffer sourceOffer;
    private final VBox window = new VBox();
    private final VBox customPresetsBox = new VBox();
    private TextField nameField;
    private TextField descField;
    private Label presetsCountLabel;

    public ShopCreatePresetModal(@Nullable ShopOffer sourceOffer) {
        this.sourceOffer = sourceOffer;

        themeEnabled(false);
        boxVisualEnabled(true);
        backgroundVisible(true);
        background(new MutableColor(0.0f, 0.0f, 0.0f, 0.70f));

        layout(style -> style
                .position(PositionType.ABSOLUTE)
                .inset(0.0f)
                .fill()
        );

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
        window.spacing(8.0f);
        window.layout(style -> style
                .fixed(MODAL_WIDTH, MODAL_HEIGHT)
                .padding(12.0f)
                .alignItems(Align.STRETCH)
                .justifyContent(Justify.START)
                .flexNone()
        );

        window.themeEnabled(false);
        window.boxVisualEnabled(true);
        window.backgroundVisible(true);
        window.background().set(UnityStyles.BG_WINDOW);
        window.borderVisible(true);
        window.border(UnityStyles.BORDER_DEFAULT);
        window.borderWidth(1.0f);
        window.radius(4.0f);

        // 1. Заголовок
        HBox header = new HBox();
        header.layout(style -> style
                .widthPercent(100.0f)
                .height(20.0f)
                .alignItems(Align.CENTER)
                .justifyContent(Justify.SPACE_BETWEEN)
                .flexNone()
        );

        Label title = new Label("Offer Presets");
        title.color().set(UnityStyles.TEXT_BOLD);

        Button closeBtn = UnityIcons.createCloseButton(this::closeSelf);

        header.addChild(title);
        header.addChild(closeBtn);
        window.addChild(header);
        window.addChild(UnityStyles.separatorLine());

        // 2. Секция создания нового пресета
        Label createSectionTitle = new Label("SAVE OFFER PRESET");
        createSectionTitle.color().set(UnityStyles.TEXT_MUTED);
        window.addChild(createSectionTitle);

        // Источник пресета
        HBox sourceRow = new HBox();
        sourceRow.spacing(6.0f);
        sourceRow.layout(style -> style.widthPercent(100.0f).height(18.0f).alignItems(Align.CENTER).flexNone());

        Label sourceLabel = UnityStyles.labelMuted("Source:", 60.0f);
        String sourceTitleStr = sourceOffer != null
                ? ShopCatalogElement.resolveTitle(sourceOffer)
                : "Default Diamond Template";
        Label sourceValLabel = new Label(sourceTitleStr);
        sourceValLabel.color().set(UnityStyles.SELECTION_BLUE);
        sourceRow.addChild(sourceLabel);
        sourceRow.addChild(sourceValLabel);
        window.addChild(sourceRow);

        // Имя
        HBox nameRow = new HBox();
        nameRow.spacing(6.0f);
        nameRow.layout(style -> style.widthPercent(100.0f).height(20.0f).alignItems(Align.CENTER).flexNone());

        Label nameLabel = UnityStyles.label("Name:");
        nameLabel.layout(style -> style.width(60.0f));

        nameField = new TextField();
        UnityStyles.styleTextField(nameField);
        nameField.placeholder("Enter preset name...");
        nameField.text(sourceOffer != null ? ShopCatalogElement.resolveTitle(sourceOffer) : "Custom Preset");
        nameField.layout(style -> style.height(20.0f).flexGrow(1.0f));

        nameRow.addChild(nameLabel);
        nameRow.addChild(nameField);
        window.addChild(nameRow);

        // Описание
        HBox descRow = new HBox();
        descRow.spacing(6.0f);
        descRow.layout(style -> style.widthPercent(100.0f).height(20.0f).alignItems(Align.CENTER).flexNone());

        Label descLabel = UnityStyles.label("Description:");
        descLabel.layout(style -> style.width(60.0f));

        descField = new TextField();
        UnityStyles.styleTextField(descField);
        descField.placeholder("Optional preset description...");
        descField.layout(style -> style.height(20.0f).flexGrow(1.0f));

        descRow.addChild(descLabel);
        descRow.addChild(descField);
        window.addChild(descRow);

        // Кнопка сохранения пресета
        HBox saveRow = new HBox();
        saveRow.layout(style -> style.widthPercent(100.0f).height(22.0f).justifyContent(Justify.END).flexNone());

        Button saveBtn = new Button("Save Preset");
        UnityStyles.styleButton(saveBtn);
        saveBtn.textColor().set(UnityStyles.TEXT_PRIMARY);
        saveBtn.layout(style -> style.fixed(95.0f, 20.0f));
        saveBtn.onClick(e -> handleSavePreset());
        saveRow.addChild(saveBtn);
        window.addChild(saveRow);

        window.addChild(UnityStyles.separatorLine());

        // 3. Секция существующих кастомных пресетов
        HBox listHeader = new HBox();
        listHeader.layout(style -> style.widthPercent(100.0f).height(18.0f).alignItems(Align.CENTER).flexNone());

        presetsCountLabel = new Label("SAVED CUSTOM PRESETS");
        presetsCountLabel.color().set(UnityStyles.TEXT_MUTED);
        listHeader.addChild(presetsCountLabel);
        window.addChild(listHeader);

        customPresetsBox.spacing(4.0f);
        customPresetsBox.layout(style -> style.widthPercent(100.0f).alignItems(Align.STRETCH));

        ScrollView presetsScroll = new ScrollView(customPresetsBox);
        presetsScroll.scrollStep(20.0f);
        presetsScroll.scrollbarGap(2.0f);
        presetsScroll.scrollbarTrackColor().set(UnityStyles.SCROLLBAR_TRACK);
        presetsScroll.scrollbarThumbColor().set(UnityStyles.SCROLLBAR_THUMB);
        presetsScroll.layout(style -> style.widthPercent(100.0f).height(120.0f).flexGrow(1.0f).flexShrink(1.0f));
        window.addChild(presetsScroll);

        window.addChild(UnityStyles.separatorLine());

        // 4. Нижняя панель с кнопкой закрытия
        HBox footer = new HBox();
        footer.layout(style -> style.widthPercent(100.0f).height(22.0f).justifyContent(Justify.END).flexNone());

        Button footerCloseBtn = new Button("Close");
        UnityStyles.styleButton(footerCloseBtn);
        footerCloseBtn.layout(style -> style.fixed(75.0f, 20.0f));
        footerCloseBtn.onClick(e -> closeSelf());
        footer.addChild(footerCloseBtn);
        window.addChild(footer);

        addChild(window);

        refreshCustomPresetsList();
    }

    private void handleSavePreset() {
        String name = nameField != null && nameField.text() != null ? nameField.text().trim() : "";
        if (name.isEmpty()) {
            name = "Custom Preset";
        }
        String desc = descField != null && descField.text() != null ? descField.text().trim() : "";

        ShopOfferPreset saved = ShopOfferPresetManager.saveOfferAsPreset(name, desc, sourceOffer);
        ShopToastHelper.success(Component.literal("Preset saved: " + saved.name()));

        if (descField != null) {
            descField.text("");
        }
        refreshCustomPresetsList();
    }

    private void refreshCustomPresetsList() {
        customPresetsBox.clearChildren();
        List<ShopOfferPreset> customPresets = ShopOfferPresetManager.getCustomPresets();

        if (presetsCountLabel != null) {
            presetsCountLabel.text("SAVED CUSTOM PRESETS (" + customPresets.size() + ")");
        }

        if (customPresets.isEmpty()) {
            Label emptyLbl = new Label("No custom presets created yet.");
            emptyLbl.color().set(UnityStyles.TEXT_MUTED);
            emptyLbl.layout(style -> style.margin(4.0f, 0.0f));
            customPresetsBox.addChild(emptyLbl);
            return;
        }

        for (ShopOfferPreset preset : customPresets) {
            HBox card = new HBox();
            card.spacing(6.0f);
            card.layout(style -> style
                    .widthPercent(100.0f)
                    .height(30.0f)
                    .padding(2.0f, 6.0f)
                    .alignItems(Align.CENTER)
                    .justifyContent(Justify.SPACE_BETWEEN)
                    .flexNone()
            );
            card.themeEnabled(false);
            card.boxVisualEnabled(true);
            card.backgroundVisible(true);
            card.background().set(UnityStyles.BG_INPUT);
            card.borderVisible(true);
            card.border(UnityStyles.BORDER_DEFAULT);
            card.borderWidth(1.0f);
            card.radius(2.0f);

            VBox infoBox = new VBox();
            infoBox.spacing(1.0f);
            infoBox.layout(style -> style.flexGrow(1.0f).justifyContent(Justify.CENTER));

            Label pName = new Label(preset.name());
            pName.color().set(UnityStyles.TEXT_PRIMARY);

            infoBox.addChild(pName);
            if (!preset.description().isBlank()) {
                Label pDesc = new Label(preset.description());
                pDesc.color().set(UnityStyles.TEXT_MUTED);
                infoBox.addChild(pDesc);
            }

            HBox actionsBox = new HBox();
            actionsBox.spacing(4.0f);
            actionsBox.layout(style -> style.alignItems(Align.CENTER).flexNone());

            Button useBtn = new Button("Use");
            UnityStyles.styleButton(useBtn);
            useBtn.layout(style -> style.fixed(42.0f, 18.0f));
            useBtn.onClick(e -> {
                ShopEditorState.get().createOfferFromPreset(preset, false);
                closeSelf();
            });

            Button delBtn = new Button("Delete");
            UnityStyles.styleButton(delBtn);
            delBtn.textColor().set(UnityStyles.AXIS_X);
            delBtn.layout(style -> style.fixed(48.0f, 18.0f));
            delBtn.onClick(e -> {
                ShopOfferPresetManager.deletePreset(preset.id());
                ShopToastHelper.info(Component.literal("Deleted preset: " + preset.name()));
                refreshCustomPresetsList();
            });

            actionsBox.addChild(useBtn);
            actionsBox.addChild(delBtn);

            card.addChild(infoBox);
            card.addChild(actionsBox);
            customPresetsBox.addChild(card);
        }
    }

    private void closeSelf() {
        if (parent() instanceof OverlayLayer ol) {
            ol.removeOverlay(this);
        } else {
            ShopScreenController.closeModal(this);
        }
    }
}
