package me.matrix89.complexlogic.gate;

import net.minecraft.util.EnumFacing;
import pl.asie.simplelogic.gates.PartGate;

public class CounterLogic extends BundledGateLogic {
    private boolean incSignalOn = false;
    private boolean updateSignalOn = false;

    @Override
    public Connection getType(EnumFacing dir) {
        switch (dir) {
            case NORTH:
                return Connection.OUTPUT_BUNDLED;
            case SOUTH:
                return Connection.INPUT_BUNDLED;
            case EAST:
            case WEST:
                return Connection.INPUT;
            default:
                return Connection.NONE;
        }
    }

    private int counter(){
        return isSideInverted(EnumFacing.EAST)? -1: 1;
    }

    @Override
    public boolean tick(PartGate parent) {
        parent.updateInputs(this.inputValues);
        if (getInputValueInside(EnumFacing.WEST) != 0) {
            if(!updateSignalOn){
                updateSignalOn = true;
                setBundledValue(EnumFacing.NORTH, getInputValueBundled(EnumFacing.SOUTH));
            }
        }else{
            updateSignalOn = false;
        }
        if (getInputValueOutside(EnumFacing.EAST) != 0) {
            if (!incSignalOn) {
                incSignalOn = true;
                setBundledValue(EnumFacing.NORTH, bundledDigiToRs(bundledRsToDigi(getOutputValueBundled(EnumFacing.NORTH)) + counter()));
            }
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
        switch (i){
            case 1: return getInputValueInside(EnumFacing.EAST)!=0? State.ON :State.OFF;
            case 0:
            default: return State.ON;
        }
    }
}
