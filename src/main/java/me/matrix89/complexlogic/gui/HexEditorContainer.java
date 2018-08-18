package me.matrix89.complexlogic.gui;

import net.minecraft.entity.player.InventoryPlayer;
import pl.asie.charset.lib.inventory.ContainerBase;

public class HexEditorContainer extends ContainerBase {
    public HexEditorContainer(InventoryPlayer inventoryPlayer) {
        super(inventoryPlayer);
    }

    @Override
    public boolean isOwnerPresent() {
        return true;
    }
}
