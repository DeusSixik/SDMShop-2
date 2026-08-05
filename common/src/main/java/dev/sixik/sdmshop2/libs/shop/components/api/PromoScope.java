package dev.sixik.sdmshop2.libs.shop.components.api;

/**
 * Область действия состояния активации акции.
 */
public enum PromoScope {
    /**
     * Одно активное состояние общее для всех игроков.
     */
    GLOBAL,

    /**
     * Активное состояние хранится отдельно для UUID каждого игрока.
     */
    PLAYER
}
