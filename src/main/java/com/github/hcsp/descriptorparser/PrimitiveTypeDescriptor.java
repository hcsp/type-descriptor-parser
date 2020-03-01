package com.github.hcsp.descriptorparser;

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
        if (BYTE.descriptor.equals(descriptor)) {
            return BYTE;
        } else if (CHAR.descriptor.equals(descriptor)) {
            return CHAR;
        } else if (DOUBLE.descriptor.equals(descriptor)) {
            return DOUBLE;
        } else if (FLOAT.descriptor.equals(descriptor)) {
            return FLOAT;
        } else if (INT.descriptor.equals(descriptor)) {
            return INT;
        } else if (LONG.descriptor.equals(descriptor)) {
            return LONG;
        } else if (SHORT.descriptor.equals(descriptor)) {
            return SHORT;
        } else if (BOOLEAN.descriptor.equals(descriptor)) {
            return BOOLEAN;
        } else if (VOID.descriptor.equals(descriptor)) {
            return VOID;
        } else {
            return null;
        }
    }

    public static boolean isPrimitive(String descriptor) {
        PrimitiveTypeDescriptor of = PrimitiveTypeDescriptor.of(descriptor);
        if (of == null) {
            return false;
        }
        return true;
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
