package me.matrix89.complexlogic.gate;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumFacing;
import pl.asie.simplelogic.gates.PartGate;

public class RTCLogic extends BundledGateLogic {
    private boolean updateSignalOn = false;
    @Override
    public Connection getType(EnumFacing dir) {
        switch (dir) {
            case NORTH: return Connection.OUTPUT_BUNDLED;
            case SOUTH: return Connection.INPUT;
            default: return Connection.NONE;
        }
    }

    @Override
    public boolean tick(PartGate parent) {
        if(Minecraft.getMinecraft().world == null) return true;

        if (getInputValueInside(EnumFacing.SOUTH) != 0) {
            if(!updateSignalOn){
                updateSignalOn = true;
                setBundledValue(EnumFacing.NORTH, bundledDigiToRs((int) Minecraft.getMinecraft().world.getWorldTime()));
            }
        }else{
            updateSignalOn = false;
        }

        return super.tick(parent);
    }

    @Override
    public State getLayerState(int i) {
        return getInputValueInside(EnumFacing.SOUTH) != 0 ? State.ON : State.OFF;
    }

    @Override
    public State getTorchState(int i) {
        return State.ON;
    }
}
