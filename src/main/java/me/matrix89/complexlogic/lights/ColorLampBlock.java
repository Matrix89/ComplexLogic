package me.matrix89.complexlogic.lights;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.Map;

public class ColorLampBlock extends Block {
    public static final PropertyBool IS_ON = PropertyBool.create("is_on");
    public static final PropertyBool INVERTED = PropertyBool.create("inverted");

    public static Map<Block, Item> LampRegistry = new HashMap<>();

    static {
        for (EnumDyeColor color : EnumDyeColor.values()) {
            Block b = new ColorLampBlock(color);
            LampRegistry.put(b, new ColorLampItem(b));
        }
    }

    public ColorLampBlock(EnumDyeColor color) {
        super(Material.REDSTONE_LIGHT, MapColor.getBlockColor(color));
        setDefaultState(this.blockState.getBaseState().withProperty(INVERTED, false).withProperty(IS_ON, false));
        setCreativeTab(CreativeTabs.REDSTONE);
        setTranslationKey("color_lamp_" + color.getTranslationKey());
        setRegistryName("color_lamp_" + color.getName());
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        items.add(new ItemStack(this, 1, 0));
        items.add(new ItemStack(this, 1, 2));
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (worldIn.isRemote) return;
        if (worldIn.isBlockPowered(pos)) {
            worldIn.setBlockState(pos, state.withProperty(IS_ON, true), 2);
        } else {
            worldIn.setBlockState(pos, state.withProperty(IS_ON, false), 2);
        }
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        if (state.getValue(INVERTED)) {
            return state.getValue(IS_ON) ? 0 : 15;
        } else {
            return state.getValue(IS_ON) ? 15 : 0;
        }
    }

    @Override
    public BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, IS_ON, INVERTED);
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        if (worldIn.isRemote) return;
        if (worldIn.isBlockPowered(pos)) {
            worldIn.setBlockState(pos, state.withProperty(IS_ON, true), 2);
        } else {
            worldIn.setBlockState(pos, state.withProperty(IS_ON, false), 2);
        }
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    public int damageDropped(IBlockState state) {
        return state.getValue(INVERTED) ? 2 : 0;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return (state.getValue(IS_ON) ? 1 : 0) | (state.getValue(INVERTED) ? 2 : 0);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(IS_ON, (meta & 1) == 1).withProperty(INVERTED, (meta & 2) == 2);
    }


}
