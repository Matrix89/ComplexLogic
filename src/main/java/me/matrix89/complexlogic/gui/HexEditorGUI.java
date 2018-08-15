package me.matrix89.complexlogic.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.item.EnumDyeColor;
import org.lwjgl.Sys;
import pl.asie.charset.lib.inventory.GuiContainerCharset;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HexEditorGUI extends GuiContainerCharset<HexEditorContainer> {

    private ArrayList<GuiNumberField> textFields = new ArrayList<>();
    private GuiTextField focusedField = null;

    private HexEditorComponent editor;

    public HexEditorGUI(HexEditorContainer container, int xSize, int ySize) {
        super(container, xSize, ySize);
    }

    private int w;
    private int h;
    private int y;
    private int x;

    @Override
    public void initGui() {
        w = this.width / 2;
        h = (int) (this.height * .98);
        y = (this.height - h) / 2;
        x = (this.width - w) / 2;
        /*
        editor = new HexEditorComponent(fontRenderer, x + 10, y + 10);
        GuiNumberField gplField = new GuiNumberField(3, fontRenderer, 200, 30, 30, fontRenderer.FONT_HEIGHT, mc);
        gplField.setValue(editor.groupsPerLine);
        gplField.setOnClick(integer -> {
            if (integer <= 0) {
                gplField.setValue(1);
                editor.groupsPerLine = 1;
            } else {
                editor.groupsPerLine = integer;
            }
        });
        textFields.add(gplField);

        GuiNumberField grSzField = new GuiNumberField(3, fontRenderer, 200, 40, 30, fontRenderer.FONT_HEIGHT, mc);
        grSzField.setValue(editor.groupSize);
        grSzField.setOnClick(integer -> {
            if (integer <= 0) {
                grSzField.setValue(1);
                editor.groupSize = 1;
            } else {
                editor.groupSize = integer;
            }
        });
        textFields.add(grSzField);
*/

        buttonList.add(new GuiButton(1, x + w - 75, y + 5, 70, 20, "delete byte"));
        buttonList.add(new GuiButton(2, x + w - 150, y + 5, 70, 20, "insert byte"));

        //editor.setPos(x, y);
        editor = new HexEditorComponent(fontRenderer, x + 10, y + 30, w - 20, h - (5 + 30));
        super.initGui();
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        editor.handleMouseInput();
    }

    @Override
    public void onResize(Minecraft mcIn, int w, int h) {
        super.onResize(mcIn, w, h);
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
        editor.keyTyped(typedChar, keyCode);
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        editor.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        for (GuiButton button : buttonList) {
            if (button.mousePressed(mc, mouseX, mouseY)) {
                return;
            }
        }
        editor.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
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
        super.mouseClicked(mouseX, mouseY, mouseButton);
        for (GuiButton button : buttonList) {
            if (button.mousePressed(mc, mouseX, mouseY)) {
                return;
            }
        }

        editor.mouseClicked(mouseX, mouseY, mouseButton);
        focusedField = null;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        drawRect(x, y, x + w, y + h, 0xffC6C6C6);
        textFields.forEach(field -> field.draw(mouseX, mouseY, partialTicks));

        editor.draw();
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 1:
                editor.insertByte();
                break;
            case 2:
                editor.deleteByte();
                break;
        }
    }
}
