package com.github.hcsp.descriptorparser;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
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

        LinkedList<Character> linkedList = new LinkedList();
        String paramsDesc = descriptor.substring(descriptor.indexOf("(") + 1, descriptor.lastIndexOf(")"));
        paramsDesc.chars().mapToObj(i -> (char) i).forEach(x -> {
                linkedList.add(x);
                if(PrimitiveTypeDescriptor.isPrimitive(x.toString()) || x.toString().equals(";")) {
                    String desc = (String) Stream.of(linkedList.toArray()).reduce("", (a, b) -> a.toString() + b.toString());
                    paramTypes.add(checkTypeDescriptor(desc));
                    linkedList.clear();
                }
        });

        String returnDesc = descriptor.substring(descriptor.lastIndexOf(")") + 1);
        this.returnType = checkTypeDescriptor(returnDesc);

        this.name = this.createName();
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

    static TypeDescriptor checkTypeDescriptor(String desc) {
        if(PrimitiveTypeDescriptor.isPrimitive(desc)) {
            return PrimitiveTypeDescriptor.of(desc);
        } else if(desc.startsWith("[")) {
            return new ArrayDescriptor(desc);
        } else {
            return new ReferenceDescriptor(desc);
        }
    }

    public String createName() {
        StringBuilder sb = new StringBuilder(returnType.getName() + " (");
        for(int i = 0; i < paramTypes.size(); i++) {
            String paramName = paramTypes.get(i).getName();
            if(i < paramTypes.size() - 1) {
                sb.append(paramName.toLowerCase()).append(", ");
            } else {
                sb.append(paramName);
            }
        }
        sb.append(")");
        return sb.toString();
    }

    public static void main(String[] args) {
        new MethodDescriptor("(IDLjava/lang/Thread;[[java/lang/Thread;)Ljava/lang/Object;");
    }
}

