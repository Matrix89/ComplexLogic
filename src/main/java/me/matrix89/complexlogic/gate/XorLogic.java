package me.matrix89.complexlogic.gate;

import net.minecraft.util.EnumFacing;
import pl.asie.simplelogic.gates.PartGate;

public class XorLogic extends BundledGateLogic {
    @Override
    public Connection getType(EnumFacing dir) {
        switch (dir) {
            case NORTH:
                return Connection.OUTPUT_BUNDLED;
            case EAST:
            case WEST:
                return Connection.INPUT_BUNDLED;
            default:
                return Connection.NONE;
        }
    }

    @Override
    boolean calculateOutput(PartGate parent) {
        int a = bundledRsToDigi(getInputValueBundled(EnumFacing.WEST));
        int b = bundledRsToDigi(getInputValueBundled(EnumFacing.EAST));
        setBundledOutputValue(EnumFacing.NORTH, bundledDigiToRs(a ^ b));
        return false;
    }

    @Override
    public State getLayerState(int i) {
        return State.ON;
    }

    @Override
    public State getTorchState(int i) {
        return State.ON;
    }
}
