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
    private static final Pattern descriptorPattern = Pattern.compile("(\\[+)([BCDFIJSZ]|L([\\w/]+);)");

    // [[Ljava/lang/Object;
    public ArrayDescriptor(String descriptor) {
        this.descriptor = descriptor;
        this.parseDescriptor(descriptor);
    }

    public static ArrayDescriptor of(String descriptor) {
        return new ArrayDescriptor(descriptor);
    }

    public static boolean isArray(String descriptor) {
        return descriptorPattern.matcher(descriptor).find();
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

    private void parseDescriptor(String descriptor) {
        Matcher matcher = descriptorPattern.matcher(descriptor);
        while (matcher.find()) {
            String bracket = matcher.group(1);
            String desc = matcher.group(2);
            this.dimension = bracket.length();
            StringBuilder sb = new StringBuilder();

            String classpath = matcher.group(3);
            if (classpath != null) {
                sb.append(classpath.replaceAll("/", "."));
            } else {
                sb.append(PrimitiveTypeDescriptor.of(desc).getName());
            }

            for (int i = 0; i < dimension; i++) {
                sb.append("[]");
            }

            this.name = sb.toString();
        }
    }
}
