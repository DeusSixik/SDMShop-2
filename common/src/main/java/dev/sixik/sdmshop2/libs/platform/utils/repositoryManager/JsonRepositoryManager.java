package dev.sixik.sdmshop2.libs.platform.utils.repositoryManager;

import dev.sixik.sdmshop2.libs.platform.utils.repository.JsonGenericRepository;
import dev.sixik.sdmshop2.libs.platform.utils.repository.Repository;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import java.nio.file.Path;
import java.util.Objects;

public class JsonRepositoryManager extends RepositoryManager{

    private final Path defaultRoot;

    public JsonRepositoryManager(Path defaultRoot) {
        this.defaultRoot = Objects.requireNonNull(defaultRoot, "defaultRoot");
    }

    @Override
    public void init() {

    }

    @Override
    public void close() {

    }

    @Override
    public <K, V> Repository<K, V> createRepository(@Nullable Path custom, @NonNull String name, RepoDefinition<K, V> def) {
        Path collectionPath = custom == null ? defaultRoot.resolve(name) : custom;
        return new JsonGenericRepository<>(
                collectionPath,
                def.keyToString(),
                def.extractKey(),
                def.serializer(),
                def.deserializer()
        );
    }
}
