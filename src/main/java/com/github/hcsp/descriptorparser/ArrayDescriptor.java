package com.github.hcsp.descriptorparser;

import java.util.HashMap;

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

    /**
     * 思路
     * 1. 解析出几维数组
     * 2. 解析出数组之后的数据类型
     * 3. 将数据类型转换格式
     * 4. 拼接
     * @param descriptor
     */
    public ArrayDescriptor(String descriptor) {
        this.descriptor = descriptor;
        String fullName = null;
        StringBuilder builder = new StringBuilder();
        //获取数组维度
        dimension = descriptor.lastIndexOf('[') + 1;
        //获取数据类型
        String type = String.valueOf(descriptor.charAt(dimension));
        //转换为完全限定名
        HashMap fqcn = DescriptorUtils.getFqcnByDataType(0, type, descriptor);

        builder.append(fqcn.get("fqcn"));
        for (int x = 0; x < dimension; x++){
            builder.append("[]");
        }

        this.name = builder.toString();
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
