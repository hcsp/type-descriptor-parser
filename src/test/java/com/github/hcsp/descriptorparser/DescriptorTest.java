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
    }
}
