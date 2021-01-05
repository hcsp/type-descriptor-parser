package com.github.hcsp.descriptorparser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        Pattern pattern = Pattern.compile("\\((.*)\\)(.*)");
        Matcher matcher = pattern.matcher(descriptor);
        if (matcher.find()) {
            String[] paramTypesArr = matcher.group(1).split(";");
            Pattern paramTypePattern = Pattern.compile("(L.*|\\[+(?:[BCDFIJSZV]|L.*)|[BCDFIJSZV])");
            Stream.of(paramTypesArr).forEach(paramTypeStr -> {
                Matcher paramMatcher = paramTypePattern.matcher(paramTypeStr);
                while (paramMatcher.find()) {
                    int i = 1;
                    paramTypes.add(TypeDescriptor.parse(paramMatcher.group(i++)));
                }
            });
            returnType = TypeDescriptor.parse(matcher.group(2));
        }
        StringBuilder methodName = new StringBuilder();
        methodName.append(returnType.getName() + " (");
        String paramTypeStr = paramTypes.stream().map(TypeDescriptor::getName).collect(Collectors.joining(", "));
        methodName.append(paramTypeStr + ")");
        name = methodName.toString();
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

