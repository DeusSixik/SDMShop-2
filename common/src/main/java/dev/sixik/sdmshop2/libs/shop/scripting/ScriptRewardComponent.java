package dev.sixik.sdmshop2.libs.shop.scripting;

import com.lowdragmc.lowdraglib.gui.widget.Widget;
import dev.sixik.sdmshop2.libs.sdmeconomy.icons.CurrencyIcon;
import dev.sixik.sdmshop2.libs.sdmeconomy.icons.IconType;
import dev.sixik.sdmshop2.libs.shop.components.api.IComponentType;
import dev.sixik.sdmshop2.libs.shop.components.api.RewardComponent;
import dev.sixik.sdmshop2.libs.shop.components.api.annotation.ComponentConfig;
import dev.sixik.sdmshop2.libs.shop.scripting.events.ShopScriptEvents;
import dev.sixik.sdmshop2.libs.shop.serializer.ComponentSerializer;
import dev.sixik.sdmshop2.libs.shop.serializer.SerializedComponentType;
import lombok.Getter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

public class ScriptRewardComponent extends RewardComponent {

    public static final IComponentType<ScriptRewardComponent> TYPE = new Type();

    @Getter
    @ComponentConfig(translationKey = "shop.component.reward.script.script_id")
    private String scripId = "";

    public ScriptRewardComponent() {
    }

    public ScriptRewardComponent(String scripId) {
        setScriptId(scripId);
    }

    @Override
    public void reward(ServerPlayer player, int amount) {
        ShopScriptEvents.SCRIP_REWARD_EVENT.invoker().invoke(player, amount, this, getScriptId());
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
    public @Nullable Widget createRender() {
        return null;
    }

    private static class Type extends SerializedComponentType<ScriptRewardComponent> {

        private static final ResourceLocation ID = ResourceLocation.tryBuild("sdm", "reward_script");
        private static final ComponentSerializer<ScriptRewardComponent> SERIALIZER = ComponentSerializer.<ScriptRewardComponent>create()
                .addString("script_id", ScriptRewardComponent::getScriptId, ScriptRewardComponent::setScriptId);

        private Type() {
            super(ScriptRewardComponent::new, SERIALIZER);
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
