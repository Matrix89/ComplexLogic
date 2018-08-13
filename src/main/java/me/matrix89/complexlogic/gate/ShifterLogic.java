package me.matrix89.complexlogic.gate;

import net.minecraft.util.EnumFacing;
import pl.asie.simplelogic.gates.PartGate;
import pl.asie.simplelogic.gates.logic.GateConnection;
import pl.asie.simplelogic.gates.logic.GateRenderState;
import pl.asie.simplelogic.gates.logic.IGateContainer;

public class ShifterLogic extends BundledGateLogic {


    @Override
    public GateConnection getType(EnumFacing dir) {
        switch (dir) {
            case NORTH:
                return GateConnection.OUTPUT_BUNDLED;
            case SOUTH:
                return GateConnection.INPUT_BUNDLED;
            case EAST:
                return GateConnection.INPUT;
            default:
                return GateConnection.NONE;
        }
    }

    @Override
    public boolean updateOutputs(IGateContainer gate) {
        byte[] input = getInputValueBundled(EnumFacing.SOUTH);
        byte[] shifted = new byte[16];
        if (input != null) {
            if (getInputValueInside(EnumFacing.EAST) == 0) {
                System.arraycopy(input, 0, shifted, 1, 15);
            } else {
                System.arraycopy(input, 1, shifted, 0, 15);
            }
        }
        setBundledOutputValue(EnumFacing.NORTH, shifted);
        return true;
    }

    @Override
    public GateRenderState getLayerState(int i) {
        return getInputValueInside(EnumFacing.EAST) != 0 ? GateRenderState.ON : GateRenderState.OFF;
    }


    @Override
    public GateRenderState getTorchState(int i) {
        switch (i) {
            case 0:
                return GateRenderState.ON;
            case 1:
                return getInputValueInside(EnumFacing.EAST) != 0 ? GateRenderState.ON : GateRenderState.OFF;
            default:
                return GateRenderState.OFF;
        }
    }
}
