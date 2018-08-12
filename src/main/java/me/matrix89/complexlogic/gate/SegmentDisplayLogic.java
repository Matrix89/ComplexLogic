package me.matrix89.complexlogic.gate;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import pl.asie.charset.lib.utils.ColorUtils;
import pl.asie.simplelogic.gates.PartGate;

import java.util.Arrays;

public class SegmentDisplayLogic extends BundledGateLogic {

    public byte[] value = new byte[16];
    public EnumDyeColor color = EnumDyeColor.WHITE;

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag, boolean isClient) {
        super.writeToNBT(tag, isClient);
        if (isClient) {
            tag.setByteArray("v", getInputValueBundled(EnumFacing.SOUTH));
        }

        tag.setInteger("color", color.getMetadata());
        return tag;
    }

    @Override
    public boolean readFromNBT(NBTTagCompound compound, boolean isClient) {
        byte[] valueOld = value;
        EnumDyeColor colorOld = color;
        if (isClient && compound.hasKey("v"))
            value = compound.getByteArray("v");
        if (compound.hasKey("color")) {
            color = EnumDyeColor.byMetadata(compound.getInteger("color"));
        }

        return super.readFromNBT(compound, isClient) || !Arrays.equals(valueOld, value) || colorOld != color;
    }

    @Override
    public Connection getType(EnumFacing dir) {
        return dir == EnumFacing.SOUTH ? Connection.INPUT_BUNDLED : Connection.NONE;
    }

    @Override
    public boolean onRightClick(PartGate gate, EntityPlayer playerIn, Vec3d vec, EnumHand hand) {
        ItemStack item = playerIn.getHeldItemMainhand();
        if (hand != EnumHand.MAIN_HAND || item.isEmpty()) return false;
        if (ColorUtils.isDye(item)) {
            color = ColorUtils.getDyeColor(item);
            return true;
        }

        return false;
    }

    @Override
    void calculateOutput(PartGate parent) {
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
