package me.matrix89.complexlogic.gate;

import net.minecraft.util.EnumFacing;
import pl.asie.simplelogic.gates.PartGate;
import pl.asie.simplelogic.gates.logic.GateLogic;

public class ModuloLogic extends BundledGateLogic {
    @Override
    public GateLogic.Connection getType(EnumFacing dir) {
        switch (dir) {
            case NORTH:
                return GateLogic.Connection.OUTPUT_BUNDLED;
            case EAST:
            case WEST:
                return GateLogic.Connection.INPUT_BUNDLED;
            case SOUTH:
                return GateLogic.Connection.OUTPUT;
            default:
                return GateLogic.Connection.NONE;
        }
    }

    @Override
    boolean calculateOutput(PartGate parent) {
        byte[] inA = getInputValueBundled(EnumFacing.WEST);
        byte[] inB = getInputValueBundled(EnumFacing.EAST);
        int a = bundledRsToDigi(inA);
        int b = bundledRsToDigi(inB);
        int v = 0;
        try {
            v = a % b;
        } catch (ArithmeticException e) {
            setRedstoneOutputValue(EnumFacing.SOUTH, (byte) 15);
        }
        setBundledOutputValue(EnumFacing.NORTH, bundledDigiToRs(v));
        return false;
    }

    @Override
    public GateLogic.State getTorchState(int i) {
        return GateLogic.State.ON;
    }

    @Override
    public GateLogic.State getLayerState(int i) {
        return getOutputValueInside(EnumFacing.SOUTH) != 0 ? GateLogic.State.ON : GateLogic.State.OFF;
    }

}
