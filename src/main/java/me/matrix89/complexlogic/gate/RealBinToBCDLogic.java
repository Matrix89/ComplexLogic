package me.matrix89.complexlogic.gate;

import net.minecraft.util.EnumFacing;
import pl.asie.simplelogic.gates.logic.GateConnection;
import pl.asie.simplelogic.gates.logic.GateRenderState;
import pl.asie.simplelogic.gates.logic.IGateContainer;

public class RealBinToBCDLogic extends BundledGateLogic {

    public RealBinToBCDLogic() {
        super();
    }

    @Override
    public GateConnection getType(EnumFacing dir) {
        switch (dir) {
            case NORTH:
            case EAST:
                return GateConnection.OUTPUT_BUNDLED;
            case SOUTH:
                return GateConnection.INPUT_BUNDLED;
            default:
                return GateConnection.NONE;
        }
    }

    @Override
    public boolean updateOutputs(IGateContainer gate) {
        int in = bundledRsToDigi(getInputValueBundled(EnumFacing.SOUTH));
        int[] lcd = new int[5];
        int i = 0;
        while (in > 0 && i < 5) {
            lcd[i] = in % 10;
            in = in / 10;
            ++i;
        }

        setBundledOutputValue(EnumFacing.NORTH, bundledDigiToRs(lcd[4]));
        setBundledOutputValue(EnumFacing.EAST, bundledDigiToRs(lcd[0] | (lcd[1]<<4)| (lcd[2]<<8) | (lcd[3]<<12)));
        return true;

    }

    @Override
    public GateRenderState getLayerState(int i) {
        return GateRenderState.ON;
    }

    @Override
    public GateRenderState getTorchState(int i) {
        return GateRenderState.ON;
    }
}
