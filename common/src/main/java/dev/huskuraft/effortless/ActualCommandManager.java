package dev.huskuraft.effortless;

import dev.huskuraft.effortless.command.CommandManager;
import dev.huskuraft.effortless.core.Entrance;

final class ActualCommandManager extends CommandManager {

    private final Entrance entrance;

    public ActualCommandManager(Entrance entrance) {
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
