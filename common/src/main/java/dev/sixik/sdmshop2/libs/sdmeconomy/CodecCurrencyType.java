package dev.sixik.sdmshop2.libs.sdmeconomy;

import com.google.gson.JsonObject;
import dev.sixik.sdmshop2.libs.shop.serializer.codec.FieldCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * {@link ICurrencyType} implementation backed by {@link FieldCodec} fields.
 *
 * <p>It keeps currency JSON flat: the type marker and all configured fields are written
 * into the same object instead of nesting data under a separate key.</p>
 */
public class CodecCurrencyType<T extends IExternalCurrency> implements ICurrencyType<T> {

    private final Class<T> ownerClass;
    private final ResourceLocation typeId;
    private final List<Field<T, ?>> fields;
    private final CurrencyFactory<T> factory;

    public <Value> CodecCurrencyType(
            Class<T> ownerClass,
            ResourceLocation typeId,
            String fieldKey,
            FieldCodec<Value> fieldCodec,
            Function<T, Value> getter,
            BiFunction<ResourceLocation, Value, T> factory,
            Value defaultValue
    ) {
        this(
                ownerClass,
                typeId,
                List.of(new Field<>(fieldKey, fieldCodec, getter, defaultValue)),
                (id, values) -> factory.apply(id, value(values, 0))
        );
    }

    private CodecCurrencyType(
            Class<T> ownerClass,
            ResourceLocation typeId,
            List<Field<T, ?>> fields,
            CurrencyFactory<T> factory
    ) {
        this.ownerClass = Objects.requireNonNull(ownerClass, "ownerClass");
        this.typeId = Objects.requireNonNull(typeId, "typeId");
        this.fields = List.copyOf(fields);
        this.factory = Objects.requireNonNull(factory, "factory");
    }

    public static <T extends IExternalCurrency> Builder<T> builder(Class<T> ownerClass, ResourceLocation typeId) {
        return new Builder<>(ownerClass, typeId);
    }

    @Override
    public Class<T> getOwnerClass() {
        return ownerClass;
    }

    @Override
    public T deserialize(ResourceLocation id, JsonObject json) {
        Object[] values = new Object[fields.size()];
        for (int i = 0; i < fields.size(); i++) {
            values[i] = fields.get(i).read(json);
        }

        return factory.create(id, new Values(values));
    }

    @Override
    public JsonObject serialize(T currency) {
        JsonObject json = new JsonObject();
        serializeType(json, typeId.toString());
        for (Field<T, ?> field : fields) {
            field.write(json, currency);
        }
        return json;
    }

    @SuppressWarnings("unchecked")
    private static <Value> Value value(Values values, int index) {
        return (Value) values.get(index);
    }

    @FunctionalInterface
    public interface CurrencyFactory<T extends IExternalCurrency> {
        T create(ResourceLocation id, Values values);
    }

    public static final class Values {
        private final Object[] values;

        private Values(Object[] values) {
            this.values = values;
        }

        @SuppressWarnings("unchecked")
        public <Value> Value get(int index) {
            return (Value) values[index];
        }

        public int size() {
            return values.length;
        }
    }

    public static final class Builder<T extends IExternalCurrency> {
        private final Class<T> ownerClass;
        private final ResourceLocation typeId;
        private final List<Field<T, ?>> fields = new ArrayList<>();

        private Builder(Class<T> ownerClass, ResourceLocation typeId) {
            this.ownerClass = Objects.requireNonNull(ownerClass, "ownerClass");
            this.typeId = Objects.requireNonNull(typeId, "typeId");
        }

        public <Value> Builder<T> field(
                String key,
                FieldCodec<Value> codec,
                Function<T, Value> getter,
                Value defaultValue
        ) {
            fields.add(new Field<>(key, codec, getter, defaultValue));
            return this;
        }

        public CodecCurrencyType<T> build(CurrencyFactory<T> factory) {
            if (fields.isEmpty()) {
                throw new IllegalStateException("CodecCurrencyType must have at least one field");
            }

            return new CodecCurrencyType<>(ownerClass, typeId, fields, factory);
        }
    }

    private record Field<T extends IExternalCurrency, Value>(
            String key,
            FieldCodec<Value> codec,
            Function<T, Value> getter,
            Value defaultValue
    ) {
        private Field {
            Objects.requireNonNull(key, "key");
            Objects.requireNonNull(codec, "codec");
            Objects.requireNonNull(getter, "getter");
        }

        private Value read(JsonObject json) {
            return codec.fromJson(json, key, defaultValue);
        }

        private void write(JsonObject json, T currency) {
            codec.toJson(json, key, getter.apply(currency));
        }
    }
}
