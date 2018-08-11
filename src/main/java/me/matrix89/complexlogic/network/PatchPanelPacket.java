package me.matrix89.complexlogic.network;

import me.matrix89.complexlogic.gui.PatchPanelContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import pl.asie.charset.lib.network.Packet;

import java.io.IOException;

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
            if (player.openContainer instanceof PatchPanelContainer) {
                if (player.world.isRemote) {
                    ((PatchPanelContainer) player.openContainer).updateFromServer(connectionGrid);
                } else {
                    ((PatchPanelContainer) player.openContainer).updateFromClient(connectionGrid);
                }
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
