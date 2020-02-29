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

    public MethodDescriptor(String descriptor) {
        List<String> list = new ArrayList<>();
        String name = null;
        if (descriptor.startsWith("(")) {
            descriptor = descriptor.substring(1);
            while (!descriptor.equals("") && !descriptor.startsWith(")")) {
                if (descriptor.startsWith("L")) {
                    descriptor = descriptor.substring(1);
                    list.add((descriptor.substring(0, descriptor.indexOf(";")).replace("/", ".")));
                    descriptor = descriptor.substring(descriptor.indexOf(";") + 1, descriptor.length() - 1);
                } else if (PrimitiveTypeDescriptor.isPrimitive(descriptor.substring(0, 1))) {
                    if (descriptor.startsWith("V"))
                        name = PrimitiveTypeDescriptor.of(descriptor.substring(0, 1)).getName();
                    else {
                        list.add(PrimitiveTypeDescriptor.of(descriptor.substring(0, 1)).getName());
                    }
                    descriptor = descriptor.substring(1);
                }
                if (descriptor.startsWith("[")) {
                    list.add(new ArrayDescriptor(descriptor).getName());
                    while (descriptor.startsWith("[") && !descriptor.startsWith("L")) {
                        descriptor = descriptor.substring(1);
                    }
                    if (descriptor.startsWith("L")) {
                        descriptor = descriptor.substring(descriptor.indexOf(";") + 1);
                    }
                    descriptor = descriptor.substring(1);
                }
            }
            if (!descriptor.equals(""))
                descriptor = descriptor.substring(1);
        }
        if (!descriptor.equals(""))
            name = new ArrayDescriptor(descriptor).getName();
        this.name = name + " (" + String.join(", ", list) + ")";
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

