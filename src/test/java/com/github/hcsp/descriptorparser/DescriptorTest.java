package com.github.hcsp.descriptorparser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DescriptorTest {

    @Test
    public void primitiveDescriptorTest() {
        for (PrimitiveTypeDescriptor descriptor : PrimitiveTypeDescriptor.values()) {
            Assertions.assertEquals(PrimitiveTypeDescriptor.of(descriptor.getDescriptor()), descriptor);
            Assertions.assertTrue(PrimitiveTypeDescriptor.isPrimitive(descriptor.getDescriptor()));
            Assertions.assertFalse(PrimitiveTypeDescriptor.isPrimitive(descriptor.getDescriptor() + "XXX"));
        }
    }

    @Test
    public void arrayDescriptorTest() {
        Assertions.assertEquals("java.lang.Object[]", new ArrayDescriptor("[Ljava/lang/Object;").getName());
        Assertions.assertEquals("int[][][]", new ArrayDescriptor("[[[I").getName());
    }

    @Test
    public void referenceDescriptorTest() {
        Assertions.assertEquals("java.lang.Thread", new ReferenceDescriptor("Ljava/lang/Thread;").getName());
        Assertions.assertEquals("com.github.hcsp.MyClass$1", new ReferenceDescriptor("Lcom/github/hcsp/MyClass$1;").getName());
    }

    @Test
    public void methodDescriptorTest() {
        Assertions.assertEquals(
                "java.lang.Object (int, double, java.lang.Thread)",
                new MethodDescriptor("(IDLjava/lang/Thread;)Ljava/lang/Object;").getName()
        );

        Assertions.assertEquals(
                "void (int, double[], java.lang.Object[][])",
                new MethodDescriptor("(I[D[[Ljava/lang/Object;)V").getName()
        );

        Assertions.assertEquals(
                "int ()",
                new MethodDescriptor("()I").getName()
        );

        Assertions.assertEquals(
                /**
                 * 之前的测试在参数中，java.lang.Thread都是放在最后一位，
                 * 这样会有漏网之鱼，
                 * 如果把下面这个例子输错了也可以通过测试
                 * 我是通过substring字符串来做的，如果把java.lang.Thread之后的字符串读取错误也可以通过之前的测试
                 * */
                "void (double[], java.lang.Thread, int[], byte[][], float)",
                new MethodDescriptor("([DLjava/lang/Thread[I[[BF;)V").getName()
        );
    }
}
