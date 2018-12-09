package com.core.reflection.bytecode.javassist;

import com.core.reflection.bytecode.Hello;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;

public class MyClassLoader extends ClassLoader {
    private ClassPool pool;

    public MyClassLoader() {
        try {
            this.pool = new ClassPool();
            // 必须在这个路径下的类才能被加载
            pool.insertClassPath("E:\\code\\ecom-review-core\\target\\classes\\com\\core\\reflection\\bytecode");
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            CtClass cc = pool.get(name);
            byte[] b = cc.toBytecode();
            return defineClass(name, b, 0, b.length);
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (CannotCompileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return super.findClass(name);
    }

    public static void main(String[] args) throws Exception {
        MyClassLoader s = new MyClassLoader();
        Class c = s.loadClass("com.core.reflection.bytecode.Hello");
        Method m = c.getDeclaredMethod("say", new Class[] { String.class });
        Hello hello = (Hello)c.newInstance();
        m.invoke(hello, new Object[] { "成都" });
    }
}
