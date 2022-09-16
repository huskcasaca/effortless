package dev.huskuraft.effortless.config.serializer;

import dev.huskuraft.effortless.math.Vector3i;
import dev.huskuraft.effortless.tag.TagElement;
import dev.huskuraft.effortless.tag.TagSerializer;

public class Vector3iSerializer extends TagSerializer<Vector3i> {

    private static final String TAG_X = "X";
    private static final String TAG_Y = "Y";
    private static final String TAG_Z = "Z";

    @Override
    public Vector3i read(TagElement tag) {
        return Vector3i.at(
                tag.getAsRecord().getInt(TAG_X),
                tag.getAsRecord().getInt(TAG_Y),
                tag.getAsRecord().getInt(TAG_Z)
        );
    }

    @Override
    public void write(TagElement tag, Vector3i vector) {
        tag.getAsRecord().putInt(TAG_X, vector.getX());
        tag.getAsRecord().putInt(TAG_Y, vector.getY());
        tag.getAsRecord().putInt(TAG_Z, vector.getZ());
    }

}