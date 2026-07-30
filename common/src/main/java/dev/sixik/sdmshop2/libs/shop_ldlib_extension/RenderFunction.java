package dev.sixik.sdmshop2.libs.shop_ldlib_extension;

import net.minecraft.client.gui.GuiGraphics;

@FunctionalInterface
public interface RenderFunction {

    void draw(GuiGraphics graphics, int mouseX, int mouseY, float x, float y, int width, int height);
}
