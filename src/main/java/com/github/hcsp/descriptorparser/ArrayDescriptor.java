package com.github.hcsp.descriptorparser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

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
        Pattern arrayPattern = Pattern.compile("(\\[+)([LBCDFIJSZV])(.*);*");
        Matcher matcher = arrayPattern.matcher(descriptor);
        if (matcher.find()) {
            String dimensionStr = matcher.group(1);
            String type = matcher.group(2);
            String clazz = matcher.group(3);

            this.dimension = dimensionStr.length();
            this.descriptor = descriptor;
            if (PrimitiveTypeDescriptor.isPrimitive(type)) {
                this.rawType = PrimitiveTypeDescriptor.of(type);
            }else {
                this.rawType = new ReferenceDescriptor(type + clazz);
            }

            String nameEnd = "";
            for (int i = 0; i < dimension; i++) {
                nameEnd += "[]";
            }

            this.name = rawType.getName() + nameEnd;
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
