package me.matrix89.complexlogic;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import pl.asie.simplelogic.gates.PartGate;
import pl.asie.simplelogic.gates.logic.GateLogic;

import java.util.Arrays;

public class BundledViewerLogic extends GateLogic {
    byte[] values = new byte[16];

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag, boolean isClient) {
        super.writeToNBT(tag, isClient);
        tag.setByteArray("v", values);
        return tag;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound, boolean isClient) {
        super.readFromNBT(compound, isClient);
        if (compound.hasKey("v"))
            values = compound.getByteArray("v");
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
    public boolean tick(PartGate parent) {
        byte[] v = parent.getBundledInput(EnumFacing.SOUTH);
        if (!Arrays.equals(values, v)) {
            values = v;
            super.updateOutputs();
            return true;
        }
        return false;
    }

    @Override
    public State getTorchState(int i) {
        return values[i] != 0 ? State.ON : State.OFF;
    }

    @Override
    public byte[] getOutputValueBundled(EnumFacing side) {
        return values;
    }

    @Override
    protected byte calculateOutputInside(EnumFacing enumFacing) {
        return 0;
    }
}
