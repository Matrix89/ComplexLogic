package me.matrix89.complexlogic.gate;

import net.minecraft.util.EnumFacing;
import pl.asie.simplelogic.gates.PartGate;
import pl.asie.simplelogic.gates.logic.GateConnection;
import pl.asie.simplelogic.gates.logic.GateRenderState;
import pl.asie.simplelogic.gates.logic.IGateContainer;

public class NAndLogic extends BundledGateLogic {
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

    public NAndLogic(){
        super();
        setBundledOutputValue(EnumFacing.NORTH, bundledDigiToRs(~(0)));
    }

    @Override
    public boolean updateOutputs(IGateContainer gate) {
        int a = bundledRsToDigi(getInputValueBundled(EnumFacing.WEST));
        int b = bundledRsToDigi(getInputValueBundled(EnumFacing.EAST));
        setBundledOutputValue(EnumFacing.NORTH, bundledDigiToRs(~(a & b)));
        return true;
    }

    @Override
    public GateRenderState getLayerState(int i) {
        return GateRenderState.ON;
    }

    @Override
    public GateRenderState getTorchState(int i) {
        return GateRenderState.ON;
    }
}
