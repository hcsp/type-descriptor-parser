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
    private String regexRawType = "(\\[*)(.*)";
    private Pattern pattern = Pattern.compile(regexRawType);

    public ArrayDescriptor(String descriptor) {
        this.descriptor = descriptor;
        Matcher matcher = pattern.matcher(descriptor);
        if (matcher.find()) {
            String dimensionStr = matcher.group(1);
            String rawType = matcher.group(2);
            this.dimension = dimensionStr.length();
            if (rawType.startsWith("L")) {
                this.rawType = new ReferenceDescriptor(rawType);
            } else {
                this.rawType = PrimitiveTypeDescriptor.of(rawType);
            }
            StringBuilder str = new StringBuilder();
            for (int i = 0; i < dimension; ++i) {
                str.append(dimensionStr.charAt(i)).append("]");
            }
            this.name = this.rawType.getName() + str;
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
