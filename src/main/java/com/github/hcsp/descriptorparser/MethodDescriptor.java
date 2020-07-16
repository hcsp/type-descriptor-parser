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
        Pattern splitPattern = Pattern.compile("\\((.*?)\\)(.*)");
        Matcher matcher = splitPattern.matcher(descriptor);
        if (matcher.find()) {
            String params = matcher.group(1);
            String returnStr = matcher.group(2);
            // 返回值
            setReturnTypeFromRegexp(returnStr);
            // 参数
            setParamFromRegexp(params);
        }
        String paramsStr = paramTypes.stream().map(TypeDescriptor::getName).collect(Collectors.joining(", "));
        this.name = returnType.getName() + " (" + paramsStr + ")";
    }

    private void setParamFromRegexp(String params) {
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
    }

    private void setReturnTypeFromRegexp(String returnStr) {
        if (returnStr.startsWith("L")) {
            this.returnType = new ReferenceDescriptor(returnStr);
        } else {
            this.returnType = PrimitiveTypeDescriptor.of(returnStr);
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

