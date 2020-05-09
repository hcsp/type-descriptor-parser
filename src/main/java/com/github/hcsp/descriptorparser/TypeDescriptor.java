package com.github.hcsp.descriptorparser;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public interface TypeDescriptor {
    /**
     * 返回人类可读的类型名，如int或者java.lang.Object[]
     *
     * @return the human-readable name
     */
    String getName();

    /**
     * 返回描述符的原始格式，如[I或者Ljava/lang/Object;
     *
     * @return the raw descriptor
     */
    String getDescriptor();

    /**
     * 解析一个描述符，根据其具体类型返回不同的子类
     *
     * @param descriptor
     * @return PrimitiveTypeDescriptor/ReferenceDescriptor/MethodDescriptor/PrimitiveTypeDescriptor
     */
    static TypeDescriptor parse(String descriptor) {
        return null;
    }

    static int countRepeat(String source, String k) {
        return (int) Arrays.stream(source.split("")).filter(elements -> k.equals(elements)).count();
    }

    static String getFullType(String descriptor) {
        Map<String, String> typeDescriptor = new HashMap<>();
        typeDescriptor.put("B", "byte");
        typeDescriptor.put("C", "char");
        typeDescriptor.put("D", "double");
        typeDescriptor.put("F", "float");
        typeDescriptor.put("I", "int");
        typeDescriptor.put("J", "long");
        typeDescriptor.put("S", "short");
        typeDescriptor.put("Z", "boolean");
        typeDescriptor.put("V", "void");
        return typeDescriptor.get(descriptor);
    }

    static String buildEnd(int dimension) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < dimension; i++) {
            sb.append("[]");
        }
        return sb.toString();
    }
}







