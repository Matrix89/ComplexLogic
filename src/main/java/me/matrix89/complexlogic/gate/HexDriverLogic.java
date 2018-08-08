package me.matrix89.complexlogic.gate;

import net.minecraft.util.EnumFacing;
import pl.asie.simplelogic.gates.PartGate;

public class HexDriverLogic extends BundledGateLogic{
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
            new byte[]{15, 15, 0, 15, 15, 15, 15},//A
            new byte[]{0, 15, 15, 15, 0, 15, 15},//B
            new byte[]{15, 0, 15, 15, 0, 15, 0},//C
            new byte[]{0, 15, 15, 0, 15, 15, 15},//D
            new byte[]{15, 15, 15, 15, 0, 15, 0},//E
            new byte[]{15, 15, 0, 15, 0, 15, 0},//F
    };

    @Override
    public Connection getType(EnumFacing dir) {
        switch (dir) {
            case NORTH:
            case WEST:
                return Connection.OUTPUT_BUNDLED;
            case SOUTH:
                return Connection.INPUT_BUNDLED;
            default:
                return Connection.NONE;
        }
    }

    @Override
    public boolean tick(PartGate parent) {
        int in = bundledRsToDigi(parent.getBundledInput(EnumFacing.SOUTH));
        int[] lcd = new int[4];
        int i = 0;
        while (in > 0 && i < 4) {
            lcd[i] = in % 16;
            in = in / 16;
            ++i;
        }
        byte[] outWest = new byte[16];
        byte[] outNorth = new byte[16];

        System.arraycopy(decoder[lcd[1]], 0, outNorth, 0, 7);
        System.arraycopy(decoder[lcd[0]], 0, outNorth, 7, 7);

        System.arraycopy(decoder[lcd[3]], 0, outWest, 0, 7);
        System.arraycopy(decoder[lcd[2]], 0, outWest, 7, 7);

        setBundledValue(EnumFacing.WEST, outWest);
        setBundledValue(EnumFacing.NORTH, outNorth);
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
