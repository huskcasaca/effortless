package dev.ftb.mods.ftbteams.api;

import net.minecraft.network.chat.Component;

import java.util.UUID;

/**
 * Represents a message sent via the team chat. Could be from a player, or a system message.
 * <p>
 * You can create an instance of this via {@link FTBTeamsAPI.API#createMessage(UUID, Component)};
 * the timestamp will be automatically set to the value of {@link System#currentTimeMillis()}.
 * </p>
 */
public interface TeamMessage {
    /**
     * The sender's ID. System messages use a sender ID of {@link net.minecraft.Util#NIL_UUID}.
     *
     * @return the sender's ID
     */
    UUID sender();

    /**
     * Message timestamp, in milliseconds since epoch, i.e. the result of {@link System#currentTimeMillis()}.
     *
     * @return the message timestamp
     */
    long date();

    /**
     * The message text.
     *
     * @return message text
     */
    Component text();
}
