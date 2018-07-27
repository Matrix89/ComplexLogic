package me.matrix89.complexlogic;

import net.minecraft.util.EnumFacing;
import pl.asie.simplelogic.gates.PartGate;

public class AdderLogic extends BundledGateLogic {

    @Override
    public Connection getType(EnumFacing dir) {
        switch (dir) {
            case NORTH:
                return Connection.OUTPUT_BUNDLED;
            case EAST:
            case WEST:
                return Connection.INPUT_BUNDLED;
            case SOUTH:
                return Connection.OUTPUT;
            default:
                return Connection.NONE;
        }
    }

    @Override
    public boolean tick(PartGate parent) {
        byte[] inA = parent.getBundledInput(EnumFacing.EAST);
        byte[] inB = parent.getBundledInput(EnumFacing.WEST);
        int a = bundledRsToDigi(inA);
        int b = bundledRsToDigi(inB);

        int v = a + b;
        setBundledValue(EnumFacing.NORTH, bundledDigiToRs(v));
        outputValues[2] = (byte) (v >>> 16 != 0 ? 15 : 0); // carry

        return super.tick(parent);
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
