package me.matrix89.complexlogic.lights;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import javax.annotation.Nullable;

public class LampBlockColorHandler implements IBlockColor, IItemColor {
    @Override
    public int colorMultiplier(IBlockState state, @Nullable IBlockAccess worldIn, @Nullable BlockPos pos, int tintIndex) {
        return state.getMapColor(worldIn,pos).colorValue;
    }

    @Override
    public int colorMultiplier(ItemStack stack, int tintIndex) {
        Block b = ((ItemBlock)stack.getItem()).getBlock();
        return b.getMapColor(null, null, null).colorValue;
    }
}
