package com.github.hcsp.descriptorparser;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
        final List<PrimitiveTypeDescriptor> typeDescriptors = Arrays.stream(PrimitiveTypeDescriptor.values())
                .filter(value -> value.descriptor.equals(descriptor))
                .collect(Collectors.toList());
        if (typeDescriptors.size() > 0) {
            return typeDescriptors.get(0);
        } else {
            return null;
        }
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
