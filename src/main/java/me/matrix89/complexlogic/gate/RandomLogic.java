package me.matrix89.complexlogic.gate;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import pl.asie.simplelogic.gates.PartGate;

import java.util.Random;

public class RandomLogic extends BundledGateLogic {
    private static Random prng = new Random();
    private boolean updateSignalOn = false;

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag, boolean isClient) {
        if(!isClient){
            tag.setBoolean("updateSignalOn", updateSignalOn);
        }
        return super.writeToNBT(tag, isClient);
    }

    @Override
    public boolean readFromNBT(NBTTagCompound compound, boolean isClient) {
        boolean updateSignalOnOld = updateSignalOn;
        if(compound.hasKey("updateSignalOn")){
            updateSignalOn = compound.getBoolean("updateSignalOn");
        }
        return super.readFromNBT(compound, isClient) || updateSignalOnOld!=updateSignalOn;
    }

    @Override
    public Connection getType(EnumFacing dir) {
        switch (dir) {
            case NORTH: return Connection.OUTPUT_BUNDLED;
            case SOUTH: return Connection.INPUT;
            default: return Connection.NONE;
        }
    }

    @Override
    public State getLayerState(int i) {
        return getInputValueInside(EnumFacing.SOUTH) != 0 ? State.ON : State.OFF;
    }

    @Override
    public boolean tick(PartGate parent) {

        if (getInputValueInside(EnumFacing.SOUTH) != 0) {
            if(!updateSignalOn){
                updateSignalOn = true;
                setBundledValue(EnumFacing.NORTH, bundledDigiToRs(prng.nextInt()));
            }
        }else{
            updateSignalOn = false;
        }
        return super.tick(parent);
    }

    @Override
    public State getTorchState(int i) {
        return State.ON;
    }
}
