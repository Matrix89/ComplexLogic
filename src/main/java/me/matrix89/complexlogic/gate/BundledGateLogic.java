package me.matrix89.complexlogic.gate;

import net.minecraft.util.EnumFacing;
import pl.asie.simplelogic.gates.logic.GateLogic;
import pl.asie.simplelogic.gates.logic.IGateContainer;

import javax.annotation.Nullable;

public abstract class BundledGateLogic extends GateLogic {

    public BundledGateLogic() {
    }

    @Override
    protected byte calculateOutputInside(EnumFacing enumFacing) {
        return 0;
    }

    @Override
    public boolean updateOutputs(IGateContainer gateContainer) {
        return false;
    }

    public int bundledRsToDigi(@Nullable byte[] values) {
        int out = 0;
        if (values != null) {
            for (int i = 0; i < 16; i++) {
                out |= ((values[i] != 0) ? 1 : 0) << i;
            }
        }
        return out;
    }

    public byte[] bundledDigiToRs(int value) {
        byte[] out = new byte[16];
        for (int i = 0; i < out.length; i++) {
            out[i] = (byte) ((value & (1 << i)) != 0 ? 15 : 0);
        }
        return out;
    }

    /**
     * can use
     */
    public void setBundledOutputValue(EnumFacing side, byte[] value) {
        outputValuesBundled[side.getIndex() - 2] = value;
    }

    /**
     * can use
     */
    public void setRedstoneOutputValue(EnumFacing side, byte value) {
        outputValues[side.getIndex() - 2] = value;
    }
}
