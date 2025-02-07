package dev.ftb.mods.ftbchunks.data;

public interface ClaimResult {

	String claimResultName();

	boolean isSuccess();

	void setClaimedTime(long time);

	void setForceLoadedTime(long time);

	String getTranslationKey();

}
