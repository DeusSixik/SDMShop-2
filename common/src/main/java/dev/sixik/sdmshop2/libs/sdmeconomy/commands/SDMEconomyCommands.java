package dev.sixik.sdmshop2.libs.sdmeconomy.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.sixik.sdmshop2.libs.sdmeconomy.*;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SDMEconomyCommands {

    private static final BigDecimal MIN_POSITIVE_AMOUNT = new BigDecimal("0.01");
    private static final BigDecimal MIN_ZERO_AMOUNT = BigDecimal.ZERO;

    public static void registerCommands(CommandDispatcher<CommandSourceStack> commandSourceStackCommandDispatcher, CommandBuildContext commandBuildContext, Commands.CommandSelection commandSelection) {
        registerCommands(commandSourceStackCommandDispatcher);
    }

    private static void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
            Commands.literal("sdm_economy")
                .then(Commands.literal("balance")
                    .then(Commands.argument("money", ResourceLocationArgument.id())
                        .suggests(SDMEconomyCommands::moneyIds)
                        .executes(SDMEconomyCommands::balanceCommand)
                        .then(Commands.argument("player", EntityArgument.player())
                            .requires(source -> source.hasPermission(2))
                            .executes(SDMEconomyCommands::balanceTargetCommand)
                        )
                    )
                )
                .then(Commands.literal("create_money")
                    .requires(source -> source.hasPermission(2))
                    .then(Commands.argument("money", ResourceLocationArgument.id())
                        .suggests(SDMEconomyCommands::moneyIds)
                        .executes(SDMEconomyCommands::createMoney)
                        .then(Commands.argument("target", EntityArgument.player())
                            .executes(SDMEconomyCommands::createMoneyTarget)
                        )
                    )
                )
                .then(Commands.literal("remove_money")
                    .requires(source -> source.hasPermission(2))
                    .then(Commands.argument("money", ResourceLocationArgument.id())
                        .suggests(SDMEconomyCommands::moneyIds)
                        .executes(SDMEconomyCommands::removeMoney)
                        .then(Commands.argument("target", EntityArgument.player())
                            .executes(SDMEconomyCommands::removeMoneyTarget)
                        )
                    )
                )
                .then(Commands.literal("pay")
                    .then(Commands.argument("money", ResourceLocationArgument.id()).suggests(SDMEconomyCommands::moneyIds)
                        .then(Commands.argument("target", EntityArgument.player())
                            .then(Commands.argument("amount", StringArgumentType.word())
                                .executes(SDMEconomyCommands::payCommand)
                            )
                        )
                    )
                )
                .then(Commands.literal("set")
                    .requires(source -> source.hasPermission(2))
                    .then(Commands.argument("money", ResourceLocationArgument.id()).suggests(SDMEconomyCommands::moneyIds)
                        .then(Commands.argument("target", EntityArgument.player())
                            .then(Commands.argument("amount", StringArgumentType.word())
                                .executes(SDMEconomyCommands::setMoneyCommand)
                            )
                        )
                    )
                )
                .then(Commands.literal("add")
                    .requires(source -> source.hasPermission(2))
                    .then(Commands.argument("money", ResourceLocationArgument.id()).suggests(SDMEconomyCommands::moneyIds)
                        .then(Commands.argument("target", EntityArgument.player())
                            .then(Commands.argument("amount", StringArgumentType.word())
                                .executes(SDMEconomyCommands::addMoneyCommand)
                            )
                        )
                    )
                )
        );
    }

    private static CompletableFuture<Suggestions> moneyIds(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        final ServerPlayer player = context.getSource().getPlayer();

        List<String> moneys = new ArrayList<>();
        moneys.addAll(SDMEconomyCurrencyRegistry.getStoredCurrenciesMap().keySet().stream()
                .map(ResourceLocation::toString)
                .toList());

        if(player != null) {
            final BankAccount account = SDMEconomyService.getInstance().getAccount(player.getGameProfile().getId());
            moneys.addAll(account.getCurrenciesIds().stream().map(ResourceLocation::toString).toList());
        }

        return SharedSuggestionProvider.suggest(moneys.stream().distinct(), builder);
    }

    private static int balanceCommand(CommandContext<CommandSourceStack> context) {
        ServerPlayer player = context.getSource().getPlayer();
        if (player == null) return 0;

        return showBalance(context.getSource(), player, ResourceLocationArgument.getId(context, "money"));
    }

    private static int balanceTargetCommand(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer targetPlayer = EntityArgument.getPlayer(context, "player");

        return showBalance(context.getSource(), targetPlayer, ResourceLocationArgument.getId(context, "money"));
    }

    private static int showBalance(CommandSourceStack source, ServerPlayer target, ResourceLocation money) {
        IStoredCurrency currency = currency(money);

        BankAccount account = SDMEconomyService.getInstance().getAccount(target.getGameProfile().getId());
        Component outMessage = Component.literal("Balance '").append(money.toString()).append("': ")
                .append(account.getBalance(currency).toPlainString());

        source.sendSuccess(() -> outMessage, false);
        return 1;
    }

    private static int createMoney(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        return processCreateMoney(context.getSource(), player, ResourceLocationArgument.getId(context, "money"));
    }

    private static int createMoneyTarget(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer target = EntityArgument.getPlayer(context, "target");
        return processCreateMoney(context.getSource(), target, ResourceLocationArgument.getId(context, "money"));
    }

    private static int processCreateMoney(CommandSourceStack source, ServerPlayer target, ResourceLocation money) {
        IStoredCurrency currency = currency(money);

        final BankAccount account = SDMEconomyService.getInstance().getAccount(target.getGameProfile().getId());
        account.setBalance(currency, BigDecimal.ZERO);
        SDMEconomyPlatform.syncPlayerAccount(target);

        source.sendSuccess(() -> Component.literal("Created currency '" + money + "' for " + target.getScoreboardName()), true);
        return 1;
    }

    private static int removeMoney(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        return processRemoveMoney(context.getSource(), player, ResourceLocationArgument.getId(context, "money"));
    }

    private static int removeMoneyTarget(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer target = EntityArgument.getPlayer(context, "target");
        return processRemoveMoney(context.getSource(), target, ResourceLocationArgument.getId(context, "money"));
    }

    private static int processRemoveMoney(CommandSourceStack source, ServerPlayer target, ResourceLocation money) {
        IStoredCurrency currency = currency(money);

        final BankAccount account = SDMEconomyService.getInstance().getAccount(target.getGameProfile().getId());
        account.removeBalance(currency);
        SDMEconomyPlatform.syncPlayerAccount(target);

        source.sendSuccess(() -> Component.literal("Removed currency '" + money + "' from " + target.getScoreboardName()), true);
        return 1;
    }

    private static int payCommand(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer sourcePlayer = context.getSource().getPlayerOrException();
        ServerPlayer targetPlayer = EntityArgument.getPlayer(context, "target");

        if (sourcePlayer.equals(targetPlayer)) {
            context.getSource().sendFailure(Component.literal("You cannot pay yourself!"));
            return 0;
        }

        final var moneyId = ResourceLocationArgument.getId(context, "money");
        BigDecimal amount = positiveAmount(context);
        IStoredCurrency currency = currency(moneyId);

        SDMEconomyService service = SDMEconomyService.getInstance();
        BankAccount sourceAccount = service.getAccount(sourcePlayer.getGameProfile().getId());
        BankAccount targetAccount = service.getAccount(targetPlayer.getGameProfile().getId());

        if (sourceAccount.getBalance(currency).compareTo(amount) < 0) {
            context.getSource().sendFailure(Component.literal("Insufficient funds!"));
            return 0;
        }

        sourceAccount.modify(currency, amount.negate());
        targetAccount.modify(currency, amount);
        SDMEconomyPlatform.syncPlayerAccount(sourcePlayer);
        SDMEconomyPlatform.syncPlayerAccount(targetPlayer);

        context.getSource().sendSuccess(() -> Component.literal("Successfully paid " + amountText(amount) + " '" + moneyId + "' to " + targetPlayer.getScoreboardName()), false);
        targetPlayer.sendSystemMessage(Component.literal("You received " + amountText(amount) + " '" + moneyId + "' from " + sourcePlayer.getScoreboardName()));
        return 1;
    }

    private static int setMoneyCommand(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer targetPlayer = EntityArgument.getPlayer(context, "target");
        final ResourceLocation moneyId = ResourceLocationArgument.getId(context, "money");
        BigDecimal amount = nonNegativeAmount(context);

        IStoredCurrency currency = currency(moneyId);
        BankAccount targetAccount = SDMEconomyService.getInstance().getAccount(targetPlayer.getGameProfile().getId());

        targetAccount.setBalance(currency, amount);
        SDMEconomyPlatform.syncPlayerAccount(targetPlayer);

        context.getSource().sendSuccess(() -> Component.literal("Set balance of " + targetPlayer.getScoreboardName() + " to " + amountText(amount) + " '" + moneyId + "'"), true);
        return 1;
    }

    private static int addMoneyCommand(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer targetPlayer = EntityArgument.getPlayer(context, "target");
        final var moneyId = ResourceLocationArgument.getId(context, "money");
        BigDecimal amount = positiveAmount(context);

        IStoredCurrency currency = currency(moneyId);
        BankAccount targetAccount = SDMEconomyService.getInstance().getAccount(targetPlayer.getGameProfile().getId());

        targetAccount.modify(currency, amount);
        SDMEconomyPlatform.syncPlayerAccount(targetPlayer);

        context.getSource().sendSuccess(() -> Component.literal("Added " + amountText(amount) + " '" + moneyId + "' to " + targetPlayer.getScoreboardName()), true);
        return 1;
    }

    private static IStoredCurrency currency(ResourceLocation id) {
        IStoredCurrency registered = SDMEconomyCurrencyRegistry.getStoredCurrency(id);
        return registered == null ? new DynamicStoredCurrency(id) : registered;
    }

    private static BigDecimal positiveAmount(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        return amount(context, MIN_POSITIVE_AMOUNT);
    }

    private static BigDecimal nonNegativeAmount(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        return amount(context, MIN_ZERO_AMOUNT);
    }

    private static BigDecimal amount(CommandContext<CommandSourceStack> context, BigDecimal min) throws CommandSyntaxException {
        return DecimalAmountParser.parse(StringArgumentType.getString(context, "amount"), min);
    }

    private static String amountText(BigDecimal amount) {
        return amount.toPlainString();
    }
}
