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
        this.descriptor = descriptor;
    }


    public List<TypeDescriptor> getParamTypes() {
        Pattern pattern = Pattern.compile("\\((.*?)\\)(.*)");
        Matcher matcher = pattern.matcher(descriptor);
        String params = null;
        if (matcher.find()) {
            params = matcher.group(1);
        }

        Pattern paramPattern = Pattern.compile("(\\[*[BCDFIJSZV]|\\[*L.*)");
        Matcher paramMatcher = paramPattern.matcher(params);
        while (paramMatcher.find()) {
            String p = paramMatcher.group();
            if (p.startsWith("[")) {
                paramTypes.add(new ArrayDescriptor(p));
            } else if (p.startsWith("L")) {
                paramTypes.add(new ReferenceDescriptor(p));
            } else {
                paramTypes.add(PrimitiveTypeDescriptor.of(p));
            }
        }

        return paramTypes;
    }

    public TypeDescriptor getReturnType() {
        Pattern pattern = Pattern.compile("\\(((\\[?)[A-Z](.*))?\\)(([A-Z])(.*)?)");
        Matcher matcher = pattern.matcher(descriptor);
        if (matcher.find()) {
            String group4 = matcher.group(4);
            String group5 = matcher.group(5);
            if (group5.equals("L")) {
                returnType = new ReferenceDescriptor(group4);
            } else {
                returnType = PrimitiveTypeDescriptor.of(group4);
            }
        }
        return returnType;
    }

    @Override
    public String getName() {
        List<TypeDescriptor> paramTypes = getParamTypes();
        TypeDescriptor returnType = getReturnType();
        String typeName = returnType.getName();

        String paramsStr = paramTypes.stream().map(TypeDescriptor::getName).collect(Collectors.joining(", "));
        name = typeName + " (" + paramsStr + ")";

        return name;
    }

    @Override
    public String getDescriptor() {
        return descriptor;
    }
}

