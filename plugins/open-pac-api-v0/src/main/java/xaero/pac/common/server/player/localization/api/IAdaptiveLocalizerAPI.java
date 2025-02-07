package xaero.pac.common.server.player.localization.api;

import javax.annotation.Nonnull;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;

/**
 * A text localizer that adapts to the player that the text is to be read by, whether the player has the mod installed or not.
 */
public interface IAdaptiveLocalizerAPI {

    /**
     * Gets a text component for translatable text adapted to a specified player.
     *
     * @param player the player to read the text component, not null
     * @param key    the key of the translated line, not null
     * @param args   the arguments to format the translated line with, optional
     * @return the text component, not null
     */
    @Nonnull
    MutableComponent getFor(@Nonnull ServerPlayer player, @Nonnull String key, @Nonnull Object... args);

    /**
     * Converts a text component to be readable by a specified player, if necessary.
     * <p>
     * Only ever converts translatable text components and always returns anything else unchanged.
     * The arguments of a translatable text component are also converted if necessary.
     * Does not convert the siblings of the text component.
     *
     * @param player    the player to read the text component, not null
     * @param component the text component to adapt to the player, not null
     * @return the converted text component or the unchanged input component if no conversion was necessary, not null
     */
    @Nonnull
    Component getFor(@Nonnull ServerPlayer player, @Nonnull Component component);

}
