package com.github.hcsp.descriptorparser;

/**
 * 代表引用类型的描述符号
 */
public class ReferenceDescriptor implements TypeDescriptor {
    /**
     * 全限定类名，如java.lang.Object
     */
    private String fqcn;
    private String descriptor;

    //Ljava/lang/Thread;
    public ReferenceDescriptor(String descriptor) {
        this.descriptor = "L";
        this.fqcn = descriptor.replaceAll("/",".")
                .substring(1, descriptor.length() - 1);
    }

    @Override
    public String getName() {
        return fqcn;
    }

    @Override
    public String getDescriptor() {
        return descriptor;
    }
}
