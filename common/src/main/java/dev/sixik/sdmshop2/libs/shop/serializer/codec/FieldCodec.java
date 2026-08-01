package dev.sixik.sdmshop2.libs.shop.serializer.codec;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;

import java.util.Objects;

/**
 * Описывает, как одно поле компонента читается и записывается в JSON и сетевой буфер.
 *
 * @param <Value> тип значения поля
 */
public interface FieldCodec<Value> {

    /**
     * Записывает значение поля в JSON под указанным ключом.
     */
    void toJson(JsonObject json, String key, Value value);

    /**
     * Читает значение поля из JSON или возвращает defaultValue, если ключ отсутствует.
     */
    Value fromJson(JsonObject json, String key, Value defaultValue);

    /**
     * Записывает значение поля в сетевой буфер.
     */
    void toNetwork(FriendlyByteBuf buf, Value value);

    /**
     * Читает значение поля из сетевого буфера.
     */
    Value fromNetwork(FriendlyByteBuf buf);

    /**
     * Создает копию значения для snapshot/diff логики.
     */
    default Value copy(Value value) {
        return value;
    }

    /**
     * Сравнивает два значения поля с учетом правил конкретного codec.
     */
    default boolean areEqual(Value first, Value second) {
        return Objects.equals(first, second);
    }

    /**
     * Возвращает имя codec для проверки совместимости сетевой схемы.
     */
    default String schemaName() {
        return getClass().getName();
    }

    /**
     * Создает builder для быстрого объявления нового codec.
     */
    static <Value> Builder<Value> builder() {
        return new Builder<>();
    }

    /**
     * Builder для создания codec из функций чтения/записи.
     */
    final class Builder<Value> {
        private String schemaName;
        private JsonWriter<Value> jsonWriter;
        private JsonReader<Value> jsonReader;
        private NetworkWriter<Value> networkWriter;
        private NetworkReader<Value> networkReader;
        private ValueCopier<Value> copier = value -> value;
        private ValueEquality<Value> equality = Objects::equals;

        /**
         * Задает стабильное имя codec для сетевой схемы.
         */
        public Builder<Value> schema(String schemaName) {
            this.schemaName = schemaName;
            return this;
        }

        /**
         * Задает функции записи и чтения JSON.
         */
        public Builder<Value> json(JsonWriter<Value> writer, JsonReader<Value> reader) {
            this.jsonWriter = writer;
            this.jsonReader = reader;
            return this;
        }

        /**
         * Задает функции записи и чтения сетевого буфера.
         */
        public Builder<Value> network(NetworkWriter<Value> writer, NetworkReader<Value> reader) {
            this.networkWriter = writer;
            this.networkReader = reader;
            return this;
        }

        /**
         * Задает способ копирования значения для snapshot.
         */
        public Builder<Value> copy(ValueCopier<Value> copier) {
            this.copier = copier == null ? value -> value : copier;
            return this;
        }

        /**
         * Задает способ сравнения значений.
         */
        public Builder<Value> equality(ValueEquality<Value> equality) {
            this.equality = equality == null ? Objects::equals : equality;
            return this;
        }

        /**
         * Собирает готовый codec и проверяет, что все обязательные функции заданы.
         */
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

    /** Функция записи значения в JSON. */
    @FunctionalInterface
    interface JsonWriter<Value> {
        void write(JsonObject json, String key, Value value);
    }

    /** Функция чтения значения из JSON. */
    @FunctionalInterface
    interface JsonReader<Value> {
        Value read(JsonObject json, String key, Value defaultValue);
    }

    /** Функция записи значения в сетевой буфер. */
    @FunctionalInterface
    interface NetworkWriter<Value> {
        void write(FriendlyByteBuf buf, Value value);
    }

    /** Функция чтения значения из сетевого буфера. */
    @FunctionalInterface
    interface NetworkReader<Value> {
        Value read(FriendlyByteBuf buf);
    }

    /** Функция копирования значения. */
    @FunctionalInterface
    interface ValueCopier<Value> {
        Value copy(Value value);
    }

    /** Функция сравнения двух значений. */
    @FunctionalInterface
    interface ValueEquality<Value> {
        boolean areEqual(Value first, Value second);
    }
}
