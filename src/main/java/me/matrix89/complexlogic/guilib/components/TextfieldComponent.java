package me.matrix89.complexlogic.guilib.components;

import me.matrix89.complexlogic.guilib.DrawingUtils;
import me.matrix89.complexlogic.guilib.IComponent;
import me.matrix89.complexlogic.guilib.Rectangle;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ChatAllowedCharacters;

public class TextfieldComponent implements IComponent {
    boolean isFocused;
    StringBuilder content = new StringBuilder();
    int caretePos = 0;
    int maxChars;
    Rectangle size;
    float cursorTime;
    int selectionStart=0;
    boolean selection=false;
    int charWidth;

    public TextfieldComponent(int x, int y, int chars, FontRenderer fontRenderer) {
        maxChars = chars;
        charWidth = fontRenderer.getCharWidth('_');
        size = new Rectangle(x, y, charWidth * chars, fontRenderer.FONT_HEIGHT);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        calculatePos(mouseX);
        if(selection)
            selection=false;
    }

    private void calculatePos(int x){
        int newPos = (int) Math.round ((x - size.getXStart()) / (double)charWidth);
        if(newPos>content.length()){
            newPos = content.length();
        }
        caretePos = newPos;
    }

    @Override
    public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        if(!selection) {
            selection = true;
            selectionStart = caretePos;
        }else{
            calculatePos(mouseX);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        calculatePos(mouseX);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        if (ChatAllowedCharacters.isAllowedCharacter(typedChar) && caretePos < maxChars) {
            content.insert(caretePos, typedChar);
            caretePos++;
            selection = false;
        }
        if(keyCode==14){
            int removeIdx = caretePos-1;
            if(removeIdx>=0 && removeIdx<content.length()) {
                if(selection){
                    content.delete(Math.min(selectionStart, removeIdx+1), Math.max(selectionStart, removeIdx+1));
                }else {
                    content.deleteCharAt(removeIdx);
                }
                caretePos = Math.min(selectionStart, removeIdx+1);
                selection = false;
            }
        }

        if(GuiScreen.isShiftKeyDown() && keyCode == 203){
            if(!selection) {
                selection = true;
                selectionStart = caretePos;
            }
            caretePos = Math.max(--caretePos, 0);
        }else
        if(GuiScreen.isShiftKeyDown() && keyCode == 205){
            if(!selection) {
                selection = true;
                selectionStart = caretePos;
            }
            caretePos = Math.min(++caretePos, content.length());
        }else {

            selection = false;
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks, FontRenderer fontRenderer) {
        DrawingUtils.drawRect(size, 0xff000000);
        if (isFocused()) {
            if(selection)
            DrawingUtils.drawRect(
                    size.getXStart() + charWidth*selectionStart,
                    size.getYStart(),
                    size.getXStart() + (charWidth)*caretePos,
                    size.getYStart() + fontRenderer.FONT_HEIGHT,
                    0xff00ffff
                    );

            cursorTime += partialTicks;
            if (cursorTime <= 10) {
                DrawingUtils.drawRect(new Rectangle(size.getXStart() + caretePos * fontRenderer.getCharWidth('_'), size.getYStart(), 1, fontRenderer.FONT_HEIGHT), 0xffffffff);
            }

            if (cursorTime > 20) {
                cursorTime = 0;
            }
        }

        fontRenderer.drawString(content.toString(), size.getXStart(), size.getYStart(), 0xffffffff);
    }

    @Override
    public void gainFocus(int x, int y) {
        isFocused = true;
    }

    @Override
    public void lostFocus() {
        isFocused = false;
        selectionStart = 0;
        cursorTime=0;
    }

    @Override
    public Rectangle getHitbox() {
        return size;
    }

    @Override
    public void focusNext() {

    }

    @Override
    public void focusPrevious() {

    }

    @Override
    public boolean isFocused() {
        return isFocused;
    }
}
