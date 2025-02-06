package xaero.pac.common.server.claims.protection;

public enum ChunkProtectionExceptionType {

	INTERACTION(ChunkProtection.INTERACT_PREFIX),
	BARRIER("barrier$"),
	EMPTY_HAND_INTERACTION(ChunkProtection.HAND_PREFIX),
	ANY_ITEM_INTERACTION(ChunkProtection.ANYTHING_PREFIX),
	BREAK(ChunkProtection.BREAK_PREFIX),
	FULL(ChunkProtection.FULL_PREFIX);

	private final String prefix;

	ChunkProtectionExceptionType(String prefix) {
		this.prefix = prefix;
	}

	public String getPrefix() {
		return prefix;
	}

	public boolean is(String prefixedName){
		return prefixedName.startsWith(prefix);
	}

}
