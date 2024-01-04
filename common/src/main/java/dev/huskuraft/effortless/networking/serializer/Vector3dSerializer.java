package dev.huskuraft.effortless.networking.serializer;

import dev.huskuraft.effortless.api.math.Vector3d;
import dev.huskuraft.effortless.api.networking.Buffer;
import dev.huskuraft.effortless.api.networking.BufferSerializer;

public class Vector3dSerializer extends BufferSerializer<Vector3d> {

    @Override
    public Vector3d read(Buffer buffer) {
        return Vector3d.at(
                buffer.readDouble(),
                buffer.readDouble(),
                buffer.readDouble()
        );
    }


    @Override
    public void write(Buffer buffer, Vector3d vector) {
        buffer.writeDouble(vector.x());
        buffer.writeDouble(vector.y());
        buffer.writeDouble(vector.z());
    }

}
