package dev.sixik.sdmshop2.libs.shop.editor;

import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class ShopEditValidationReport {

    private final List<Entry> entries = new ArrayList<>();

    public void info(ResourceLocation source, String message) {
        add(ShopEditValidationLevel.INFO, source, message);
    }

    public void warning(ResourceLocation source, String message) {
        add(ShopEditValidationLevel.WARNING, source, message);
    }

    public void error(ResourceLocation source, String message) {
        add(ShopEditValidationLevel.ERROR, source, message);
    }

    public void add(ShopEditValidationLevel level, ResourceLocation source, String message) {
        entries.add(new Entry(
                Objects.requireNonNull(level, "level"),
                source,
                Objects.requireNonNull(message, "message")
        ));
    }

    public boolean hasErrors() {
        return entries.stream().anyMatch(entry -> entry.level() == ShopEditValidationLevel.ERROR);
    }

    public boolean hasWarnings() {
        return entries.stream().anyMatch(entry -> entry.level() == ShopEditValidationLevel.WARNING);
    }

    public boolean isOk() {
        return !hasErrors();
    }

    public List<Entry> entries() {
        return List.copyOf(entries);
    }

    public List<Entry> entries(ShopEditValidationLevel level) {
        return entries.stream()
                .filter(entry -> entry.level() == level)
                .toList();
    }

    public record Entry(ShopEditValidationLevel level, ResourceLocation source, String message) {
    }
}
