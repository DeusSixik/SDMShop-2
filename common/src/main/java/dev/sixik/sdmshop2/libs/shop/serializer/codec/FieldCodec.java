package dev.sixik.sdmshop2.libs.shop.serializer.codec;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;

import java.util.Objects;

public interface FieldCodec<Value> {

    void toJson(JsonObject json, String key, Value value);

    Value fromJson(JsonObject json, String key, Value defaultValue);

    void toNetwork(FriendlyByteBuf buf, Value value);

    Value fromNetwork(FriendlyByteBuf buf);

    default Value copy(Value value) {
        return value;
    }

    default boolean areEqual(Value first, Value second) {
        return Objects.equals(first, second);
    }

    default String schemaName() {
        return getClass().getName();
    }

    static <Value> Builder<Value> builder() {
        return new Builder<>();
    }

    final class Builder<Value> {
        private String schemaName;
        private JsonWriter<Value> jsonWriter;
        private JsonReader<Value> jsonReader;
        private NetworkWriter<Value> networkWriter;
        private NetworkReader<Value> networkReader;
        private ValueCopier<Value> copier = value -> value;
        private ValueEquality<Value> equality = Objects::equals;

        public Builder<Value> schema(String schemaName) {
            this.schemaName = schemaName;
            return this;
        }

        public Builder<Value> json(JsonWriter<Value> writer, JsonReader<Value> reader) {
            this.jsonWriter = writer;
            this.jsonReader = reader;
            return this;
        }

        public Builder<Value> network(NetworkWriter<Value> writer, NetworkReader<Value> reader) {
            this.networkWriter = writer;
            this.networkReader = reader;
            return this;
        }

        public Builder<Value> copy(ValueCopier<Value> copier) {
            this.copier = copier == null ? value -> value : copier;
            return this;
        }

        public Builder<Value> equality(ValueEquality<Value> equality) {
            this.equality = equality == null ? Objects::equals : equality;
            return this;
        }

        public FieldCodec<Value> build() {
            if (jsonWriter == null) throw new IllegalStateException("Json writer is not configured");
            if (jsonReader == null) throw new IllegalStateException("Json reader is not configured");
            if (networkWriter == null) throw new IllegalStateException("Network writer is not configured");
            if (networkReader == null) throw new IllegalStateException("Network reader is not configured");
            String finalSchemaName = schemaName == null ? "custom:" + getClass().getName() : schemaName;

            return new FieldCodec<>() {
                @Override
                public void toJson(JsonObject json, String key, Value value) {
                    jsonWriter.write(json, key, value);
                }

                @Override
                public Value fromJson(JsonObject json, String key, Value defaultValue) {
                    return jsonReader.read(json, key, defaultValue);
                }

                @Override
                public void toNetwork(FriendlyByteBuf buf, Value value) {
                    networkWriter.write(buf, value);
                }

                @Override
                public Value fromNetwork(FriendlyByteBuf buf) {
                    return networkReader.read(buf);
                }

                @Override
                public Value copy(Value value) {
                    return copier.copy(value);
                }

                @Override
                public boolean areEqual(Value first, Value second) {
                    return equality.areEqual(first, second);
                }

                @Override
                public String schemaName() {
                    return finalSchemaName;
                }
            };
        }
    }

    @FunctionalInterface
    interface JsonWriter<Value> {
        void write(JsonObject json, String key, Value value);
    }

    @FunctionalInterface
    interface JsonReader<Value> {
        Value read(JsonObject json, String key, Value defaultValue);
    }

    @FunctionalInterface
    interface NetworkWriter<Value> {
        void write(FriendlyByteBuf buf, Value value);
    }

    @FunctionalInterface
    interface NetworkReader<Value> {
        Value read(FriendlyByteBuf buf);
    }

    @FunctionalInterface
    interface ValueCopier<Value> {
        Value copy(Value value);
    }

    @FunctionalInterface
    interface ValueEquality<Value> {
        boolean areEqual(Value first, Value second);
    }
}
