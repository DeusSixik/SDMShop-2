package dev.sixik.sdmshop2.libs.shop.client.config.constructors;

import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.gui.widget.layout.Layout;
import com.lowdragmc.lowdraglib.utils.Size;
import dev.sixik.sdmshop2.SDMShop2;
import dev.sixik.sdmshop2.libs.shop.base.ShopEntity;
import dev.sixik.sdmshop2.libs.shop.client.SDMShopClient;
import dev.sixik.sdmshop2.libs.shop.client.config.ComponentCollapsedGroupWidget;
import dev.sixik.sdmshop2.libs.shop.client.config.ComponentConfigurationWidget;
import dev.sixik.sdmshop2.libs.shop.client.screens.widgets.CollapsedGroupWidget;
import dev.sixik.sdmshop2.libs.shop.client.screens.widgets.SDMBlockSelectorWidget;
import dev.sixik.sdmshop2.libs.shop.client.screens.widgets.SDMItemStackSelectorWidget;
import dev.sixik.sdmshop2.libs.shop.client.textures.ColorRectAndBorderTexture;
import dev.sixik.sdmshop2.libs.shop.components.api.ShopComponent;
import dev.sixik.sdmshop2.libs.shop.components.api.annotation.ComponentNumberRange;
import dev.sixik.sdmshop2.libs.shop.components.api.annotation.ComponentStringRegex;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.ButtonWidget;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.InputTextBox;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.TextLabel;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.containers.DropDownBox;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

public class ComponentConfigWidgetConstructor extends WidgetGroup {

    private static final int DEFAULT_W = 85;
    private static final int DEFAULT_H = 20;

    protected final ShopComponent targetComponent;
    protected final ComponentConfigAccess.CachedField cachedField;
    protected Style style = Style.defaults();
    protected @Nullable Widget editor;
    private boolean layingOut;

    public ComponentConfigWidgetConstructor(ShopComponent targetComponent, ComponentConfigAccess.CachedField cachedField) {
        super(0, 0, DEFAULT_W, DEFAULT_H);
        this.targetComponent = targetComponent;
        this.cachedField = cachedField;
        setClientSideWidget();
        setLayout(Layout.NONE);
        setDynamicSized(false);
        rebuildEditor();
    }

    public static void createShopOfferWidget(WidgetGroup root, ShopEntity offer, int w) {
        final var components = offer.getComponents();

        for (ShopComponent component : components) {
            CollapsedGroupWidget widget = new ComponentCollapsedGroupWidget(component, offer, w);
            widget.useTabulation();
            widget.addWidget(new ComponentConfigurationWidget(w, component));
            root.addWidget(widget);
        }
    }

    public static @Nullable Widget createWidget(ShopComponent targetComponent, ComponentConfigAccess.CachedField cachedField) {
        return supports(cachedField) ? new ComponentConfigWidgetConstructor(targetComponent, cachedField) : null;
    }

    public ComponentConfigWidgetConstructor setStyle(@Nullable Style style) {
        this.style = style == null ? Style.defaults() : style.copy();
        rebuildEditor();
        return this;
    }

    public Style getStyle() {
        return style;
    }

    public ShopComponent getTargetComponent() {
        return targetComponent;
    }

    public ComponentConfigAccess.CachedField getCachedField() {
        return cachedField;
    }

    public @Nullable Widget getEditor() {
        return editor;
    }

    public void rebuildEditor() {
        clearAllWidgets();
        editor = createEditor();
        if (editor != null) {
            editor.setSelfPosition(0, 0);
            addWidget(editor);
        }
        layoutEditor(getSizeWidth());
    }

    @Override
    public void setSize(Size size) {
        super.setSize(size);
        if (!layingOut) {
            layoutEditor(size.width);
        }
    }

    private void layoutEditor(int width) {
        if (editor == null || layingOut) return;

        layingOut = true;
        try {
            int safeWidth = Math.max(1, width);
            editor.setSelfPosition(0, 0);
            editor.setSize(new Size(safeWidth, style.editorHeight()));
            int targetHeight = Math.max(style.editorHeight(), editor.getSizeHeight());
            if (getSizeWidth() != safeWidth || getSizeHeight() != targetHeight) {
                super.setSize(new Size(safeWidth, targetHeight));
            }
        } finally {
            layingOut = false;
        }
    }

    private @Nullable Widget createEditor() {
        Class<?> type = cachedField.type();
        if (type.isArray() || Collection.class.isAssignableFrom(type)) {
            Class<?> innerType = type.isArray() ? type.getComponentType() : cachedField.innerType();
            return innerType == null || !supportsValueType(innerType) ? null : new CollectionEditor(type, innerType);
        }

        return createValueEditor(type, readFieldValue(), cachedField, value -> {
            try {
                cachedField.setter().invoke(targetComponent, value);
                invokeUpdate(targetComponent);
            } catch (Throwable e) {
                SDMShop2.LOGGER.error("Failed to set {} value", type.getSimpleName(), e);
            }
        });
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private @Nullable Widget createValueEditor(Class<?> type, @Nullable Object value, ComponentConfigAccess.CachedField field, Consumer<Object> onChange) {
        Widget optionsEditor = createOptionsEditor(type, value, field, onChange);
        if (optionsEditor != null) {
            return optionsEditor;
        }

        if (isTextEditableType(type)) {
            return createTextEditor(type, value == null ? "" : String.valueOf(value), field, onChange);
        }

        if (type == boolean.class || type == Boolean.class) {
            return createBooleanEditor(Boolean.TRUE.equals(value), onChange);
        }

        if (type.isEnum()) {
            Object[] constants = type.getEnumConstants();
            if (constants == null || constants.length == 0) return null;

            DropDownBox dropDownBox = new DropDownBox(0, 0, DEFAULT_W, style.editorHeight())
                    .setOptionHeight(style.editorHeight())
                    .setMaxVisibleOptions(style.dropdownMaxVisibleOptions())
                    .setPadding(4, 1)
                    .setColors(
                            style.dropdownHeaderFill(),
                            style.dropdownPopupFill(),
                            style.dropdownBorderColor(),
                            style.dropdownHoverFill(),
                            style.dropdownSelectedFill()
                    )
                    .setSelectedOptionFill(style.dropdownSelectedFill())
                    .setScrollBarColors(style.dropdownScrollTrack(), style.dropdownScrollThumb(), style.dropdownScrollThumbHover());
            dropDownBox.setClientSideWidget();

            int selectedIndex = 0;
            for (int i = 0; i < constants.length; i++) {
                Enum<?> constant = (Enum<?>) constants[i];
                TextLabel option = new TextLabel(Component.literal(constant.name()))
                        .setAutoSize(false)
                        .setPadding(4, 0)
                        .setOverflowMode(TextLabel.OverflowMode.ELLIPSIS)
                        .alignMiddle();
                option.setSize(DEFAULT_W, style.editorHeight());
                dropDownBox.addOption(option);

                if (value == constant) {
                    selectedIndex = i;
                }
            }

            dropDownBox.setSelectedIndex(selectedIndex, false);
            dropDownBox.setSelectionChangedListener(index -> {
                if (index < 0 || index >= constants.length) return;
                try {
                    onChange.accept(Enum.valueOf((Class<Enum>) type, ((Enum<?>) constants[index]).name()));
                } catch (IllegalArgumentException e) {
                    SDMShop2.LOGGER.error("Unknown Enum value: {}", constants[index]);
                }
            });
            return dropDownBox;
        }

        if (type == ItemStack.class || type == Item.class) {
            SDMItemStackSelectorWidget selectorWidget = new SDMItemStackSelectorWidget(0, 0, DEFAULT_W, false);
            selectorWidget.setClientSideWidget();
            if (type == ItemStack.class) {
                selectorWidget.setItemStack(value instanceof ItemStack stack ? stack : ItemStack.EMPTY);
                selectorWidget.setOnItemStackUpdate(onChange::accept);
            } else {
                selectorWidget.setItemStack(value instanceof Item item ? item.getDefaultInstance() : ItemStack.EMPTY);
                selectorWidget.setOnItemStackUpdate(itemStack -> onChange.accept(itemStack.getItem()));
            }
            return selectorWidget;
        }

        if (type == Block.class || type == BlockState.class) {
            boolean isState = type == BlockState.class;
            SDMBlockSelectorWidget selectorWidget = new SDMBlockSelectorWidget(0, 0, DEFAULT_W, isState);
            selectorWidget.setClientSideWidget();
            if (isState) {
                selectorWidget.setBlock(value instanceof BlockState state ? state : Blocks.AIR.defaultBlockState());
                selectorWidget.setOnBlockStateUpdate(blockState -> onChange.accept(blockState == null ? Blocks.AIR.defaultBlockState() : blockState));
            } else {
                selectorWidget.setBlock(value instanceof Block block ? block.defaultBlockState() : Blocks.AIR.defaultBlockState());
                selectorWidget.setOnBlockStateUpdate(blockState -> onChange.accept(blockState == null ? Blocks.AIR : blockState.getBlock()));
            }
            return selectorWidget;
        }

        return null;
    }

    private @Nullable Widget createOptionsEditor(Class<?> type, @Nullable Object currentValue, ComponentConfigAccess.CachedField field, Consumer<Object> onChange) {
        if (field.options() == null) return null;

        List<ComponentConfigOptionProviders.Option> options = new ArrayList<>(ComponentConfigOptionProviders.getOptions(targetComponent, field));
        if (options.isEmpty()) {
            return field.options().allowCustom() ? null : createDisabledOptionsButton();
        }

        int selectedIndex = -1;
        for (int i = 0; i < options.size(); i++) {
            if (Objects.equals(options.get(i).value(), currentValue)) {
                selectedIndex = i;
                break;
            }
        }

        if (selectedIndex < 0 && currentValue != null) {
            selectedIndex = options.size();
            options.add(new ComponentConfigOptionProviders.Option(
                    currentValue,
                    Component.literal(String.valueOf(currentValue)).append(Component.literal(" §8(custom)"))
            ));
        }

        DropDownBox dropDownBox = new DropDownBox(0, 0, DEFAULT_W, style.editorHeight())
                .setOptionHeight(style.editorHeight())
                .setMaxVisibleOptions(style.dropdownMaxVisibleOptions())
                .setPadding(4, 1)
                .setColors(
                        style.dropdownHeaderFill(),
                        style.dropdownPopupFill(),
                        style.dropdownBorderColor(),
                        style.dropdownHoverFill(),
                        style.dropdownSelectedFill()
                )
                .setSelectedOptionFill(style.dropdownSelectedFill())
                .setScrollBarColors(style.dropdownScrollTrack(), style.dropdownScrollThumb(), style.dropdownScrollThumbHover());
        dropDownBox.setClientSideWidget();

        for (ComponentConfigOptionProviders.Option option : options) {
            TextLabel label = new TextLabel(option.label())
                    .setAutoSize(false)
                    .setPadding(4, 0)
                    .setOverflowMode(TextLabel.OverflowMode.ELLIPSIS)
                    .alignMiddle();
            label.setSize(DEFAULT_W, style.editorHeight());
            dropDownBox.addOption(label);
        }

        dropDownBox.setSelectedIndex(Math.max(0, selectedIndex), false);
        dropDownBox.setSelectionChangedListener(index -> {
            if (index < 0 || index >= options.size()) return;

            Object selectedValue = options.get(index).value();
            if (ComponentConfigOptionProviders.applyOption(targetComponent, field, selectedValue)) {
                invokeUpdate(targetComponent);
                return;
            }

            Object converted = convertOptionValue(type, selectedValue);
            if (converted != null || !type.isPrimitive()) {
                onChange.accept(converted);
            }
        });
        return dropDownBox;
    }

    private ButtonWidget createDisabledOptionsButton() {
        ButtonWidget button = createActionButton(Component.translatable("client.shop.component.editor.options.empty"), ignored -> { });
        button.setActive(false);
        return button;
    }

    private @Nullable Object convertOptionValue(Class<?> type, @Nullable Object value) {
        if (value == null || type.isInstance(value)) return value;

        String text = String.valueOf(value);
        try {
            if (type == String.class) return text;
            if (type == ResourceLocation.class) return parseResourceLocation(text);
            if (type == UUID.class) return UUID.fromString(text);
            if (type == int.class || type == Integer.class) return Integer.parseInt(text);
            if (type == long.class || type == Long.class) return Long.parseLong(text);
            if (type == float.class || type == Float.class) return Float.parseFloat(text);
            if (type == double.class || type == Double.class) return Double.parseDouble(text);
        } catch (Exception ignored) {
            SDMShop2.LOGGER.warn("Failed to convert option value '{}' to {}", value, type.getSimpleName());
        }
        return null;
    }

    private InputTextBox createTextEditor(Class<?> type, String currentValue, ComponentConfigAccess.CachedField field, Consumer<Object> onChange) {
        InputTextBox widget = new InputTextBox(0, 0, DEFAULT_W, style.editorHeight());
        applyInputStyle(widget);
        configureTextEditor(widget, type, field);
        widget.setCurrentString(currentValue == null ? "" : currentValue);
        widget.setTextResponder(text -> parseEditableValue(type, text, field).ifPresent(onChange));
        widget.setClientSideWidget();
        return widget;
    }

    private ButtonWidget createBooleanEditor(boolean initialValue, Consumer<Object> onChange) {
        final boolean[] value = {initialValue};
        final ButtonWidget[] buttonRef = new ButtonWidget[1];
        ButtonWidget button = createActionButton(booleanText(value[0]), ignored -> {
            value[0] = !value[0];
            buttonTextUpdate(buttonRef[0], value[0]);
            onChange.accept(value[0]);
        });
        buttonRef[0] = button;
        buttonTextUpdate(button, value[0]);
        return button;
    }

    private void buttonTextUpdate(@Nullable ButtonWidget button, boolean value) {
        if (button != null) {
            button.setText(booleanText(value));
        }
    }

    private Component booleanText(boolean value) {
        return Component.translatable(value
                ? "client.shop.component.editor.switch.on"
                : "client.shop.component.editor.switch.off");
    }

    private ButtonWidget createActionButton(Component text, Consumer<Object> onPress) {
        ButtonWidget button = new ButtonWidget(0, 0, DEFAULT_W, style.editorHeight(), text, ignored -> onPress.accept(ignored));
        applyButtonStyle(button);
        button.setClientSideWidget();
        return button;
    }

    private void applyInputStyle(InputTextBox widget) {
        widget.setBackground(new ColorRectAndBorderTexture(style.inputFill(), style.inputBorder(), 1).setRadius(style.radius()));
        widget.setFocusedOutline(style.inputFill(), style.inputFocusedBorder());
        widget.setTextColor(style.inputTextColor());
        widget.setPadding(style.inputPaddingX(), style.inputPaddingY());
    }

    private void applyButtonStyle(ButtonWidget button) {
        button.setButtonColors(style.buttonFill(), style.buttonBorder());
        button.setHoverColors(style.buttonHoverFill(), style.buttonHoverBorder());
        button.setClickedColors(style.buttonPressedFill(), style.buttonHoverBorder());
        button.setTextColor(style.buttonTextColor());
        button.setTextPadding(style.buttonTextPadding());
        button.setMinTextScale(0.35f);
    }

    private void configureTextEditor(InputTextBox widget, Class<?> type, ComponentConfigAccess.CachedField field) {
        ComponentNumberRange numberRange = field.numberRange();

        if (type == int.class || type == Integer.class) {
            int min = numberRange != null ? numberRange.intMin() : Integer.MIN_VALUE;
            int max = numberRange != null ? numberRange.intMax() : Integer.MAX_VALUE;
            widget.setNumbersOnly(min, max);
        } else if (type == long.class || type == Long.class) {
            long min = numberRange != null ? numberRange.longMin() : Long.MIN_VALUE;
            long max = numberRange != null ? numberRange.longMax() : Long.MAX_VALUE;
            widget.setNumbersOnly(min, max);
        } else if (type == float.class || type == Float.class) {
            float min = numberRange != null ? numberRange.floatMin() : -Float.MAX_VALUE;
            float max = numberRange != null ? numberRange.floatMax() : Float.MAX_VALUE;
            widget.setNumbersOnly(min, max);
        } else if (type == double.class || type == Double.class) {
            double min = numberRange != null ? numberRange.doubleMin() : -Double.MAX_VALUE;
            double max = numberRange != null ? numberRange.doubleMax() : Double.MAX_VALUE;
            widget.setNumbersOnly(min, max);
        } else if (type == ResourceLocation.class) {
            widget.setResourceLocationOnly();
        } else if (type == UUID.class) {
            widget.setUuidOnly();
        }
    }

    private @Nullable Object readFieldValue() {
        try {
            return cachedField.getter().invoke(targetComponent);
        } catch (Throwable e) {
            SDMShop2.LOGGER.error("Failed to get {} value", cachedField.translationKey(), e);
            return null;
        }
    }

    private Optional<Object> parseEditableValue(Class<?> type, String text, ComponentConfigAccess.CachedField field) {
        if (isIntermediateText(type, text)) {
            return Optional.empty();
        }

        try {
            if (type == String.class) {
                return validateString(text == null ? "" : text, field).map(value -> value);
            }
            if (type == int.class || type == Integer.class) return Optional.of(Integer.parseInt(text));
            if (type == long.class || type == Long.class) return Optional.of(Long.parseLong(text));
            if (type == float.class || type == Float.class) return Optional.of(Float.parseFloat(text));
            if (type == double.class || type == Double.class) return Optional.of(Double.parseDouble(text));
            if (type == UUID.class) return Optional.of(UUID.fromString(text));
            if (type == ResourceLocation.class) {
                ResourceLocation location = parseResourceLocation(text);
                return location != null ? Optional.of(location) : Optional.empty();
            }
        } catch (Exception ignored) {
            return Optional.empty();
        }

        return Optional.empty();
    }

    private Optional<Object> validateString(String text, ComponentConfigAccess.CachedField field) {
        ComponentStringRegex regexInfo = field.stringRegex();
        if (regexInfo != null && !text.matches(regexInfo.value())) {
            SDMShop2.LOGGER.warn("Regex validation failed for {}: Expected {} but got '{}' ({})",
                    field.translationKey(), regexInfo.value(), text, regexInfo.errorMessage());
            return Optional.empty();
        }
        return Optional.of(text);
    }

    private @Nullable ResourceLocation parseResourceLocation(String text) {
        if (text == null || text.isEmpty()) {
            return null;
        }
        return text.contains(":") ? ResourceLocation.tryParse(text) : ResourceLocation.tryBuild("minecraft", text);
    }

    private boolean isIntermediateText(Class<?> type, String text) {
        if (text == null || text.isEmpty()) {
            return type != String.class;
        }
        if (isIntegralType(type)) {
            return text.equals("-");
        }
        if (isDecimalType(type)) {
            return text.equals("-") || text.equals(".") || text.equals("-.");
        }
        return false;
    }

    private static boolean supports(ComponentConfigAccess.CachedField field) {
        Class<?> type = field.type();
        if (type.isArray()) {
            return supportsValueType(type.getComponentType());
        }
        if (Collection.class.isAssignableFrom(type)) {
            return field.innerType() != null && supportsValueType(field.innerType());
        }
        return supportsValueType(type);
    }

    private static boolean supportsValueType(Class<?> type) {
        return isTextEditableType(type)
                || type == boolean.class
                || type == Boolean.class
                || type.isEnum()
                || type == ItemStack.class
                || type == Item.class
                || type == Block.class
                || type == BlockState.class;
    }

    private static boolean isTextEditableType(Class<?> type) {
        return type == String.class
                || isIntegralType(type)
                || isDecimalType(type)
                || type == ResourceLocation.class
                || type == UUID.class;
    }

    private static boolean isIntegralType(Class<?> type) {
        return type == int.class || type == Integer.class || type == long.class || type == Long.class;
    }

    private static boolean isDecimalType(Class<?> type) {
        return type == float.class || type == Float.class || type == double.class || type == Double.class;
    }

    public static void invokeUpdate(ShopComponent targetComponent) {
        SDMShopClient.UPDATE_COMPONENT_EVENT.invoker().onUpdateComponentEvent(targetComponent.getRoot(), targetComponent);
    }

    private class CollectionEditor extends WidgetGroup {

        private final Class<?> collectionType;
        private final Class<?> innerType;
        private boolean collectionLayout;

        private CollectionEditor(Class<?> collectionType, Class<?> innerType) {
            super(0, 0, DEFAULT_W, DEFAULT_H);
            this.collectionType = collectionType;
            this.innerType = innerType;
            setClientSideWidget();
            setLayout(Layout.NONE);
            setDynamicSized(false);
            rebuild();
        }

        private void rebuild() {
            int currentWidth = Math.max(1, getSizeWidth());
            CollectionState state = readCollection();
            List<Object> currentList = state.values();
            Object existingRef = state.source();

            clearAllWidgets();
            for (int i = 0; i < currentList.size(); i++) {
                final int index = i;
                Widget editorWidget = createValueEditor(innerType, currentList.get(i), cachedField, newValue -> {
                    currentList.set(index, newValue);
                    saveCollection(currentList, existingRef);
                });
                if (editorWidget == null) continue;

                WidgetGroup row = new WidgetGroup(0, 0, currentWidth, style.editorHeight());
                row.setLayout(Layout.NONE);
                row.setDynamicSized(false);
                row.addWidget(editorWidget);

                ButtonWidget removeButton = createActionButton(Component.literal("×"), ignored -> {
                    currentList.remove(index);
                    saveCollection(currentList, existingRef);
                    rebuild();
                });
                removeButton.setHoverTooltips("client.shop.component.editor.arrays.button.remove_element");
                row.addWidget(removeButton);
                addWidget(row);
            }

            ButtonWidget addButton = createActionButton(Component.translatable("client.shop.component.editor.arrays.button.add_element"), ignored -> {
                currentList.add(getDefaultValue(innerType));
                saveCollection(currentList, existingRef);
                rebuild();
            });
            addWidget(addButton);
            layoutRows(currentWidth);
        }

        @Override
        public void setSize(Size size) {
            super.setSize(size);
            if (!collectionLayout) {
                layoutRows(size.width);
            }
        }

        private void layoutRows(int width) {
            if (collectionLayout) return;
            collectionLayout = true;
            try {
                int safeWidth = Math.max(1, width);
                int y = 0;
                int removeWidth = Math.min(style.collectionButtonWidth(), Math.max(1, safeWidth));
                int editorWidth = Math.max(1, safeWidth - removeWidth - style.collectionGap());

                for (Widget child : widgets) {
                    child.setSelfPosition(0, y);
                    child.setSize(new Size(safeWidth, style.editorHeight()));

                    if (child instanceof WidgetGroup row && row.widgets.size() >= 2) {
                        Widget rowEditor = row.widgets.get(0);
                        Widget removeButton = row.widgets.get(1);
                        rowEditor.setSelfPosition(0, 0);
                        rowEditor.setSize(new Size(editorWidth, style.editorHeight()));
                        removeButton.setSelfPosition(safeWidth - removeWidth, 0);
                        removeButton.setSize(new Size(removeWidth, style.editorHeight()));
                    }

                    y += style.editorHeight() + style.collectionRowSpacing();
                }

                int targetHeight = widgets.isEmpty() ? style.editorHeight() : Math.max(style.editorHeight(), y - style.collectionRowSpacing());
                if (getSizeWidth() != safeWidth || getSizeHeight() != targetHeight) {
                    super.setSize(new Size(safeWidth, targetHeight));
                }
            } finally {
                collectionLayout = false;
            }
        }

        private CollectionState readCollection() {
            List<Object> values = new ArrayList<>();
            Object source = null;

            try {
                source = cachedField.getter().invoke(targetComponent);
                if (source != null) {
                    if (collectionType.isArray()) {
                        int length = Array.getLength(source);
                        for (int i = 0; i < length; i++) {
                            values.add(Array.get(source, i));
                        }
                    } else if (source instanceof Collection<?> collection) {
                        values.addAll(collection);
                    }
                }
            } catch (Throwable e) {
                SDMShop2.LOGGER.error("Failed to read collection {}", cachedField.translationKey(), e);
            }

            return new CollectionState(values, source);
        }

        @SuppressWarnings("unchecked")
        private void saveCollection(List<Object> values, @Nullable Object existingRef) {
            try {
                if (collectionType.isArray()) {
                    Object array = Array.newInstance(innerType, values.size());
                    for (int i = 0; i < values.size(); i++) {
                        Array.set(array, i, values.get(i));
                    }
                    cachedField.setter().invoke(targetComponent, array);
                } else if (existingRef instanceof Collection) {
                    Collection<Object> collection = (Collection<Object>) existingRef;
                    collection.clear();
                    collection.addAll(values);
                    cachedField.setter().invoke(targetComponent, collection);
                } else {
                    Collection<Object> collection;
                    if (Set.class.isAssignableFrom(collectionType)) {
                        collection = new LinkedHashSet<>(values);
                    } else if (collectionType.getName().contains("fastutil")) {
                        collection = new ObjectArrayList<>(values);
                    } else {
                        collection = new ArrayList<>(values);
                    }
                    cachedField.setter().invoke(targetComponent, collection);
                }
                invokeUpdate(targetComponent);
            } catch (Throwable e) {
                SDMShop2.LOGGER.error("Failed to save collection {}", cachedField.translationKey(), e);
            }
        }
    }

    private record CollectionState(List<Object> values, @Nullable Object source) {
    }

    private Object getDefaultValue(Class<?> type) {
        if (type == String.class) return "";
        if (type == int.class || type == Integer.class) return 0;
        if (type == long.class || type == Long.class) return 0L;
        if (type == double.class || type == Double.class) return 0.0;
        if (type == float.class || type == Float.class) return 0.0f;
        if (type == boolean.class || type == Boolean.class) return false;
        if (type == UUID.class) return UUID.randomUUID();
        if (type == ResourceLocation.class) return new ResourceLocation("minecraft", "air");
        if (type.isEnum()) return Arrays.stream(type.getEnumConstants()).findFirst().orElse(null);
        if (type == ItemStack.class) return ItemStack.EMPTY.copy();
        if (type == Item.class) return Items.AIR;
        if (type == Block.class) return Blocks.AIR;
        if (type == BlockState.class) return Blocks.AIR.defaultBlockState();
        return null;
    }

    public static class Style {
        private int editorHeight = DEFAULT_H;
        private int collectionRowSpacing = 2;
        private int collectionButtonWidth = 18;
        private int collectionGap = 2;
        private int dropdownMaxVisibleOptions = 6;
        private int radius = 3;

        private int inputFill = 0xFF101016;
        private int inputBorder = 0xFF5C637A;
        private int inputFocusedBorder = 0xFFFFFFFF;
        private int inputTextColor = 0xFFE8E8F0;
        private int inputPaddingX = 4;
        private int inputPaddingY = 1;

        private int buttonFill = 0xFF2A2A36;
        private int buttonBorder = 0xFF3D3D4E;
        private int buttonHoverFill = 0xFF34384A;
        private int buttonHoverBorder = 0xFF7986CB;
        private int buttonPressedFill = 0xFF202331;
        private int buttonTextColor = 0xFFFFFFFF;
        private int buttonTextPadding = 3;

        private int dropdownHeaderFill = 0xFF252733;
        private int dropdownPopupFill = 0xFF1E1F28;
        private int dropdownBorderColor = 0xFF4C5265;
        private int dropdownHoverFill = 0xFF343746;
        private int dropdownSelectedFill = 0x553F51B5;
        private int dropdownScrollTrack = 0x5530303A;
        private int dropdownScrollThumb = 0xFF6D7485;
        private int dropdownScrollThumbHover = 0xFFAAB2C5;

        public static Style defaults() {
            return new Style();
        }

        public Style copy() {
            Style copy = new Style();
            copy.editorHeight = editorHeight;
            copy.collectionRowSpacing = collectionRowSpacing;
            copy.collectionButtonWidth = collectionButtonWidth;
            copy.collectionGap = collectionGap;
            copy.dropdownMaxVisibleOptions = dropdownMaxVisibleOptions;
            copy.radius = radius;
            copy.inputFill = inputFill;
            copy.inputBorder = inputBorder;
            copy.inputFocusedBorder = inputFocusedBorder;
            copy.inputTextColor = inputTextColor;
            copy.inputPaddingX = inputPaddingX;
            copy.inputPaddingY = inputPaddingY;
            copy.buttonFill = buttonFill;
            copy.buttonBorder = buttonBorder;
            copy.buttonHoverFill = buttonHoverFill;
            copy.buttonHoverBorder = buttonHoverBorder;
            copy.buttonPressedFill = buttonPressedFill;
            copy.buttonTextColor = buttonTextColor;
            copy.buttonTextPadding = buttonTextPadding;
            copy.dropdownHeaderFill = dropdownHeaderFill;
            copy.dropdownPopupFill = dropdownPopupFill;
            copy.dropdownBorderColor = dropdownBorderColor;
            copy.dropdownHoverFill = dropdownHoverFill;
            copy.dropdownSelectedFill = dropdownSelectedFill;
            copy.dropdownScrollTrack = dropdownScrollTrack;
            copy.dropdownScrollThumb = dropdownScrollThumb;
            copy.dropdownScrollThumbHover = dropdownScrollThumbHover;
            return copy;
        }

        public int editorHeight() {
            return editorHeight;
        }

        public Style setEditorHeight(int editorHeight) {
            this.editorHeight = Math.max(1, editorHeight);
            return this;
        }

        public int collectionRowSpacing() {
            return collectionRowSpacing;
        }

        public Style setCollectionRowSpacing(int collectionRowSpacing) {
            this.collectionRowSpacing = Math.max(0, collectionRowSpacing);
            return this;
        }

        public int collectionButtonWidth() {
            return collectionButtonWidth;
        }

        public Style setCollectionButtonWidth(int collectionButtonWidth) {
            this.collectionButtonWidth = Math.max(1, collectionButtonWidth);
            return this;
        }

        public int collectionGap() {
            return collectionGap;
        }

        public Style setCollectionGap(int collectionGap) {
            this.collectionGap = Math.max(0, collectionGap);
            return this;
        }

        public int dropdownMaxVisibleOptions() {
            return dropdownMaxVisibleOptions;
        }

        public Style setDropdownMaxVisibleOptions(int dropdownMaxVisibleOptions) {
            this.dropdownMaxVisibleOptions = Math.max(1, dropdownMaxVisibleOptions);
            return this;
        }

        public int radius() {
            return radius;
        }

        public Style setRadius(int radius) {
            this.radius = Math.max(0, radius);
            return this;
        }

        public int inputFill() {
            return inputFill;
        }

        public int inputBorder() {
            return inputBorder;
        }

        public int inputFocusedBorder() {
            return inputFocusedBorder;
        }

        public int inputTextColor() {
            return inputTextColor;
        }

        public int inputPaddingX() {
            return inputPaddingX;
        }

        public int inputPaddingY() {
            return inputPaddingY;
        }

        public Style setInputColors(int fill, int border, int focusedBorder, int textColor) {
            this.inputFill = fill;
            this.inputBorder = border;
            this.inputFocusedBorder = focusedBorder;
            this.inputTextColor = textColor;
            return this;
        }

        public Style setInputPadding(int horizontal, int vertical) {
            this.inputPaddingX = Math.max(0, horizontal);
            this.inputPaddingY = Math.max(0, vertical);
            return this;
        }

        public int buttonFill() {
            return buttonFill;
        }

        public int buttonBorder() {
            return buttonBorder;
        }

        public int buttonHoverFill() {
            return buttonHoverFill;
        }

        public int buttonHoverBorder() {
            return buttonHoverBorder;
        }

        public int buttonPressedFill() {
            return buttonPressedFill;
        }

        public int buttonTextColor() {
            return buttonTextColor;
        }

        public int buttonTextPadding() {
            return buttonTextPadding;
        }

        public Style setButtonColors(int fill, int border, int hoverFill, int hoverBorder, int pressedFill, int textColor) {
            this.buttonFill = fill;
            this.buttonBorder = border;
            this.buttonHoverFill = hoverFill;
            this.buttonHoverBorder = hoverBorder;
            this.buttonPressedFill = pressedFill;
            this.buttonTextColor = textColor;
            return this;
        }

        public Style setButtonTextPadding(int buttonTextPadding) {
            this.buttonTextPadding = Math.max(0, buttonTextPadding);
            return this;
        }

        public int dropdownHeaderFill() {
            return dropdownHeaderFill;
        }

        public int dropdownPopupFill() {
            return dropdownPopupFill;
        }

        public int dropdownBorderColor() {
            return dropdownBorderColor;
        }

        public int dropdownHoverFill() {
            return dropdownHoverFill;
        }

        public int dropdownSelectedFill() {
            return dropdownSelectedFill;
        }

        public int dropdownScrollTrack() {
            return dropdownScrollTrack;
        }

        public int dropdownScrollThumb() {
            return dropdownScrollThumb;
        }

        public int dropdownScrollThumbHover() {
            return dropdownScrollThumbHover;
        }

        public Style setDropdownColors(int headerFill, int popupFill, int borderColor, int hoverFill, int selectedFill) {
            this.dropdownHeaderFill = headerFill;
            this.dropdownPopupFill = popupFill;
            this.dropdownBorderColor = borderColor;
            this.dropdownHoverFill = hoverFill;
            this.dropdownSelectedFill = selectedFill;
            return this;
        }

        public Style setDropdownScrollColors(int track, int thumb, int thumbHover) {
            this.dropdownScrollTrack = track;
            this.dropdownScrollThumb = thumb;
            this.dropdownScrollThumbHover = thumbHover;
            return this;
        }
    }
}
