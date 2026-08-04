package dev.sixik.sdmshop2.libs.shop.client.config;

import com.google.gson.JsonObject;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import dev.sixik.sdmshop2.SDMShop2;
import dev.sixik.sdmshop2.libs.shop.base.ShopEntity;
import dev.sixik.sdmshop2.libs.shop.client.SDMShopClient;
import dev.sixik.sdmshop2.libs.shop.client.WidgetGroupAccessor;
import dev.sixik.sdmshop2.libs.shop.client.ui.elements.ShopEntityEditorElement;
import dev.sixik.sdmshop2.libs.shop.client.ui.elements.ShopScreenElement;
import dev.sixik.sdmshop2.libs.shop.client.ui.widgets.CollapsedGroupWidget;
import dev.sixik.sdmshop2.libs.shop.components.api.ShopComponent;
import dev.sixik.sdmshop2.libs.shop.components.api.ShopComponentRegistry;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.containers.ContextMenuWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

public class ComponentCollapsedGroupWidget extends CollapsedGroupWidget {

    private static final int CONTEXT_MENU_WIDTH = 138;

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

            if (button == 1 && isEditContextMenuEnabled()) {
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
        if (!isEditContextMenuEnabled()) return;

        ShopEntityEditorElement editor = findEntityEditor();

        ContextMenuWidget menu = new ContextMenuWidget(mouseX, mouseY, CONTEXT_MENU_WIDTH);
        menu.setScale(0.75f);
        menu.addItem(Component.translatable("client.shop.component.editor.json.copy"), () -> {
                    JsonObject json = ShopComponentRegistry.toJson(component);
                    String formattedJson = SDMShop2.GSON.toJson(json);
                    Minecraft.getInstance().keyboardHandler.setClipboard(formattedJson);
                    Minecraft.getInstance().player.sendSystemMessage(Component.translatable("client.shop.component.editor.copied"));
                })
                .addSeparator()
                .addItem(Component.translatable("shop.ui.common.duplicate"), editor != null, () -> {
                    if (editor != null) {
                        editor.duplicateComponent(component);
                    }
                })
                .addSeparator()
                .addItem(Component.translatable("shop.ui.common.move_up"), editor != null && editor.canMoveComponent(component, -1), () -> {
                    if (editor != null) {
                        editor.moveComponent(component, -1);
                    }
                })
                .addItem(Component.translatable("shop.ui.common.move_down"), editor != null && editor.canMoveComponent(component, 1), () -> {
                    if (editor != null) {
                        editor.moveComponent(component, 1);
                    }
                })
                .addItem(Component.translatable("shop.ui.common.move_to"), editor != null && editor.getComponentCount() > 1, () -> {
                    if (editor != null) {
                        editor.openMoveComponentModal(component);
                    }
                })
                .addSeparator()
                .addItem(Component.translatable("client.shop.component.editor.components.delete"), () -> {
                    if (component.getRoot() != null) {
                        if (ShopScreenElement.Instance != null && ShopScreenElement.Instance.getEditSession() != null) {
                            ShopScreenElement.Instance.getEditSession().recordHistory(
                                    "component.delete",
                                    root == null ? "unknown" : root.getClass().getSimpleName(),
                                    root instanceof dev.sixik.sdmshop2.libs.shop.base.ObjectIdGetter idGetter ? idGetter.getUUID() : null,
                                    component.getType().getId(),
                                    "Deleted component " + component.getType().getId()
                            );
                        }
                        component.getRoot().removeComponent(component);
                    }

                    WidgetGroup parentGroup = this.getParent();
                    if (parentGroup != null) {
                        parentGroup.removeWidget(this);
                        ((WidgetGroupAccessor) parentGroup).sdm$onChildSizeUpdate(this);
                    }
                });
        ContextMenuWidget.open(this, menu);
    }

    protected boolean isEditContextMenuEnabled() {
        return ShopScreenElement.Instance != null
                && ShopScreenElement.Instance.getEditSession() != null
                && !ShopScreenElement.Instance.getEditSession().closed();
    }

    protected ShopEntityEditorElement findEntityEditor() {
        Widget current = this;
        while (current != null) {
            if (current instanceof ShopEntityEditorElement editor) {
                return editor;
            }
            current = current.getParent();
        }

        return null;
    }
}
