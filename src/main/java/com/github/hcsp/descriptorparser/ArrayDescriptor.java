package com.github.hcsp.descriptorparser;

import java.util.Arrays;

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
        this.descriptor = descriptor;
        // 1 or 2
        this.dimension = (int) Arrays.stream(descriptor.split("")).filter(s -> s.equals("[")).count();
        // Ljava/lang/Object;  or  I
        String rawDescType = descriptor.substring(this.dimension);
        StringBuilder rawTypeBuilder;
        if (PrimitiveTypeDescriptor.isPrimitive(rawDescType)) {
            rawTypeBuilder = new StringBuilder(PrimitiveTypeDescriptor.of(rawDescType).getName().toLowerCase());
        } else {
            rawTypeBuilder = new StringBuilder(new ReferenceDescriptor(rawDescType).getName());
        }
        for (int i = 0; i < dimension; i++) {
            rawTypeBuilder.append("[]");
        }
        this.name = rawTypeBuilder.toString();
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
}
