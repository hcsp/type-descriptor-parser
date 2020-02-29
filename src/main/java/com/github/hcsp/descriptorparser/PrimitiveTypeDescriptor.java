package com.github.hcsp.descriptorparser;

import java.util.stream.Stream;

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
        ;return Stream.of(PrimitiveTypeDescriptor.values())
                .filter(desc-> desc.getDescriptor().equals(descriptor))
                .findFirst()
                .get();
    }

    public static boolean isPrimitive(String descriptor) {
        return Stream.of(PrimitiveTypeDescriptor.values())
                .anyMatch(desc-> desc.getDescriptor().equals(descriptor));
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
