package dev.sixik.sdmshop2.libs.shop.components;

import com.lowdragmc.lowdraglib.gui.texture.ItemStackTexture;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import dev.sixik.sdmshop2.libs.sdmeconomy.icons.CurrencyIcon;
import dev.sixik.sdmshop2.libs.sdmeconomy.icons.IconType;
import dev.sixik.sdmshop2.libs.shop.client.screens.widgets.ShopEmptyWidget;
import dev.sixik.sdmshop2.libs.shop.components.api.IComponentType;
import dev.sixik.sdmshop2.libs.shop.components.api.RewardComponent;
import dev.sixik.sdmshop2.libs.shop.components.api.annotation.ComponentConfig;
import dev.sixik.sdmshop2.libs.shop.serializer.ComponentSerializer;
import dev.sixik.sdmshop2.libs.shop.serializer.SerializedComponentType;
import lombok.Getter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

public class CommandRewardComponent extends RewardComponent {

    public static final String EMPTY = "Command";
    public static final String DEFAULT_COMMAND = "/time set day";

    public static final IComponentType<CommandRewardComponent> TYPE = new Type();

    protected static final ItemStackTexture DEFAULT_TEXTURE = new ItemStackTexture(Items.COMMAND_BLOCK);

    @Getter
    @ComponentConfig(translationKey = "shop.component.reward.command.display_name")
    private String displayName;

    @Getter
    @ComponentConfig(translationKey = "shop.component.reward.command.command")
    private String command;

    public CommandRewardComponent() {
        this(DEFAULT_COMMAND, EMPTY);
    }

    public CommandRewardComponent(String command, String displayName) {
        this.command = command;
        this.displayName = displayName;
    }

    @Override
    public void reward(ServerPlayer player, int amount) {
        CommandSourceStack source = player.createCommandSourceStack();
        source.withPermission(2);
        source.withSuppressedOutput();

        String format = formatCommand(command, player);
        for (int i = 0; i < amount; i++) {
            player.getServer().getCommands().performPrefixedCommand(source, format);
        }
    }

    @Override
    public IComponentType<?> getType() {
        return TYPE;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public @Nullable Widget createRender() {
        return new ShopEmptyWidget().setBackground(DEFAULT_TEXTURE)
                .setHoverTooltips(Component.translatable("shop.ui.offer_element.component.reward.command", command));
    }

    private static class Type extends SerializedComponentType<CommandRewardComponent> {

        private static final ResourceLocation ID = ResourceLocation.tryBuild("sdm", "reward_command");
        private static final ComponentSerializer<CommandRewardComponent> SERIALIZER = ComponentSerializer.<CommandRewardComponent>create()
                .addDefaultedString("name", CommandRewardComponent::getDisplayName, (component, value) -> component.displayName = value, EMPTY)
                .addDefaultedString("command", CommandRewardComponent::getCommand, (component, value) -> component.command = value, DEFAULT_COMMAND, false);

        private Type() {
            super(CommandRewardComponent::new, SERIALIZER);
        }

        @Override
        public ResourceLocation getId() {
            return ID;
        }

        @Override
        public CommandRewardComponent createFromBuilder(Object... args) {
            if(args.length != 2)
                throw new IllegalArgumentException("CommandRewardComponent.createFromBuilder() takes 2 arguments (String, String)");

            return new CommandRewardComponent((String) args[0], (String) args[1]);
        }

        @Override
        public CurrencyIcon getIcon() {
            return new CurrencyIcon(IconType.ITEM, Items.COMMAND_BLOCK);
        }
    }

    protected static String formatCommand(String command, ServerPlayer player) {
        String copy = command;

        if(command.contains("{player}"))
            copy = copy.replace("{player}", player.getName().getString());
        return copy;
    }
}
