package dev.huskcasaca.effortless.network;

import dev.huskcasaca.effortless.buildmodifier.BlockSet;
import dev.huskcasaca.effortless.buildmodifier.UndoRedo;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;

/***
 * Sends a message to the client asking to add a block to the undo stack.
 */
public class AddUndoMessage implements Message {
    private final BlockPos coordinate;
    private final BlockState previousBlockState;
    private final BlockState newBlockState;

    public AddUndoMessage() {
        coordinate = BlockPos.ZERO;
        previousBlockState = null;
        newBlockState = null;
    }

    public AddUndoMessage(BlockPos coordinate, BlockState previousBlockState, BlockState newBlockState) {
        this.coordinate = coordinate;
        this.previousBlockState = previousBlockState;
        this.newBlockState = newBlockState;
    }

    public static void encode(AddUndoMessage message, FriendlyByteBuf buf) {
        buf.writeInt(message.coordinate.getX());
        buf.writeInt(message.coordinate.getY());
        buf.writeInt(message.coordinate.getZ());
        buf.writeInt(Block.getId(message.previousBlockState));
        buf.writeInt(Block.getId(message.newBlockState));
    }

    public static AddUndoMessage decode(FriendlyByteBuf buf) {
        BlockPos coordinate = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
        BlockState previousBlockState = Block.stateById(buf.readInt());
        BlockState newBlockState = Block.stateById(buf.readInt());
        return new AddUndoMessage(coordinate, previousBlockState, newBlockState);
    }

    public BlockPos getCoordinate() {
        return coordinate;
    }

    public BlockState getPreviousBlockState() {
        return previousBlockState;
    }

    public BlockState getNewBlockState() {
        return newBlockState;
    }

    public static class Serializer implements MessageSerializer<AddUndoMessage> {

        @Override
        public void encode(AddUndoMessage message, FriendlyByteBuf buf) {
            AddUndoMessage.encode(message, buf);
        }

        @Override
        public AddUndoMessage decode(FriendlyByteBuf buf) {
            return AddUndoMessage.decode(buf);
        }

    }

    public static class ServerHandler implements ServerMessageHandler<AddUndoMessage> {

        @Override
        public void handleServerSide(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, AddUndoMessage message, PacketSender responseSender) {
            // TODO: 13/9/22
        }
//        public static void handle(AddUndoMessage message, Supplier<NetworkEvent.Context> ctx) {
//            ctx.get().enqueueWork(() -> {
//                if (ctx.get().getDirection().getReceptionSide() == LogicalSide.CLIENT) {
//                    //Received clientside
//                    DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientHandler.handle(message, ctx));
//                }
//            });
//            ctx.get().setPacketHandled(true);
//        }

    }

    @Environment(EnvType.CLIENT)
    public static class ClientHandler implements ClientMessageHandler<AddUndoMessage> {

        @Override
        public void handleClientSide(Minecraft client, LocalPlayer player, ClientPacketListener handler, AddUndoMessage message, PacketSender responseSender) {
            client.execute(() -> {
                if (player != null) {
                    UndoRedo.addUndo(player, new BlockSet(
                            new ArrayList<BlockPos>() {{
                                add(message.getCoordinate());
                            }},
                            new ArrayList<BlockState>() {{
                                add(message.getPreviousBlockState());
                            }},
                            new ArrayList<BlockState>() {{
                                add(message.getNewBlockState());
                            }},
                            new Vec3(0, 0, 0),
                            message.getCoordinate(), message.getCoordinate()
                    ));
                }
            });
        }
    }
}
