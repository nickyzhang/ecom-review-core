package com.core.reflection.bytecode;

public class Rectangle extends AbstractShape {

    public Rectangle() {
    }

    public Rectangle(String desc, int perimeter, float acreage) {
        super(desc, perimeter, acreage);
    }

    public void draw() {
        System.out.println("长方形==>面积："+getAcreage()+"，周长==>"+getPerimeter());
    }
}
