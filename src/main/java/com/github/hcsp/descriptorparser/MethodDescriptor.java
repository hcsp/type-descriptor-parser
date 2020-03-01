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
        this.descriptor = descriptor;
        char[] chars = descriptor.toCharArray();

        int length = descriptor.length();

        for (int i = 0; i < length; i++) {
            char c = descriptor.charAt(i);
            if ('(' == c) {
                continue;
            }
            if ('L' == c) {

                for (int j = i; j < length; j++) {
                    char charAt = descriptor.charAt(j);
                    if (';' == charAt) {
                        String substring = descriptor.substring(i, j);
                        ReferenceDescriptor referenceDescriptor = new ReferenceDescriptor(substring);
                        paramTypes.add(referenceDescriptor);
                        // 下一个
                        i = j;
                        break;
                    }
                }
            } else if ('[' == c) {
                int lastArrayIndex = getLastArrayIndex(descriptor, i);
                if ('L' == descriptor.charAt(lastArrayIndex)) {
                    for (int j = i; j < length; j++) {
                        if (';' == descriptor.charAt(j)) {
                            String substring = descriptor.substring(i, j);
                            ArrayDescriptor arrayDescriptor = new ArrayDescriptor(substring);
                            paramTypes.add(arrayDescriptor);
                            // 下一个
                            i = j;
                        }
                    }
                } else {
                    int chartIndex = lastArrayIndex + 1;
                    String substring = descriptor.substring(i, chartIndex);
                    ArrayDescriptor arrayDescriptor = new ArrayDescriptor(substring);
                    paramTypes.add(arrayDescriptor);
                    // 下一个
                    i = lastArrayIndex;
                }

            } else if (')' != c) {
                PrimitiveTypeDescriptor of = PrimitiveTypeDescriptor.of("" + c);
                paramTypes.add(of);
            } else {
                int startIndex = i + 1;
                char returnType = descriptor.charAt(startIndex);
                if ('L' == returnType) {
                    String substring = descriptor.substring(startIndex, descriptor.length());
                    ReferenceDescriptor referenceDescriptor = new ReferenceDescriptor(substring);
                    this.returnType = referenceDescriptor;
                } else {
                    this.returnType = PrimitiveTypeDescriptor.of("" + returnType);
                }
                break;
            }
        }
        StringBuffer sb = new StringBuffer(this.returnType.getName());
        sb.append(" (");
        for (int i = 0; i < this.paramTypes.size(); i++) {

            sb.append(this.paramTypes.get(i).getName());
            if (i + 1 < this.paramTypes.size()) {
                sb.append(", ");
            }
        }
        sb.append(")");
        this.name = sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(new MethodDescriptor("()I").getName());
    }

    /**
     * 获取 最后 非 '[' 索引
     * @param descriptor des
     * @param index 索引
     * @return
     */
    private int getLastArrayIndex(String descriptor, int index) {
        int nextIndex = index + 1;
        if (descriptor.charAt(nextIndex) == '[') {
            return getLastArrayIndex(descriptor, nextIndex);
        }
        return nextIndex;
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

