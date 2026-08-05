package dev.sixik.sdmshop2.libs.shop.client.config;

import com.lowdragmc.lowdraglib.gui.texture.ColorBorderTexture;
import com.lowdragmc.lowdraglib.gui.texture.TransformTexture;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.gui.widget.layout.Layout;
import com.lowdragmc.lowdraglib.utils.Size;
import dev.sixik.sdmshop2.libs.shop.client.WidgetGroupAccessor;
import dev.sixik.sdmshop2.libs.shop.client.config.constructors.ComponentConfigAccess;
import dev.sixik.sdmshop2.libs.shop.client.config.constructors.ComponentConfigWidgetConstructor;
import dev.sixik.sdmshop2.libs.shop.client.ui.textures.ColorRectAndBorderTexture;
import dev.sixik.sdmshop2.libs.shop.components.api.ShopComponent;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.TextLabel;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;

public class ComponentConfigurationWidget extends WidgetGroup {

    protected static final int DEFAULT_WIDTH = 60;
    protected static final int LABEL_X = 10;
    protected static final int TOP_PADDING = 10;
    protected static final int ROW_SPACING = 5;
    protected static final int EDITOR_WIDTH = 85;
    protected static final int RIGHT_PADDING = 10;
    protected static final int LABEL_EDITOR_GAP = 5;
    protected static final int DEFAULT_EDITOR_HEIGHT = 20;
    protected static final float MIN_LABEL_SCALE = 0.4f;

    public static final ColorRectAndBorderTexture texture = new ColorRectAndBorderTexture();
    public static final ColorBorderTexture hoverTexture = new ColorBorderTexture(1, -1);

    protected final List<UiPair> uiPairs = new ArrayList<>();

    @Getter
    @Nullable
    protected ShopComponent component;

    @Setter
    protected ComponentConfigWidgetConstructor.Style editorStyle = ComponentConfigWidgetConstructor.Style.defaults();

    @Setter
    protected BiConsumer<Integer, ComponentConfigWidgetConstructor> modifyConfigWidgetCallback = (index, widget) -> { };

    @Setter
    protected BiConsumer<Integer, TextLabel> modifyTextLabelCreateCallback = (index, widget) -> { };

    @Setter
    protected ModifyElements modifyInitElementsCallback = ((main, label, editor, font, editorWidth, editorX, currentY) -> {
        editor.setSizeWidth(editorWidth);
        editor.setSelfPosition(editorX, currentY);

        int maxLabelWidth = editorX - LABEL_X - LABEL_EDITOR_GAP;
        int originalTextWidth = font.width(label.getText());

        /*
            Считаем Scale
         */
        float scale = 1.0f;
        if (originalTextWidth > maxLabelWidth && originalTextWidth > 0) {
            /*
                Если текст шире, чем доступное место, считаем коэффициент сжатия.
             */
            scale = (float) maxLabelWidth / originalTextWidth;
            scale = Math.max(scale, MIN_LABEL_SCALE);
        }

        label.setScale(scale);
        label.setSize(maxLabelWidth, Math.max(1, Math.round(font.lineHeight * scale)));

        int editorHeight = editor.getSizeHeight();
        float visualTextHeight = font.lineHeight * scale;

        int labelY = currentY + (int)((editorHeight - visualTextHeight) / 2f);

        label.setSelfPosition(LABEL_X, labelY);

        /*
            Возвращаем шаг по Y для следующего элемента (высота виджета + отступ)
         */
        return editorHeight + ROW_SPACING;
    });

    @Getter @Setter
    protected int fixedWidth;

    public ComponentConfigurationWidget(@Nullable ShopComponent component) {
        this(DEFAULT_WIDTH, component);
    }

    public ComponentConfigurationWidget(int width, @Nullable ShopComponent component) {
        super(0, 0, width, 0);
        this.fixedWidth = width;
        this.component = component;
        this.setDynamicSized(true);
        this.setLayout(Layout.NONE);

        setBackground(getTexture());
    }

    @Override
    public void initWidget() {
        rebuildConfiguration();
        super.initWidget();
        repositionWidgets();
    }

    public void setComponent(ShopComponent component) {
        if(Objects.equals(component, this.component)) return;
        this.component = component;
        updateConfiguration();
    }

    public void updateConfiguration() {
        rebuildConfiguration();
        if (isInitialized()) {
            super.initWidget();
        }
        repositionWidgets();
    }

    protected void rebuildConfiguration() {
        clearAllWidgets();
        uiPairs.clear();
        if(component == null) return;

        ObjectArrayList<ComponentConfigAccess.CachedField> fieldsList =
                ComponentConfigAccess.getCachedFields(component.getClass());

        for (int i = 0; i < fieldsList.size(); i++) {
            final var datum = fieldsList.get(i);
            Widget editorWidget = ComponentConfigWidgetConstructor.createWidget(component, datum);
            if (editorWidget == null) continue;
            if (editorWidget instanceof ComponentConfigWidgetConstructor configWidget) {
                configWidget.setStyle(editorStyle);
                modifyConfigWidgetCallback.accept(i, configWidget);
            }

            editorWidget.setHoverTexture(getHoverTexture());

            if (!(editorWidget instanceof ComponentConfigWidgetConstructor)) {
                editorWidget.setSizeHeight(DEFAULT_EDITOR_HEIGHT);
            }

            TextLabel textLabel = new TextLabel(Component.translatable(datum.translationKey()))
                    .setAutoSize(false);
            modifyTextLabelCreateCallback.accept(i, textLabel);

            @Nullable String tooltip = datum.tooltipTranslationKey();
            if(tooltip != null && I18n.exists(tooltip)) {
                editorWidget.setHoverTooltips(tooltip);
                textLabel.setHoverTooltips(tooltip);
            }

            uiPairs.add(new UiPair(textLabel, editorWidget));
        }


        /*
            Самый верхний элемент интерфейса (index 0) будет добавлен последним,
            поэтому его DropDown перекроет все нижние виджеты и заберет клики.
         */
        for (int i = uiPairs.size() - 1; i >= 0; i--) {
            UiPair pair = uiPairs.get(i);
            this.addWidget(pair.label()); // Метка
            this.addWidget(pair.editor()); // Редактор
        }
    }

    /**
     * Тот самый метод, который двигает всё "в прямом эфире"
     */
    public void repositionWidgets() {
        int currentY = TOP_PADDING;
        int editorWidth = EDITOR_WIDTH;
        int paddingRight = RIGHT_PADDING;
        int editorX = getSizeWidth() - editorWidth - paddingRight;
        Font font = Minecraft.getInstance().font;

        for (UiPair pair : uiPairs) {
            currentY += modifyInitElementsCallback.accept(this, pair.label(), pair.editor(), font, editorWidth, editorX, currentY);
        }

        /*
            Заставляем саму группу пересчитать свою высоту
         */
        this.recomputeSize();
    }

    /**
     * Твой переопределенный метод из setSize теперь прилетит сюда
     */
    @Override
    public void onChildSizeUpdate(Widget child) {
        /*
            Пересчитываем координаты существующих виджетов
         */
        repositionWidgets();

        /*
             Пробрасываем уведомление дальше вверх к CollapsedGroupWidget
         */
        if (parent != null) {
            ((WidgetGroupAccessor)parent).sdm$onChildSizeUpdate(this);
        }
    }

    public TransformTexture getTexture() {
        return texture;
    }

    public TransformTexture getHoverTexture() {
        return hoverTexture;
    }

    //////////////////////////////////////////////////////
    ///             ИСПРАВЛЕНИЕ ШИРИНЫ                ///
    //////////////////////////////////////////////////////
    @Override
    public void setSize(Size size) {
        super.setSize(size);
        this.fixedWidth = size.width;
    }

    @Override
    public Size getSize() {
        return new Size(fixedWidth, super.getSize().height);
    }

    @Override
    protected Size computeDynamicSize() {
        Size wrappedSize = super.computeDynamicSize();
        return new Size(fixedWidth, wrappedSize.height);
    }

    public interface ModifyElements {

        int accept(ComponentConfigurationWidget main, TextLabel label, Widget editor, Font font, int editorWidth, int editorX, int currentY);
    }

    protected record UiPair(TextLabel label, Widget editor) {
    }
}
