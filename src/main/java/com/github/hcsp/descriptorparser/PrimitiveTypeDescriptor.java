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
        for (PrimitiveTypeDescriptor p : PrimitiveTypeDescriptor.values()) {
            if (p.getDescriptor().equals(descriptor)) {
                return p;
            }
        }
        return null;
    }

    public static boolean isPrimitive(String descriptor) {
        PrimitiveTypeDescriptor[] values = values();
        return Stream.of(values).map(PrimitiveTypeDescriptor::getDescriptor)
                .anyMatch(s -> s.equals(descriptor));
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
