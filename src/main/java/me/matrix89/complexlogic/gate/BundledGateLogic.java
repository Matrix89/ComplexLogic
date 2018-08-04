package me.matrix89.complexlogic.gate;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import pl.asie.simplelogic.gates.PartGate;
import pl.asie.simplelogic.gates.logic.GateLogic;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class BundledGateLogic extends GateLogic {
    private final Map<EnumFacing, byte[]> bundledValues = new EnumMap<>(EnumFacing.class);
    private EnumFacing[] horizontals = EnumFacing.HORIZONTALS;
    private boolean shouldUpdate = false;

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag, boolean isClient) {
        if(!isClient){
            NBTTagCompound bundledValuesTag = new NBTTagCompound();
            bundledValues.forEach( (k,v) -> bundledValuesTag.setByteArray(k.getName(), v));
            tag.setTag("bundledValues", bundledValuesTag);
            tag.setBoolean("shouldUpdate", shouldUpdate);
        }

        return super.writeToNBT(tag, isClient);
    }

    @Override
    public boolean readFromNBT(NBTTagCompound compound, boolean isClient) {
        boolean oldSshouldUpdate  = shouldUpdate;
        AtomicBoolean update = new AtomicBoolean(false);
        if(!isClient){
            if(compound.hasKey("shouldUpdate")){
                shouldUpdate = compound.getBoolean("shouldUpdate");
            }
            if(compound.hasKey("bundledValues")){
                 NBTTagCompound tag = compound.getCompoundTag("bundledValues");
                 Arrays.stream(horizontals).map(EnumFacing::getName).filter(tag::hasKey).forEach(i -> {
                     EnumFacing dir = EnumFacing.byName(i);
                     byte[] oldArray = bundledValues.get(dir);
                     byte[] newArray = tag.getByteArray(i);
                     if(!Arrays.equals(newArray, oldArray)) {
                         bundledValues.put(dir, tag.getByteArray(i));
                         update.set(true);
                     }
                 } );
            }
        }

        return super.readFromNBT(compound, isClient) || oldSshouldUpdate != shouldUpdate || update.get();
    }

    public BundledGateLogic() {
        for (EnumFacing horizontal : horizontals) {
            if (getType(horizontal) == Connection.OUTPUT_BUNDLED || getType(horizontal) == Connection.INPUT_BUNDLED) {
                bundledValues.put(horizontal, new byte[16]);
            }
        }
    }

    @Override
    public boolean tick(PartGate parent) {
        boolean change = false;

        for (EnumFacing facing : horizontals) {
            if (getType(facing) != Connection.INPUT_BUNDLED) continue;
            byte[] newValue = parent.getBundledInput(facing);
            if (!Arrays.equals(bundledValues.get(facing), newValue)) {
                change = true;
                bundledValues.replace(facing, newValue);
            }
        }

        for (EnumFacing facing : horizontals) {
            if (getType(facing) != Connection.OUTPUT_BUNDLED) continue;
            byte[] newValue = calculateBundledOutput(facing);
            if (!Arrays.equals(bundledValues.get(facing), newValue)) {
                change = true;
                bundledValues.replace(facing, newValue);
            }
        }

        boolean update = super.tick(parent);
        if (shouldUpdate) {
            shouldUpdate = false;
            return true;
        }
        return change || update;
    }

    public int bundledRsToDigi(byte[] values) {
        int out = 0;
        for (int i = 0; i < 16; i++) {
            out |= ((values[i] != 0) ? 1 : 0) << i;
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

    public void setBundledValue(EnumFacing side, byte[] value) {
        bundledValues.replace(side, value);
        shouldUpdate = true;
    }

    public byte[] getInputValueBundled(EnumFacing side) {
        return bundledValues.getOrDefault(side, new byte[16]);
    }

    @Override
    public byte[] getOutputValueBundled(EnumFacing side) {
        return bundledValues.getOrDefault(side, new byte[16]);
    }

    @Override
    protected byte calculateOutputInside(EnumFacing enumFacing) {
        return outputValues[enumFacing.getIndex() - 1];
    }

    public byte[] calculateBundledOutput(EnumFacing facing) {
        return bundledValues.getOrDefault(facing, new byte[16]);
    }
}
