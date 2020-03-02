package com.github.hcsp.descriptorparser;

import com.github.hcsp.descriptorparser.util.TypeDescriptorParser;

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
                return BYTE;
            case "C":
                return CHAR;
            case "D":
                return DOUBLE;
            case "F":
                return FLOAT;
            case "I":
                return INT;
            case "J":
                return LONG;
            case "S":
                return SHORT;
            case "Z":
                return BOOLEAN;
            case "V":
                return VOID;
            default:
                throw new IllegalArgumentException();
        }

    }

    public static boolean isPrimitive(String descriptor) {
        return TypeDescriptorParser.isPrimitive(descriptor);
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
