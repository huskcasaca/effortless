package dev.huskuraft.effortless.vanilla.adapters;

import dev.huskuraft.effortless.text.Text;
import dev.huskuraft.effortless.text.TextStyle;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.util.Arrays;

class MinecraftText extends Text {

    private final Component component;

    MinecraftText(Component component) {
        this.component = component;
    }

    public Component getRef() {
        return component;
    }

    @Override
    public Text withStyle(TextStyle... styles) {
        return MinecraftAdapter.adapt(component.copy().withStyle(Arrays.stream(styles).map(MinecraftAdapter::adapt).toArray(ChatFormatting[]::new)));
    }

    @Override
    public Text append(Text append) {
        return MinecraftAdapter.adapt(component.copy().append(MinecraftAdapter.adapt(append)));
    }

    @Override
    public Text copy() {
        return MinecraftAdapter.adapt(component.copy());
    }

    @Override
    public String getString() {
        return getRef().getString();
    }

}
