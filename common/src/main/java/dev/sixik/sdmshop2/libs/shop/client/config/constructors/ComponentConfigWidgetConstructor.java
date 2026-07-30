package dev.sixik.sdmshop2.libs.shop.client.config.constructors;

import com.lowdragmc.lowdraglib.gui.texture.TextTexture;
import com.lowdragmc.lowdraglib.gui.widget.*;
import com.lowdragmc.lowdraglib.gui.widget.layout.Layout;
import com.lowdragmc.lowdraglib.utils.Size;
import dev.sixik.sdmshop2.SDMShop2;
import dev.sixik.sdmshop2.libs.shop.base.ShopEntity;
import dev.sixik.sdmshop2.libs.shop.base.ShopOffer;
import dev.sixik.sdmshop2.libs.shop.client.SDMShopClient;
import dev.sixik.sdmshop2.libs.shop.client.config.ComponentCollapsedGroupWidget;
import dev.sixik.sdmshop2.libs.shop.client.config.ComponentConfigurationWidget;
import dev.sixik.sdmshop2.libs.shop.client.screens.widgets.CollapsedGroupWidget;
import dev.sixik.sdmshop2.libs.shop.client.screens.widgets.ExternTextFieldWidget;
import dev.sixik.sdmshop2.libs.shop.client.screens.widgets.SDMBlockSelectorWidget;
import dev.sixik.sdmshop2.libs.shop.client.screens.widgets.SDMItemStackSelectorWidget;
import dev.sixik.sdmshop2.libs.shop.components.api.ShopComponent;
import dev.sixik.sdmshop2.libs.shop.components.api.annotation.ComponentNumberRange;
import dev.sixik.sdmshop2.libs.shop.components.api.annotation.ComponentStringRegex;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Consumer;

public class ComponentConfigWidgetConstructor {

    private static final int DEFAULT_W = 60;
    private static final int DEFAULT_H = 15;

    public static void createShopOfferWidget(WidgetGroup root, ShopEntity offer, int w) {
        final var components = offer.getComponents();

        for (int i = 0; i < components.size(); i++) {
            ShopComponent component = components.get(i);

            CollapsedGroupWidget widget = new ComponentCollapsedGroupWidget(component, offer, w);
            widget.useTabulation();
            widget.addWidget(new ComponentConfigurationWidget(w, component));
            root.addWidget(widget);
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static Widget createWidget(ShopComponent targetComponent, ComponentConfigAccess.CachedField cachedField) {
        Class<?> type = cachedField.type();
        
        if (type.isArray() || Collection.class.isAssignableFrom(type)) {
            return createCollectionWidget(targetComponent, cachedField);
        }

        Widget someWidget = createField(targetComponent, cachedField);
        if(someWidget != null) return someWidget;

        someWidget = createSwitch(targetComponent, cachedField);
        if(someWidget != null) return someWidget;

        if (type.isEnum()) {
            Object[] constants = type.getEnumConstants();

            SelectorWidget widget = new SelectorWidget();
            List<String> options = Arrays.stream(constants)
                    .map(obj -> ((Enum<?>) obj).name())
                    .toList();
            widget.setCandidates(options);

            try {
                Object currentValue = cachedField.getter().invoke(targetComponent);
                if (currentValue != null) {
                    widget.setValue(((Enum<?>) currentValue).name());
                }
            } catch (Throwable e) {
                SDMShop2.LOGGER.error("Failed to get Enum value", e);
            }

            widget.setOnChanged(selectedString -> {
                try {
                    Enum<?> newValue = Enum.valueOf((Class<Enum>) type, selectedString);
                    cachedField.setter().invoke(targetComponent, newValue);
                    invokeUpdate(targetComponent);
                } catch (IllegalArgumentException e) {
                    SDMShop2.LOGGER.error("Unknown Enum value: {}", selectedString);
                } catch (Throwable e) {
                    SDMShop2.LOGGER.error("Failed to set Enum value", e);
                }
            });

            return widget;
        }

        if(type == ItemStack.class) {
            SDMItemStackSelectorWidget selectorWidget = new SDMItemStackSelectorWidget(0, 0, DEFAULT_W + 22, false);
            try {
                Object val = cachedField.getter().invoke(targetComponent);
                selectorWidget.setItemStack(val != null ? (ItemStack) val : ItemStack.EMPTY);
            } catch (Throwable e) {
                SDMShop2.LOGGER.error("Failed to get ItemStack value", e);
            }
            selectorWidget.setOnItemStackUpdate(itemStack -> {
                try {
                    cachedField.setter().invoke(targetComponent, itemStack);
                    invokeUpdate(targetComponent);
                } catch (Throwable e) {
                    SDMShop2.LOGGER.error("Failed to set ItemStack value", e);
                }
            });
            return selectorWidget;
        } else if(type == Item.class) {
            SDMItemStackSelectorWidget selectorWidget = new SDMItemStackSelectorWidget(0, 0, DEFAULT_W + 22, false);
            try {
                Object val = cachedField.getter().invoke(targetComponent);
                selectorWidget.setItemStack(val != null ? ((Item) val).getDefaultInstance() : ItemStack.EMPTY);
            } catch (Throwable e) {
                SDMShop2.LOGGER.error("Failed to get Item value", e);
            }
            selectorWidget.setOnItemStackUpdate(itemStack -> {
                try {
                    cachedField.setter().invoke(targetComponent, itemStack.getItem());
                    invokeUpdate(targetComponent);
                } catch (Throwable e) {
                    SDMShop2.LOGGER.error("Failed to set Item value", e);
                }
            });
            return selectorWidget;
        }

        if(type == Block.class) {
            SDMBlockSelectorWidget widget = new SDMBlockSelectorWidget(0, 0, DEFAULT_W + 22, false);
            widget.setOnBlockStateUpdate(((blockState) -> {
                try {
                    cachedField.setter().invoke(targetComponent, blockState.getBlock());
                    invokeUpdate(targetComponent);
                } catch (Throwable e) {
                    SDMShop2.LOGGER.error("Failed to set Block value", e);
                }
            }));

            try {
                Object val = cachedField.getter().invoke(targetComponent);
                widget.setBlock(val != null ? ((Block) val).defaultBlockState() : Blocks.AIR.defaultBlockState());
            } catch (Throwable e) {
                SDMShop2.LOGGER.error("Failed to get Block value", e);
            }

            return widget;
        } else if(type == BlockState.class) {
            SDMBlockSelectorWidget widget = new SDMBlockSelectorWidget(0, 0, DEFAULT_W + 22, false);
            widget.setOnBlockStateUpdate(((blockState) -> {
                try {
                    cachedField.setter().invoke(targetComponent, blockState);
                    invokeUpdate(targetComponent);
                } catch (Throwable e) {
                    SDMShop2.LOGGER.error("Failed to set BlockState value", e);
                }
            }));

            try {
                Object val = cachedField.getter().invoke(targetComponent);
                widget.setBlock(val != null ? ((BlockState) val) : Blocks.AIR.defaultBlockState());
            } catch (Throwable e) {
                SDMShop2.LOGGER.error("Failed to get BlockState value", e);
            }

            return widget;
        }

        return null;
    }

    private static Widget createCollectionWidget(ShopComponent targetComponent, ComponentConfigAccess.CachedField cachedField) {
        Class<?> collectionType = cachedField.type();
        Class<?> innerType = collectionType.isArray() ? collectionType.getComponentType() : cachedField.innerType();

        if (innerType == null) return null;

        WidgetGroup listContainer = new WidgetGroup(0, 0, 85, 0) {
            @Override
            public void setSize(Size size) {
                super.setSize(size);
                for (Widget w : widgets) {
                    w.setSizeWidth(size.width);
                    if (w instanceof WidgetGroup row && row.widgets.size() >= 2) {
                        Widget editor = row.widgets.get(0);
                        Widget btn = row.widgets.get(1);

                        btn.setSelfPosition(size.width - 15, 0);
                        /*
                            ath.max защищает от отрицательной ширины и крашей OpenGL
                         */
                        editor.setSizeWidth(Math.max(1, size.width - 17));
                    }
                }
            }

            /*
                Блокируем схлопывание ширины в 0 при clearAllWidgets()
             */
            @Override
            protected Size computeDynamicSize() {
                Size size = super.computeDynamicSize();
                return new Size(getSizeWidth(), size.height);
            }
        };
        listContainer.setLayout(Layout.VERTICAL_LEFT);
        listContainer.setLayoutPadding(2);
        listContainer.setDynamicSized(true);

        class UIBuilder {
            void rebuild() {
                /*
                    Запоминаем ширину ДО очистки списка
                 */
                int currentWidth = listContainer.getSizeWidth();
                if (currentWidth < 10) currentWidth = 85; // Fallback для самого первого рендера

                listContainer.clearAllWidgets();
                List<Object> currentList = new ArrayList<>();
                Object existingCollection = null;

                try {
                    existingCollection = cachedField.getter().invoke(targetComponent);
                    if (existingCollection != null) {
                        if (collectionType.isArray()) {
                            int length = java.lang.reflect.Array.getLength(existingCollection);
                            for (int i = 0; i < length; i++) currentList.add(java.lang.reflect.Array.get(existingCollection, i));
                        } else if (existingCollection instanceof Collection<?> coll) {
                            currentList.addAll(coll);
                        }
                    }
                } catch (Throwable ignored) {}

                final Object existingRef = existingCollection;

                for (int i = 0; i < currentList.size(); i++) {
                    final int index = i;
                    Object item = currentList.get(i);

                    WidgetGroup row = new WidgetGroup(0, 0, currentWidth, 15);
                    row.setLayout(Layout.NONE);

                    Widget editor = createListElementEditor(innerType, item, cachedField, newValue -> {
                        currentList.set(index, newValue);
                        saveCollection(targetComponent, cachedField, currentList, collectionType, innerType, existingRef);
                    });

                    ButtonWidget removeBtn = new ButtonWidget(currentWidth - 15, 0, 15, 15, new TextTexture(() -> I18n.get("client.shop.component.editor.arrays.button.remove_element")), btn -> {
                        currentList.remove(index);
                        saveCollection(targetComponent, cachedField, currentList, collectionType, innerType, existingRef);
                        rebuild();
                    });

                    if (editor != null) {
                        editor.setSelfPosition(0, 0);
                        editor.setSizeHeight(15);
                        editor.setSizeWidth(Math.max(1, currentWidth - 17));
                        row.addWidget(editor);
                    }
                    row.addWidget(removeBtn);
                    listContainer.addWidget(row);
                }

                ButtonWidget addBtn = new ButtonWidget(0, 0, currentWidth, 15, new TextTexture(() -> I18n.get("client.shop.component.editor.arrays.button.add_element")), btn -> {
                    currentList.add(getDefaultValue(innerType));
                    saveCollection(targetComponent, cachedField, currentList, collectionType, innerType, existingRef);
                    rebuild();
                });

                listContainer.addWidget(addBtn);

                /*
                    Принудительно вызываем перерасчет Layout для новых виджетов
                 */
                listContainer.setSizeWidth(currentWidth);
            }
        }

        new UIBuilder().rebuild();
        return listContainer;
    }

    /**
     * Конвертирует наш временный List обратно в нужный тип (Array, Set, List) и сохраняет в компонент.
     */
    @SuppressWarnings("unchecked")
    private static void saveCollection(ShopComponent targetComponent, ComponentConfigAccess.CachedField cachedField, List<Object> list, Class<?> collType, Class<?> innerType, Object existingRef) {
        try {
            if (collType.isArray()) {
                /*
                    Работа с сырыми массивами (Type[])
                 */
                Object array = Array.newInstance(innerType, list.size());
                for (int i = 0; i < list.size(); i++) {
                    Array.set(array, i, list.get(i));
                }
                cachedField.setter().invoke(targetComponent, array);

            } else if (existingRef instanceof Collection) {
                /*
                    Мы просто очищаем её и заливаем новые данные
                 */
                Collection<Object> coll = (Collection<Object>) existingRef;
                coll.clear();
                coll.addAll(list);
                cachedField.setter().invoke(targetComponent, coll);

            } else {
                /*
                     Если поле изначально было null
                 */
                Collection<Object> newColl;
                if (Set.class.isAssignableFrom(collType)) {
                    newColl = new java.util.LinkedHashSet<>(list);
                } else if (collType.getName().contains("fastutil")) {
                    /*
                        Если это тип из FastUtil, создаем ObjectArrayList
                     */
                    newColl = new ObjectArrayList<>(list);
                } else {
                    newColl = new ArrayList<>(list);
                }
                cachedField.setter().invoke(targetComponent, newColl);
            }
            invokeUpdate(targetComponent);
        } catch (Throwable e) {
            SDMShop2.LOGGER.error("Failed to save collection", e);
        }
    }

    /**
     * Возвращает безопасное дефолтное значение при нажатии кнопки "Добавить".
     */
    private static Object getDefaultValue(Class<?> type) {
        if (type == String.class) return "";
        if (type == int.class || type == Integer.class) return 0;
        if (type == long.class || type == Long.class) return 0L;
        if (type == double.class || type == Double.class) return 0.0;
        if (type == float.class || type == Float.class) return 0.0f;
        if (type == boolean.class || type == Boolean.class) return false;
        if (type == UUID.class) return UUID.randomUUID();
        if (type == ResourceLocation.class) return new ResourceLocation("minecraft", "air");
        if (type.isEnum()) return type.getEnumConstants()[0];
        if (type == ItemStack.class) return ItemStack.EMPTY;
        return null;
    }

    /**
     * Создает изолированный виджет для редактирования конкретного значения из коллекции.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private static Widget createListElementEditor(Class<?> type, Object value, ComponentConfigAccess.CachedField cachedField, Consumer<Object> onChange) {
        if (isTextEditableType(type)) {
            return createTextEditor(type, value != null ? String.valueOf(value) : "", cachedField, onChange);
        }

        if (type.isEnum()) {
            SelectorWidget widget = new SelectorWidget();
            List<String> options = Arrays.stream(type.getEnumConstants()).map(obj -> ((Enum<?>) obj).name()).toList();
            widget.setCandidates(options);
            if (value != null) widget.setValue(((Enum<?>) value).name());

            widget.setOnChanged(selectedString -> {
                try {
                    onChange.accept(Enum.valueOf((Class<Enum>) type, selectedString));
                } catch (Exception ignored) {}
            });
            return widget;
        }

        if (type == boolean.class || type == Boolean.class) {
            SwitchWidget widget = new SwitchWidget();
            widget.setPressed(Boolean.TRUE.equals(value));
            widget.setOnPressCallback((s1, s2) -> onChange.accept(s2));
            return widget;
        }

        return null;
    }

    private static @Nullable SwitchWidget createSwitch(ShopComponent targetComponent, ComponentConfigAccess.CachedField cachedField) {
        SwitchWidget widget = new SwitchWidget();
        Class<?> type = cachedField.type();
        if(type == boolean.class || type == Boolean.class) {
            try {
                widget.setPressed(cachedField.getter().invoke(targetComponent) == Boolean.TRUE);
            } catch (Throwable e) {
                SDMShop2.LOGGER.error("Failed to get boolean value", e);
                widget.setPressed(false);
            }
            widget.setOnPressCallback((s1, s2) -> {
                try {
                    cachedField.setter().invoke(targetComponent, s2);
                    invokeUpdate(targetComponent);
                } catch (Throwable e) {
                    SDMShop2.LOGGER.error("Failed to toggle boolean value", e);
                }
            });
        } else
            return null;

        return widget;
    }

    private static @Nullable ExternTextFieldWidget createField(ShopComponent targetComponent, ComponentConfigAccess.CachedField cachedField) {
        Class<?> type = cachedField.type();

        if (!isTextEditableType(type)) {
            return null;
        }

        ExternTextFieldWidget widget = createTextEditor(type, readFieldAsString(targetComponent, cachedField), cachedField, value -> {
            try {
                cachedField.setter().invoke(targetComponent, value);
                invokeUpdate(targetComponent);
            } catch (Throwable e) {
                SDMShop2.LOGGER.error("Failed to set {} value", type.getSimpleName(), e);
            }
        });

        widget.setTextSupplier(() -> readFieldAsString(targetComponent, cachedField));
        return widget;
    }

    private static ExternTextFieldWidget createTextEditor(Class<?> type, String currentValue, ComponentConfigAccess.CachedField cachedField, Consumer<Object> onChange) {
        ExternTextFieldWidget widget = new ExternTextFieldWidget();
        configureTextEditor(widget, type, cachedField);
        widget.setCurrentString(currentValue != null ? currentValue : "");
        widget.setTextResponder(text -> parseEditableValue(type, text, cachedField).ifPresent(onChange));
        return widget;
    }

    private static void configureTextEditor(ExternTextFieldWidget widget, Class<?> type, ComponentConfigAccess.CachedField cachedField) {
        ComponentNumberRange numberRange = cachedField.numberRange();

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

    private static Optional<Object> parseEditableValue(Class<?> type, String text, ComponentConfigAccess.CachedField cachedField) {
        if (isIntermediateText(type, text)) {
            return Optional.empty();
        }

        try {
            if (type == String.class) {
                return validateString(text == null ? "" : text, cachedField).map(value -> value);
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

    private static Optional<Object> validateString(String text, ComponentConfigAccess.CachedField cachedField) {
        ComponentStringRegex regexInfo = cachedField.stringRegex();
        if (regexInfo != null && !text.matches(regexInfo.value())) {
            SDMShop2.LOGGER.warn("Regex validation failed for {}: Expected {} but got '{}' ({})",
                    cachedField.translationKey(), regexInfo.value(), text, regexInfo.errorMessage());
            return Optional.empty();
        }
        return Optional.of(text);
    }

    private static ResourceLocation parseResourceLocation(String text) {
        if (text == null || text.isEmpty()) {
            return null;
        }
        return text.contains(":") ? ResourceLocation.tryParse(text) : ResourceLocation.tryBuild("minecraft", text);
    }

    private static boolean isIntermediateText(Class<?> type, String text) {
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

    private static String readFieldAsString(ShopComponent targetComponent, ComponentConfigAccess.CachedField cachedField) {
        try {
            Object value = cachedField.getter().invoke(targetComponent);
            return value != null ? String.valueOf(value) : "";
        } catch (Throwable e) {
            SDMShop2.LOGGER.error("Failed to get {} value", cachedField.translationKey(), e);
            return "";
        }
    }

    private static void invokeUpdate(ShopComponent targetComponent) {
        SDMShopClient.UPDATE_COMPONENT_EVENT.invoker().onUpdateComponentEvent(targetComponent.getRoot(), targetComponent);
    }
}
