package dev.huskuraft.effortless.api.gui.tooltip;

import java.text.BreakIterator;
import java.util.LinkedList;
import java.util.List;

import dev.huskuraft.effortless.api.gui.Typeface;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.api.text.TextStyle;

public class TooltipHelper {

    public static final int MAX_WIDTH_PER_LINE = 200;

    public static String makeProgressBar(int length, int filledLength) {
        return " " + "█".repeat(Math.max(0, filledLength)) + "▒".repeat(Math.max(0, length - filledLength)) + " ";
    }

    private static TextStyle getLastTextStyle(String string) {
        var index = string.lastIndexOf(TextStyle.PREFIX_CODE);
        if (index != -1 && index + 1 != string.length()) {
            return TextStyle.getByCode(string.charAt(index + 1));
        }
        return null;
    }

    public static List<Text> wrapLines(Typeface typeface, Text text) {
        return wrapLines(typeface, text.getString()).stream().map(Text::text).toList();
    }


    public static List<String> wrapLines(Typeface typeface, String text) {
        var words = new LinkedList<String>();
        var iterator = BreakIterator.getLineInstance();
        iterator.setText(text);
        int start = iterator.first();

        for (var end = iterator.next(); end != BreakIterator.DONE; start = end, end = iterator.next()) {
            var word = text.substring(start, end);
            words.add(word);
        }

        var lines = new LinkedList<String>();
        var currentLine = new StringBuilder();
        var newLine = false;
        var width = 0;
        for (var word : words) {
            var newWidth = typeface.measureWidth(word.replaceAll("_", ""));
            if (width + newWidth > MAX_WIDTH_PER_LINE) {
                if (width > 0) {
                    var line = currentLine.toString();
                    lines.add(line);
                    currentLine = new StringBuilder();
                    newLine = true;
                    width = 0;
                } else {
                    lines.add(word);
                    continue;
                }
            }
            if (newLine) {
                newLine = false;
                var style = getLastTextStyle(lines.get(lines.size() - 1));
                if (style != null) {
                    currentLine.append(style);
                }
            }
            currentLine.append(word);
            width += newWidth;
        }
        if (width > 0) {
            lines.add(currentLine.toString());
        }

        return lines;
    }
}
