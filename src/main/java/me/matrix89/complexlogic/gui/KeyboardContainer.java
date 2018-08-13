package me.matrix89.complexlogic.gui;

import me.matrix89.complexlogic.gate.KeyboardLogic;
import me.matrix89.complexlogic.utils.HIDUtil;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.EnumFacing;
import pl.asie.charset.lib.inventory.ContainerBase;
import pl.asie.simplelogic.gates.PartGate;

public class KeyboardContainer extends ContainerBase {
    private KeyboardLogic logic;
    private PartGate gate;

    public KeyboardContainer(InventoryPlayer inventoryPlayer, KeyboardLogic logic, PartGate partGate) {
        super(inventoryPlayer);
        this.logic = logic;
        this.gate = partGate;
    }

    @Override
    public boolean isOwnerPresent() {
        return true;
    }

    public void updateFromClient(int buttonId) {
        if (HIDUtil.keys.get(buttonId) == null) return;
        logic.setBundledOutputValue(EnumFacing.NORTH, logic.bundledDigiToRs(HIDUtil.keys.get(buttonId).scanCode));
        logic.forceUpdate();
        gate.scheduleTick();
    }
}
