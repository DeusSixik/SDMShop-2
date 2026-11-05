package dev.sixik.sdmshop2.libs.shop.components;

import dev.sixik.unigui.widgets.display.Label;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class CommandRewardComponentRenderTest {
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t"})
    void createRender_missingDisplayName_returnsFreshDefaultLabel(String name) {
        CommandRewardComponent component = new CommandRewardComponent("/time set day", name);
        Label first = assertInstanceOf(Label.class, component.createRender());
        Label second = assertInstanceOf(Label.class, component.createRender());
        assertEquals(CommandRewardComponent.EMPTY, first.text());
        assertNotSame(first, second);
        assertNull(first.parent());
        assertNull(second.parent());
    }
}
