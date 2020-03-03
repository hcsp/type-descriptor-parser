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
        this.descriptor = descriptor;
        //第一次出现位置
        int firstShowIndex = descriptor.indexOf("[");
        //最后一次出现位置
        int lastShowIndesx = descriptor.lastIndexOf("[");
        if (firstShowIndex == lastShowIndesx) {
            this.dimension = 1;
        } else {
            this.dimension = (lastShowIndesx - firstShowIndex) + 1;
        }

        //拼接
        StringBuffer stringBuffer = new StringBuffer();

        String temp = descriptor.replace("[", "");

        this.rawType = TypeDescriptor.parse(temp);
        stringBuffer.append(rawType.getName());

        for (int i = 0; i < dimension; i++) {
            stringBuffer.append("[]");
        }
        this.name = stringBuffer.toString();
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
