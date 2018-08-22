package me.matrix89.complexlogic.guilib;

import net.minecraft.client.gui.FontRenderer;

public interface IComponent {
	void mouseClicked(int mouseX, int mouseY, int mouseButton);
	
	void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick);
	
	void mouseReleased(int mouseX, int mouseY, int state);
	
	void keyTyped(char typedChar, int keyCode);
	
	void render(int mouseX, int mouseY, float partialTicks, FontRenderer fontRenderer);
	
	void gainFocus(int x, int y);
	
	void lostFocus();
	
	Rectangle getHitbox();
	
	void focusNext();
	void focusPrevious();
	boolean isFocused();
}
