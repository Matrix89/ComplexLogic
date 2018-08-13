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

public class TextDisplayLogic extends BundledGateLogic {
    public int codepointClient;

    public EnumDyeColor color = EnumDyeColor.WHITE;

    @Override
    protected boolean shouldSyncBundledWithClient() {
        return true;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag, boolean isClient) {
        super.writeToNBT(tag, isClient);
        tag.setInteger("color", color.getMetadata());
        return tag;
    }

    @Override
    public boolean readFromNBT(NBTTagCompound compound, boolean isClient) {
        EnumDyeColor colorOld = color;
        if(compound.hasKey("color"))
            color = EnumDyeColor.byMetadata(compound.getInteger("color"));

        boolean valueChanged = super.readFromNBT(compound, isClient) || color != colorOld;
        if (valueChanged && isClient) {
            codepointClient = 0;
            byte[] value = getInputValueBundled(EnumFacing.SOUTH);
            if (value != null) {
                for (int i = 0; i < 16; i++) {
                    if (value[i] != 0) {
                        codepointClient |= 1 << i;
                    }
                }
            }
        }
        return valueChanged;
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
    public GateConnection getType(EnumFacing dir) {
         return dir == EnumFacing.SOUTH ? GateConnection.INPUT_BUNDLED : GateConnection.NONE;
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
