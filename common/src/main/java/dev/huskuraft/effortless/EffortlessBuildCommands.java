package dev.huskuraft.effortless;

public final class EffortlessBuildCommands {

//    private static final DynamicCommandExceptionType ERROR_NO_SUCH_BUILD_MODE = new DynamicCommandExceptionType(object -> {
//        return FabricAdapter.adapt(Component.translatable("commands.attribute.failed.build_mode", object));
//    });

//    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcher, CommandBuildContext commandBuildContext) {
//
//        var effortlessCommand = Commands.literal("effortless");
//
//        var placeCommand = Commands.literal("place");
//        var breakCommand = Commands.literal("break");
//
//        for (BuildMode buildMode : BuildMode.values()) {
//            if (buildMode == BuildMode.DISABLE) continue;
//            if (buildMode.getInstance() instanceof SingleClickBuildable) {
//                registerOneClickBuildModePlace(placeCommand, commandBuildContext, buildMode);
//                registerOneClickBuildModeBreak(breakCommand, commandBuildContext, buildMode);
//            } else if (buildMode.getInstance() instanceof TwoClickBuildable) {
//                registerTwoClickBuildModePlace(placeCommand, commandBuildContext, buildMode);
//                registerTwoClickBuildModeBreak(breakCommand, commandBuildContext, buildMode);
//            } else if (buildMode.getInstance() instanceof ThreeClickBuildable) {
//                registerThreeClickBuildModePlace(placeCommand, commandBuildContext, buildMode);
//                registerThreeClickBuildModeBreak(breakCommand, commandBuildContext, buildMode);
//            }
//        }
//
//        effortlessCommand.then(placeCommand);
//        effortlessCommand.then(breakCommand);
//        commandDispatcher.register(effortlessCommand);
//
//    }
//
//    private static void registerOneClickBuildModePlace(ArgumentBuilder<CommandSourceStack, ?> builder, CommandBuildContext commandBuildContext, BuildMode buildMode) {
//        var command = Commands.literal(buildMode.getCommandName()).then(Commands.argument("firstPos", BlockPosArgument.blockPos()).then(Commands.argument("block", BlockStateArgument.block(commandBuildContext)).executes(context -> {
//            var player = context.getSource().getPlayer();
//            if (player == null) return 1;
//
//            var firstPos = BlockPosArgument.getLoadedBlockPos(context, "firstPos");
//
//            var blockState = BlockStateArgument.getBlock(context, "block").getState();
//
//            var coordinates = ((SingleClickBuildable) buildMode.getInstance()).collectFinalBlocks(context.getSource().getPlayer(), firstPos.getX(), firstPos.getY(), firstPos.getZ());
//
//            for (var blockPosition : coordinates) {
//                // TODO: 23/1/23  SurvivalHelper.useBlock(player.getLevel(), player, blockPosition, blockState);
//            }
//            return 1;
//        })));
//
//        builder.then(command);
//    }
//
//    private static void registerTwoClickBuildModePlace(ArgumentBuilder<CommandSourceStack, ?> builder, CommandBuildContext commandBuildContext, BuildMode buildMode) {
//        var command = Commands.literal(buildMode.getCommandName()).then(Commands.argument("firstPos", BlockPosArgument.blockPos()).then(Commands.argument("secondPos", BlockPosArgument.blockPos()).then(Commands.argument("block", BlockStateArgument.block(commandBuildContext)).executes(context -> {
//            var player = context.getSource().getPlayer();
//            if (player == null) return 1;
//
//            var firstPos = BlockPosArgument.getLoadedBlockPos(context, "firstPos");
//            var secondPos = BlockPosArgument.getLoadedBlockPos(context, "secondPos");
//
//            var blockState = BlockStateArgument.getBlock(context, "block").getState();
//
//            var coordinates = ((TwoClickBuildable) buildMode.getInstance()).collectFinalBlocks(context.getSource().getPlayer());
//
//            for (var blockPosition : coordinates) {
//                // TODO: 23/1/23  SurvivalHelper.useBlock(player.getLevel(), player, blockPosition, blockState);
//            }
//            return 1;
//        }))));
//
//        builder.then(command);
//    }
//
//    private static void registerThreeClickBuildModePlace(ArgumentBuilder<CommandSourceStack, ?> builder, CommandBuildContext commandBuildContext, BuildMode buildMode) {
//        var command = Commands.literal(buildMode.getCommandName()).then(Commands.argument("firstPos", BlockPosArgument.blockPos()).then(Commands.argument("secondPos", BlockPosArgument.blockPos()).then(Commands.argument("thirdPos", BlockPosArgument.blockPos()).then(Commands.argument("block", BlockStateArgument.block(commandBuildContext)).executes(context -> {
//            var player = context.getSource().getPlayer();
//            if (player == null) return 1;
//
//            var firstPos = BlockPosArgument.getLoadedBlockPos(context, "firstPos");
//            var secondPos = BlockPosArgument.getLoadedBlockPos(context, "secondPos");
//            var thirdPos = BlockPosArgument.getLoadedBlockPos(context, "thirdPos");
//
//            var blockState = BlockStateArgument.getBlock(context, "block").getState();
//
//            var coordinates = ((ThreeClickBuildable) buildMode.getInstance()).collectFinalBlocks(context.getSource().getPlayer(), firstPos.getX(), firstPos.getY(), firstPos.getZ(), secondPos.getX(), secondPos.getY(), secondPos.getZ(), thirdPos.getX(), thirdPos.getY(), thirdPos.getZ());
//
//            for (var blockPosition : coordinates) {
//                // TODO: 23/1/23  SurvivalHelper.useBlock(player.getLevel(), player, blockPosition, blockState);
//            }
//            return 1;
//        })))));
//
//        builder.then(command);
//    }
//
//    private static void registerOneClickBuildModeBreak(ArgumentBuilder<CommandSourceStack, ?> builder, CommandBuildContext commandBuildContext, BuildMode buildMode) {
//        var command = Commands.literal(buildMode.getCommandName()).then(Commands.argument("firstPos", BlockPosArgument.blockPos()).executes(context -> {
//            var player = context.getSource().getPlayer();
//            if (player == null) return 1;
//
//            var firstPos = BlockPosArgument.getLoadedBlockPos(context, "firstPos");
//
//            var coordinates = ((SingleClickBuildable) buildMode.getInstance()).collectFinalBlocks(context.getSource().getPlayer(), firstPos.getX(), firstPos.getY(), firstPos.getZ());
//
//            for (var blockPosition : coordinates) {
//                SurvivalHelper.destroyBlock(player.getLevel(), player, blockPosition, false);
//            }
//            return 1;
//        }));
//
//        builder.then(command);
//    }
//
//    private static void registerTwoClickBuildModeBreak(ArgumentBuilder<CommandSourceStack, ?> builder, CommandBuildContext commandBuildContext, BuildMode buildMode) {
//        var command = Commands.literal(buildMode.getCommandName()).then(Commands.argument("firstPos", BlockPosArgument.blockPos()).then(Commands.argument("secondPos", BlockPosArgument.blockPos()).executes(context -> {
//            var player = context.getSource().getPlayer();
//            if (player == null) return 1;
//
//            var firstPos = BlockPosArgument.getLoadedBlockPos(context, "firstPos");
//            var secondPos = BlockPosArgument.getLoadedBlockPos(context, "secondPos");
//
//            var coordinates = ((TwoClickBuildable) buildMode.getInstance()).collectFinalBlocks(context.getSource().getPlayer());
//
//            for (var blockPosition : coordinates) {
//                SurvivalHelper.destroyBlock(player.getLevel(), player, blockPosition, false);
//            }
//            return 1;
//        })));
//
//        builder.then(command);
//    }
//
//    private static void registerThreeClickBuildModeBreak(ArgumentBuilder<CommandSourceStack, ?> builder, CommandBuildContext commandBuildContext, BuildMode buildMode) {
//        var command = Commands.literal(buildMode.getCommandName()).then(Commands.argument("firstPos", BlockPosArgument.blockPos()).then(Commands.argument("secondPos", BlockPosArgument.blockPos()).then(Commands.argument("thirdPos", BlockPosArgument.blockPos()).executes(context -> {
//            var player = context.getSource().getPlayer();
//            if (player == null) return 1;
//
//            var firstPos = BlockPosArgument.getLoadedBlockPos(context, "firstPos");
//            var secondPos = BlockPosArgument.getLoadedBlockPos(context, "secondPos");
//            var thirdPos = BlockPosArgument.getLoadedBlockPos(context, "thirdPos");
//
//            var coordinates = ((ThreeClickBuildable) buildMode.getInstance()).collectFinalBlocks(context.getSource().getPlayer(), firstPos.getX(), firstPos.getY(), firstPos.getZ(), secondPos.getX(), secondPos.getY(), secondPos.getZ(), thirdPos.getX(), thirdPos.getY(), thirdPos.getZ());
//
//            for (var blockPosition : coordinates) {
//                SurvivalHelper.destroyBlock(player.getLevel(), player, blockPosition, false);
//            }
//            return 1;
//        }))));
//
//        builder.then(command);
//    }

}
