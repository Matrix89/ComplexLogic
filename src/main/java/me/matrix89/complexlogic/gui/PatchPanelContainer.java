package me.matrix89.complexlogic.gui;

import me.matrix89.complexlogic.ComplexLogic;
import me.matrix89.complexlogic.gate.PatchPanelLogic;
import me.matrix89.complexlogic.network.PatchPanelPacket;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import pl.asie.charset.lib.inventory.ContainerBase;
import pl.asie.simplelogic.gates.PartGate;

import java.util.Arrays;

public class PatchPanelContainer extends ContainerBase {
    PatchPanelLogic logic;
    PartGate partGate;
    byte[][] oldCo0nnections = new byte[16][16];
    public byte[][] connectionGrid = new byte[16][16];

    public PatchPanelContainer(InventoryPlayer player, PatchPanelLogic logic, PartGate partGate){
        super(player);
        if (logic != null) {
            for (int i = 0; i < 16; i++) {
                System.arraycopy(logic.getConnectionGrid()[i], 0, connectionGrid[i], 0, 16);
            }
        }
        this.logic = logic;
        this.partGate = partGate;
    }

    public void updateFromServer(byte[][] connectionGrid) {
        for (int i = 0; i < 16; i++) {
            System.arraycopy(connectionGrid[i], 0, this.connectionGrid[i], 0, 16);
        }
    }

    public void updateFromClient(byte[][] connectionGrid) {
        if (logic == null || partGate == null) return;
        this.connectionGrid = connectionGrid;
        logic.setConnectionGrid(connectionGrid);
        logic.forceUpdate();
        partGate.scheduleTick();
    }

    public void detectAndSendChanges() {
        if (logic == null) return;
        for (int i = 0; i < 16; i++) {
            if (!Arrays.equals(logic.getConnectionGrid()[i], oldCo0nnections[i])) {
                for (int j = 0; j < 16; j++) {
                    System.arraycopy(logic.getConnectionGrid()[j], 0, oldCo0nnections[j], 0, 16);
                }
                for (IContainerListener p : listeners) {
                    if(p instanceof EntityPlayerMP)
                        ComplexLogic.registry.sendTo(new PatchPanelPacket(logic.getConnectionGrid()), (EntityPlayerMP) p);
                }
                break;
            }
        }
    }


    @Override
    public boolean isOwnerPresent() {
        return true;
    }
}
