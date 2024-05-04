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
import dev.huskuraft.effortless.api.platform.TagFactory;
import dev.huskuraft.effortless.api.text.Text;

public interface TagRecord extends TagElement {

    static TagRecord newRecord() {
        return TagFactory.getInstance().newRecord();
    }


    TagElement get(String key);

    TagElement put(String key, TagElement value);

    void remove(String key);




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
            var positions = getList(key, tag1 -> tag1.asPrimitive().getAsDouble());
            return new Vector3d(positions.get(0), positions.get(1), positions.get(2));
        } catch (Exception e) {
            return null;
        }
    }

    default void putVector3d(String key, Vector3d value) {
        putList(key, List.of(value.x(), value.y(), value.z()), TagPrimitive::of);;
    }

    default Vector3i getVector3i(String key) {
        try {
            var positions = getList(key, tag1 -> tag1.asPrimitive().getAsInt());
            return new Vector3i(positions.get(0), positions.get(1), positions.get(2));
        } catch (Exception e) {
            return null;
        }
    }

    default void putVector3i(String key, Vector3i value) {
        putList(key, List.of(value.x(), value.y(), value.z()), TagPrimitive::of);
    }

    default Vector2d getVector2d(String key) {
        try {
            var positions = getList(key, tag1 -> tag1.asPrimitive().getAsDouble());
            return new Vector2d(positions.get(0), positions.get(1));
        } catch (Exception e) {
            return null;
        }
    }

    default void putVector2d(String key, Vector2d value) {
        putList(key, List.of(value.x(), value.y()), TagPrimitive::of);
    }

    default Vector2i getVector2i(String key) {
        try {
            var positions = getList(key, tag1 -> tag1.asPrimitive().getAsInt());
            return new Vector2i(positions.get(0), positions.get(1));
        } catch (Exception e) {
            return null;
        }
    }

    default void putVector2i(String key, Vector2i value) {
        putList(key, List.of(value.x(), value.y()), TagPrimitive::of);
    }

    default Text getText(String key) {
        return Text.text(get(key).asLiteral().getAsString());
    }

    default void putText(String key, Text value) {
        putString(key, value.getString());
    }








    default String getString(String key) {
        return get(key).asPrimitive().getAsString();
    }

    default void putString(String key, String value) {
        put(key, TagLiteral.of(value));
    }

    default boolean getBoolean(String key) {
        return get(key).asPrimitive().getAsByte() != 0;
    }

    default void putBoolean(String key, boolean value) {
        put(key, TagPrimitive.of(value));
    }

    default byte getByte(String key) {
        return get(key).asPrimitive().getAsByte();
    }

    default void putByte(String key, byte value) {
        put(key, TagPrimitive.of(value));
    }

    default short getShort(String key) {
        return get(key).asPrimitive().getAsShort();
    }

    default void putShort(String key, short value) {
        put(key, TagPrimitive.of(value));
    }

    default int getInt(String key) {
        return get(key).asPrimitive().getAsInt();
    }

    default void putInt(String key, int value) {
        put(key, TagPrimitive.of(value));
    }

    default long getLong(String key) {
        return get(key).asPrimitive().getAsLong();
    }

    default void putLong(String key, long value) {
        put(key, TagPrimitive.of(value));
    }

    default float getFloat(String key) {
        return get(key).asPrimitive().getAsFloat();
    }

    default void putFloat(String key, float value) {
        put(key, TagPrimitive.of(value));
    }

    default double getDouble(String key) {
        return get(key).asPrimitive().getAsDouble();
    }

    default void putDouble(String key, double value) {
        put(key, TagPrimitive.of(value));
    }

    default boolean[] getBooleanArray(String key) {
        throw new NotImplementedException("getBooleanArray is not implemented yet");
    }

    default void putBooleanArray(String key, boolean[] value) {
        throw new NotImplementedException("putBooleanArray is not implemented yet");
    }

    default byte[] getByteArray(String key) {
        throw new NotImplementedException("getByteArray is not implemented yet");
    }

    default void putByteArray(String key, byte[] value) {
        throw new NotImplementedException("putByteArray is not implemented yet");
    }

    default short[] getShortArray(String key) {
        throw new NotImplementedException("getShortArray is not implemented yet");
    }

    default void putShortArray(String key, short[] value) {
        throw new NotImplementedException("putShortArray is not implemented yet");
    }

    default int[] getIntArray(String key) {
        throw new NotImplementedException("getIntArray is not implemented yet");
    }

    default void putIntArray(String key, int[] value) {
        throw new NotImplementedException("putIntArray is not implemented yet");
    }

    default long[] getLongArray(String key) {
        throw new NotImplementedException("getLongArray is not implemented yet");
    }

    default void putLongArray(String key, long[] value) {
        throw new NotImplementedException("putLongArray is not implemented yet");
    }

    default float[] getFloatArray(String key) {
        throw new NotImplementedException("getFloatArray is not implemented yet");
    }

    default void putFloatArray(String key, float[] value) {
        throw new NotImplementedException("putFloatArray is not implemented yet");
    }

    default double[] getDoubleArray(String key) {
        throw new NotImplementedException("getDoubleArray is not implemented yet");
    }

    default void putDoubleArray(String key, double[] value) {
        throw new NotImplementedException("putDoubleArray is not implemented yet");
    }

    @Deprecated
    default TagElement getElement(String key) {
        return get(key);
    }

    @Deprecated
    default void putElement(String key, TagElement value) {
        put(key, value);
    }

    default <T> T getElement(String key, TagDecoder<T> decoder) {
        return decoder.decode(get(key), true);
    }

    default <T> void putElement(String key, T value, TagEncoder<T> encoder) {
        put(key, encoder.encode(value));
    }

    default <T> List<T> getList(String key, TagDecoder<T> reader) {
        return get(key).asList().stream().map(reader::decode).toList();
    }

    default <T> void putList(String key, Collection<T> collection, TagEncoder<T> writer) {
        put(key, TagList.of(collection.stream().map(writer::encode).toList()));
    }

    class NotImplementedException extends RuntimeException {

        NotImplementedException(String message) {
            super(message);
        }
    }
}
