package com.github.hcsp.descriptorparser;

/**
 * 数组类型的描述符，如输入[[Ljava/lang/Object;
 * 得到的name是java.lang.Object[][]
 * dimension为2（二维数组）
 * rawType代表无数组的原始类型
 */
public class ArrayDescriptor implements TypeDescriptor {
    // [ -> 1
    // [[ -> 2
    private String name;
    private String descriptor;
    private int dimension;
    private TypeDescriptor rawType;

    // [[Ljava/lang/Object;
    public ArrayDescriptor(String descriptor) {
        String tempName = "";
        for (int i = 0; i < descriptor.length(); i++) {
            char curChar = descriptor.charAt(i);
            if ("[".equals(String.valueOf(curChar))) {
                tempName = tempName.concat("[]");
                continue;
            }
            if ("L".equals(String.valueOf(curChar))) {
                tempName = new ReferenceDescriptor(descriptor.substring(i)).getName().concat(tempName);
                break;
            }
            tempName = PrimitiveTypeDescriptor.of(descriptor.substring(i)).getName().concat(tempName);
            break;
        }
        this.name = tempName;
    }

    @Override
    public String getName() {
        return name;
    }

    public int getDimension() {
        return dimension;
    }

    public TypeDescriptor getRawType() {
        return rawType;
    }

    @Override
    public String getDescriptor() {
        return descriptor;
    }
}
