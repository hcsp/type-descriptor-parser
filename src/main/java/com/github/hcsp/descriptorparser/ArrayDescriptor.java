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
    public ArrayDescriptor(String descriptor) {
        this.descriptor = descriptor;
    }

    @Override
    public String getName() {
        Pattern pattern = Pattern.compile("(\\[+)?([A-Z])(((.*/)?.*);)?");
        Matcher matcher = pattern.matcher(descriptor);
        int dimension = getDimension();
        StringBuilder result = new StringBuilder();
        for (int i = 1; i <= dimension; i++) {
            result.append("[]");
        }

        if (matcher.find()) {
            String letter = matcher.group(2);
            if (letter.equals("L")) {
                String group3 = matcher.group(4);
                String fqcn = group3.replace("/", ".");
                name = fqcn + result;
            } else {
                String rawName = getRawType().getName();
                name = rawName + result;
            }
        }
        return name;
    }

    public int getDimension() {
        Pattern pattern = Pattern.compile("(\\[+)?");
        Matcher matcher = pattern.matcher(descriptor);
        if (matcher.find()) {
            String group = matcher.group();
            dimension = group.length();
        }
        return dimension;
    }

    public TypeDescriptor getRawType() {
        Pattern pattern = Pattern.compile("(\\[+)?([A-Z])");
        Matcher matcher = pattern.matcher(descriptor);
        if (matcher.find()) {
            String group = matcher.group(2);
            for (PrimitiveTypeDescriptor descriptor : PrimitiveTypeDescriptor.values()) {
                if (group.equals(descriptor.getDescriptor())) {
                    rawType = descriptor;
                }
            }
        }
        return rawType;
    }

    @Override
    public String getDescriptor() {
        return descriptor;
    }
}
