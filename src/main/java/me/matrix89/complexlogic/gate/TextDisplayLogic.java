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

public class TextDisplayLogic extends BundledGateLogic {

    public byte[] value = new byte[16];
    public int codepointClient;

    public EnumDyeColor color = EnumDyeColor.WHITE;

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag, boolean isClient) {
        super.writeToNBT(tag, isClient);
        tag.setByteArray("v", getInputValueBundled(EnumFacing.SOUTH));
        tag.setInteger("color", color.getMetadata());
        return tag;
    }

    @Override
    public boolean readFromNBT(NBTTagCompound compound, boolean isClient) {
        byte[] valueOld = value;
        EnumDyeColor colorOld = color;
        if (compound.hasKey("v"))
            value = compound.getByteArray("v");

        if(compound.hasKey("color"))
            color = EnumDyeColor.byMetadata(compound.getInteger("color"));

        boolean valueChanged = !Arrays.equals(valueOld, value) || color != colorOld;
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
