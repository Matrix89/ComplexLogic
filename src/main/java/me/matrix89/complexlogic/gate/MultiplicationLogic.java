package me.matrix89.complexlogic.gate;

import net.minecraft.util.EnumFacing;
import pl.asie.simplelogic.gates.PartGate;
import pl.asie.simplelogic.gates.logic.GateLogic;

public class MultiplicationLogic extends BundledGateLogic {
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
    void calculateOutput(PartGate parent) {
        byte[] inA = getInputValueBundled(EnumFacing.WEST);
        byte[] inB = getInputValueBundled(EnumFacing.EAST);
        int a = bundledRsToDigi(inA);
        int b = bundledRsToDigi(inB);

        int v = a * b;
        setBundledOutputValue(EnumFacing.NORTH, bundledDigiToRs(v));
        setRedstoneOutputValue(EnumFacing.SOUTH, (byte) (v >>> 16 != 0 ? 15 : 0)); // carry
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
