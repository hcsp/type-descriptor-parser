package com.github.hcsp.descriptorparser;

import java.util.regex.Pattern;

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

    //方法类型正则表达式
    public static String methodRegex = "^\\(.*";
    //方法类型正则表达式
    public static String referenceRegex = "^L.*";
    //方法类型正则表达式
    public static String arrayRegex = "^\\[.*";

    /**
     * 解析一个描述符，根据其具体类型返回不同的子类
     *
     * @param descriptor
     * @return PrimitiveTypeDescriptor/ReferenceDescriptor/MethodDescriptor/PrimitiveTypeDescriptor
     */
    static TypeDescriptor parse(String descriptor) {

        /*
            1.原生类型   B	byte
            2.方法类型  (IDLjava/lang/Thread;)Ljava/lang/Object;
            3.数组类型   多维数组每增加一个维度，前面增加一个[。
            4.引用类型   java.lang.String表示为Ljava/lang/String;
         */

        if (Pattern.matches(methodRegex, descriptor)) {
            //方法类型
            return new MethodDescriptor(descriptor);

        } else if (Pattern.matches(referenceRegex, descriptor)) {
            //引用类型
            return new ReferenceDescriptor(descriptor);

        } else if (Pattern.matches(arrayRegex, descriptor)) {
            //数组类型
            return new ArrayDescriptor(descriptor);

        } else if (PrimitiveTypeDescriptor.isPrimitive(descriptor)) {
            //原生类型
            return PrimitiveTypeDescriptor.of(descriptor);

        } else {
            return null;
        }
    }
}







