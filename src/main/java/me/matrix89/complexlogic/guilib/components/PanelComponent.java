package me.matrix89.complexlogic.guilib.components;

import java.util.LinkedList;
import java.util.Optional;
import java.util.function.Function;

import net.minecraft.client.gui.FontRenderer;

import me.matrix89.complexlogic.guilib.IComponent;
import me.matrix89.complexlogic.guilib.IComponentContainer;
import me.matrix89.complexlogic.guilib.Rectangle;

public class PanelComponent implements IComponentContainer {
	LinkedList<IComponent> components = new LinkedList<>();
	IComponent focused = null;
	boolean isFocused = false;
	Rectangle size;
	
	public PanelComponent(Rectangle size) {
		this.size = size;
	}
	
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		Optional<IComponent> clicked = components.stream().filter(c -> c.getHitbox().checkCollision(mouseX, mouseY)).findFirst();
		if (clicked.isPresent() && !clicked.get().equals(focused)) {
			if (focused != null) {
				focused.lostFocus();
			}
			focused = clicked.get();
			focused.gainFocus(mouseX, mouseY);
		} else if (!clicked.isPresent()) {
			if (focused != null) {
				focused.lostFocus();
			}
			focused = null;
		}
		
		
		if (focused != null) {
			focused.mouseClicked(mouseX, mouseY, mouseButton);
		}
	}
	
	@Override
	public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
		if (focused != null) {
			focused.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
		}
	}
	
	@Override
	public void mouseReleased(int mouseX, int mouseY, int state) {
		if (focused != null) {
			focused.mouseReleased(mouseX, mouseY, state);
		}
	}
	
	@Override
	public void keyTyped(char typedChar, int keyCode) {
		System.out.println(keyCode);
		switch (keyCode) {
			case 15:
				focusNext();
				break;
		}
		if (focused != null) {
			focused.keyTyped(typedChar, keyCode);
		}
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks, FontRenderer fontRenderer) {
		components.forEach(c -> c.render(mouseX, mouseY, partialTicks, fontRenderer));
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
		focusFunction(i -> ++i);
	}
	
	private void focusFunction(Function<Integer, Integer> indexFunction) {
		if (focused != null) {
			int idx = components.indexOf(focused);
			if (idx < 0) {
				return;
			}
			idx = indexFunction.apply(idx);
			if (idx < 0) {
				idx = components.size() - 1;
			} else if (idx >= components.size()) {
				idx = 0;
			}
			focused.lostFocus();
			focused = null;
			try {
				focused = components.get(idx);
				focused.gainFocus(0, 0); //TODO offset
			}catch (IndexOutOfBoundsException e){}
		}
	}
	
	@Override
	public void focusPrevious() {
		focusFunction(i -> --i);
	}
	
	@Override
	public boolean isFocused() {
		return isFocused;
	}
	
	@Override
	public void addChild(IComponent component) {
		if (!components.contains(component)) {
			components.add(component);
		}
	}
	
	@Override
	public void removeChild(IComponent component) {
		components.remove(component);
	}
	
	@Override
	public void removeAllChildren() {
		components.clear();
	}
}
