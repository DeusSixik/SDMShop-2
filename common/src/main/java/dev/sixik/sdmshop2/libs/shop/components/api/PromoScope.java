package dev.sixik.sdmshop2.libs.shop.components.api;

/**
 * Scope for promo activation state.
 */
public enum PromoScope {
    /**
     * One active state is shared by every player.
     */
    GLOBAL,

    /**
     * Active state is stored independently for each player UUID.
     */
    PLAYER
}
