package me.matrix89.complexlogic.gate;

import net.minecraft.util.EnumFacing;
import pl.asie.simplelogic.gates.PartGate;
import pl.asie.simplelogic.gates.logic.GateConnection;
import pl.asie.simplelogic.gates.logic.GateRenderState;
import pl.asie.simplelogic.gates.logic.IGateContainer;

public class BitReordererLogic extends BundledGateLogic {

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
    public boolean updateOutputs(IGateContainer gate) {
        byte[] in = getInputValueBundled(EnumFacing.SOUTH);
        byte[] out = new byte[16];
        if (in != null) {
            for (int i = 0; i < in.length; i++) {
                out[15 - i] = in[i];
            }
        }
        setBundledOutputValue(EnumFacing.NORTH, out);
        return true;
    }

    @Override
    public GateRenderState getLayerState(int i) {
        return GateRenderState.OFF;
    }

    @Override
    public GateRenderState getTorchState(int i) {
        return GateRenderState.OFF;
    }
}
