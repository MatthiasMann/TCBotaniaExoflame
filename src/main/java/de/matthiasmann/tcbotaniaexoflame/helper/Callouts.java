package de.matthiasmann.tcbotaniaexoflame.helper;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBookshelf;
import net.minecraft.init.Blocks;

public class Callouts {

    public static boolean isBookshelf(Block b) {
        if(b == null)
            return false;
        if((b == Blocks.bookshelf) || (b instanceof BlockBookshelf))
            return true;
        String name = b.getClass().getName();
        return "team.chisel.block.BlockCarvableBookshelf".equals(name) ||
            "tuhljin.automagy.blocks.BlockBookshelfEnchanted".equals(name);
    }
    
}