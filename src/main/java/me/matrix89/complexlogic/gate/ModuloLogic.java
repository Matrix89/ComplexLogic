package me.matrix89.complexlogic.gate;

import net.minecraft.util.EnumFacing;
import pl.asie.simplelogic.gates.PartGate;
import pl.asie.simplelogic.gates.logic.GateLogic;

public class ModuloLogic extends BundledGateLogic{
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
    public boolean tick(PartGate parent) {
        byte[] inA = parent.getBundledInput(EnumFacing.WEST);
        byte[] inB = parent.getBundledInput(EnumFacing.EAST);
        int a = bundledRsToDigi(inA);
        int b = bundledRsToDigi(inB);
        int v = 0;
        try {
            v = a % b;
        }catch (ArithmeticException e){
            outputValues[2] = 15;
        }
        setBundledValue(EnumFacing.NORTH, bundledDigiToRs(v));
        return super.tick(parent);
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
