package me.matrix89.complexlogic.gate;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import pl.asie.simplelogic.gates.logic.GateConnection;
import pl.asie.simplelogic.gates.logic.GateRenderState;
import pl.asie.simplelogic.gates.logic.IGateContainer;

import java.util.Arrays;

public class ButtonPanelLogic extends BundledGateLogic {
    @Override
    protected boolean shouldSyncBundledWithClient() {
        return true;
    }

    @Override
    public GateConnection getType(EnumFacing dir) {
        return EnumFacing.NORTH == dir ? GateConnection.OUTPUT_BUNDLED : GateConnection.NONE;
    }


    @Override
    public boolean onRightClick(IGateContainer gate, EntityPlayer playerIn, Vec3d vec, EnumHand hand) {
        if (EnumHand.MAIN_HAND != hand) return false;
        Vec3d click = vec.scale(16).add(-2.5, 0, -2.5);
        int clickModX = (int) Math.ceil(click.x % 3);
        int clickModY = (int) Math.ceil(click.z % 3);
        int clickDivX = (int) (click.x / 3);
        int clickDivY = (int) (click.z / 3);
        if (clickModX > 0 && clickModY > 0 && clickModX < 3 && clickModY < 3) {
            int index = clickDivX + clickDivY * 4;
            if (index >= 0 && index < 16) {
                byte[] currentOut = getOutputValueBundled(EnumFacing.NORTH);
                if (currentOut == null)
                    currentOut = new byte[16];
                else
                    currentOut = Arrays.copyOf(currentOut, 16);
                currentOut[index] = (byte) (currentOut[index] > 0 ? 0 : 15);
                boolean isSet = currentOut[index] != 0;
                gate.getGateWorld().playSound(null, gate.getGatePos(), isSet ? SoundEvents.BLOCK_STONE_BUTTON_CLICK_ON : SoundEvents.BLOCK_STONE_BUTTON_CLICK_OFF, SoundCategory.BLOCKS, 0.5f, isSet ? 1.0f : 1.0f);
                setBundledOutputValue(EnumFacing.NORTH, currentOut);
                gate.markGateChanged(true);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean updateOutputs(IGateContainer gate) {
        return false;
    }

    @Override
    public GateRenderState getLayerState(int i) {
        return GateRenderState.OFF;
    }

    @Override
    public GateRenderState getTorchState(int i) {
        return GateRenderState.OFF;
    }
}
