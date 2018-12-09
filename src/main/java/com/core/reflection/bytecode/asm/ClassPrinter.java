package com.core.reflection.bytecode.asm;

import org.objectweb.asm.*;

import java.io.IOException;

public class ClassPrinter extends ClassVisitor {
    static int ASM4 = 4 << 16 | 0 << 8 | 0;
    static int ASM5 = 5 << 16 | 0 << 8 | 0;
    public ClassPrinter() {
        super(ASM5);
    }

    @Override
    public void visit(int version, int access, String name,
                      String signature, String superName, String[] interfaces) {
        System.out.println(name + " extends " + superName + " {");
    }

    public void visitSource(String source, String debug) {
    }
    public void visitOuterClass(String owner, String name, String desc) {
    }
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        return null;
    }
    public void visitAttribute(Attribute attr) {
    }
    public void visitInnerClass(String name, String outerName, String innerName, int access) {
    }
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        System.out.println(" " + desc + " " + name);
        return null;
    }
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        System.out.println(" " + name + desc);
        return null;
    }
    public void visitEnd() {
        System.out.println("}");
    }

    public static void main(String[] args) throws IOException {
        ClassPrinter printer = new ClassPrinter();
        ClassReader reader = new ClassReader("java.lang.Runnable");
        reader.accept(printer,0);


        String x ="version, access, name, signature, superName, interfaces";
        ClassWriter writer = new ClassWriter(0);
        writer.visit(Opcodes.V1_8,Opcodes.ACC_PUBLIC+Opcodes.ACC_ABSTRACT+Opcodes.ACC_INTERFACE,
                "com/core/reflection/bytecode/Comparator",null,"java/lang/Object",
                new String[]{"com/core/reflection/bytecode/Measurable"});
        writer.visitField(Opcodes.ACC_PUBLIC+Opcodes.ACC_FINAL+Opcodes.ACC_STATIC,"LESS","I",
                null,new Integer(-1)).visitEnd();
        writer.visitField(Opcodes.ACC_PUBLIC+Opcodes.ACC_FINAL+Opcodes.ACC_STATIC,"EQUAL","I",
                null,new Integer(0)).visitEnd();
        writer.visitField(Opcodes.ACC_PUBLIC+Opcodes.ACC_FINAL+Opcodes.ACC_STATIC,"GREATER","I",
                null,new Integer(1)).visitEnd();
        writer.visitMethod(Opcodes.ACC_PUBLIC+Opcodes.ACC_ABSTRACT,"compareTo","(Ljava/lang/Object;)I",
                null,null).visitEnd();
        writer.visitEnd();
        byte[] b = writer.toByteArray();



    }
}
