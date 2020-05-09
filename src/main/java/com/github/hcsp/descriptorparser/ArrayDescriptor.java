package com.github.hcsp.descriptorparser;

import static com.github.hcsp.descriptorparser.TypeDescriptor.*;
import static com.github.hcsp.descriptorparser.TypeDescriptor.buildEnd;
import static com.github.hcsp.descriptorparser.TypeDescriptor.countRepeat;

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
        int dimension = countRepeat(descriptor, "[");
        String end = buildEnd(dimension);
        String typeFlag = descriptor.replace("[", "").substring(0, 1);
        if (typeFlag.equals("L")) {
            name = descriptor
                    .replace("[", "")
                    .replace("L", "")
                    .replace(";", "")
                    .replaceAll("/", ".")
                    + end;
        } else {
            name = getFullType(typeFlag) + end;
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
}
