package me.matrix89.complexlogic;

import net.minecraft.util.EnumFacing;
import pl.asie.simplelogic.gates.PartGate;
import pl.asie.simplelogic.gates.logic.GateLogic;

public class AdderLogic extends GateLogic {

    byte[] value = new byte[16];
    int v = 0;
    byte carry = 0;

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

    @Override
    public State getLayerState(int i) {
        return getOutputValueInside(EnumFacing.SOUTH) != 0 ? State.ON : State.OFF;
    }

    @Override
    public boolean tick(PartGate parent) {
        int temp = v;
        byte[] inA = parent.getBundledInput(EnumFacing.EAST);
        byte[] inB = parent.getBundledInput(EnumFacing.WEST);
        int a = 0;
        int b = 0;
        for (int i = 0; i < 16; i++) {
            a |= ((inA[i] != 0) ? 1 : 0) << i;
            b |= ((inB[i] != 0) ? 1 : 0) << i;
        }

        v = a + b;
        for (int i = 0; i < value.length; i++) {
            value[i] = (byte) ((v & (1 << i)) != 0 ? 15 : 0);
        }
        if (v >>> 16 != 0) {
            carry = 15;
        } else {
            carry = 0;
        }
        return updateOutputs() || temp != v;
    }

    @Override
    public byte[] getOutputValueBundled(EnumFacing side) {
        return value;
    }

    @Override
    public State getTorchState(int i) {
        return State.ON;
    }

    @Override
    protected byte calculateOutputInside(EnumFacing side) {
        return side == EnumFacing.SOUTH ? carry : 0;
    }
}
