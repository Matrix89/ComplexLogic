package me.matrix89.complexlogic.gui;

import net.minecraft.client.gui.GuiButton;
import pl.asie.charset.lib.inventory.GuiContainerCharset;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.regex.Pattern;

public class HexEditorGUI extends GuiContainerCharset<HexEditorContainer> {
    ArrayList<HexTextField> lines = new ArrayList<>();

    int cursorX;
    int cursorY;
    int selectionStartX;
    int selectionStartY;
    int selectionEndX;
    int selectionEndY;
    int spacing = 2;

    boolean cursorWhite = false;
    float lastCursorBlink = 0;
    Random rnd = new Random();
    int charWidth;
    int charHeight;

    public HexEditorGUI(HexEditorContainer container, int xSize, int ySize) {
        super(container, xSize, ySize);
        byte[] table = new byte[32];
        rnd.nextBytes(table);
        for (int i =0; i<table.length; i++){
            lines.add(new HexTextField(table[i]));
        }

    }

    @Override
    public void initGui() {
        super.initGui();
        charWidth = fontRenderer.getCharWidth('_');
        charHeight = fontRenderer.FONT_HEIGHT;
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {

        String character = String.valueOf(typedChar).toUpperCase();
        Pattern p = Pattern.compile("[a-fA-F0-9]");
        if (p.matcher(character).find()) {
            int index = (cursorX + cursorY * spacing*3)/2;
            int indexMod = (cursorX + cursorY * spacing) % 2;
            System.out.println(index + ":" + indexMod);
            HexTextField ht = lines.get(index);
            if(ht!=null){
                try {
                    System.out.println(Byte.parseByte(character, 16));
                    ht.setValue((byte)((ht.value & 0xF0) | Byte.parseByte(character, 16)));
                }catch (Exception e){

                }
            }
        }

        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
    }


    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        System.out.println(String.format("%d:%d", mouseX, mouseY));
        int offsetX = mouseX;
        int offsetY = mouseY;
        if (offsetX >= 0 && offsetY >= 0) {
            cursorX = (int) Math.min(spacing*3 - 2, (offsetX / charWidth));
            cursorY = (int) Math.min(lines.size()/spacing,(offsetY / charHeight));
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    class HexTextField {
        private byte value;

        public HexTextField(byte value) {
            this.value= value;
        }

        public void render(int x, int y) {
            fontRenderer.drawString(String.format("%02X", value), x, y, 0);
        }

        public void setValue(byte value) {
            this.value = value;
        }

        public String getLower() {
            return String.format("%02X", value & 0xF);
        }

        public String getUpper() {
            return String.format("%02X", (value >> 4) & 0xF);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        drawRect(0, 0, 256, 256, 0xffC6C6C6);
        int yStart = 0;
        int xStart = 0;
        for (int i = 0; i < lines.size(); i++) {
            if(i%spacing==0 && i!=0){
                yStart++;
                xStart=0;
            }

            lines.get(i).render(xStart* charWidth*3, yStart*charHeight);
            xStart++;
        }
        spacing = 3;
        drawRect( (cursorX * charWidth) - 1,
                (cursorY * charHeight) - 1,
                ((cursorX + 1) * charWidth) ,
                (cursorY + 1) * charHeight,
                spacing == 0 ? 0 : 0xffffffff);
//
//        for (int i = 0; i < lines.size(); i++) {
//            fontRenderer.drawString(lines.get(i), 10, i * fontRenderer.FONT_HEIGHT + 3, 0);
//        }
//        float marginX = 10;
//        float marginY = 10;
//
//        lastCursorBlink += partialTicks;
//        if (lastCursorBlink >= 10) {
//            if (spacing == 0) {
//                spacing = 1;
//            } else {
//                spacing = 0;
//            }
//            lastCursorBlink = 0;
//        }

        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
    }
}
