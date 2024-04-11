package dev.huskuraft.effortless.api.command;

public abstract class CommandManager implements CommandRegister {

    public abstract void dispatch(Command command);

}
