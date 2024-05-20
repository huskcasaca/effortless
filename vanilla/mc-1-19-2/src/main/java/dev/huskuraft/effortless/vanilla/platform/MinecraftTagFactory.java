package dev.huskuraft.effortless.vanilla.platform;

import com.google.auto.service.AutoService;

import dev.huskuraft.effortless.api.platform.TagFactory;
import dev.huskuraft.effortless.api.tag.TagList;
import dev.huskuraft.effortless.api.tag.TagLiteral;
import dev.huskuraft.effortless.api.tag.TagPrimitive;
import dev.huskuraft.effortless.api.tag.TagRecord;
import dev.huskuraft.effortless.vanilla.tag.MinecraftTagList;
import dev.huskuraft.effortless.vanilla.tag.MinecraftTagLiteral;
import dev.huskuraft.effortless.vanilla.tag.MinecraftTagPrimitive;
import dev.huskuraft.effortless.vanilla.tag.MinecraftTagRecord;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.ShortTag;
import net.minecraft.nbt.StringTag;

@AutoService(TagFactory.class)
public final class MinecraftTagFactory implements TagFactory {

    @Override
    public TagRecord newRecord() {
        return new MinecraftTagRecord(new CompoundTag());
    }

    @Override
    public TagList newList() {
        return new MinecraftTagList(new ListTag());
    }

    @Override
    public TagLiteral newLiteral(String value) {
        return new MinecraftTagLiteral(StringTag.valueOf(value));
    }

    @Override
    public TagPrimitive newPrimitive(boolean value) {
        return new MinecraftTagPrimitive(ByteTag.valueOf(value));
    }

    @Override
    public TagPrimitive newPrimitive(byte value) {
        return new MinecraftTagPrimitive(ByteTag.valueOf(value));
    }

    @Override
    public TagPrimitive newPrimitive(short value) {
        return new MinecraftTagPrimitive(ShortTag.valueOf(value));
    }

    @Override
    public TagPrimitive newPrimitive(int value) {
        return new MinecraftTagPrimitive(IntTag.valueOf(value));
    }

    @Override
    public TagPrimitive newPrimitive(long value) {
        return new MinecraftTagPrimitive(LongTag.valueOf(value));
    }

    @Override
    public TagPrimitive newPrimitive(float value) {
        return new MinecraftTagPrimitive(FloatTag.valueOf(value));
    }

    @Override
    public TagPrimitive newPrimitive(double value) {
        return new MinecraftTagPrimitive(DoubleTag.valueOf(value));
    }
}
