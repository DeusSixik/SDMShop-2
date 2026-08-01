package dev.sixik.sdmshop2.libs.shop.client.config;

import com.google.gson.JsonObject;
import com.lowdragmc.lowdraglib.gui.texture.TextTexture;
import com.lowdragmc.lowdraglib.gui.widget.ButtonWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.gui.widget.layout.Layout;
import dev.sixik.sdmshop2.SDMShop2;
import dev.sixik.sdmshop2.libs.shop.base.ShopEntity;
import dev.sixik.sdmshop2.libs.shop.client.SDMShopClient;
import dev.sixik.sdmshop2.libs.shop.client.WidgetGroupAccessor;
import dev.sixik.sdmshop2.libs.shop.client.screens.widgets.CollapsedGroupWidget;
import dev.sixik.sdmshop2.libs.shop.client.textures.ColorRectAndBorderTexture;
import dev.sixik.sdmshop2.libs.shop.components.api.ShopComponent;
import dev.sixik.sdmshop2.libs.shop.components.api.ShopComponentRegistry;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.containers.ModalWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

public class ComponentCollapsedGroupWidget extends CollapsedGroupWidget {

    private static final int CONTEXT_MENU_WIDTH = 120;
    private static final int CONTEXT_MENU_ROW_HEIGHT = 20;

    private static long jsonTooltipVersion;

    static {
        SDMShopClient.UPDATE_COMPONENT_EVENT.register((entity, component) -> jsonTooltipVersion++);
    }

    protected ShopComponent component;
    protected ShopEntity root;
    protected ShopComponent cachedJsonTooltipComponent;
    protected long cachedJsonTooltipVersion = -1L;
    protected List<Component> cachedJsonTooltip;

    public ComponentCollapsedGroupWidget(ShopComponent component, ShopEntity root, int width) {
        super(Component.literal(component.getType().getId().toString()), width);
        this.component = component;
        this.root = root;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int x = getPositionX();
        int y = getPositionY();

        if (isMouseOver(x, y, getSizeWidth(), headerHeight, mouseX, mouseY)) {
            if (canCollapse && button == 0) {
                setCollapsed(!isCollapsed);
                Widget.playButtonClickSound();
                return true;
            }

            if (button == 1) {
                openContextMenu((int) mouseX, (int) mouseY);
                Widget.playButtonClickSound();
                return true;
            }
        }

        if (isCollapsed) return false;
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void drawInForeground(@NonNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.drawInForeground(graphics, mouseX, mouseY, partialTicks);

        int x = getPositionX();
        int y = getPositionY();

        if (!isMouseOver(x, y, getSizeWidth(), headerHeight, mouseX, mouseY)
                || gui == null
                || gui.getModularUIGui() == null) {
            return;
        }

        for (Widget widget : widgets) {
            if (widget instanceof ComponentConfigurationWidget configWidget) {
                ShopComponent tooltipComponent = configWidget.getComponent();
                if (tooltipComponent != null) {
                    gui.getModularUIGui().setHoverTooltip(getJsonTooltip(tooltipComponent), ItemStack.EMPTY, null, null);
                }
                return;
            }
        }
    }

    protected List<Component> getJsonTooltip(ShopComponent tooltipComponent) {
        if (cachedJsonTooltip == null
                || cachedJsonTooltipComponent != tooltipComponent
                || cachedJsonTooltipVersion != jsonTooltipVersion) {
            cachedJsonTooltip = buildJsonTooltip(tooltipComponent);
            cachedJsonTooltipComponent = tooltipComponent;
            cachedJsonTooltipVersion = jsonTooltipVersion;
        }
        return cachedJsonTooltip;
    }

    protected List<Component> buildJsonTooltip(ShopComponent tooltipComponent) {
        List<Component> tooltip = new ArrayList<>();
        tooltip.add(Component.translatable("client.shop.component.editor.json.preview"));

        try {
            JsonObject json = ShopComponentRegistry.toJson(tooltipComponent);
            String formattedJson = SDMShop2.GSON.toJson(json);
            for (String line : formattedJson.split("\n")) {
                tooltip.add(Component.literal("§7" + line.replace("  ", " ")));
            }
        } catch (Exception e) {
            tooltip.add(Component.translatable("client.shop.component.editor.json.generation_error"));
        }

        return tooltip;
    }

    protected void openContextMenu(int mouseX, int mouseY) {
        if (this.gui == null) return;

        WidgetGroup menuRoot = findContextMenuRoot();
        if (menuRoot == null) return;

        closeContextMenus(menuRoot);

        int menuX = mouseX - menuRoot.getPositionX();
        int menuY = mouseY - menuRoot.getPositionY();

        WidgetGroup contextMenu = new ComponentContextMenu(menuRoot, menuX, menuY);

        contextMenu.setDynamicSized(true);
        contextMenu.setLayout(Layout.VERTICAL_LEFT);
        contextMenu.setBackground(new ColorRectAndBorderTexture(0xFF1E1E1E, 1, 0xFF555555));

        ButtonWidget copyButton = new ButtonWidget(
                0,
                0,
                CONTEXT_MENU_WIDTH,
                CONTEXT_MENU_ROW_HEIGHT,
                new TextTexture(() -> I18n.get("client.shop.component.editor.json.copy")),
                button -> {
                    JsonObject json = ShopComponentRegistry.toJson(component);
                    String formattedJson = SDMShop2.GSON.toJson(json);
                    Minecraft.getInstance().keyboardHandler.setClipboard(formattedJson);
                    Minecraft.getInstance().player.sendSystemMessage(Component.translatable("client.shop.component.editor.copied"));
                    menuRoot.removeWidget(contextMenu);
                }
        );
        copyButton.initTemplate();
        contextMenu.addWidget(copyButton);

        ButtonWidget deleteButton = new ButtonWidget(
                0,
                0,
                CONTEXT_MENU_WIDTH,
                CONTEXT_MENU_ROW_HEIGHT,
                new TextTexture(() -> I18n.get("client.shop.component.editor.components.delete")),
                button -> {
                    if (component.getRoot() != null) {
                        component.getRoot().removeComponent(component);
                    }

                    menuRoot.removeWidget(contextMenu);

                    WidgetGroup parentGroup = this.getParent();
                    if (parentGroup != null) {
                        parentGroup.removeWidget(this);
                        ((WidgetGroupAccessor) parentGroup).sdm$onChildSizeUpdate(this);
                    }
                }
        );
        deleteButton.initTemplate();
        contextMenu.addWidget(deleteButton);

        menuRoot.addWidget(contextMenu);
        contextMenu.setFocus(true);
    }

    protected WidgetGroup findContextMenuRoot() {
        Widget current = this;
        while (current != null) {
            if (current instanceof ModalWidget modal) {
                return modal.getPanel();
            }
            current = current.getParent();
        }

        current = this;
        while (current.getParent() != null) {
            current = current.getParent();
        }

        return current instanceof WidgetGroup group ? group : null;
    }

    protected static void closeContextMenus(WidgetGroup root) {
        for (int i = root.widgets.size() - 1; i >= 0; i--) {
            Widget widget = root.widgets.get(i);
            if (widget instanceof ComponentContextMenu) {
                root.removeWidget(widget);
            }
        }
    }

    protected static class ComponentContextMenu extends WidgetGroup {

        protected final WidgetGroup owner;

        protected ComponentContextMenu(WidgetGroup owner, int x, int y) {
            super(x, y, CONTEXT_MENU_WIDTH, 0);
            this.owner = owner;
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            boolean inside = isMouseOverElement(mouseX, mouseY);
            boolean handled = inside && super.mouseClicked(mouseX, mouseY, button);
            if (!inside) {
                owner.removeWidget(this);
                return true;
            }
            return handled;
        }

        @Override
        public void onFocusChanged(Widget lastFocus, Widget focus) {
            if (focus != this && (focus == null || !focus.isParent(this))) {
                owner.removeWidget(this);
            }
        }
    }
}
