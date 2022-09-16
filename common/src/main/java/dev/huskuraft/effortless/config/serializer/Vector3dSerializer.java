package dev.huskuraft.effortless.config.serializer;

import dev.huskuraft.effortless.math.Vector3d;
import dev.huskuraft.effortless.tag.TagElement;
import dev.huskuraft.effortless.tag.TagSerializer;

public class Vector3dSerializer extends TagSerializer<Vector3d> {

    private static final String TAG_X = "X";
    private static final String TAG_Y = "Y";
    private static final String TAG_Z = "Z";

    @Override
    public Vector3d read(TagElement tag) {
        return Vector3d.at(
                tag.getAsRecord().getDouble(TAG_X),
                tag.getAsRecord().getDouble(TAG_Y),
                tag.getAsRecord().getDouble(TAG_Z)
        );
    }

    @Override
    public void write(TagElement tag, Vector3d vector) {
        tag.getAsRecord().putDouble(TAG_X, vector.getX());
        tag.getAsRecord().putDouble(TAG_Y, vector.getY());
        tag.getAsRecord().putDouble(TAG_Z, vector.getZ());
    }

}