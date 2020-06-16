package com.github.hcsp.descriptorparser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 代表方法的描述符，如给定方法描述符(IDLjava/lang/Thread;)Ljava/lang/Object;
 * paramTypes应该是对应的参数类型
 * returnType应该是返回值类型
 * name应该是人类可读的格式 java.lang.Object (int i, double d, java.lang.Thread t)
 */
public class MethodDescriptor implements TypeDescriptor {
    private List<TypeDescriptor> paramTypes = new ArrayList<>();
    private TypeDescriptor returnType;
    private String descriptor;
    private String name;

    private static final Pattern FIRST_PATTERN = Pattern.compile("^\\((.*)\\)(.+)$");
    private static final Pattern SECOND_PATTERN = Pattern.compile("(\\[*(?:[BCDFIJSZV]|L.*;))");

    public MethodDescriptor(String descriptor) {
        this.descriptor = descriptor;
        Matcher firstMatcher = FIRST_PATTERN.matcher(descriptor);
        while (firstMatcher.find()) {
            String returnTypeDesc = firstMatcher.group(2);
            String paramTypesDesc = firstMatcher.group(1);
            Matcher secondMatcher = SECOND_PATTERN.matcher(paramTypesDesc);
            while (secondMatcher.find()) {
                this.paramTypes.add(getTypeDescriptor(secondMatcher.group(1)));
            }
            this.returnType = getTypeDescriptor(returnTypeDesc);
        }
        this.name = getStringFromTypeDescriptor(this.returnType.getName(),
                this.paramTypes.stream()
                        .map(TypeDescriptor::getName)
                        .collect(Collectors.joining(", ")));
    }

    private static String getStringFromTypeDescriptor(String returnType, String paramTypesToString) {
        return returnType + " (" + paramTypesToString + ")";
    }

    private static TypeDescriptor getTypeDescriptor(String descriptor) {
        TypeDescriptor typeDescriptor;
        if (ArrayDescriptor.isArray(descriptor)) {
            typeDescriptor = new ArrayDescriptor(descriptor);
        } else if (PrimitiveTypeDescriptor.isPrimitive(descriptor)) {
            typeDescriptor = PrimitiveTypeDescriptor.of(descriptor);
        } else if (ReferenceDescriptor.isReference(descriptor)) {
            typeDescriptor = new ReferenceDescriptor(descriptor);
        } else {
            typeDescriptor = null;
        }
        return typeDescriptor;
    }

    public List<TypeDescriptor> getParamTypes() {
        return paramTypes;
    }

    public TypeDescriptor getReturnType() {
        return returnType;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescriptor() {
        return descriptor;
    }
}

