package me.matrix89.complexlogic.gate;

import net.minecraft.util.EnumFacing;
import pl.asie.simplelogic.gates.PartGate;
import pl.asie.simplelogic.gates.logic.GateConnection;
import pl.asie.simplelogic.gates.logic.GateRenderState;
import pl.asie.simplelogic.gates.logic.IGateContainer;

public class SubtractorLogic extends BundledGateLogic {

    @Override
    public GateConnection getType(EnumFacing dir) {
        switch (dir) {
            case NORTH:
                return GateConnection.OUTPUT_BUNDLED;
            case EAST:
            case WEST:
                return GateConnection.INPUT_BUNDLED;
            default:
                return GateConnection.NONE;
        }
    }

    @Override
    public boolean updateOutputs(IGateContainer gate) {
        byte[] inA = getInputValueBundled(EnumFacing.EAST);
        byte[] inB = getInputValueBundled(EnumFacing.WEST);
        int a = bundledRsToDigi(inA);
        int b = bundledRsToDigi(inB);

        int v = b - a;
        setBundledOutputValue(EnumFacing.NORTH, bundledDigiToRs(v));
        return true;
    }

    @Override
    public GateRenderState getTorchState(int i) {
        return GateRenderState.ON;
    }

    @Override
    public GateRenderState getLayerState(int i) {
        return getOutputValueInside(EnumFacing.SOUTH) != 0 ? GateRenderState.ON : GateRenderState.OFF;
    }
}
