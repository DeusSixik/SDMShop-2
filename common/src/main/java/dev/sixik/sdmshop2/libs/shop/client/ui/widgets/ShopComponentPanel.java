package dev.sixik.sdmshop2.libs.shop.client.ui.widgets;

import dev.sixik.sdmshop2.libs.shop.client.ui.api.ShopRenderElement;
import dev.sixik.unigui.api.layout.Align;
import dev.sixik.unigui.api.layout.Justify;
import dev.sixik.unigui.api.layout.Overflow;
import dev.sixik.unigui.api.widget.Widget;
import dev.sixik.unigui.widgets.containers.HBox;
import dev.sixik.unigui.widgets.containers.LinearBox;
import dev.sixik.unigui.widgets.containers.PanelWidget;
import dev.sixik.unigui.widgets.containers.ScrollView;
import dev.sixik.unigui.widgets.containers.WrapPanel;
import dev.sixik.unigui.widgets.core.Orientation;
import dev.sixik.unigui.widgets.feedback.OverlayLayer;

import org.jetbrains.annotations.Nullable;

import java.util.List;

/** Mounts arbitrary component widgets without interpreting their type, data or visual layout. */
public final class ShopComponentPanel extends LinearBox {
    /** Prevents repeated container disposal from disposing add-on widgets twice. */
    private boolean disposed;

    /** Single row of component widgets, centered inside the available viewport. */
    private final HBox content = new HBox();

    /** Badges mounted below the products and above the separator. */
    private final WrapPanel badges = new WrapPanel().spacing(3).lineSpacing(3);

    /**
     * Creates a fresh widget for every visible element in encounter order.
     *
     * @param elements component factories; null widgets are skipped.
     */
    public ShopComponentPanel(List<ShopRenderElement> elements) {
        this(elements, null);
    }

    /**
     * Mounts content horizontally and routes component-owned badges to the requested area.
     *
     * @param elements component factories; null widgets are skipped.
     * @param badgeHost separator area, or null to display badges above this panel's content.
     */
    public ShopComponentPanel(List<ShopRenderElement> elements, @Nullable PanelWidget badgeHost) {
        this(elements, badgeHost, 0);
    }

    /**
     * Creates a component view with an optional fixed-height, horizontally scrolling viewport.
     *
     * @param elements factories creating fresh component widgets.
     * @param badgeHost separate badge row, or null to include badges in this panel.
     * @param viewportHeight viewport height in logical pixels; zero uses natural content size.
     */
    public ShopComponentPanel(
            List<ShopRenderElement> elements,
            @Nullable PanelWidget badgeHost,
            float viewportHeight) {
        super(Orientation.VERTICAL);
        themeEnabled(false);
        boxVisualEnabled(false);
        layout(style -> style.flexNone().minWidth(0).gap(4));
        content.layout(
                style ->
                        style.flexNone()
                                .alignItems(Align.CENTER)
                                .justifyContent(Justify.CENTER)
                                .gap(4));
        badges.layout(
                style -> style.widthPercent(100).minWidth(0).flexNone().alignItems(Align.CENTER));
        try {
            for (ShopRenderElement element : elements) {
                Widget widget = element.createRender();
                if (widget == null) continue;
                if (widget.parent() != null)
                    throw new IllegalStateException(
                            "createRender must return a fresh, unparented widget");
                if (widget instanceof ShopBadge) badges.addChild(widget);
                else content.addChild(widget);
            }
            content.applyQueuedMutations();
            badges.applyQueuedMutations();
            if (!badges.children().isEmpty()) {
                if (badgeHost != null) badgeHost.addChild(badges);
                else addChild(badges);
            }
            if (viewportHeight > 0) {
                ScrollView viewport =
                        new ScrollView(content)
                                .overlayScrollBars(false)
                                .scrollbarSize(4)
                                .scrollbarGap(2)
                                .scrollStep(20);
                viewport.layout(
                        style ->
                                style.widthPercent(100)
                                        .height(viewportHeight)
                                        .minWidth(0)
                                        .flexNone()
                                        .overflowX(Overflow.AUTO)
                                        .overflowY(Overflow.HIDDEN));
                addChild(viewport);
            } else if (!content.children().isEmpty()) addChild(content);
            applyQueuedMutations();
        } catch (RuntimeException | Error failure) {
            try {
                dispose();
            } catch (RuntimeException cleanup) {
                failure.addSuppressed(cleanup);
            }
            throw failure;
        }
    }

    /**
     * Attaches nested item tooltips to the actual owning surface.
     *
     * @param owner overlay, or null to detach tooltips.
     */
    public void attachTooltips(@Nullable OverlayLayer owner) {
        visit(this, owner);
        visit(content, owner);
        visit(badges, owner);
    }

    /**
     * Walks a complete widget tree, including queued children created by add-ons.
     *
     * @param widget subtree root.
     * @param owner tooltip overlay, or null to detach.
     */
    private static void visit(Widget widget, @Nullable OverlayLayer owner) {
        if (widget instanceof PanelWidget panel) panel.applyQueuedMutations();
        if (widget instanceof ShopItemWidget item) {
            if (owner == null) item.detachTooltip(null);
            else item.attachTooltip(owner);
        }
        for (Widget child : widget.children()) visit(child, owner);
    }

    /** Detaches nested tooltips and disposes every component-owned widget. */
    @Override
    public void dispose() {
        if (disposed) return;
        disposed = true;
        attachTooltips(null);
        content.dispose();
        badges.dispose();
        super.dispose();
    }
}
