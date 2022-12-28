package dev.huskcasaca.effortless.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import dev.huskcasaca.effortless.building.BuildAction;
import dev.huskcasaca.effortless.building.BuildActionHandler;
import dev.huskcasaca.effortless.buildmode.BuildMode;
import dev.huskcasaca.effortless.buildmode.BuildModeHelper;
import dev.huskcasaca.effortless.buildmodifier.BuildModifierHelper;
import dev.huskcasaca.effortless.buildmodifier.ReplaceMode;
import dev.huskcasaca.effortless.building.ReachHelper;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;

import java.util.Arrays;

public class SettingsCommand {

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcher) {

        var effortlessCommand = Commands.literal("effortless");

        var actionCommand = Commands.literal("action");

        Arrays.stream(BuildAction.values()).forEach(buildAction -> {
            actionCommand.then(Commands.literal(buildAction.getCommandName()).executes(context -> {
                var player = context.getSource().getPlayer();
                BuildActionHandler.performAction(player, buildAction);
                context.getSource().sendSuccess(Component.nullToEmpty("Action set to " + buildAction.getCommandName()), true);
                return 1;
            }));
        });

        var replaceCommand = Commands.literal("replacemode").executes(context -> {
            var player = context.getSource().getPlayer();
            try {
                var mode = BuildModifierHelper.getReplaceMode(player);
                context.getSource().sendSuccess(Component.translatable("commands.reserved"), true);
            } catch (Exception e) {
                context.getSource().sendFailure(Component.translatable("commands.reserved"));
                e.printStackTrace();
            }
            return 0;
        });

        Arrays.stream(ReplaceMode.values()).forEach(mode -> {
            replaceCommand.then(Commands.literal(mode.getCommandName()).executes(context -> {
                var player = context.getSource().getPlayer();
                try {
                    BuildModifierHelper.setReplaceMode(player, mode);
                    BuildModifierHelper.sync(player);
                    context.getSource().sendSuccess(Component.translatable("commands.reserved"), true);
                } catch (Exception e) {
                    context.getSource().sendFailure(Component.translatable("commands.reserved"));
                    e.printStackTrace();
                }
                return 0;
            }));
        });

        var modeCommand = Commands.literal("buildmode").executes(context -> {
            var player = context.getSource().getPlayer();
            try {
                var mode = BuildModeHelper.getBuildMode(player);
                context.getSource().sendSuccess(Component.translatable("commands.reserved"), true);
            } catch (Exception e) {
                context.getSource().sendFailure(Component.translatable("commands.reserved"));
                e.printStackTrace();
            }
            return 0;
        });

        Arrays.stream(BuildMode.values()).forEach(mode -> {
            modeCommand.then(Commands.literal(mode.getCommandName()).executes(context -> {
                var player = context.getSource().getPlayer();
                try {
                    BuildModeHelper.setBuildMode(player, mode);
                    BuildModeHelper.sync(player);
                    context.getSource().sendSuccess(Component.translatable("commands.reserved"), true);
                } catch (Exception e) {
                    context.getSource().sendFailure(Component.translatable("commands.reserved"));
                    e.printStackTrace();
                }
                return 0;
            }));
        });

        var modifierCommand = Commands.literal("action");

        var arrayCommand = Commands.literal("array");
        var radialArrayCommand = Commands.literal("radialArray");
        var mirrorCommand = Commands.literal("mirror");

        modifierCommand.then(arrayCommand);
        modifierCommand.then(radialArrayCommand);
        modifierCommand.then(mirrorCommand);

        var ruleCommand = Commands.literal("buildingrule");
        var playerSettingsCommand = Commands.argument("player", EntityArgument.players());

        playerSettingsCommand.then(Commands.literal("maxReachDistance").then(Commands.argument("value", IntegerArgumentType.integer(ReachHelper.MIN_MAX_REACH_DISTANCE, ReachHelper.MAX_MAX_REACH_DISTANCE)).executes(context -> {
            EntityArgument.getPlayers(context, "player").forEach(player -> {
                try {
                    var value = IntegerArgumentType.getInteger(context, "value");
                    ReachHelper.setMaxReachDistance(player, value);
                    ReachHelper.sync(player);
                    context.getSource().sendSuccess(Component.translatable("commands.effortless.max_reach_distance.success", player.getDisplayName(), value), true);
                } catch (Exception e) {
                    context.getSource().sendFailure(Component.translatable("commands.effortless.max_reach_distance.failure", player.getDisplayName()));
                }
            });
            return 0;
        })));

        playerSettingsCommand.then(Commands.literal("maxBlockPlacePerAxis").then(Commands.argument("value", IntegerArgumentType.integer(ReachHelper.MIN_MAX_BLOCK_PLACE_PER_AXIS, ReachHelper.MAX_MAX_BLOCK_PLACE_PER_AXIS)).executes(context -> {
            EntityArgument.getPlayers(context, "player").forEach(player -> {
                try {
                    var value = IntegerArgumentType.getInteger(context, "value");
                    ReachHelper.setMaxBlockPlacePerAxis(player, value);
                    ReachHelper.sync(player);
                    context.getSource().sendSuccess(Component.translatable("commands.effortless.max_block_place_per_axis.success", player.getDisplayName(), value), true);
                } catch (Exception e) {
                    context.getSource().sendFailure(Component.translatable("commands.effortless.max_block_place_per_axis.failure", player.getDisplayName()));
                }
            });
            return 0;
        })));

        playerSettingsCommand.then(Commands.literal("maxBlockPlaceAtOnce").then(Commands.argument("value", IntegerArgumentType.integer(ReachHelper.MIN_MAX_BLOCK_PLACE_AT_ONCE, ReachHelper.MAX_MAX_BLOCK_PLACE_AT_ONCE)).executes(context -> {
            EntityArgument.getPlayers(context, "player").forEach(player -> {
                try {
                    var value = IntegerArgumentType.getInteger(context, "value");
                    ReachHelper.setMaxBlockPlaceAtOnce(player, value);
                    ReachHelper.sync(player);
                    context.getSource().sendSuccess(Component.translatable("commands.effortless.max_block_place_at_once.success", player.getDisplayName(), value), true);
                } catch (Exception e) {
                    context.getSource().sendFailure(Component.translatable("commands.effortless.max_block_place_at_once.failure", player.getDisplayName()));
                }
            });
            return 0;
        })));

        playerSettingsCommand.then(Commands.literal("canBreakFar").then(Commands.argument("value", BoolArgumentType.bool()).executes(context -> {
            EntityArgument.getPlayers(context, "player").forEach(player -> {
                try {
                    var value = BoolArgumentType.getBool(context, "value");
                    ReachHelper.setCanBreakFar(player, value);
                    ReachHelper.sync(player);
                    context.getSource().sendSuccess(Component.translatable("commands.effortless.can_break_far.success", player.getDisplayName(), value), true);
                } catch (Exception e) {
                    context.getSource().sendFailure(Component.translatable("commands.effortless.can_break_far.failure", player.getDisplayName()));
                }
            });
            return 0;
        })));

        playerSettingsCommand.then(Commands.literal("enableUndoRedo").then(Commands.argument("value", BoolArgumentType.bool()).executes(context -> {
            EntityArgument.getPlayers(context, "player").forEach(player -> {
                try {
                    var value = BoolArgumentType.getBool(context, "value");
                    ReachHelper.setEnableUndoRedo(player, value);
                    ReachHelper.sync(player);
                    context.getSource().sendSuccess(Component.translatable("commands.effortless.enable_undo_redo.success", player.getDisplayName(), value), true);
                } catch (Exception e) {
                    context.getSource().sendFailure(Component.translatable("commands.effortless.enable_undo_redo.failure", player.getDisplayName()));
                }
            });
            return 0;
        })));

        playerSettingsCommand.then(Commands.literal("undoStackSize").then(Commands.argument("value", IntegerArgumentType.integer(ReachHelper.MIN_UNDO_STACK_SIZE, ReachHelper.MAX_UNDO_STACK_SIZE)).executes(context -> {
            EntityArgument.getPlayers(context, "player").forEach(player -> {
                try {
                    var value = IntegerArgumentType.getInteger(context, "value");
                    ReachHelper.setUndoStackSize(player, value);
                    ReachHelper.sync(player);
                    context.getSource().sendSuccess(Component.translatable("commands.effortless.undo_stack_size.success", player.getDisplayName(), value), true);
                } catch (Exception e) {
                    context.getSource().sendFailure(Component.translatable("commands.effortless.undo_stack_size.failure", player.getDisplayName()));
                }
            });
            return 0;
        })));

        ruleCommand.then(playerSettingsCommand);

        effortlessCommand.then(ruleCommand);
        effortlessCommand.then(actionCommand);
        effortlessCommand.then(replaceCommand);
        effortlessCommand.then(modeCommand);
        effortlessCommand.then(modifierCommand);

        commandDispatcher.register(effortlessCommand);
    }


}
