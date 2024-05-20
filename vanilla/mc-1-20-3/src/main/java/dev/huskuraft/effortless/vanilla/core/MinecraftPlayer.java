package dev.huskuraft.effortless.vanilla.core;

import java.util.UUID;

import dev.huskuraft.effortless.api.core.BlockInteraction;
import dev.huskuraft.effortless.api.core.GameMode;
import dev.huskuraft.effortless.api.core.InteractionHand;
import dev.huskuraft.effortless.api.core.Inventory;
import dev.huskuraft.effortless.api.core.ItemStack;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.core.PlayerProfile;
import dev.huskuraft.effortless.api.core.Stat;
import dev.huskuraft.effortless.api.core.World;
import dev.huskuraft.effortless.api.math.Vector3d;
import dev.huskuraft.effortless.api.platform.Client;
import dev.huskuraft.effortless.api.platform.Server;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.vanilla.platform.MinecraftClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.server.level.ServerPlayer;

public record MinecraftPlayer(net.minecraft.world.entity.player.Player refs) implements Player {

    public static Player ofNullable(net.minecraft.world.entity.player.Player reference) {
        return reference == null ? null : new MinecraftPlayer(reference);
    }

    @Override
    public UUID getId() {
        return refs.getUUID();
    }

    @Override
    public PlayerProfile getProfile() {
        return new MinecraftPlayerProfile(refs.getGameProfile());
    }

    @Override
    public Inventory getInventory() {
        return new MinecraftInventory(refs.getInventory());
    }

    @Override
    public boolean isDeadOrDying() {
        return refs.isDeadOrDying();
    }

    @Override
    public Client getClient() {
        return new MinecraftClient(Minecraft.getInstance());
    }

    @Override
    public Server getServer() {
        return new MinecraftServer(refs.getServer());
    }

    @Override
    public World getWorld() {
        return MinecraftWorld.ofNullable(refs.level());
    }

    @Override
    public Text getDisplayName() {
        return new MinecraftText(refs.getDisplayName());
    }

    @Override
    public Vector3d getPosition() {
        return MinecraftConvertor.fromPlatformVector3d(refs.position());
    }

    @Override
    public void drop(ItemStack itemStack, boolean dropAround, boolean includeThrowerName) {
        refs.drop(itemStack.reference(), dropAround, includeThrowerName);
    }

    @Override
    public void sendMessage(Text message) {
        refs.sendSystemMessage(message.reference());
//        getRef().displayClientMessage(message, true);
    }

    @Override
    public void sendClientMessage(Text message, boolean actionBar) {
        refs.displayClientMessage(message.reference(), actionBar);
    }

    @Override
    public void swing(InteractionHand hand) {
        refs.swing(MinecraftConvertor.toPlatformInteractionHand(hand));
    }

    @Override
    public GameMode getGameMode() {
        if (refs instanceof ServerPlayer serverPlayer) {
            return switch (serverPlayer.gameMode.getGameModeForPlayer()) {
                case SURVIVAL -> GameMode.SURVIVAL;
                case CREATIVE -> GameMode.CREATIVE;
                case ADVENTURE -> GameMode.ADVENTURE;
                case SPECTATOR -> GameMode.SPECTATOR;
            };
        }
        if (refs instanceof AbstractClientPlayer localPlayer) {
            return switch (localPlayer.getPlayerInfo().getGameMode()) {
                case SURVIVAL -> GameMode.SURVIVAL;
                case CREATIVE -> GameMode.CREATIVE;
                case ADVENTURE -> GameMode.ADVENTURE;
                case SPECTATOR -> GameMode.SPECTATOR;
            };
        }
        return null;
    }

    @Override
    public BlockInteraction raytrace(double maxDistance, float deltaTick, boolean includeFluids) {
        return (BlockInteraction) MinecraftConvertor.fromPlatformInteraction(refs.pick(maxDistance, deltaTick, includeFluids));
    }

    @Override
    public void awardStat(Stat<?> stat, int increment) {
        refs.awardStat((net.minecraft.stats.Stat<?>) stat.reference(), increment);
    }

    @Override
    public void resetStat(Stat<?> stat) {
        refs.resetStat(stat.reference());
    }

}
