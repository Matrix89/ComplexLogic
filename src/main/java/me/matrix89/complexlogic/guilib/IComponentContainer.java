package me.matrix89.complexlogic.guilib;

public interface IComponentContainer extends IComponent {
	void addChild(IComponent component);
	void removeChild(IComponent component);
	void removeAllChildren();
}
