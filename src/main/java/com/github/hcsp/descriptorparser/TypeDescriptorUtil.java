package com.github.hcsp.descriptorparser;

class TypeDescriptorUtil {
    static TypeDescriptor getTypeDescriptor(String descriptor) {
        if (PrimitiveTypeDescriptor.isPrimitive(descriptor)) {
            return PrimitiveTypeDescriptor.of(descriptor);
        }

        if (ReferenceDescriptor.isReference(descriptor)) {
            return new ReferenceDescriptor(descriptor);
        }

        return new ArrayDescriptor(descriptor);

    }
}
