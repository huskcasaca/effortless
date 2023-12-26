package dev.huskuraft.effortless;

import dev.huskuraft.effortless.command.CommandManager;
import dev.huskuraft.effortless.core.Entrance;

final class EffortlessCommandManager extends CommandManager {

    private final Entrance entrance;

    public EffortlessCommandManager(Entrance entrance) {
        this.entrance = entrance;
    }

    public Entrance getEntrance() {
        return entrance;
    }

//    @Override
//    public void onRegisterCommands(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext commandBuildContext) {
//        ActualSettingsCommands.register(dispatcher);
//        ActualBuildCommands.register(dispatcher, commandBuildContext);
//    }


}
