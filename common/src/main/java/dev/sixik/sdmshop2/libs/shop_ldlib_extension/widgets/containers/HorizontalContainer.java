package dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.containers;

import com.lowdragmc.lowdraglib.utils.Position;
import com.lowdragmc.lowdraglib.utils.Size;

public class HorizontalContainer extends LinearContainer<HorizontalContainer> {

    public HorizontalContainer() {
        super(true);
    }

    public HorizontalContainer(Position selfPosition) {
        super(true, selfPosition);
    }

    public HorizontalContainer(Position selfPosition, Size size) {
        super(true, selfPosition, size);
    }

    public HorizontalContainer(int x, int y, int width, int height) {
        super(true, x, y, width, height);
    }

    public HorizontalContainer alignTop() {
        return setCrossAxisAlignment(CrossAxisAlignment.START);
    }

    public HorizontalContainer alignMiddle() {
        return setCrossAxisAlignment(CrossAxisAlignment.CENTER);
    }

    public HorizontalContainer alignBottom() {
        return setCrossAxisAlignment(CrossAxisAlignment.END);
    }

    @Override
    protected HorizontalContainer self() {
        return this;
    }
}
