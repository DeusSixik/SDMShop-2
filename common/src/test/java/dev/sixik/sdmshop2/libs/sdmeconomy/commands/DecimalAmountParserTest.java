package dev.sixik.sdmshop2.libs.sdmeconomy.commands;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DecimalAmountParserTest {

    @Test
    void parsesDecimalExactlyWithoutDoubleRounding() throws CommandSyntaxException {
        BigDecimal parsed = DecimalAmountParser.parse(
                "12345678901234567890.12345678901234567890",
                BigDecimal.ZERO
        );

        assertEquals(new BigDecimal("12345678901234567890.12345678901234567890"), parsed);
        assertEquals("12345678901234567890.12345678901234567890", parsed.toPlainString());
    }

    @Test
    void acceptsScientificNotationExactly() throws CommandSyntaxException {
        BigDecimal parsed = DecimalAmountParser.parse("1E-8", BigDecimal.ZERO);

        assertEquals(new BigDecimal("0.00000001"), parsed);
    }

    @Test
    void rejectsInvalidDecimal() {
        assertThrows(
                CommandSyntaxException.class,
                () -> DecimalAmountParser.parse("12abc", BigDecimal.ZERO)
        );
    }

    @Test
    void rejectsAmountBelowMinimum() {
        assertThrows(
                CommandSyntaxException.class,
                () -> DecimalAmountParser.parse("0.009", new BigDecimal("0.01"))
        );
    }
}
