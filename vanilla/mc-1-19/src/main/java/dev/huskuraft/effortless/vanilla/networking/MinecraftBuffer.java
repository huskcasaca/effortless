package dev.huskuraft.effortless.vanilla.networking;

import dev.huskuraft.effortless.api.core.Item;
import dev.huskuraft.effortless.api.core.ItemStack;
import dev.huskuraft.effortless.api.networking.Buffer;
import dev.huskuraft.effortless.api.tag.TagRecord;
import dev.huskuraft.effortless.vanilla.core.MinecraftItem;
import dev.huskuraft.effortless.vanilla.core.MinecraftItemStack;
import dev.huskuraft.effortless.vanilla.tag.MinecraftTagRecord;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.network.FriendlyByteBuf;

public class MinecraftBuffer implements Buffer {

    private final FriendlyByteBuf reference;

    public MinecraftBuffer(FriendlyByteBuf reference) {
        this.reference = reference;
    }

    @Override
    public FriendlyByteBuf referenceValue() {
        return reference;
    }

    @Override
    public String readString() {
        return reference.readUtf();
    }

    @Override
    public boolean readBoolean() {
        return reference.readBoolean();
    }

    @Override
    public byte readByte() {
        return reference.readByte();
    }

    @Override
    public short readShort() {
        return reference.readShort();
    }

    @Override
    public int readInt() {
        return reference.readInt();
    }

    @Override
    public int readVarInt() {
        return reference.readVarInt();
    }

    @Override
    public long readLong() {
        return reference.readLong();
    }

    @Override
    public long readVarLong() {
        return reference.readVarLong();
    }

    @Override
    public float readFloat() {
        return reference.readFloat();
    }

    @Override
    public double readDouble() {
        return reference.readDouble();
    }

    @Override
    public Item readItem() {
        return new MinecraftItem(reference.readById(DefaultedRegistry.ITEM));
    }

    @Override
    public ItemStack readItemStack() {
        return new MinecraftItemStack(reference.readItem());
    }

    @Override
    public TagRecord readTagRecord() {
        return new MinecraftTagRecord(reference.readNbt());
    }

    @Override
    public void writeString(String value) {
        reference.writeUtf(value);
    }

    @Override
    public void writeBoolean(boolean value) {
        reference.writeBoolean(value);
    }

    @Override
    public void writeByte(byte value) {
        reference.writeByte(value);
    }

    @Override
    public void writeShort(short value) {
        reference.writeShort(value);
    }

    @Override
    public void writeInt(int value) {
        reference.writeInt(value);
    }

    @Override
    public void writeVarInt(int value) {
        reference.writeVarInt(value);
    }

    @Override
    public void writeLong(long value) {
        reference.writeLong(value);
    }

    @Override
    public void writeVarLong(long value) {
        reference.writeVarLong(value);
    }

    @Override
    public void writeFloat(float value) {
        reference.writeFloat(value);
    }

    @Override
    public void writeDouble(double value) {
        reference.writeDouble(value);
    }

    @Override
    public void writeItem(Item value) {
        reference.writeId(DefaultedRegistry.ITEM, value.reference());
    }

    @Override
    public void writeItemStack(ItemStack value) {
        reference.writeItem(value.reference());
    }

    @Override
    public void writeTagRecord(TagRecord value) {
        reference.writeNbt(value.reference());
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof MinecraftBuffer buffer && reference.equals(buffer.reference);
    }

    @Override
    public int hashCode() {
        return reference.hashCode();
    }

}
