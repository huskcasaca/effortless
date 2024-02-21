package dev.huskuraft.effortless.fabric.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.level.ServerPlayer;

public final class ServerPlayerEvents {

    /**
     * An event that is called after a player has logged in.
     */
    public static final Event<LoggedIn> LOGGED_IN = EventFactory.createArrayBacked(LoggedIn.class, callbacks -> player -> {
        for (LoggedIn callback : callbacks) {
            callback.onLoggedIn(player);
        }
    });

    /**
     * An event that is called after a player has logged out.
     */
    public static final Event<LoggedOut> LOGGED_OUT = EventFactory.createArrayBacked(LoggedOut.class, callbacks -> player -> {
        for (LoggedOut callback : callbacks) {
            callback.onLoggedOut(player);
        }
    });

    /**
     * An event that is called after a player has been respawned.
     *
     * <p>Mods may use this event for reference clean up on the old player.
     */
    public static final Event<Respawn> RESPAWN = EventFactory.createArrayBacked(Respawn.class, callbacks -> (oldPlayer, newPlayer, alive) -> {
        for (Respawn callback : callbacks) {
            callback.onRespawn(oldPlayer, newPlayer, alive);
        }
    });

    @FunctionalInterface
    public interface Respawn {
        /**
         * Called after player a has been respawned.
         *
         * @param oldPlayer the old player
         * @param newPlayer the new player
         * @param alive     whether the old player is still alive
         */
        void onRespawn(ServerPlayer oldPlayer, ServerPlayer newPlayer, boolean alive);
    }

    @FunctionalInterface
    public interface LoggedIn {
        /**
         * Called after a player has logged in.
         *
         * @param player the player
         */
        void onLoggedIn(ServerPlayer player);
    }

    @FunctionalInterface
    public interface LoggedOut {
        /**
         * Called after a player has logged out.
         *
         * @param player the player
         */
        void onLoggedOut(ServerPlayer player);
    }


}
