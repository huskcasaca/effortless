package dev.huskuraft.effortless.api.platform;

import dev.huskuraft.effortless.api.tag.TagList;
import dev.huskuraft.effortless.api.tag.TagLiteral;
import dev.huskuraft.effortless.api.tag.TagPrimitive;
import dev.huskuraft.effortless.api.tag.TagRecord;

public interface TagFactory {

    static TagFactory getInstance() {
        return PlatformLoader.getSingleton();
    }

    TagRecord newRecord();

    TagList newList();

    TagLiteral newLiteral(String value);

    TagPrimitive newPrimitive(boolean value);

    TagPrimitive newPrimitive(byte value);

    TagPrimitive newPrimitive(short value);

    TagPrimitive newPrimitive(int value);

    TagPrimitive newPrimitive(long value);

    TagPrimitive newPrimitive(float value);

    TagPrimitive newPrimitive(double value);


}
