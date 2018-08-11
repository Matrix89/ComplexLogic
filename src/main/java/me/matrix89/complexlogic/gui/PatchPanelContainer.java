package me.matrix89.complexlogic.gui;

import me.matrix89.complexlogic.gate.PatchPanelLogic;
import me.matrix89.complexlogic.network.PacketRegistry;
import me.matrix89.complexlogic.network.packets.PatchPanelPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.Sys;
import pl.asie.simplelogic.gates.PartGate;

import java.util.Arrays;

public class PatchPanelContainer extends Container {
    PatchPanelLogic logic;
    PartGate partGate;
    byte[][] oldCo0nnections = new byte[16][16];
    public byte[][] connectionGrid = new byte[16][16];
    public PatchPanelContainer(PatchPanelLogic logic, PartGate partGate){
        if(logic!=null) {
            for (int i = 0; i < 16; i++) {
                System.arraycopy(logic.getConnectionGrid()[i], 0, connectionGrid[i], 0, 16);
            }
        }
        this.logic = logic;
        this.partGate = partGate;
        System.out.println("new class: " + (logic==null?"CLIENT":"SERVER"));
    }

    @Override
    public void addListener(IContainerListener listener) {
        super.addListener(listener);
    }

    public void updateFromServer(byte[][] connectionGrid){
        System.out.println("UPDATE FROM SERVER");
        for (int i = 0; i < 16; i++) {
            System.arraycopy(connectionGrid[i], 0, this.connectionGrid[i], 0, 16);
        }
    }

    public void updateFromClient(byte[][] connectionGrid){
        if(logic==null || partGate == null) return;
        this.connectionGrid = connectionGrid;
        logic.setConnectionGrid(connectionGrid);
        logic.forceUpdate();
        partGate.scheduleTick();
        System.out.println("UPDATE FROM CLIENT");
//        for (IContainerListener p : listeners) {
//            if(p instanceof EntityPlayerMP)
//                PacketRegistry.INSTANCE.packetHandler.sendTo(new PatchPanelPacket(connectionGrid), (EntityPlayerMP) p);
//            System.out.println("UPDATE");
//        }
    }

    public void detectAndSendChanges(){
        if(logic==null) return;
        for (int i = 0; i < 16; i++) {
            if(!Arrays.equals(logic.getConnectionGrid()[i], oldCo0nnections[i])){
                for (int j = 0; j < 16; j++) {
                    System.arraycopy(logic.getConnectionGrid()[j], 0, oldCo0nnections[j], 0, 16);
                }
                for (IContainerListener p : listeners) {
                    if(p instanceof EntityPlayerMP)
                        PacketRegistry.INSTANCE.packetHandler.sendTo(new PatchPanelPacket(logic.getConnectionGrid()), (EntityPlayerMP) p);
                    System.out.println("UPDATE");
                }
                break;
            }
        }
    }

    @Override
    public void onCraftMatrixChanged(IInventory inventoryIn) {
        System.out.println("UPDATE2");
        super.onCraftMatrixChanged(inventoryIn);
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }
}
