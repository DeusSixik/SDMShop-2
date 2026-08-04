package dev.sixik.sdmshop2.libs.shop.scripting;

import com.lowdragmc.lowdraglib.gui.widget.Widget;
import dev.sixik.sdmshop2.libs.sdmeconomy.icons.CurrencyIcon;
import dev.sixik.sdmshop2.libs.sdmeconomy.icons.IconType;
import dev.sixik.sdmshop2.libs.shop.components.api.ConditionComponent;
import dev.sixik.sdmshop2.libs.shop.components.api.IComponentType;
import dev.sixik.sdmshop2.libs.shop.components.api.annotation.ComponentConfig;
import dev.sixik.sdmshop2.libs.shop.scripting.events.ShopScriptEvents;
import dev.sixik.sdmshop2.libs.shop.serializer.ComponentSerializer;
import dev.sixik.sdmshop2.libs.shop.serializer.SerializedComponentType;
import lombok.Getter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

public class ScriptConditionComponent extends ConditionComponent {

    public static final IComponentType<ScriptConditionComponent> TYPE = new Type();

    @Getter
    @ComponentConfig(translationKey = "shop.component.condition.script.script_id")
    private String scripId = "";

    public ScriptConditionComponent() {
    }

    public ScriptConditionComponent(String scripId) {
        setScriptId(scripId);
    }

    @Override
    public boolean isChecked(Player player) {
        return ShopScriptEvents.SCRIP_CONDITION_EVENT.invoker().invoke(player, this, getScriptId());
    }

    public String getScriptId() {
        return scripId;
    }

    public void setScriptId(String scriptId) {
        this.scripId = normalizeScriptId(scriptId);
    }

    public void setScripId(String scriptId) {
        setScriptId(scriptId);
    }

    @Override
    public IComponentType<?> getType() {
        return TYPE;
    }

    @Override
    public boolean verifiedOnClient() {
        return false;
    }

    @Override
    public @Nullable Widget createRender() {
        return null;
    }

    private static class Type extends SerializedComponentType<ScriptConditionComponent> {

        private static final ResourceLocation ID = ResourceLocation.tryBuild("sdm", "condition_script");
        private static final ComponentSerializer<ScriptConditionComponent> SERIALIZER = ComponentSerializer.<ScriptConditionComponent>create()
                .addString("script_id", ScriptConditionComponent::getScriptId, ScriptConditionComponent::setScriptId);

        private Type() {
            super(ScriptConditionComponent::new, SERIALIZER);
        }

        @Override
        public ResourceLocation getId() {
            return ID;
        }

        @Override
        public CurrencyIcon getIcon() {
            return new CurrencyIcon(IconType.ITEM, Items.REPEATING_COMMAND_BLOCK);
        }
    }

    private static String normalizeScriptId(String scriptId) {
        return scriptId == null ? "" : scriptId.trim();
    }
}
