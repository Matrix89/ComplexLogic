package me.matrix89.complexlogic.lights;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.asie.charset.lib.utils.ColorUtils;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ColorLampBlock extends Block {
    public static final PropertyBool IS_ON = PropertyBool.create("is_on");
    public static final PropertyDirection FACING = PropertyDirection.create("facing");

    public static Map<Block, Item> LampRegistry = new HashMap<>();

    static {
        for (EnumDyeColor color : EnumDyeColor.values()) {
            Block b = new ColorLampBlock("color_lamp", color, false, true,0);
            Block bi = new ColorLampBlock("color_lamp", color, true,true,0);
            LampRegistry.put(b, new ColorLampItem(b));
            LampRegistry.put(bi, new ColorLampItem(bi));
            //cage lamp
            Block c = new ColorLampBlock("cage_lamp", color, false,false,1);
            Block ci = new ColorLampBlock("cage_lamp", color, true,false,1);
            LampRegistry.put(c, new ColorLampItem(c));
            LampRegistry.put(ci, new ColorLampItem(ci));
            //flat lamp
            Block f = new ColorLampBlock("flat_lamp", color, false,false,2);
            Block fi = new ColorLampBlock("flat_lamp", color, true,false,2);
            LampRegistry.put(f, new ColorLampItem(f));
            LampRegistry.put(fi, new ColorLampItem(fi));
        }
    }

    private final EnumDyeColor color;
    private boolean inverted;
    private boolean isFull;
    private int aabbIndex;

            /*
            DOWN
            UP
            NORTH
            SOUTH
            WEST
            EAST
            */
    public static final AxisAlignedBB[][] boundingBox =
    new AxisAlignedBB[][]{
            new AxisAlignedBB[]{
                    new AxisAlignedBB(0, 0, 0, 1, 1, 1),
                    new AxisAlignedBB(0, 0, 0, 1, 1, 1),
                    new AxisAlignedBB(0, 0, 0, 1, 1, 1),
                    new AxisAlignedBB(0, 0, 0, 1, 1, 1),
                    new AxisAlignedBB(0, 0, 0, 1, 1, 1),
                    new AxisAlignedBB(0, 0, 0, 1, 1, 1)
            },

            new AxisAlignedBB[]{
                    new AxisAlignedBB(0, 0, 0, 1, 1, 1),
                    new AxisAlignedBB(0, 0, 0, 1, 1, 1),
                    new AxisAlignedBB(0, 0, 0, 1, 1, 1),
                    new AxisAlignedBB(0, 0, 0, 1, 1, 1),
                    new AxisAlignedBB(0, 0, 0, 1, 1, 1),
                    new AxisAlignedBB(0, 0, 0, 1, 1, 1)
            },

            new AxisAlignedBB[]{
                    new AxisAlignedBB(0, 12/16f, 0, 1, 1, 1),
                    new AxisAlignedBB(0, 0, 0, 1, 4/16f, 1),
                    new AxisAlignedBB(0, 0, 12/16f, 1, 1, 1),
                    new AxisAlignedBB(0, 0, 0, 1, 1, 4/16f),
                    new AxisAlignedBB(12/16f, 0, 0, 1, 1, 1),
                    new AxisAlignedBB(0, 0, 0, 4/16f, 1, 1)
            }
    };

    public ColorLampBlock(String prefix, EnumDyeColor color, boolean inverted, boolean isFull, int aabbIndex) {
        super(Material.REDSTONE_LIGHT, MapColor.getBlockColor(color));
        setDefaultState(this.blockState.getBaseState().withProperty(IS_ON, false).withProperty(FACING, EnumFacing.DOWN));
        setCreativeTab(CreativeTabs.REDSTONE);
        setTranslationKey(prefix + (inverted?"_inverted":""));
        setRegistryName(prefix + "_" + color.getName() + (inverted?"_inverted":""));
        this.color = color;
        this.inverted = inverted;
        this.isFull = isFull;
        this.aabbIndex = aabbIndex;
    }

    @Override
    public String getLocalizedName() {
        return I18n.translateToLocalFormatted(getTranslationKey() + ".name", I18n.translateToLocal(ColorUtils.getLangEntry("charset.color.", color)));
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
    public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return isFull || base_state.getValue(FACING).getOpposite()==side;
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return state.getValue(FACING).getOpposite() == face?BlockFaceShape.SOLID:BlockFaceShape.UNDEFINED;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        switch (state.getValue(FACING)){
            case SOUTH:
                return boundingBox[aabbIndex][EnumFacing.SOUTH.getIndex()];
            case NORTH:
                return boundingBox[aabbIndex][EnumFacing.NORTH.getIndex()];
            case EAST:
                return boundingBox[aabbIndex][EnumFacing.EAST.getIndex()];
            case WEST:
                return boundingBox[aabbIndex][EnumFacing.WEST.getIndex()];
            case DOWN:
                return boundingBox[aabbIndex][EnumFacing.DOWN.getIndex()];
            case UP:
            default:
                return boundingBox[aabbIndex][EnumFacing.UP.getIndex()];
        }
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return isFull;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return isFull;
    }

    @Override
    public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
        return isFull;
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

    @Override
    public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return super.getMapColor(state, worldIn, pos);
    }
}
