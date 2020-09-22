package com.github.hcsp.descriptorparser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

import static com.github.hcsp.descriptorparser.ArrayDescriptor.getMatcher;

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

    public MethodDescriptor(String descriptor) {
        String regex = "\\((.*?)\\)(.*)";
        Matcher matcher = getMatcher(descriptor, regex);
        while (matcher.find()) {
            this.descriptor = matcher.group(0);
            Matcher m = getMatcher(matcher.group(1), "(\\[*(?:[BCDFIJSZV]|L.*;))");
            while (m.find()) {
                paramTypes.add(getTypeDescriptor(m.group(1)));
            }
            String param = " (" + paramTypes.stream().map(TypeDescriptor::getName).collect(Collectors.joining(", ")) + ")";
            returnType = getTypeDescriptor(matcher.group(2));
            if (returnType != null) {
                name = returnType.getName() + param;
            }
        }
    }

    private TypeDescriptor getTypeDescriptor(String descriptor) {
        if (descriptor.length() == 1) {
            return PrimitiveTypeDescriptor.of(descriptor);
        } else if (descriptor.contains("[")) {
            return new ArrayDescriptor(descriptor);
        } else if (descriptor.contains("/")) {
            return new ReferenceDescriptor(descriptor);
        } else {
            return null;
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

