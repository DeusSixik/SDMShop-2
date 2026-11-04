package dev.sixik.sdmshop2.libs.shop.client.ui.editor;

import dev.sixik.sdmshop2.libs.shop.client.ui.editor.unity.UnityStyles;
import dev.sixik.sdmshop2.libs.shop.client.ui.widgets.ShopItemWidget;
import dev.sixik.sdmshop2.libs.shop.components.api.annotation.ComponentNumberRange;
import dev.sixik.unigui.api.layout.Align;
import dev.sixik.unigui.api.widget.CheckboxState;
import dev.sixik.unigui.widgets.containers.HBox;
import dev.sixik.unigui.widgets.display.Label;
import dev.sixik.unigui.widgets.interaction.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;

/**
 * Фабрика стандартных строк свойств в инспекторе магазина в стиле Unity Inspector.
 * Поля ввода занимают всё доступное пространство до правого края Inspector и не выходят за его границы.
 */
public final class ShopPropertyRows {

    public static final float ROW_HEIGHT = UnityStyles.ROW_HEIGHT;
    public static final float LABEL_WIDTH = UnityStyles.LABEL_WIDTH;

    private ShopPropertyRows() {
    }

    public static Label label(String text) {
        Label label = new Label(text != null ? text : "");
        label.color(UnityStyles.TEXT_LABEL);
        label.layout(style -> style
                .width(LABEL_WIDTH)
                .height(ROW_HEIGHT)
                .flexNone()
        );
        return label;
    }

    public static void styleTextInput(TextInput input) {
        UnityStyles.styleTextInput(input);
    }

    public static void styleNumberField(NumberField field) {
        UnityStyles.styleNumberField(field);
    }

    public static void styleCheckbox(Checkbox checkbox) {
        UnityStyles.styleCheckbox(checkbox);
    }

    public static void styleComboBox(ComboBox combo) {
        UnityStyles.styleComboBox(combo);
    }

    /**
     * Строка ввода строки (String / ResourceLocation).
     */
    public static HBox stringRow(String labelText, String initialValue, Consumer<String> onChange) {
        HBox row = new HBox();
        row.spacing(6.0f);
        row.layout(style -> style
                .widthPercent(100.0f)
                .height(ROW_HEIGHT)
                .alignItems(Align.CENTER)
                .flexGrow(0.0f)
                .flexShrink(0.0f)
        );

        row.addChild(label(labelText));

        TextInput input = new TextInput(initialValue != null ? initialValue : "");
        styleTextInput(input);
        input.layout(style -> style.height(ROW_HEIGHT).flexGrow(1.0f).flexShrink(1.0f).minWidth(0.0f));
        if (onChange != null) {
            input.onTextChanged(event -> onChange.accept(event.newText()));
        }

        row.addChild(input);
        return row;
    }

    /**
     * Строка ввода числа (int, long, double, float).
     */
    public static HBox numberRow(
            String labelText,
            double initialValue,
            boolean isInteger,
            @Nullable ComponentNumberRange range,
            DoubleConsumer onChange
    ) {
        HBox row = new HBox();
        row.spacing(6.0f);
        row.layout(style -> style
                .widthPercent(100.0f)
                .height(ROW_HEIGHT)
                .alignItems(Align.CENTER)
                .flexGrow(0.0f)
                .flexShrink(0.0f)
        );

        row.addChild(label(labelText));

        NumberField field = new NumberField();
        styleNumberField(field);
        field.value(initialValue);
        if (range != null) {
            double min = isInteger ? range.intMin() : range.doubleMin();
            double max = isInteger ? range.intMax() : range.doubleMax();
            field.range(min, max);
        }
        field.step(isInteger ? 1.0 : 0.1);
        field.layout(style -> style.height(ROW_HEIGHT).flexGrow(1.0f).flexShrink(1.0f).minWidth(0.0f));

        if (onChange != null) {
            field.onValueChanged(event -> onChange.accept(event.newValue()));
        }

        row.addChild(field);
        return row;
    }

    /**
     * Строка переключателя Boolean (Checkbox).
     */
    public static HBox booleanRow(String labelText, boolean checked, Consumer<Boolean> onChange) {
        HBox row = new HBox();
        row.spacing(6.0f);
        row.layout(style -> style
                .widthPercent(100.0f)
                .height(ROW_HEIGHT)
                .alignItems(Align.CENTER)
                .flexGrow(0.0f)
                .flexShrink(0.0f)
        );

        row.addChild(label(labelText));

        Checkbox checkbox = new Checkbox();
        styleCheckbox(checkbox);
        checkbox.checked(checked);
        checkbox.layout(style -> style.size(14.0f, ROW_HEIGHT).alignSelf(Align.CENTER).flexNone());

        if (onChange != null) {
            checkbox.onStateChanged(event -> onChange.accept(event.newState() == CheckboxState.CHECKED));
        }

        row.addChild(checkbox);
        return row;
    }

    /**
     * Строка перечисления Enum (ComboBox).
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static HBox enumRow(String labelText, Enum<?> current, Class<?> enumClass, Consumer<Object> onChange) {
        HBox row = new HBox();
        row.spacing(6.0f);
        row.layout(style -> style
                .widthPercent(100.0f)
                .height(ROW_HEIGHT)
                .alignItems(Align.CENTER)
                .flexGrow(0.0f)
                .flexShrink(0.0f)
        );

        row.addChild(label(labelText));

        Object[] constants = enumClass.getEnumConstants();
        List<String> names = new ArrayList<>();
        int selectedIndex = 0;
        if (constants != null) {
            for (int i = 0; i < constants.length; i++) {
                Enum<?> c = (Enum<?>) constants[i];
                names.add(c.name());
                if (c == current || (current != null && c.name().equalsIgnoreCase(current.name()))) {
                    selectedIndex = i;
                }
            }
        }

        ComboBox combo = new ComboBox();
        combo.items(names);
        combo.selectedIndex(selectedIndex);
        styleComboBox(combo);
        combo.layout(style -> style.height(ROW_HEIGHT).flexGrow(1.0f).flexShrink(1.0f).minWidth(0.0f));

        if (onChange != null && constants != null) {
            combo.onSelectionChanged(event -> {
                int idx = combo.selectedIndex();
                if (idx >= 0 && idx < constants.length) {
                    onChange.accept(constants[idx]);
                }
            });
        }

        row.addChild(combo);
        return row;
    }

    /**
     * Строка выбора ItemStack: превью слота + текстовый ввод ID предмета.
     * Поле количества отключено по требованию пользователя.
     */
    public static HBox itemStackRow(String labelText, ItemStack stack, Consumer<ItemStack> onChange) {
        HBox row = new HBox();
        row.spacing(6.0f);
        row.layout(style -> style
                .widthPercent(100.0f)
                .height(24.0f)
                .alignItems(Align.CENTER)
                .flexGrow(0.0f)
                .flexShrink(0.0f)
        );

        row.addChild(label(labelText));

        final ItemStack currentStack = stack != null ? stack : ItemStack.EMPTY;
        ShopItemWidget slot = new ShopItemWidget(currentStack, 16.0f).asSlot(20.0f, 20.0f);
        slot.layout(style -> style.fixed(20.0f, 20.0f).flexNone());
        row.addChild(slot);

        String idStr = BuiltInRegistries.ITEM.getKey(currentStack.getItem()).toString();
        TextInput idInput = new TextInput(idStr);
        styleTextInput(idInput);
        idInput.layout(style -> style.height(ROW_HEIGHT).flexGrow(1.0f).flexShrink(1.0f).minWidth(0.0f));

        idInput.onTextChanged(event -> {
            try {
                ResourceLocation id = ResourceLocation.tryParse(event.newText());
                Item item = id != null ? BuiltInRegistries.ITEM.get(id) : Items.AIR;
                if (item != null && item != Items.AIR) {
                    ItemStack newStack = new ItemStack(item, 1);
                    slot.item(newStack);
                    if (onChange != null) {
                        onChange.accept(newStack);
                    }
                }
            } catch (Exception ignored) {
            }
        });

        row.addChild(idInput);
        return row;
    }

    /**
     * Строка вариантов (options) через выпадающий ComboBox или ввод строки.
     */
    public static HBox optionsRow(
            String labelText,
            String initialValue,
            List<String> options,
            boolean allowCustom,
            Consumer<String> onChange
    ) {
        HBox row = new HBox();
        row.spacing(6.0f);
        row.layout(style -> style
                .widthPercent(100.0f)
                .height(ROW_HEIGHT)
                .alignItems(Align.CENTER)
                .flexGrow(0.0f)
                .flexShrink(0.0f)
        );

        row.addChild(label(labelText));

        if (options != null && !options.isEmpty()) {
            int selectedIdx = Math.max(0, options.indexOf(initialValue));
            ComboBox combo = new ComboBox();
            combo.items(options);
            combo.selectedIndex(selectedIdx);
            styleComboBox(combo);
            combo.layout(style -> style.height(ROW_HEIGHT).flexGrow(1.0f).flexShrink(1.0f).minWidth(0.0f));

            if (onChange != null) {
                combo.onSelectionChanged(event -> {
                    String selected = combo.selectedItem();
                    if (selected != null) {
                        onChange.accept(selected);
                    }
                });
            }

            row.addChild(combo);
        } else {
            TextInput input = new TextInput(initialValue != null ? initialValue : "");
            styleTextInput(input);
            input.layout(style -> style.height(ROW_HEIGHT).flexGrow(1.0f).flexShrink(1.0f).minWidth(0.0f));
            if (onChange != null) {
                input.onTextChanged(event -> onChange.accept(event.newText()));
            }
            row.addChild(input);
        }

        return row;
    }
}
