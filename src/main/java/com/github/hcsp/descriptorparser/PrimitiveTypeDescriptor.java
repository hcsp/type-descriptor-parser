package com.github.hcsp.descriptorparser;

/**
 * 代表原生类型的描述符
 */
public enum PrimitiveTypeDescriptor implements TypeDescriptor {//原始类型枚举
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
        switch(descriptor){
            case "B":return PrimitiveTypeDescriptor.BYTE;
            case"C":return PrimitiveTypeDescriptor.CHAR;
            case "D":return PrimitiveTypeDescriptor.DOUBLE;
            case "F":return PrimitiveTypeDescriptor.FLOAT;
            case "I":return PrimitiveTypeDescriptor.INT;
            case "J":return PrimitiveTypeDescriptor.LONG;
            case "S":return PrimitiveTypeDescriptor.SHORT;
            case "Z":return PrimitiveTypeDescriptor.BOOLEAN;
            default:return PrimitiveTypeDescriptor.VOID;
        }
    }

    public static boolean isPrimitive(String descriptor) {
        switch(descriptor){
            case "B":;
            case"C":;
            case "D":;
            case "F":;
            case "I":;
            case "J":;
            case "S":;
            case "Z":;
            case"V":return true;

            default:return false;
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
