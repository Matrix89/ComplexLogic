package me.matrix89.complexlogic.utils;

import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import org.lwjgl.input.Keyboard;

import java.util.*;

public class HIDUtil {

    public static LinkedHashMap<Integer, HIDEntry> keys = new LinkedHashMap<>();

    static {
        regenerateKeys();
    }

    public static void regenerateKeys() {
        keys.put(29, new HIDEntry(0x0100, "Ctrl", "Ctrl", (int) (20 * 1.5f), 4));
        keys.put(219, new HIDEntry(0x0800, "MC", "MC", (int) (20 * 1.2f), 4));
        keys.put(56, new HIDEntry(0x0400, "Alt", "Alt", (int) (20 * 1.2f), 4));
        keys.put(57, new HIDEntry(0x2c00, " ", " ", (int) (20 * 7.4f), 4));
        keys.put(184, new HIDEntry(0x4000, "Alt", "Alt", (int) (20*1.2f), 4));
        keys.put(220, new HIDEntry(0x8000, "MC", "MC", (int) (20*1.2f), 4));
        keys.put(221, new HIDEntry(0x7600, "M", "M", (int) (20*1.2f), 4));
        keys.put(157, new HIDEntry(0x1000, "Ctrl", "Ctrl", (int) (20*1.5f), 4));

        keys.put(15, new HIDEntry(0x2b, "Tab", "Tab", (int) (20 * 1.5f), 1));
        keys.put(16, new HIDEntry(0x14, "q", "Q", 20, 1));
        keys.put(17, new HIDEntry(0x1a, "w", "W", 20, 1));
        keys.put(18, new HIDEntry(0x08, "e", "E", 20, 1));
        keys.put(19, new HIDEntry(0x15, "r", "R", 20, 1));
        keys.put(20, new HIDEntry(0x17, "t", "T", 20, 1));
        keys.put(21, new HIDEntry(0x1c, "y", "Y", 20, 1));
        keys.put(22, new HIDEntry(0x18, "u", "U", 20, 1));
        keys.put(23, new HIDEntry(0x0c, "i", "I", 20, 1));
        keys.put(24, new HIDEntry(0x12, "o", "O", 20, 1));
        keys.put(25, new HIDEntry(0x13, "p", "P", 20, 1));
        keys.put(26, new HIDEntry(0x2f, "[", "{", 20, 1));
        keys.put(27, new HIDEntry(0x30, "]", "}", 20, 1));
        keys.put(43, new HIDEntry(0x31, "\\", "|", (int) (20 * 1.5f), 1));

        keys.put(1,  new HIDEntry(0x29, "Esc", "Esc", (int) (20 * 1.7f), 2));
        keys.put(30, new HIDEntry(0x04, "a", "A", 20, 2));
        keys.put(31, new HIDEntry(0x16, "s", "S", 20, 2));
        keys.put(32, new HIDEntry(0x07, "d", "D", 20, 2));
        keys.put(33, new HIDEntry(0x09, "f", "F", 20, 2));
        keys.put(34, new HIDEntry(0x0a, "g", "G", 20, 2));
        keys.put(35, new HIDEntry(0x0b, "h", "H", 20, 2));
        keys.put(36, new HIDEntry(0x0d, "j", "J", 20, 2));
        keys.put(37, new HIDEntry(0x0e, "k", "K", 20, 2));
        keys.put(38, new HIDEntry(0x0f, "l", "L", 20, 2));
        keys.put(39, new HIDEntry(0x33, ";", ":", 20, 2));
        keys.put(40, new HIDEntry(0x34, "'", "\"", 20, 2));
        keys.put(28, new HIDEntry(0x28, "Enter", "Enter", (int) (20 * 2.5f), 2));

        keys.put(42, new HIDEntry(0x02, "Shift", "Shift", (int) (20 * 2.2f),3));
        keys.put(44, new HIDEntry(0x1d, "z", "Z", 20, 3));
        keys.put(45, new HIDEntry(0x1b, "x", "X", 20, 3));
        keys.put(46, new HIDEntry(0x06, "c", "C", 20, 3));
        keys.put(47, new HIDEntry(0x19, "v", "V", 20, 3));
        keys.put(48, new HIDEntry(0x05, "b", "B", 20, 3));
        keys.put(49, new HIDEntry(0x11, "n", "N", 20, 3));
        keys.put(50, new HIDEntry(0x10, "m", "M", 20, 3));
        keys.put(51, new HIDEntry(0x36, ",", "<", 20, 3));
        keys.put(52, new HIDEntry(0x37, ".", ">", 20, 3));
        keys.put(53, new HIDEntry(0x38, "/", "?", 20, 3));
        keys.put(54, new HIDEntry(0x02, "Shift", "Shift", (int) (20 * 3.2f), 3));

        keys.put(41, new HIDEntry(0x35, "`", "~", 20, 0));
        keys.put(2, new HIDEntry(0x1e, "1", "!", 20, 0));
        keys.put(3, new HIDEntry(0x1f, "2", "@", 20, 0));
        keys.put(4, new HIDEntry(0x20, "3", "#", 20, 0));
        keys.put(5, new HIDEntry(0x21, "4", "$", 20, 0));
        keys.put(6, new HIDEntry(0x22, "5", "%", 20, 0));
        keys.put(7, new HIDEntry(0x23, "6", "^", 20, 0));
        keys.put(8, new HIDEntry(0x24, "7", "&", 20, 0));
        keys.put(9, new HIDEntry(0x25, "8", "*", 20, 0));
        keys.put(10, new HIDEntry(0x26, "9", "(", 20, 0));
        keys.put(11, new HIDEntry(0x27, "0", ")", 20, 0));
        keys.put(12, new HIDEntry(0x2d, "-", "_", 20, 0));
        keys.put(13, new HIDEntry(0x2e, "=", "+", 20, 0));
        keys.put(14, new HIDEntry(0x2a, "<-- BS", "<-- BS", (int) (20 * 2f), 0));
    }

    public static class HIDEntry {
        public final int scanCode;
        public final String lowercase;
        public final String uppercase;
        public final int keyWidth;
        public final int row;

        public HIDEntry(int scanCode, String lowercase, String uppercase, int keyWidth, int row) {
            this.scanCode = scanCode;
            this.lowercase = lowercase;
            this.uppercase = uppercase;
            this.keyWidth = keyWidth;
            this.row = row;
        }
    }
}
