package dev.ftb.mods.ftbteams.client.gui;

import com.mojang.authlib.GameProfile;
import dev.architectury.networking.NetworkManager;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.icon.FaceIcon;
import dev.ftb.mods.ftblibrary.icon.Icons;
import dev.ftb.mods.ftblibrary.ui.*;
import dev.ftb.mods.ftblibrary.ui.input.Key;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftblibrary.ui.misc.NordColors;
import dev.ftb.mods.ftbteams.api.FTBTeamsAPI;
import dev.ftb.mods.ftbteams.api.client.ClientTeamManager;
import dev.ftb.mods.ftbteams.api.property.TeamProperties;
import dev.ftb.mods.ftbteams.data.FTBTUtils;
import dev.ftb.mods.ftbteams.net.CreatePartyMessage;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.User;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;

import java.util.HashSet;
import java.util.Set;

public class CreatePartyScreen extends BaseScreen implements NordColors, InvitationSetup {
	private final ClientTeamManager manager;
	private final Set<GameProfile> invitedMembers;

	private Panel invitePanel;
	private Panel settingsPanel;
	private Button createTeamButton;
	private Color4I teamColor;
	private TextBox nameTextBox;
	private TextBox descriptionTextBox;

	public CreatePartyScreen() {
		setSize(300, 200);
		manager = FTBTeamsAPI.api().getClientManager();
		invitedMembers = new HashSet<>();
		teamColor = manager.selfTeam().getProperty(TeamProperties.COLOR);
	}

	@Override
	public void addWidgets() {
		Button closeButton;
		add(closeButton = new SimpleButton(this, Component.translatable("gui.cancel"), Icons.CANCEL.withTint(SNOW_STORM_2), (simpleButton, mouseButton) -> closeGui()) {
			@Override
			public void draw(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
				drawIcon(graphics, theme, x, y, w, h);
			}
		});

		Button colorButton;
		add(colorButton = new SimpleButton(this, Component.translatable("gui.color"), teamColor.withBorder(POLAR_NIGHT_0, false), (simpleButton, mouseButton) -> {
			teamColor = FTBTUtils.randomColor();
			simpleButton.setIcon(teamColor.withBorder(POLAR_NIGHT_0, false));
		}) {
			@Override
			public void draw(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
				icon.draw(graphics, x, y, w, h);
			}
		});

		add(invitePanel = new InvitePanel());

		add(settingsPanel = new SettingsPanel());

		add(createTeamButton = new NordButton(this, Component.translatable("ftbteams.create_party").withStyle(Style.EMPTY.withColor(TextColor.fromRgb(NordColors.GREEN.rgb()))), Icons.ACCEPT) {
			@Override
			public boolean renderTitleInCenter() {
				return true;
			}

			@Override
			public void onClicked(MouseButton mouseButton) {
				closeGui(false);
				NetworkManager.sendToServer(new CreatePartyMessage(nameTextBox.getText(), descriptionTextBox.getText(), teamColor.rgb(), invitedMembers));
			}
		});

		closeButton.setPosAndSize(width - 18, 5, 12, 12);
		colorButton.setPosAndSize(5, 5, 12, 12);
		invitePanel.setPosAndSize(1, 22, 89, height - 23);
	}

	@Override
	public void drawBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
		GuiHelper.drawHollowRect(graphics, x, y, w, h, POLAR_NIGHT_0, true);
		POLAR_NIGHT_1.draw(graphics, x + 1, y + 1, w - 2, h - 2);
		POLAR_NIGHT_0.draw(graphics, x + 1, y + 21, w - 2, 1);
		POLAR_NIGHT_0.draw(graphics, x + invitePanel.width + 1, y + invitePanel.posY, 1, invitePanel.height);
	}

	@Override
	public void drawForeground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
		super.drawForeground(graphics, theme, x, y, w, h);
		theme.drawString(graphics, Component.translatable("ftbteams.create_party"), x + w / 2, y + 7, SNOW_STORM_1, Theme.CENTERED);
	}

	@Override
	public boolean keyPressed(Key key) {
		return super.keyPressed(key);
	}

	@Override
	public boolean isInvited(GameProfile profile) {
		return invitedMembers.contains(profile);
	}

	@Override
	public void setInvited(GameProfile profile, boolean invited) {
		if (invited) {
			invitedMembers.add(profile);
		} else {
			invitedMembers.remove(profile);
		}
	}

	private class InvitePanel extends Panel {
		public InvitePanel() {
			super(CreatePartyScreen.this);
		}

		@Override
		public void addWidgets() {
			add(new TextField(this).addFlags(Theme.CENTERED).setText(Component.translatable("ftbteams.gui.add_members")));
			add(new VerticalSpaceWidget(this, 2));

			User self = Minecraft.getInstance().getUser();
			var profile = Minecraft.getInstance().getGameProfile();

			add(new NordButton(this, Component.literal("✦ ").withStyle(ChatFormatting.GOLD).append(self.getName()), FaceIcon.getFace(profile)) {
				@Override
				public void onClicked(MouseButton mouseButton) {
				}
			});

			manager.knownClientPlayers().stream()
					.filter(kcp -> kcp.isOnlineAndNotInParty() && !kcp.equals(manager.self()))
					.sorted()
					.forEach(kcp -> add(new InvitedButton(this, CreatePartyScreen.this, kcp)));
		}

		@Override
		public void alignWidgets() {
			align(new WidgetLayout.Vertical(4, 2, 1));

			width = 80;

			for (Widget widget : widgets) {
				width = Math.max(width, widget.width);
			}

			for (Widget widget : widgets) {
				widget.setX(1);
				widget.setWidth(width - 2);
			}

			settingsPanel.setPosAndSize(width + 3, 23, CreatePartyScreen.this.width - invitePanel.width - 5, CreatePartyScreen.this.height - 40);
			createTeamButton.setPosAndSize(settingsPanel.posX, CreatePartyScreen.this.height - 15, settingsPanel.width, 13);
		}
	}

	private class SettingsPanel extends Panel {
		public SettingsPanel() {
			super(CreatePartyScreen.this);
		}

		@Override
		public void addWidgets() {
			add(new TextField(this).setMaxWidth(width - 6).setText(Component.translatable("ftbteams.gui.party_name")));
			add(nameTextBox = new TextBox(this) {
				@Override
				public void drawTextBox(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
					NordColors.POLAR_NIGHT_0.draw(graphics, x, y, w, h);
				}
			});
			add(new VerticalSpaceWidget(this, 4));

			add(new TextField(this).setMaxWidth(width - 6).setText(Component.translatable("ftbteams.gui.party_description")));
			add(descriptionTextBox = new TextBox(this) {
				@Override
				public void drawTextBox(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
					NordColors.POLAR_NIGHT_0.draw(graphics, x, y, w, h);
				}
			});
			add(new VerticalSpaceWidget(this, 4));

			nameTextBox.setHeight(14);
			nameTextBox.ghostText = Minecraft.getInstance().getUser().getName() + "'s Team";
			descriptionTextBox.setHeight(14);
			descriptionTextBox.ghostText = "<None>";
		}

		@Override
		public void alignWidgets() {
			for (Widget w : widgets) {
				w.setX(3);
				w.setWidth(width - 6);
			}

			align(new WidgetLayout.Vertical(3, 3, 10));
		}

		@Override
		public void drawBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
			NordColors.POLAR_NIGHT_2.draw(graphics, x, y, w, h);
		}
	}
}
