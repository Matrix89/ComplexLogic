package me.matrix89.complexlogic;

import me.matrix89.complexlogic.gate.KeyboardLogic;
import me.matrix89.complexlogic.gate.PatchPanelLogic;
import me.matrix89.complexlogic.gui.KeyboardContainer;
import me.matrix89.complexlogic.gui.KeyboardGUI;
import me.matrix89.complexlogic.gui.PatchPanelContainer;
import me.matrix89.complexlogic.gui.PatchPanelGUI;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import pl.asie.simplelogic.gates.PartGate;

import javax.annotation.Nullable;

public class GUIHandler implements IGuiHandler {
    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
        if (ID == 0 && te instanceof PartGate) {
            PartGate pg = (PartGate) te;
            if (pg.logic instanceof PatchPanelLogic) {
                PatchPanelLogic logic = (PatchPanelLogic) pg.logic;
                return new PatchPanelContainer(player.inventory, logic, pg);
            }
        }
        if (ID == 1 && te instanceof PartGate) {
            PartGate pg = (PartGate) te;
            if (pg.logic instanceof KeyboardLogic) {
                KeyboardLogic logic = (KeyboardLogic) pg.logic;
                return new KeyboardContainer(player.inventory, logic, pg);
            }
        }
        return null;
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
        if (ID == 0 && te instanceof PartGate) {
            PartGate pg = (PartGate) te;
            if (pg.logic instanceof PatchPanelLogic) {
                PatchPanelLogic logic = (PatchPanelLogic) pg.logic;
                return new PatchPanelGUI(new PatchPanelContainer(player.inventory, logic, pg));
            }
        }
        if (ID == 1 && te instanceof PartGate) {
            PartGate pg = (PartGate) te;
            if (pg.logic instanceof KeyboardLogic) {
                KeyboardLogic logic = (KeyboardLogic) pg.logic;
                return new KeyboardGUI(new KeyboardContainer(player.inventory, logic, pg));
            }
        }
        return null;
    }
}
