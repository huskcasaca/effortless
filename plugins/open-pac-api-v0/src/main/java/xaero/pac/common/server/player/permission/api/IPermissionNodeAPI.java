package xaero.pac.common.server.player.permission.api;

import javax.annotation.Nonnull;

import net.minecraft.network.chat.Component;

/**
 * A representation of a player permission node.
 * <p>
 */
public interface IPermissionNodeAPI<T> {

    /**
     * Gets the default string representation of this node.
     *
     * @return default string representation of this node, not null
     */
    @Nonnull
    String getDefaultNodeString();

    /**
     * Gets the user-configured string representation of this node.
     *
     * @return the user-configured string representation of this node, not null
     */
    @Nonnull
    String getNodeString();

    /**
     * Gets the text component of the name of this node, to be used in UIs.
     *
     * @return the text component of the name, not null
     */
    @Nonnull
    Component getName();

    /**
     * Gets the text component of the comment/tooltip for this node, to be used in UIs.
     *
     * @return the text component of the comment/tooltip, not null
     */
    @Nonnull
    Component getComment();

    /**
     * Gets the type of value stored with this permission node.
     *
     * @return the type of value stored with this permission node, not null
     */
    @Nonnull
    Class<T> getType();

}
