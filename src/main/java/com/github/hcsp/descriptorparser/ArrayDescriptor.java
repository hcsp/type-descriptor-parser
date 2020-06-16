package com.github.hcsp.descriptorparser;

import java.util.Objects;
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

    private static final Pattern PATTERN = Pattern.compile("^(\\[+)([^\\[]+)$");

    // [[Ljava/lang/Object;
    public ArrayDescriptor(String descriptor) {
        Matcher matcher = PATTERN.matcher(descriptor);
        String brackets = "";
        String subDescriptor = "";
        while (matcher.find()) {
            brackets = matcher.group(1);
            subDescriptor = matcher.group(2);
        }

        this.descriptor = descriptor;
        this.dimension = brackets.length();
        this.rawType = PrimitiveTypeDescriptor.isPrimitive(subDescriptor)
                ? PrimitiveTypeDescriptor.of(subDescriptor) : new ReferenceDescriptor(subDescriptor);
        this.name = getString(Objects.requireNonNull(this.rawType).getName(), this.dimension);
    }

    private static String getString(String rawType, int dimension) {
        StringBuilder builder = new StringBuilder();
        builder.append(rawType);
        for (int i = 0; i < dimension; i++) {
            builder.append("[]");
        }
        return builder.toString();
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

    public static boolean isArray(String descriptor) {
        return PATTERN.matcher(descriptor).find();
    }
}
