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
    /**
     * group(1)  只匹配 [
     * group(2)  匹配 类型 Ljava/lang/Object; ｜ I
     * group(3)  不带L不带; java/lang/Object
     */
    public static final Pattern ARRAY_REGEX = Pattern.compile("^(\\[*)(L?(.*?);?)$");

    // [[Ljava/lang/Object;
    public ArrayDescriptor(String descriptor) {
        this.descriptor = descriptor;
        Matcher matcher = ARRAY_REGEX.matcher(descriptor);
        if (matcher.find()) {
            // [
            dimension = matcher.group(1).length();
            String type = matcher.group(2);
            rawType = TypeDescriptor.parse(type);
            StringBuilder sb = new StringBuilder(rawType.getName());
            for (int i = 0; i < dimension; i++) {
                sb.append("[]");
            }
            name = sb.toString();
        } else {
            throw new RuntimeException(" no match");
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
