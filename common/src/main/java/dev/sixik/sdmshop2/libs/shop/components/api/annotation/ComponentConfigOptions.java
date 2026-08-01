package dev.sixik.sdmshop2.libs.shop.components.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ComponentConfigOptions {

    /**
     * ID провайдера вариантов для поля.
     * Например: {@code sdm:money_ids} или {@code sdm:catalog_ids}.
     */
    String provider();

    /**
     * Если список пустой или провайдер не найден — оставить обычный ручной ввод.
     */
    boolean allowCustom() default true;
}
