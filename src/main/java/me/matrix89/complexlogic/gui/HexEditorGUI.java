package me.matrix89.complexlogic.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.item.EnumDyeColor;
import pl.asie.charset.lib.inventory.GuiContainerCharset;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HexEditorGUI extends GuiContainerCharset<HexEditorContainer> {
    private int cursor = 0;

    private enum Nibble {UPPER, LOWER}

    private Nibble cursorNibble = Nibble.UPPER;

    private int groupSize = 2;
    private int groupsPerLine = 4;
    //spacing size in character widths
    private int spacing = 2;
    private int charWidth;

    private static final int SELECTION_NONE = Integer.MAX_VALUE;
    private int selectionAnchor = SELECTION_NONE;

    private Random rnd = new Random();

    private byte[] data = new byte[256];

    private ArrayList<GuiNumberField> textFields = new ArrayList<>();
    private GuiTextField focusedField = null;

    private int scroll = 0;

    public HexEditorGUI(HexEditorContainer container, int xSize, int ySize) {
        super(container, xSize, ySize);
        rnd.nextBytes(data);
    }

    @Override
    public void initGui() {
        charWidth = fontRenderer.getCharWidth('_');
        rnd.nextBytes(data);
        spacing = 1;

        GuiNumberField gplField = new GuiNumberField(3, fontRenderer, 200, 30, 30, fontRenderer.FONT_HEIGHT, mc);
        gplField.setValue(groupsPerLine);
        gplField.setOnClick(integer -> {
            if (integer <= 0) {
                gplField.setValue(1);
                groupsPerLine = 1;
            } else {
                groupsPerLine = integer;
            }
        });
        textFields.add(gplField);

        GuiNumberField grSzField = new GuiNumberField(3, fontRenderer, 200, 40, 30, fontRenderer.FONT_HEIGHT, mc);
        grSzField.setValue(groupSize);
        grSzField.setOnClick(integer -> {
            if (integer <= 0) {
                grSzField.setValue(1);
                groupSize = 1;
            } else {
                groupSize = integer;
            }
        });
        textFields.add(grSzField);

        super.initGui();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (focusedField != null) {
            if (keyCode == 1) {
                focusedField.setFocused(false);
                focusedField = null;
                return;
            }
            focusedField.textboxKeyTyped(typedChar, keyCode);
            return;
        }
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
        super.keyTyped(typedChar, keyCode);
    }

    private void handledMovement(int keyCode) {
        if (isShiftKeyDown()) {
            if(selectionAnchor == SELECTION_NONE) {
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
                setCursor(cursor - (groupSize * groupsPerLine));
                break;
            case 208://down
                setCursor(cursor + (groupSize * groupsPerLine));
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
                scroll++;
                break;
            case 12: //-
                scroll = Math.max(scroll - 1, 0);
                break;

        }
        cursorNibble = Nibble.UPPER;
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        if(selectionAnchor == cursor) {
            selectionAnchor = SELECTION_NONE;
        }
    }

    private void mouseUpdateCursor(int mouseX, int mouseY) {
        int line = (mouseY / fontRenderer.FONT_HEIGHT) + scroll  -1;
        int printedSpacingWidth = mouseX / (2 * charWidth * groupSize + spacing * charWidth);
        int column = (mouseX / charWidth - printedSpacingWidth) / 2;

        setCursor(column + line * (groupSize * groupsPerLine));
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        mouseUpdateCursor(mouseX,mouseY);
    }

    public void setCursor(int cursor) {
        this.cursor = Math.max(0, Math.min(cursor, data.length - 1));
    }


    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        for (GuiTextField textField : textFields) {
            if (textField.mouseClicked(mouseX, mouseY, mouseButton)) {
                textField.setFocused(true);
                focusedField = textField;
                return;
            }
        }
        mouseUpdateCursor(mouseX,mouseY);
        selectionAnchor = cursor;
        focusedField = null;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        drawRect(0, 0, 256, 256, 0xffC6C6C6);
        textFields.forEach(field -> field.draw(mouseX, mouseY, partialTicks));
        FontRenderer fontRenderer = mc.fontRenderer;

        int x = 0;
        int y = 0;
        for (int i = scroll * groupSize * groupsPerLine; i < data.length; i++) {
            if (i % (groupSize * groupsPerLine) == 0) {
                y += 1;
                x = 0;
            }
            if (x != 0 && i % groupSize == 0) {
                x += spacing * charWidth;
            }

            if (selectionAnchor != SELECTION_NONE && selectionAnchor != cursor && ((i >= selectionAnchor && i <= cursor) || (i <= selectionAnchor && i >= cursor))) {
                drawRect(x, y * fontRenderer.FONT_HEIGHT, x + 2 * charWidth, y * fontRenderer.FONT_HEIGHT + fontRenderer.FONT_HEIGHT, 0xff000000 | EnumDyeColor.GREEN.getColorValue());
            }
            if (i == cursor) {
                int nx = x + (cursorNibble == Nibble.UPPER ? 0 : charWidth);
                drawRect(nx, (int) (y * fontRenderer.FONT_HEIGHT + fontRenderer.FONT_HEIGHT * 0.9f), nx + charWidth, y * fontRenderer.FONT_HEIGHT + fontRenderer.FONT_HEIGHT, 0xff000000 | EnumDyeColor.BLUE.getColorValue());
            }
            fontRenderer.drawString(String.format("%02X", data[i]), x, y * fontRenderer.FONT_HEIGHT, EnumDyeColor.WHITE.getColorValue());
            x += 2 * charWidth;
        }
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
    }
}
