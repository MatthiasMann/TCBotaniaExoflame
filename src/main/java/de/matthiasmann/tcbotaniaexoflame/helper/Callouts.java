package de.matthiasmann.tcbotaniaexoflame.helper;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBookshelf;
import net.minecraft.init.Blocks;

import team.chisel.block.BlockCarvableBookshelf;

import tuhljin.automagy.blocks.BlockBookshelfEnchanted;

public class Callouts {

    public static boolean isBookshelf(Block b) {
        return (b == Blocks.bookshelf) || (b instanceof BlockBookshelf) || (b instanceof BlockCarvableBookshelf) || (b instanceof BlockBookshelfEnchanted);
    }
    
}