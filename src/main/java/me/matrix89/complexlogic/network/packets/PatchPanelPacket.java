package me.matrix89.complexlogic.network.packets;

import me.matrix89.complexlogic.gui.PatchPanelContainer;
import me.matrix89.complexlogic.network.Packet;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Container;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.io.IOException;

public class PatchPanelPacket extends Packet<PatchPanelPacket, IMessage> {
    byte[][] connectionGrid = new byte[16][16];

    public PatchPanelPacket() {
    }

    public PatchPanelPacket(byte[][] connectionGrid) {
        this.connectionGrid = connectionGrid;
    }

    @Override
    protected void read() throws IOException {
        for (int i = 0; i < 16; i++) {
            connectionGrid[i] = readByteArray();
        }
    }

    @Override
    protected void write() throws IOException {
        for (int i = 0; i < 16; i++) {
            writeByteArray(connectionGrid[i]);
        }
    }

    @Override
    protected IMessage executeOnClient() {
        Container container = Minecraft.getMinecraft().player.openContainer;
        if (container instanceof PatchPanelContainer) {
            ((PatchPanelContainer) container).updateFromServer(connectionGrid);
        }
        return null;
    }


    @Override
    protected IMessage executeOnServer(NetHandlerPlayServer server) {
        Container container = server.player.openContainer;
        if (container instanceof PatchPanelContainer) {
            ((PatchPanelContainer) container).updateFromClient(connectionGrid);
        }
        return null;
    }
}
