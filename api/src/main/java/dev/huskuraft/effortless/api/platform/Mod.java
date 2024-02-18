package dev.huskuraft.effortless.api.platform;

public interface Mod {

    String getId();

    String getVersionStr();

    String getDescription();

    String getName();

    static Mod create(String id, String version, String description, String name) {
        return new Mod() {

            @Override
            public String getId() {
                return id;
            }

            @Override
            public String getVersionStr() {
                return version;
            }

            @Override
            public String getDescription() {
                return description;
            }

            @Override
            public String getName() {
                return name;
            }
        };
    }

}
