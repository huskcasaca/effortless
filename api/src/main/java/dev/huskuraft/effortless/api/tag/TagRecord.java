package dev.huskuraft.effortless.api.tag;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

import dev.huskuraft.effortless.api.core.Item;
import dev.huskuraft.effortless.api.core.ResourceLocation;
import dev.huskuraft.effortless.api.math.Vector2d;
import dev.huskuraft.effortless.api.math.Vector2i;
import dev.huskuraft.effortless.api.math.Vector3d;
import dev.huskuraft.effortless.api.math.Vector3i;
import dev.huskuraft.effortless.api.platform.ContentFactory;
import dev.huskuraft.effortless.api.text.Text;

public interface TagRecord extends TagElement {

    static TagRecord newRecord() {
        return ContentFactory.getInstance().newTagRecord();
    }

    String getString(String key);

    void putString(String key, String value);

    Text getText(String key);

    void putText(String key, Text value);

    boolean getBoolean(String key);

    void putBoolean(String key, boolean value);

    byte getByte(String key);

    void putByte(String key, byte value);

    short getShort(String key);

    void putShort(String key, short value);

    int getInt(String key);

    void putInt(String key, int value);

    long getLong(String key);

    void putLong(String key, long value);

    float getFloat(String key);

    void putFloat(String key, float value);

    double getDouble(String key);

    void putDouble(String key, double value);

    boolean[] getBooleanArray(String key);

    void putBooleanArray(String key, boolean[] value);

    byte[] getByteArray(String key);

    void putByteArray(String key, byte[] value);

    short[] getShortArray(String key);

    void putShortArray(String key, short[] value);

    int[] getIntArray(String key);

    void putIntArray(String key, int[] value);

    long[] getLongArray(String key);

    void putLongArray(String key, long[] value);

    float[] getFloatArray(String key);

    void putFloatArray(String key, float[] value);

    double[] getDoubleArray(String key);

    void putDoubleArray(String key, double[] value);

    TagElement getElement(String key);

    void putElement(String key, TagElement value);

    <T> T getElement(String key, TagReader<T> reader);

    <T> void putElement(String key, T value, TagWriter<T> writer);

    <T> List<T> getList(String key, TagReader<T> reader);

    <T> void putList(String key, Collection<T> collection, TagWriter<T> writer);

    default <T> T get(TagReader<T> reader) {
        return reader.read(this, true);
    }

    default <T> void put(T value, TagWriter<T> writer) {
        writer.write(this, value, true);
    }

    default <T extends Enum<T>> T getEnum(String key, Class<T> clazz) {
        try {
            var id = ResourceLocation.decompose(getString(key));
            return Enum.valueOf(clazz, id.getPath().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException | NullPointerException e) {
            return null;
        }
    }

    default <T extends Enum<T>> void putEnum(String key, Enum<T> value) {
        var id = ResourceLocation.of("effortless", value.name().toLowerCase(Locale.ROOT));
        putString(key, id.toString());
    }

    default ResourceLocation getResource(String key) {
        try {
            return ResourceLocation.decompose(getString(key));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    default void putResource(String key, ResourceLocation value) {
        putString(key, value.toString());
    }

    default UUID getUUID(String key) {
        try {
            return UUID.fromString(getString(key));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    default void putUUID(String key, UUID value) {
        putString(key, value.toString());
    }

    default Item getItem(String key) {
        try {
            return Item.fromId(Objects.requireNonNull(getResource(key)));
        } catch (IllegalArgumentException | NullPointerException e) {
            return null;
        }
    }

    default void putItem(String key, Item value) {
        putResource(key, value.getId());
    }

    default Vector3d getVector3d(String key) {
        try {
            var positions = getList(key, (tag1) -> tag1.asPrimitive().getDouble()).stream().mapToDouble(Double::doubleValue).toArray();
            return new Vector3d(positions[0], positions[1], positions[2]);
        } catch (Exception e) {
            return null;
        }
    }

    default void putVector3d(String key, Vector3d value) {
        putList(key, List.of(value.x(), value.y(), value.z()), (tag1, value1) -> {
            tag1.asPrimitive().putDouble(value1);
        });
    }

    default Vector3i getVector3i(String key) {
        try {
            var positions = getList(key, (tag1) -> tag1.asPrimitive().getInt()).stream().mapToInt(Integer::intValue).toArray();
            return new Vector3i(positions[0], positions[1], positions[2]);
        } catch (Exception e) {
            return null;
        }
    }

    default void putVector3i(String key, Vector3i value) {
        putList(key, List.of(value.x(), value.y(), value.z()), (tag1, value1) -> {
            tag1.asPrimitive().putInt(value1);
        });
    }

    default Vector2d getVector2d(String key) {
        try {
            var positions = getList(key, (tag1) -> tag1.asPrimitive().getDouble()).stream().mapToDouble(Double::doubleValue).toArray();
            return new Vector2d(positions[0], positions[1]);
        } catch (Exception e) {
            return null;
        }
    }

    default void putVector2d(String key, Vector2d value) {
        putList(key, List.of(value.x(), value.y()), (tag1, value1) -> {
            tag1.asPrimitive().putDouble(value1);
        });
    }

    default Vector2i getVector2i(String key) {
        try {
            var positions = getList(key, (tag1) -> tag1.asPrimitive().getInt()).stream().mapToInt(Integer::intValue).toArray();
            return new Vector2i(positions[0], positions[1]);
        } catch (Exception e) {
            return null;
        }
    }

    default void putVector2i(String key, Vector2i value) {
        putList(key, List.of(value.x(), value.y()), (tag1, value1) -> {
            tag1.asPrimitive().putInt(value1);
        });
    }

}
