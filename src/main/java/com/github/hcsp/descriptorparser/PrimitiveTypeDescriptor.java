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

    PrimitiveTypeDescriptor(String descriptor) {
        this.descriptor = descriptor;
    }

    public static PrimitiveTypeDescriptor of(String descriptor) {
        for (PrimitiveTypeDescriptor primitiveTypeDescriptor : PrimitiveTypeDescriptor.values()) {
            if(primitiveTypeDescriptor.getDescriptor().equalsIgnoreCase(descriptor)){
                return primitiveTypeDescriptor;
            }
        }
        return null;
    }

    public static boolean isPrimitive(String descriptor) {

        String regex = "[B,C,D,F,I,J,S,Z,V]";

        if(descriptor.length() == 1 && Pattern.matches(regex, descriptor)){
            return true;
        }else{
            return false;
        }
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
