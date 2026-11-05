package dev.sixik.sdmshop2.libs.shop.client.ui.theme;

import dev.sixik.unigui.api.math.MutableColor;

/** Shared dark editor palette and layout tokens. Values are in logical UI pixels. */
public abstract class ShopEditorTheme {
    /** Constructor for the styling compatibility facade. */
    protected ShopEditorTheme() {}

    /** Editor token BG_WORKSPACE. */
    public static final MutableColor BG_WORKSPACE =
            MutableColor.rgba255(32, 32, 32, 255); // #202020

    /** Editor token BG_WINDOW. */
    public static final MutableColor BG_WINDOW = MutableColor.rgba255(56, 56, 56, 255); // #383838

    /** Editor token BG_PANEL. */
    public static final MutableColor BG_PANEL = MutableColor.rgba255(48, 48, 48, 255); // #303030

    /** Editor token BG_HEADER. */
    public static final MutableColor BG_HEADER = MutableColor.rgba255(62, 62, 62, 255); // #3E3E3E

    /** Editor token BG_HEADER_HOVER. */
    public static final MutableColor BG_HEADER_HOVER =
            MutableColor.rgba255(72, 72, 72, 255); // #484848

    /** Editor token BG_TOOLBAR. */
    public static final MutableColor BG_TOOLBAR = MutableColor.rgba255(44, 44, 44, 255); // #2C2C2C

    /** Editor token BG_TAB_ACTIVE. */
    public static final MutableColor BG_TAB_ACTIVE =
            MutableColor.rgba255(56, 56, 56, 255); // #383838

    /** Editor token BG_TAB_INACTIVE. */
    public static final MutableColor BG_TAB_INACTIVE =
            MutableColor.rgba255(38, 38, 38, 255); // #262626

    /** Editor token BG_INPUT. */
    public static final MutableColor BG_INPUT = MutableColor.rgba255(42, 42, 42, 255); // #2A2A2A

    /** Editor token BG_HELP_BOX. */
    public static final MutableColor BG_HELP_BOX = MutableColor.rgba255(44, 44, 44, 255); // #2C2C2C

    /** Editor token BG_BUTTON. */
    public static final MutableColor BG_BUTTON = MutableColor.rgba255(76, 76, 76, 255); // #4C4C4C

    /** Editor token BG_BUTTON_HOVER. */
    public static final MutableColor BG_BUTTON_HOVER =
            MutableColor.rgba255(90, 90, 90, 255); // #5A5A5A

    /** Editor token BG_BUTTON_ACTIVE. */
    public static final MutableColor BG_BUTTON_ACTIVE =
            MutableColor.rgba255(60, 60, 60, 255); // #3C3C3C

    /** Editor token TAB_ACCENT. */
    public static final MutableColor TAB_ACCENT =
            MutableColor.rgba255(59, 120, 216, 255); // #3B78D8

    /** Editor token WARNING_YELLOW. */
    public static final MutableColor WARNING_YELLOW =
            MutableColor.rgba255(245, 185, 45, 255); // #F5B92D

    /** Editor token BORDER_DEFAULT. */
    public static final MutableColor BORDER_DEFAULT =
            MutableColor.rgba255(34, 34, 34, 255); // #222222

    /** Editor token BORDER_SUBTLE. */
    public static final MutableColor BORDER_SUBTLE =
            MutableColor.rgba255(44, 44, 44, 255); // #2C2C2C

    /** Editor token BORDER_INPUT. */
    public static final MutableColor BORDER_INPUT =
            MutableColor.rgba255(32, 32, 32, 255); // #202020

    /** Editor token BORDER_INPUT_FOCUS. */
    public static final MutableColor BORDER_INPUT_FOCUS =
            MutableColor.rgba255(59, 120, 216, 255); // #3B78D8

    /** Editor token BORDER_BUTTON. */
    public static final MutableColor BORDER_BUTTON =
            MutableColor.rgba255(48, 48, 48, 255); // #303030

    /** Editor token SEPARATOR. */
    public static final MutableColor SEPARATOR = MutableColor.rgba255(36, 36, 36, 255); // #242424

    /** Editor token TEXT_PRIMARY. */
    public static final MutableColor TEXT_PRIMARY =
            MutableColor.rgba255(220, 220, 220, 255); // #DCDCDC

    /** Editor token TEXT_LABEL. */
    public static final MutableColor TEXT_LABEL =
            MutableColor.rgba255(190, 190, 190, 255); // #BEBEBE

    /** Editor token TEXT_MUTED. */
    public static final MutableColor TEXT_MUTED =
            MutableColor.rgba255(136, 136, 136, 255); // #888888

    /** Editor token TEXT_BOLD. */
    public static final MutableColor TEXT_BOLD =
            MutableColor.rgba255(236, 236, 236, 255); // #ECECEC

    /** Editor token TEXT_HELP_BOX. */
    public static final MutableColor TEXT_HELP_BOX =
            MutableColor.rgba255(196, 196, 196, 255); // #C4C4C4

    /** Editor token AXIS_X. */
    public static final MutableColor AXIS_X = MutableColor.rgba255(224, 93, 93, 255); // Red X

    /** Editor token AXIS_Y. */
    public static final MutableColor AXIS_Y = MutableColor.rgba255(123, 209, 94, 255); // Green Y

    /** Editor token AXIS_Z. */
    public static final MutableColor AXIS_Z = MutableColor.rgba255(90, 148, 232, 255); // Blue Z

    /** Editor token CUBE_BLUE_TOP. */
    public static final MutableColor CUBE_BLUE_TOP = MutableColor.rgba255(75, 163, 227, 255);

    /** Editor token CUBE_BLUE_LEFT. */
    public static final MutableColor CUBE_BLUE_LEFT = MutableColor.rgba255(32, 114, 196, 255);

    /** Editor token CUBE_BLUE_RIGHT. */
    public static final MutableColor CUBE_BLUE_RIGHT = MutableColor.rgba255(16, 76, 140, 255);

    /** Editor token AUDIO_GOLD. */
    public static final MutableColor AUDIO_GOLD = MutableColor.rgba255(244, 184, 64, 255);

    /** Editor token SELECTION_BLUE. */
    public static final MutableColor SELECTION_BLUE =
            MutableColor.rgba255(44, 93, 136, 255); // #2C5D88

    /** Editor token PLAY_GREEN. */
    public static final MutableColor PLAY_GREEN = MutableColor.rgba255(63, 166, 82, 255);

    /** Editor token BG_DROPDOWN_HOST. */
    public static final MutableColor BG_DROPDOWN_HOST =
            MutableColor.rgba255(34, 34, 34, 255); // #222222

    /** Editor token BORDER_DROPDOWN_HOST. */
    public static final MutableColor BORDER_DROPDOWN_HOST =
            MutableColor.rgba255(44, 44, 44, 255); // #2C2C2C

    /** Editor token BG_DROPDOWN_ITEM_NORMAL. */
    public static final MutableColor BG_DROPDOWN_ITEM_NORMAL =
            MutableColor.rgba255(38, 38, 38, 255); // #262626 (Unity dark gray)

    /** Editor token BG_DROPDOWN_ITEM_CHECKED. */
    public static final MutableColor BG_DROPDOWN_ITEM_CHECKED =
            MutableColor.rgba255(36, 68, 98, 255); // #244462 (глубокий Unity Blue)

    /** Editor token BG_DROPDOWN_ITEM_HOVER. */
    public static final MutableColor BG_DROPDOWN_ITEM_HOVER =
            MutableColor.rgba255(44, 93, 136, 255); // #2C5D88 (SELECTION_BLUE в Unity)

    /** Editor token BG_DROPDOWN_ITEM_PRESS. */
    public static final MutableColor BG_DROPDOWN_ITEM_PRESS =
            MutableColor.rgba255(32, 72, 112, 255); // #204870

    /** Editor token BORDER_DROPDOWN_ITEM_HOVER. */
    public static final MutableColor BORDER_DROPDOWN_ITEM_HOVER =
            MutableColor.rgba255(72, 142, 238, 255); // #488EEE (акцент Unity Focus Blue)

    /** Editor token SCROLLBAR_TRACK. */
    public static final MutableColor SCROLLBAR_TRACK =
            MutableColor.rgba255(26, 26, 26, 200); // Тёмный трек скроллбара

    /** Editor token SCROLLBAR_THUMB. */
    public static final MutableColor SCROLLBAR_THUMB =
            MutableColor.rgba255(88, 88, 88, 220); // Серый ползунок скроллбара Unity

    /** Editor token ROW_HEIGHT. */
    public static final float ROW_HEIGHT = 18.0f;

    /** Editor token COMPONENT_HEADER_HEIGHT. */
    public static final float COMPONENT_HEADER_HEIGHT = 22.0f;

    /** Editor token LABEL_WIDTH. */
    public static final float LABEL_WIDTH = 112.0f;
}
