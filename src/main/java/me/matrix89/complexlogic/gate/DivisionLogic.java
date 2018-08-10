package me.matrix89.complexlogic.gate;

import net.minecraft.util.EnumFacing;
import pl.asie.simplelogic.gates.PartGate;

public class DivisionLogic extends BundledGateLogic {
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

    public DivisionLogic(){
        super();
        setRedstoneOutputValue(EnumFacing.SOUTH, (byte) 15);
    }

    @Override
    void calculateOutput(PartGate parent) {
        byte[] inA = getInputValueBundled(EnumFacing.WEST);
        byte[] inB = getInputValueBundled(EnumFacing.EAST);
        int a = bundledRsToDigi(inA);
        int b = bundledRsToDigi(inB);
        int v = 0;
        try {
            v = a / b;
        } catch (ArithmeticException e) {
            setRedstoneOutputValue(EnumFacing.SOUTH, (byte) 15);
        }
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
