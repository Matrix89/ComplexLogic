package me.matrix89.complexlogic.gate;

import me.matrix89.complexlogic.ComplexLogic;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import pl.asie.simplelogic.gates.PartGate;

public class KeyboardLogic extends BundledGateLogic {
    @Override
    public boolean onRightClick(PartGate gate, EntityPlayer playerIn, Vec3d vec, EnumHand hand) {
        playerIn.openGui(ComplexLogic.INSTANCE, 1, gate.getWorld(), gate.getPos().getX(), gate.getPos().getY(), gate.getPos().getZ());
        return true;
    }

    @Override
    public Connection getType(EnumFacing dir) {
        return dir == EnumFacing.NORTH ? Connection.OUTPUT_BUNDLED : Connection.NONE;
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
