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
    private List<TypeDescriptor> paramTypes;
    private TypeDescriptor returnType;
    private String descriptor;
    private String name;

    private static final Pattern TYPE_DESCRIPTOR_PATTERN = Pattern.compile("\\[*([BCDFIJSZV]|L[^;]+;)");

    public MethodDescriptor(String descriptor) {
        this.descriptor = descriptor;
        List<TypeDescriptor> types = getTypeDescriptors(descriptor);
        this.paramTypes = types.subList(0, types.size() - 1);
        this.returnType = types.get(types.size() - 1);
        this.name = String.format("%s (%s)", returnType.getName(), getFullParamName());
    }

    private List<TypeDescriptor> getTypeDescriptors(String descriptor) {
        Matcher matcher = TYPE_DESCRIPTOR_PATTERN.matcher(descriptor);
        List<TypeDescriptor> types = new ArrayList<>();
        while (matcher.find()) {
            types.add(TypeDescriptorUtil.getTypeDescriptor(matcher.group()));

        }
        return types;
    }

    private String getFullParamName() {
        return paramTypes.stream()
                .map(TypeDescriptor::getName)
                .collect(Collectors.joining(", "));
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

