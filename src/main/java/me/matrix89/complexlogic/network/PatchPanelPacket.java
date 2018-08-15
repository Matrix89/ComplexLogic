package me.matrix89.complexlogic.network;

import me.matrix89.complexlogic.gate.PatchPanelLogic;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;
import pl.asie.charset.lib.network.Packet;
import pl.asie.simplelogic.gates.PacketGate;
import pl.asie.simplelogic.gates.PartGate;
import pl.asie.simplelogic.gates.gui.ContainerGate;
import pl.asie.simplelogic.gates.logic.IGateContainer;

public class PatchPanelPacket extends PacketGate {
    byte[][] connectionGrid = new byte[16][16];

    public PatchPanelPacket() {

    }


    public PatchPanelPacket(byte[][] connectionGrid, IGateContainer partGate) {
        super((PartGate) partGate);
        this.connectionGrid = connectionGrid;
    }

    @Override
    public void readData(INetHandler iNetHandler, PacketBuffer packetBuffer) {
        super.readData(iNetHandler, packetBuffer);
        for (int i = 0; i < 16; i++) {
            connectionGrid[i] = packetBuffer.readByteArray();
        }

    }

    @Override
    public void writeData(PacketBuffer packetBuffer) {
        super.writeData(packetBuffer);
        for (int i = 0; i < 16; i++) {
            packetBuffer.writeByteArray(connectionGrid[i]);
        }
    }

    @Override
    public void applyGate(PartGate partGate, EntityPlayer entityPlayer) {
        if (!(partGate.logic instanceof PatchPanelLogic)) return;
        PatchPanelLogic logic = (PatchPanelLogic) partGate.logic;
        logic.setGateConnectionGrid(connectionGrid);
        if(!partGate.getGateWorld().isRemote) {
            partGate.logic.onChanged(partGate);
        }
    }

    @Override
    public boolean isAsynchronous() {
        return false;
    }
}
