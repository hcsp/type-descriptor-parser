package com.github.hcsp.descriptorparser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Ruby
 * @date 2021/1/5 20:49
 */
public class TypeFly {
    private static final String TYPE_REGEX = "(?<array>(\\[*)?)((?<primitiveType>[BCDFIJSZV])|(?<referenceType>L[^;]*;))";
    public static final Pattern TYPE_PATTERN = Pattern.compile(TYPE_REGEX);

    public static TypeDescriptor of(String descriptor) {
        Matcher matcher = TYPE_PATTERN.matcher(descriptor);
        if (matcher.find()) {
            String primitiveType = matcher.group("primitiveType");
            String array = matcher.group("array");
            String referenceType = matcher.group("referenceType");
            return new BaseType(array, primitiveType, referenceType).typeFly();
        }
        throw new RuntimeException("匹配错误");
    }

    public static List<TypeDescriptor> ofList(String descriptor) {
        Matcher matcher = TYPE_PATTERN.matcher(descriptor);
        List<TypeDescriptor> list = new ArrayList<>();
        while (matcher.find()) {
            String array = matcher.group("array");
            String primitiveType = matcher.group("primitiveType");
            String referenceType = matcher.group("referenceType");
            list.add(new BaseType(array, primitiveType, referenceType).typeFly());
        }
        return list;
    }

    private static class BaseType {
        private final String array;
        private final String primitiveType;
        private final String referenceType;

        public BaseType(String array, String primitiveType, String referenceType) {
            this.array = array;
            this.primitiveType = primitiveType;
            this.referenceType = referenceType;
        }

        private TypeDescriptor typeFly() {
            if (array == null || array.isEmpty()) {
                if (primitiveType != null) {
                    return PrimitiveTypeDescriptor.of(primitiveType);
                }
                if (referenceType != null) {
                    return new ReferenceDescriptor(referenceType);
                }
                return null;
            } else {
                return new ArrayDescriptor(array + primitiveType + referenceType);
            }
        }
    }
}
