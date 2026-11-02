package dev.sixik.sdmshop2.libs.shop.client.ui.elements;

import dev.sixik.sdmshop2.libs.shop.client.ui.api.ShopUiElement;
import dev.sixik.sdmshop2.libs.shop.client.ui.events.ShopUIEvents;
import dev.sixik.sdmshop2.libs.shop.components.misc.CatalogComponent;
import dev.sixik.unigui.widgets.interaction.ToggleButton;
import lombok.Getter;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.Nullable;

/** UniGUI category toggle retained under the historical public class name. */
public class ShopTabElement extends ToggleButton implements ShopUiElement {
    public static final Component DEFAULT_TITLE = Component.translatable("shop.ui.common.no_title");
    public static final Component ALL_TITLE = Component.translatable("shop.ui.tabs.button.all");
    @Getter private final @Nullable CatalogComponent component;
    private final @Nullable ShopTabsPanelElement panel;

    public ShopTabElement(@Nullable CatalogComponent component) { this(null, component); }
    public ShopTabElement(@Nullable ShopTabsPanelElement panel, @Nullable CatalogComponent component) {
        super((component == null ? ALL_TITLE : Component.literal(component.getId())).getString());
        this.panel = panel;
        this.component = component;
        onClick(event -> ShopUIEvents.invokeSelectCategory(component));
    }

    public ShopTabElement setSelected(boolean selected) { checked(selected); return this; }
    public boolean isSelected() { return checked(); }
    @Override public void alightWidget() { }
}
