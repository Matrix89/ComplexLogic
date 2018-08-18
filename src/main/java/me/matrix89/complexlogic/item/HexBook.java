package me.matrix89.complexlogic.item;

import me.matrix89.complexlogic.ComplexLogic;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class HexBook extends Item {
    public static final HexBook INSTANCE = new HexBook();

    public HexBook() {
        setRegistryName("hex_book");
        setTranslationKey("complex-logic.hexbook");
        setCreativeTab(CreativeTabs.REDSTONE);
        setMaxStackSize(1);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        return ComplexLogic.PROXY.onHexBookRightClick(worldIn, playerIn, handIn);
    }

}
