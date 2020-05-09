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
        int dimension = TypeDescriptor.countRepeat(descriptor, "[");
        String typeFlag = descriptor.replace("[", "").substring(0, 1);
        if (typeFlag.equals("L")) {
            fqcn = descriptor
                    .replace("[", "")
                    .replace("L", "")
                    .replace(";", "")
                    .replaceAll("/", ".");
        } else {
            fqcn = TypeDescriptor.getFullType(typeFlag);
        }
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
