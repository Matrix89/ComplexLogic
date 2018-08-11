package me.matrix89.complexlogic.gate;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import pl.asie.simplelogic.gates.PartGate;

import java.util.Arrays;

public class ButtonPanelLogic extends BundledGateLogic {
    public byte[] value = new byte[16];

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag, boolean isClient) {
        super.writeToNBT(tag, isClient);
        if(isClient)
            tag.setByteArray("v", getOutputValueBundled(EnumFacing.NORTH));
        return tag;
    }

    @Override
    public boolean readFromNBT(NBTTagCompound compound, boolean isClient) {
        byte[] valueOld = value;
        if (isClient && compound.hasKey("v"))
            value = compound.getByteArray("v");
        return super.readFromNBT(compound, isClient) || !Arrays.equals(valueOld, value);
    }

    @Override
    public Connection getType(EnumFacing dir) {
        return EnumFacing.NORTH == dir ? Connection.OUTPUT_BUNDLED : Connection.NONE;
    }

    @Override
    public boolean onRightClick(PartGate gate, EntityPlayer playerIn, Vec3d vec, EnumHand hand) {
        if(EnumHand.MAIN_HAND!=hand) return false;
        Vec3d click = vec.scale(16).add(-2.5,0,-2.5);
        int clickModX = (int)Math.ceil(click.x%3);
        int clickModY = (int)Math.ceil(click.z%3);
        int clickDivX = (int)(click.x/3);
        int clickDivY = (int)(click.z/3);
        if(clickModX>0 && clickModY>0 && clickModX<3 && clickModY<3){
            int index = clickDivX + clickDivY*4;
            if(index>=0 && index<16){
                byte[] currentOut = getOutputValueBundled(EnumFacing.NORTH);
                currentOut[index] = (byte)(currentOut[index]>0?0:15);
                setBundledOutputValue(EnumFacing.NORTH, currentOut);
                forceUpdate();
                gate.scheduleTick();
                return true;
            }
        }
        return false;
    }

    @Override
    void calculateOutput(PartGate parent) {
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
