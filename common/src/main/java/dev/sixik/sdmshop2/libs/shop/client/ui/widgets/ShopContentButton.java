package dev.sixik.sdmshop2.libs.shop.client.ui.widgets;

import dev.sixik.unigui.api.input.MouseCursor;
import dev.sixik.unigui.api.layout.LayoutContext;
import dev.sixik.unigui.api.math.MutableRect;
import dev.sixik.unigui.api.math.RectView;
import dev.sixik.unigui.api.widget.Widget;
import dev.sixik.unigui.widgets.containers.Box;
import dev.sixik.unigui.widgets.interaction.Button;

import org.jetbrains.annotations.Nullable;

/**
 * Button with decorative widget content and a single pointer target for the entire row. The content
 * must not contain interactive controls: its subtree is excluded from hit testing.
 */
public class ShopContentButton extends Button {
    /** Decorative content measured and arranged inside the button padding. */
    private final @Nullable Widget content;

    /** Non-interactive host keeps decorative children visually enabled. */
    private final Box contentHost = new Box();

    /**
     * Creates a button whose content cannot intercept hover, clicks or the pointer cursor.
     *
     * @param content decorative row, or null for an empty button.
     */
    public ShopContentButton(@Nullable Widget content) {
        super("");
        this.content = content;
        mouseCursor(MouseCursor.POINTER);
        if (content != null) {
            // UniGUI hit-tests the deepest enabled child and sends hover only to that target.
            contentHost.enabled(false);
            contentHost.themeEnabled(false);
            contentHost.boxVisualEnabled(false);
            contentHost.addChild(content);
            addChild(contentHost);
        }
    }

    /**
     * @return decorative content, or null for an empty button.
     */
    public @Nullable Widget getContent() {
        return content;
    }

    /**
     * Measures the row plus the button's text padding.
     *
     * @param context available logical UI size.
     */
    @Override
    public void measure(LayoutContext context) {
        super.measure(context);
        if (content == null) return;
        content.measure(context);
        float width = content.desiredSize().width() + textPaddingX() * 2;
        float height =
                Math.max(
                        intrinsicTextHeight(), content.desiredSize().height() + textPaddingY() * 2);
        setDesiredSize(resolveDesiredSize(context, width, height));
    }

    /**
     * Places content inside the button padding.
     *
     * @param bounds button rectangle in logical UI coordinates.
     */
    @Override
    public void arrange(RectView bounds) {
        super.arrange(bounds);
        if (content == null) return;
        float padX = textPaddingX();
        float padY = textPaddingY();
        MutableRect contentBounds =
                new MutableRect(
                        bounds.x() + padX,
                        bounds.y() + padY,
                        Math.max(0.0f, bounds.width() - padX * 2),
                        Math.max(0.0f, bounds.height() - padY * 2));
        contentHost.arrange(contentBounds);
        content.arrange(contentBounds);
    }
}
