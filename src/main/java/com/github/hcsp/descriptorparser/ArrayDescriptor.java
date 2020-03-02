package com.github.hcsp.descriptorparser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 数组类型的描述符，如输入[[Ljava/lang/Object;
 * 得到的name是java.lang.Object[][]
 * dimension为2（二维数组）
 * rawType代表无数组的原始类型
 */
public class ArrayDescriptor implements TypeDescriptor {
    // [ -> 1
    // [[ -> 2
    private String name;
    private String descriptor;
    private int dimension;
    private TypeDescriptor rawType;

    // [[Ljava/lang/Object; -> java.lang.Object [][]
    public ArrayDescriptor(String descriptor) {
        char[] chars = descriptor.replace(';', ' ').trim().toCharArray();
        for (char aChar : chars) {
            if (aChar == '[') {
                dimension += 1;
            }
        }

        if (chars[dimension] == 'L') {
            name = String.valueOf(chars).replaceAll("/", ".").substring(dimension + 1);
        } else {
            name = TypeDescriptor.parse(Character.toString(chars[dimension])).getName();
            //这句话可以替代非常复杂的switch语句(case char=='I'|'L'等等)
        }


        for (int j = 0; j < dimension; j++) {
            name += "[]";
        }


    }

    @Override
    public String getName() {
        return name;
    }

    public int getDimension() {

        return dimension;
    }

    public TypeDescriptor getRawType() {
        return rawType;
    }

    @Override
    public String getDescriptor() {
        return descriptor;
    }

    public static void main(String[] args) {
        System.out.println(new ArrayDescriptor("[[Ljava/lang/Object;").getName());
    }
}
