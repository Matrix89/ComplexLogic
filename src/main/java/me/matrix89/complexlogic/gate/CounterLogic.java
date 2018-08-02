package me.matrix89.complexlogic.gate;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import pl.asie.simplelogic.gates.PartGate;

public class CounterLogic extends BundledGateLogic {
    private boolean incSignalOn = false;
    private boolean updateSignalOn = false;

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag, boolean isClient) {
        if(!isClient){
            tag.setBoolean("incSignalOn", incSignalOn);
            tag.setBoolean("updateSignalOn", updateSignalOn);
        }
        return super.writeToNBT(tag, isClient);
    }

    @Override
    public boolean readFromNBT(NBTTagCompound compound, boolean isClient) {
        boolean updateSignalOnOld = updateSignalOn;
        boolean incSignalOnOld = incSignalOn;
            if(compound.hasKey("updateSignalOn")){
                updateSignalOn = compound.getBoolean("updateSignalOn");
            }
            if(compound.hasKey("incSignalOn")){
                incSignalOn = compound.getBoolean("incSignalOn");
            }
        return super.readFromNBT(compound, isClient) || updateSignalOnOld!=updateSignalOn || incSignalOnOld!=incSignalOn;
    }

    @Override
    public Connection getType(EnumFacing dir) {
        switch (dir) {
            case NORTH:
                return Connection.OUTPUT_BUNDLED;
            case SOUTH:
                return Connection.INPUT_BUNDLED;
            case EAST:
            case WEST:
                return Connection.INPUT;
            default:
                return Connection.NONE;
        }
    }

    private int counter(){
        return isSideInverted(EnumFacing.EAST)? -1: 1;
    }

    @Override
    public boolean tick(PartGate parent) {
        boolean update = parent.updateInputs(this.inputValues);
        boolean updateSignalOnOld = updateSignalOn;
        boolean incSignalOnOld = incSignalOn;
        if (getInputValueInside(EnumFacing.WEST) != 0) {
            if(!updateSignalOn){
                updateSignalOn = true;
                setBundledValue(EnumFacing.NORTH, getInputValueBundled(EnumFacing.SOUTH));
            }
        }else{
            updateSignalOn = false;
        }
        if (getInputValueOutside(EnumFacing.EAST) != 0) {
            if (!incSignalOn) {
                incSignalOn = true;
                setBundledValue(EnumFacing.NORTH, bundledDigiToRs(bundledRsToDigi(getOutputValueBundled(EnumFacing.NORTH)) + counter()));
            }
        } else {
            incSignalOn = false;
        }

        return super.tick(parent) || update || updateSignalOnOld!=updateSignalOn || incSignalOnOld!=incSignalOn;
    }

    @Override
    public State getLayerState(int i) {
        switch (i) {
            case 0: return getInputValueInside(EnumFacing.EAST)!=0? State.ON: State.OFF;
            case 1: return getInputValueInside(EnumFacing.WEST)!=0? State.ON: State.OFF;
            default: return State.ON;
        }
    }

    @Override
    public boolean canInvertSide(EnumFacing side) {
        return side==EnumFacing.EAST;
    }

    @Override
    public State getTorchState(int i) {
        switch (i){
            case 1: return getInputValueInside(EnumFacing.EAST)!=0? State.ON :State.OFF;
            case 0:
            default: return State.ON;
        }
    }
}
