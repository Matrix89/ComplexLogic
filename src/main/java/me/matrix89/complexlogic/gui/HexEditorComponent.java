package me.matrix89.complexlogic.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.math.MathHelper;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.lwjgl.input.Mouse;

import java.util.Random;
import java.util.regex.Pattern;

import static net.minecraft.client.gui.GuiScreen.*;

public class HexEditorComponent extends Gui {
    private int cursor = 0;

    private enum Nibble {UPPER, LOWER}

    private Nibble cursorNibble = Nibble.UPPER;

    int groupSize = 2;
    int groupsPerLine = 4;
    //spacing size in character widths
    private int spacing = 0;
    private int charWidth;
    private int scroll = 0;
    private int overScroll = 3;

    private static final int SELECTION_NONE = Integer.MAX_VALUE;
    private int selectionAnchor = SELECTION_NONE;
    private FontRenderer fontRenderer;

    private Random rnd = new Random();

    private Minecraft mc;
    private int posX;
    private int posY;
    private int h;
    private int w;

    private int scrollBarWidth = 10;

    private DataModel data;

    public HexEditorComponent(FontRenderer fontRenderer, int x, int y, int w, int h) {
        this.fontRenderer = fontRenderer;
        charWidth = fontRenderer.getCharWidth('_');
        this.posX = x;
        this.posY = y;
        this.w = w;
        this.h = h;
        spacing = 2;
        this.data = new DataModel();

        int aw = (w - scrollBarWidth) - getAddressColumnCharacterCount() * charWidth;
        groupsPerLine = aw / (charWidth * 2 * groupSize + (spacing * charWidth));
    }

    public byte[] getData() {
        return data.getRawData();
    }

    public void setData(byte[] data) {
        this.data.setRawData(data);
    }

    public void setPos(int x, int y) {
        this.posX = x;
        this.posY = y;
    }

    public void keyTyped(char typedChar, int keyCode) {
        Pattern p = Pattern.compile("[a-fA-F0-9]");
        if (!isCtrlKeyDown() && p.matcher(String.valueOf(typedChar).toUpperCase()).find()) {
            byte b = data.getByte(cursor);
            switch (cursorNibble) {
                case UPPER:
                    b &= (b << 8 | 0x0f);
                    b |= Byte.parseByte("" + typedChar, 16) << 4;

                    data.setByte(cursor, b);
                    cursorNibble = Nibble.LOWER;
                    return;
                case LOWER:
                    b &= 0xf0;
                    b |= Byte.parseByte("" + typedChar, 16);

                    data.setByte(cursor, b);
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
        for (int i = startIdx; i < Math.min(data.length(), startIdx + h / fontRenderer.FONT_HEIGHT * getBytesPreLine()); i++) {
            if (i > data.length() || i < 0) return;
            if (x != posX && i % getBytesPreLine() == 0) {
                y += fontRenderer.FONT_HEIGHT;
                x = posX;
            }
            // Address column
            if (x == posX) {
                fontRenderer.drawString(String.format("%0" + dataLenCharacterCount + "X", i), x, y, EnumDyeColor.RED.getColorValue());
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
            fontRenderer.drawString(String.format("%02X", data.getByte(i)), x, y, EnumDyeColor.WHITE.getColorValue());
            x += 2 * charWidth;
        }
        drawScrollBar();
    }

    /**
     * Deletes byte under cursor
     */
    public void deleteByte() {
        data.deleteByte(cursor);
    }

    /**
     * Inserts byte before cursor
     */
    public void insertByte() {
        data.insertByte(cursor, (byte) 0);
    }

    public float getScrollPercentage() {
        return scroll / (float) getLineCount();
    }

    public void drawScrollBar() {
        int rx = posX + w - scrollBarWidth;
        drawRect(rx, posY, rx + scrollBarWidth, posY + h, 0xff000000 | EnumDyeColor.RED.getColorValue());

        int knobH = h / Math.max(getLineCount(), 1);
        int knobY = (int) ((h * getScrollPercentage()) - knobH / 2);
        knobY = MathHelper.clamp(knobY, 0, h - knobH);
        drawRect(rx, posY + knobY, rx + scrollBarWidth, posY + knobY + knobH, 0xff000000 | EnumDyeColor.BLACK.getColorValue()); // Knob
    }

    public void setCursor(int cursor) {
        if (cursor == data.length()) {
            data.insertByte(cursor, (byte) 0);
        }
        this.cursor = Math.max(0, Math.min(data.length() - 1, cursor));
    }

    private void handledMovement(int keyCode) {
        if (isShiftKeyDown()) {
            if (selectionAnchor == SELECTION_NONE) {
                selectionAnchor = cursor;
            }
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
            case 45:
            case 46: //copy
                StringBuilder sb = new StringBuilder();
                int start = Math.min(selectionAnchor, cursor);
                int end = Math.max(selectionAnchor, cursor);
                for (int i = start; i <= end; i++) {
                    sb.append(String.format("%02X", data.getByte(i)));
                }
                setClipboardString(sb.toString());
                if (keyCode == 45) {
                }
                break;
            case 47: //paste
                String cb = getClipboardString();

                byte[] cbData;
                try {
                    cbData = Hex.decodeHex(cb.toCharArray());
                } catch (DecoderException e) {
                    break;
                }
                for (byte c : cbData) {
                    data.setByte(cursor, c);
                    setCursor(++cursor);
                }

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
        return ("" + data.length()).length();
    }

    public int getDataLength() {
        return data.length();
    }


    public int getBytesPreLine() {
        return groupsPerLine * groupSize;
    }

    public int getLineCount() {
        return data.length() / getBytesPreLine() - ((h / fontRenderer.FONT_HEIGHT) - overScroll);
    }

    public void handleMouseInput() {
        int s = Mouse.getEventDWheel();
        if (s != 0) {
            addScroll(s > 0 ? -1 : 1);
        }
    }

    public int getSelectionLength() {
        if (selectionAnchor > cursor) {
            return selectionAnchor - cursor + 1;
        } else {
            return cursor - selectionAnchor + 1;
        }
    }

    public void addScroll(int a) {
        scroll = MathHelper.clamp(scroll + a, 0, getLineCount());
    }

    public int getSelectionStart() {
        if (selectionAnchor > cursor) {
            return cursor;
        } else {
            return selectionAnchor;
        }
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

    private class DataModel {
        private final static int MAX_DATA_LEN = 65535;
        private byte[] data = new byte[0];

        private byte[] range(int start, int end) {
            start = MathHelper.clamp(start, 0, data.length);
            end = MathHelper.clamp(end, 0, data.length);

            byte[] out = new byte[Math.abs(start - end)];
            System.arraycopy(data, start, out, 0, out.length);
            return out;
        }

        private void deleteByte(int pos) {
            if (pos < 0 || data.length == 0 || pos >= data.length) return;
            byte[] newData = new byte[data.length - 1];
            try {
                System.arraycopy(data, 0, newData, 0, pos);
                System.arraycopy(data, pos + 1, newData, pos, data.length - pos - 1);
            }catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
            data = newData;
        }

        private void insertByte(int pos, byte b) {
            byte[] newData = new byte[data.length + 1];

            try {
                System.arraycopy(data, 0, newData, 0, Math.max(0, pos));
                System.arraycopy(data, pos, newData, pos + 1, data.length - pos);

                newData[pos] = b;
            }catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }

            data = newData;
        }

        private void setRawData(byte[] b) {
            data = b;
        }

        private void setByte(int pos, byte b) {
            if (pos < 0 || pos > data.length) return;
            data[pos] = b;
        }


        private byte getByte(int pos) {
            return data[pos];
        }

        private int length() {
            return data.length;
        }

        private byte[] getRawData() {
            return data;
        }
    }
}
