package com.github.hcsp.descriptorparser;

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

    private static final String ARR_DESC_REGEX = "(\\[+)([^\\[]+)";
    private static final Pattern PATTERN = Pattern.compile(ARR_DESC_REGEX);

    // [[Ljava/lang/Object;
    public ArrayDescriptor(String descriptor) {
        this.descriptor = descriptor;
        Matcher matcher = PATTERN.matcher(descriptor);
        if (matcher.find()) {
            String arrDim = matcher.group(1);
            String arrType = matcher.group(2);
            this.dimension = arrDim.length();
            if (arrType.startsWith("L")) {
                this.rawType = new ReferenceDescriptor(arrType);
            } else {
                this.rawType = PrimitiveTypeDescriptor.of(arrType);
            }
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < dimension; i++) {
                builder.append("[]");
            }
            this.name = rawType.getName() + builder.toString();
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
