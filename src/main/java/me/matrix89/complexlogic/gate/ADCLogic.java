package me.matrix89.complexlogic.gate;

import net.minecraft.util.EnumFacing;
import pl.asie.simplelogic.gates.logic.GateConnection;
import pl.asie.simplelogic.gates.logic.GateRenderState;
import pl.asie.simplelogic.gates.logic.IGateContainer;

public class ADCLogic extends BundledGateLogic {

    @Override
    public GateConnection getType(EnumFacing dir) {
        switch (dir) {
            case NORTH:
                return GateConnection.INPUT_ANALOG;
            case SOUTH:
                return GateConnection.OUTPUT_BUNDLED;
            default:
                return GateConnection.NONE;
        }
    }

    @Override
    public boolean updateOutputs(IGateContainer gate) {
        byte inV = getInputValueInside(EnumFacing.NORTH);
        setBundledOutputValue(EnumFacing.SOUTH, bundledDigiToRs(inV));
        return true;
    }

    @Override
    public GateRenderState getLayerState(int i) {
        return getInputValueInside(EnumFacing.NORTH) != 0 ? GateRenderState.ON : GateRenderState.OFF;
    }

    @Override
    public GateRenderState getTorchState(int i) {
        return GateRenderState.ON;
    }
}
