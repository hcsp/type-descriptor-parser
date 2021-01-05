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
    private static final String METHOD_REGEX = "^\\((?<paramTypes>.*)\\)(?<returnType>.*)";
    private static final Pattern METHOD_PATTERN = Pattern.compile(METHOD_REGEX);

    public MethodDescriptor(String descriptor) {
        Matcher matcher = METHOD_PATTERN.matcher(descriptor);
        while (matcher.find()) {
            String paramTypes = matcher.group("paramTypes");
            String returnType = matcher.group("returnType");
            this.paramTypes = TypeFly.ofList(paramTypes);
            this.returnType = TypeFly.of(returnType);
            this.descriptor = descriptor;
            this.name = this.returnType.getName() + " (" + this.paramTypes.stream().map(TypeDescriptor::getName).collect(Collectors.joining(", ")) + ")";
        }
    }


    public static void main(String[] args) {
        String descriptor = "(IDLjava/lang/Thread;)Ljava/lang/Object;";
        MethodDescriptor methodDescriptor = new MethodDescriptor(descriptor);
        System.out.println(methodDescriptor.getName());
        System.out.println();
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

