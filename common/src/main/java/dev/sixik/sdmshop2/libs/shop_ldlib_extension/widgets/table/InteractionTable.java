package dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.table;

import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.utils.Position;
import com.lowdragmc.lowdraglib.utils.Size;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import org.jspecify.annotations.NonNull;

public class InteractionTable extends AbstractTable<Widget, InteractionTable> {

    private boolean recomputingLayout;

    public InteractionTable(int coll, int row) {
        this(coll, row, Position.ORIGIN, Size.ZERO);
    }

    public InteractionTable(int coll, int row, Position selfPosition, Size size) {
        super(coll, row, selfPosition, size);
    }

    public InteractionTable(int coll, int row, int x, int y, int width, int height) {
        super(coll, row, x, y, width, height);
    }

    @Override
    protected InteractionTable self() {
        return this;
    }

    @Override
    public InteractionTable addElement(Widget widget) {
        return addElement(widget, 1f);
    }

    @Override
    public InteractionTable addElement(Widget widget, float scale) {
        return addWidget(widgets.size(), widget, scale);
    }

    @Override
    public InteractionTable addWidget(Widget widget) {
        return addWidget(widgets.size(), widget, 1f);
    }

    @Override
    public InteractionTable addWidget(int index, Widget widget) {
        return addWidget(index, widget, 1f);
    }

    public InteractionTable addWidget(int index, Widget widget, float scale) {
        if (widget == null) return this;

        elementsScales.add(index, scale);
        try {
            super.addWidget(index, widget);
        } catch (RuntimeException exception) {
            elementsScales.removeFloat(index);
            throw exception;
        }

        updateTableBounds();
        return this;
    }

    @Override
    public InteractionTable addWidgets(Widget... widgets) {
        for (Widget widget : widgets) {
            addWidget(widget);
        }
        return this;
    }

    @Override
    public void removeWidget(Widget widget) {
        int index = widgets.indexOf(widget);
        if (index >= 0 && index < elementsScales.size()) {
            elementsScales.removeFloat(index);
        }
        super.removeWidget(widget);
        updateTableBounds();
    }

    @Override
    public void clearAllWidgets() {
        elementsScales.clear();
        super.clearAllWidgets();
        updateTableBounds();
    }

    @Override
    protected int getTableElementCount() {
        return widgets.size();
    }

    @Override
    protected void onTableBoundsChanged() {
        if (recomputingLayout) return;
        recomputingLayout = true;

        try {
            for (int i = 0; i < widgets.size(); i++) {
                Widget widget = widgets.get(i);
                widget.setSelfPosition(getCellSelfPosition(i));
                widget.setSize(cellWidth, cellHeight);
            }
        } finally {
            recomputingLayout = false;
        }
    }

    @Override
    protected void onChildSelfPositionUpdate(Widget child) {
        if (!recomputingLayout) {
            updateTableBounds();
        }
    }

    @Override
    protected void onChildSizeUpdate(Widget child) {
        if (!recomputingLayout) {
            updateTableBounds();
        }
    }

    @Override
    public Widget getHoverElement(double mouseX, double mouseY) {
        for (int i = widgets.size() - 1; i >= 0; i--) {
            Widget widget = widgets.get(i);
            if (!widget.isVisible()) continue;

            Widget hovered = widget.getHoverElement(getScaledMouseX(i, mouseX), getScaledMouseY(i, mouseY));
            if (hovered != null) {
                return hovered;
            }
        }

        return isMouseOverElement(mouseX, mouseY) ? this : null;
    }

    @Override
    public boolean mouseWheelMove(double mouseX, double mouseY, double wheelDelta) {
        for (int i = widgets.size() - 1; i >= 0; i--) {
            Widget widget = widgets.get(i);
            if (widget.isVisible() && widget.isActive()
                    && widget.mouseWheelMove(getScaledMouseX(i, mouseX), getScaledMouseY(i, mouseY), wheelDelta)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (int i = widgets.size() - 1; i >= 0; i--) {
            Widget widget = widgets.get(i);
            if (widget.isVisible() && widget.isActive()
                    && widget.mouseClicked(getScaledMouseX(i, mouseX), getScaledMouseY(i, mouseY), button)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        for (int i = widgets.size() - 1; i >= 0; i--) {
            Widget widget = widgets.get(i);
            if (!widget.isVisible() || !widget.isActive()) continue;

            float scale = getElementScale(i);
            double scaledDragX = scale == 0f ? dragX : dragX / scale;
            double scaledDragY = scale == 0f ? dragY : dragY / scale;
            if (widget.mouseDragged(getScaledMouseX(i, mouseX), getScaledMouseY(i, mouseY), button, scaledDragX, scaledDragY)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        for (int i = widgets.size() - 1; i >= 0; i--) {
            Widget widget = widgets.get(i);
            if (widget.isVisible() && widget.isActive()
                    && widget.mouseReleased(getScaledMouseX(i, mouseX), getScaledMouseY(i, mouseY), button)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean mouseMoved(double mouseX, double mouseY) {
        for (int i = widgets.size() - 1; i >= 0; i--) {
            Widget widget = widgets.get(i);
            if (widget.isVisible() && widget.isActive()
                    && widget.mouseMoved(getScaledMouseX(i, mouseX), getScaledMouseY(i, mouseY))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void drawInBackground(@NonNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        drawTableBackground(graphics, mouseX, mouseY);
        drawWidgetsBackgroundScaled(graphics, mouseX, mouseY, partialTicks);
    }

    @Override
    public void drawInForeground(@NonNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        drawTooltipTexts(mouseX, mouseY);
        drawWidgetsForegroundScaled(graphics, mouseX, mouseY, partialTicks);
    }

    @Override
    public void drawOverlay(@NonNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        if (overlay != null) {
            overlay.draw(graphics, mouseX, mouseY, getPositionX(), getPositionY(), getSizeWidth(), getSizeHeight());
        }

        for (int i = 0; i < widgets.size(); i++) {
            Widget widget = widgets.get(i);
            if (!widget.isVisible()) continue;

            RenderSystem.setShaderColor(1, 1, 1, 1);
            RenderSystem.enableBlend();
            int index = i;
            withCellScale(graphics, index, () -> widget.drawOverlay(graphics, getScaledMouseX(index, mouseX), getScaledMouseY(index, mouseY), partialTicks));
        }
    }

    private void drawWidgetsBackgroundScaled(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        for (int i = 0; i < widgets.size(); i++) {
            Widget widget = widgets.get(i);
            if (!widget.isVisible()) continue;

            RenderSystem.setShaderColor(1, 1, 1, 1);
            RenderSystem.enableBlend();
            int index = i;
            withCellScale(graphics, index, () -> {
                if (widget.inAnimate()) {
                    widget.getAnimation().drawInBackground(graphics, getScaledMouseX(index, mouseX), getScaledMouseY(index, mouseY), partialTicks);
                } else {
                    widget.drawInBackground(graphics, getScaledMouseX(index, mouseX), getScaledMouseY(index, mouseY), partialTicks);
                }
            });
        }
    }

    private void drawWidgetsForegroundScaled(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        for (int i = 0; i < widgets.size(); i++) {
            Widget widget = widgets.get(i);
            if (!widget.isVisible()) continue;

            RenderSystem.setShaderColor(1, 1, 1, 1);
            RenderSystem.enableBlend();
            int index = i;
            withCellScale(graphics, index, () -> {
                if (widget.inAnimate()) {
                    widget.getAnimation().drawInForeground(graphics, getScaledMouseX(index, mouseX), getScaledMouseY(index, mouseY), partialTicks);
                } else {
                    widget.drawInForeground(graphics, getScaledMouseX(index, mouseX), getScaledMouseY(index, mouseY), partialTicks);
                }
            });
        }
    }

    private int getScaledMouseX(int index, double mouseX) {
        float scale = getElementScale(index);
        if (scale == 1f || scale == 0f) {
            return (int) mouseX;
        }

        float pivotX = getCellPositionX(index) + cellWidth * 0.5f;
        return (int) (pivotX + (mouseX - pivotX) / scale);
    }

    private int getScaledMouseY(int index, double mouseY) {
        float scale = getElementScale(index);
        if (scale == 1f || scale == 0f) {
            return (int) mouseY;
        }

        float pivotY = getCellPositionY(index) + cellHeight * 0.5f;
        return (int) (pivotY + (mouseY - pivotY) / scale);
    }

    @Override
    protected void draw(Widget element, GuiGraphics graphics, int mouseX, int mouseY, int x, int y, int cellWidth, int cellHeight) {
    }
}
