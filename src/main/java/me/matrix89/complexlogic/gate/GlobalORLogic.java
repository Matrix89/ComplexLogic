package me.matrix89.complexlogic.gate;

import net.minecraft.util.EnumFacing;
import pl.asie.simplelogic.gates.PartGate;
import pl.asie.simplelogic.gates.logic.GateConnection;
import pl.asie.simplelogic.gates.logic.GateRenderState;
import pl.asie.simplelogic.gates.logic.IGateContainer;

public class GlobalORLogic extends BundledGateLogic {

    @Override
    public GateConnection getType(EnumFacing dir) {
        switch (dir) {
            case SOUTH:
                return GateConnection.INPUT_BUNDLED;
            case NORTH:
                return GateConnection.OUTPUT;
            default:
                return GateConnection.NONE;
        }
    }


    @Override
    public void onChanged(IGateContainer gate) {
        byte[] in = getInputValueBundled(EnumFacing.SOUTH);
        byte out = 0;
        if (in != null) {
            for (byte i : in) {
                out = (byte) Math.max(out, i);
            }
        }
        setRedstoneOutputValue(EnumFacing.NORTH, out);
        gate.markGateChanged(true);
    }


    @Override
    public GateRenderState getLayerState(int i) {
        return i == 0 && getOutputValueInside(EnumFacing.NORTH) != 0 ? GateRenderState.ON : GateRenderState.OFF;
    }

    @Override
    public GateRenderState getTorchState(int i) {
        return GateRenderState.ON;
    }
}
