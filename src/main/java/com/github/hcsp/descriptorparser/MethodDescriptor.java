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

    private static final Pattern methodPattern = Pattern.compile("(\\(.*\\))([BCDFIJSZV]|L[\\w\\/\\$]*;)");
    private static final Pattern paraPattern = Pattern.compile("(\\[{1,})?([BCDFIJSZV]|L[\\w\\/\\$]*;)");

    public MethodDescriptor(String descriptor) {
        Matcher methodMatcher = methodPattern.matcher(descriptor);
        if (methodMatcher.find()) {
            String parameterInfo = methodMatcher.group(1);
            buildPara(parameterInfo);

            String returnInfo = methodMatcher.group(2);
            buildReturnType(returnInfo);

            buildName();
        }
    }

    private void buildName() {
        StringBuilder builder = new StringBuilder(returnType.getName() + " ");
        builder.append("(");
        String collect = paramTypes.stream().map(TypeDescriptor::getName)
                .collect(Collectors.joining(", "));
        builder.append(collect);
        builder.append(")");
        this.name = builder.toString();
    }

    private void buildReturnType(String returnInfo) {
        if (PrimitiveTypeDescriptor.isPrimitive(returnInfo)) {
            this.returnType = PrimitiveTypeDescriptor.of(returnInfo);
        } else {
            this.returnType = new ReferenceDescriptor(returnInfo);
        }
    }

    private void buildPara(String parameterInfo) {
        Matcher matcher = paraPattern.matcher(parameterInfo);
        //这里需要多次匹配
        while (matcher.find()) {
            if (matcher.group(1) == null) {
                String type = matcher.group(2);
                if (PrimitiveTypeDescriptor.isPrimitive(type)) {
                    paramTypes.add(PrimitiveTypeDescriptor.of(type));
                } else {
                    paramTypes.add(new ReferenceDescriptor(type));
                }
            } else {
                paramTypes.add(new ArrayDescriptor(matcher.group(1) + matcher.group(2)));
            }
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

