package dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.containers;

import com.lowdragmc.lowdraglib.utils.Position;
import com.lowdragmc.lowdraglib.utils.Size;

public class VerticalContainer extends LinearContainer<VerticalContainer> {

    public VerticalContainer() {
        super(false);
    }

    public VerticalContainer(Position selfPosition) {
        super(false, selfPosition);
    }

    public VerticalContainer(Position selfPosition, Size size) {
        super(false, selfPosition, size);
    }

    public VerticalContainer(int x, int y, int width, int height) {
        super(false, x, y, width, height);
    }

    @Override
    protected VerticalContainer self() {
        return this;
    }
}
