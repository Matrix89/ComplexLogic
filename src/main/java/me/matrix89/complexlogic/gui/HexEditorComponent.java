package me.matrix89.complexlogic.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Mouse;

import java.util.Random;
import java.util.regex.Pattern;

import static net.minecraft.client.gui.GuiScreen.*;

public class HexEditorComponent extends Gui {
    private int cursor = 0;

    private enum Nibble {UPPER, LOWER}

    private Nibble cursorNibble = Nibble.UPPER;

    public int groupSize = 2;
    public int groupsPerLine = 4;
    //spacing size in character widths
    private int spacing = 0;
    private int charWidth;
    private int scroll = 0;
    private int overScroll = 3;

    private static final int SELECTION_NONE = Integer.MAX_VALUE;
    private int selectionAnchor = SELECTION_NONE;
    private FontRenderer fontRenderer;

    private Random rnd = new Random();

    private byte[] data = new byte[512];

    private Minecraft mc;
    private int posX;
    private int posY;
    private int h;
    private int w;

    private int scrollBarWidth = 10;


    public HexEditorComponent(FontRenderer fontRenderer, int x, int y, int w, int h) {
        this.fontRenderer = fontRenderer;
        charWidth = fontRenderer.getCharWidth('_');
        this.posX = x;
        this.posY = y;
        this.w = w;
        this.h = h;
        spacing = 2;

        int aw = (w - scrollBarWidth) - getAddressColumnCharacterCount() * charWidth;
        groupsPerLine = aw / (charWidth * 2 * groupSize + (spacing * charWidth));
    }

    public void setPos(int x, int y) {
        this.posX = x;
        this.posY = y;
    }

    public void keyTyped(char typedChar, int keyCode) {
        Pattern p = Pattern.compile("[a-fA-F0-9]");
        if (!isCtrlKeyDown() && p.matcher(String.valueOf(typedChar).toUpperCase()).find()) {
            switch (cursorNibble) {
                case UPPER:
                    data[cursor] &= (data[cursor] << 8 | 0x0f);
                    data[cursor] |= Byte.parseByte("" + typedChar, 16) << 4;

                    cursorNibble = Nibble.LOWER;
                    return;
                case LOWER:
                    data[cursor] &= 0xf0;
                    data[cursor] |= Byte.parseByte("" + typedChar, 16);
                    setCursor(cursor + 1);
                    cursorNibble = Nibble.UPPER;
            }
            return;
        }

        handledMovement(keyCode);
    }

    private void mouseUpdateCursor(int mouseX, int mouseY) {
        mouseX -= posX;
        mouseY -= posY;

        //remove the address column
        mouseX -= getAddressColumnCharacterCount() * charWidth + spacing * charWidth;

        int line = (mouseY / fontRenderer.FONT_HEIGHT) + scroll;

        int clickedGroup = (int) (mouseX / (float) (((2 * groupSize) + spacing) * charWidth));
        int groupWidth = (2 * charWidth * groupSize) + (spacing * charWidth);
        int groupX = clickedGroup * groupWidth;
        int xInGroup = mouseX - groupX;
        int byteInGroup = xInGroup / (2 * charWidth);

        int column = clickedGroup * groupSize + byteInGroup;

        setCursor(column + line * getBytesPreLine());
    }


    public void draw() {
        int x = posX;
        int y = posY;
        int startIdx = scroll * getBytesPreLine();
        int dataLenCharacterCount = getAddressColumnCharacterCount();
        for (int i = startIdx; i < Math.min(data.length, startIdx + h / fontRenderer.FONT_HEIGHT * getBytesPreLine()); i++) {
            if (i > data.length || i < 0) return;
            if (x != posX && i % getBytesPreLine() == 0) {
                y += fontRenderer.FONT_HEIGHT;
                x = posX;
            }
            // Address column
            if(x == posX) {
                fontRenderer.drawString(String.format("%0" + dataLenCharacterCount +"X", i), x, y, EnumDyeColor.RED.getColorValue());
                x += dataLenCharacterCount * charWidth;
            }

            if (x != posX && i % groupSize == 0) {
                x += spacing * charWidth;
            }

            if (selectionAnchor != SELECTION_NONE && selectionAnchor != cursor && ((i >= selectionAnchor && i <= cursor) || (i <= selectionAnchor && i >= cursor))) {
                drawRect(x, y + fontRenderer.FONT_HEIGHT, x + 2 * charWidth, y, 0xff000000 | EnumDyeColor.GREEN.getColorValue());
            }
            if (i == cursor) {
                int nx = x + (cursorNibble == Nibble.UPPER ? 0 : charWidth);
                drawRect(nx, (int) (y + fontRenderer.FONT_HEIGHT * 0.9f), nx + charWidth, y + fontRenderer.FONT_HEIGHT, 0xff000000 | EnumDyeColor.BLUE.getColorValue());
            }
            fontRenderer.drawString(String.format("%02X", data[i]), x, y, EnumDyeColor.WHITE.getColorValue());
            x += 2 * charWidth;
        }
        drawScrollBar();
    }

    /**
     * Deletes byte under cursor
     */
    public void deleteByte() {
        byte[] newData = new byte[data.length + 1];
        System.arraycopy(data, 0, newData, 0, Math.max(0, cursor - 1));
        System.arraycopy(data, cursor, newData, cursor + 1, data.length - cursor);
        data = newData;
    }

    /**
     * Inserts byte before cursor
     */
    public void insertByte() {
        byte[] newData = new byte[data.length - 1];
        System.arraycopy(data, 0, newData, 0, cursor);
        System.arraycopy(data, cursor + 1, newData, cursor, data.length - cursor - 1);
        if (cursor >= data.length - 1) {
            setCursor(cursor - 1);
        }
        data = newData;
    }

    public float getScrollPercentage() {
        return scroll / (float) getLineCount();
    }

    public void drawScrollBar() {
        int rx = posX + w - scrollBarWidth;
        drawRect(rx, posY, rx + scrollBarWidth, posY + h, 0xff000000 | EnumDyeColor.RED.getColorValue());

        int knobH = h / getLineCount();
        int knobY = (int) ((h * getScrollPercentage()) - knobH / 2);
        knobY = MathHelper.clamp(knobY, 0, h- knobH);
        drawRect(rx, posY + knobY, rx + scrollBarWidth, posY + knobY + knobH, 0xff000000 | EnumDyeColor.BLACK.getColorValue()); // Knob
    }

    public void setCursor(int cursor) {
        this.cursor = Math.max(0, Math.min(cursor, data.length - 1));
    }

    private void handledMovement(int keyCode) {
        if (isShiftKeyDown()) {
            if (selectionAnchor == SELECTION_NONE) {
                selectionAnchor = cursor;
            }
        } else {
            selectionAnchor = SELECTION_NONE;
        }
        switch (keyCode) {
            case 205:
                setCursor(++cursor);
                break;
            case 203:
                setCursor(--cursor);
                break;
            case 200://up
                setCursor(cursor - getBytesPreLine());
                break;
            case 208://down
                setCursor(cursor + getBytesPreLine());
                break;
            case 46: //copy
                StringBuilder sb = new StringBuilder();
                int start = Math.min(selectionAnchor, cursor);
                int end = Math.max(selectionAnchor, cursor);
                for (int i = start; i <= end; i++) {
                    sb.append(String.format("%02X", data[i]));
                }
                setClipboardString(sb.toString());
                break;
            case 47: //paste
                break;
            case 13: // +
                addScroll(1);
                break;
            case 12: //-
                addScroll(-1);
                break;

        }
        cursorNibble = Nibble.UPPER;
    }

    private int getAddressColumnCharacterCount() {
        return (""+data.length).length();
    }

    public int getDataLength() {
        return data.length;
    }


    public int getBytesPreLine() {
        return groupsPerLine * groupSize;
    }

    public int getLineCount() {
        return data.length / getBytesPreLine() - ((h / fontRenderer.FONT_HEIGHT) - overScroll);
    }

    public void handleMouseInput() {
        int s = Mouse.getEventDWheel();
        if (s != 0) {
            addScroll(s > 0 ? -1 : 1);
        }
    }

    public void addScroll(int a) {
        scroll = MathHelper.clamp(scroll + a, 0, getLineCount());
    }


    public void mouseReleased(int mouseX, int mouseY, int state) {
        if (selectionAnchor == cursor) {
            selectionAnchor = SELECTION_NONE;
        }
    }

    public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        mouseUpdateCursor(mouseX, mouseY);
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        mouseUpdateCursor(mouseX, mouseY);
        selectionAnchor = cursor;
    }
}
