package me.matrix89.complexlogic;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import pl.asie.simplelogic.gates.logic.GateLogic;

public class BundledViewerLogic extends BundledGateLogic {

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag, boolean isClient) {
        super.writeToNBT(tag, isClient);
        tag.setByteArray("v", getOutputValueBundled(EnumFacing.NORTH));
        return tag;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound, boolean isClient) {
        super.readFromNBT(compound, isClient);
        if (compound.hasKey("v"))
            bundledValues.replace(EnumFacing.NORTH, compound.getByteArray("v"));
    }

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

    public boolean renderEquals(GateLogic other) {
        // if(!(other instanceof BundledViewerLogic)) return false;
       // System.out.println(Arrays.toString(((BundledViewerLogic) other).values) + " " + Arrays.toString(values));
       // return Arrays.equals(((BundledViewerLogic) other).values, values);
        return false;
    }

    @Override
    public State getLayerState(int i) {
        return State.ON;
    }

    @Override
    public byte[] calculateBundledOutput(EnumFacing facing) {
        return facing == EnumFacing.NORTH ? getOutputValueBundled(EnumFacing.SOUTH) : new byte[16];
    }

    @Override
    public State getTorchState(int i) {
        return getOutputValueBundled(EnumFacing.NORTH)[i] != 0 ? State.ON : State.OFF;
    }
}
