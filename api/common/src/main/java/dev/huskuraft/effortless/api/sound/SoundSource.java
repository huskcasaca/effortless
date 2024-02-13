package dev.huskuraft.effortless.api.sound;

public enum SoundSource {
    MASTER("master"),
    MUSIC("music"),
    RECORDS("record"),
    WEATHER("weather"),
    BLOCKS("block"),
    HOSTILE("hostile"),
    NEUTRAL("neutral"),
    PLAYERS("player"),
    AMBIENT("ambient"),
    VOICE("voice");

    private final String name;

    private SoundSource(String pName) {
        this.name = pName;
    }

    public String getName() {
        return this.name;
    }
}
