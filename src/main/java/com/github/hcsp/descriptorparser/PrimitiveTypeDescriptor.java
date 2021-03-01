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
        switch (descriptor) {
            case "B":
                return PrimitiveTypeDescriptor.BYTE;
            case "C":
                return PrimitiveTypeDescriptor.CHAR;
            case "D":
                return PrimitiveTypeDescriptor.DOUBLE;
            case "F":
                return PrimitiveTypeDescriptor.FLOAT;
            case "I":
                return PrimitiveTypeDescriptor.INT;
            case "J":
                return PrimitiveTypeDescriptor.LONG;
            case "S":
                return PrimitiveTypeDescriptor.SHORT;
            case "Z":
                return PrimitiveTypeDescriptor.BOOLEAN;
            case "V":
                return PrimitiveTypeDescriptor.VOID;
            default:
                return null;
        }

    }

    public static boolean isPrimitive(String descriptor) {
        for (PrimitiveTypeDescriptor primitive : values()) {
            if (primitive.getDescriptor().equals(descriptor)) {
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
