package me.matrix89.complexlogic.gate;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.util.Constants;
import pl.asie.charset.lib.utils.ColorUtils;
import pl.asie.simplelogic.gates.PartGate;
import pl.asie.simplelogic.gates.logic.GateConnection;
import pl.asie.simplelogic.gates.logic.GateRenderState;
import pl.asie.simplelogic.gates.logic.IGateContainer;

import java.util.Arrays;

public class SegmentDisplayLogic extends BundledGateLogic {

    public byte[] value = new byte[16];
    public EnumDyeColor color = EnumDyeColor.WHITE;

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag, boolean isClient) {
        super.writeToNBT(tag, isClient);
        if (isClient) {
            byte[] v = getInputValueBundled(EnumFacing.SOUTH);
            if (v != null) {
                tag.setByteArray("v", v);
            }
        }

        tag.setInteger("color", color.getMetadata());
        return tag;
    }

    @Override
    public boolean readFromNBT(NBTTagCompound compound, boolean isClient) {
        byte[] valueOld = value;
        EnumDyeColor colorOld = color;
        if (isClient)
            if (compound.hasKey("v", Constants.NBT.TAG_BYTE_ARRAY))
                value = compound.getByteArray("v");
            else
                value = new byte[16];
        if (compound.hasKey("color")) {
            color = EnumDyeColor.byMetadata(compound.getInteger("color"));
        }

        return super.readFromNBT(compound, isClient) || !Arrays.equals(valueOld, value) || colorOld != color;
    }

    @Override
    public GateConnection getType(EnumFacing dir) {
        return dir == EnumFacing.SOUTH ? GateConnection.INPUT_BUNDLED : GateConnection.NONE;
    }

    @Override
    public boolean onRightClick(IGateContainer gate, EntityPlayer playerIn, Vec3d vec, EnumHand hand) {
        ItemStack item = playerIn.getHeldItemMainhand();
        if (hand != EnumHand.MAIN_HAND || item.isEmpty()) return false;
        if (ColorUtils.isDye(item)) {
            color = ColorUtils.getDyeColor(item);
            return true;
        }

        return false;
    }

    @Override
    public void onChanged(IGateContainer gate) {
        gate.markGateChanged(false);
    }

    @Override
    public GateRenderState getLayerState(int i) {
        return GateRenderState.ON;
    }

    @Override
    public GateRenderState getTorchState(int i) {
        return GateRenderState.ON;
    }
}
