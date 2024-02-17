package dev.huskuraft.effortless.api.command;

import dev.huskuraft.effortless.api.platform.ClientEntrance;

public abstract class CommandManager implements CommandRegister {

    public static void dispatch(Command command) {
        command.execute(ClientEntrance.getInstance().getClient()::sendCommand);
    }

}
