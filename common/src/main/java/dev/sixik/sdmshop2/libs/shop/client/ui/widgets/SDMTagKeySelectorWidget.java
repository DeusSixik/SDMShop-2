package dev.sixik.sdmshop2.libs.shop.client.ui.widgets;

import com.lowdragmc.lowdraglib.gui.editor.ColorPattern;
import com.lowdragmc.lowdraglib.gui.widget.DraggableScrollableWidgetGroup;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.gui.widget.layout.Layout;
import com.lowdragmc.lowdraglib.utils.Size;
import dev.sixik.sdmshop2.libs.shop.client.ui.textures.ColorRectAndBorderTexture;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.ButtonWidget;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.InputTextBox;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.TextLabel;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.containers.ModalWidget;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

public class SDMTagKeySelectorWidget extends WidgetGroup {

    private static final int HEIGHT = 20;
    private static final int BUTTON_WIDTH = 20;
    private static final int GAP = 2;
    private static final int PICKER_WIDTH = 340;
    private static final int PICKER_HEIGHT = 260;
    private static final int PICKER_PADDING = 4;
    private static final int PICKER_SEARCH_HEIGHT = 20;
    private static final int PICKER_ROW_HEIGHT = 20;
    private static final int PICKER_SCROLLBAR_WIDTH = 4;

    private final TargetRegistry targetRegistry;
    private final ButtonWidget pickerButton;
    private final InputTextBox tagField;

    private @Nullable TagKey<?> tagKey;
    private @Nullable Consumer<TagKey<?>> onTagKeyUpdate;

    public SDMTagKeySelectorWidget(int x, int y, int width, TargetRegistry targetRegistry) {
        super(x, y, width, HEIGHT);
        this.targetRegistry = targetRegistry == null ? TargetRegistry.ITEM : targetRegistry;
        setClientSideWidget();
        setLayout(Layout.NONE);
        setDynamicSized(false);

        pickerButton = new ButtonWidget(0, 0, BUTTON_WIDTH, HEIGHT, Component.literal("#"), ignored -> openTagPicker());
        styleButton(pickerButton);
        pickerButton.setHoverTooltips(Component.translatable("shop.ui.tag_selector.open"));
        pickerButton.setClientSideWidget();

        tagField = new InputTextBox(BUTTON_WIDTH + GAP, 0, getTagFieldWidth(width), HEIGHT);
        styleInput(tagField);
        tagField.setResourceLocationOnly();
        tagField.setPlaceholder(Component.translatable("shop.ui.tag_selector.search.placeholder"));
        tagField.setTextResponder(this::updateFromText);
        tagField.setClientSideWidget();

        addWidget(pickerButton);
        addWidget(tagField);
    }

    @Override
    public void setSize(Size size) {
        super.setSize(size);
        pickerButton.setSelfPosition(0, 0);
        pickerButton.setSize(BUTTON_WIDTH, HEIGHT);
        tagField.setSelfPosition(BUTTON_WIDTH + GAP, 0);
        tagField.setSize(getTagFieldWidth(size.width), HEIGHT);
    }

    public @Nullable TagKey<?> getTagKey() {
        return tagKey;
    }

    public SDMTagKeySelectorWidget setTagKey(@Nullable TagKey<?> tagKey) {
        this.tagKey = tagKey;
        tagField.setCurrentStringSilently(tagKey == null ? "" : tagKey.location().toString());
        return this;
    }

    public SDMTagKeySelectorWidget setOnTagKeyUpdate(@Nullable Consumer<TagKey<?>> onTagKeyUpdate) {
        this.onTagKeyUpdate = onTagKeyUpdate;
        return this;
    }

    private int getTagFieldWidth(int width) {
        return Math.max(1, width - BUTTON_WIDTH - GAP);
    }

    private void updateFromText(String value) {
        ResourceLocation location = parseLocation(value);
        if (location == null) return;

        TagKey<?> next = createTagKey(location);
        if (!next.equals(tagKey)) {
            tagKey = next;
            onUpdate();
        }
    }

    private void openTagPicker() {
        ModalWidget modal = new ModalWidget(PICKER_WIDTH, PICKER_HEIGHT)
                .setTitle(Component.translatable(targetRegistry.titleKey()));
        ModalWidget opened = ModalWidget.openNested(this, modal);
        if (opened == null) return;

        int contentWidth = Math.max(1, opened.getContentWidth());
        int contentHeight = Math.max(1, opened.getContentHeight());
        int listY = PICKER_SEARCH_HEIGHT + PICKER_PADDING;
        int listHeight = Math.max(1, contentHeight - listY);
        int listWidth = Math.max(1, contentWidth - PICKER_SCROLLBAR_WIDTH - 2);

        InputTextBox search = new InputTextBox(0, 0, contentWidth, PICKER_SEARCH_HEIGHT);
        styleInput(search);
        search.setPlaceholder(Component.translatable("shop.ui.tag_selector.search.placeholder"));
        search.setClientSideWidget();

        DraggableScrollableWidgetGroup scroll = new DraggableScrollableWidgetGroup(0, listY, contentWidth, listHeight);
        scroll.setScrollWheelDirection(DraggableScrollableWidgetGroup.ScrollWheelDirection.VERTICAL);
        scroll.setLayout(Layout.NONE);
        scroll.setYScrollBarWidth(PICKER_SCROLLBAR_WIDTH);
        scroll.setYBarStyle(null, ColorPattern.WHITE.rectTexture().setRadius(2));
        scroll.setClientSideWidget();

        WidgetGroup list = new WidgetGroup(0, 0, listWidth, 1);
        list.setLayout(Layout.NONE);
        list.setDynamicSized(false);
        scroll.addWidget(list);

        final Runnable[] rebuild = new Runnable[1];
        rebuild[0] = () -> rebuildTagList(opened, list, listWidth, search.getCurrentString());
        search.setTextResponder(ignored -> rebuild[0].run());

        opened.addWidget(search);
        opened.addWidget(scroll);
        rebuild[0].run();
    }

    private void rebuildTagList(ModalWidget modal, WidgetGroup list, int listWidth, @Nullable String query) {
        list.clearAllWidgets();

        String filter = query == null ? "" : query.trim().toLowerCase(Locale.ROOT);
        int y = 0;

        for (TagOption option : collectTagOptions()) {
            if (!filter.isEmpty() && !option.searchText().contains(filter)) continue;

            ButtonWidget row = new ButtonWidget(0, y, listWidth, PICKER_ROW_HEIGHT, option.label(), ignored -> {
                setTagKey(option.tagKey());
                onUpdate();
                modal.close();
            });
            styleButton(row);
            row.setTextPadding(4);
            row.setHoverTooltips(option.tooltip());
            row.setClientSideWidget();
            list.addWidget(row);
            y += PICKER_ROW_HEIGHT + 2;
        }

        if (list.widgets.isEmpty()) {
            TextLabel empty = new TextLabel(Component.translatable("shop.ui.tag_selector.empty"))
                    .setAutoSize(false)
                    .setPadding(4, 0)
                    .setOverflowMode(TextLabel.OverflowMode.ELLIPSIS)
                    .alignMiddle();
            empty.setSelfPosition(0, 0);
            empty.setSize(listWidth, PICKER_ROW_HEIGHT);
            list.addWidget(empty);
            y = PICKER_ROW_HEIGHT;
        }

        list.setSize(listWidth, Math.max(1, y));
    }

    private List<TagOption> collectTagOptions() {
        List<TagOption> tags = new ArrayList<>();

        if (targetRegistry == TargetRegistry.ITEM) {
            BuiltInRegistries.ITEM.getTags().forEach(entry -> tags.add(createItemOption(entry.getFirst(), entry.getSecond())));
        } else {
            BuiltInRegistries.BLOCK.getTags().forEach(entry -> tags.add(createBlockOption(entry.getFirst(), entry.getSecond())));
        }

        tags.sort(Comparator.comparing(option -> option.tagKey().location().toString()));
        return tags;
    }

    private TagOption createItemOption(TagKey<Item> tagKey, HolderSet.Named<Item> holders) {
        List<String> samples = new ArrayList<>();
        StringBuilder search = new StringBuilder(tagKey.location().toString());

        holders.stream().limit(8).forEach(holder -> {
            Item item = holder.value();
            ResourceLocation id = BuiltInRegistries.ITEM.getKey(item);
            String name = item.getDefaultInstance().getHoverName().getString();
            samples.add(name);
            search.append(' ').append(id).append(' ').append(name);
        });

        return createOption(tagKey, holders.size(), samples, search.toString());
    }

    private TagOption createBlockOption(TagKey<Block> tagKey, HolderSet.Named<Block> holders) {
        List<String> samples = new ArrayList<>();
        StringBuilder search = new StringBuilder(tagKey.location().toString());

        holders.stream().limit(8).forEach(holder -> {
            Block block = holder.value();
            ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
            String name = block.getName().getString();
            samples.add(name);
            search.append(' ').append(id).append(' ').append(name);
        });

        return createOption(tagKey, holders.size(), samples, search.toString());
    }

    private TagOption createOption(TagKey<?> tagKey, int size, List<String> samples, String searchText) {
        Component label = Component.literal("#" + tagKey.location() + " §8(" + size + ")");
        Component tooltip = samples.isEmpty()
                ? Component.literal("#" + tagKey.location())
                : Component.literal("#" + tagKey.location() + "\n" + String.join(", ", samples));
        return new TagOption(tagKey, label, tooltip, searchText.toLowerCase(Locale.ROOT));
    }

    private TagKey<?> createTagKey(ResourceLocation location) {
        return targetRegistry == TargetRegistry.ITEM
                ? TagKey.create(Registries.ITEM, location)
                : TagKey.create(Registries.BLOCK, location);
    }

    private void onUpdate() {
        if (tagKey != null && onTagKeyUpdate != null) {
            onTagKeyUpdate.accept(tagKey);
        }
    }

    private @Nullable ResourceLocation parseLocation(String value) {
        if (value == null || value.isBlank()) return null;
        String normalized = value.trim();
        if (normalized.startsWith("#")) {
            normalized = normalized.substring(1);
        }
        return normalized.contains(":")
                ? ResourceLocation.tryParse(normalized)
                : ResourceLocation.tryBuild("minecraft", normalized);
    }

    private void styleInput(InputTextBox input) {
        input.setBackground(new ColorRectAndBorderTexture(0xFF101016, 0xFF5C637A, 1).setRadius(2));
        input.setFocusedOutline(0xFF101016, 0xFFFFFFFF);
        input.setTextColor(0xFFE8E8F0);
        input.setPadding(4, 1);
    }

    private void styleButton(ButtonWidget button) {
        button.setButtonColors(0xFF2A2A36, 0xFF3D3D4E);
        button.setHoverColors(0xFF34384A, 0xFF7986CB);
        button.setClickedColors(0xFF202331, 0xFF7986CB);
        button.setTextColor(0xFFFFFFFF);
        button.setTextPadding(3);
        button.setMinTextScale(0.35f);
    }

    private record TagOption(TagKey<?> tagKey, Component label, Component tooltip, String searchText) {
    }

    public enum TargetRegistry {
        ITEM("shop.ui.tag_selector.item.title"),
        BLOCK("shop.ui.tag_selector.block.title");

        private final String titleKey;

        TargetRegistry(String titleKey) {
            this.titleKey = titleKey;
        }

        private String titleKey() {
            return titleKey;
        }
    }
}