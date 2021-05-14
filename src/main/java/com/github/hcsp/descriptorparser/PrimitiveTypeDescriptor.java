package com.github.hcsp.descriptorparser;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
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
        Map<String, PrimitiveTypeDescriptor> descriptorMap = Arrays.stream(PrimitiveTypeDescriptor.values())
                .collect(Collectors.toMap(PrimitiveTypeDescriptor::getDescriptor, i -> i));
        return descriptorMap.get(descriptor);
    }

    public static boolean isPrimitive(String descriptor) {
        List<String> descriptorList = Arrays.stream(PrimitiveTypeDescriptor.values())
                .map(PrimitiveTypeDescriptor::getDescriptor)
                .collect(Collectors.toList());
        return descriptorList.contains(descriptor);
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
