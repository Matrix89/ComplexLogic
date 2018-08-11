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
    private final Map<EnumFacing, byte[]> bundledInputValues = new EnumMap<>(EnumFacing.class);
    private final Map<EnumFacing, byte[]> bundledOutputValues = new EnumMap<>(EnumFacing.class);
    private EnumFacing[] horizontals = EnumFacing.HORIZONTALS;
    private boolean shouldUpdate = false;

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag, boolean isClient) {
        if (!isClient) {
            NBTTagCompound bundledIntputValuesTag = new NBTTagCompound();
            bundledInputValues.forEach((k, v) -> bundledIntputValuesTag.setByteArray(k.getName(), v));
            tag.setTag("bundledInputValues", bundledIntputValuesTag);


            NBTTagCompound bundledOutputValuesTag = new NBTTagCompound();
            bundledOutputValues.forEach((k, v) -> bundledOutputValuesTag.setByteArray(k.getName(), v));
            tag.setTag("bundledOutputValues", bundledOutputValuesTag);


            tag.setBoolean("shouldUpdate", shouldUpdate);
        }

        return super.writeToNBT(tag, isClient);
    }

    private void readValues(AtomicBoolean update, NBTTagCompound tag, Map<EnumFacing, byte[]> map) {
        Arrays.stream(horizontals).map(EnumFacing::getName).filter(tag::hasKey).forEach(i -> {
            EnumFacing dir = EnumFacing.byName(i);
            byte[] oldArray = map.get(dir);
            byte[] newArray = tag.getByteArray(i);
            if (!Arrays.equals(newArray, oldArray)) {
                map.put(dir, tag.getByteArray(i));
                update.set(true);
            }
        });
    }

    @Override
    public boolean readFromNBT(NBTTagCompound compound, boolean isClient) {
       // boolean oldShouldUpdate = shouldUpdate;
        AtomicBoolean update = new AtomicBoolean(false);
        //if (!isClient) {
            if (compound.hasKey("shouldUpdate")) {
                shouldUpdate = compound.getBoolean("shouldUpdate");
            }
            if (compound.hasKey("bundledInputValues")) {
                NBTTagCompound tag = compound.getCompoundTag("bundledInputValues");
                readValues(update, tag, bundledInputValues);
            }
            if (compound.hasKey("bundledOutputValues")) {
                NBTTagCompound tag = compound.getCompoundTag("bundledOutputValues");
                readValues(update, tag, bundledOutputValues);
            }
       // }

        return super.readFromNBT(compound, isClient) /*|| oldShouldUpdate != shouldUpdate*/ || update.get();
    }

    public BundledGateLogic() {
        for (EnumFacing horizontal : horizontals) {
            if (getType(horizontal) == Connection.OUTPUT_BUNDLED) {
                bundledOutputValues.put(horizontal, new byte[16]);
            } else if (getType(horizontal) == Connection.INPUT_BUNDLED) {
                bundledInputValues.put(horizontal, new byte[16]);
            }
        }
    }

    abstract void calculateOutput(PartGate parent);

    final void forceUpdate(){
        shouldUpdate = true;
    }

    @Override
    public final boolean tick(PartGate parent) {
        boolean change = parent.updateInputs(this.inputValues); //Update redstone inputs

        for (EnumFacing facing : horizontals) { //Update bundled inputs
            if (getType(facing) != Connection.INPUT_BUNDLED) continue;
            byte[] newValue = parent.getBundledInput(facing);
            if (!Arrays.equals(bundledInputValues.get(facing), newValue)) {
                change = true;
                bundledInputValues.replace(facing, newValue);
            }
        }
        change = change || shouldUpdate;
        if (!change) return false;
        calculateOutput(parent);
        shouldUpdate = false;
        return true;
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

    /**
     * can use
     */
    public void setBundledInputValue(EnumFacing side, byte[] value) {
        //shouldUpdate = !Arrays.equals(bundledInputValues.get(side), value) || shouldUpdate;
        bundledInputValues.replace(side, value);
    }

    /**
     * can use
     */
    public void setBundledOutputValue(EnumFacing side, byte[] value) {
        //shouldUpdate = !Arrays.equals(bundledOutputValues.get(side), value) || shouldUpdate;
        bundledOutputValues.replace(side, value);
    }

    /**
     * can use
     */
    public void setRedstoneInputValue(EnumFacing side, byte value) {
        //shouldUpdate = inputValues[side.getIndex() - 2] != value || shouldUpdate;
        inputValues[side.getIndex() - 2] = value;
    }

    /**
     * can use
     */
    public void setRedstoneOutputValue(EnumFacing side, byte value) {
        //shouldUpdate = outputValues[side.getIndex() - 2] != value || shouldUpdate;
        outputValues[side.getIndex() - 2] = value;
    }

    /**
     * can use
     */
    public byte[] getInputValueBundled(EnumFacing side) {
        return bundledInputValues.getOrDefault(side, new byte[16]);
    }

    /**
     * can use
     */
    @Override
    public byte[] getOutputValueBundled(EnumFacing side) {
        return bundledOutputValues.getOrDefault(side, new byte[16]);
    }


    @Override
    protected byte calculateOutputInside(EnumFacing enumFacing) {
        return 0;
    }
}
