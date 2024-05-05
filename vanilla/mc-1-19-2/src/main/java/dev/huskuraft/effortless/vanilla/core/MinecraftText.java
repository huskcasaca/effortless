package dev.huskuraft.effortless.vanilla.core;

import java.util.Collection;
import java.util.stream.Collectors;

import dev.huskuraft.effortless.api.text.Style;
import dev.huskuraft.effortless.api.text.Text;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;

public record MinecraftText(Component referenceValue) implements Text {

    public static Text ofNullable(Component reference) {
        return reference == null ? null : new MinecraftText(reference);
    }


    @Override
    public Text withBold(Boolean bold) {
        return new MinecraftText(referenceValue().copy().withStyle(referenceValue().getStyle().withBold(bold)));
    }

    @Override
    public Text withItalic(Boolean italic) {
        return new MinecraftText(referenceValue().copy().withStyle(referenceValue().getStyle().withItalic(italic)));
    }

    @Override
    public Text withUnderlined(Boolean underlined) {
        return new MinecraftText(referenceValue().copy().withStyle(referenceValue().getStyle().withUnderlined(underlined)));
    }

    @Override
    public Text withStrikethrough(Boolean strikethrough) {
        return new MinecraftText(referenceValue().copy().withStyle(referenceValue().getStyle().withStrikethrough(strikethrough)));
    }

    @Override
    public Text withObfuscated(Boolean obfuscated) {
        return new MinecraftText(referenceValue().copy().withStyle(referenceValue().getStyle().withObfuscated(obfuscated)));
    }

    @Override
    public Text withColor(Integer color) {
        return new MinecraftText(referenceValue().copy().withStyle(referenceValue().getStyle().withColor(color == null ? null : TextColor.fromRgb(color))));
    }

    @Override
    public Boolean isBold() {
        return referenceValue().getStyle().isBold();
    }

    @Override
    public Boolean isItalic() {
        return referenceValue().getStyle().isItalic();
    }

    @Override
    public Boolean isUnderlined() {
        return referenceValue().getStyle().isUnderlined();
    }

    @Override
    public Boolean isStrikethrough() {
        return referenceValue().getStyle().isStrikethrough();
    }

    @Override
    public Boolean isObfuscated() {
        return referenceValue().getStyle().isObfuscated();
    }

    @Override
    public Integer getColor() {
        return referenceValue().getStyle().getColor() == null ? null : referenceValue().getStyle().getColor().getValue();
    }

    @Override
    public String getString() {
        return referenceValue().getString();
    }

    @Override
    public Collection<Text> getSiblings() {
        return referenceValue().getSiblings().stream().map(MinecraftText::new).collect(Collectors.toList());
    }

    @Override
    public Text append(Text text) {
        return new MinecraftText(referenceValue().copy().append((Component) text.reference()));
    }

    @Override
    public void decompose(Sink sink) {
        referenceValue().getVisualOrderText().accept((i, style, i1) -> sink.accept(i, Character.toString(i1), new Style(style.getColor() == null ? null : style.getColor().getValue(), style.isBold(), style.isItalic(), style.isUnderlined(), style.isStrikethrough(), style.isObfuscated())));
    }


}
