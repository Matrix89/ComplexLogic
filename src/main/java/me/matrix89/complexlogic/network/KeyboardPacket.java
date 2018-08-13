package me.matrix89.complexlogic.network;


import me.matrix89.complexlogic.gui.KeyboardContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;
import org.lwjgl.input.Keyboard;
import pl.asie.charset.lib.network.Packet;

public class KeyboardPacket extends Packet {
    private int buttonId = 0;
    public KeyboardPacket() {}

    public KeyboardPacket(int buttonId) {
        this.buttonId = buttonId;
    }


    @Override
    public void readData(INetHandler iNetHandler, PacketBuffer packetBuffer) {
        buttonId = packetBuffer.readInt();
    }

    @Override
    public void apply(INetHandler iNetHandler) {
        EntityPlayer player = getPlayer(iNetHandler);
        if (player == null || !(player.openContainer instanceof KeyboardContainer)) return;;
        if(player.world.isRemote) {
            //((KeyboardContainer) player.openContainer).updateFromServer;
        } else {
            System.out.println("test");
            ((KeyboardContainer) player.openContainer).updateFromClient(buttonId);
        }
    }

    @Override
    public void writeData(PacketBuffer packetBuffer) {
        packetBuffer.writeInt(buttonId);
    }

    @Override
    public boolean isAsynchronous() {
        return false;
    }


}
