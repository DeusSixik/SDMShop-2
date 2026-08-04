package dev.sixik.sdmshop2.libs.shop.client.cache;

import dev.architectury.platform.Platform;
import dev.sixik.sdmshop2.libs.platform.SDMPlatform;
import dev.sixik.sdmshop2.libs.shop.base.ObjectIdGetter;
import dev.sixik.sdmshop2.libs.shop.base.ShopEntity;
import dev.sixik.sdmshop2.libs.shop.base.ShopInstance;
import dev.sixik.sdmshop2.libs.shop.base.ShopOffer;
import dev.sixik.sdmshop2.libs.shop.client.ui.events.ShopUIEvents;
import dev.sixik.sdmshop2.libs.shop.components.api.RewardComponent;
import dev.sixik.sdmshop2.libs.shop.components.api.ShopComponent;
import dev.sixik.sdmshop2.libs.shop.components.api.ShopComponentCategory;
import dev.sixik.sdmshop2.libs.shop.components.misc.NameComponent;
import dev.sixik.sdmshop2.libs.shop.components.utils.ShopComponentsUtils;
import dev.sixik.sdmshop2.libs.shop.editor.ShopEditSession;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Environment(EnvType.CLIENT)
public final class ShopClientCache {

    private static final int MAX_EDITOR_HISTORY = 64;
    private static final AtomicBoolean SHUTDOWN_HOOK_REGISTERED = new AtomicBoolean();

    private static volatile ClientCache cache;

    public static ClientCache get() {
        ClientCache local = cache;
        if (local == null) {
            synchronized (ShopClientCache.class) {
                local = cache;
                if (local == null) {
                    local = ClientCache.load(defaultFile());
                    cache = local;
                    registerShutdownHook();
                }
            }
        }
        return local;
    }

    public static Path defaultFile() {
        return SDMPlatform.resolveSdmDir(Platform.getConfigFolder(), "shop").resolve("client_cache.json");
    }

    public static Set<UUID> getFavorites() {
        return new LinkedHashSet<>(get().get(ClientCacheKeys.FAVORITES, ClientCacheCodecs.FAVORITES, List.of()));
    }

    public static boolean isFavorite(UUID offerId) {
        return offerId != null && getFavorites().contains(offerId);
    }

    public static boolean toggleFavorite(UUID offerId) {
        if (offerId == null) {
            return false;
        }

        List<UUID> current = new ArrayList<>(get().get(ClientCacheKeys.FAVORITES, ClientCacheCodecs.FAVORITES, List.of()));
        boolean removed = false;
        while (current.remove(offerId)) {
            removed = true;
        }

        boolean favorite = !removed;
        if (favorite) {
            current.add(offerId);
        }

        get().set(ClientCacheKeys.FAVORITES, ClientCacheCodecs.FAVORITES, current);
        ShopUIEvents.invokeFavoritesChanged(offerId, favorite, new LinkedHashSet<>(current));
        get().saveAsync();
        return favorite;
    }

    public static void setFavorite(UUID offerId, boolean favorite) {
        if (offerId == null || isFavorite(offerId) == favorite) {
            return;
        }

        toggleFavorite(offerId);
    }

    public static Set<ResourceLocation> getFavoriteComponents() {
        return new LinkedHashSet<>(get().get(ClientCacheKeys.COMPONENT_FAVORITES, ClientCacheCodecs.COMPONENT_FAVORITES, List.of()));
    }

    public static boolean isFavoriteComponent(ResourceLocation componentId) {
        return componentId != null && getFavoriteComponents().contains(componentId);
    }

    public static boolean toggleFavoriteComponent(ResourceLocation componentId) {
        if (componentId == null) {
            return false;
        }

        List<ResourceLocation> current = new ArrayList<>(get().get(ClientCacheKeys.COMPONENT_FAVORITES, ClientCacheCodecs.COMPONENT_FAVORITES, List.of()));
        boolean removed = false;
        while (current.remove(componentId)) {
            removed = true;
        }

        boolean favorite = !removed;
        if (favorite) {
            current.add(componentId);
        }

        get().set(ClientCacheKeys.COMPONENT_FAVORITES, ClientCacheCodecs.COMPONENT_FAVORITES, current);
        get().saveAsync();
        return favorite;
    }

    public static void setFavoriteComponent(ResourceLocation componentId, boolean favorite) {
        if (componentId == null || isFavoriteComponent(componentId) == favorite) {
            return;
        }

        toggleFavoriteComponent(componentId);
    }

    public static List<EditorHistoryEntry> getEditorHistory() {
        return List.copyOf(get().get(ClientCacheKeys.EDITOR_HISTORY, ClientCacheCodecs.EDITOR_HISTORY, List.of()));
    }

    public static void addEditorHistory(EditorHistoryEntry entry) {
        if (entry == null) {
            return;
        }

        List<EditorHistoryEntry> history = new ArrayList<>(get().get(ClientCacheKeys.EDITOR_HISTORY, ClientCacheCodecs.EDITOR_HISTORY, List.of()));
        history.removeIf(entry::matchesEntity);
        history.add(0, entry);
        if (history.size() > MAX_EDITOR_HISTORY) {
            history = new ArrayList<>(history.subList(0, MAX_EDITOR_HISTORY));
        }

        get().set(ClientCacheKeys.EDITOR_HISTORY, ClientCacheCodecs.EDITOR_HISTORY, history);
        get().saveAsync();
    }

    public static void rememberEditorOpen(@Nullable ShopEntity entity) {
        if (entity == null) {
            return;
        }

        UUID entityId = entity instanceof ObjectIdGetter objectIdGetter ? objectIdGetter.getUUID() : null;
        addEditorHistory(new EditorHistoryEntry(
                System.currentTimeMillis(),
                entity.getClass().getName(),
                entityId,
                describeEntity(entity),
                UUID.randomUUID().toString()
        ));
    }

    @Nullable
    public static PersistentEditSession getEditSession() {
        return get().get(ClientCacheKeys.EDIT_SESSION, ClientCacheCodecs.EDIT_SESSION, null);
    }

    public static void saveEditSession(@Nullable ShopEditSession session, @Nullable ShopInstance draftShop) {
        PersistentEditSession snapshot = PersistentEditSession.fromSession(session, draftShop);
        if (snapshot == null) {
            return;
        }

        get().set(ClientCacheKeys.EDIT_SESSION, ClientCacheCodecs.EDIT_SESSION, snapshot);
        get().saveAsync();
    }

    public static void clearEditSession() {
        get().remove(ClientCacheKeys.EDIT_SESSION);
        get().saveAsync();
    }

    public static void saveAsync() {
        get().saveAsync();
    }

    public static void saveNow() {
        get().saveNow();
    }

    public static String describeEntity(ShopEntity entity) {
        if (entity == null) {
            return "";
        }

        if (entity instanceof ShopOffer offer) {
            return describeOffer(offer);
        }

        return entity.getClass().getSimpleName();
    }

    private static String describeOffer(ShopOffer offer) {
        return offer.getComponent(NameComponent.class)
                .map(NameComponent::getName)
                .filter(name -> !name.isBlank())
                .map(ShopClientCache::resolveDisplayText)
                .orElseGet(() -> describeOfferReward(offer));
    }

    private static String describeOfferReward(ShopOffer offer) {
        Map<ShopComponentCategory, ObjectList<ShopComponent>> components = ShopComponentsUtils.getComponentsByCategory(offer);
        ObjectList<ShopComponent> rewards = components.get(ShopComponentCategory.REWARD);
        if (rewards != null && !rewards.isEmpty() && rewards.get(0) instanceof RewardComponent rewardComponent) {
            Component display = rewardComponent.getDisplayTitle();
            if (display != null && !display.getString().isBlank()) {
                return display.getString();
            }
        }

        return "Offer " + offer.getUUID();
    }

    private static String resolveDisplayText(String value) {
        return I18n.exists(value) ? Component.translatable(value).getString() : value;
    }

    private static void registerShutdownHook() {
        if (!SHUTDOWN_HOOK_REGISTERED.compareAndSet(false, true)) {
            return;
        }

        Runtime.getRuntime().addShutdownHook(new Thread(ShopClientCache::saveNow, "SDMShop2 Client Cache Shutdown"));
    }

    private ShopClientCache() {
    }
}
