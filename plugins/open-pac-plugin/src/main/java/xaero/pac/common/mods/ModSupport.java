package xaero.pac.common.mods;

import xaero.pac.common.mods.prometheus.Prometheus;

public abstract class ModSupport {

	public boolean LUCK_PERMS;
	private LuckPerms luckPerms;
	public boolean FTB_RANKS;
	private FTBRanks ftbRanks;
	public boolean PROMETHEUS;
	private Prometheus prometheus;
	public boolean FTB_TEAMS;
	private FTBTeams ftbTeams;
	public boolean ARGONAUTS;
	private Argonauts argonauts;

	public void check(boolean client){
		if(!client) {
			try {
				Class.forName("net.luckperms.api.LuckPerms");
				LUCK_PERMS = true;
				luckPerms = new LuckPerms();
			} catch (ClassNotFoundException e) {
			}
		}
		try {
			Class.forName("dev.ftb.mods.ftbranks.api.FTBRanksAPI");
			FTB_RANKS = true;
			ftbRanks = new FTBRanks();
		} catch (ClassNotFoundException e) {
		}
		try {
			Class.forName("earth.terrarium.prometheus.api.permissions.PermissionApi");
			PROMETHEUS = true;
			prometheus = new Prometheus(client);
		} catch (ClassNotFoundException e) {
		}
		try {
			Class.forName("dev.ftb.mods.ftbteams.api.FTBTeamsAPI");
			FTB_TEAMS = true;
			ftbTeams = new FTBTeams();
		} catch (ClassNotFoundException e) {
		}
		try {
			Class.forName("earth.terrarium.argonauts.api.ApiHelper");
			ARGONAUTS = true;
			argonauts = new Argonauts();
		} catch (ClassNotFoundException e) {
		}
	}

	public LuckPerms getLuckPerms() {
		return luckPerms;
	}

	public FTBRanks getFTBRanksSupport(){
		return ftbRanks;
	}

	public Prometheus getPrometheusSupport() {
		return prometheus;
	}

	public FTBTeams getFTBTeamsSupport() {
		return ftbTeams;
	}

	public Argonauts getArgonautsSupport() {
		return argonauts;
	}

	public void init() {
		if(PROMETHEUS)
			prometheus.init();
	}

	public void initClient() {
		if(PROMETHEUS)
			prometheus.initClient();
	}

}
