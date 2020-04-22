package com.github.hcsp.descriptorparser;

import java.util.regex.Matcher;

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

    PrimitiveTypeDescriptor(String descriptor) {
        this.descriptor = descriptor;
    }

    public static PrimitiveTypeDescriptor of(String descriptor) {
        for (PrimitiveTypeDescriptor pd : values()) {
            if (pd.descriptor.equals(descriptor)) {
                return pd;
            }
        }
        return null;
    }

    public static boolean isPrimitive(String descriptor) {
        for (PrimitiveTypeDescriptor pd : values()) {
            if (pd.descriptor.equals(descriptor)) {
                return true;
            }
        }
        return false;
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
