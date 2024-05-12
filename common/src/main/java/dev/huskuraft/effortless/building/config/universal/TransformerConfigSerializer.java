package dev.huskuraft.effortless.building.config.universal;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.ConfigSpec;
import com.electronwill.nightconfig.core.EnumGetMethod;

import dev.huskuraft.effortless.api.config.ConfigSerializer;
import dev.huskuraft.effortless.api.core.Item;
import dev.huskuraft.effortless.api.core.Items;
import dev.huskuraft.effortless.api.core.ResourceLocation;
import dev.huskuraft.effortless.api.math.BoundingBox3d;
import dev.huskuraft.effortless.api.math.Vector3d;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.building.PositionType;
import dev.huskuraft.effortless.building.pattern.Transformer;
import dev.huskuraft.effortless.building.pattern.Transformers;
import dev.huskuraft.effortless.building.pattern.array.ArrayTransformer;
import dev.huskuraft.effortless.building.pattern.mirror.MirrorTransformer;
import dev.huskuraft.effortless.building.pattern.raidal.RadialTransformer;
import dev.huskuraft.effortless.building.pattern.randomize.Chance;
import dev.huskuraft.effortless.building.pattern.randomize.ItemRandomizer;

public class TransformerConfigSerializer implements ConfigSerializer<Transformer> {

    public static final TransformerConfigSerializer INSTANCE = new TransformerConfigSerializer();
    private static final String KEY_ID = "id";
//    private static final String KEY_NAME = "name";
    private static final String KEY_TYPE = "type";

    private TransformerConfigSerializer() {
    }

    public static String randomIdString() {
        return UUID.randomUUID().toString();
    }

    public static boolean isIdCorrect(Object string) {
        try {
            UUID.fromString((String) string);
            return true;
        } catch (ClassCastException | IllegalArgumentException e) {
            return false;
        }
    }

    public static void defineVector3d(ConfigSpec configSpec, String key, Vector3d defaultValue, double minX, double maxX, double minY, double maxY, double minZ, double maxZ) {
        configSpec.define(key, () -> List.of(defaultValue.x(), defaultValue.y(), defaultValue.z()), value ->
                value instanceof List list && list.size() == 3 &&
                        list.get(0) instanceof Number x && x.doubleValue() >= minX && x.doubleValue() <= maxX &&
                        list.get(1) instanceof Number y && y.doubleValue() >= minY && y.doubleValue() <= maxY &&
                        list.get(2) instanceof Number z && z.doubleValue() >= minZ && z.doubleValue() <= maxZ);
    }

    public static void defineVector3d(ConfigSpec configSpec, String key, Vector3d defaultValue, BoundingBox3d boundingBox3d) {
        defineVector3d(configSpec, key, defaultValue, boundingBox3d.minX, boundingBox3d.maxX, boundingBox3d.minY, boundingBox3d.maxY, boundingBox3d.minZ, boundingBox3d.maxZ);
    }

    public static void defineVector3d(ConfigSpec configSpec, String key, Vector3d defaultValue) {
        defineVector3d(configSpec, key, defaultValue, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    public static void defineVector3d(ConfigSpec configSpec, String key) {
        defineVector3d(configSpec, key, Vector3d.ZERO);
    }

    public static <T extends Enum<T>> void defineEnum(ConfigSpec configSpec, String key, T defaultValue) {
        configSpec.define(key, () -> defaultValue.name().toLowerCase(Locale.ROOT), (value) -> value instanceof String name && Arrays.stream(defaultValue.getDeclaringClass().getEnumConstants()).anyMatch(e -> e.name().equals(name.toUpperCase(Locale.ROOT))));
    }

    public static void setVector3d(Config config, String key, Vector3d value) {
        config.set(key, List.of(value.x(), value.y(), value.z()));
    }

    public static Vector3d getVector3d(Config config, String key) {
        var values = config.<List<Number>>get(key).stream().map(Number::doubleValue).toArray(Double[]::new);
        return new Vector3d(values[0], values[1], values[2]);
    }

    public static void definePositionType(ConfigSpec configSpec, String key, PositionType[] defaultValue) {
        configSpec.define(key, () -> Arrays.stream(defaultValue).map(Enum::name).map(String::toLowerCase).toList(), value -> value instanceof List<?> list && list.size() == defaultValue.length && list.stream().allMatch(value1 -> value1 instanceof String name && isSuccess(() -> PositionType.valueOf(name.toUpperCase(Locale.ROOT)))));
    }

    public static void setPositionType(Config config, String key, PositionType[] value) {
        config.set(key, Arrays.stream(value).map(Enum::name).map(String::toLowerCase).toList());
    }

    public static PositionType[] getPositionType(Config config, String key) {
        return config.<List<String>>get(key).stream().map(String::toUpperCase).map(PositionType::valueOf).toArray(PositionType[]::new);
    }

    public static <T extends Enum<T>> void setEnum(Config config, String key, T value) {
        config.set(key, value.name().toLowerCase(Locale.ROOT));
    }

    public static <T extends Enum<T>> T getEnum(Config config, String key, T... typeGetter) {
        return config.getEnum(key, (Class<T>) typeGetter.getClass().getComponentType());
    }

    public static boolean isSuccess(Runnable runnable) {
        try {
            runnable.run();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static Predicate<Object> predicate(Consumer<Object> consumer) {
        return (object) -> {
            try {
                consumer.accept(object);
                return true;
            } catch (Exception e) {
                return false;
            }
        };
    }

    @Override
    public ConfigSpec getSpec(Config config) {
        var spec = new ConfigSpec();
        spec.define(KEY_ID, TransformerConfigSerializer::randomIdString, TransformerConfigSerializer::isIdCorrect);
//        spec.define(KEY_NAME, () -> getDefault().getName().getString(), String.class::isInstance);
        return spec;
    }

    @Override
    public Transformer deserialize(Config config) {
        Transformers type;
        try {
            type = Objects.requireNonNull(config.getEnum(KEY_TYPE, Transformers.class, EnumGetMethod.NAME_IGNORECASE));
        } catch (IllegalArgumentException | ClassCastException | NullPointerException e) {
            return null;
        }

        return (switch (type) {
            case ARRAY -> ArrayTransformerConfigSerializer.INSTANCE;
            case MIRROR -> MirrorTransformerConfigSerializer.INSTANCE;
            case RADIAL -> RadialTransformerConfigSerializer.INSTANCE;
            case ITEM_RAND -> ItemRandomizerConfigSerializer.INSTANCE;
        }).deserialize(config);
    }

    @Override
    public Transformer getDefault() {
        return null;
    }

    @Override
    public Config serialize(Transformer transformer) {
        return (switch (transformer.getType()) {
            case ARRAY -> ArrayTransformerConfigSerializer.INSTANCE.serialize((ArrayTransformer) transformer);
            case MIRROR -> MirrorTransformerConfigSerializer.INSTANCE.serialize((MirrorTransformer) transformer);
            case RADIAL -> RadialTransformerConfigSerializer.INSTANCE.serialize((RadialTransformer) transformer);
            case ITEM_RAND -> ItemRandomizerConfigSerializer.INSTANCE.serialize((ItemRandomizer) transformer);
        });
    }

    public static class ArrayTransformerConfigSerializer implements ConfigSerializer<ArrayTransformer> {

        private static final String KEY_OFFSET = "offset";
        private static final String KEY_COUNT = "count";
        public static ArrayTransformerConfigSerializer INSTANCE = new ArrayTransformerConfigSerializer();

        private ArrayTransformerConfigSerializer() {
        }

        @Override
        public ConfigSpec getSpec(Config config) {
            var spec = new ConfigSpec();
            spec.define(KEY_ID, TransformerConfigSerializer::randomIdString, TransformerConfigSerializer::isIdCorrect);
//            spec.define(KEY_NAME, () -> getDefault().getName().getString(), String.class::isInstance);
            defineEnum(spec, KEY_TYPE, getDefault().getType());
            defineVector3d(spec, KEY_OFFSET, ArrayTransformer.ZERO.offset(), ArrayTransformer.OFFSET_BOUND);
            spec.defineInRange(KEY_COUNT, getDefault().count(), ArrayTransformer.MIN_COUNT, ArrayTransformer.MAX_COUNT);
            return spec;
        }

        @Override
        public ArrayTransformer getDefault() {
            return ArrayTransformer.ZERO;
        }

        @Override
        public ArrayTransformer deserialize(Config config) {
            validate(config);
            return new ArrayTransformer(
                    UUID.fromString(config.get(KEY_ID)),
                    Text.empty(),
                    getVector3d(config, KEY_OFFSET),
                    config.get(KEY_COUNT)
            );
        }

        @Override
        public Config serialize(ArrayTransformer transformer) {
            var config = Config.inMemory();
            config.set(KEY_ID, transformer.getId().toString());
//            config.set(KEY_NAME, transformer.getName().getString());
            setEnum(config, KEY_TYPE, transformer.getType());
            setVector3d(config, KEY_OFFSET, transformer.offset());
            config.set(KEY_COUNT, transformer.count());
            validate(config);
            return config;
        }

    }

    public static class MirrorTransformerConfigSerializer implements ConfigSerializer<MirrorTransformer> {

        private static final String KEY_POSITION = "position";
        private static final String KEY_POSITION_TYPE = "positionType";
        private static final String KEY_AXIS = "axis";
        public static MirrorTransformerConfigSerializer INSTANCE = new MirrorTransformerConfigSerializer();

        private MirrorTransformerConfigSerializer() {
        }

        @Override
        public ConfigSpec getSpec(Config config) {
            var spec = new ConfigSpec();
            spec.define(KEY_ID, TransformerConfigSerializer::randomIdString, TransformerConfigSerializer::isIdCorrect);
//            spec.define(KEY_NAME, () -> getDefault().getName().getString(), String.class::isInstance);
            defineEnum(spec, KEY_TYPE, getDefault().getType());
            defineVector3d(spec, KEY_POSITION, MirrorTransformer.ZERO_Y.position());
            definePositionType(spec, KEY_POSITION_TYPE, getDefault().getPositionType());
            defineEnum(spec, KEY_AXIS, getDefault().axis());

            return spec;
        }

        @Override
        public MirrorTransformer getDefault() {
            return MirrorTransformer.ZERO_Y;
        }

        @Override
        public MirrorTransformer deserialize(Config config) {
            validate(config);
            return new MirrorTransformer(
                    UUID.fromString(config.get(KEY_ID)),
                    Text.empty(),
                    getVector3d(config, KEY_POSITION),
                    getPositionType(config, KEY_POSITION_TYPE),
                    getEnum(config, KEY_AXIS)
            );
        }

        @Override
        public Config serialize(MirrorTransformer transformer) {
            var config = Config.inMemory();
            config.set(KEY_ID, transformer.getId().toString());
//            config.set(KEY_NAME, transformer.getName().getString());
            setEnum(config, KEY_TYPE, transformer.getType());
            setVector3d(config, KEY_POSITION, transformer.position());
            setPositionType(config, KEY_POSITION_TYPE, transformer.getPositionType());
            config.set(KEY_AXIS, transformer.axis().name().toLowerCase(Locale.ROOT));
            validate(config);
            return config;
        }

    }

    public static class RadialTransformerConfigSerializer implements ConfigSerializer<RadialTransformer> {

        private static final String KEY_POSITION = "position";
        private static final String KEY_POSITION_TYPE = "positionType";
        private static final String KEY_SLICE = "slices";
        public static RadialTransformerConfigSerializer INSTANCE = new RadialTransformerConfigSerializer();

        private RadialTransformerConfigSerializer() {
        }

        @Override
        public ConfigSpec getSpec(Config config) {
            var spec = new ConfigSpec();
            spec.define(KEY_ID, TransformerConfigSerializer::randomIdString, TransformerConfigSerializer::isIdCorrect);
//            spec.define(KEY_NAME, () -> getDefault().getName().getString(), String.class::isInstance);
            defineEnum(spec, KEY_TYPE, getDefault().getType());
            defineVector3d(spec, KEY_POSITION, RadialTransformer.ZERO.position());
            definePositionType(spec, KEY_POSITION_TYPE, getDefault().getPositionType());
            spec.defineInRange(KEY_SLICE, getDefault().slices(), RadialTransformer.SLICE_RANGE.min(), RadialTransformer.SLICE_RANGE.max());

            return spec;
        }

        @Override
        public RadialTransformer getDefault() {
            return RadialTransformer.ZERO;
        }

        @Override
        public RadialTransformer deserialize(Config config) {
            validate(config);
            return new RadialTransformer(
                    UUID.fromString(config.get(KEY_ID)),
                    Text.empty(),
                    getVector3d(config, KEY_POSITION),
                    getPositionType(config, KEY_POSITION_TYPE),
                    config.get(KEY_SLICE)
            );
        }

        @Override
        public Config serialize(RadialTransformer transformer) {
            var config = Config.inMemory();
            config.set(KEY_ID, transformer.getId().toString());
//            config.set(KEY_NAME, transformer.getName().getString());
            setEnum(config, KEY_TYPE, transformer.getType());
            setVector3d(config, KEY_POSITION, transformer.position());
            setPositionType(config, KEY_POSITION_TYPE, transformer.getPositionType());
            config.set(KEY_SLICE, transformer.slices());
            validate(config);
            return config;
        }

    }

    public static class ItemRandomizerConfigSerializer implements ConfigSerializer<ItemRandomizer> {

        private static final String KEY_ORDER = "order";
        private static final String KEY_SUPPLIER = "supplier";
        private static final String KEY_TARGET = "target";
        private static final String KEY_CHANCES = "chances";
        public static ItemRandomizerConfigSerializer INSTANCE = new ItemRandomizerConfigSerializer();

        private ItemRandomizerConfigSerializer() {
        }

        @Override
        public ConfigSpec getSpec(Config config) {
            var spec = new ConfigSpec();
            spec.define(KEY_ID, TransformerConfigSerializer::randomIdString, TransformerConfigSerializer::isIdCorrect);
//            spec.define(KEY_NAME, () -> getDefault().getName().getString(), String.class::isInstance);
            defineEnum(spec, KEY_TYPE, getDefault().getType());
            defineEnum(spec, KEY_ORDER, getDefault().getOrder());
            defineEnum(spec, KEY_SUPPLIER, getDefault().getTarget());
            defineEnum(spec, KEY_TARGET, getDefault().getCategory());
            spec.defineList(KEY_CHANCES, () -> getDefault().getChances().stream().map(ItemChanceConfigSerializer.INSTANCE::serialize).toList(), Config.class::isInstance);
            return spec;
        }

        @Override
        public ItemRandomizer getDefault() {
            return ItemRandomizer.EMPTY;
        }

        @Override
        public ItemRandomizer deserialize(Config config) {
            validate(config);
            return new ItemRandomizer(
                    UUID.fromString(config.get(KEY_ID)),
                    Text.empty(),
                    getEnum(config, KEY_ORDER),
                    getEnum(config, KEY_SUPPLIER),
                    getEnum(config, KEY_TARGET),
                    config.<List<Config>>get(KEY_CHANCES).stream().map(ItemChanceConfigSerializer.INSTANCE::deserialize).toList()
            );
        }

        @Override
        public Config serialize(ItemRandomizer transformer) {
            var config = Config.inMemory();
            config.set(KEY_ID, transformer.getId().toString());
//            config.set(KEY_NAME, transformer.getName().getString());
            setEnum(config, KEY_TYPE, transformer.getType());
            setEnum(config, KEY_ORDER, transformer.getOrder());
            setEnum(config, KEY_SUPPLIER, transformer.getTarget());
            setEnum(config, KEY_TARGET, transformer.getCategory());
            config.set(KEY_CHANCES, transformer.getChances().stream().map(ItemChanceConfigSerializer.INSTANCE::serialize).toList());
            validate(config);
            return config;
        }

    }

    public static class ItemChanceConfigSerializer implements ConfigSerializer<Chance<Item>> {

        private static final String KEY_CONTENT = "content";
        private static final String KEY_CHANCE = "chance";
        public static ItemChanceConfigSerializer INSTANCE = new ItemChanceConfigSerializer();

        private ItemChanceConfigSerializer() {
        }

        @Override
        public ConfigSpec getSpec(Config config) {
            var spec = new ConfigSpec();
            spec.define(KEY_CONTENT, () -> getDefault().content().getId().getString(), predicate((value) -> Item.fromId(ResourceLocation.decompose((String) value))));
            spec.defineInRange(KEY_CHANCE, getDefault().chance(), Chance.MIN_ITEM_COUNT, Chance.MAX_ITEM_COUNT);
            return spec;
        }

        @Override
        public Chance<Item> getDefault() {
            return Chance.item(Items.AIR, (byte) 0);
        }

        @Override
        public Chance<Item> deserialize(Config config) {
            validate(config);
            return Chance.item(
                    Item.fromId(ResourceLocation.decompose(config.get(KEY_CONTENT))),
                    config.getInt(KEY_CHANCE)
            );
        }

        @Override
        public Config serialize(Chance<Item> itemChance) {
            var config = Config.inMemory();
            config.set(KEY_CONTENT, itemChance.content().getId().getString());
            config.set(KEY_CHANCE, itemChance.chance());
            validate(config);
            return config;
        }
    }

}
