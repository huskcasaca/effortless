package dev.huskuraft.effortless.api.platform;

import java.util.List;
import java.util.UUID;

import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.core.PlayerProfile;

public interface PlayerList extends PlatformReference {

    List<Player> getPlayers();

    Player getPlayer(UUID uuid);

    boolean isOperator(PlayerProfile profile);

}
