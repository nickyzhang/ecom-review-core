package com.core.reflection;

import com.core.reflection.access.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.List;
import java.util.Map;

public class ClassParser {
    public static Class forName(String path) {
        Class clazz = null;
        try {
            clazz  = Class.forName(path);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return clazz;
    }

    public static Class dotGetClass(Object obj) {

        return obj.getClass();
    }
    // 获取类的构造器
    public void parseConstructors() {
        Class<Service> clazz = forName("com.core.reflection.access.ServiceImpl");
        // 获取指定的Class对象中所有的public构造器，即可访问的，private的构造器就不能返回
        Constructor<?>[] constructors = clazz.getConstructors();
        // 获取指定的Class对象中所有的构造器，不管可不可以访问，比如private也可以返回
        Constructor<?>[] decalredConstructors = clazz.getDeclaredConstructors();
        // 获取指定参数类型的构造器，需要有可访问权限
        Constructor c = null;
        // 获取指定参数类型的构造器，无关访问权限
        Constructor dc = null;
        try {
            c = clazz.getConstructor(Mapper.class);
            dc = clazz.getDeclaredConstructor(Mapper.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }




    public void parseField() {
        Class<Service> clazz = forName("com.core.reflection.access.ServiceImpl");
        // 获取所有可以访问的字段
        Field[] fields = clazz.getFields();
        // 获取所有字段,跟访问权限没有关系
        Field[] declaredFields = clazz.getDeclaredFields();
        try {
            // 获取指定名字的字段，并且该字段有权限访问
            Field field = clazz.getField("mapper");
            // 获取指定名字的字段，跟访问权限没有关系
            Field declaredField = clazz.getDeclaredField("mapper");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void parseMethod() {
        Class<Service> clazz = forName("com.core.reflection.access.ServiceImpl");
        // 获取所有可以访问的方法
        Method[] methods = clazz.getMethods();
        // 获取所有方法,跟访问权限没有关系
        Method[] declaredMethods = clazz.getDeclaredMethods();
        try {
            // 获取指定名字的方法，并且该方法有权限访问
            Method method = clazz.getMethod("list",String.class,String.class,Integer.class);
            // 获取指定名字的方法，跟访问权限没有关系
            Method declaredMethod = clazz.getDeclaredMethod("mapper",Mapper.class,String.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void parseAnnotation() {
        Class<Service> clazz = forName("com.core.reflection.access.ServiceImpl");
        // 获取所有有权限访问的注解
        Annotation[] annotations = clazz.getAnnotations();
        // 获取所有注解，有无权限都要返回
        Annotation[] declaredAnnotations = clazz.getDeclaredAnnotations();
        // 获取指定类型有权限访问的注解
        Annotation serviceAnnotation = clazz.getAnnotation(Service.class);
        // 获取指定类型注解，有无权限都要返回
        Annotation transactionAnnotation = clazz.getDeclaredAnnotation(Transaction.class);
    }

    public void parseInnerClass() {
        Class<Service> clazz = forName("com.core.reflection.access.ServiceImpl");
        // 返回Class所包含全部的内部类对象
        Class<?>[] inners = clazz.getDeclaredClasses();
        // 返回Class对应类所在外部类
        Class<?> outer = clazz.getDeclaringClass();
    }


    public void parseInterface() {
        Class<Service> clazz = forName("com.core.reflection.access.ServiceImpl");
        // 返回Class所实现的全部接口
        Class<?>[] interfaces = clazz.getInterfaces();

    }


    public void parseSupers() {
        Class<Service> clazz = forName("com.core.reflection.access.ServiceImpl");
        // 返回Class所继承的类
        Class<?> supers = clazz.getSuperclass();
    }

    public void parseModifiers() {
        Class<Service> clazz = forName("com.core.reflection.access.ServiceImpl");
        // 返回Class所继承的类
        int code = clazz.getModifiers();
        String modifier = null;
        if (Modifier.isPrivate(code)) {
            modifier = "private";
        } else if (Modifier.isProtected(code)) {
            modifier = "protected";
        } else if (Modifier.isPublic(code)) {
            modifier = "public";
        } else if (Modifier.isStatic(code)){
            modifier = "static";
        } else if (Modifier.isFinal(code)){
            modifier = "final";
        } else if (Modifier.isAbstract(code)){
            modifier = "abstract";
        }
    }

    public void parseOthers() {
        Class<Service> clazz = forName("com.core.reflection.access.ServiceImpl");
        // 获取此类的包
        Package p = clazz.getPackage();

        // 以字符串的形式返回class对象的类的全限定名称和简称
        String name = clazz.getName();
        String simpleName = clazz.getSimpleName();
    }

    public void check() {
        Class<Service> clazz = forName("com.core.reflection.access.MapperImpl");
        // 判断该类的类型是不是接口、枚举或者注解
        // 是否注解
        boolean isAnnotation = clazz.isAnnotation();
        // 某一个类上(不是方法上)是否有某一种类型的注解
        boolean isAnnotationPresent = clazz.isAnnotationPresent(Transaction.class);
        // 该对象的类是不是匿名类
        boolean isAnonymousClass = clazz.isAnonymousClass();
        // 是否表示一个数组类
        boolean isArray = clazz.isArray();
        // 是否表示一个枚举
        boolean isEnum = clazz.isEnum();
        // 是否表示一个接口
        boolean isInterface = clazz.isInterface();
        // 判断此对象是不是这个类的实例
        IService service = new ServiceImpl();
        clazz.isInstance(service);

    }
    public void jdk8newMethod() {
        Class clazz = ServiceImpl.class;
        Constructor c = null;
        try {
            c = clazz.getConstructor(Mapper.class);
            // 获取形参个数
            int count = c.getParameterCount();
            // 获取所有形参
            Parameter[] parameters = c.getParameters();
            // 获取修饰形参的修饰符
            int mode = c.getModifiers();
            // 获取形参名字
            c.getName();
            // 获取带泛型的形参类型
            Type[] genericParameterTypes = c.getGenericParameterTypes();
            // 获取形参类型
            Type[] types = c.getParameterTypes();
            // 判断形参是否为可变形参
            boolean isVarArgs = c.isVarArgs();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        Method method = null;
        try {
            method = clazz.getMethod("find",String.class);
            // 获取形参个数
            int count = method.getParameterCount();
            // 获取所有形参
            Parameter[] parameters = method.getParameters();
            // 获取修饰形参的修饰符
            int mode = method.getModifiers();
            // 获取带泛型的形参类型
            Type[] genericParameterTypes = method.getGenericParameterTypes();
            // 获取形参类型
            Type[] types = method.getParameterTypes();
            // 判断形参是否为可变形参
            boolean isVarArgs = method.isVarArgs();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public void operation() {

        try {
            Class clazz = ServiceImpl.class;
            Constructor c = clazz.getConstructor(Mapper.class);
            Mapper<Model> mapper = new MapperImpl();
            Object target = c.newInstance(mapper);
            Method method = clazz.getMethod("selectOne",Long.class);
            Object result = method.invoke(target,1000L);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void accessMemberField() {
        try {
            Model model = new Model();
            Field modelName = Model.class.getDeclaredField("modelName");
            // 如果该字段对外不访问，则设置为可访问的；如果是直接获取getField，则默认获取的都是可以访问的字段。
            if (!modelName.isAccessible()) {
                modelName.setAccessible(Boolean.TRUE);
            }
            // 重新为字段设置值
            modelName.set(model,"Model Name");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        ClassParser parser = new ClassParser();
        parser.check();
    }
}
