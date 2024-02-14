package dev.huskuraft.effortless.building;

import dev.huskuraft.effortless.api.core.BlockInteraction;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.core.World;
import dev.huskuraft.effortless.building.operation.block.BlockBreakOnClientOperation;
import dev.huskuraft.effortless.building.operation.block.BlockBreakOperation;
import dev.huskuraft.effortless.building.operation.block.BlockPlaceOnClientOperation;
import dev.huskuraft.effortless.building.operation.block.BlockPlaceOperation;

public class ClientBuildSession extends BuildSession {

    public ClientBuildSession(World world, Player player, Context context) {
        super(world, player, context);
    }

    protected BlockPlaceOperation createBlockPlaceOperationFromHit(World world, Player player, Context context, Storage storage, BlockInteraction interaction) {
        return new BlockPlaceOnClientOperation(world, player, context, storage, interaction, null);
    }

    protected BlockBreakOperation createBlockBreakOperationFromHit(World world, Player player, Context context, Storage storage, BlockInteraction interaction) {
        return new BlockBreakOnClientOperation(world, player, context, storage, interaction);
    }

}
