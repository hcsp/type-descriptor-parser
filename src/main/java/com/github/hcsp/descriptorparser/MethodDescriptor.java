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

    String methodRegex = "\\((.*)\\)(.*)";
    String paramRegex = "(\\[*[BICDFJSZV]|\\[*L.*)";

    public MethodDescriptor(String descriptor) {
        this.descriptor = descriptor;
        Matcher matcher = Pattern.compile(methodRegex).matcher(descriptor);
        if (matcher.find()) {
            String paramDescriptor = matcher.group(1);
            setParam(paramDescriptor);

            String returnDescriptor = matcher.group(2);
            seReturn(returnDescriptor);
        }
        String param = paramTypes.stream().map(TypeDescriptor::getName).collect(Collectors.joining(", "));
        this.name = returnType.getName() + " (" + param + ")";

    }

    private void setParam(String param) {
        Matcher matcher = Pattern.compile(paramRegex).matcher(param);
        while (matcher.find()) {
            String s = matcher.group();
            if (s.startsWith("L")) {
                this.paramTypes.add(new ReferenceDescriptor(s));
            } else if (s.startsWith("[")) {
                this.paramTypes.add(new ArrayDescriptor(s));
            } else {
                this.paramTypes.add(PrimitiveTypeDescriptor.of(s));
            }
        }
    }

    private void seReturn(String returnType) {
        if (returnType.startsWith("L")) {
            this.returnType = new ReferenceDescriptor(returnType);
        } else {
            this.returnType = PrimitiveTypeDescriptor.of(returnType);
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

