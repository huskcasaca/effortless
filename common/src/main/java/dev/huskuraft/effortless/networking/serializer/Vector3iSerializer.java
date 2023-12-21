package dev.huskuraft.effortless.networking.serializer;

import dev.huskuraft.effortless.math.Vector3i;
import dev.huskuraft.effortless.networking.Buffer;
import dev.huskuraft.effortless.networking.BufferSerializer;

public class Vector3iSerializer extends BufferSerializer<Vector3i> {

    @Override
    public Vector3i read(Buffer buffer) {
        return Vector3i.at(
                buffer.readInt(),
                buffer.readInt(),
                buffer.readInt()
        );
    }


    @Override
    public void write(Buffer buffer, Vector3i vector) {
        buffer.writeInt(vector.x());
        buffer.writeInt(vector.y());
        buffer.writeInt(vector.z());
    }

}
