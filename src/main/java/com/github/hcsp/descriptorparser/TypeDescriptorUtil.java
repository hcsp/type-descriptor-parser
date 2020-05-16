package com.github.hcsp.descriptorparser;

import java.util.HashMap;
import java.util.Map;

public class TypeDescriptorUtil {
    public static Map<String, Object> assignName(String typeDesc) {
        Map<String, Object> result = new HashMap<>();
        String name = "";
        TypeDescriptor rawType;
        if (PrimitiveTypeDescriptor.isPrimitive(typeDesc)) {
            rawType = PrimitiveTypeDescriptor.of(typeDesc);
            if (rawType != null) {
                name = rawType.getName();
                result.put("name", name);
                result.put("typeDescriptor", rawType);
            }
        } else {
            rawType = new ReferenceDescriptor(typeDesc);
            name = rawType.getName();
            result.put("name", name);
            result.put("typeDescriptor", rawType);
        }

        return result;
    }

    public static Map<String, Object> assignArrayName(String typeDesc, int dimension) {
        Map<String, Object> result = assignName(typeDesc);
        String name = (String) result.get("name");
        for (int i = 0; i < dimension; i++) {
            name += "[]";
        }
        result.put("name", name);
        return result;
    }
}
