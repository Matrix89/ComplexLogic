package me.matrix89.complexlogic.lights;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
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
    public static final PropertyDirection FACING = PropertyDirection.create("facing");

    public static Map<Block, Item> LampRegistry = new HashMap<>();

    static {
        for (EnumDyeColor color : EnumDyeColor.values()) {
            Block b = new ColorLampBlock("color_lamp_", color, false);
            Block bi = new ColorLampBlock("color_lamp_", color, true);
            LampRegistry.put(b, new ColorLampItem(b));
            LampRegistry.put(bi, new ColorLampItem(bi));
        }
        Block b = new ColorLampBlock("cage_lamp_", EnumDyeColor.WHITE, false);
        LampRegistry.put(b, new ColorLampItem(b));
    }

    private boolean inverted;

    public ColorLampBlock(String prefix,EnumDyeColor color, boolean inverted) {
        super(Material.REDSTONE_LIGHT, MapColor.getBlockColor(color));
        setDefaultState(this.blockState.getBaseState().withProperty(IS_ON, false).withProperty(FACING, EnumFacing.DOWN));
        setCreativeTab(CreativeTabs.REDSTONE);
        setTranslationKey(prefix + color.getTranslationKey() + (inverted?"_inverted":""));
        setRegistryName(prefix + color.getName() + (inverted?"_inverted":""));
        this.inverted = inverted;
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        items.add(new ItemStack(this, 1, 0));
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
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand).withProperty(FACING, facing);
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        if (inverted) {
            return state.getValue(IS_ON) ? 0 : 15;
        } else {
            return state.getValue(IS_ON) ? 15 : 0;
        }
    }

    @Override
    public BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, IS_ON, FACING);
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

    @Override
    public int getMetaFromState(IBlockState state) {
        return (state.getValue(IS_ON) ? 1 : 0)  | (state.getValue(FACING).getIndex()<<1);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FACING, EnumFacing.byIndex(meta >> 1)).withProperty(IS_ON, (meta & 1) == 1);
    }


}
