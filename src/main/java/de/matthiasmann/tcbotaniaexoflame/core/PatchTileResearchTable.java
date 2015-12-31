package de.matthiasmann.tcbotaniaexoflame.core;

import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Label;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.MethodInsnNode;

public class PatchTileResearchTable extends ClassVisitor implements Opcodes {
    private String owner;

    private static final ObfSafeName bookshelf = new ObfSafeName("bookshelf", "field_150342_X");
    
    public PatchTileResearchTable(ClassVisitor cv) {
        super(Opcodes.ASM5, cv);
    }
    
    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        owner = name;
        super.visit(version, access, name, signature, superName, interfaces);
    }
    
    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        if("recalculateBonus".equals(name) && "()V".equals(desc)) {
            LogManager.getLogger("TCBotaniaExoflame").info("Trying to patch 'recalculateBonus' method");
            return new RecalcBonusPatcher(cv, access, name, desc, signature, exceptions);
        }
        return cv.visitMethod(access, name, desc, signature, exceptions);
    }
    
    static class RecalcBonusPatcher extends MethodNode implements Opcodes {
        private final ClassVisitor cv;
        
        RecalcBonusPatcher(ClassVisitor cv, int access, String name, String desc, String signature, String[] exceptions) {
            super(Opcodes.ASM5, access, name, desc, signature, exceptions);
            this.cv = cv;
        }
        
        @Override
        public void visitJumpInsn(int opcode, Label label) {
            AbstractInsnNode last = instructions.getLast();
            if(opcode == IF_ACMPNE && last != null && last.getOpcode() == GETSTATIC) {
                FieldInsnNode lastFIN = (FieldInsnNode)last;
                if(bookshelf.matches(lastFIN.name) && "net/minecraft/init/Blocks".equals(lastFIN.owner)) {
                    instructions.set(last, new MethodInsnNode(INVOKESTATIC, "de/matthiasmann/tcbotaniaexoflame/helper/Callouts", "isBookshelf", "(Lnet/minecraft/block/Block;)Z", false));
                    opcode = IFEQ;
                    LogManager.getLogger("TCBotaniaExoflame").info("patched check for BlockBookshelf");
                }
            }
            super.visitJumpInsn(opcode, label);
        }
        
        @Override
        public void visitEnd() {
            super.visitEnd();
            accept(cv);
        }
    }
}