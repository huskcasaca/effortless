package dev.huskuraft.effortless;

import dev.huskuraft.effortless.api.command.CommandManager;

final class EffortlessCommandManager extends CommandManager {

    private final Effortless entrance;

    public EffortlessCommandManager(Effortless entrance) {
        this.entrance = entrance;
    }

    public Effortless getEntrance() {
        return entrance;
    }

//    @Override
//    public void onRegisterCommands(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext commandBuildContext) {
//        ActualSettingsCommands.register(dispatcher);
//        ActualBuildCommands.register(dispatcher, commandBuildContext);
//    }


}
