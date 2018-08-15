package me.matrix89.complexlogic.gate;

import net.minecraft.util.EnumFacing;
import pl.asie.simplelogic.gates.logic.GateConnection;
import pl.asie.simplelogic.gates.logic.GateRenderState;
import pl.asie.simplelogic.gates.logic.IGateContainer;

public class BinTo7Seg extends BundledGateLogic {
    private static final byte[][] decoder = new byte[][]{
            new byte[]{15, 0, 15, 15, 15, 15, 15},//0
            new byte[]{0, 0, 0, 0, 15, 0, 15},//1
            new byte[]{15, 15, 15, 0, 15, 15, 0},//2
            new byte[]{15, 15, 15, 0, 15, 0, 15},//3
            new byte[]{0, 15, 0, 15, 15, 0, 15},//4
            new byte[]{15, 15, 15, 15, 0, 0, 15},//5
            new byte[]{15, 15, 15, 15, 0, 15, 15},//6
            new byte[]{15, 0, 0, 0, 15, 0, 15},//7
            new byte[]{15, 15, 15, 15, 15, 15, 15},//8
            new byte[]{15, 15, 15, 15, 15, 0, 15},//9
    };

    public BinTo7Seg() {
        super();
        byte[] outWest = new byte[16];
        byte[] outNorth = new byte[16];
        byte[] outEast = new byte[16];
        System.arraycopy(decoder[0], 0, outEast, 0, 7);
        System.arraycopy(decoder[0], 0, outEast, 7, 7);

        System.arraycopy(decoder[0], 0, outNorth, 0, 7);
        System.arraycopy(decoder[0], 0, outNorth, 7, 7);

        System.arraycopy(decoder[0], 0, outWest, 7, 7);

        setBundledOutputValue(EnumFacing.WEST, outWest);
        setBundledOutputValue(EnumFacing.NORTH, outNorth);
        setBundledOutputValue(EnumFacing.EAST, outEast);
    }

    @Override
    public GateConnection getType(EnumFacing dir) {
        switch (dir) {
            case NORTH:
            case EAST:
            case WEST:
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
        byte[] outWest = new byte[16];
        byte[] outNorth = new byte[16];
        byte[] outEast = new byte[16];
        System.arraycopy(decoder[lcd[1]], 0, outEast, 0, 7);
        System.arraycopy(decoder[lcd[0]], 0, outEast, 7, 7);

        System.arraycopy(decoder[lcd[3]], 0, outNorth, 0, 7);
        System.arraycopy(decoder[lcd[2]], 0, outNorth, 7, 7);

        System.arraycopy(decoder[lcd[4]], 0, outWest, 7, 7);

        setBundledOutputValue(EnumFacing.WEST, outWest);
        setBundledOutputValue(EnumFacing.NORTH, outNorth);
        setBundledOutputValue(EnumFacing.EAST, outEast);
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
