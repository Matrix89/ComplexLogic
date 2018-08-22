package me.matrix89.complexlogic.guilib.components;

import net.minecraft.client.gui.FontRenderer;

import me.matrix89.complexlogic.guilib.DrawingUtils;
import me.matrix89.complexlogic.guilib.IComponent;
import me.matrix89.complexlogic.guilib.Rectangle;

public class ButtonComponent implements IComponent {
	String message;
	Rectangle size;
	boolean pressed = false;
	boolean isFocused = false;
	
	public ButtonComponent(String message, int x, int y, FontRenderer renderer) {
		this.message = message;
		size = new Rectangle(x, y, renderer.getStringWidth(message), renderer.FONT_HEIGHT);
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {

	}
	
	@Override
	public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
	
	}
	
	@Override
	public void mouseReleased(int mouseX, int mouseY, int state) {
	
	}
	
	@Override
	public void keyTyped(char typedChar, int keyCode) {
	
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks, FontRenderer fontRenderer) {
		if(isFocused()) {
			DrawingUtils.drawRect(size, 0xFF00FF00);
		}else{
			DrawingUtils.drawRect(size, 0xFF0000FF);
		}
		if(size.checkCollision(mouseX, mouseY))
			DrawingUtils.drawRect(size, 0x77ffffff);
		fontRenderer.drawString(message, size.getXStart(), size.getYStart(), 0xffffffff);
	}
	
	@Override
	public void gainFocus(int x, int y) {
		isFocused = true;
	}
	
	@Override
	public void lostFocus() {
		isFocused = false;
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
