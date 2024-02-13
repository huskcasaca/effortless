package dev.huskuraft.effortless.api.platform;

public interface ClientEntrance extends Entrance {

    ClientManager getClientManager();

    default Client getClient() {
        return getClientManager().getRunningClient();
    }

    static ClientEntrance getInstance() {
        return Instance.get();
    }

    class Instance {
        private Instance() {
        }
        private static ClientEntrance instance;
        public static ClientEntrance get() {
            return ClientEntrance.Instance.instance;
        }
        public static void set(ClientEntrance instance) {
            ClientEntrance.Instance.instance = instance;
        }
    }

}

