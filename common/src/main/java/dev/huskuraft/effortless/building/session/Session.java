package dev.huskuraft.effortless.building.session;

import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.core.World;
import dev.huskuraft.effortless.building.interceptor.BuildInterceptor;
import dev.huskuraft.effortless.building.operation.OperationResult;

import java.util.List;

public interface Session {

    World getWorld();

    Player getPlayer();

    OperationResult commit();

    List<BuildInterceptor> getInterceptors();

}
