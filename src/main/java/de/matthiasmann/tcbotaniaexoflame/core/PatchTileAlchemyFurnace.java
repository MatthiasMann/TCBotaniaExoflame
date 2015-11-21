package de.matthiasmann.tcbotaniaexoflame.core;

import java.util.Arrays;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Label;

public class PatchTileAlchemyFurnace extends ClassVisitor implements Opcodes {
    private String owner;

    private static final ObfSafeName worldObj = new ObfSafeName("worldObj", "field_145850_b");
    private static final ObfSafeName isRemote = new ObfSafeName("isRemote", "field_72995_K");
    private static final ObfSafeName markBlockForUpdate = new ObfSafeName("markBlockForUpdate", "func_147471_g");
    private static final ObfSafeName onInventoryChanged = new ObfSafeName("onInventoryChanged", "func_70296_d");
    private static final ObfSafeName xCoord = new ObfSafeName("xCoord", "field_145851_c");
    private static final ObfSafeName yCoord = new ObfSafeName("yCoord", "field_145848_d");
    private static final ObfSafeName zCoord = new ObfSafeName("zCoord", "field_145849_e");
    
    public PatchTileAlchemyFurnace(ClassVisitor cv) {
        super(Opcodes.ASM5, cv);
    }
    
    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        owner = name;
        interfaces = Arrays.copyOf(interfaces, interfaces.length + 1);
        interfaces[interfaces.length-1] = "vazkii/botania/api/item/IExoflameHeatable";
        super.visit(version, access, name, signature, superName, interfaces);
    }
    
    @Override
    public void visitEnd() {
        // generates "public int getBurnTime()" method
        MethodVisitor gv = cv.visitMethod(ACC_PUBLIC, "getBurnTime", "()I", null, null);
        gv.visitVarInsn(ALOAD, 0);
        gv.visitFieldInsn(GETFIELD, owner, "furnaceBurnTime", "I");
        gv.visitInsn(IRETURN);
        gv.visitMaxs(2, 1);
        gv.visitEnd();
        
        // generate "public void boostBurnTime()" method
        gv = cv.visitMethod(ACC_PUBLIC, "boostBurnTime", "()V", null, null);
        gv.visitVarInsn(ALOAD, 0);
        gv.visitFieldInsn(GETFIELD, owner, worldObj.getName(), "Lnet/minecraft/world/World;");
        gv.visitFieldInsn(GETFIELD, "net/minecraft/world/World", isRemote.getName(), "Z");
        Label end = new Label();
        gv.visitJumpInsn(IFNE, end);
        gv.visitVarInsn(ALOAD, 0);
        gv.visitVarInsn(ALOAD, 0);
        gv.visitFieldInsn(GETFIELD, owner, "smeltTime", "I");
        gv.visitInsn(ICONST_1);
        gv.visitInsn(IADD);
        gv.visitFieldInsn(PUTFIELD, owner, "furnaceBurnTime", "I");
        gv.visitVarInsn(ALOAD, 0);
        gv.visitFieldInsn(GETFIELD, owner, worldObj.getName(), "Lnet/minecraft/world/World;");
        gv.visitVarInsn(ALOAD, 0);
        gv.visitFieldInsn(GETFIELD, owner, xCoord.getName(), "I");
        gv.visitVarInsn(ALOAD, 0);
        gv.visitFieldInsn(GETFIELD, owner, yCoord.getName(), "I");
        gv.visitVarInsn(ALOAD, 0);
        gv.visitFieldInsn(GETFIELD, owner, zCoord.getName(), "I");
        gv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/world/World", markBlockForUpdate.getName(), "(III)V", false);
        gv.visitVarInsn(ALOAD, 0);
        gv.visitMethodInsn(INVOKEVIRTUAL, owner, onInventoryChanged.getName(), "()V", false);
        gv.visitLabel(end); 
        gv.visitInsn(RETURN);
        gv.visitMaxs(5, 1);
        gv.visitEnd();
        
        // generate "public void boostCookTime()" method
        gv = cv.visitMethod(ACC_PUBLIC, "boostCookTime", "()V", null, null);
        gv.visitInsn(RETURN);
        gv.visitMaxs(1, 1);
        gv.visitEnd();
        
        // generate "public bool canSmelt()" method
        gv = cv.visitMethod(ACC_PUBLIC, "canSmelt", "()Z", null, null);
        gv.visitVarInsn(ALOAD, 0);
        gv.visitFieldInsn(GETFIELD, owner, "furnaceBurnTime", "I");
        Label notBurning = new Label();
        gv.visitJumpInsn(IFLE, notBurning);
        gv.visitInsn(ICONST_0);
        gv.visitInsn(IRETURN);
        gv.visitLabel(notBurning);
        gv.visitVarInsn(ALOAD, 0);
        gv.visitMethodInsn(INVOKESPECIAL, owner, "canSmeltTC", "()Z", false);
        gv.visitInsn(IRETURN);
        gv.visitMaxs(2, 1);
        gv.visitEnd();
        
        super.visitEnd();
    }
    
    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        name = replaceCanSmelt(name, desc);
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
        if(mv != null) {
            mv = new MethodCallAdapter(mv, owner);
        }
        return mv;
    }
    
    static String replaceCanSmelt(String name, String desc) {
        if("canSmelt".equals(name) && "()Z".equals(desc)) {
            return "canSmeltTC";
        }
        return name;
    }
    
    static class MethodCallAdapter extends MethodVisitor implements Opcodes {
        private final String owner;
        
        MethodCallAdapter(MethodVisitor mv, String owner) {
            super(Opcodes.ASM5, mv);
            this.owner = owner;
        }

        @Override
        public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
            if (owner.equals(this.owner)) {
                name = replaceCanSmelt(name, desc);
            }
            super.visitMethodInsn(opcode, owner, name, desc, itf);
        }
    }
}
