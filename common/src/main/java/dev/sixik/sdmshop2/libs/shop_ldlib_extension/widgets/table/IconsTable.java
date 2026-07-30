package dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.table;

import com.lowdragmc.lowdraglib.utils.Position;
import com.lowdragmc.lowdraglib.utils.Size;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.RenderFunction;
import net.minecraft.client.gui.GuiGraphics;

public class IconsTable extends AbstractTable<RenderFunction, IconsTable> {

    public IconsTable(int coll, int row) {
        super(coll, row);
    }

    public IconsTable(int coll, int row, Position selfPosition, Size size) {
        super(coll, row, selfPosition, size);
    }

    public IconsTable(int coll, int row, int x, int y, int width, int height) {
        super(coll, row, x, y, width, height);
    }

    @Override
    protected IconsTable self() {
        return this;
    }

    @Override
    protected void draw(RenderFunction element, GuiGraphics graphics, int mouseX, int mouseY, int x, int y, int cellWidth, int cellHeight) {
        element.draw(graphics, mouseX, mouseY, x, y, cellWidth, cellHeight);
    }
}
