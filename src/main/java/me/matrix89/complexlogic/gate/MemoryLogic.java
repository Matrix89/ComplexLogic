package me.matrix89.complexlogic.gate;

import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.EnumFacing;
import pl.asie.simplelogic.gates.PartGate;

import java.util.Map;

public class MemoryLogic extends BundledGateLogic {

    private int address = 0;
    private Map<Integer, Integer> memory = new Int2IntOpenHashMap();

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag, boolean isClient) {
        if(!isClient){
            tag.setInteger("address", address);
            NBTTagCompound mapTag = new NBTTagCompound();
            memory.forEach((k,v) -> mapTag.setInteger(k.toString(), v));
            tag.setTag("memory", mapTag);
        }
        return super.writeToNBT(tag, isClient);
    }

    @Override
    public boolean readFromNBT(NBTTagCompound compound, boolean isClient) {
        if(!isClient){
            if(compound.hasKey("address")) {
                address = compound.getInteger("address");
            }
            if(compound.hasKey("memory")){
                NBTTagCompound tag = compound.getCompoundTag("memory");
                tag.getKeySet().forEach((k)-> memory.put(Integer.parseInt(k), tag.getInteger(k)));
            }
        }
        return super.readFromNBT(compound, isClient);
    }

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
            int value = bundledRsToDigi(parent.getBundledInput(EnumFacing.SOUTH));
            if(value==0) {
                memory.remove(address);
            }else {
                memory.put(address, value);
            }
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
        return i==0 && getInputValueInside(EnumFacing.EAST)!=0? State.ON : State.OFF;
    }

    @Override
    public State getTorchState(int i) {
        return State.ON;
    }
}
