package me.matrix89.complexlogic.gate;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import pl.asie.simplelogic.gates.PartGate;

public class BundledViewerLogic extends BundledGateLogic {

    @Override
    public Connection getType(EnumFacing dir) {
        switch (dir) {
            case NORTH:
                return Connection.OUTPUT_BUNDLED;
            case SOUTH:
                return Connection.INPUT_BUNDLED;
            default:
                return Connection.NONE;
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag, boolean isClient) {
        super.writeToNBT(tag, isClient);
        if(isClient)
            tag.setByteArray("v", getOutputValueBundled(EnumFacing.NORTH));
        return tag;
    }

    @Override
    public boolean readFromNBT(NBTTagCompound compound, boolean isClient) {
        if (isClient && compound.hasKey("v"))
            setBundledOutputValue(EnumFacing.NORTH, compound.getByteArray("v"));
        return super.readFromNBT(compound, isClient);
    }



    @Override
    public State getLayerState(int i) {
        return State.ON;
    }

    @Override
    void calculateOutput(PartGate parent) {
        setBundledOutputValue(EnumFacing.NORTH, getInputValueBundled(EnumFacing.SOUTH));
    }

    @Override
    public State getTorchState(int i) {
        return getOutputValueBundled(EnumFacing.NORTH)[i] != 0 ? State.ON : State.OFF;
    }
}
