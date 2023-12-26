package dev.huskuraft.effortless.vanilla.adapters;

import dev.huskuraft.effortless.core.Item;
import dev.huskuraft.effortless.core.ItemStack;
import dev.huskuraft.effortless.networking.Buffer;
import dev.huskuraft.effortless.tag.TagRecord;
import dev.huskuraft.effortless.text.Text;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;

import java.util.UUID;

public class MinecraftBuffer extends Buffer {

    private final FriendlyByteBuf buffer;

    public MinecraftBuffer(FriendlyByteBuf buffer) {
        this.buffer = buffer;
    }

    public FriendlyByteBuf getRef() {
        return buffer;
    }

    @Override
    public UUID readUUID() {
        return getRef().readUUID();
    }

    @Override
    public <T extends Enum<T>> T readEnum(Class<T> clazz) {
        return getRef().readEnum(clazz);
    }

    @Override
    public String readString() {
        return getRef().readUtf();
    }

    @Override
    public Text readText() {
        return MinecraftAdapter.adapt(getRef().readComponent());
    }

    @Override
    public boolean readBoolean() {
        return getRef().readBoolean();
    }

    @Override
    public byte readByte() {
        return getRef().readByte();
    }

    @Override
    public short readShort() {
        return getRef().readShort();
    }

    @Override
    public int readInt() {
        return getRef().readInt();
    }

    @Override
    public int readVarInt() {
        return getRef().readVarInt();
    }

    @Override
    public long readLong() {
        return getRef().readLong();
    }

    @Override
    public long readVarLong() {
        return getRef().readVarLong();
    }

    @Override
    public float readFloat() {
        return getRef().readFloat();
    }

    @Override
    public double readDouble() {
        return getRef().readDouble();
    }

    @Override
    public Item readItem() {
        return MinecraftAdapter.adapt(getRef().readById(BuiltInRegistries.ITEM));
    }

    @Override
    public ItemStack readItemStack() {
        return new MinecraftItemStack(getRef().readItem());
    }

    @Override
    public TagRecord readTagRecord() {
        return MinecraftAdapter.adapt(getRef().readNbt());
    }

    @Override
    public void writeUUID(UUID uuid) {
        getRef().writeUUID(uuid);
    }

    @Override
    public <T extends Enum<T>> void writeEnum(Enum<T> value) {
        getRef().writeEnum(value);
    }

    @Override
    public void writeString(String value) {
        getRef().writeUtf(value);
    }

    @Override
    public void writeText(Text value) {
        getRef().writeComponent(MinecraftAdapter.adapt(value));
    }

    @Override
    public void writeBoolean(boolean value) {
        getRef().writeBoolean(value);
    }

    @Override
    public void writeByte(byte value) {
        getRef().writeByte(value);
    }

    @Override
    public void writeShort(short value) {
        getRef().writeShort(value);
    }

    @Override
    public void writeInt(int value) {
        getRef().writeInt(value);
    }

    @Override
    public void writeVarInt(int value) {
        getRef().writeVarInt(value);
    }

    @Override
    public void writeLong(long value) {
        getRef().writeLong(value);
    }

    @Override
    public void writeVarLong(long value) {
        getRef().writeVarLong(value);
    }

    @Override
    public void writeFloat(float value) {
        getRef().writeFloat(value);
    }

    @Override
    public void writeDouble(double value) {
        getRef().writeDouble(value);
    }

    @Override
    public void writeItem(Item value) {
        getRef().writeId(BuiltInRegistries.ITEM, MinecraftAdapter.adapt(value));
    }

    @Override
    public void writeItemStack(ItemStack value) {
        getRef().writeItem(((MinecraftItemStack) value).getRef());
    }

    @Override
    public void writeTagRecord(TagRecord value) {
        getRef().writeNbt(MinecraftAdapter.adapt(value));

    }

}
