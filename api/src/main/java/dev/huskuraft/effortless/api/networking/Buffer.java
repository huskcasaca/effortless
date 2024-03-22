package dev.huskuraft.effortless.api.networking;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import dev.huskuraft.effortless.api.core.Item;
import dev.huskuraft.effortless.api.core.ItemStack;
import dev.huskuraft.effortless.api.core.ResourceLocation;
import dev.huskuraft.effortless.api.math.Vector2d;
import dev.huskuraft.effortless.api.math.Vector2i;
import dev.huskuraft.effortless.api.math.Vector3d;
import dev.huskuraft.effortless.api.math.Vector3i;
import dev.huskuraft.effortless.api.platform.ContentFactory;
import dev.huskuraft.effortless.api.platform.PlatformReference;
import dev.huskuraft.effortless.api.tag.TagRecord;
import dev.huskuraft.effortless.api.text.Text;

public interface Buffer extends PlatformReference {

    static Buffer newBuffer() {
        return ContentFactory.getInstance().newBuffer();
    }

    default <T> T readNullable(BufferReader<T> reader) {
        if (readBoolean()) return read(reader);
        return null;
    }

    UUID readUUID();

    <T extends Enum<T>> T readEnum(Class<T> clazz);

    String readString();

    Text readText();

    boolean readBoolean();

    byte readByte();

    short readShort();

    int readInt();

    int readVarInt();

    long readLong();

    long readVarLong();

    float readFloat();

//    boolean[] readBooleanArray();
//
//    byte[] readByteArray();
//
//    short[] readShortArray();
//
//    int[] readIntArray();
//
//    long[] readLongArray();
//
//    float[] readFloatArray();
//
//    double[] readDoubleArray();

    double readDouble();

    @Deprecated
        // use Registries
    Item readItem();

    ItemStack readItemStack();

    TagRecord readTagRecord();

    default <T> T read(BufferReader<T> reader) {
        return reader.read(this);
    }

    default <T> List<T> readList(BufferReader<T> reader) {
        var i = readInt();
        var list = new ArrayList<T>();

        for (int j = 0; j < i; ++j) {
            list.add(read(reader));
        }
        return Collections.unmodifiableList(list);
    }

    default <K, V> Map<K, V> readMap(BufferReader<K> keyReader, BufferReader<V> valueReader) {
        var i = readInt();
        var map = new LinkedHashMap<K, V>();

        for (int j = 0; j < i; ++j) {
            map.put(read(keyReader), read(valueReader));
        }
        return Collections.unmodifiableMap(map);
    }

    default <T> void writeNullable(T value, BufferWriter<T> writer) {
        writeBoolean(value != null);
        if (value != null) write(value, writer);
    }

    void writeUUID(UUID uuid);

    <T extends Enum<T>> void writeEnum(Enum<T> value);

    void writeString(String value);

    void writeText(Text value);

    void writeBoolean(boolean value);

    void writeByte(byte value);

    void writeShort(short value);

    void writeInt(int value);

    void writeVarInt(int value);

    void writeLong(long value);

    void writeVarLong(long value);

    void writeFloat(float value);

//    void writeBooleanArray(boolean[] value);
//
//    void writeByteArray(byte[] value);
//
//    void writeShortArray(short[] value);
//
//    void writeIntArray(int[] value);
//
//    void writeLongArray(long[] value);
//
//    void writeFloatArray(float[] value);
//
//    void writeDoubleArray(double[] value);

    void writeDouble(double value);

    @Deprecated
        // use Registries
    void writeItem(Item value);

    // TODO: 7/12/23 extract
    void writeItemStack(ItemStack value);

    void writeTagRecord(TagRecord value);

    default <T> void write(T value, BufferWriter<T> writer) {
        writer.write(this, value);
    }

    default <T> void writeList(Collection<T> collection, BufferWriter<T> writer) {
        writeInt(collection.size());
        for (var object : collection) {
            write(object, writer);
        }
    }

    default <K, V> void writeMap(Map<K, V> map, BufferWriter<K> keyWriter, BufferWriter<V> valueWriter) {
        writeInt(map.size());
        for (var entry : map.entrySet()) {
            keyWriter.write(this, entry.getKey());
            valueWriter.write(this, entry.getValue());
        }
    }

    default Vector3d readVector3d() {
        return new Vector3d(readDouble(), readDouble(), readDouble());
    }

    default void writeVector3d(Vector3d vector) {
        writeDouble(vector.x());
        writeDouble(vector.y());
        writeDouble(vector.z());
    }

    default Vector2d readVector2d() {
        return new Vector2d(readDouble(), readDouble());
    }

    default void writeVector2d(Vector2d vector) {
        writeDouble(vector.x());
        writeDouble(vector.y());
    }

    default Vector3i readVector3i() {
        return new Vector3i(readInt(), readInt(), readInt());
    }

    default void writeVector3i(Vector3i vector) {
        writeInt(vector.x());
        writeInt(vector.y());
        writeInt(vector.z());
    }

    default Vector2i readVector2i() {
        return new Vector2i(readInt(), readInt());
    }

    default void writeVector2i(Vector2i vector) {
        writeInt(vector.x());
        writeInt(vector.y());
    }

    default ResourceLocation readResourceLocation() {
        return ResourceLocation.of(readString(), readString());
    }

    default void writeResourceLocation(ResourceLocation resourceLocation) {
        writeString(resourceLocation.getNamespace());
        writeString(resourceLocation.getPath());
    }

}
