package me.matrix89.complexlogic.network;

import me.matrix89.complexlogic.gate.PatchPanelLogic;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;
import pl.asie.charset.lib.network.Packet;
import pl.asie.simplelogic.gates.gui.ContainerGate;

public class PatchPanelPacket extends Packet {
    byte[][] connectionGrid = new byte[16][16];

    public PatchPanelPacket() {

    }

    public PatchPanelPacket(byte[][] connectionGrid) {
        this.connectionGrid = connectionGrid;
    }

    @Override
    public void readData(INetHandler iNetHandler, PacketBuffer packetBuffer) {
        for (int i = 0; i < 16; i++) {
            connectionGrid[i] = packetBuffer.readByteArray();
        }

    }

    @Override
    public void apply(INetHandler iNetHandler) {
        EntityPlayer player = getPlayer(iNetHandler);
        if (player != null) {
            if (player.openContainer instanceof ContainerGate) {
                //if (player.world.isRemote) {
                ((PatchPanelLogic) ((ContainerGate) player.openContainer).getGate().getLogic()).setGateConnectionGrid(connectionGrid);
                //} else {
                //    ((ContainerGate) player.openContainer).updateFromClient(connectionGrid);
                // }
            }
        }
    }

    @Override
    public void writeData(PacketBuffer packetBuffer) {
        for (int i = 0; i < 16; i++) {
            packetBuffer.writeByteArray(connectionGrid[i]);
        }
    }

    @Override
    public boolean isAsynchronous() {
        return false;
    }
}
