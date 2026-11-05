package dev.sixik.sdmshop2.libs.shop.client.ui.theme;


import dev.sixik.unigui.api.layout.EdgeInsets;
import dev.sixik.unigui.api.layout.LayoutConstraints;
import dev.sixik.unigui.api.style.StyleKeys;
import dev.sixik.unigui.api.style.TreeDisclosureStyle;
import dev.sixik.unigui.api.style.TreeRowStyle;
import dev.sixik.unigui.api.widget.render.WidgetRender;
import dev.sixik.unigui.widgets.containers.Box;
import dev.sixik.unigui.widgets.display.Label;
import dev.sixik.unigui.widgets.display.Separator;
import dev.sixik.unigui.widgets.feedback.ContextMenu;
import dev.sixik.unigui.widgets.feedback.Tooltip;
import dev.sixik.unigui.widgets.interaction.Button;
import dev.sixik.unigui.widgets.interaction.Checkbox;
import dev.sixik.unigui.widgets.interaction.ComboBox;
import dev.sixik.unigui.widgets.interaction.NumberField;
import dev.sixik.unigui.widgets.interaction.TextField;
import dev.sixik.unigui.widgets.interaction.TextInput;
import dev.sixik.unigui.widgets.interaction.ToggleButton;
import dev.sixik.unigui.widgets.navigation.TreeView;

/** Цветовая палитра и стили в духе Unity Dark Theme (Unity 2020 / 2022+). */
public class ShopEditorStyles extends ShopEditorTheme {
    protected ShopEditorStyles() {}

    // --- Фоны и панели ---

    // --- Границы и разделители ---

    // --- Цвета текста ---

    // --- Оси координат Unity ---

    // --- Акценты и иконки ---

    // --- Выпадающие списки (Dropdown / ComboBox) в палитре Unity Dark ---

    // --- Размеры ---

    /**
     * Кастомный рендерер Checkbox в стиле Unity: Тёмный квадрат с тонкой рамкой (белая при hover,
     * синяя при press) и изящной белой галочкой (✓) правильных пропорций.
     */
    public static final WidgetRender CHECKBOX_RENDERER = ShopEditorRenderers.CHECKBOX_RENDERER;

    /**
     * Рендерер кнопки выбора целевого объекта (Target Texture: ⊙) с центрированной векторной
     * иконкой мишени/круга и подсветкой при наведении.
     */
    public static final WidgetRender TARGET_PICKER_RENDERER =
            ShopEditorRenderers.TARGET_PICKER_RENDERER;

    /** Рендерер кнопки пипетки выбора цвета с центрированной векторной пипеткой. */
    public static final WidgetRender EYEDROPPER_RENDERER = ShopEditorRenderers.EYEDROPPER_RENDERER;

    /**
     * Рендерер опций выпадающего списка (ComboBox popup items) в палитре Unity: - Покой:
     * нейтральный тёмно-серый фон (#262626), текст TEXT_PRIMARY (#DCDCDC) - Выбранный пункт:
     * глубокий синий фон (#244462), текст TEXT_BOLD (#FFFFFF) - Наведение (Hover): фирменный Unity
     * Selection Blue (#2C5D88), рамка фокуса (#488EEE), текст TEXT_BOLD (#FFFFFF) - Нажатие
     * (Press): глубокий синий (#204870)
     */
    public static final WidgetRender DROPDOWN_OPTION_RENDERER =
            ShopEditorRenderers.DROPDOWN_OPTION_RENDERER;

    public static void styleCheckbox(Checkbox checkbox) {
        checkbox.themeEnabled(false);
        checkbox.boxVisualEnabled(false);
        checkbox.focusRingVisible(false);
        checkbox.focusable(false);
        checkbox.renderer(CHECKBOX_RENDERER);
        checkbox.boxSize(12.0f);
        checkbox.checkSize(8.0f);
        checkbox.textGap(4.0f);
    }

    public static void styleNumberField(NumberField field) {
        field.themeEnabled(false);
        field.backgroundVisible(true);
        field.background().set(BG_INPUT);
        field.borderVisible(true);
        field.borderColor().set(BORDER_INPUT);
        field.borderWidth(1.0f);
        field.radius(2.0f);
        field.textColor().set(TEXT_PRIMARY);
        field.textPaddingX(4.0f);
        field.layout(
                style -> style.height(ROW_HEIGHT).flexGrow(1.0f).flexShrink(1.0f).minWidth(0.0f));
    }

    public static void styleTextInput(TextInput input) {
        input.themeEnabled(false);
        input.backgroundVisible(true);
        input.background().set(BG_INPUT);
        input.borderVisible(true);
        input.borderColor().set(BORDER_INPUT);
        input.borderWidth(1.0f);
        input.radius(2.0f);
        input.textColor().set(TEXT_PRIMARY);
        input.textPaddingX(4.0f);
        input.layout(
                style -> style.height(ROW_HEIGHT).flexGrow(1.0f).flexShrink(1.0f).minWidth(0.0f));
    }

    public static void styleComboBox(ComboBox combo) {
        combo.optionRowHeight(18.0f);
        combo.optionsHost().themeEnabled(false);
        combo.optionsHost().backgroundVisible(true);
        combo.optionsHost().background().set(BG_DROPDOWN_HOST);
        combo.optionsHost().borderVisible(true);
        combo.optionsHost().borderColor().set(BORDER_DROPDOWN_HOST);
        combo.optionsHost().borderWidth(1.0f);
        combo.optionsHost().radius(2.0f);

        combo.optionsScroll().scrollbarGap(1.0f);
        combo.optionsScroll().scrollStep(18.0f);
        combo.optionsScroll().scrollbarTrackColor().set(SCROLLBAR_TRACK);
        combo.optionsScroll().scrollbarThumbColor().set(SCROLLBAR_THUMB);

        Button header = combo.headerButton();
        header.themeEnabled(false);
        header.backgroundVisible(true);
        header.background().set(BG_INPUT);
        header.borderVisible(true);
        header.borderColor().set(BORDER_INPUT);
        header.radius(2.0f);
        header.textPaddingX(6.0f);
        header.textColor().set(TEXT_PRIMARY);
        header.layout(style -> style.widthPercent(100.0f).height(ROW_HEIGHT));

        combo.layout(
                style -> style.height(ROW_HEIGHT).flexGrow(1.0f).flexShrink(1.0f).minWidth(0.0f));

        for (int i = 0; i < combo.itemCount(); i++) {
            ToggleButton option = combo.optionButton(i);
            styleComboBoxOption(option);
        }
    }

    public static void styleComboBoxOption(ToggleButton option) {
        option.themeEnabled(false);
        option.backgroundVisible(false);
        option.borderVisible(false);
        option.focusRingVisible(false);
        option.renderer(DROPDOWN_OPTION_RENDERER);
        option.layout(
                style ->
                        style.size(LayoutConstraints.AUTO, 18.0f)
                                .margin(0.0f, 0.0f)
                                .flexGrow(0.0f)
                                .flexShrink(0.0f));
    }

    public static void styleButton(Button button) {
        button.themeEnabled(false);
        button.backgroundVisible(true);
        button.background().set(BG_BUTTON);
        button.hoverBackground().set(BG_BUTTON_HOVER);
        button.pressedBackground().set(BG_BUTTON_ACTIVE);
        button.borderVisible(true);
        button.borderColor().set(BORDER_BUTTON);
        button.radius(3.0f);
        button.textPaddingX(8.0f);
        button.textColor().set(TEXT_BOLD);
    }

    public static void styleTextField(TextField textField) {
        if (textField == null) return;
        textField.themeEnabled(false);
        textField.boxVisualEnabled(true);
        textField.backgroundVisible(true);
        textField.background().set(BG_INPUT);
        textField.borderVisible(true);
        textField.borderColor().set(BORDER_INPUT);
        textField.borderWidth(1.0f);
        textField.radius(2.0f);
        textField.textColor().set(TEXT_PRIMARY);
        textField.placeholderColor().set(TEXT_MUTED);
    }

    public static Box separatorLine() {
        Box line = new Box();
        line.themeEnabled(false);
        line.backgroundVisible(true);
        line.background().set(SEPARATOR);
        line.borderVisible(false);
        line.layout(style -> style.widthPercent(100.0f).height(1.0f).margin(0.0f, 2.0f));
        return line;
    }

    public static Label label(String text) {
        Label lbl = new Label(text);
        lbl.color(TEXT_LABEL);
        lbl.layout(style -> style.width(LABEL_WIDTH).height(ROW_HEIGHT));
        return lbl;
    }

    public static Label labelMuted(String text, float width) {
        Label lbl = new Label(text);
        lbl.color(TEXT_MUTED);
        lbl.layout(style -> style.width(width).height(ROW_HEIGHT));
        return lbl;
    }

    /**
     * Кастомный рендерер элементов контекстного меню в стиле Unity: - при наведении синяя подсветка
     * SELECTION_BLUE и белый текст - при нажатии тёмно-синяя заливка BG_DROPDOWN_ITEM_PRESS - для
     * отключённых элементов серый цвет TEXT_MUTED и отсутствие hover-фона - ровное выравнивание по
     * левому краю с аккуратным отступом
     */
    public static final WidgetRender CONTEXT_MENU_ITEM_RENDERER =
            ShopEditorRenderers.CONTEXT_MENU_ITEM_RENDERER;

    /** Стилизация всплывающего контекстного меню под тёмную тему Unity. */
    /** Стилизация TreeView под компактный и строгий вид иерархии / проекта Unity. */
    public static void styleTreeView(TreeView tree) {
        if (tree == null) return;
        tree.style(
                style ->
                        style.put(StyleKeys.TREE_ROW_HEIGHT, ROW_HEIGHT)
                                .put(StyleKeys.TREE_ROW_STYLE, TreeRowStyle.FULL_WIDTH)
                                .put(StyleKeys.TREE_DISCLOSURE_STYLE, TreeDisclosureStyle.TRIANGLE)
                                .put(StyleKeys.TREE_ROW_RADIUS, 0.0f)
                                .put(StyleKeys.TREE_ROW_INSET_X, 0.0f)
                                .put(StyleKeys.TREE_ROW_INSET_Y, 0.0f)
                                .put(StyleKeys.TREE_INDENT_WIDTH, 14.0f)
                                .put(StyleKeys.TREE_SELECTED_COLOR, SELECTION_BLUE)
                                .put(StyleKeys.TREE_HOVER_COLOR, BG_HEADER_HOVER)
                                .put(StyleKeys.TREE_TEXT_COLOR, TEXT_PRIMARY)
                                .put(StyleKeys.TREE_SELECTED_TEXT_COLOR, TEXT_BOLD)
                                .put(StyleKeys.TREE_DISABLED_TEXT_COLOR, TEXT_MUTED)
                                .put(StyleKeys.TREE_DISCLOSURE_COLOR, TEXT_MUTED)
                                .put(StyleKeys.TREE_ICON_COLOR, TEXT_PRIMARY));
    }

    public static void styleContextMenu(ContextMenu menu) {
        if (menu == null) return;
        menu.themeEnabled(false);
        menu.backgroundVisible(true);
        menu.background().set(BG_DROPDOWN_HOST);
        menu.borderVisible(true);
        menu.borderColor().set(BORDER_DROPDOWN_HOST);
        menu.borderWidth(1.0f);
        menu.radius(3.0f);
        menu.padding(EdgeInsets.all(3.0f));
    }

    /** Добавление элемента контекстного меню с кастомным рендерером и размером. */
    public static class UnityContextMenuItem extends Button {
        private String shortcut;
        private Boolean checked;

        public UnityContextMenuItem(String text) {
            this(text, null);
        }

        public UnityContextMenuItem(String text, String shortcut) {
            super(text);
            this.shortcut = shortcut;
        }

        public String shortcut() {
            return shortcut;
        }

        public void shortcut(String shortcut) {
            this.shortcut = shortcut;
        }

        public boolean isCheckable() {
            return checked != null;
        }

        public boolean isChecked() {
            return checked != null && checked;
        }

        public UnityContextMenuItem checked(Boolean checked) {
            this.checked = checked;
            return this;
        }
    }

    public static Button addToggleContextMenuItem(
            ContextMenu menu,
            String text,
            boolean checked,
            boolean enabled,
            float menuWidth,
            Runnable action) {
        UnityContextMenuItem btn = new UnityContextMenuItem(text);
        btn.checked(checked);
        btn.themeEnabled(false);
        btn.enabled(enabled);
        btn.renderer(CONTEXT_MENU_ITEM_RENDERER);
        if (action != null) {
            btn.onClick(
                    e -> {
                        if (btn.enabled()) {
                            menu.close();
                            action.run();
                        }
                    });
        }
        menu.item(btn);
        btn.layout(style -> style.size(menuWidth, 19.0f).flexGrow(0.0f).flexShrink(0.0f));
        return btn;
    }

    public static Button addContextMenuItem(
            ContextMenu menu,
            String text,
            String shortcut,
            boolean enabled,
            float menuWidth,
            Runnable action) {
        UnityContextMenuItem btn = new UnityContextMenuItem(text, shortcut);
        btn.themeEnabled(false);
        btn.enabled(enabled);
        btn.renderer(CONTEXT_MENU_ITEM_RENDERER);
        if (action != null) {
            btn.onClick(
                    e -> {
                        if (btn.enabled()) {
                            menu.close();
                            action.run();
                        }
                    });
        }
        menu.item(btn);
        btn.layout(style -> style.size(menuWidth, 19.0f).flexGrow(0.0f).flexShrink(0.0f));
        return btn;
    }

    public static Button addContextMenuItem(
            ContextMenu menu, String text, boolean enabled, float menuWidth, Runnable action) {
        return addContextMenuItem(menu, text, null, enabled, menuWidth, action);
    }

    public static Button addContextMenuSubmenu(
            ContextMenu menu, String text, ContextMenu subMenu, float menuWidth) {
        UnityContextMenuItem btn = new UnityContextMenuItem(text, ">");
        btn.themeEnabled(false);
        btn.renderer(CONTEXT_MENU_ITEM_RENDERER);
        btn.layout(style -> style.size(menuWidth, 19.0f).flexGrow(0.0f).flexShrink(0.0f));
        menu.submenu(btn, subMenu);
        return btn;
    }

    /** Добавление тонкого разделителя в контекстное меню Unity. */
    public static Separator addContextMenuSeparator(ContextMenu menu, float menuWidth) {
        Separator sep = new Separator();
        sep.color().set(BORDER_SUBTLE);
        sep.thickness(1.0f);
        menu.separator(sep);
        sep.layout(
                style ->
                        style.size(menuWidth, 1.0f)
                                .margin(1.0f, 2.0f)
                                .flexGrow(0.0f)
                                .flexShrink(0.0f));
        return sep;
    }

    /** Стилизация подсказки Tooltip в стиле Unity. */
    public static void styleTooltip(Tooltip tooltip) {
        if (tooltip == null) return;
        tooltip.themeEnabled(false);
        tooltip.backgroundVisible(true);
        tooltip.background().set(BG_WORKSPACE);
        tooltip.borderVisible(true);
        tooltip.borderColor().set(BORDER_SUBTLE);
        tooltip.borderWidth(1.0f);
        tooltip.radius(3.0f);
        tooltip.textColor().set(TEXT_PRIMARY);
        tooltip.offset(4.0f, 6.0f);
    }

    /** Сегментная кнопка пути в строке хлебных крошек Unity. */
    public static class BreadcrumbSegmentButton extends Button {
        private boolean current;

        public BreadcrumbSegmentButton(String text, boolean current) {
            super(text);
            this.current = current;
            themeEnabled(false);
            textPadding(4.0f, 2.0f);
        }

        public boolean isCurrent() {
            return current;
        }

        public void setCurrent(boolean current) {
            this.current = current;
        }
    }

    /**
     * Рендерер сегментов пути (хлебных крошек) в стиле Unity: - текущая папка выделяется более
     * ярким шрифтом (TEXT_BOLD) - промежуточные папки отображаются цветом TEXT_PRIMARY - при
     * наведении или нажатии отображается аккуратный закруглённый hover-фон
     */
    public static final WidgetRender BREADCRUMB_SEGMENT_RENDERER =
            ShopEditorRenderers.BREADCRUMB_SEGMENT_RENDERER;

    /**
     * Рендерер кнопки-стрелочки шеврона (>) в хлебных крошках Unity: - процедурная стрелочка вправо
     * правильных пропорций Unity - при наведении подсвечивается ярким цветом и полупрозрачным фоном
     */
    public static final WidgetRender BREADCRUMB_CHEVRON_RENDERER =
            ShopEditorRenderers.BREADCRUMB_CHEVRON_RENDERER;

    /**
     * Элемент выпадающего меню хлебных крошек с отметкой активной папки (галочка в голубом
     * квадрате).
     */
    public static class BreadcrumbMenuItem extends Button {
        private boolean checked;

        public BreadcrumbMenuItem(String text, boolean checked) {
            super(text);
            this.checked = checked;
        }

        public boolean isChecked() {
            return checked;
        }

        public void setChecked(boolean checked) {
            this.checked = checked;
        }
    }

    /**
     * Рендерер элементов выпадающего меню хлебных крошек (Скриншот 2): - при наведении синяя
     * подсветка SELECTION_BLUE - слева слот для чекбокса - если элемент выбран (находится на
     * текущем пути), рисуется голубой квадратик (#90C8F6) с тёмной галочкой внутри (✓)
     */
    public static final WidgetRender BREADCRUMB_DROPDOWN_ITEM_RENDERER =
            ShopEditorRenderers.BREADCRUMB_DROPDOWN_ITEM_RENDERER;

    /** Добавление элемента в выпадающее меню хлебных крошек. */
    public static Button addBreadcrumbDropdownItem(
            ContextMenu menu,
            String folderName,
            boolean isChecked,
            float menuWidth,
            Runnable action) {
        BreadcrumbMenuItem btn = new BreadcrumbMenuItem(folderName, isChecked);
        btn.themeEnabled(false);
        btn.renderer(BREADCRUMB_DROPDOWN_ITEM_RENDERER);
        if (action != null) {
            btn.onClick(e -> action.run());
        }
        menu.item(btn);
        btn.layout(style -> style.size(menuWidth, 20.0f).flexGrow(0.0f).flexShrink(0.0f));
        return btn;
    }
}
