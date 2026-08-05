package dev.sixik.sdmshop2.libs.shop.client.sound;

import dev.sixik.sdmshop2.libs.shop.sound.ShopSounds;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvent;

@Environment(EnvType.CLIENT)
public final class ShopClientSounds {

    private ShopClientSounds() {
    }

    public static void playPurchaseSuccess() {
        playUi(ShopSounds.BUY_SUCCESS.get());
    }

    public static void playPurchaseFail() {
        playUi(ShopSounds.BUY_FAIL.get());
    }

    private static void playUi(SoundEvent sound) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft == null) {
            return;
        }

        minecraft.getSoundManager().play(SimpleSoundInstance.forUI(sound, 1.0F));
    }
}
