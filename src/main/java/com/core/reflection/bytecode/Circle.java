package com.core.reflection.bytecode;

public class Circle extends AbstractShape {

    public Circle() {
    }

    public Circle(String desc, int perimeter, float acreage) {
        super(desc, perimeter, acreage);
    }

    public void draw() {
        System.out.println("原形==>面积："+getAcreage()+"，周长==>"+getPerimeter());
    }
}
