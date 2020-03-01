package com.github.hcsp.descriptorparser;

import java.util.stream.IntStream;
import java.util.stream.Stream;

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

    // [[Ljava/lang/Object;
    public ArrayDescriptor(String descriptor) {
        int indexOfRawType = descriptor.lastIndexOf('[') + 1;

        this.dimension = descriptor.substring(0, indexOfRawType).length();

        String rawTypeDescriptor = descriptor.substring(indexOfRawType);
        if(PrimitiveTypeDescriptor.isPrimitive(rawTypeDescriptor)) {
            this.rawType =  PrimitiveTypeDescriptor.of(rawTypeDescriptor);
        } else {
            this.rawType = new ReferenceDescriptor(rawTypeDescriptor);
        }

        this.name = this.rawType.getName();
        IntStream.rangeClosed(1, this.dimension).forEach( i -> {
            this.name += "[]";
        });
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
        System.out.println(new ArrayDescriptor("[[[I").rawType.getName());
    }
}
