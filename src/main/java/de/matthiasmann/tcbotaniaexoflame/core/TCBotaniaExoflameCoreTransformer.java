package de.matthiasmann.tcbotaniaexoflame.core;

import net.minecraft.launchwrapper.IClassTransformer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

public class TCBotaniaExoflameCoreTransformer implements IClassTransformer {
	@Override
	public byte[] transform(String className, String transformedName, byte[] origCode)
	{
	    if("thaumcraft.common.tiles.TileAlchemyFurnace".equals(className)) {
            LogManager.getLogger("TCBotaniaExoflame").info("Patching 'TileAlchemyFurnace'");
            ClassReader rd = new ClassReader(origCode);
            ClassWriter wr = new ClassWriter(0);
            ClassVisitor patcher = new PatchTileAlchemyFurnace(wr);
            rd.accept(patcher, 0);
            return wr.toByteArray();
	    }
	    
	    if("tuhljin.automagy.tiles.TileEntityBoiler".equals(className)) {
	        // TileEntityBoiler uses the exact same fields and methods as TileAlchemyFurnace
            LogManager.getLogger("TCBotaniaExoflame").info("Patching 'TileEntityBoiler'");
            ClassReader rd = new ClassReader(origCode);
            ClassWriter wr = new ClassWriter(0);
            ClassVisitor patcher = new PatchTileAlchemyFurnace(wr);
            rd.accept(patcher, 0);
            return wr.toByteArray();
	    }
	    
	    if("net.minecraft.block.BlockCake".equals(transformedName)) {
            LogManager.getLogger("TCBotaniaExoflame").info("Patching 'BlockCake'");
            ClassReader rd = new ClassReader(origCode);
            ClassWriter wr = new ClassWriter(0);
            ClassVisitor patcher = new PatchBlockCake(wr);
            rd.accept(patcher, 0);
            return wr.toByteArray();
	    }
	    
	    return origCode;
	}
}

