package me.matrix89.complexlogic.gate;

import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import pl.asie.simplelogic.gates.PartGate;

import java.util.Map;

public class MemoryLogic extends BundledGateLogic {

    private int address = 0;
    private Map<Integer, Integer> memory = new Int2IntOpenHashMap();
    private boolean updateSignalOn = false;

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag, boolean isClient) {
        if (!isClient) {
            tag.setInteger("address", address);
            NBTTagCompound mapTag = new NBTTagCompound();
            memory.forEach((k, v) -> mapTag.setInteger(k.toString(), v));
            tag.setTag("memory", mapTag);
            tag.setBoolean("updateSignalOn", updateSignalOn);
            tag.setInteger("newAddress", newAddress);
            tag.setInteger("newValue", newValue);
        }
        return super.writeToNBT(tag, isClient);
    }

    @Override
    public boolean readFromNBT(NBTTagCompound compound, boolean isClient) {
        boolean oldValue = updateSignalOn;
        int newAddressOld = newAddress;
        int newValueOld = newValue;
        if (!isClient) {
            if (compound.hasKey("address")) {
                address = compound.getInteger("address");
            }
            if (compound.hasKey("memory")) {
                NBTTagCompound tag = compound.getCompoundTag("memory");
                tag.getKeySet().forEach((k) -> memory.put(Integer.parseInt(k), tag.getInteger(k)));
            }
            if (compound.hasKey("updateSignalOn")) {
                updateSignalOn = compound.getBoolean("updateSignalOn");
            }
            if (compound.hasKey("newAddress")) {
                newAddress = compound.getInteger("newAddress");
            }
            if (compound.hasKey("newValue")) {
                newValue = compound.getInteger("newValue");
            }
        }
        return super.readFromNBT(compound, isClient) || updateSignalOn != oldValue;
    }

    @Override
    public Connection getType(EnumFacing dir) {
        switch (dir) {
            case NORTH:
                return Connection.OUTPUT_BUNDLED;
            case EAST:
                return Connection.INPUT;
            case WEST:
                return Connection.INPUT_BUNDLED;
            case SOUTH:
                return Connection.INPUT_BUNDLED;
            default:
                return Connection.NONE;
        }
    }

    int newAddress = 0;
    int newValue = 0;

    @Override
    void calculateOutput(PartGate parent) {
        address = bundledRsToDigi(getInputValueBundled(EnumFacing.WEST));
        boolean updateSignalOnOld = updateSignalOn;
        if (getInputValueInside(EnumFacing.EAST) != 0) {
            if (!updateSignalOn) {
                updateSignalOn = true;
                int value = bundledRsToDigi(getInputValueBundled(EnumFacing.SOUTH));
                newAddress = address;
                newValue = value;
            }
        } else {
            updateSignalOn = false;
            if (newValue == 0) {
                memory.remove(newAddress);
            } else {
                memory.put(newAddress, newValue);
            }
        }
        setBundledOutputValue(EnumFacing.NORTH, bundledDigiToRs(memory.getOrDefault(address, 0)));
    }

    @Override
    public State getLayerState(int i) {
        return i == 0 && getInputValueInside(EnumFacing.EAST) != 0 ? State.ON : State.OFF;
    }

    @Override
    public State getTorchState(int i) {
        return State.ON;
    }
}
