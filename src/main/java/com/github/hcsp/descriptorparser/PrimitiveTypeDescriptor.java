package com.github.hcsp.descriptorparser;

import java.util.regex.Matcher;
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
        }
        return null;
    }

    public static boolean isPrimitive(String descriptor) {
        for (PrimitiveTypeDescriptor primitive : PrimitiveTypeDescriptor.values()) {
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

    public static void main(String[] args) {
        String str = "IDLjava/lang/Thread;";
        Pattern compile = Pattern.compile("(\\[*[BCDFIJSZV]|\\[*L.*)");
        Matcher matcher = compile.matcher(str);
        while (matcher.find()) {
            System.out.println(matcher.group());
        }

    }
}
