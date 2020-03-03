package com.github.hcsp.descriptorparser;

import java.util.regex.Pattern;

/**
 * 代表原生类型的描述符
 */
public enum PrimitiveTypeDescriptor implements TypeDescriptor {
    BYTE("B"),
    CHAR("C"),
    DOUBLE("D"),
    FLOAT("F"),
    INT("I"),
    LONG("J"),
    SHORT("S"),
    BOOLEAN("Z"),
    VOID("V");
    private String descriptor;

    //原生类型正则表达式
    public static String regex = "[BCDFIJSZV]";

    PrimitiveTypeDescriptor(String descriptor) {
        this.descriptor = descriptor;
    }

    public static PrimitiveTypeDescriptor of(String descriptor) {
        for (PrimitiveTypeDescriptor primitiveTypeDescriptor : PrimitiveTypeDescriptor.values()) {
            if (primitiveTypeDescriptor.getDescriptor().equalsIgnoreCase(descriptor)) {
                return primitiveTypeDescriptor;
            }
        }
        return null;
    }

    public static boolean isPrimitive(String descriptor) {
        return descriptor.length() == 1 && Pattern.matches(regex, descriptor);
    }

    @Override
    public String getName() {

        return name().toLowerCase();
    }

    @Override
    public String getDescriptor() {
        return descriptor;
    }
}
