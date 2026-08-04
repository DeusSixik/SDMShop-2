package dev.sixik.sdmshop2.libs.shop.editor;

import com.google.gson.JsonElement;
import dev.sixik.sdmshop2.libs.sdmeconomy.CurrencyDraft;
import dev.sixik.sdmshop2.libs.shop.base.ShopInstance;
import net.minecraft.resources.ResourceLocation;

import java.util.*;

public final class ShopEditSession implements AutoCloseable {

    private final UUID sessionId;
    private final UUID baseRevision;
    private final UUID author;
    private final long createdAt;
    private long updatedAt;

    private final ShopEditDraftStore drafts = new ShopEditDraftStore();
    private final List<ShopEditCommand> commands = new ArrayList<>();
    private final List<ShopEditHistoryEntry> history = new ArrayList<>();
    private final List<CurrencyDraft> currencyDrafts = new ArrayList<>();
    private final Map<ResourceLocation, Object> metadata = new LinkedHashMap<>();

    private boolean dirty;
    private boolean closed;
    private Runnable onChanged = () -> {
    };

    private ShopEditSession(UUID sessionId, UUID baseRevision, UUID author) {
        this(sessionId, baseRevision, author, System.currentTimeMillis(), System.currentTimeMillis());
    }

    private ShopEditSession(UUID sessionId, UUID baseRevision, UUID author, long createdAt, long updatedAt) {
        this.sessionId = Objects.requireNonNull(sessionId, "sessionId");
        this.baseRevision = Objects.requireNonNull(baseRevision, "baseRevision");
        this.author = Objects.requireNonNull(author, "author");
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;

        ShopEditorModuleContext context = new ShopEditorModuleContext(this);
        for (ShopEditorModule module : ShopEditorRegistry.modules()) {
            module.onSessionCreated(context);
        }
    }

    public static ShopEditSession create(UUID author) {
        return new ShopEditSession(UUID.randomUUID(), UUID.randomUUID(), author);
    }

    public static ShopEditSession create(UUID sessionId, UUID baseRevision, UUID author) {
        return new ShopEditSession(sessionId, baseRevision, author);
    }

    public static ShopEditSession forShop(ShopInstance liveShop, UUID author) {
        Objects.requireNonNull(liveShop, "liveShop");

        ShopEditSession session = create(author);
        session.setDraft(ShopEditTargets.shop(liveShop.getId()), copyShop(liveShop));
        session.recordHistory("session.create", "shop", null, null, "Created edit session for " + liveShop.getId());
        session.markClean();
        return session;
    }

    public static ShopEditSession restore(
            UUID sessionId,
            UUID baseRevision,
            UUID author,
            long createdAt,
            long updatedAt,
            ShopInstance draftShop,
            List<ShopEditHistoryEntry> history
    ) {
        return restore(sessionId, baseRevision, author, createdAt, updatedAt, draftShop, history, List.of());
    }

    public static ShopEditSession restore(
            UUID sessionId,
            UUID baseRevision,
            UUID author,
            long createdAt,
            long updatedAt,
            ShopInstance draftShop,
            List<ShopEditHistoryEntry> history,
            List<CurrencyDraft> currencyDrafts
    ) {
        Objects.requireNonNull(draftShop, "draftShop");

        ShopEditSession session = new ShopEditSession(sessionId, baseRevision, author, createdAt, updatedAt);
        session.drafts.put(ShopEditTargets.shop(draftShop.getId()), draftShop);
        if (history != null) {
            session.history.addAll(history);
        }
        if (currencyDrafts != null) {
            session.currencyDrafts.addAll(currencyDrafts);
        }
        session.dirty = !session.history.isEmpty() || !session.currencyDrafts.isEmpty();
        return session;
    }

    public UUID sessionId() {
        return sessionId;
    }

    public UUID baseRevision() {
        return baseRevision;
    }

    public UUID author() {
        return author;
    }

    public long createdAt() {
        return createdAt;
    }

    public long updatedAt() {
        return updatedAt;
    }

    public boolean dirty() {
        return dirty;
    }

    public boolean closed() {
        return closed;
    }

    public void setOnChanged(Runnable onChanged) {
        this.onChanged = onChanged == null ? () -> {
        } : onChanged;
    }

    public ShopEditDraftStore drafts() {
        ensureOpen();
        return drafts;
    }

    public List<ShopEditCommand> commands() {
        return List.copyOf(commands);
    }

    public List<ShopEditHistoryEntry> history() {
        return List.copyOf(history);
    }

    public int historySize() {
        return history.size();
    }

    public List<CurrencyDraft> currencyDrafts() {
        ensureOpen();
        return List.copyOf(currencyDrafts);
    }

    public void setCurrencyDrafts(List<CurrencyDraft> drafts) {
        ensureOpen();
        currencyDrafts.clear();
        if (drafts != null) {
            currencyDrafts.addAll(drafts);
        }
        markDirty();
    }

    public Map<ResourceLocation, Object> metadata() {
        return Map.copyOf(metadata);
    }

    public <T> Optional<T> draft(ShopEditTarget<T> target) {
        ensureOpen();
        return drafts.get(target);
    }

    public <T> void setDraft(ShopEditTarget<T> target, T value) {
        ensureOpen();
        drafts.put(target, value);
        markDirty();
    }

    public <T> void removeDraft(ShopEditTarget<T> target) {
        ensureOpen();
        drafts.remove(target);
        markDirty();
    }

    public ShopInstance draftShop(ResourceLocation shopId) {
        return draft(ShopEditTargets.shop(shopId))
                .orElseThrow(() -> new IllegalStateException("No draft shop in session: " + shopId));
    }

    public void putMetadata(ResourceLocation key, Object value) {
        ensureOpen();
        if (value == null) {
            metadata.remove(key);
        } else {
            metadata.put(key, value);
        }
        markDirty();
    }

    public Optional<Object> metadata(ResourceLocation key) {
        ensureOpen();
        return Optional.ofNullable(metadata.get(key));
    }

    public ShopEditValidationReport execute(ShopEditCommand command) {
        ensureOpen();
        Objects.requireNonNull(command, "command");

        ShopEditContext context = new ShopEditContext(this);
        command.apply(context);
        commands.add(command);
        markDirty();

        ShopEditValidationReport report = new ShopEditValidationReport();
        command.validate(context, report);
        return report;
    }

    public void record(ShopEditCommand command) {
        ensureOpen();
        commands.add(Objects.requireNonNull(command, "command"));
        markDirty();
    }

    public void recordHistory(
            String action,
            String entityType,
            UUID entityId,
            ResourceLocation componentType,
            String summary
    ) {
        ensureOpen();
        history.add(ShopEditHistoryEntry.create(action, entityType, entityId, componentType, summary));
        markDirty();
    }

    public void clearHistory() {
        ensureOpen();
        commands.clear();
        history.clear();
        markDirty();
    }

    public void resetDraft(ShopInstance liveShop) {
        Objects.requireNonNull(liveShop, "liveShop");
        ensureOpen();

        drafts.clear();
        drafts.put(ShopEditTargets.shop(liveShop.getId()), copyShop(liveShop));
        commands.clear();
        history.clear();
        currencyDrafts.clear();
        markClean();
    }

    public ShopEditValidationReport validate() {
        ensureOpen();
        ShopEditValidationReport report = new ShopEditValidationReport();
        ShopEditContext context = new ShopEditContext(this);

        for (ShopEditCommand command : commands) {
            command.validate(context, report);
        }

        for (ShopEditorModule module : ShopEditorRegistry.modules()) {
            if (module.supports(this)) {
                module.validate(this, report);
            }
        }

        return report;
    }

    public ShopEditBatch buildBatch() {
        ensureOpen();
        return new ShopEditBatch(sessionId, baseRevision, commands);
    }

    public void markDirty() {
        dirty = true;
        updatedAt = System.currentTimeMillis();
        onChanged.run();
    }

    public void markClean() {
        dirty = false;
        updatedAt = System.currentTimeMillis();
    }

    @Override
    public void close() {
        if (closed) {
            return;
        }

        closed = true;
        drafts.clear();
        commands.clear();
        history.clear();
        currencyDrafts.clear();
        metadata.clear();
    }

    private void ensureOpen() {
        if (closed) {
            throw new IllegalStateException("Shop edit session is closed: " + sessionId);
        }
    }

    public static ShopInstance copyShop(ShopInstance source) {
        JsonElement copy = source.serialize().deepCopy();
        ShopInstance draft = ShopInstance.fromJson(copy);
        draft.setShouldSave(false);
        return draft;
    }
}
