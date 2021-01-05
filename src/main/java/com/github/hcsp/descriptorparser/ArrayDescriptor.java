package com.github.hcsp.descriptorparser;

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
    private static final String REGEX = "^(\\[+)(.*)";
    public static final Pattern PATTERN = Pattern.compile(REGEX);

    // [[Ljava/lang/Object;
    public ArrayDescriptor(String descriptor) {
        Matcher matcher = PATTERN.matcher(descriptor);
        if (matcher.find()) {
            this.descriptor = descriptor;
            dimension = matcher.group(1).length();
            rawType = TypeFly.of(matcher.group(2));
            name = rawType.getName() + String.join("", Collections.nCopies(dimension, "[]"));
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
