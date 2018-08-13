package me.matrix89.complexlogic.gate;

import net.minecraft.util.EnumFacing;
import pl.asie.simplelogic.gates.PartGate;
import pl.asie.simplelogic.gates.logic.GateConnection;
import pl.asie.simplelogic.gates.logic.GateLogic;
import pl.asie.simplelogic.gates.logic.GateRenderState;
import pl.asie.simplelogic.gates.logic.IGateContainer;

public class MultiplicationLogic extends BundledGateLogic {
    @Override
    public GateConnection getType(EnumFacing dir) {
        switch (dir) {
            case NORTH:
                return GateConnection.OUTPUT_BUNDLED;
            case EAST:
            case WEST:
                return GateConnection.INPUT_BUNDLED;
            case SOUTH:
                return GateConnection.OUTPUT;
            default:
                return GateConnection.NONE;
        }
    }

    @Override
    public boolean updateOutputs(IGateContainer gate) {
        byte[] inA = getInputValueBundled(EnumFacing.WEST);
        byte[] inB = getInputValueBundled(EnumFacing.EAST);
        int a = bundledRsToDigi(inA);
        int b = bundledRsToDigi(inB);

        int v = a * b;
        setBundledOutputValue(EnumFacing.NORTH, bundledDigiToRs(v));
        setRedstoneOutputValue(EnumFacing.SOUTH, (byte) (v >>> 16 != 0 ? 15 : 0)); // carry
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
