package me.matrix89.complexlogic.gate;

import net.minecraft.util.EnumFacing;
import pl.asie.simplelogic.gates.PartGate;

public class BitReordererLogic extends BundledGateLogic {

    @Override
    public Connection getType(EnumFacing dir) {
        switch (dir) {
            case NORTH:
                return Connection.OUTPUT_BUNDLED;
            case SOUTH:
                return Connection.INPUT_BUNDLED;
            default:
                return Connection.NONE;
        }
    }

    @Override
    public boolean tick(PartGate parent) {
        byte[] in = getInputValueBundled(EnumFacing.SOUTH);
        byte[] out = new byte[16];
        for (int i = 0; i < in.length; i++) {
            out[15 - i] = in[i];
        }
        setBundledValue(EnumFacing.NORTH, out);

        return super.tick(parent);
    }

    @Override
    public State getLayerState(int i) {
        return State.OFF;
    }

    @Override
    public State getTorchState(int i) {
        return State.OFF;
    }
}
