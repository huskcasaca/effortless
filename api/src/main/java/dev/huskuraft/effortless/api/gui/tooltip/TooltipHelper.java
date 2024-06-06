package dev.huskuraft.effortless.api.gui.tooltip;

import java.text.BreakIterator;
import java.util.LinkedList;
import java.util.List;

import dev.huskuraft.effortless.api.gui.Typeface;
import dev.huskuraft.effortless.api.input.Keys;
import dev.huskuraft.effortless.api.text.ChatFormatting;
import dev.huskuraft.effortless.api.text.Style;
import dev.huskuraft.effortless.api.text.Text;

public class TooltipHelper {

    public static final int MAX_WIDTH_PER_LINE = 200;

    public static String makeProgressBar(int length, int filledLength) {
        return " " + "█".repeat(Math.max(0, filledLength)) + "▒".repeat(Math.max(0, length - filledLength)) + " ";
    }

    private static ChatFormatting getLastTextStyle(String string) {
        var index = string.lastIndexOf(ChatFormatting.PREFIX_CODE);
        if (index != -1 && index + 1 != string.length()) {
            return ChatFormatting.getByCode(string.charAt(index + 1));
        }
        return null;
    }

    public static List<Text> wrapLines(Typeface typeface, Text text) {
        return wrapLines(typeface, text, MAX_WIDTH_PER_LINE);
    }

    public static List<Text> wrapLines(Typeface typeface, Text text, int lineWidth) {
        var letters = new StringBuilder();
        var styles = new LinkedList<Style>();

        text.decompose((index, chr, style) -> {
            for (int i = 0; i < chr.length(); i++) {
                letters.append(chr.charAt(i));
                styles.add(style);
            }
            return true;
        });

        var iterator = BreakIterator.getLineInstance();
        iterator.setText(letters.toString());
        int start = iterator.first();
        var words = new LinkedList<Text>();

        for (var end = iterator.next(); end != BreakIterator.DONE; start = end, end = iterator.next()) {

            var word = letters.substring(start, end);
            var worldStyle = styles.subList(start, end);

            var wrapLine = false;
            if (word.endsWith("\n")) {
                word = word.substring(0, word.length() - 1);
                wrapLine = true;
            }

            var result = Text.empty();
            for (var i = 0; i < word.length(); i++) {
                result = result.append(Text.text(word.substring(i, i + 1)).withStyle(worldStyle.get(i)));
            }
            words.add(result);

            if (wrapLine) {
                words.add(Text.text("\n"));
            }
        }

        var lines = new LinkedList<Text>();
        var lastLine = Text.empty();
        var width = 0;
        for (var word : words) {
            if (word.getString().equals("\n")) {
                lines.add(lastLine);
                lastLine = Text.empty();
                width = 0;
                continue;
            }
            var newWidth = typeface.measureWidth(word);
            if (width + newWidth > lineWidth) {
                lines.add(lastLine);
                lastLine = Text.empty();
                width = 0;
            }
            lastLine = lastLine.append(word);
            width += newWidth;
        }
        if (width > 0) {
            lines.add(lastLine);
        }

        return lines;
    }

    public static Text holdShiftForSummary() {
        if (isSummaryButtonDown()) {
            return Text.translate("effortless.tooltip.hold_for_summary", Text.translate("key.effortless.shift").withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_GRAY);
        } else {
            return Text.translate("effortless.tooltip.hold_for_summary", Text.translate("key.effortless.shift").withStyle(ChatFormatting.DARK_GRAY)).withStyle(ChatFormatting.DARK_GRAY);
        }
    }

    public static boolean isSummaryButtonDown() {
        return Keys.KEY_LEFT_SHIFT.getBinding().isDown() || Keys.KEY_LEFT_SHIFT.getBinding().isDown();
    }

}
