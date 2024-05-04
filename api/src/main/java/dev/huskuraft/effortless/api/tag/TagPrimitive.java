package dev.huskuraft.effortless.api.tag;

import dev.huskuraft.effortless.api.platform.TagFactory;

public interface TagPrimitive extends TagElement {


    static TagPrimitive of(boolean value) {
        return TagFactory.getInstance().newPrimitive(value);
    }
    static TagPrimitive of(byte value) {
        return TagFactory.getInstance().newPrimitive(value);
    }
    static TagPrimitive of(short value) {
        return TagFactory.getInstance().newPrimitive(value);
    }
    static TagPrimitive of(int value) {
        return TagFactory.getInstance().newPrimitive(value);
    }
    static TagPrimitive of(long value) {
        return TagFactory.getInstance().newPrimitive(value);
    }
    static TagPrimitive of(float value) {
        return TagFactory.getInstance().newPrimitive(value);
    }
    static TagPrimitive of(double value) {
        return TagFactory.getInstance().newPrimitive(value);
    }

    long getAsLong();

    int getAsInt();

    short getAsShort();

    byte getAsByte();

    double getAsDouble();

    float getAsFloat();

    Number getAsNumber();

//    default <T extends Enum<T>> void putEnum(Enum<T> value) {
//        var id = ResourceLocation.of("effortless", value.name().toLowerCase(Locale.ROOT));
//        putString(id.toString());
//    }
//
//    default <T extends Enum<T>> T getEnum(Class<T> clazz) {
//        var id = ResourceLocation.decompose(getString());
//        return Enum.valueOf(clazz, id.getPath().toUpperCase(Locale.ROOT));
//    }

}
