package de.matthiasmann.tcbotaniaexoflame.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Label;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodNode;

public class PatchBlockCake extends ClassVisitor implements Opcodes {
    private String owner;
    private boolean hasComparatorSupport;

    private static final ObfSafeName hasComparatorInputOverride = new ObfSafeName("hasComparatorInputOverride", "func_149740_M");
    private static final ObfSafeName getComparatorInputOverride = new ObfSafeName("getComparatorInputOverride", "func_149736_g");
    private static final ObfSafeName getBlockMetadata = new ObfSafeName("getBlockMetadata", "func_72805_g");
    private static final ObfSafeName setBlockMetadataWithNotify = new ObfSafeName("setBlockMetadataWithNotify", "func_72921_c");
    
    public PatchBlockCake(ClassVisitor cv) {
        super(Opcodes.ASM5, cv);
    }
    
    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        owner = name;
        super.visit(version, access, name, signature, superName, interfaces);
    }
    
    @Override
    public void visitEnd() {
        if(!hasComparatorSupport) {
            // generates "public int hasComparatorInputOverride()" method
            MethodVisitor gv = cv.visitMethod(ACC_PUBLIC, hasComparatorInputOverride.getName(), "()Z", null, null);
            gv.visitInsn(ICONST_1);
            gv.visitInsn(IRETURN);
            gv.visitMaxs(2, 1);
            gv.visitEnd();
            
            // generates "public int getComparatorInputOverride()" method
            gv = cv.visitMethod(ACC_PUBLIC, getComparatorInputOverride.getName(), "(Lnet/minecraft/world/World;IIII)I", null, null);
            gv.visitIntInsn(BIPUSH, 14);
            gv.visitVarInsn(ALOAD, 1);
            gv.visitVarInsn(ILOAD, 2);
            gv.visitVarInsn(ILOAD, 3);
            gv.visitVarInsn(ILOAD, 4);
            gv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/world/World", getBlockMetadata.getName(), "(III)I", false);
            gv.visitInsn(ICONST_2);
            gv.visitInsn(IMUL);
            gv.visitInsn(ISUB);
            gv.visitIntInsn(BIPUSH, 15);
            gv.visitInsn(IAND);
            gv.visitInsn(IRETURN);
            gv.visitMaxs(6, 1);
            gv.visitEnd();
        } else
            LogManager.getLogger("TCBotaniaExoflame").info("'BlockCake' already has Redstone Comparator support");
        super.visitEnd();
    }
    
    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        if((hasComparatorInputOverride.matches(name) && "()Z".equals(desc)) ||
           (getComparatorInputOverride.matches(name) && "(Lnet/minecraft/world/World;IIII)I".equals(desc))) {
            hasComparatorSupport = true;
        }
        //LogManager.getLogger("TCBotaniaExoflame").info("Found method '" + name + "' with desc '" + desc + "'");
        if("func_150036_b".equals(name) && "(Lnet/minecraft/world/World;IIILnet/minecraft/entity/player/EntityPlayer;)V".equals(desc)) {
            LogManager.getLogger("TCBotaniaExoflame").info("Trying to patch 'eatCake' method");
            return new EatCakeMethodPatcher(cv, access, name, desc, signature, exceptions);
        }
        return cv.visitMethod(access, name, desc, signature, exceptions);
    }
    
    static class EatCakeMethodPatcher extends MethodNode implements Opcodes {
        private final ClassVisitor cv;
        
        EatCakeMethodPatcher(ClassVisitor cv, int access, String name, String desc, String signature, String[] exceptions) {
            super(Opcodes.ASM5, access, name, desc, signature, exceptions);
            this.cv = cv;
        }
        
        @Override
        public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
            if(setBlockMetadataWithNotify.matches(name) && "(IIIII)Z".equals(desc)) {
                AbstractInsnNode last = instructions.getLast();
                if(last != null && last.getOpcode() == ICONST_2) {
                    instructions.set(last, new InsnNode(ICONST_3));
                    LogManager.getLogger("TCBotaniaExoflame").info("patched flags for setBlockMetadataWithNotify call");
                }
            }
            super.visitMethodInsn(opcode, owner, name, desc, itf);
        }
        
        @Override
        public void visitEnd() {
            super.visitEnd();
            accept(cv);
        }
    }
}

