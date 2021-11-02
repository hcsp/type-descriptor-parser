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

    private static final String METHOD_DESC_REGEX = "\\((.*)\\)(.*)";
    private static final String PARAM_DESC_REGEX = "(\\[*[BCDFIJSZV]|\\[*L.+)";
    private static final Pattern METHOD_DESC_PATTERN = Pattern.compile(METHOD_DESC_REGEX);

    public MethodDescriptor(String descriptor) {
        this.descriptor = descriptor;
        Matcher matcher = METHOD_DESC_PATTERN.matcher(descriptor);
        if (matcher.find()) {
            String params = matcher.group(1);
            initParamTypes(params);

            String returnStr = matcher.group(2);
            initReturnType(returnStr);
        }
        String methodSignature = paramTypes.stream()
                .map(TypeDescriptor::getName)
                .collect(Collectors.joining(", "));
        this.name = returnType.getName() + " (" + methodSignature + ")";

    }

    private void initParamTypes(String params) {
        Matcher matcher = Pattern.compile(PARAM_DESC_REGEX).matcher(params);
        while (matcher.find()) {
            String s = matcher.group();
            if (s.startsWith("[")) {
                paramTypes.add(new ArrayDescriptor(s));
            } else if (s.startsWith("L")) {
                paramTypes.add(new ReferenceDescriptor(s));
            } else {
                paramTypes.add(PrimitiveTypeDescriptor.of(s));
            }
        }
    }

    private void initReturnType(String returnStr) {
        if (returnStr.startsWith("L")) {
            this.returnType = new ReferenceDescriptor(returnStr);
        } else {
            this.returnType = PrimitiveTypeDescriptor.of(returnStr);
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

