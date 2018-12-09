package com.core.search;

import org.apache.commons.lang3.math.NumberUtils;

public class DirectGraph {
    public static void main(String[] args) {
        System.out.println(NumberUtils.isNumber("22"));
        System.out.println(NumberUtils.isNumber("0.22"));

        System.out.println(NumberUtils.isDigits("22"));
        System.out.println(NumberUtils.isDigits("0.22"));
    }
}
