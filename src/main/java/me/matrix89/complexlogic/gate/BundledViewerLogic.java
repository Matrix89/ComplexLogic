package me.matrix89.complexlogic.gate;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import pl.asie.simplelogic.gates.PartGate;
import pl.asie.simplelogic.gates.logic.GateConnection;
import pl.asie.simplelogic.gates.logic.GateRenderState;
import pl.asie.simplelogic.gates.logic.IGateContainer;

import java.util.Arrays;

public class BundledViewerLogic extends BundledGateLogic {

    @Override
    public GateConnection getType(EnumFacing dir) {
        switch (dir) {
            case NORTH:
                return GateConnection.OUTPUT_BUNDLED;
            case SOUTH:
                return GateConnection.INPUT_BUNDLED;
            default:
                return GateConnection.NONE;
        }
    }

    @Override
    protected boolean shouldSyncBundledWithClient() {
        return true;
    }

    @Override
    public GateRenderState getLayerState(int i) {
        return GateRenderState.ON;
    }

    @Override
    public boolean updateOutputs(IGateContainer gate) {
        setBundledOutputValue(EnumFacing.NORTH, getInputValueBundled(EnumFacing.SOUTH));
        return true;
    }

    @Override
    public GateRenderState getTorchState(int i) {
        return GateRenderState.OFF;
    }
}
