package de.matthiasmann.tcbotaniaexoflame.core;

import net.minecraft.launchwrapper.IClassTransformer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

public class TCBotaniaExoflameCoreTransformer implements IClassTransformer {
	@Override
	public byte[] transform(String className, String newClassName, byte[] origCode)
	{
	    if("thaumcraft.common.tiles.TileAlchemyFurnace".equals(className)) {
            LogManager.getLogger("TCBotaniaExoflame").info("Patching 'TileAlchemyFurnace'");
            ClassReader rd = new ClassReader(origCode);
            ClassWriter wr = new ClassWriter(0);
            ClassVisitor patcher = new PatchTileAlchemyFurnace(wr);
            rd.accept(patcher, 0);
            return wr.toByteArray();
	    }
	    
	    return origCode;
	}
}

