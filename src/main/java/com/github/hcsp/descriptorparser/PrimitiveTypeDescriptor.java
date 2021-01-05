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
        String s = descriptor.toUpperCase();
        for (PrimitiveTypeDescriptor primitiveTypeDescriptor : PrimitiveTypeDescriptor.values()) {
            if (primitiveTypeDescriptor.getDescriptor().equals(s)) {
                return primitiveTypeDescriptor;
            }
        }
        return null;
    }

    public static boolean isPrimitive(String descriptor) {
        return of(descriptor) != null;
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
