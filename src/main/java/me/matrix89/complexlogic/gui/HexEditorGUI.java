package me.matrix89.complexlogic.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
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
    private int groupsPerLine = 2;
    private int spacing = 2;
    private int charWidth = 3;

    private Random rnd = new Random();

    private byte[] data = new byte[32];

    public HexEditorGUI(HexEditorContainer container, int xSize, int ySize) {
        super(container, xSize, ySize);
        rnd.nextBytes(data);
    }

    @Override
    public void initGui() {
        super.initGui();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        System.out.println(keyCode);
        Pattern p = Pattern.compile("[a-fA-F0-9]");
        System.out.println(cursorNibble);
        if (p.matcher(String.valueOf(typedChar).toUpperCase()).find()) {
            switch (cursorNibble) {
                case UPPER:
                    data[cursor] &= (data[cursor] << 8 | 0x0f);
                    data[cursor] |= Byte.parseByte("" + typedChar, 16) << 4;

                    cursorNibble = Nibble.LOWER;
                    return;
                case LOWER:
                    data[cursor] &= 0xf0;
                    data[cursor] |= Byte.parseByte("" + typedChar, 16);
                    cursor++;
                    cursorNibble = Nibble.UPPER;
            }
            return;
        }
        switch (keyCode) {
            case 205:
                setCursor(++cursor);
                break;
            case 203:
                setCursor(--cursor);
                break;
            case 200://up
                setCursor(-(groupSize * groupsPerLine)); // TODO: broken
                break;
            case 208://down
                setCursor(+(groupSize * groupsPerLine));
                break;
        }
        cursorNibble = Nibble.UPPER;
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        setCursor(mouseX / ((charWidth * 2 * groupSize) + spacing * (mouseX / ((charWidth * 2 * groupSize)) / groupSize)) + mouseY / fontRenderer.FONT_HEIGHT * groupSize * groupsPerLine);// inaccurate
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }

    public void setCursor(int cursor) {
        this.cursor = Math.max(0, Math.min(cursor, data.length - 1));
    }


    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        drawRect(0, 0, 256, 256, 0xffC6C6C6);
        FontRenderer fontRenderer = mc.fontRenderer;


        int x = 0;
        int y = 0;
        for (int i = 0; i < data.length; i++) {
            if (i != 0 && i % (groupSize * groupsPerLine) == 0) {
                y += 1;
                x = 0;
            }
            if (x != 0 && i % groupSize == 0) {
                x += spacing;
            }

            if (i == cursor) {
                drawRect(x * charWidth, y * fontRenderer.FONT_HEIGHT, x * charWidth + 4 * charWidth, y * fontRenderer.FONT_HEIGHT + fontRenderer.FONT_HEIGHT, 0xff000000 | EnumDyeColor.RED.getColorValue());
                int nx = x + (cursorNibble == Nibble.UPPER ? 0 : 1);
                drawRect(nx * charWidth, (int) (y * fontRenderer.FONT_HEIGHT + fontRenderer.FONT_HEIGHT * 0.9f), nx * charWidth + 2 * charWidth, y * fontRenderer.FONT_HEIGHT + fontRenderer.FONT_HEIGHT, 0xff000000 | EnumDyeColor.BLUE.getColorValue());
            }
            fontRenderer.drawString(String.format("%02x", data[i]), x * charWidth, y * fontRenderer.FONT_HEIGHT, EnumDyeColor.WHITE.getColorValue());
            x += 4;
        }
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
    }
}
