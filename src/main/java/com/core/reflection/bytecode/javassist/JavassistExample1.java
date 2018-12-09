package com.core.reflection.bytecode.javassist;

import javassist.*;

public class JavassistExample1 {
    public void run() throws Exception {
        ClassPool pool = ClassPool.getDefault();
        CtClass cc = pool.makeClass("com.core.reflection.bytecode.User");
        // 创建属性
        CtField id = CtField.make("private int id;",cc);
        CtField name = CtField.make("private String name;",cc);
        cc.addField(id);
        cc.addField(name);

        // 创建方法
        CtMethod nameGetter = CtMethod.make("public String getName() {return name;}",cc);
        CtMethod nameSetter = CtMethod.make("public void setName(String name) {this.name = name;}",cc);
        cc.addMethod(nameGetter);
        cc.addMethod(nameSetter);

        //无参构造器
        CtConstructor nonParamConstructor = new CtConstructor(null,cc);
        nonParamConstructor.setBody("{}");
        cc.addConstructor(nonParamConstructor);

        //添加有参构造器
        CtClass[] params = {CtClass.intType,pool.get("java.lang.String")};
        CtConstructor paramConstructor = new CtConstructor(params,cc);
        paramConstructor.setBody("{this.id = id; this.name = name;}");
        cc.addConstructor(paramConstructor);
        cc.writeFile("D:/lib");
    }
}
