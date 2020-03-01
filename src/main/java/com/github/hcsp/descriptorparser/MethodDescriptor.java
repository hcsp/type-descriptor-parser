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
        if (!descriptor.startsWith("(")) {
            throw new IllegalArgumentException("Invalid Descriptor");
        }
        int index = 1;
        int referenceTypeIndex = 0;
        int arrayTypeIndex = 0;
        String curChar = String.valueOf(descriptor.charAt(index));
        String methodStr = "(";
        while (!")".equals(curChar)) {
            if ("[".equals(curChar)) {
                arrayTypeIndex--;
                index++;
                curChar = String.valueOf(descriptor.charAt(index));
                continue;
            }
            TypeDescriptor curType = null;
            if ("L".equals(curChar)) {
                int endIndex = descriptor.indexOf(";", referenceTypeIndex);
                if (arrayTypeIndex < 0) {
                    curType = new ArrayDescriptor(descriptor.substring(index + arrayTypeIndex, endIndex));
                    arrayTypeIndex = 0;
                } else {
                    curType = new ReferenceDescriptor(descriptor.substring(index, endIndex));
                }
                index = endIndex + 1;
                referenceTypeIndex++;
            } else {
                if (arrayTypeIndex < 0) {
                    curType = new ArrayDescriptor(descriptor.substring(index + arrayTypeIndex, index + 1));
                    arrayTypeIndex = 0;
                } else {
                    curType = PrimitiveTypeDescriptor.of(curChar);
                }
                index += 1;
            }
            if (methodStr.length() > 1) methodStr = methodStr.concat(", ");
            methodStr = methodStr.concat(curType.getName());
            paramTypes.add(curType);
            curChar = String.valueOf(descriptor.charAt(index));
        }
        methodStr = methodStr.concat(")");

        if ("L".equals(String.valueOf(descriptor.charAt(index + 1)))) {
            returnType = new ReferenceDescriptor(descriptor.substring(index + 2));
            name = returnType.getName().concat(" ").concat(methodStr);
        } else {
            returnType = PrimitiveTypeDescriptor.of(descriptor.substring(index + 1));
            name = returnType.getName().concat(" ").concat(methodStr);
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

