package dev.sixik.sdmshop2.libs.shop.client.ui.api;

/**
 * Абстрактный интерфейс для реализации собственого рендера и поведения.
 */
public interface OfferElementRender {

    /**
     * Вызывается в конструкторе класса
     */
    void constructor(OfferElementContextRender ctx);

    /**
     * Вызывается на этапе добавления виджетов
     */
    void addWidgets(OfferElementContextRender ctx);

    /**
     * Вызывается на этапе установки параметрова (размер, позиция и т.п)
     */
    void alightWidgets(OfferElementContextRender ctx);

    boolean mouseClicked(OfferElementContextRender ctx, double mouseX, double mouseY, int button);
}
