package xaero.pac.common.parties.party.member;

import net.minecraft.ChatFormatting;

public enum PartyMemberRank {

    MEMBER(ChatFormatting.GRAY),
    MODERATOR(ChatFormatting.AQUA),
    ADMIN(ChatFormatting.YELLOW);

    private final ChatFormatting color;

    PartyMemberRank(ChatFormatting color) {
        this.color = color;
    }

    public ChatFormatting getColor() {
        return color;
    }

}
