package com.github.hcsp.descriptorparser;

/**
 * 代表引用类型的描述符号
 */
public class ReferenceDescriptor implements TypeDescriptor {
    /**
     * Ljava/lang/Object;
     * 全限定类名，如java.lang.Object
     */
    private String fqcn;
    private String descriptor;

    public ReferenceDescriptor(String descriptor) {
        this.descriptor = descriptor;
        this.fqcn = descriptor.replace("L", "")
                .replace(";", "")
                .replace("/", ".");
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
