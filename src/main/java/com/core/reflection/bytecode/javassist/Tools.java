package com.core.reflection.bytecode.javassist;

import com.core.reflection.bytecode.Hello;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;

public class Tools {
    public static void main(String[] args) throws Exception {
        JavassistExmaple2 example2= new JavassistExmaple2();
        example2.modifyMethod();
    }
}
