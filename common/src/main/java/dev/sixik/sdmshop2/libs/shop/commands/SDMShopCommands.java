package dev.sixik.sdmshop2.libs.shop.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.architectury.platform.Platform;
import dev.sixik.sdmshop2.libs.shop.base.ShopInstance;
import dev.sixik.sdmshop2.libs.shop.base.ShopOffer;
import dev.sixik.sdmshop2.libs.shop.base.ShopTable;
import dev.sixik.sdmshop2.libs.shop.commands.builder.CommandBuilder;
import dev.sixik.sdmshop2.libs.shop.limiter.ShopLimiters;
import dev.sixik.sdmshop2.libs.shop.network.ShopNetworkManager;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class SDMShopCommands {

    public static void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context, Commands.CommandSelection selection) {
        registerCommands(dispatcher);
    }

    private static void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher) {

        CommandBuilder.create("sdm_shop create_shop")
                .requires(2)
                .then(Commands.argument("shop_id", ResourceLocationArgument.id())
                        .executes(ctx -> {
                            ResourceLocation shopId = ResourceLocationArgument.getId(ctx, "shop_id");
                            if(shopId.getNamespace().equals("minecraft"))
                                shopId = new ResourceLocation("sdm", shopId.getPath());

                            ResourceLocation finalShopId = shopId;

                            final ShopInstance manager = ShopInstance.createManager(shopId, true);
                            ShopTable.Instance.addShop(manager);
                            ShopTable.Instance.save(manager);
                            ctx.getSource().sendSuccess(
                                    () -> Component.literal("Shop created: " + finalShopId),
                                    true
                            );

                            return 1;
                        })
                )
                .register(dispatcher);

        CommandBuilder.create("sdm_shop open_shop")
                .requires(2)
                .then(Commands.argument("targets", EntityArgument.players())
                        .then(Commands.argument("shop_id", ResourceLocationArgument.id())
                                .suggests((context, builder) -> {
                                    return SharedSuggestionProvider.suggestResource(ShopTable.Instance.getShopsId(), builder);
                                })

                                .executes(ctx -> {
                                    final Collection<ServerPlayer> targets = EntityArgument.getPlayers(ctx, "targets");
                                    ResourceLocation shopId = ResourceLocationArgument.getId(ctx, "shop_id");
                                    if(shopId.getNamespace().equals("minecraft"))
                                        shopId = new ResourceLocation("sdm", shopId.getPath());
                                    final ResourceLocation finalShopId = shopId;

                                    final ShopInstance shop = ShopTable.Instance.getShop(shopId);
                                    if(shop == null) {
                                        ctx.getSource().sendFailure(Component.literal("Shop with id '" + shopId + "' not found").withStyle(ChatFormatting.RED));
                                        return 0;
                                    }

                                    ShopNetworkManager.sendShopDataAndOpen(shop, targets);
                                    ctx.getSource().sendSuccess(
                                            () -> Component.literal("Shop opened: " + finalShopId + " for " + targets.size() + " player(s)").withStyle(ChatFormatting.GREEN),
                                            true
                                    );
                                    return targets.size();
                                })
                        )
                )
                .register(dispatcher);

        CommandBuilder.create("sdm_shop limiter sync")
                .requires(2)
                .executesVoid(ctx -> {
                    ShopNetworkManager.sendLimiterData(ctx.getSource().getPlayerOrException());
                })
                .register(dispatcher);

        CommandBuilder.create("sdm_shop limiter reset")
                .requires(2)
                .then(Commands.literal("world")
                        .then(Commands.argument("shop_id", ResourceLocationArgument.id())
                                .suggests((context, builder) -> SharedSuggestionProvider.suggestResource(ShopTable.Instance.getShopsId(), builder))
                                .then(Commands.argument("offer_id", StringArgumentType.word())
                                        .suggests(SDMShopCommands::suggestOfferIds)
                                        .executes(ctx -> resetWorldLimit(ctx))
                                )
                        )
                )
                .then(Commands.literal("player")
                        .then(Commands.argument("target", EntityArgument.player())
                                .then(Commands.argument("shop_id", ResourceLocationArgument.id())
                                        .suggests((context, builder) -> SharedSuggestionProvider.suggestResource(ShopTable.Instance.getShopsId(), builder))
                                        .then(Commands.argument("offer_id", StringArgumentType.word())
                                                .suggests(SDMShopCommands::suggestOfferIds)
                                                .executes(ctx -> resetPlayerLimit(ctx))
                                        )
                                )
                        )
                )
                .then(Commands.literal("offer")
                        .then(Commands.argument("shop_id", ResourceLocationArgument.id())
                                .suggests((context, builder) -> SharedSuggestionProvider.suggestResource(ShopTable.Instance.getShopsId(), builder))
                                .then(Commands.argument("offer_id", StringArgumentType.word())
                                        .suggests(SDMShopCommands::suggestOfferIds)
                                        .executes(ctx -> resetOfferLimit(ctx))
                                )
                        )
                )
                .register(dispatcher);

        CommandBuilder.create("sdm_shop reload shops")
                .requires(2)
                .executesVoid(ctx -> {

                    if(ShopTable.Instance.isReloading()) {
                        ctx.getSource().sendFailure(Component.literal("Shops are already reloading").withStyle(ChatFormatting.RED));
                        return;
                    }

                    ShopTable.Instance.reload();
                    ctx.getSource().sendSuccess(() -> Component.literal("Shops reloaded successfully").withStyle(ChatFormatting.GREEN), true);
                })
                .register(dispatcher);

        if(Platform.isDevelopmentEnvironment()) {
            SDMShopCommandsDebug.init(dispatcher);
        }
    }

    private static int resetWorldLimit(CommandContext<CommandSourceStack> ctx) {
        ShopOffer offer = getCommandOffer(ctx);
        if (offer == null) {
            return 0;
        }

        boolean changed = ShopLimiters.resetWorld(offer);
        sendResetResult(ctx, changed, "World limit reset", "No stored world limit data found");
        return changed ? 1 : 0;
    }

    private static int resetPlayerLimit(CommandContext<CommandSourceStack> ctx) throws com.mojang.brigadier.exceptions.CommandSyntaxException {
        ShopOffer offer = getCommandOffer(ctx);
        if (offer == null) {
            return 0;
        }

        ServerPlayer target = EntityArgument.getPlayer(ctx, "target");
        boolean changed = ShopLimiters.resetPlayer(offer, target);
        sendResetResult(
                ctx,
                changed,
                "Player limit reset for " + target.getScoreboardName(),
                "No stored player limit data found for " + target.getScoreboardName()
        );
        return changed ? 1 : 0;
    }

    private static int resetOfferLimit(CommandContext<CommandSourceStack> ctx) {
        ShopOffer offer = getCommandOffer(ctx);
        if (offer == null) {
            return 0;
        }

        boolean changed = ShopLimiters.resetOffer(offer);
        sendResetResult(ctx, changed, "All offer limits reset", "No stored offer limit data found");
        return changed ? 1 : 0;
    }

    private static ShopOffer getCommandOffer(CommandContext<CommandSourceStack> ctx) {
        ShopInstance shop = getCommandShop(ctx);
        if (shop == null) {
            return null;
        }

        UUID offerId = parseOfferId(ctx);
        if (offerId == null) {
            return null;
        }

        ShopOffer offer = shop.getEntries().getEntry(offerId);
        if (offer == null) {
            ctx.getSource().sendFailure(Component.literal("Offer with id '" + offerId + "' not found in shop '" + shop.getId() + "'").withStyle(ChatFormatting.RED));
        }
        return offer;
    }

    private static ShopInstance getCommandShop(CommandContext<CommandSourceStack> ctx) {
        ResourceLocation shopId = normalizeShopId(ResourceLocationArgument.getId(ctx, "shop_id"));
        ShopInstance shop = ShopTable.Instance.getShop(shopId);
        if (shop == null) {
            ctx.getSource().sendFailure(Component.literal("Shop with id '" + shopId + "' not found").withStyle(ChatFormatting.RED));
        }
        return shop;
    }

    private static UUID parseOfferId(CommandContext<CommandSourceStack> ctx) {
        String rawOfferId = StringArgumentType.getString(ctx, "offer_id");
        try {
            return UUID.fromString(rawOfferId);
        } catch (IllegalArgumentException e) {
            ctx.getSource().sendFailure(Component.literal("Invalid offer UUID: " + rawOfferId).withStyle(ChatFormatting.RED));
            return null;
        }
    }

    private static ResourceLocation normalizeShopId(ResourceLocation shopId) {
        return shopId.getNamespace().equals("minecraft") ? new ResourceLocation("sdm", shopId.getPath()) : shopId;
    }

    private static void sendResetResult(CommandContext<CommandSourceStack> ctx, boolean changed, String success, String noData) {
        if (changed) {
            ctx.getSource().sendSuccess(() -> Component.literal(success).withStyle(ChatFormatting.GREEN), true);
        } else {
            ctx.getSource().sendFailure(Component.literal(noData).withStyle(ChatFormatting.YELLOW));
        }
    }

    private static CompletableFuture<Suggestions> suggestOfferIds(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        try {
            ShopInstance shop = ShopTable.Instance.getShop(normalizeShopId(ResourceLocationArgument.getId(context, "shop_id")));
            if (shop != null) {
                return SharedSuggestionProvider.suggest(
                        shop.getEntries().getEntryMap().keySet().stream().map(UUID::toString),
                        builder
                );
            }
        } catch (Exception ignored) {
        }

        return builder.buildFuture();
    }
}
