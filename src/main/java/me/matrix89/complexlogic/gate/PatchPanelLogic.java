package me.matrix89.complexlogic.gate;

import me.matrix89.complexlogic.ComplexLogic;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import pl.asie.simplelogic.gates.PartGate;
import pl.asie.simplelogic.gates.logic.GateConnection;
import pl.asie.simplelogic.gates.logic.GateRenderState;
import pl.asie.simplelogic.gates.logic.IGateContainer;

public class PatchPanelLogic extends BundledGateLogic {
    byte[][] GateConnectionGrid = new byte[16][16];

    public void setGateConnectionGrid(byte[][] GateConnectionGrid){
        this.GateConnectionGrid = GateConnectionGrid;
    }

    public byte[][] getGateConnectionGrid(){
        return GateConnectionGrid;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag, boolean isClient) {
        if(!isClient){
            NBTTagCompound array = new NBTTagCompound();
            for (int i = 0; i < 16; i++) {
                array.setByteArray(String.valueOf(i), GateConnectionGrid[i]);
            }
            tag.setTag("GateConnectionGrid", array);
        }
        return super.writeToNBT(tag, isClient);
    }

    @Override
    public boolean readFromNBT(NBTTagCompound compound, boolean isClient) {
        if(compound.hasKey("GateConnectionGrid")){
            NBTTagCompound array = compound.getCompoundTag("GateConnectionGrid");
            for (int i = 0; i < 16; i++) {
                GateConnectionGrid[i] = array.getByteArray(String.valueOf(i));
            }
        }

        return super.readFromNBT(compound, isClient);
    }

    @Override
    public GateConnection getType(EnumFacing dir) {
        switch (dir){
            case SOUTH: return GateConnection.INPUT_BUNDLED;
            case NORTH: return GateConnection.OUTPUT_BUNDLED;
            default: return GateConnection.NONE;
        }
    }

    @Override
    public boolean onRightClick(IGateContainer gate, EntityPlayer player, Vec3d vec, EnumHand hand) {
        if (!player.isSneaking() && EnumHand.MAIN_HAND==hand) {
            player.openGui(ComplexLogic.INSTANCE, 0, gate.getGateWorld(), gate.getGatePos().getX(), gate.getGatePos().getY(), gate.getGatePos().getZ());
            return true;
        }
        return false;
    }

    @Override
    public void onChanged(IGateContainer gate) {
        byte[] in = getInputValueBundled(EnumFacing.SOUTH);
        byte[] out = new byte[16];
        if (in != null) {
            for (int i = 0; i < 16; i++) {
                byte value = in[i];
                for (int j = 0; j < 16; j++) {
                    if (GateConnectionGrid[i][j] != 0) {
                        out[j] = (byte) (out[j] | value);
                    }
                }
            }
        }
        setBundledOutputValue(EnumFacing.NORTH, out);
        gate.markGateChanged(true);
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
