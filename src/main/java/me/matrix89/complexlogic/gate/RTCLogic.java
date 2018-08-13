package me.matrix89.complexlogic.gate;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import pl.asie.simplelogic.gates.PartGate;
import pl.asie.simplelogic.gates.logic.GateConnection;
import pl.asie.simplelogic.gates.logic.GateRenderState;
import pl.asie.simplelogic.gates.logic.IGateContainer;

public class RTCLogic extends BundledGateLogic {
    private boolean updateSignalOn = false;

    @Override
    public GateConnection getType(EnumFacing dir) {
        switch (dir) {
            case NORTH:
                return GateConnection.OUTPUT_BUNDLED;
            case SOUTH:
                return GateConnection.INPUT;
            default:
                return GateConnection.NONE;
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag, boolean isClient) {
        if (!isClient) {
            tag.setBoolean("updateSignalOn", updateSignalOn);
        }
        return super.writeToNBT(tag, isClient);
    }

    @Override
    public boolean readFromNBT(NBTTagCompound compound, boolean isClient) {
        boolean updateSignalOnOld = updateSignalOn;
        if (compound.hasKey("updateSignalOn")) {
            updateSignalOn = compound.getBoolean("updateSignalOn");
        }
        return super.readFromNBT(compound, isClient) || updateSignalOnOld != updateSignalOn;
    }


    @Override
    public boolean updateOutputs(IGateContainer gate) {
        if (getInputValueInside(EnumFacing.SOUTH) != 0) {
            if (!updateSignalOn) {
                updateSignalOn = true;
                setBundledOutputValue(EnumFacing.NORTH, bundledDigiToRs((int) gate.getGateWorld().getWorldTime()));
            }
        } else {
            updateSignalOn = false;
        }
        return true;
    }

    @Override
    public GateRenderState getLayerState(int i) {
        return getInputValueInside(EnumFacing.SOUTH) != 0 ? GateRenderState.ON : GateRenderState.OFF;
    }

    @Override
    public GateRenderState getTorchState(int i) {
        return GateRenderState.ON;
    }
}
