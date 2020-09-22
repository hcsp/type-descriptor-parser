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

    public ArrayDescriptor(String descriptor) {
        String regex = "(\\[+)(\\w)((\\w+/)+\\w+)?;?";
        Matcher matcher = getMatcher(descriptor, regex);
        while (matcher.find()) {
            this.descriptor = matcher.group(0);
            this.dimension = matcher.group(1).length();
            if (matcher.group(3) != null) {
                name = getAssembleName(matcher.group(3).replaceAll("/", "."));
            } else {
                name = getAssembleName(Objects.requireNonNull(PrimitiveTypeDescriptor.of(matcher.group(2))).getName());
            }
        }
    }

    private String getAssembleName(String type) {
        StringBuilder sb = new StringBuilder();
        sb.append(type);
        for (int i = 0; i < dimension; i++) {
            sb.append("[]");
        }
        return sb.toString();
    }

    public static Matcher getMatcher(String descriptor, String regex) {
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(descriptor);
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
