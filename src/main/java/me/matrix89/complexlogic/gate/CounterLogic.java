package me.matrix89.complexlogic.gate;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import pl.asie.simplelogic.gates.PartGate;
import pl.asie.simplelogic.gates.logic.GateConnection;
import pl.asie.simplelogic.gates.logic.GateRenderState;
import pl.asie.simplelogic.gates.logic.IGateContainer;

public class CounterLogic extends BundledGateLogic {
    private boolean incSignalOn = false;
    private boolean updateSignalOn = false;

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag, boolean isClient) {
        if (!isClient) {
            tag.setBoolean("incSignalOn", incSignalOn);
            tag.setBoolean("updateSignalOn", updateSignalOn);
        }
        return super.writeToNBT(tag, isClient);
    }

    @Override
    public boolean readFromNBT(NBTTagCompound compound, boolean isClient) {
        boolean updateSignalOnOld = updateSignalOn;
        boolean incSignalOnOld = incSignalOn;
        if (compound.hasKey("updateSignalOn")) {
            updateSignalOn = compound.getBoolean("updateSignalOn");
        }
        if (compound.hasKey("incSignalOn")) {
            incSignalOn = compound.getBoolean("incSignalOn");
        }
        return super.readFromNBT(compound, isClient) || updateSignalOnOld != updateSignalOn || incSignalOnOld != incSignalOn;
    }

    @Override
    public GateConnection getType(EnumFacing dir) {
        switch (dir) {
            case NORTH:
                return GateConnection.OUTPUT_BUNDLED;
            case SOUTH:
                return GateConnection.INPUT_BUNDLED;
            case EAST:
            case WEST:
                return GateConnection.INPUT;
            default:
                return GateConnection.NONE;
        }
    }

    private int counter() {
        return isSideInverted(EnumFacing.EAST) ? -1 : 1;
    }


    @Override
    public boolean updateOutputs(IGateContainer gate) {
        boolean updateSignalOnOld = updateSignalOn;
        boolean incSignalOnOld = incSignalOn;
        if (getInputValueInside(EnumFacing.WEST) != 0) {
            if (!updateSignalOn) {
                updateSignalOn = true;
                setBundledOutputValue(EnumFacing.NORTH, getInputValueBundled(EnumFacing.SOUTH));
            }
        } else {
            updateSignalOn = false;
        }
        if (getInputValueOutside(EnumFacing.EAST) != 0) {
            if (!incSignalOn) {
                incSignalOn = true;
                setBundledOutputValue(EnumFacing.NORTH, bundledDigiToRs(bundledRsToDigi(getOutputValueBundled(EnumFacing.NORTH)) + counter()));
            }
        } else {
            incSignalOn = false;
        }
        return true;
    }

    @Override
    public GateRenderState getLayerState(int i) {
        switch (i) {
            case 0:
                return getInputValueInside(EnumFacing.EAST) != 0 ? GateRenderState.ON : GateRenderState.OFF;
            case 1:
                return getInputValueInside(EnumFacing.WEST) != 0 ? GateRenderState.ON : GateRenderState.OFF;
            default:
                return GateRenderState.ON;
        }
    }

    @Override
    public boolean canInvertSide(EnumFacing side) {
        return side == EnumFacing.EAST;
    }

    @Override
    public GateRenderState getTorchState(int i) {
        switch (i) {
            case 1:
                return getInputValueInside(EnumFacing.EAST) != 0 ? GateRenderState.ON : GateRenderState.OFF;
            case 0:
            default:
                return GateRenderState.ON;
        }
    }
}
