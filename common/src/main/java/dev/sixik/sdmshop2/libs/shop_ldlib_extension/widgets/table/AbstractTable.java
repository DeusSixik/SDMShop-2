package dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.table;

import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.utils.Position;
import com.lowdragmc.lowdraglib.utils.Size;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.sixik.sdmshop2.libs.shop.client.ui.textures.ColorRectAndBorderTexture;
import it.unimi.dsi.fastutil.floats.FloatArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.gui.GuiGraphics;
import org.jspecify.annotations.NonNull;

public abstract class AbstractTable<T, SELF extends AbstractTable<T, SELF>> extends WidgetGroup {

    protected int coll, row;
    protected int cellWidth = 8;
    protected int cellHeight = 8;
    protected int spacingX, spacingY;

    protected final FloatArrayList elementsScales = new FloatArrayList();
    protected final ObjectArrayList<T> elements = new ObjectArrayList<>();

    public AbstractTable() {
        this(1, 1);
    }

    public AbstractTable(int coll, int row) {
        this(coll, row, Position.ORIGIN, Size.ZERO);
    }

    public AbstractTable(int coll, int row, Position selfPosition, Size size) {
        super(selfPosition, size);
        this.coll = Math.max(1, coll);
        this.row = Math.max(1, row);

        setSize(computeTableSize(this.row));
    }

    public AbstractTable(int coll, int row, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.coll = Math.max(1, coll);
        this.row = Math.max(1, row);

        setSize(computeTableSize(this.row));
    }

    public SELF setCollAndRow(int coll, int row) {
        this.coll = Math.max(1, coll);
        this.row = Math.max(1, row);
        setSize(computeTableSize(this.row));
        return self();
    }

    public SELF addElement(T element) {
        return addElement(element, 1f);
    }

    public SELF addElement(T element, float scale) {
        if (element == null) return self();

        elements.add(element);
        elementsScales.add(scale);
        updateTableBounds();
        return self();
    }

    public SELF setElementScale(int index, float scale) {
        elementsScales.set(index, scale);
        return self();
    }

    public SELF setCellSize(int cellWidth, int cellHeight) {
        this.cellWidth = Math.max(0, cellWidth);
        this.cellHeight = Math.max(0, cellHeight);
        updateTableBounds();
        return self();
    }

    public SELF setSpacing(int spacingX, int spacingY) {
        this.spacingX = Math.max(0, spacingX);
        this.spacingY = Math.max(0, spacingY);
        updateTableBounds();
        return self();
    }

    public int getElements() {
        return getTableElementCount();
    }

    protected int getTableElementCount() {
        return elements.size();
    }

    protected int getTableRows() {
        return Math.max(row, (getTableElementCount() + coll - 1) / coll);
    }

    protected Position getCellSelfPosition(int index) {
        int col = index % coll;
        int row = index / coll;
        return new Position(col * (cellWidth + spacingX), row * (cellHeight + spacingY));
    }

    protected int getCellPositionX(int index) {
        return getPositionX() + getCellSelfPosition(index).x;
    }

    protected int getCellPositionY(int index) {
        return getPositionY() + getCellSelfPosition(index).y;
    }

    protected float getElementScale(int index) {
        return index < elementsScales.size() ? elementsScales.getFloat(index) : 1f;
    }

    protected void updateTableBounds() {
        onTableBoundsChanged();
        if (isDynamicSized()) {
            recomputeSize();
        } else {
            setSize(computeTableSize(getTableRows()));
        }
    }

    protected void onTableBoundsChanged() {
    }

    @Override
    protected Size computeDynamicSize() {
        return computeTableSize(getTableRows());
    }

    protected Size computeTableSize(int rows) {
        int width = coll * cellWidth + Math.max(0, coll - 1) * spacingX;
        int height = rows * cellHeight + Math.max(0, rows - 1) * spacingY;
        return new Size(width, height);
    }

    protected void drawTableBackground(@NonNull GuiGraphics graphics, int mouseX, int mouseY) {
        if(backgroundTexture != null) {
            backgroundTexture.draw(graphics, mouseX, mouseY, getPositionX(), getPositionY(), getSizeWidth(), getSizeHeight());
        } else {
            new ColorRectAndBorderTexture().draw(graphics, mouseX, mouseY, getPositionX(), getPositionY(), getSizeWidth(), getSizeHeight());
        }
    }

    @Override
    public void drawInBackground(@NonNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        drawTableBackground(graphics, mouseX, mouseY);

        for (int i = 0; i < elements.size(); i++) {
            T element = elements.get(i);
            int index = i;
            withCellScale(graphics, index, () -> draw(element, graphics, mouseX, mouseY, getCellPositionX(index), getCellPositionY(index), cellWidth, cellHeight));
        }
    }

    protected void withCellScale(GuiGraphics graphics, int index, Runnable renderer) {
        float scale = getElementScale(index);
        if (scale == 1f) {
            renderer.run();
            return;
        }

        int x = getCellPositionX(index);
        int y = getCellPositionY(index);
        float pivotX = x + cellWidth * 0.5f;
        float pivotY = y + cellHeight * 0.5f;

        PoseStack stack = graphics.pose();
        stack.pushPose();
        stack.translate(pivotX, pivotY, 0);
        stack.scale(scale, scale, 1f);
        stack.translate(-pivotX, -pivotY, 0);
        renderer.run();
        stack.popPose();
    }

    protected abstract SELF self();

    protected abstract void draw(T element, GuiGraphics graphics, int mouseX, int mouseY, int x, int y, int cellWidth, int cellHeight);
}
