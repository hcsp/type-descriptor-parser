package com.github.hcsp.descriptorparser;

public interface TypeDescriptor {
    /**
     * 返回人类可读的类型名，如int或者java.lang.Object[]
     *
     * @return the human-readable name
     */
    String getName();

    /**
     * 返回描述符的原始格式，如[I或者Ljava/lang/Object;
     *
     * @return the raw descriptor
     */
    String getDescriptor();

    /**
     * 解析一个描述符，根据其具体类型返回不同的子类
     * @param descriptor
     * @return PrimitiveTypeDescriptor/ReferenceDescriptor/MethodDescriptor/PrimitiveTypeDescriptor
     */
    static TypeDescriptor parse(String descriptor) {
        return null;
    }
}







