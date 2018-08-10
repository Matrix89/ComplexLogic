package me.matrix89.complexlogic.gate;

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
    boolean calculateOutput(PartGate parent) {
        byte[] inA = getInputValueBundled(EnumFacing.EAST);
        byte[] inB = getInputValueBundled(EnumFacing.WEST);
        int a = bundledRsToDigi(inA);
        int b = bundledRsToDigi(inB);
        int v = a + b;
        setBundledOutputValue(EnumFacing.NORTH, bundledDigiToRs(v));
        setRedstoneOutputValue(EnumFacing.SOUTH, (byte) (v >>> 16 != 0 ? 15 : 0)); // carry
        return false;
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
