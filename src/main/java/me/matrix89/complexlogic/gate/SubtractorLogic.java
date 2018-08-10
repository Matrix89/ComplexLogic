package me.matrix89.complexlogic.gate;

import net.minecraft.util.EnumFacing;
import pl.asie.simplelogic.gates.PartGate;

public class SubtractorLogic extends BundledGateLogic {

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
    void calculateOutput(PartGate parent) {
        byte[] inA = parent.getBundledInput(EnumFacing.EAST);
        byte[] inB = parent.getBundledInput(EnumFacing.WEST);
        int a = bundledRsToDigi(inA);
        int b = bundledRsToDigi(inB);

        int v = b - a;
        setBundledOutputValue(EnumFacing.NORTH, bundledDigiToRs(v));

    }

    @Override
    public State getTorchState(int i) {
        return State.ON;
    }

    @Override
    public State getLayerState(int i) {
        return getOutputValueInside(EnumFacing.SOUTH) != 0 ? State.ON : State.OFF;
    }
}
