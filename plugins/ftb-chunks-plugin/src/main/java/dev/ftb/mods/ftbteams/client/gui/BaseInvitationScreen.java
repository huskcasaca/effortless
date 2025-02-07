package dev.ftb.mods.ftbteams.client.gui;

import com.mojang.authlib.GameProfile;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.icon.Icons;
import dev.ftb.mods.ftblibrary.ui.*;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftblibrary.ui.misc.NordColors;
import dev.ftb.mods.ftbteams.api.FTBTeamsAPI;
import dev.ftb.mods.ftbteams.api.client.KnownClientPlayer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static dev.ftb.mods.ftblibrary.ui.misc.NordColors.*;

public abstract class BaseInvitationScreen extends BaseScreen implements InvitationSetup {
    protected final Set<KnownClientPlayer> available;
    protected final Set<GameProfile> invites = new HashSet<>();
    private Panel playerPanel;
    private Button executeButton;
    private Button closeButton;
    private final Component title;

    public BaseInvitationScreen(Component title) {
        this.title = title;

        this.available = FTBTeamsAPI.api().getClientManager().knownClientPlayers().stream()
                .filter(this::shouldIncludePlayer)
                .collect(Collectors.toSet());
    }

    protected abstract boolean shouldIncludePlayer(KnownClientPlayer player);

    protected abstract ExecuteButton makeExecuteButton();

    @Override
    public boolean onInit() {
        setWidth(200);
        setHeight(getScreen().getGuiScaledHeight() * 3 / 4);
        return true;
    }

    @Override
    public void drawBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
        GuiHelper.drawHollowRect(graphics, x, y, w, h, POLAR_NIGHT_0, true);
        POLAR_NIGHT_0.draw(graphics, x + 1, y + 1, w - 2, h - 2);
        POLAR_NIGHT_1.draw(graphics, x + playerPanel.posX, y + playerPanel.posY, playerPanel.width, playerPanel.height);
        POLAR_NIGHT_0.draw(graphics, x + 1, y + h - 20, w - 2, 18);
    }

    @Override
    public void drawForeground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
        theme.drawString(graphics, title, x + w / 2, y + 5, SNOW_STORM_1, Theme.CENTERED);
    }

    @Override
    public void addWidgets() {
        add(closeButton = new SimpleButton(this, Component.translatable("gui.cancel"), Icons.CANCEL.withTint(SNOW_STORM_2), (simpleButton, mouseButton) -> closeGui()) {
            @Override
            public void draw(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
                drawIcon(graphics, theme, x, y, w, h);
            }
        });

        add(playerPanel = new PlayerButtonPanel());

        add(executeButton = makeExecuteButton());
    }

    @Override
    public void alignWidgets() {
        closeButton.setPosAndSize(width - 20, 2, 16, 16);
        playerPanel.setPosAndSize(2, 20, width - 4, height - 40);
        executeButton.setPosAndSize(60, height - 18, 80, 16);
    }

    @Override
    public boolean isInvited(GameProfile profile) {
        return invites.contains(profile);
    }

    @Override
    public void setInvited(GameProfile profile, boolean invited) {
        if (invited) {
            invites.add(profile);
        } else {
            invites.remove(profile);
        }
    }

    private class PlayerButtonPanel extends Panel {
        public PlayerButtonPanel() {
            super(BaseInvitationScreen.this);
        }

        @Override
        public void addWidgets() {
            if (available.isEmpty()) {
                add(new TextField(this).setText(Component.translatable("ftbteams.gui.no_players").withStyle(ChatFormatting.ITALIC)).addFlags(Theme.CENTERED));
            } else {
                available.forEach(player -> add(new InvitedButton(this, BaseInvitationScreen.this, player)));
            }
        }

        @Override
        public void alignWidgets() {
            align(new WidgetLayout.Vertical(2, 1, 2));
            widgets.forEach(w -> w.setX(4));
        }

        @Override
        public void drawBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
            NordColors.POLAR_NIGHT_2.draw(graphics, x, y, w, h);
        }
    }

    protected abstract class ExecuteButton extends NordButton {
        private final Component titleDark;
        private final Runnable callback;

        public ExecuteButton(Component txt, Icon icon, Runnable callback) {
            super(BaseInvitationScreen.this, txt, icon);
            this.titleDark = title.copy().withStyle(Style.EMPTY.withColor(POLAR_NIGHT_0.rgb()));
            this.callback = callback;
        }

        @Override
        public void onClicked(MouseButton button) {
            if (isEnabled()) callback.run();
        }

        @Override
        public Component getTitle() {
            return isEnabled() ? title : titleDark;
        }

        @Override
        public boolean renderTitleInCenter() {
            return true;
        }
    }
}
