package me.matrix89.complexlogic.network;

import me.matrix89.complexlogic.ComplexLogic;
import me.matrix89.complexlogic.item.HexBook;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumHand;
import pl.asie.charset.lib.network.Packet;

public class HexEditorPacket extends Packet {
    private byte[] data;

    public HexEditorPacket() {
    }

    public HexEditorPacket(byte[] data) {
        this.data = data;
    }

    @Override
    public void readData(INetHandler iNetHandler, PacketBuffer packetBuffer) {
        data = packetBuffer.readByteArray();
    }

    @Override
    public void apply(INetHandler iNetHandler) {
        EntityPlayer player = getPlayer(iNetHandler);
        if (player != null) {
            if(player.getHeldItem(EnumHand.MAIN_HAND).isItemEqual(new ItemStack(HexBook.INSTANCE))) {
                player.getHeldItem(EnumHand.MAIN_HAND).setTagCompound(createBookTag(data));
            }
        }
    }

    public static NBTTagCompound createBookTag(byte[] data){
        byte[] toWrite = data;
        if(data.length%2!=0){
            toWrite = new byte[data.length+1];
            System.arraycopy(data, 0, toWrite, 0, data.length);
        }

        NBTTagCompound mapTag = new NBTTagCompound();
        for (int i = 0; i < toWrite.length; i+=2) {
            int value = ((int)toWrite[i+1]&0xff) | (((int)toWrite[i]&0xff)<<8);
            if(value!=0) {
                mapTag.setInteger(String.valueOf(i / 2), value);
            }
        }
        return mapTag;
    }

    @Override
    public void writeData(PacketBuffer packetBuffer) {
        packetBuffer.writeByteArray(data);
    }

    @Override
    public boolean isAsynchronous() {
        return false;
    }
}
