package dev.sixik.sdmshop2.libs.shop.sound;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.sixik.sdmshop2.SDMShop2;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public final class ShopSounds {

    private static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(SDMShop2.MODID, Registries.SOUND_EVENT);

    public static final RegistrySupplier<SoundEvent> BUY_SUCCESS = register("buy_success");
    public static final RegistrySupplier<SoundEvent> BUY_FAIL = register("buy_fail");

    private static boolean initialized;

    private ShopSounds() {
    }

    public static void init() {
        if (initialized) {
            return;
        }

        initialized = true;
        SOUND_EVENTS.register();
    }

    private static RegistrySupplier<SoundEvent> register(String path) {
        ResourceLocation id = SDMShop2.resource(path);
        return SOUND_EVENTS.register(id, () -> SoundEvent.createVariableRangeEvent(id));
    }
}
