package com.github.hcsp.descriptorparser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
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
        String[] split = descriptor.split("\\)");
        String argsString = split[0].substring(1);
        String returnString = split[1];
        resolveArgs(argsString);
        resolveReturn(returnString);
        String paramString = this.paramTypes.stream().map(TypeDescriptor::getName)
                .collect(Collectors.joining(", "));
        this.name = this.returnType.getName() + " (" + paramString + ")";
    }

    private void resolveReturn(String returnString) {
        if (PrimitiveTypeDescriptor.isPrimitive(returnString)) {
            this.returnType = PrimitiveTypeDescriptor.of(returnString);
        } else {
            this.returnType = new ReferenceDescriptor(returnString);
        }
    }

    // I[D[[Ljava/lang/Object;
    private void resolveArgs(String argsString) {
        if (argsString.length() == 0) {
            return;
        }
        String first = argsString.substring(0, 1);
        if (PrimitiveTypeDescriptor.isPrimitive(first)) {
            this.paramTypes.add(PrimitiveTypeDescriptor.of(first));
            resolveArgs(argsString.substring(1));
        }
        if (first.equals("L")) {
            String substring = argsString.substring(0, argsString.indexOf(";") + 1);
            ReferenceDescriptor referenceDescriptor = new ReferenceDescriptor(substring);
            this.paramTypes.add(referenceDescriptor);
            resolveArgs(argsString.substring(argsString.indexOf(";") + 1));
        }
        if (first.equals("[")) {
            String[] split = argsString.split("");
            String key = null;
            int index = 0;
            for (int i = 0; i < split.length; i++) {
                if (!split[i].equals("[")) {
                    index = i + 1;
                    key = split[i];
                    break;
                }
            }
            assert key != null;
            if (key.equals("L")) {
                index = argsString.indexOf(";") + 1;
            }
            ArrayDescriptor arrayDescriptor = new ArrayDescriptor(argsString.substring(0, index));
            this.paramTypes.add(arrayDescriptor);
            resolveArgs(argsString.substring(index));
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

