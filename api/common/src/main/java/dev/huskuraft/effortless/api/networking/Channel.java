package dev.huskuraft.effortless.api.networking;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.RejectedExecutionException;
import java.util.logging.Logger;

import javax.annotation.Nullable;

import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.core.ResourceLocation;

public abstract class Channel<P extends PacketListener> implements PacketSender, PacketReceiver {

    protected static final Logger LOGGER = Logger.getLogger("Effortless");

    protected final ResourceLocation channelId;
    protected PacketSet<P> packetSet = new PacketSet<>();
    private BufferSender platformSender;

    protected Channel(ResourceLocation channelId) {
        this.channelId = channelId;
    }

    @Override
    public void sendPacket(Packet packet, Player player) {
        sendBuffer(createBuffer(packet), player);
    }

    @Override
    public void sendBuffer(Buffer buffer, Player player) {
        platformSender.sendBuffer(buffer, player);
    }

    @Override
    public abstract void receivePacket(Packet packet, Player player);

    @Override
    public void receiveBuffer(Buffer buffer, Player player) {
        var packet = (Packet<P>) null;
        try {
            packet = createPacket(buffer);
            Objects.requireNonNull(packet);
        } catch (Exception e) {
            throw new RuntimeException("Could not create packet in channel '" + channelId + "'", e);
        }
        try {
            receivePacket(packet, player);
        } catch (RejectedExecutionException | ClassCastException ignored) {
        }
    }

    @SuppressWarnings("unchecked")
    public Packet<P> createPacket(Buffer buffer) {
        return (Packet<P>) packetSet.createPacket(buffer);
    }

    public Buffer createBuffer(Packet<P> packet) {
        return packetSet.createBuffer(packet);
    }

    public <T extends Packet<P>> void registerPacket(Class<T> clazz, BufferSerializer<T> serializer) {
        try {
            packetSet.addPacket(clazz, serializer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void unregisterPackets() {
        this.packetSet = new PacketSet<>();
    }

    public abstract int getCompatibilityVersion();

    public String getCompatibilityVersionStr() {
        return Integer.toString(getCompatibilityVersion());
    }

    public void setPlatformSender(BufferSender platformSender) {
        this.platformSender = platformSender;
    }

    public final ResourceLocation getChannelId() {
        return channelId;
    }

    private class PacketSet<T extends PacketListener> {

        private final Map<Class<?>, Integer> classToId = new LinkedHashMap<>();
        private final List<BufferSerializer<? extends Packet<T>>> idToDeserializer = new ArrayList<>();

        public <P extends Packet<T>> PacketSet<T> addPacket(Class<P> clazz, BufferSerializer<P> serializer) {

            if (classToId.containsKey(clazz)) {
                throw new IllegalArgumentException("Packet " + clazz + " is already registered to ID " + classToId.get(clazz));
            } else {
                classToId.put(clazz, idToDeserializer.size());
                idToDeserializer.add(serializer);
                return this;
            }
        }

        @Nullable
        public Integer getId(Class<?> clazz) {
            return classToId.getOrDefault(clazz, null);
        }

        public Buffer createBuffer(Packet<T> packet) {
            var id = getId(packet.getClass());
            if (id == null) {
                throw new IllegalArgumentException("Packet " + packet.getClass() + " is not registered");
            }
            var buffer = Buffer.newBuffer();
            var serializer = (BufferSerializer<Packet<T>>) idToDeserializer.get(getId(packet.getClass()));
            buffer.writeInt(id);
            serializer.write(buffer, packet);
            return buffer;
        }

        @Nullable
        public Packet<?> createPacket(Buffer buffer) {
            var id = buffer.readInt();
            var serializer = idToDeserializer.get(id);
            if (serializer != null) return serializer.read(buffer);
            return null;
        }
    }

}
