package com.github.hcsp.descriptorparser;

import com.github.hcsp.descriptorparser.util.TypeDescriptorParser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

    public MethodDescriptor(String descriptor) {
        this.descriptor = descriptor;
        addParamDescriptor(descriptor, paramTypes);
        this.returnType = TypeDescriptorParser.getReturnTypeDescriptor(descriptor);
        this.name = getFullParameterName(paramTypes);
    }

    private void addParamDescriptor(String descriptor, List<TypeDescriptor> types) {
        String parameters = TypeDescriptorParser.getParameterListFromMethodDescriptor(descriptor);
        if (parameters.isEmpty())
            return;

        String[] typeDescriptors = parameters.split(";");
        for (String des : typeDescriptors) {
            types.addAll(TypeDescriptorParser.parseParameterDescriptor(des));
        }
    }

    private String getFullParameterName(List<TypeDescriptor> paramTypes) {
        StringBuilder sb = new StringBuilder();
        sb.append(returnType.getName());
        sb.append(" (");

        List<String> names = paramTypes.stream()
                .map(TypeDescriptor::getName)
                .collect(Collectors.toList());
        sb.append(String.join(", ", names));
        sb.append(")");
        return sb.toString();
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

