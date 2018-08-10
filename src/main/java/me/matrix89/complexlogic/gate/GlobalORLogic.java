package me.matrix89.complexlogic.gate;

import net.minecraft.util.EnumFacing;
import pl.asie.simplelogic.gates.PartGate;

public class GlobalORLogic extends BundledGateLogic {

    @Override
    public Connection getType(EnumFacing dir) {
        switch (dir) {
            case SOUTH:
                return Connection.INPUT_BUNDLED;
            case NORTH:
                return Connection.OUTPUT;
            default:
                return Connection.NONE;
        }
    }

    @Override
    boolean calculateOutput(PartGate parent) {
        byte[] in = getInputValueBundled(EnumFacing.SOUTH);
        byte out = 0;
        for (byte i : in) {
            out |= i;
        }
        setRedstoneOutputValue(EnumFacing.NORTH, out);
        return false;
    }

    @Override
    public State getLayerState(int i) {
        return i == 0 && getOutputValueInside(EnumFacing.NORTH) != 0 ? State.ON : State.OFF;
    }

    @Override
    public State getTorchState(int i) {
        return State.ON;
    }
}
