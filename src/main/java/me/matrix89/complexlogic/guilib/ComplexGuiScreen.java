package me.matrix89.complexlogic.guilib;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

import me.matrix89.complexlogic.guilib.components.ButtonComponent;
import me.matrix89.complexlogic.guilib.components.PanelComponent;

public class ComplexGuiScreen extends GuiScreen {
	IComponentContainer rootComponent = null;
	
	public ComplexGuiScreen() {
		width = 200;
		height = 200;
	}
	
	@Override
	public void initGui() {
		super.initGui();
		rootComponent = new PanelComponent(new Rectangle(0, 0, width, height));
		rootComponent.addChild(new ButtonComponent("Witaj", 10, 10, fontRenderer));
		rootComponent.addChild(new ButtonComponent("Witaj", 50, 10, fontRenderer));
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks){
		drawDefaultBackground();
		if (rootComponent != null) {
			rootComponent.render(mouseX, mouseY, partialTicks, fontRenderer);
		}
	}
	
	@Override
	    public void setWorldAndResolution(Minecraft mc, int width, int height) {
			this.mc = mc;
			this.itemRender = mc.getRenderItem();
			this.fontRenderer = mc.fontRenderer;
			this.width = width;
			this.height = height;
			this.initGui();
		}
	
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		if (rootComponent != null && rootComponent.getHitbox().checkCollision(mouseX, mouseY)) {
			if (!rootComponent.isFocused()) {
				rootComponent.gainFocus(mouseX, mouseY);
			}
			rootComponent.mouseClicked(mouseX, mouseY, mouseButton);
		}
	}
	
	@Override
	protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
		if (rootComponent != null && rootComponent.isFocused()) {
			rootComponent.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
		}
	}
	
	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		if (rootComponent != null && rootComponent.isFocused()) {
			rootComponent.mouseReleased(mouseX, mouseY, state);
		}
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		super.keyTyped(typedChar, keyCode);
		if (rootComponent != null && rootComponent.isFocused()) {
			rootComponent.keyTyped(typedChar, keyCode);
		}
	}
}
