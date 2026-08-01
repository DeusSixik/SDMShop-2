package dev.sixik.sdmshop2.libs.shop.serializer.codec;

import com.google.gson.JsonObject;
import dev.sixik.sdmshop2.libs.shop.components.api.IComponentType;
import dev.sixik.sdmshop2.libs.shop.components.api.ShopComponent;
import dev.sixik.sdmshop2.libs.shop.serializer.ComponentSerializer;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FieldCodecsNestedStructureTest {

    private static final FieldCodec<List<Integer>> INT_LIST_CODEC = FieldCodecs.list(FieldCodecs.INT);
    private static final FieldCodec<Map<String, List<Integer>>> STRING_TO_INT_LIST_CODEC = FieldCodecs.map(FieldCodecs.STRING, INT_LIST_CODEC);
    private static final FieldCodec<List<Map<String, List<Integer>>>> MAP_LIST_CODEC = FieldCodecs.list(STRING_TO_INT_LIST_CODEC);
    private static final FieldCodec<Map<String, List<Map<String, List<Integer>>>>> NESTED_CODEC = FieldCodecs.map(FieldCodecs.STRING, MAP_LIST_CODEC);

    private static final ComponentSerializer<NestedComponent> COMPONENT_SERIALIZER = ComponentSerializer.<NestedComponent>create()
            .add("nested", NESTED_CODEC, component -> component.nested, (component, value) -> component.nested = value)
            .addDefaultedInt("version", component -> component.version, (component, value) -> component.version = value, 1)
            .addOptional("label", FieldCodecs.STRING, component -> component.label, (component, value) -> component.label = value)
            .addDiskOnly("local_note", FieldCodecs.STRING, component -> component.localNote, (component, value) -> component.localNote = value);

    private static volatile int blackhole;

    @Test
    void nestedCodecRoundTripsThroughJson() {
        Map<String, List<Map<String, List<Integer>>>> value = createNestedValue(5, 3, 4, 6);

        JsonObject json = new JsonObject();
        NESTED_CODEC.toJson(json, "nested", value);
        Map<String, List<Map<String, List<Integer>>>> read = NESTED_CODEC.fromJson(json, "nested", Map.of());

        assertTrue(NESTED_CODEC.areEqual(value, read));
        assertEquals(value, read);
    }

    @Test
    void nestedCodecRoundTripsThroughNetwork() {
        Map<String, List<Map<String, List<Integer>>>> value = createNestedValue(5, 3, 4, 6);
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());

        try {
            NESTED_CODEC.toNetwork(buf, value);
            Map<String, List<Map<String, List<Integer>>>> read = NESTED_CODEC.fromNetwork(buf);

            assertTrue(NESTED_CODEC.areEqual(value, read));
            assertEquals(value, read);
        } finally {
            buf.release();
        }
    }

    @Test
    void nestedCodecCopyIsDeepEnoughForSnapshots() {
        Map<String, List<Map<String, List<Integer>>>> value = createNestedValue(2, 2, 2, 3);
        Map<String, List<Map<String, List<Integer>>>> copy = NESTED_CODEC.copy(value);

        assertNotSame(value, copy);
        assertNotSame(value.get("group_0"), copy.get("group_0"));
        assertTrue(NESTED_CODEC.areEqual(value, copy));

        value.get("group_0").get(0).get("leaf_0").add(999);

        assertFalse(NESTED_CODEC.areEqual(value, copy));
    }

    @Test
    void mapEqualityKeepsCustomKeyCodecFallback() {
        FieldCodec<String> caseInsensitiveString = FieldCodec.<String>builder()
                .schema("case_insensitive_string")
                .json(
                        (json, key, value) -> json.addProperty(key, value),
                        (json, key, defaultValue) -> json.has(key) ? json.get(key).getAsString() : defaultValue
                )
                .network(
                        (buf, value) -> buf.writeUtf(value == null ? "" : value),
                        FriendlyByteBuf::readUtf
                )
                .copy(value -> value)
                .equality((first, second) -> first == second || first != null && second != null && first.equalsIgnoreCase(second))
                .build();

        FieldCodec<Map<String, Integer>> codec = FieldCodecs.map(caseInsensitiveString, FieldCodecs.INT);
        Map<String, Integer> first = new LinkedHashMap<>();
        first.put("Alpha", 1);
        first.put("Beta", 2);
        Map<String, Integer> second = new LinkedHashMap<>();
        second.put("alpha", 1);
        second.put("BETA", 2);

        assertTrue(codec.areEqual(first, second));
    }

    @Test
    void componentSerializerHandlesNestedSnapshotsAndDiffs() {
        NestedComponent component = new NestedComponent();
        component.nested = createNestedValue(3, 2, 3, 4);
        component.version = 7;
        component.label = "nested";
        component.localNote = "disk only";

        JsonObject json = COMPONENT_SERIALIZER.serialize(component);
        NestedComponent fromJson = COMPONENT_SERIALIZER.deserialize(json, new NestedComponent());

        assertTrue(NESTED_CODEC.areEqual(component.nested, fromJson.nested));
        assertEquals(component.version, fromJson.version);
        assertEquals(component.label, fromJson.label);
        assertEquals(component.localNote, fromJson.localNote);

        ComponentSerializer.Snapshot clean = COMPONENT_SERIALIZER.takeSnapshot(component);
        component.nested.get("group_1").get(0).get("leaf_0").add(42);
        ComponentSerializer.Snapshot diff = COMPONENT_SERIALIZER.diff(clean, component);

        assertTrue(diff.contains("nested"));
        assertFalse(diff.contains("local_note"));
    }

    @Test
    void nestedCodecBenchmarkSmoke() {
        Map<String, List<Map<String, List<Integer>>>> value = createNestedValue(24, 4, 4, 8);

        double jsonMicros = measureMicros(200, () -> {
            JsonObject json = new JsonObject();
            NESTED_CODEC.toJson(json, "nested", value);
            Map<String, List<Map<String, List<Integer>>>> read = NESTED_CODEC.fromJson(json, "nested", Map.of());
            blackhole ^= read.size();
        });

        double networkMicros = measureMicros(600, () -> {
            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            try {
                NESTED_CODEC.toNetwork(buf, value);
                Map<String, List<Map<String, List<Integer>>>> read = NESTED_CODEC.fromNetwork(buf);
                blackhole ^= read.size();
            } finally {
                buf.release();
            }
        });

        double copyEqualsMicros = measureMicros(1_000, () -> {
            Map<String, List<Map<String, List<Integer>>>> copy = NESTED_CODEC.copy(value);
            if (!NESTED_CODEC.areEqual(value, copy)) {
                throw new AssertionError("copy must be equal to original");
            }
            blackhole ^= copy.size();
        });

        System.out.printf(Locale.ROOT,
                "Nested codec benchmark: json %.2f us/op, network %.2f us/op, copy+equals %.2f us/op%n",
                jsonMicros, networkMicros, copyEqualsMicros);
    }

    private static double measureMicros(int iterations, Runnable runnable) {
        int warmupIterations = Math.max(50, iterations / 5);
        for (int i = 0; i < warmupIterations; i++) {
            runnable.run();
        }

        long start = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            runnable.run();
        }
        return (System.nanoTime() - start) / (iterations * 1_000.0);
    }

    private static Map<String, List<Map<String, List<Integer>>>> createNestedValue(int groups, int branches, int leaves, int width) {
        Map<String, List<Map<String, List<Integer>>>> out = new LinkedHashMap<>();
        for (int group = 0; group < groups; group++) {
            List<Map<String, List<Integer>>> branchList = new ArrayList<>(branches);
            for (int branch = 0; branch < branches; branch++) {
                Map<String, List<Integer>> leafMap = new LinkedHashMap<>();
                for (int leaf = 0; leaf < leaves; leaf++) {
                    List<Integer> numbers = new ArrayList<>(width);
                    for (int index = 0; index < width; index++) {
                        numbers.add(group * 10_000 + branch * 1_000 + leaf * 100 + index);
                    }
                    leafMap.put("leaf_" + leaf, numbers);
                }
                branchList.add(leafMap);
            }
            out.put("group_" + group, branchList);
        }
        return out;
    }

    private static final class NestedComponent extends ShopComponent {
        private Map<String, List<Map<String, List<Integer>>>> nested = new LinkedHashMap<>();
        private int version = 1;
        private String label;
        private String localNote;

        @Override
        public IComponentType<?> getType() {
            return null;
        }
    }
}
