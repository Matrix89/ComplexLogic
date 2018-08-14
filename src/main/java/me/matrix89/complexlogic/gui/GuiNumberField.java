package me.matrix89.complexlogic.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;

import java.util.function.Consumer;
import java.util.regex.Pattern;

public class GuiNumberField extends GuiTextField {

    private GuiButton incBtn;
    private GuiButton decBtn;

    private Minecraft mc;

    private Consumer<Integer> onClick;

    public GuiNumberField(int componentId, FontRenderer fontrendererObj, int x, int y, int par5Width, int par6Height, Minecraft mc) {
        super(componentId, fontrendererObj, x, y, par5Width, par6Height);
        this.mc = mc;
        int btnHeight = this.height / 2;
        incBtn = new GuiButton(Integer.MAX_VALUE, x + par5Width, y - 1, 10, btnHeight + 1, "+");
        decBtn = new GuiButton(Integer.MAX_VALUE, x + par5Width, y + btnHeight, 10, btnHeight + 2, "-");
        setText(""+0);
        setValidator(input -> {
            try {
                Integer.parseInt(input);
                return true;
            } catch (NumberFormatException | NullPointerException e) {
                return false;
            }
        });
    }

    public void draw(int mouseX, int mouseY, float partialTics) {
        super.drawTextBox();
        incBtn.drawButton(mc, mouseX, mouseY, partialTics);
        decBtn.drawButton(mc, mouseX, mouseY, partialTics);
    }

    public void setOnClick(Consumer<Integer> onClick) {
        this.onClick = onClick;
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(incBtn.mousePressed(mc, mouseX, mouseY)) {
            int val = Integer.parseInt(getText()) + 1;
            setText(""+val);
            onClick.accept(val);
        }
        if(decBtn.mousePressed(mc, mouseX, mouseY)) {
            int val = Integer.parseInt(getText()) - 1;
            setText(""+val);
            onClick.accept(val);
        }
        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public final void setText(String textIn) {
        super.setText(textIn);
    }

    public void setValue(int i) {
        setText(""+i);
    }
}
