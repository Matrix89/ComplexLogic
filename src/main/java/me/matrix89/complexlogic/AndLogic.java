package me.matrix89.complexlogic;

import net.minecraft.util.EnumFacing;

public class AndLogic extends BundledGateLogic {
    @Override
    public Connection getType(EnumFacing dir) {
        switch (dir) {
            case NORTH:
                return Connection.OUTPUT_BUNDLED;
            case EAST:
            case WEST:
                return Connection.INPUT_BUNDLED;
            default:
                return Connection.NONE;
        }
    }

    @Override
    public byte[] calculateBundledOutput(EnumFacing facing) {
        int a = bundledRsToDigi(bundledValues.getOrDefault(EnumFacing.WEST, new byte[16]));
        int b = bundledRsToDigi(bundledValues.getOrDefault(EnumFacing.WEST, new byte[16]));
        return bundledDigiToRs(a & b);
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
