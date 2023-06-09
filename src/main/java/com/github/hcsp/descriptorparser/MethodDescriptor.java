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
    public static final Pattern ARRAY_REGEX_PATTERN = Pattern.compile("\\[*\\w");
    public static final Pattern REFERENCE_REGEX_PATTERN = Pattern.compile("\\[*L(.+)");
    public static final Pattern METHOD_REGEX_PATTERN = Pattern.compile("^\\(((?:\\[*\\w)*)((?:\\[*L.+)*)\\)(.+)");

    public MethodDescriptor(String descriptor) {
        this.descriptor = descriptor;
        Matcher matcher = METHOD_REGEX_PATTERN.matcher(descriptor);
        if (matcher.find()) {
            String primitiveParamTypes = matcher.group(1);
            String referenceParamType = matcher.group(2);
            String returnTypeDesc = matcher.group(3);

            this.returnType = assignType(returnTypeDesc);
            dealWithParams(primitiveParamTypes, referenceParamType);
            String paramNames = paramTypes.stream().map(TypeDescriptor::getName).collect(Collectors.joining(", "));
            String returnTypeName = this.returnType.getName();
            this.name = returnTypeName + " (" + paramNames + ")";
        }

    }

    private void dealWithParams(String primitiveParamTypes, String referenceParamType) {
        if ("".equals(primitiveParamTypes) || "".equals(referenceParamType)) {
            return;
        }
        Matcher matcher = ARRAY_REGEX_PATTERN.matcher(primitiveParamTypes);
        while (matcher.find()) {
            dealWithPrimitiveTypeOrNot(matcher.group(0), true);
        }
        Matcher matcher2 = REFERENCE_REGEX_PATTERN.matcher(referenceParamType);
        while (matcher2.find()) {
            dealWithPrimitiveTypeOrNot(matcher2.group(0), false);
        }
    }

    private void dealWithPrimitiveTypeOrNot(String type, boolean isPrimitive) {
        if (type.startsWith("[")) {
            paramTypes.add(new ArrayDescriptor(type));
        } else {
            if (isPrimitive) {
                paramTypes.add(PrimitiveTypeDescriptor.of(type));
            } else {
                paramTypes.add(new ReferenceDescriptor(type));
            }
        }
    }

    public static TypeDescriptor assignType(String typeDesc) {
        PrimitiveTypeDescriptor primitiveTypeDescriptor = PrimitiveTypeDescriptor.of(typeDesc);
        if (primitiveTypeDescriptor != null) {
            return primitiveTypeDescriptor;
        } else {
            return new ReferenceDescriptor(typeDesc);
        }
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

