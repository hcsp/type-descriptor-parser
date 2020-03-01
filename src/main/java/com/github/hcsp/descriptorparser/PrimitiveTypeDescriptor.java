package com.github.hcsp.descriptorparser;

import java.util.Arrays;

/**
 * 代表原生类型的描述符
 *
 * @author athos
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
    private final String descriptor;

    PrimitiveTypeDescriptor(String descriptor) {
        this.descriptor = descriptor;
    }

    public static PrimitiveTypeDescriptor of(String descriptor) {
        return Arrays.stream(values()).filter(v -> v.getDescriptor().equals(descriptor)).findFirst().orElse(null);
    }

    public static boolean isPrimitive(String descriptor) {
        return Arrays.stream(values()).anyMatch(v -> v.getDescriptor().equals(descriptor));
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
