package me.matrix89.complexlogic.gate;

import net.minecraft.util.EnumFacing;
import pl.asie.simplelogic.gates.PartGate;

public class MultiplexerLogic extends BundledGateLogic{

    @Override
    public Connection getType(EnumFacing dir) {
        switch (dir) {
            case NORTH:
                return Connection.OUTPUT_BUNDLED;
            case EAST:
            case WEST:
                return Connection.INPUT_BUNDLED;
            case SOUTH:
                return Connection.INPUT;
            default:
                return Connection.NONE;
        }
    }

    @Override
    public boolean tick(PartGate parent) {
        boolean inputChange = parent.updateInputs(this.inputValues);
        if(getInputValueInside(EnumFacing.SOUTH)!=0){
            setBundledValue(EnumFacing.NORTH, parent.getBundledInput(EnumFacing.EAST));
        }else {
            setBundledValue(EnumFacing.NORTH, parent.getBundledInput(EnumFacing.WEST));
        }
        return  super.tick(parent) || inputChange;
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
