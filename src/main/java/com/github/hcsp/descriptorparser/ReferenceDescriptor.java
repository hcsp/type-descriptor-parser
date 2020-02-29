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

    public ReferenceDescriptor(String descriptor) {
        StringBuilder rs = new StringBuilder();
        if (descriptor.endsWith(";"))
            descriptor= descriptor.substring(0,descriptor.length()-1);
        if (descriptor.startsWith("L")) {
            descriptor = descriptor.substring(1);
            rs.append(descriptor.replace("/", "."));
        }
        this.fqcn =rs.toString();
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
