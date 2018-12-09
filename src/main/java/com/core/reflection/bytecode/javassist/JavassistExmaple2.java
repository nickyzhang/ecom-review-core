package com.core.reflection.bytecode.javassist;

import com.core.reflection.bytecode.Hello;
import com.core.reflection.bytecode.Rectangle;
import javassist.*;

import java.lang.reflect.Method;

public class JavassistExmaple2 {
    public void getClassInfo() throws Exception {
        ClassPool pool = ClassPool.getDefault();
        CtClass cc = pool.makeClass("com.core.reflection.bytecode.Rectangle");
        //得到字节码
        byte[] bytes = cc.toBytecode();
        System.out.println(cc.getName());//获取类名
        System.out.println(cc.getSimpleName());//获取简要类名
        System.out.println(cc.getSuperclass());//获取父类
        System.out.println(cc.getInterfaces());//获取接口
        System.out.println(cc.getMethods());//获取
    }

    // 创建新的方法
    public void makeNewMethod() throws Exception {
        ClassPool pool = ClassPool.getDefault();
        CtClass cc = pool.makeClass("com.core.reflection.bytecode.Rectangle");

        CtMethod cm = new CtMethod(CtClass.intType,"add",new CtClass[]{CtClass.intType,CtClass.intType},cc);
        cm.setModifiers(Modifier.PUBLIC);
        cm.setBody("{return $1 + $2;}");
        cc.addMethod(cm);

        // 通过反射调用方法
        Class c = cc.toClass();
        Rectangle rectangle = (Rectangle)c.newInstance();
        Method m = c.getMethod("add",new Class[]{int.class,int.class});
        Object res = m.invoke(rectangle,new Object[]{10,20});
        System.out.println(res.toString());
    }

    // 修改已有的方法
    public void modifyMethod() throws Exception {
        ClassPool pool = ClassPool.getDefault();
        CtClass cc = pool.get("com.core.reflection.bytecode.Hello");
        CtMethod cm = cc.getDeclaredMethod("say",new CtClass[]{pool.get("java.lang.String")});

        cm.insertBefore("System.out.println(\"修改方法体内容\");");
        cm.insertAfter("System.out.println(\"方法体内容完毕\");");

        // 通过反射调用方法
        Class c = cc.toClass();
        Hello hello = (Hello)c.newInstance();
        Method m = c.getMethod("say",String.class);
        m.invoke(hello,new Object[]{"成都"});
    }

    // 修改已有属性

    public void modifyField() throws Exception {
        ClassPool pool = ClassPool.getDefault();
        CtClass cc = pool.get("com.core.reflection.bytecode.Rectangle");
        // 属性
        CtField descF = new CtField(pool.get("java.lang.String"),"desc",cc);
        CtField perimeterF = new CtField(CtClass.intType,"perimeter",cc);
        CtField acreageF = new CtField(CtClass.floatType,"acreage",cc);
        acreageF.setModifiers(Modifier.PRIVATE);

    }
}
