package me.matrix89.complexlogic.lights;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ColorLampItem extends ItemBlock {
    public ColorLampItem(Block block) {
        super(block);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
        this.setRegistryName(block.getRegistryName());
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        if(stack.getMetadata()==2)
            return super.getTranslationKey(stack)+ "_inverted";
        else
            return super.getTranslationKey(stack);
    }
}
