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
    public Style getStyle() {
        var style = referenceValue().getStyle();
        return new Style(
                style.getColor() == null ? null : style.getColor().getValue(),
                style.isBold(),
                style.isItalic(),
                style.isUnderlined(),
                style.isStrikethrough(),
                style.isObfuscated()
        );
    }

    @Override
    public Text withStyle(Style style) {
        return new MinecraftText(
                referenceValue().copy().setStyle(
                        referenceValue().getStyle()
                                .withColor(style.color() == null ? null : TextColor.fromRgb(style.color()))
                                .withBold(style.bold())
                                .withItalic(style.italic())
                                .withUnderlined(style.underlined())
                                .withStrikethrough(style.strikethrough())
                                .withObfuscated(style.obfuscated())
                )
        );
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
    public Text withSiblings(Collection<Text> siblings) {
        var minecraftText = referenceValue().copy();
        minecraftText.getSiblings().clear();
        siblings.stream().map(sibling -> (Component) sibling.reference()).forEach(minecraftText::append);
        return new MinecraftText(minecraftText);
    }

    @Override
    public Text copy() {
        return new MinecraftText(referenceValue().copy());
    }

    @Override
    public void decompose(Sink sink) {
        referenceValue().getVisualOrderText().accept((i, style, i1) -> sink.accept(i, Character.toString(i1), new Style(style.getColor() == null ? null : style.getColor().getValue(), style.isBold(), style.isItalic(), style.isUnderlined(), style.isStrikethrough(), style.isObfuscated())));
    }


}
