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

    private static final Pattern pattern = Pattern.compile("^[BCDFIJSZV]$");

    PrimitiveTypeDescriptor(String descriptor) {
        this.descriptor = descriptor;
    }

    public static PrimitiveTypeDescriptor of(String descriptor) {
        for (PrimitiveTypeDescriptor value : PrimitiveTypeDescriptor.values()) {
            if(value.getDescriptor().equals(descriptor)){
                return value;
            }
        }
        return null;
    }

    public static boolean isPrimitive(String descriptor) {
        return pattern.matcher(descriptor).find();
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
