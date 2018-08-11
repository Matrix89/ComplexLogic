package me.matrix89.complexlogic.gate;

import net.minecraft.util.EnumFacing;
import pl.asie.simplelogic.gates.PartGate;

public class ShifterLogic extends BundledGateLogic {


    @Override
    public Connection getType(EnumFacing dir) {
        switch (dir) {
            case NORTH:
                return Connection.OUTPUT_BUNDLED;
            case SOUTH:
                return Connection.INPUT_BUNDLED;
            case EAST:
                return Connection.INPUT;
            default:
                return Connection.NONE;
        }
    }

    @Override
    void calculateOutput(PartGate parent) {
        byte[] input = parent.getBundledInput(EnumFacing.SOUTH);
        byte[] shifted = new byte[16];
        parent.updateInputs(this.inputValues);
        if (getInputValueInside(EnumFacing.EAST) == 0) {
            System.arraycopy(input, 0, shifted, 1, 15);
        } else {
            System.arraycopy(input, 1, shifted, 0, 15);
        }
        setBundledOutputValue(EnumFacing.NORTH, shifted);
    }

    @Override
    public State getLayerState(int i) {
        return getInputValueInside(EnumFacing.EAST) != 0 ? State.ON : State.OFF;
    }


    @Override
    public State getTorchState(int i) {
        switch (i) {
            case 0:
                return State.ON;
            case 1:
                return getInputValueInside(EnumFacing.EAST) != 0 ? State.ON : State.OFF;
            default:
                return State.OFF;
        }
    }
}
