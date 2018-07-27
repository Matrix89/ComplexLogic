package me.matrix89.complexlogic.gate;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

public class SegmentDisplayLogic extends BundledGateLogic {

    public byte[] value = new byte[16];

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag, boolean isClient) {
        super.writeToNBT(tag, isClient);
        tag.setByteArray("v", getInputValueBundled(EnumFacing.SOUTH));
        return tag;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound, boolean isClient) {
        super.readFromNBT(compound, isClient);
        if (compound.hasKey("v"))
            value = compound.getByteArray("v");
    }

    @Override
    public Connection getType(EnumFacing dir) {
         return dir == EnumFacing.SOUTH ? Connection.INPUT_BUNDLED : Connection.NONE;
    }

    @Override
    public State getLayerState(int i) {
        return State.ON;
    }

    @Override
    public State getTorchState(int i) {
        return State.ON;
    }
}
