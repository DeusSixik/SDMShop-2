package dev.sixik.sdmshop2.libs.shop.client.ui;

import dev.sixik.sdmshop2.libs.shop.client.ui.api.ShopScreenElement;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.ShopUiContext;
import dev.sixik.sdmshop2.libs.shop.client.ui.root.ShopRootPanel;
import dev.sixik.sdmshop2.libs.shop.client.ui.root.ShopToolPanel;
import dev.sixik.unigui.api.screen.UniScreen;
import dev.sixik.unigui.widgets.containers.VBox;
import dev.sixik.unigui.widgets.feedback.OverlayLayer;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/** One shop window with its own widgets, filters, overlays and lifecycle. */
public final class ShopScreen implements ShopUiContext, AutoCloseable {
    /** Catalog belonging to this window. */
    private final ShopRootPanel rootPanel = new ShopRootPanel(this);

    /** Toolbar belonging to this window. */
    private final ShopToolPanel toolPanel = new ShopToolPanel(this);

    /** Contributions in mounting order. */
    private final List<ShopScreenElement> elements = new ArrayList<>();

    /** Root overlay while mounted. */
    private @Nullable OverlayLayer overlay;

    /** Lazily initialized backend screen. */
    private @Nullable UniScreen screen;

    /** Prevents reusing disposed widgets. */
    private boolean disposed;

    /** Creates a window containing the standard toolbar and catalog. */
    public ShopScreen() {
        this(List.of());
    }

    /**
     * Creates fresh contributions for this window.
     *
     * @param factories contribution factories in registration order.
     */
    private ShopScreen(List<Function<ShopUiContext, ShopScreenElement>> factories) {
        elements.add(toolPanel);
        elements.add(rootPanel);
        for (Function<ShopUiContext, ShopScreenElement> factory : factories) {
            elements.add(Objects.requireNonNull(factory.apply(this), "element"));
        }
        elements.sort(Comparator.comparingInt(ShopScreenElement::priority));
    }

    /**
     * @return a builder for independent windows with contributed elements.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * @return catalog belonging to this window.
     */
    public ShopRootPanel rootPanel() {
        return rootPanel;
    }

    /**
     * @return toolbar belonging to this window.
     */
    public ShopToolPanel toolPanel() {
        return toolPanel;
    }

    /**
     * Mounts the widget tree once without opening a backend GUI.
     *
     * @return backend screen description.
     * @throws IllegalStateException if this window was disposed.
     */
    public UniScreen createScreen() {
        if (disposed) throw new IllegalStateException("Shop screen has been disposed");
        if (screen == null) {
            VBox layout = new VBox();
            layout.layout(style -> style.fill().padding(4.0f).gap(4.0f));
            overlay = new OverlayLayer(layout);
            try {
                for (ShopScreenElement element : elements) element.initWidget(layout, this);
                screen = new UniScreen(overlay).onClose(this::close);
            } catch (RuntimeException | Error failure) {
                try {
                    close();
                } catch (RuntimeException cleanupFailure) {
                    failure.addSuppressed(cleanupFailure);
                }
                throw failure;
            }
        }
        return screen;
    }

    /** Opens this window through the installed UniGUI backend. */
    public void open() {
        createScreen().openGui();
    }

    /** {@inheritDoc} */
    @Override
    public @Nullable OverlayLayer overlay() {
        return overlay;
    }

    /** {@inheritDoc} */
    @Override
    public void search(String query) {
        rootPanel.setSearchQuery(query);
    }

    /** {@inheritDoc} */
    @Override
    public void toggleFavorites() {
        toolPanel.setFavoritesActive(rootPanel.toggleFavoritesFirst());
    }

    /** Releases this tree once; invoked when the backend closes its GUI. */
    @Override
    public void close() {
        if (disposed) return;
        disposed = true;
        RuntimeException failure = null;
        for (int i = elements.size() - 1; i >= 0; i--) {
            try {
                elements.get(i).dispose();
            } catch (RuntimeException ex) {
                if (failure == null) failure = ex;
                else failure.addSuppressed(ex);
            }
        }
        if (overlay != null) {
            try {
                overlay.dispose();
            } catch (RuntimeException ex) {
                if (failure == null) failure = ex;
                else failure.addSuppressed(ex);
            }
        }
        overlay = null;
        screen = null;
        if (failure != null) throw failure;
    }

    /** Reusable configuration that creates independent contributions per window. */
    public static final class Builder {
        /** Factories in registration order. */
        private final List<Function<ShopUiContext, ShopScreenElement>> factories =
                new ArrayList<>();

        /** Creates an empty contribution list. */
        private Builder() {}

        /**
         * Registers an element factory.
         *
         * @param factory factory receiving the owning window's context.
         * @return this builder.
         * @throws NullPointerException if factory is null.
         */
        public Builder element(Function<ShopUiContext, ShopScreenElement> factory) {
            factories.add(Objects.requireNonNull(factory, "factory"));
            return this;
        }

        /**
         * @return a fresh unmounted window.
         */
        public ShopScreen build() {
            return new ShopScreen(List.copyOf(factories));
        }
    }
}
