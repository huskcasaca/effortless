package xaero.pac.client.claims.player.sub;

public final class ClientPlayerSubClaimInfo {

	private final int subIndex;
	private String claimsName;
	private Integer claimsColor;

	public ClientPlayerSubClaimInfo(int subIndex) {
		this.subIndex = subIndex;
	}

	public Integer getClaimsColor() {
		return claimsColor;
	}

	public String getClaimsName() {
		return claimsName;
	}

	public void setClaimsColor(Integer claimsColor) {
		this.claimsColor = claimsColor;
	}

	public void setClaimsName(String claimsName) {
		this.claimsName = claimsName;
	}

}
