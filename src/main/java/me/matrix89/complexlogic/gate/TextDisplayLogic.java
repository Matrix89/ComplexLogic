package me.matrix89.complexlogic.gate;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import pl.asie.simplelogic.gates.PartGate;

import java.util.Arrays;

public class TextDisplayLogic extends BundledGateLogic {

    public byte[] value = new byte[16];
    public int codepointClient;

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag, boolean isClient) {
        super.writeToNBT(tag, isClient);
        tag.setByteArray("v", getInputValueBundled(EnumFacing.SOUTH));
        return tag;
    }

    @Override
    public boolean readFromNBT(NBTTagCompound compound, boolean isClient) {
        byte[] valueOld = value;
        if (compound.hasKey("v"))
            value = compound.getByteArray("v");
        boolean valueChanged = !Arrays.equals(valueOld, value);
        if (valueChanged && isClient) {
            codepointClient = 0;
            for (int i = 0; i < 16; i++) {
                if (value[i] != 0) {
                    codepointClient |= 1 << i;
                }
            }
        }
        return super.readFromNBT(compound, isClient) || valueChanged;
    }

    @Override
    void calculateOutput(PartGate parent) {

    }

    @Override
    public Connection getType(EnumFacing dir) {
         return dir == EnumFacing.SOUTH ? Connection.INPUT_BUNDLED : Connection.NONE;
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
