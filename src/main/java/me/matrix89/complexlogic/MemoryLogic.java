package me.matrix89.complexlogic;

import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import net.minecraft.util.EnumFacing;
import pl.asie.simplelogic.gates.PartGate;

import java.util.Map;

public class MemoryLogic extends BundledGateLogic {

    private int address = 0;
    private Map<Integer, Integer> memory = new Int2IntOpenHashMap();

    @Override
    public Connection getType(EnumFacing dir) {
        switch (dir) {
            case NORTH: return Connection.OUTPUT_BUNDLED;
            case EAST: return Connection.INPUT;
            case WEST: return Connection.INPUT_BUNDLED;
            case SOUTH: return Connection.INPUT_BUNDLED;
            default: return Connection.NONE;
        }
    }

    @Override
    public boolean tick(PartGate parent) {
        address = bundledRsToDigi(parent.getBundledInput(EnumFacing.WEST));
        if(getInputValueInside(EnumFacing.EAST) != 0) {
            memory.put(address, bundledRsToDigi(parent.getBundledInput(EnumFacing.SOUTH)));
        }
        return super.tick(parent);
    }

    @Override
    public byte[] calculateBundledOutput(EnumFacing facing) {
        if(facing == EnumFacing.NORTH) return bundledDigiToRs(memory.getOrDefault(address,0));
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
