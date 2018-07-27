package me.matrix89.complexlogic;

import net.minecraft.util.EnumFacing;
import pl.asie.simplelogic.gates.PartGate;

public class MemoryLogic extends BundledGateLogic {

    private byte[] value = new byte[16];

    @Override
    public Connection getType(EnumFacing dir) {
        switch (dir) {
            case NORTH: return Connection.OUTPUT_BUNDLED;
            case EAST: return Connection.INPUT;
            case SOUTH: return Connection.INPUT_BUNDLED;
            default: return Connection.NONE;
        }
    }

    @Override
    public boolean tick(PartGate parent) {
        if(getInputValueInside(EnumFacing.WEST) != 0) {
            value = parent.getBundledInput(EnumFacing.SOUTH);
        }
        return super.tick(parent);
    }

    @Override
    public byte[] calculateBundledOutput(EnumFacing facing) {
        if(facing == EnumFacing.NORTH) return value;
        return new byte[16];
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
