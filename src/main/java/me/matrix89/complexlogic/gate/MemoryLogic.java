package me.matrix89.complexlogic.gate;

import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import me.matrix89.complexlogic.item.HexBook;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import pl.asie.simplelogic.gates.PartGate;
import pl.asie.simplelogic.gates.logic.GateConnection;
import pl.asie.simplelogic.gates.logic.GateRenderState;
import pl.asie.simplelogic.gates.logic.IGateContainer;

import java.util.Map;

public class MemoryLogic extends BundledGateLogic {

    private int address = 0;
    private Map<Integer, Integer> memory = new Int2IntOpenHashMap();
    private boolean updateSignalOn = false;

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag, boolean isClient) {
        if (!isClient) {
            tag.setInteger("address", address);
            NBTTagCompound mapTag = new NBTTagCompound();
            memory.forEach((k, v) -> mapTag.setInteger(k.toString(), v));
            tag.setTag("memory", mapTag);
            tag.setBoolean("updateSignalOn", updateSignalOn);
            tag.setInteger("newAddress", newAddress);
            tag.setInteger("newValue", newValue);
        }
        return super.writeToNBT(tag, isClient);
    }

    @Override
    public boolean onRightClick(IGateContainer gate, EntityPlayer playerIn, Vec3d vec, EnumHand hand) {
        if(playerIn.getHeldItem(EnumHand.MAIN_HAND).isItemEqual(new ItemStack(HexBook.INSTANCE))){
            if(playerIn.isSneaking() && !gate.getGateWorld().isRemote) {
                NBTTagCompound mapTag = new NBTTagCompound();
                memory.forEach((k, v) -> mapTag.setInteger(k.toString(), v));
                playerIn.getHeldItem(EnumHand.MAIN_HAND).setTagCompound(mapTag);
            }else if(!gate.getGateWorld().isRemote){
                memory.clear();
                if(playerIn.getHeldItem(EnumHand.MAIN_HAND).hasTagCompound()) {
                    NBTTagCompound tag = playerIn.getHeldItem(EnumHand.MAIN_HAND).getTagCompound();
                    tag.getKeySet().forEach((k) -> memory.put(Integer.parseInt(k), tag.getInteger(k)));
                }
                gate.markGateChanged(true);
                gate.scheduleRedstoneTick();
            }
            return true;
        }
        return super.onRightClick(gate, playerIn, vec, hand);
    }

    @Override
    public boolean readFromNBT(NBTTagCompound compound, boolean isClient) {
        boolean oldValue = updateSignalOn;
        int newAddressOld = newAddress;
        int newValueOld = newValue;
        if (!isClient) {
            if (compound.hasKey("address")) {
                address = compound.getInteger("address");
            }
            if (compound.hasKey("memory")) {
                NBTTagCompound tag = compound.getCompoundTag("memory");
                tag.getKeySet().forEach((k) -> memory.put(Integer.parseInt(k), tag.getInteger(k)));
            }
            if (compound.hasKey("updateSignalOn")) {
                updateSignalOn = compound.getBoolean("updateSignalOn");
            }
            if (compound.hasKey("newAddress")) {
                newAddress = compound.getInteger("newAddress");
            }
            if (compound.hasKey("newValue")) {
                newValue = compound.getInteger("newValue");
            }
        }
        return super.readFromNBT(compound, isClient) || updateSignalOn != oldValue;
    }

    @Override
    public GateConnection getType(EnumFacing dir) {
        switch (dir) {
            case NORTH:
                return GateConnection.OUTPUT_BUNDLED;
            case EAST:
                return GateConnection.INPUT;
            case WEST:
                return GateConnection.INPUT_BUNDLED;
            case SOUTH:
                return GateConnection.INPUT_BUNDLED;
            default:
                return GateConnection.NONE;
        }
    }

    int newAddress = 0;
    int newValue = 0;

    @Override
    public boolean updateOutputs(IGateContainer gate) {
        address = bundledRsToDigi(getInputValueBundled(EnumFacing.WEST));
        boolean updateSignalOnOld = updateSignalOn;
        if (getInputValueInside(EnumFacing.EAST) != 0) {
            if (!updateSignalOn) {
                updateSignalOn = true;
                int value = bundledRsToDigi(getInputValueBundled(EnumFacing.SOUTH));
                newAddress = address;
                newValue = value;
            }
        } else if(updateSignalOn){
            updateSignalOn = false;
            if (newValue == 0) {
                memory.remove(newAddress);
            } else {
                memory.put(newAddress, newValue);
            }
        }
        setBundledOutputValue(EnumFacing.NORTH, bundledDigiToRs(memory.getOrDefault(address, 0)));
        return true;
    }

    @Override
    public GateRenderState getLayerState(int i) {
        return i == 0 && getInputValueInside(EnumFacing.EAST) != 0 ? GateRenderState.ON : GateRenderState.OFF;
    }

    @Override
    public GateRenderState getTorchState(int i) {
        return GateRenderState.ON;
    }
}
