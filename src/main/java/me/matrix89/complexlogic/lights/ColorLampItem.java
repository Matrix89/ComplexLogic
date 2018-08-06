package me.matrix89.complexlogic.lights;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

public class ColorLampItem extends ItemBlock {
    public ColorLampItem(Block block) {
        super(block);
        this.setMaxDamage(0);
        this.setRegistryName(block.getRegistryName());
    }
}
