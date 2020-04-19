package com.github.hcsp.descriptorparser;

import java.util.ArrayList;
import java.util.LinkedList;
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
        this.descriptor = descriptor;
        this.returnType = compileReturnType();
        this.paramTypes = compileParamTypes();
        this.name = compileName();
    }

    private String compileName() {
        return this.returnType.getName() + " (" + String.join(", ", this.paramTypes.stream().map(item -> item.getName()).collect(Collectors.toList())) + ")";
    }


    public List<TypeDescriptor> compileParamTypes() {
        List<TypeDescriptor> paramList = new LinkedList<>();
        Matcher matcher = Pattern.compile("((\\[*([BCDFIJSZV]|(L[\\w\\/]*;))))(?=((\\[*([BCDFIJSZV]|(L[\\w\\/]*;))))*\\))").matcher(this.descriptor);
        while (matcher.find()) {
            String param = matcher.group(0);
            paramList.add(new ArrayDescriptor(param));
        }
        return paramList;
    }

    private TypeDescriptor compileReturnType() {
        Matcher matcher = Pattern.compile("\\)(L.*;)").matcher(this.descriptor);
        boolean isReference = matcher.find();
        if (isReference) {
            return new ReferenceDescriptor(matcher.group(1));
        }
        return PrimitiveTypeDescriptor.of(descriptor.substring(descriptor.length() - 1));
    }


    public List<TypeDescriptor> getParamTypes() {
        return this.paramTypes;
    }

    public TypeDescriptor getReturnType() {
        return this.returnType;
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

