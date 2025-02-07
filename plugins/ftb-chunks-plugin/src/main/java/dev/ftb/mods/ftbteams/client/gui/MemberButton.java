package dev.ftb.mods.ftbteams.client.gui;

import com.mojang.authlib.GameProfile;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.icon.FaceIcon;
import dev.ftb.mods.ftblibrary.icon.Icons;
import dev.ftb.mods.ftblibrary.ui.ContextMenuItem;
import dev.ftb.mods.ftblibrary.ui.NordButton;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.Theme;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftbteams.api.TeamRank;
import dev.ftb.mods.ftbteams.api.client.KnownClientPlayer;
import dev.ftb.mods.ftbteams.data.ClientTeam;
import dev.ftb.mods.ftbteams.data.ClientTeamManagerImpl;
import dev.ftb.mods.ftbteams.data.TeamType;
import dev.ftb.mods.ftbteams.net.PlayerGUIOperationMessage.Operation;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class MemberButton extends NordButton {
	private final KnownClientPlayer player;

	MemberButton(Panel panel, KnownClientPlayer p) {
		super(panel, Component.literal(p.name()), FaceIcon.getFace(p.profile()));
		setWidth(width + 18);  // to fit in the rank icon
		player = p;
	}

	@Override
	public void drawIcon(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
		super.drawIcon(graphics, theme, x, y, w, h);

		if (player.online()) {
			graphics.pose().pushPose();
			graphics.pose().translate(x + w - 1.5D, y - 0.5D, 0);
			Color4I.GREEN.draw(graphics, 0, 0, 2, 2);
			graphics.pose().popPose();
		}

		ClientTeam selfTeam = ClientTeamManagerImpl.getInstance().selfTeam();
		if (selfTeam.getType() == TeamType.PARTY) {
			TeamRank tr = selfTeam.getRankForPlayer(player.id());
			tr.getIcon().ifPresent(icon -> icon.draw(graphics, getX() + width - 14, getY() + 2, 12, 12));
		}
	}

	@Override
	public void onClicked(MouseButton button) {
		KnownClientPlayer self = ClientTeamManagerImpl.getInstance().self();
		ClientTeam selfTeam = ClientTeamManagerImpl.getInstance().selfTeam();

		if (selfTeam == null || self == null) return;

		TeamRank selfRank = selfTeam.getRankForPlayer(self.id());
		TeamRank playerRank = selfTeam.getRankForPlayer(player.id());

		if (selfTeam.getType() != TeamType.PARTY) return;

		List<ContextMenuItem> items0 = new ArrayList<>();
		if (player.id().equals(self.id())) {
			if (selfRank.isAtLeast(TeamRank.OWNER)) {
				if (selfTeam.getMembers().size() == 1) {
					items0.add(new ContextMenuItem(Component.translatable("ftbteams.gui.disband"), Icons.CANCEL,
							(b) -> Operation.LEAVE.sendMessage(player))
							.setYesNoText(Component.translatable("ftbteams.gui.disband.confirm")));
				}
			} else {
				items0.add(new ContextMenuItem(Component.translatable("ftbteams.gui.leave"), Icons.CANCEL,
						(b) -> Operation.LEAVE.sendMessage(player))
						.setYesNoText(Component.translatable("ftbteams.gui.leave.confirm")));
			}
		} else {
			if (selfRank.isAtLeast(TeamRank.OWNER)) {
				if (playerRank == TeamRank.MEMBER) {
					items0.add(new ContextMenuItem(Component.translatable("ftbteams.gui.promote", player.name()), Icons.SHIELD,
							(b) -> Operation.PROMOTE.sendMessage(player))
							.setYesNoText(Component.translatable("ftbteams.gui.promote.confirm", player.name())));
				} else if (playerRank == TeamRank.OFFICER) {
					items0.add(new ContextMenuItem(Component.translatable("ftbteams.gui.demote", player.name()), Icons.ACCEPT_GRAY,
							(b) -> Operation.DEMOTE.sendMessage(player))
							.setYesNoText(Component.translatable("ftbteams.gui.demote.confirm", player.name())));
				}
				if (playerRank.isMemberOrBetter()) {
					items0.add(new ContextMenuItem(Component.translatable("ftbteams.gui.transfer_ownership", player.name()), Icons.DIAMOND,
							(b) -> Operation.TRANSFER_OWNER.sendMessage(player))
							.setYesNoText(Component.translatable("ftbteams.gui.transfer_ownership.confirm", player.name())));
				}
			}
		}
		if (selfRank.getPower() > playerRank.getPower()) {
			if (playerRank.isMemberOrBetter()) {
				items0.add(new ContextMenuItem(Component.translatable("ftbteams.gui.kick", player.name()), Icons.CANCEL,
						(b) -> Operation.KICK.sendMessage(player))
						.setYesNoText(Component.translatable("ftbteams.gui.kick.confirm", player.name())));
			} else if (selfRank.isOfficerOrBetter() && playerRank.isAllyOrBetter()) {
				items0.add(new ContextMenuItem(Component.translatable("ftbteams.gui.remove_ally", player.name()), Icons.CANCEL,
						(b) -> Operation.REMOVE_ALLY.sendMessage(player))
						.setYesNoText(Component.translatable("ftbteams.gui.remove_ally.confirm", player.name())));
				}
		}

		if (!items0.isEmpty()) {
			List<ContextMenuItem> items = new ArrayList<>(List.of(
					new ContextMenuItem(playerRank.getDisplayName(), FaceIcon.getFace(new GameProfile(player.id(), "")), null).setCloseMenu(false),
					ContextMenuItem.SEPARATOR
			));
			items.addAll(items0);
			getGui().openContextMenu(items);
		}
	}
}
