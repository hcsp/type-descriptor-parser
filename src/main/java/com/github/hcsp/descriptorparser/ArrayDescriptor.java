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

    // [[Ljava/lang/Object;
    private static final Pattern pattern = Pattern.compile("(\\[{1,})([BCDFIJSZV]|L[\\w\\/\\$]*;)");

    public ArrayDescriptor(String descriptor) {
        this.descriptor = descriptor;
        Matcher matcher = pattern.matcher(descriptor);
        if (matcher.find()) {
            this.dimension = matcher.group(1).length();
            buildType(matcher);
            buildName();
        }
    }

    private void buildType(Matcher matcher) {
        String type = matcher.group(2);
        if (PrimitiveTypeDescriptor.isPrimitive(type)) {
            this.rawType=PrimitiveTypeDescriptor.of(type);
        }else {
            this.rawType=new ReferenceDescriptor(type);
        }
    }

    private void buildName() {
        String symbol ="[]";
        this.name = rawType.getName();
        for (int i = 0; i < dimension; i++) {
            name+=symbol;
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
