package com.github.hcsp.descriptorparser;

import java.util.ArrayList;
import java.util.List;

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
        descriptor = descriptor.replace("(", "");
        final String[] descriptorToTwoPart = descriptor.split("\\)");
        String param = descriptorToTwoPart[0];
        String returns = descriptorToTwoPart[1];
        List<String> params = new ArrayList<>();
        for (int i = 0; i < param.length(); ) {
            String handlingString = "";
            char handlingChar;
            do {
                handlingChar = param.charAt(i++);
                if (handlingChar == 'L') {
                    int indexOfSeparator = param.substring(i - 1).indexOf(";");
                    handlingString += param.substring(i - 1, indexOfSeparator + i - 1);
                    i += indexOfSeparator;
                    break;
                }
                handlingString += handlingChar;
            } while (handlingChar == '[');
            if (handlingString.startsWith("[")) {
                params.add(new ArrayDescriptor(handlingString).getName());
                continue;
            }
            params.add(DescriptorUtil.getClassName(handlingString));

        }
        returns = DescriptorUtil.getClassName(returns);
        name = returns + " " + params.toString().replaceFirst("\\[", "\\(").substring(0, params.toString().length() - 1) + ")";
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

