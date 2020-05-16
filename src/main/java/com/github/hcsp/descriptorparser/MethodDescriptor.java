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

    Pattern methodPattern = Pattern.compile("^\\((((\\[*)(\\w*))*?)(((\\[*)(L.*?;)*)*)\\)(\\[*)((L.*?;)*(\\w)*)");

    public MethodDescriptor(String descriptor) {
        final Matcher matcher = methodPattern.matcher(descriptor);

        if (matcher.find()) {
            paramTypes = primitiveArrayMixedParsing(matcher);

            paramTypes.addAll(referenceArrayMixedParsing(matcher));

            final String paramString = paramTypes.stream()
                    .map(item -> item.getName())
                    .collect(Collectors.joining(", "));
            ArrayDescriptor arrayDescriptor = returnTypeParsing(matcher);

            this.name = arrayDescriptor.getName() + " (" + paramString + ")";
        }
    }

    private ArrayDescriptor returnTypeParsing(Matcher matcher) {
        final String returnArray = matcher.group(9) == null ? "" : matcher.group(9);
        String rawReturnTypeName = matcher.group(10) == null ? "" : matcher.group(10);
        return new ArrayDescriptor(returnArray + rawReturnTypeName);
    }

    private List<TypeDescriptor> referenceArrayMixedParsing(Matcher matcher) {
        final String rawParamTypes2 = matcher.group(5);
        List<TypeDescriptor> result = new ArrayList<>();
        if (rawParamTypes2.length() > 0) {
            final List<TypeDescriptor> referenceArrayDescriptors = Stream.of(rawParamTypes2.split(";"))
                    .map(item -> item + ";")
                    .map(ArrayDescriptor::new)
                    .collect(Collectors.toList());
            result.addAll(referenceArrayDescriptors);
        }
        return result;
    }

    private List<TypeDescriptor> primitiveArrayMixedParsing(Matcher matcher) {
        final String rawParamTypes1 = matcher.group(1);
        List<TypeDescriptor> result = new ArrayList<>();
        if (rawParamTypes1.length() > 0) {
            int j = 0;
            List<String> paramTypeStrings = new ArrayList<>();
            String arrayDesc = "";
            for (int i = 0; i < rawParamTypes1.length(); i++) {
                String paramType = String.valueOf(rawParamTypes1.charAt(i));
                if ("[".equals(paramType) || j != 0) {
                    if (!"[".equals(paramType)) {
                        arrayDesc += paramType;
                        j = 0;
                        paramTypeStrings.add(arrayDesc);
                        arrayDesc = "";
                    } else {
                        arrayDesc += paramType;
                        j = i;
                    }
                } else {
                    j = 0;
                    paramTypeStrings.add(paramType);
                }
            }
            final List<TypeDescriptor> primitiveArrayTypeDescriptors = paramTypeStrings.stream()
                    .map(item -> {
                        if (item.contains("[")) {
                            return new ArrayDescriptor(item);
                        } else {
                            return PrimitiveTypeDescriptor.of(item);
                        }
                    })
                    .collect(Collectors.toList());
            result.addAll(primitiveArrayTypeDescriptors);
        }
        return result;
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

