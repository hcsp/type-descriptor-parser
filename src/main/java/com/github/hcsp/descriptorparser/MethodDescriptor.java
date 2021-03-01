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
    private final Pattern pattern1 = Pattern.compile("\\((.*?)\\)(.*)");
    private final Pattern pattern2 = Pattern.compile("(\\[*[BCDFIJSZV]|\\[*L.*)");

    public MethodDescriptor(String descriptor) {
        this.descriptor = descriptor;
        Matcher matcher = pattern1.matcher(descriptor);
        if (matcher.find()) {
            String param = matcher.group(1);
            setParamTypes(param);

            String returnValue = matcher.group(2);
            setReturnType(returnValue);
        }
        String params = paramTypes.stream().map(TypeDescriptor::getName).collect(Collectors.joining(", "));
        name = returnType.getName() + " (" + params + ")";
    }

    private void setParamTypes(String param) {
        Matcher matcher = pattern2.matcher(param);
        while (matcher.find()) {
            String p = matcher.group();
            if (p.startsWith("[")) {
                paramTypes.add(new ArrayDescriptor(p));
            } else if (p.startsWith("L")) {
                paramTypes.add(new ReferenceDescriptor(p));
            } else {
                paramTypes.add(PrimitiveTypeDescriptor.of(p));
            }
        }


    }

    private void setReturnType(String returnValue) {
        if (returnValue.startsWith("L")) {
            returnType = new ReferenceDescriptor(returnValue);
        } else {
            returnType = PrimitiveTypeDescriptor.of(returnValue);
        }
    }

    public static void main(String[] args) {
        MethodDescriptor s = new MethodDescriptor("(IDLjava/lang/Thread;)Ljava/lang/Object;");
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

