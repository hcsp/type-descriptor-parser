package com.github.hcsp.descriptorparser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    private static final Pattern methodPattern = Pattern.compile("\\((.*?)\\)(.+)");
    private static final Pattern descriptorPattern = Pattern.compile("(\\[*(?:[BCDFIJSZV]|L[\\w/]+;))");

    public MethodDescriptor(String descriptor) {
        this.descriptor = descriptor;
        this.parseDescriptor(descriptor);
    }

    public static MethodDescriptor of(String descriptor) {
        return new MethodDescriptor(descriptor);
    }

    public static boolean isMethod(String descriptor) {
        return methodPattern.matcher(descriptor).find();
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

    private void parseDescriptor(String descriptor) {
        Matcher matcher = methodPattern.matcher(descriptor);
        while (matcher.find()) {
            this.parseParameters(matcher.group(1));
            this.parseReturnValue(matcher.group(2));
        }

        StringBuilder sb = new StringBuilder();
        sb.append(this.returnType.getName());
        sb.append(" (");
        for (int i = 0; i < this.paramTypes.size(); i++) {
            TypeDescriptor typeDescriptor = this.paramTypes.get(i);
            sb.append(typeDescriptor.getName());
            if (i != this.paramTypes.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append(")");
        this.name = sb.toString();
    }

    private void parseParameters(String descriptor) {
        Matcher matcher = descriptorPattern.matcher(descriptor);
        while (matcher.find()) {
            String result = matcher.group(1);
            if (PrimitiveTypeDescriptor.isPrimitive(result)) {
                paramTypes.add(PrimitiveTypeDescriptor.of(result));
            } else if (ReferenceDescriptor.isReference(result)) {
                paramTypes.add(ReferenceDescriptor.of(result));
            } else if (ArrayDescriptor.isArray(result)) {
                paramTypes.add(ArrayDescriptor.of(result));
            }
        }
    }

    private void parseReturnValue(String descriptor) {
        Matcher matcher = descriptorPattern.matcher(descriptor);
        if (matcher.find()) {
            String result = matcher.group(1);
            if (PrimitiveTypeDescriptor.isPrimitive(result)) {
                this.returnType = PrimitiveTypeDescriptor.of(result);
            } else if (ReferenceDescriptor.isReference(result)) {
                this.returnType = ReferenceDescriptor.of(result);
            } else if (ArrayDescriptor.isArray(result)) {
                this.returnType = ArrayDescriptor.of(result);
            }
        }
    }
}

