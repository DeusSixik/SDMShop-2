package dev.sixik.sdmshop2.libs.shop.editor;

import java.util.List;
import java.util.UUID;

public final class ShopEditBatch {

    private final UUID sessionId;
    private final UUID baseRevision;
    private final List<ShopEditCommand> commands;

    public ShopEditBatch(UUID sessionId, UUID baseRevision, List<ShopEditCommand> commands) {
        this.sessionId = sessionId;
        this.baseRevision = baseRevision;
        this.commands = List.copyOf(commands);
    }

    public UUID sessionId() {
        return sessionId;
    }

    public UUID baseRevision() {
        return baseRevision;
    }

    public List<ShopEditCommand> commands() {
        return commands;
    }
}
