package com.github.hcsp.descriptorparser;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        StringBuilder sb = new StringBuilder();
        StringBuilder rs = new StringBuilder();
        if (descriptor.endsWith(";"))
            descriptor= descriptor.substring(0,descriptor.length()-1);
        loop(descriptor, sb, rs);
        this.name = rs.append(sb).toString();
    }

    private void loop(String descriptor, StringBuilder sb, StringBuilder rs) {
        if (descriptor.startsWith("[")) {
            sb.append("[]");
            loop(descriptor.substring(1), sb, rs);
        } else {
            if (descriptor.startsWith("L")) {
                descriptor = descriptor.substring(1);
                if (descriptor.contains(";")){
                    rs.append(descriptor.substring(0,descriptor.indexOf(";")).replace("/", "."));
                }else{
                    rs.append(descriptor.replace("/", "."));
                }
            } else if (PrimitiveTypeDescriptor.isPrimitive(descriptor.substring(0,1))) {
                rs.append(PrimitiveTypeDescriptor.of(descriptor.substring(0,1)).getName());
            }
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
