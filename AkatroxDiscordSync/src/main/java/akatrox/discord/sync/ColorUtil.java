package akatrox.discord.sync;
import net.md_5.bungee.api.ChatColor;

public class ColorUtil {
    public static String translateHexColorCodes(String message) {
        final char colorChar = ChatColor.COLOR_CHAR;
        StringBuilder translatedMessage = new StringBuilder();
        int length = message.length();

        for (int i = 0; i < length; i++) {
            char c = message.charAt(i);
            if (c == '#' && i + 6 < length) {
                String hexCode = message.substring(i, i + 7);
                try {
                    ChatColor color = ChatColor.of(hexCode);
                    translatedMessage.append(color.toString());
                    i += 6;
                } catch (IllegalArgumentException ex) {
                    translatedMessage.append(c);
                }
            } else if (c == '&' && i + 1 < length) {

                char colorCode = message.charAt(i + 1);
                translatedMessage.append(colorChar).append(colorCode);
                i++;
            } else {
                translatedMessage.append(c);
            }
        }

        return translatedMessage.toString();
    }


    public static String translateAnsiColorCodes(String message) {
        return message
                .replace("&0", "\u001B[30m")  // Black
                .replace("&1", "\u001B[34m")  // Dark Blue
                .replace("&2", "\u001B[32m")  // Dark Green
                .replace("&3", "\u001B[36m")  // Dark Aqua
                .replace("&4", "\u001B[31m")  // Dark Red
                .replace("&5", "\u001B[35m")  // Dark Purple
                .replace("&6", "\u001B[33m")  // Gold
                .replace("&7", "\u001B[37m")  // Gray
                .replace("&8", "\u001B[90m")  // Dark Gray
                .replace("&9", "\u001B[94m")  // Blue
                .replace("&a", "\u001B[92m")  // Green
                .replace("&b", "\u001B[96m")  // Aqua
                .replace("&c", "\u001B[91m")  // Red
                .replace("&d", "\u001B[95m")  // Light Purple
                .replace("&e", "\u001B[93m")  // Yellow
                .replace("&f", "\u001B[97m")  // White
                .replace("&r", "\u001B[0m");  // Reset
    }


    public static String color(String message) {
        return translateHexColorCodes(message);
    }
}
