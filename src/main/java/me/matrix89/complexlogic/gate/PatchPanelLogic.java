package me.matrix89.complexlogic.gate;

import me.matrix89.complexlogic.ComplexLogic;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import pl.asie.simplelogic.gates.PartGate;

public class PatchPanelLogic extends BundledGateLogic {
    byte[][] connectionGrid = new byte[16][16];

    public void setConnectionGrid(byte[][] connectionGrid){
        this.connectionGrid = connectionGrid;
    }

    public byte[][] getConnectionGrid(){
        return connectionGrid;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag, boolean isClient) {
        if(!isClient){
            NBTTagCompound array = new NBTTagCompound();
            for (int i = 0; i < 16; i++) {
                array.setByteArray(String.valueOf(i), connectionGrid[i]);
            }
            tag.setTag("connectionGrid", array);
        }
        return super.writeToNBT(tag, isClient);
    }

    @Override
    public boolean readFromNBT(NBTTagCompound compound, boolean isClient) {
        if(compound.hasKey("connectionGrid")){
            NBTTagCompound array = compound.getCompoundTag("connectionGrid");
            for (int i = 0; i < 16; i++) {
                connectionGrid[i] = array.getByteArray(String.valueOf(i));
            }
        }

        return super.readFromNBT(compound, isClient);
    }

    @Override
    public Connection getType(EnumFacing dir) {
        switch (dir){
            case SOUTH: return Connection.INPUT_BUNDLED;
            case NORTH: return Connection.OUTPUT_BUNDLED;
            default: return Connection.NONE;
        }
    }

    @Override
    public boolean onRightClick(PartGate gate, EntityPlayer player, Vec3d vec, EnumHand hand) {
        if (!player.isSneaking() && EnumHand.MAIN_HAND==hand) {
            player.openGui(ComplexLogic.INSTANCE, 0, gate.getWorld(), gate.getPos().getX(), gate.getPos().getY(), gate.getPos().getZ());
            return true;
        }
        return false;
    }

    @Override
    void calculateOutput(PartGate parent) {
        byte[] in = getInputValueBundled(EnumFacing.SOUTH);
        byte[] out = new byte[16];
        for (int i = 0; i < 16; i++) {
            byte value = in[i];
            for (int j = 0; j < 16; j++) {
                if(connectionGrid[i][j]!=0){
                    out[j] = (byte)(out[j] | value);
                }
            }
        }
        setBundledOutputValue(EnumFacing.NORTH, out);
    }

    @Override
    public State getLayerState(int i) {
        return State.OFF;
    }

    @Override
    public State getTorchState(int i) {
        return State.OFF;
    }
}
