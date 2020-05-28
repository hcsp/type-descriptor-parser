package com.github.hcsp.descriptorparser;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.github.hcsp.descriptorparser.MethodDescriptor.assignType;

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
    public static final String ARRAY_REGEX = "(\\[+)(.+)";

    // [[Ljava/lang/Object;
    public ArrayDescriptor(String descriptor) {
        this.descriptor = descriptor;
        Pattern pattern = Pattern.compile(ARRAY_REGEX);
        Matcher matcher = pattern.matcher(descriptor);
        if (matcher.find()) {
            this.dimension = matcher.group(1).length();
            String typeDesc = matcher.group(2);
            TypeDescriptor typeDescriptor = assignType(typeDesc);
            this.rawType = typeDescriptor;
            setName(typeDescriptor);
        }
    }

    private void setName(TypeDescriptor typeDescriptor) {
        StringBuilder name = new StringBuilder(Objects.requireNonNull(typeDescriptor).getName());
        for (int i = 0; i < dimension; i++) {
            name.append("[]");
        }
        this.name = name.toString();
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
