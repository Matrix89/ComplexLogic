package me.matrix89.complexlogic.gate;

import net.minecraft.util.EnumFacing;
import pl.asie.simplelogic.gates.logic.GateConnection;
import pl.asie.simplelogic.gates.logic.GateRenderState;
import pl.asie.simplelogic.gates.logic.IGateContainer;

public class DACLogic extends BundledGateLogic {

    @Override
    public GateConnection getType(EnumFacing dir) {
        switch (dir) {
            case NORTH:
                return GateConnection.OUTPUT_ANALOG;
            case SOUTH:
                return GateConnection.INPUT_BUNDLED;
            default:
                return GateConnection.NONE;
        }
    }

    @Override
    public boolean updateOutputs(IGateContainer gate) {
        byte[] inV = getInputValueBundled(EnumFacing.SOUTH);
        setRedstoneOutputValue(EnumFacing.NORTH, (byte) Math.min(15, Math.max(bundledRsToDigi(inV), 0)));
        return true;
    }

    @Override
    public GateRenderState getLayerState(int i) {
        return getOutputValueInside(EnumFacing.NORTH) != 0 ? GateRenderState.ON : GateRenderState.OFF;
    }

    @Override
    public GateRenderState getTorchState(int i) {
        return GateRenderState.ON;
    }
}
