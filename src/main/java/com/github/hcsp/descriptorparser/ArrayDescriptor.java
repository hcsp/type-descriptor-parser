package com.github.hcsp.descriptorparser;

import java.util.Map;
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

    Pattern arrayPattern = Pattern.compile("^(\\[*)((L.*?;)*(\\w)*)");

    // [[Ljava/lang/Object;
    public ArrayDescriptor(String descriptor) {
        this.descriptor = descriptor;
        this.name = "";
        final Matcher matcher = arrayPattern.matcher(descriptor);
        if (matcher.find()) {
            final String arrayDesc = matcher.group(1);
            final String typeDesc = matcher.group(2);
            this.dimension = arrayDesc.length();

            final Map<String, Object> nameTypeMap = TypeDescriptorUtil.assignArrayName(typeDesc, this.dimension);
            this.name = ((String) nameTypeMap.get("name"));
            this.rawType = ((TypeDescriptor) nameTypeMap.get("typeDescriptor"));
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
