package dev.sixik.sdmshop2.libs.shop.client.ui.api;

import dev.sixik.sdmshop2.libs.shop.client.ui.widgets.ShopRenderWidgets;
import dev.sixik.sdmshop2.libs.shop.components.api.CostComponent;
import dev.sixik.unigui.api.widget.Widget;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.Nullable;

import java.util.List;

/** Read-only presentation contract shared by catalog cards and purchase dialogs. */
public interface ShopOfferView {
    /**
     * @return visible offer title.
     */
    String getTitle();

    /**
     * @return factories for visible components in offer order; widgets are never cached here.
     */
    List<ShopRenderElement> getRenderElements();

    /**
     * @return alternative payment groups in display order.
     */
    List<CostGroup> getCostGroups();

    /**
     * Payment metadata. Real component visuals come from createRender; icon fields only adapt
     * legacy previews.
     *
     * @param itemIcon item icon, or null.
     * @param textureIcon texture icon, or null.
     * @param textIcon text symbol, or null.
     * @param amount formatted amount.
     * @param component originating cost, or null for a preview.
     */
    record CostEntry(
            @Nullable ItemStack itemIcon,
            @Nullable ResourceLocation textureIcon,
            @Nullable String textIcon,
            String amount,
            @Nullable CostComponent component) {
        public CostEntry(
                @Nullable ItemStack itemIcon,
                @Nullable String textIcon,
                String amount,
                @Nullable CostComponent component) {
            this(itemIcon, null, textIcon, amount, component);
        }

        public CostEntry(ItemStack itemIcon, String amount) {
            this(itemIcon, null, null, amount, null);
        }

        public CostEntry(String textIcon, String amount) {
            this(null, null, textIcon, amount, null);
        }

        public CostEntry(ItemStack itemIcon, @Nullable String textIcon, String amount) {
            this(itemIcon, null, textIcon, amount, null);
        }

        /**
         * @return a fresh component-owned payment widget, or null to omit its visuals.
         */
        public Widget createRender() {
            return component != null
                    ? component.createRender()
                    : ShopRenderWidgets.legacyCost(this);
        }

        public ItemStack icon() {
            return itemIcon != null ? itemIcon : ItemStack.EMPTY;
        }
    }

    /**
     * One alternative payment group with costs in display order.
     *
     * @param groupId server payment-group identifier.
     * @param costs costs that must be paid together.
     */
    record CostGroup(String groupId, List<CostEntry> costs) {
        /**
         * Takes an immutable snapshot of the supplied cost list.
         *
         * @param groupId server payment-group identifier; null means the default group.
         * @param costs costs that must be paid together.
         */
        public CostGroup {
            groupId = groupId != null ? groupId : "";
            costs = List.copyOf(costs);
        }
    }
}
