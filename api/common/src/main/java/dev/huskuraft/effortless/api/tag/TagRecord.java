package dev.huskuraft.effortless.api.tag;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import dev.huskuraft.effortless.api.core.Item;
import dev.huskuraft.effortless.api.core.ItemStack;
import dev.huskuraft.effortless.api.core.ResourceLocation;
import dev.huskuraft.effortless.api.math.Vector2d;
import dev.huskuraft.effortless.api.math.Vector2i;
import dev.huskuraft.effortless.api.math.Vector3d;
import dev.huskuraft.effortless.api.math.Vector3i;
import dev.huskuraft.effortless.api.text.Text;

public interface TagRecord extends TagElement {

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

    <T> List<T> getList(String key, TagReader<T> writer);

    <T> void putList(String key, Collection<T> collection, TagWriter<T> writer);

    default <T> T get(TagReader<T> writer) {
        return writer.read(this);
    }

    default <T> void put(T value, TagWriter<T> writer) {
        writer.write(this, value);
    }

    default <T extends Enum<T>> T getEnum(String key, Class<T> clazz) {
        var id = ResourceLocation.decompose(getString(key));
        return Enum.valueOf(clazz, id.getPath().toUpperCase(Locale.ROOT));
    }

    default <T extends Enum<T>> void putEnum(String key, Enum<T> value) {
        var id = ResourceLocation.of("effortless", value.name().toLowerCase(Locale.ROOT));
        putString(key, id.toString());
    }

    default ResourceLocation getResource(String key) {
        return ResourceLocation.decompose(getString(key));
    }

    default void putResource(String key, ResourceLocation value) {
        putString(key, value.toString());
    }

    default UUID getUUID(String key) {
        return UUID.fromString(getString(key));
    }

    default void putUUID(String key, UUID value) {
        putString(key, value.toString());
    }

    default Item getItem(String key) {
        return Item.fromId(getResource(key));
    }

    default void putItem(String key, Item value) {
        putResource(key, value.getId());
    }

    default ItemStack getItemStack(String key) {
        return getElement(key, (tag1) -> ItemStack.of(
                tag1.asRecord().getItem("Item"),
                tag1.asRecord().getInt("Count"),
                tag1.asRecord().getElement("Tag").asRecord()
        ));
    }

    default void putItemStack(String key, ItemStack value) {
        putElement(key, value, (tag1, itemStack) -> {
            tag1.asRecord().putItem("Item", itemStack.getItem());
            tag1.asRecord().putInt("Count", itemStack.getStackSize());
            tag1.asRecord().putElement("Tag", itemStack.getTag());
        });
    }

    default Vector3d getVector3d(String key) {
        var positions = getList(key, (tag1) -> tag1.asPrimitive().getDouble()).stream().mapToDouble(Double::doubleValue).toArray();
        return new Vector3d(positions[0], positions[1], positions[2]);
    }

    default void putVector3d(String key, Vector3d value) {
        putList(key, List.of(value.x(), value.y(), value.z()), (tag1, value1) -> {
            tag1.asPrimitive().putDouble(value1);
        });
    }

    default Vector3i getVector3i(String key) {
        var positions = getList(key, (tag1) -> tag1.asPrimitive().getInt()).stream().mapToInt(Integer::intValue).toArray();
        return new Vector3i(positions[0], positions[1], positions[2]);
    }

    default void putVector3i(String key, Vector3i value) {
        putList(key, List.of(value.x(), value.y(), value.z()), (tag1, value1) -> {
            tag1.asPrimitive().putInt(value1);
        });
    }

    default Vector2d getVector2d(String key) {
        var positions = getList(key, (tag1) -> tag1.asPrimitive().getDouble()).stream().mapToDouble(Double::doubleValue).toArray();
        return new Vector2d(positions[0], positions[1]);
    }

    default void putVector2d(String key, Vector2d value) {
        putList(key, List.of(value.x(), value.y()), (tag1, value1) -> {
            tag1.asPrimitive().putDouble(value1);
        });
    }

    default Vector2i getVector2i(String key) {
        var positions = getList(key, (tag1) -> tag1.asPrimitive().getInt()).stream().mapToInt(Integer::intValue).toArray();
        return new Vector2i(positions[0], positions[1]);
    }

    default void putVector2i(String key, Vector2i value) {
        putList(key, List.of(value.x(), value.y()), (tag1, value1) -> {
            tag1.asPrimitive().putInt(value1);
        });
    }

}
