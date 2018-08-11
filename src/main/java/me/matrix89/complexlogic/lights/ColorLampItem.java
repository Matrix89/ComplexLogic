package me.matrix89.complexlogic.lights;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;

public class ColorLampItem extends ItemBlock {
    public ColorLampItem(Block block) {
        super(block);
        this.setMaxDamage(0);
        this.setRegistryName(block.getRegistryName());
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return this.block.getLocalizedName();
    }
}
