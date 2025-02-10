package dev.huskuraft.effortless;

import dev.huskuraft.universal.api.command.Command;
import dev.huskuraft.universal.api.command.CommandManager;

public final class EffortlessClientCommandManager extends CommandManager {

    private final EffortlessClient entrance;

    public EffortlessClientCommandManager(EffortlessClient entrance) {
        this.entrance = entrance;
    }

    public EffortlessClient getEntrance() {
        return entrance;
    }

    @Override
    public void dispatch(Command command) {
        command.execute(getEntrance().getClient()::sendCommand);
    }

//    @Override
//    public void onRegisterCommands(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext commandBuildContext) {
//        ActualSettingsCommands.register(dispatcher);
//        ActualBuildCommands.register(dispatcher, commandBuildContext);
//    }


}
