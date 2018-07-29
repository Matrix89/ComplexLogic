package me.matrix89.complexlogic.gate;

import net.minecraft.util.EnumFacing;
import pl.asie.simplelogic.gates.PartGate;

public class CounterLogic extends BundledGateLogic {
    private boolean incSignalOn = false;

    @Override
    public Connection getType(EnumFacing dir) {
        switch (dir) {
            case NORTH: return Connection.OUTPUT_BUNDLED;
            case SOUTH: return Connection.INPUT_BUNDLED;
            case EAST: return Connection.INPUT;
            default: return Connection.NONE;
        }
    }

    @Override
    public boolean tick(PartGate parent) {
/*        if(getInputValueInside(EnumFacing.WEST) != 0) {
            setBundledValue(EnumFacing.NORTH, getInputValueBundled(EnumFacing.SOUTH));
        }*/
        System.out.println(getInputValueInside(EnumFacing.EAST) + " " + incSignalOn);
        if(getInputValueInside(EnumFacing.EAST) != 0 && !incSignalOn) {
            incSignalOn = true;
            setBundledValue(EnumFacing.NORTH, bundledDigiToRs( bundledRsToDigi(getOutputValueBundled(EnumFacing.NORTH)) + 1 ));
        } else {
            incSignalOn = false;
        }

        return super.tick(parent);
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
