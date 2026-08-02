package dev.sixik.sdmshop2.libs.sdmeconomy.commands;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import net.minecraft.network.chat.Component;

import java.math.BigDecimal;

final class DecimalAmountParser {

    private static final DynamicCommandExceptionType INVALID_AMOUNT = new DynamicCommandExceptionType(
            value -> Component.literal("Invalid decimal amount: " + value)
    );
    private static final Dynamic2CommandExceptionType AMOUNT_TOO_SMALL = new Dynamic2CommandExceptionType(
            (value, min) -> Component.literal("Amount must be at least " + min + ", got " + value)
    );

    private DecimalAmountParser() {
    }

    static BigDecimal parse(String raw, BigDecimal min) throws CommandSyntaxException {
        try {
            BigDecimal amount = new BigDecimal(raw);
            if (amount.compareTo(min) < 0) {
                throw AMOUNT_TOO_SMALL.create(amount.toPlainString(), min.toPlainString());
            }

            return amount;
        } catch (NumberFormatException e) {
            throw INVALID_AMOUNT.create(raw);
        }
    }
}
